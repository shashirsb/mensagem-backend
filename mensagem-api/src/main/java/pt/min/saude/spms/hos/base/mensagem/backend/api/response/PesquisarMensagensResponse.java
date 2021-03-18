package pt.min.saude.spms.hos.base.mensagem.backend.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.collection.List;
import lombok.Value;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;

@Value
public class PesquisarMensagensResponse implements Jsonable {
    List<Mensagem> mensagens;
    String vistaId;

    @JsonCreator
    public PesquisarMensagensResponse(final List<Mensagem> mensagens,
                                      final String vistaId) {
        this.mensagens = mensagens;
        this.vistaId = vistaId;
    }

}