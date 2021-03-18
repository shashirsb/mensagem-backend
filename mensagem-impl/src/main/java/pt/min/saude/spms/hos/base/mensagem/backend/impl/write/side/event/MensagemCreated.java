package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemCriada;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemEvento;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.common.classes.backend.EntityEvent;
import pt.min.saude.spms.hos.common.classes.backend.LogPrincipal;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;
import pt.min.saude.spms.hos.service.utils.backend.ExecutionContext;

@Value
@With
public class MensagemCreated implements MensagemEvent {
    EntityEvent.Header header;
    Stamp seloAuditoria;
    MensagemState mensagemState;

    public MensagemCreated(final Stamp seloAuditoria,
                           final MensagemState mensagemState) {
        this(HEADER, seloAuditoria, mensagemState);
    }

    @JsonCreator
    private MensagemCreated(final EntityEvent.Header header,
                            final Stamp seloAuditoria,
                            final MensagemState mensagemState) {
        this.header = Preconditions.checkNotNull(header);
        this.seloAuditoria = Preconditions.checkNotNull(seloAuditoria);
        this.mensagemState = Preconditions.checkNotNull(mensagemState);
    }

    @Override
    @JsonIgnore
    public String getID() {
        return mensagemState.getId().toString();
    }

    @Override
    public String getUserID() {
        return this.seloAuditoria.getUserID();
    }

    @Override
    public Stamp getStamp() {
        return seloAuditoria;
    }

    @Override
    public MensagemEvento convert() {
        return new MensagemCriada(
                ExecutionContext.getAppName(),
                seloAuditoria,
                mensagemState.convert()
        );
    }
}
