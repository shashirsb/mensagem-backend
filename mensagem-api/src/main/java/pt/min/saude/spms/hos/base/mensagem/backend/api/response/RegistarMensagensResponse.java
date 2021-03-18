package pt.min.saude.spms.hos.base.mensagem.backend.api.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.collection.List;
import lombok.Value;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemId;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MsgIdError;

@Value
public class RegistarMensagensResponse implements Jsonable {
    List<MensagemId> registadas;
    List<MensagemId> jaExistentes;
    List<MsgIdError> naoRegistadas;

    @JsonCreator
    public RegistarMensagensResponse(final List<MensagemId> registadas,
                                     final List<MensagemId> jaExistentes,
                                     final List<MsgIdError> naoRegistadas) {
        this.registadas = registadas;
        this.jaExistentes = jaExistentes;
        this.naoRegistadas = naoRegistadas;
    }

}
