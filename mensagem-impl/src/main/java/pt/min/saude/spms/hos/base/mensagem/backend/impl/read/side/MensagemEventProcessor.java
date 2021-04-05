package pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;


import com.typesafe.config.Config;
import io.vavr.Lazy;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import org.pcollections.PSequence;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemCreated;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemEvent;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemUpdated;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static pt.min.saude.spms.hos.common.classes.backend.LogBuilder.Type.ERROR;
import static pt.min.saude.spms.hos.common.classes.backend.LogBuilder.Type.TRACE;

public class MensagemEventProcessor extends ReadSideProcessor<MensagemEvent> {

    private final LogBuilder log = LogBuilder.getLogger(MensagemEventProcessor.class);

    private final CassandraReadSide readSideSupport;

    private final MensagemCassandraReadSide cassandra;
    private final MensagemElasticReadSide elasticsearch;

    private final Config configuration;

    @Inject
    public MensagemEventProcessor(final CassandraReadSide readSideSupport,
                                  final MensagemCassandraReadSide cassandra,
                                  final MensagemElasticReadSide elasticsearch,
                                  Config configuration) {
        this.readSideSupport = readSideSupport;
        this.cassandra = cassandra;
        this.elasticsearch = elasticsearch;
        this.configuration = configuration;
    }

    @Override
    public ReadSideProcessor.ReadSideHandler<MensagemEvent> buildHandler() {
        return readSideSupport.<MensagemEvent>builder(configuration.getString("implementation.cassandra.read.side.processor.offset"))
                .setGlobalPrepare(this::globalPrepare)
                .setPrepare(this::prepare)
                .setEventHandler(MensagemCreated.class, this::handleCreatedMensagem)
                .setEventHandler(MensagemUpdated.class, this::handleUpdatedMensagem)
                .build();
    }

    @Override
    public PSequence<AggregateEventTag<MensagemEvent>> aggregateTags() {
        return MensagemEvent.TAG.allTags();
    }

    //--------------------------------------------Operations------------------------------------------------------------

    private CompletionStage<Done> globalPrepare() {
        Lazy<String> context = Lazy.of(() -> "MensagemEventProcessor.globalPrepare()");

        return Future
                .sequence(Arrays.asList(
                        cassandra.setup()
                                .onSuccess(done -> log.of(TRACE, "Setup mensagem cassandra readside bem sucedido", context)),
                        elasticsearch.setup()
                                .onSuccess(done -> log.of(TRACE, "Setup mensagem elasticsearch readside bem sucedido", context))
                ))
                .map(success -> Done.getInstance())
                .onFailure(failure -> log.of(ERROR, context, Option.none(), failure))
                .toCompletableFuture();
    }

    private CompletionStage<Done> prepare(AggregateEventTag<MensagemEvent> tag) {
        Lazy<String> context = Lazy.of(() -> "MensagemEventProcessor.prepare()");
        return cassandra.prepare()
                .whenComplete((r,t) -> Option.of(t).forEach(failure ->  log.of(ERROR, context, Option.none(), failure)));
    }

    //-------------------------------------------Event Handlers---------------------------------------------------------

    private CompletionStage<List<BoundStatement>> handleCreatedMensagem(final MensagemCreated mensagemCreated) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemEventProcessor.handleCreatedMensagem(mensagem: %s )",
                mensagemCreated
        ));
        return Future
                .of(() -> {
                    String id = mensagemCreated.getID();
                    Stamp stamp = mensagemCreated.getStamp();
                    MensagemState newState = mensagemCreated.getMensagemState();
                    return elasticsearch.upsert(newState, Option.of(stamp))
                            .zip(cassandra.upsert(id, newState, Option.of(stamp)))
                            .map(Tuple2::_2)
                            .map(Arrays::asList);
                })
                .flatMap(flatten -> flatten)
                .onFailure(failure -> log.of(ERROR, context, Option.none(), failure))
                .toCompletableFuture();
    }

    private CompletionStage<List<BoundStatement>> handleUpdatedMensagem(final MensagemUpdated mensagemUpdated) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemEventProcessor.handleUpdatedMensagem(mensagem: %s )",
                mensagemUpdated
        ));
        return Future
                .of(() -> {
                    String id = mensagemUpdated.getID();
                    Stamp stamp = mensagemUpdated.getStamp();
                    Future<MensagemState> futureCurrentState = state(id, Option.of(stamp));
                    Future<MensagemState> futureNewState =
                            futureCurrentState.map(
                                    currentState -> currentState.withAtributos(
                                            mensagemUpdated.getAttributesUpdated().complement(currentState.getAtributos())
                                    )
                            );

                    return futureNewState.flatMap(
                            newState -> elasticsearch.upsert(newState, Option.of(stamp))
                                    .zip(cassandra.upsert(id, newState, Option.of(stamp)))
                                    .map(Tuple2::_2)
                                    .map(Arrays::asList)
                    );
                })
                .flatMap(flatten -> flatten)
                .onFailure(failure -> log.of(ERROR, context, Option.none(), failure))
                .toCompletableFuture();
    }

    //------------------------------------------------Utils-------------------------------------------------------------

    private Future<MensagemState> state(final String id, Option<Stamp> stamp) {
        return cassandra.select(id, stamp).map(
                optionState -> optionState.getOrElse(MensagemState.initialState())
        );
    }
}
