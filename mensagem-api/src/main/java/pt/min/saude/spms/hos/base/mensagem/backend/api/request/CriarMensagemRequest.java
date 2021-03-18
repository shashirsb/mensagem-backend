package pt.min.saude.spms.hos.base.mensagem.backend.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.control.Try;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemAtributos;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemId;

@Value
public class CriarMensagemRequest implements Jsonable {
    MensagemId id;
    MensagemAtributos atributos;

    @JsonCreator
    public CriarMensagemRequest(final MensagemId id,
                                final MensagemAtributos atributos) {

        this.id = id;
        this.atributos = atributos;
    }

    @Value
    public static class Valid implements Jsonable {
        MensagemId id;
        MensagemAtributos atributos;

        @JsonCreator
        private Valid(final MensagemId id,
                      final MensagemAtributos atributos) {
            this.id = id;
            this.atributos = atributos;
        }

        public static Try<CriarMensagemRequest.Valid> validate(CriarMensagemRequest body) {
            return Try.of(
                    () -> {
                        MensagemId _id = Preconditions.checkNotNull(body.getId());
                        Preconditions.checkNotNull(_id.getChave());
                        Preconditions.checkNotNull(_id.getIdioma());
                        Preconditions.checkArgument(_id.getChave().length() < 50);
                        Preconditions.checkArgument(_id.getIdioma().length() < 20);

                        MensagemAtributos _atributos = Preconditions.checkNotNull(body.getAtributos());
                        Preconditions.checkNotNull(_atributos.getTipo());
                        Preconditions.checkNotNull(_atributos.getConteudo());
                        Preconditions.checkNotNull(_atributos.getRecurso());
                        Preconditions.checkNotNull(_atributos.getAtivo());
                        Preconditions.checkArgument(_atributos.getConteudo().length() < 500);
                        Preconditions.checkArgument(_atributos.getRecurso().map(s -> s.length() < 500).getOrElse(true));

                        return new CriarMensagemRequest.Valid(
                                _id,
                                _atributos
                        );
                    }
            );
        }
    }
}
