package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side;

import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemEvento;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemStateId;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.MensagemCommand;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemEvent;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class MensagemWriteSide {

    private final LogBuilder log = LogBuilder.getLogger(MensagemWriteSide.class);

    private final PersistentEntityRegistry registry;

    @Inject
    public MensagemWriteSide(PersistentEntityRegistry registry) {
        this.registry = registry;
    }

    // ----------------------------------Services-------------------------------------

    public <Reply, Cmd extends MensagemCommand & PersistentEntity.ReplyType<Reply>> Future<Reply> askTo(MensagemStateId id, Cmd comando) {
        return Future.fromCompletableFuture(
                registry.refFor(MensagemEntity.class, id.toString())
                        .ask(comando)
                        .toCompletableFuture()
        );
    }

    // -----------------------------------Events--------------------------------------

    public Topic<MensagemEvento> topicProducer() {

        return TopicProducer.<MensagemEvento,MensagemEvent>taggedStreamWithOffset(
                MensagemEvent.TAG.allTags(),
                (tag, offset) -> registry.eventStream(tag, offset)
                        .filter(eventOffsetPair -> eventOffsetPair.first().isPublic())
                        .mapAsync(1, eventAndOffset -> CompletableFuture.completedFuture(
                                new Pair<>(
                                        eventAndOffset.first().convert(),
                                        eventAndOffset.second()
                                )
                        ))
        );
    }

}
