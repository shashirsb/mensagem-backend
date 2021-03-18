package pt.min.saude.spms.hos.base.mensagem.backend.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.api.deser.ExceptionMessage;
import com.lightbend.lagom.javadsl.api.transport.TransportException;
import io.vavr.Function1;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Value;
import org.elasticsearch.search.sort.SortOrder;
import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemBackendSelfServices;
import pt.min.saude.spms.hos.base.mensagem.backend.api.request.*;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.PesquisarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.RegistarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemId;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemTipo;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MsgIdError;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemCassandraReadSide;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemElasticReadSide;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.*;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.MensagemWriteSide;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.CreateMensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.GetMensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.UpdateMensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.exception.ConflitoNoEstadoException;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.exception.InexistenciaEntidadeException;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.ValorDominio;
import pt.min.saude.spms.hos.elastic.utils.backend.ScrollIdException;
import pt.min.saude.spms.hos.service.utils.backend.Message;
import pt.min.saude.spms.hos.service.utils.backend.exceptions.S3TransportException;


import javax.inject.Inject;
import java.util.Optional;
import java.util.StringJoiner;


import static com.google.common.base.Throwables.getRootCause;
import static io.vavr.API.*;
import static io.vavr.Predicates.*;
import static pt.min.saude.spms.hos.base.mensagem.backend.api.request.CriarMensagemRequest.Valid.validate;
import static pt.min.saude.spms.hos.base.mensagem.backend.impl.MensagemSystemErrors.MENSAGEM_EXISTENTE;
import static pt.min.saude.spms.hos.base.mensagem.backend.impl.MensagemSystemMessages.*;
import static pt.min.saude.spms.hos.common.classes.backend.LogBuilder.Type.ERROR;
import static pt.min.saude.spms.hos.common.classes.backend.LogBuilder.Type.INFO;
import static pt.min.saude.spms.hos.service.utils.backend.ServiceErrorBuilder.*;

public class MensagemBackendSelfServicesImpl implements MensagemBackendSelfServices {

    private final LogBuilder log = LogBuilder.getLogger(MensagemBackendSelfServicesImpl.class);

    private final MensagemWriteSide writeSide;
    private final MensagemElasticReadSide elasticReadSide;
    private final MensagemCassandraReadSide cassandraReadSide;

    @Inject
    public MensagemBackendSelfServicesImpl(MensagemWriteSide writeSide,
                                           MensagemElasticReadSide elasticReadSide,
                                           MensagemCassandraReadSide cassandraReadSide) {
        this.writeSide = writeSide;
        this.elasticReadSide = elasticReadSide;
        this.cassandraReadSide = cassandraReadSide;
    }


    @Override
    public Future<Done> criarMensagem(Principal principal, CriarMensagemRequest criarMensagemRequest) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemServiceImpl.criarMensagem( %s )",
                criarMensagemRequest
        ));


        CriarMensagemRequest.Valid request = validate(criarMensagemRequest)
                .getOrElseThrow(failure -> BadRequest(principal));

        String idiomaLowerCase = request.getId().getIdioma().toLowerCase();

        CreateMensagem cmd = new CreateMensagem(
                new MensagemStateAtributos(
                        MensagemStateTipo.valueOf(request.getAtributos().getTipo().name()),
                        request.getAtributos().getConteudo(),
                        request.getAtributos().getRecurso(),
                        request.getAtributos().getAtivo()
                ),
                principal
        );

        return writeSide
                .askTo(
                        new MensagemStateId(request.getId().getChave(), idiomaLowerCase),
                        cmd
                ).recover(exception -> recoverFromError(context.get(), exception, principal, ERR_UNABLE_CREATE_MESSAGE));
    }

    @Override
    public Future<RegistarMensagensResponse> registarMensagens(Principal principal, RegistrarMensagensRequest registrarMensagensRequest) {
        Function1<Option<String>, Lazy<String>> context = detail -> Lazy.of(() -> String.format(
                "MensagemServiceImpl.registarMensagens(%s)",
                detail.map(id -> "detail: " + id).getOrElse(registrarMensagensRequest.toString())
        ));

        abstract class SubResult {
        }

        @Value
        class Registada extends SubResult {
            MensagemId id;
        }

        @Value
        class JaExistente extends SubResult {
            MensagemId id;
        }

        @Value
        class NaoRegistada extends SubResult {
            MensagemId id;
            S3TransportException error;
        }


        RegistrarMensagensRequest.Valid registarRequest = RegistrarMensagensRequest.Valid.validate(registrarMensagensRequest)
                .getOrElseThrow(failure -> BadRequest(principal));

        return Future
                .sequence(
                        registarRequest.getMensagens().map(criarRequest ->
                                writeSide
                                        .askTo(
                                                new MensagemStateId(
                                                        criarRequest.getId().getChave(),
                                                        criarRequest.getId().getIdioma().toLowerCase()
                                                ),
                                                new CreateMensagem(
                                                        new MensagemStateAtributos(
                                                                MensagemStateTipo.valueOf(criarRequest.getAtributos().getTipo().name()),
                                                                criarRequest.getAtributos().getConteudo(),
                                                                criarRequest.getAtributos().getRecurso(),
                                                                criarRequest.getAtributos().getAtivo()
                                                        ),
                                                        principal
                                                )
                                        )
                                        .onFailure(failure -> {

                                            if (failure instanceof ConflitoNoEstadoException || failure.getCause() instanceof ConflitoNoEstadoException) {

                                                ConflitoNoEstadoException cee =  failure instanceof ConflitoNoEstadoException ?
                                                        (ConflitoNoEstadoException) failure :
                                                        (ConflitoNoEstadoException) failure.getCause();

                                                if (cee.getMessage().equalsIgnoreCase(MENSAGEM_EXISTENTE)){
                                                    log.of(INFO, String.format("Mensagem existente: '%s' no idioma '%s'",
                                                            criarRequest.getId().getChave(),
                                                            criarRequest.getId().getIdioma().toLowerCase()));
                                                } else {
                                                    log.of(ERROR, context.apply(Option.of(criarRequest.getId().getChave())), Option.of(principal.toLogPrincipal()), failure);
                                                }
                                            } else {
                                                log.of(ERROR, context.apply(Option.of(criarRequest.getId().getChave())), Option.of(principal.toLogPrincipal()), failure);
                                            }
                                        })
                                        .map(Try::success)
                                        .recover(Try::failure)
                                        .map(result -> {
                                            if (result.isSuccess()) {
                                                return new Registada(criarRequest.getId());
                                            } else if (result.isFailure() && !result.getCause().getMessage().equalsIgnoreCase(MENSAGEM_EXISTENTE)) {
                                                return new JaExistente(criarRequest.getId());
                                            } else {
                                                return new NaoRegistada(criarRequest.getId(), InternalServerError(principal));
                                            }
                                        })
                        )
                )
                .map(subResults ->
                        new RegistarMensagensResponse(
                                subResults
                                        .filter(s -> s instanceof Registada)
                                        .map(subResult -> ((Registada) subResult).getId())
                                        .toList(),
                                subResults
                                        .filter(s -> s instanceof JaExistente)
                                        .map(subResult -> ((JaExistente) subResult).getId())
                                        .toList(),
                                subResults
                                        .filter(s -> s instanceof NaoRegistada)
                                        .map(subResult -> new MsgIdError(
                                                ((NaoRegistada) subResult).getId(),
                                                TransportException.fromCodeAndMessage(
                                                        ((NaoRegistada) subResult).getError().getTransportErrorCode(),
                                                        new ExceptionMessage(
                                                                ((NaoRegistada) subResult).getError().getDefaultMessage().getKey(),
                                                                ((NaoRegistada) subResult).getError().getDefaultMessage().getDesc()
                                                        )

                                                )

                                        ))
                                        .toList()
                        )
                )
                .onFailure(failure ->
                        log.of(ERROR, context.apply(Option.none()), Option.of(principal.toLogPrincipal()), failure));
    }

    @Override
    public Future<Done> alterarMensagem(Principal principal, AlterarMensagemRequest alterarMensagemRequest, String chave, String idioma) {

        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemServiceImpl.alterarMensagem( %s )",
                alterarMensagemRequest
        ));

        AlterarMensagemRequest.Valid request = AlterarMensagemRequest.Valid.validate(alterarMensagemRequest, chave, idioma)
                .getOrElseThrow(failure -> BadRequest(principal));

        String idiomaLowerCase = request.getIdioma().toLowerCase();

        UpdateMensagem cmd = new UpdateMensagem(
                new MensagemAtributosParciais(
                        request.getTipo().map(t -> MensagemStateTipo.valueOf(t.name())),
                        request.getConteudo(),
                        request.getRecurso(),
                        request.getAtivo()
                ),
                principal
        );

        return writeSide
                .askTo(
                        new MensagemStateId(request.getChave(), idiomaLowerCase),
                        cmd
                )
                .recover(exception -> recoverFromError(context.get(), exception, principal, ERR_UNABLE_UPDATE_MESSAGE));
    }

    @Override
    public Future<Mensagem> obterMensagem(Principal principal, String chave, String idioma) {
        //return authorized(perms, principal -> requestBody -> {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemServiceImpl.obterMensagem(%s, %s)",
                chave,
                idioma
        ));

        ObterMensagemRequest.Valid request = ObterMensagemRequest.Valid.validate(chave, idioma)
                .getOrElseThrow(failure -> BadRequest(principal));

        String idiomaLowerCase = request.getIdioma().toLowerCase();

        return writeSide
                .askTo(new MensagemStateId(chave, idiomaLowerCase), new GetMensagem(principal))
                .map(MensagemState::convert)
                .recover(exception -> recoverFromError(context.get(), exception, principal, ERR_UNABLE_GET_MESSAGE));
    }

    @Override
    public Future<List<Mensagem>> consultarMensagens(Principal principal, ConsultarMensagensRequest consultarMensagensRequest) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemServiceImpl.consultarMensagens(chaves: %s )",
                consultarMensagensRequest.getChavesMensagens().foldLeft(
                        new StringJoiner(","),
                        StringJoiner::add
                ).toString()
        ));


        ConsultarMensagensRequest.Valid request = ConsultarMensagensRequest.Valid.validate(consultarMensagensRequest)
                .getOrElseThrow(failure -> BadRequest(Option.of(principal), Option.none()));

        return elasticReadSide
                .getMensagens(request.getChavesMensagens(), Option.of(principal.getSeloAuditoria()))
                .map(collection -> collection.map(MensagemState::convert))
                .recover(exception -> recoverFromError(context.get(), exception, principal, ERR_UNABLE_CONSULT_MESSAGES));
    }

    @Override
    public Future<List<ValorDominio>> obterTipos(Principal principal) {
        return Future.of(() -> List.of(MensagemTipo.ERROR, MensagemTipo.INFO, MensagemTipo.WARN)
                .map(r -> new ValorDominio(
                        r.toString(),
                        r.name()
                )));
    }

    @Override
    public Future<PesquisarMensagensResponse> pesquisarMensagens(Principal principal, PesquisarMensagensRequest pesquisarMensagensRequest, Optional<String> vistaId, Optional<Boolean> inativos, Optional<Integer> limite, Optional<String> ordem) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemServiceImpl.pesquisarMensagens(vistaId: %s pesquisa: %s inativos: %s limite: %s ordem: %s )",
                vistaId,
                pesquisarMensagensRequest.getPesquisa(),
                inativos,
                limite,
                ordem
        ));


        PesquisarMensagensRequest.Valid request = PesquisarMensagensRequest.Valid.validate(pesquisarMensagensRequest, vistaId, inativos, limite, ordem)
                .getOrElseThrow(failure -> BadRequest(principal));

        return request.getVistaId()
                .map(vista ->
                        elasticReadSide
                                .searchMensagem(vista, Option.of(principal.getSeloAuditoria()))
                )
                .getOrElse(Future.failed(new NoSuchFieldException("vistaId não especificada")))
                .recoverWith(failure ->
                        elasticReadSide
                                .searchMensagem(
                                        request.getPesquisa(),
                                        request.getInativos().getOrElse(false),
                                        Option.ofOptional(limite),
                                        request.getOrdem().map(t -> t.map((coluna, operador) -> Tuple.of(coluna, SortOrder.valueOf(operador)))),
                                        Option.of(principal.getSeloAuditoria())
                                )
                ).map(result -> new PesquisarMensagensResponse(
                        result._1.map(MensagemState::convert),
                        result._2
                ))
                .recover(exception -> { //handles abnormal behavior
                            Throwable failure = getRootCause(exception);
                            throw Match(failure).of(
                                    Case($(instanceOf(ScrollIdException.class)), Gone(principal)),
                                    Case($(), InternalServerError(principal, ERR_UNABLE_SEARCH_LOCALLY.getKey()))
                            );
                        }
                );
    }


    /**
     * @param context
     * @param exception
     * @param principal
     * @param defaultMessage - Default message to use
     * @return
     */
    private <T> T recoverFromError(String context, Throwable exception, Principal principal, Message defaultMessage) {
        Throwable failure = getRootCause(exception);
        throw Match(failure).of(
                Case($(instanceOf(ConflitoNoEstadoException.class)), () -> {
                    log.of(LogBuilder.Type.ERROR, context, Option.of(principal.toLogPrincipal()), failure);
                    return BusinessError(principal, failure.getMessage());
                }),
                Case($(instanceOf(InexistenciaEntidadeException.class)), () -> {
                    log.of(LogBuilder.Type.DEBUG, context, Option.of(principal.toLogPrincipal()), failure);
                    return NotFound(principal, failure.getMessage());
                }),
                Case($(instanceOf(TransportException.class)), () -> {
                    log.of(LogBuilder.Type.ERROR, context, Option.of(principal.toLogPrincipal()), failure);
                    return (TransportException) failure;
                }),
                Case($(), () -> {
                    log.of(LogBuilder.Type.ERROR, context, Option.of(principal.toLogPrincipal()), failure);
                    return InternalServerError(principal, defaultMessage.getKey());
                })
        );
    }

}
