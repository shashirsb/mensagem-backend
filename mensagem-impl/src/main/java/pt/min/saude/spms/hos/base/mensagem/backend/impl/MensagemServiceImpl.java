package pt.min.saude.spms.hos.base.mensagem.backend.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Option;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import io.vavr.collection.List;
import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemBackendSelfServices;
import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemService;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemEvento;
import pt.min.saude.spms.hos.base.mensagem.backend.api.request.*;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.PesquisarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.RegistarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemCassandraReadSide;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemElasticReadSide;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.MensagemWriteSide;
import pt.min.saude.spms.hos.common.classes.backend.*;
import pt.min.saude.spms.hos.service.utils.backend.BaseServiceImpl;

import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.Optional;

import static pt.min.saude.spms.hos.service.utils.backend.handlers.AuthenticationRequestHandler.authenticated;
//import static pt.min.saude.spms.hos.service.utils.backend.handlers.AuthenticationRequestHandler.authenticatedForMensagemBackend;


public class MensagemServiceImpl extends BaseServiceImpl implements MensagemService {

    private final JpaSession jpaSession;

    private final LogBuilder log = LogBuilder.getLogger(MensagemServiceImpl.class);

    private final MensagemWriteSide writeSide;
    private final MensagemElasticReadSide elasticReadSide;
    private final MensagemCassandraReadSide cassandraReadSide;
    private final MensagemBackendSelfServices mensagemBackendSelfServices;

    private final Principal principal = new Principal("dummy user", new AuditStamp("dummy","dummy","dummy", io.vavr.control.Option.none()), "dummy installation");

    @Inject
    public MensagemServiceImpl(MensagemWriteSide writeSide,
                               MensagemElasticReadSide elasticReadSide,
                               MensagemCassandraReadSide cassandraReadSide,
                               JpaSession jpaSession,
                               MensagemBackendSelfServices mensagemBackendSelfServices) {
        this.writeSide = writeSide;
        this.elasticReadSide = elasticReadSide;
        this.cassandraReadSide = cassandraReadSide;
        this.mensagemBackendSelfServices = mensagemBackendSelfServices;
        this.jpaSession = jpaSession;
    }

    @Override
    public Topic<MensagemEvento> topicMensagemCentral() {
        return writeSide.topicProducer();
    }

 
    public ServiceCall<NotUsed, PSequence<MensagemSummary>> getMensagemSummaries() {
      return request ->
          jpaSession.withTransaction(this::selectMensagemSummaries).thenApply(TreePVector::from);
    }

    @Override
    public ServiceCall<CriarMensagemRequest, Done> criarMensagem() {

        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Criar.Executar);

        //return authorized(perms, principal -> request -> {
//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(
//                principal -> requestBody ->
          return (requestBody -> mensagemBackendSelfServices.criarMensagem(principal,requestBody).toCompletableFuture());
//        );
    }

    @Override
    public ServiceCall<RegistrarMensagensRequest, RegistarMensagensResponse> registarMensagens() {

        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Criar.Executar);

        //return authorized(perms, principal -> request -> {
//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(principal -> requestBody ->
        return (requestBody -> mensagemBackendSelfServices.registarMensagens(principal, requestBody).toCompletableFuture()
        );
    }

    @Override
    public ServiceCall<AlterarMensagemRequest, Done> alterarMensagem(String chave, String idioma) {

        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Atualizar.Executar);

        //return authorized(perms, principal -> request -> {
//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(principal -> requestBody ->
        return (requestBody ->       mensagemBackendSelfServices.alterarMensagem(principal, requestBody, chave, idioma).toCompletableFuture()
        );
    }

//    @Override
//    public ServiceCall<NotUsed, Mensagem> obterMensagem(String chave, String idioma) {
//
//        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Consultar.Executar);
//
//        //return authorized(perms, principal -> request -> {
//        return authenticatedForMensagemBackend(principal -> requestBody -> {
//
//            Lazy<String> context = Lazy.of(() -> String.format(
//                    "MensagemServiceImpl.obterMensagem(%s, %s)",
//                    chave,
//                    idioma
//            ));
//            return Future
//                    .of(() -> {
//
//                        ObterMensagemRequest.Valid request = ObterMensagemRequest.Valid.validate(chave, idioma)
//                                .getOrElseThrow(failure -> BadRequest(principal));
//
//                        String idiomaLowerCase = request.getIdioma().toLowerCase();
//
//                        return cassandraReadSide
//                                .select(new MensagemStateId(chave, idiomaLowerCase).toString(), Option.of(principal.getSeloAuditoria()))
//                                .map(result -> result.getOrElseThrow(() -> NotFound(principal, MENSAGEM_NAO_ENCONTRADA.getKey())))
//                                .map(MensagemState::convert);
//
//                    }).flatMap(flatten -> flatten)
//                    .recoverWith(failure -> {
//
//                        StdErrorHandler.forwardIfTransportExceptionOrElseThrow(failure, () -> {
//                            log.of(LogBuilder.Type.ERROR, context, Option.of(principal.toLogPrincipal()), failure);
//                            return InternalServerError(principal, failure.getMessage());
//                        });
//
//                        return null;//unreachable
//                    })
//                    .toCompletableFuture();
//
//        });
//    }

    @Override
    public ServiceCall<NotUsed, Mensagem> obterMensagem(String chave, String idioma) {

        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Consultar.Executar);

//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(principal -> requestBody ->
        return (requestBody ->      mensagemBackendSelfServices.obterMensagem(principal, chave, idioma).toCompletableFuture()
        );
    }

    @Override
    public ServiceCall<ConsultarMensagensRequest, List<Mensagem>> consultarMensagens() {

        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Consultar.Executar);

        //return authorized(perms, principal -> request -> {
//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(principal -> requestBody ->
        return (requestBody ->       mensagemBackendSelfServices.consultarMensagens(principal, requestBody).toCompletableFuture()
        );
    }

    @Override
    public ServiceCall<PesquisarMensagensRequest, PesquisarMensagensResponse> pesquisarMensagens
            (Optional<String> vistaId,
             Optional<Boolean> inativos,
             Optional<Integer> limite,
             Optional<String> ordem) {

        List<String> perms = List.of(MensagemPermissions.Base.Mensagem.Pesquisar.Executar);

        //return authorized(perms, principal -> request -> {
//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(principal -> requestBody ->
        return (requestBody ->
                mensagemBackendSelfServices.pesquisarMensagens(principal,
                        requestBody,
                        vistaId,
                        inativos,
                        limite,
                        ordem).toCompletableFuture()
        );
    }

    @Override
    public ServiceCall<NotUsed, List<ValorDominio>> obterTipos() {
//        return authenticatedForMensagemBackend(principal -> requestBody ->
//        return authenticated(principal -> requestBody ->
        return (requestBody ->
                mensagemBackendSelfServices.obterTipos(principal)
                        .toCompletableFuture()
        );
    }

    private List<MensagemSummary> selectMensagemSummaries(EntityManager entityManager) {
        return entityManager
            .createQuery(
                "SELECT"
                    + " NEW pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemSummary(s.id, s.json)"
                    + " FROM MensagemSummaryJpaEntity s",
                    MensagemSummary.class)
            .getResultList();
      }


}
