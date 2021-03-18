package pt.min.saude.spms.hos.base.mensagem.backend.api.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.api.transport.TransportException;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

@Value
public class MsgIdError implements Jsonable {
    MensagemId mensagemId;
    TransportException error;

    @JsonCreator
    public MsgIdError(MensagemId mensagemId, TransportException error) {
        this.mensagemId = mensagemId;
        this.error = error;
    }
}
