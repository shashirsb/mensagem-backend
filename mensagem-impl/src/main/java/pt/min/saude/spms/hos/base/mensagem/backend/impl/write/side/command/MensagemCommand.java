package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lightbend.lagom.serialization.Jsonable;
import pt.min.saude.spms.hos.common.classes.backend.Auditable;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateMensagem.class, name = "CreateMensagem"),
        @JsonSubTypes.Type(value = UpdateMensagem.class, name = "UpdateMensagem"),
})
public interface MensagemCommand extends Jsonable { }

