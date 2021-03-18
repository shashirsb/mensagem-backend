package pt.min.saude.spms.hos.base.mensagem.backend.api.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pt.min.saude.spms.hos.common.classes.backend.KafkaEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MensagemCriada.class, name = "MensagemCriada"),
        @JsonSubTypes.Type(value = MensagemAlterada.class, name = "MensagemAlterada"),
})
public interface MensagemEvento extends KafkaEvent {
}