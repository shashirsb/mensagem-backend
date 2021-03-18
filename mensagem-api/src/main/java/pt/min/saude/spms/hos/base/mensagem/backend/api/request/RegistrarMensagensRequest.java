package pt.min.saude.spms.hos.base.mensagem.backend.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.Value;
import lombok.val;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemAtributos;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemId;

@Value
public class RegistrarMensagensRequest implements Jsonable {
    List<CriarMensagemRequest> mensagens;

    @JsonCreator
    public RegistrarMensagensRequest(final List<CriarMensagemRequest> mensagens) {
        this.mensagens = mensagens;
    }

    @Value
    public static class Valid implements Jsonable {
        List<CriarMensagemRequest.Valid> mensagens;

        @JsonCreator
        private Valid(final List<CriarMensagemRequest.Valid> mensagens) {
            this.mensagens = mensagens;
        }

        public static Try<RegistrarMensagensRequest.Valid> validate(RegistrarMensagensRequest body) {
            return Try
                    .of(() -> {
                        List<CriarMensagemRequest.Valid> _mensagens =
                                Preconditions.checkNotNull(body.getMensagens())
                                        .map(requestBody ->
                                                CriarMensagemRequest.Valid.validate(requestBody).get()
                                        );

                        return new RegistrarMensagensRequest.Valid(
                                _mensagens
                        );

                    });
        }
    }
}
