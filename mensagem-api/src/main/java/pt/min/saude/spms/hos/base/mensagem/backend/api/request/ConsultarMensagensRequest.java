package pt.min.saude.spms.hos.base.mensagem.backend.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.Value;

@Value
public class ConsultarMensagensRequest {
    List<String> chavesMensagens;

    @JsonCreator
    public ConsultarMensagensRequest(List<String> chavesMensagens) {
        this.chavesMensagens = chavesMensagens;
    }

    @Value
    public static class Valid {
        List<String> chavesMensagens;

        @JsonCreator
        private Valid(List<String> chavesMensagens) {
            this.chavesMensagens = chavesMensagens;
        }

        public static Try<ConsultarMensagensRequest.Valid> validate(ConsultarMensagensRequest body) {
            return Try.of(
                    () -> {
                        List<String> _chaves = Preconditions.checkNotNull(body.getChavesMensagens());
                        return new ConsultarMensagensRequest.Valid(
                                _chaves
                        );
                    }
            );
        }
    }


}
