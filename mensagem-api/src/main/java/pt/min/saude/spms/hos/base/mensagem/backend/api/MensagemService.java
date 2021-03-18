package pt.min.saude.spms.hos.base.mensagem.backend.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceAcl;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;
import com.lightbend.lagom.javadsl.api.transport.Method;
import io.vavr.collection.List;
import pt.min.saude.spms.hos.base.mensagem.backend.api.request.*;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.PesquisarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.RegistarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemEvento;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemTipo;
import pt.min.saude.spms.hos.common.classes.backend.ValorCodDesc;
import pt.min.saude.spms.hos.common.classes.backend.ValorDominio;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface MensagemService extends Service {

    String SERVICE_NAME = "mensagem-backend";
    String TOPIC_NAME = "eventos-negocio-mensagem";

    Topic<MensagemEvento> topicMensagemCentral();

    // ---------------------------------------------------Services------------------------------------------------------

    ServiceCall<CriarMensagemRequest, Done> criarMensagem();

    ServiceCall<RegistrarMensagensRequest, RegistarMensagensResponse> registarMensagens();

    ServiceCall<AlterarMensagemRequest, Done> alterarMensagem(String chave,
                                                              String idioma);

    ServiceCall<NotUsed, Mensagem> obterMensagem(String chave,
                                                 String idioma);

    ServiceCall<ConsultarMensagensRequest, List<Mensagem>> consultarMensagens();

    ServiceCall<NotUsed, List<ValorDominio>> obterTipos();

    ServiceCall<PesquisarMensagensRequest, PesquisarMensagensResponse> pesquisarMensagens(Optional<String> vistaId,
                                                                                          Optional<Boolean> inativos,
                                                                                          Optional<Integer> limite,
                                                                                          Optional<String> ordem);

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    default Descriptor descriptor() {
        return named(SERVICE_NAME)
                .withCalls(
                        restCall(Method.POST, "/base/mensagem", this::criarMensagem),
                        restCall(Method.POST, "/base/mensagem/registar", this::registarMensagens),
                        restCall(Method.PUT, "/base/mensagem/:chave/idioma/:idioma", this::alterarMensagem),
                        restCall(Method.GET, "/base/mensagem/:chave/idioma/:idioma", this::obterMensagem),
                        restCall(Method.GET, "/base/mensagem/tipos", this::obterTipos),
                        restCall(Method.POST, "/base/mensagem/consultar", this::consultarMensagens),
                        restCall(Method.POST, "/base/mensagem/pesquisar?vistaId&inativos&limite&ordem", this::pesquisarMensagens)
                )
                .withTopics(
                        topic(TOPIC_NAME, this::topicMensagemCentral)
                                .withProperty(
                                        KafkaProperties.partitionKeyStrategy(),
                                        MensagemEvento::getPartitionKey
                                )
                )
                .withAutoAcl(true)
                .withServiceAcls(ServiceAcl.methodAndPath(Method.OPTIONS, "/base/mensagem/[^/]*"));
    }
}