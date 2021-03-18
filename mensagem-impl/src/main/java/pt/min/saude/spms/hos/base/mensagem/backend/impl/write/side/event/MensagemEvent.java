package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemEvento;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.BuildInfo;
import pt.min.saude.spms.hos.common.classes.backend.EntityEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MensagemCreated.class, name = "MensagemCreated"),
        @JsonSubTypes.Type(value = MensagemUpdated.class, name = "MensagemUpdated")
})
public interface MensagemEvent extends EntityEvent, AggregateEvent<MensagemEvent> {

    EntityEvent.Header HEADER = new EntityEvent.Header(BuildInfo.version());

    AggregateEventShards<MensagemEvent> TAG = AggregateEventTag.sharded(MensagemEvent.class, 20);

    @Override
    default AggregateEventTagger<MensagemEvent> aggregateTag() {
        return TAG;
    }

    default boolean isPublic() {
        return true;
    }

    MensagemEvento convert();

}
