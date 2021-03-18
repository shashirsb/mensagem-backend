package pt.min.saude.spms.hos.base.mensagem.backend.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.control.Try;
import lombok.Value;

public class ObterMensagemRequest {

    @Value
    public static class Valid implements Jsonable {
        String chave;
        String idioma;

        @JsonCreator
        private Valid(final String chave,
                      final String idioma) {
            this.chave = chave;
            this.idioma = idioma;
        }

        public static Try<ObterMensagemRequest.Valid> validate(String chave, String idioma) {
            return Try.of(
                    () -> {

                        String _chave = Preconditions.checkNotNull(chave);
                        String _idioma = Preconditions.checkNotNull(idioma);

                        return new ObterMensagemRequest.Valid(
                                _chave,
                                _idioma
                        );
                    }
            );
        }
    }
}
