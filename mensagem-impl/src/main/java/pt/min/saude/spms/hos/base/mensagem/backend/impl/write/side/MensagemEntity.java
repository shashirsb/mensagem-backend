package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemStateId;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.CreateMensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.GetMensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.MensagemCommand;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command.UpdateMensagem;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemCreated;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemEvent;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event.MensagemUpdated;

import java.time.ZonedDateTime;
import java.util.Optional;

import static pt.min.saude.spms.hos.base.mensagem.backend.impl.MensagemSystemMessages.MENSAGEM_ALREADY_EXISTS;
import static pt.min.saude.spms.hos.base.mensagem.backend.impl.MensagemSystemMessages.MENSAGEM_NAO_ENCONTRADA;
import static pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.exception.EntityExceptions.ConflitoNoEstado;
import static pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.exception.EntityExceptions.InexistenciaEntidade;

public class MensagemEntity extends PersistentEntity<MensagemCommand, MensagemEvent, MensagemState> {

    @Override
    public Behavior initialBehavior(Optional<MensagemState> snapshotState) {
        if (snapshotState.isPresent())
            return Existente(snapshotState.get());
        else
            return Nova();
    }


    private Behavior Nova() {

        BehaviorBuilder builder = newBehaviorBuilder(MensagemState.initialState());

        // -------------------------------------------Command Handlers--------------------------------------------------

        builder.setCommandHandler(CreateMensagem.class, (request, ctx) ->
                persistAndDone(ctx,
                        new MensagemCreated(
                                request.getStamp(),
                                new MensagemState(
                                        MensagemStateId.fromString(entityId()),
                                        request.getAttributes(),
                                        request.getStamp(),
                                        request.getStamp().getUserID(),
                                        ZonedDateTime.now()
                                )
                        )
                )
        );

        builder.setReadOnlyCommandHandler(UpdateMensagem.class, (request, ctx) -> ctx.commandFailed(InexistenciaEntidade(MENSAGEM_NAO_ENCONTRADA)));

        builder.setReadOnlyCommandHandler(GetMensagem.class, (request, ctx) -> ctx.commandFailed(InexistenciaEntidade(MENSAGEM_NAO_ENCONTRADA)));

        // --------------------------------------------Event Handlers---------------------------------------------------

        builder.setEventHandler(MensagemCreated.class,
                MensagemCreated::getMensagemState
        );
        builder.setEventHandlerChangingBehavior(MensagemCreated.class,
                event -> Existente(event.getMensagemState())
        );

        return builder.build();
    }


    private Behavior Existente(MensagemState snapshot) {

        BehaviorBuilder builder = newBehaviorBuilder(snapshot);

        // -------------------------------------------Command Handlers--------------------------------------------------

        builder.setCommandHandler(UpdateMensagem.class, (request, ctx) ->
                persistAndDone(ctx,
                        new MensagemUpdated(
                                request.getStamp(),
                                MensagemStateId.fromString(entityId()),
                                request.getAttributes()
                        )
                )
        );
        builder.setReadOnlyCommandHandler(CreateMensagem.class, (request, ctx) -> ctx.commandFailed(ConflitoNoEstado(MENSAGEM_ALREADY_EXISTS)));

        builder.setReadOnlyCommandHandler(GetMensagem.class, (request, ctx) -> ctx.reply(state()));

        // -------------------------------------------Event Handlers----------------------------------------------------

        builder.setEventHandler(MensagemUpdated.class, event ->
                state().withAtributos(
                        event.getAttributesUpdated().complement(state().getAtributos())
                ).withSeloAuditoria(event.getStamp()).withUserUltAtualiz(event.getUserID()).withDtUltAtualiz(event.getStamp().getInstant())
        );

        return builder.build();
    }


    // ----------------------------------------------------Utils--------------------------------------------------------

    // ----------------------------------------------------Utils--------------------------------------------------------

    private Persist<MensagemEvent> persistAndDone(CommandContext<Done> ctx, MensagemEvent event) {
        return ctx.thenPersist(event, (e) -> ctx.reply(Done.getInstance()));
    }
}
