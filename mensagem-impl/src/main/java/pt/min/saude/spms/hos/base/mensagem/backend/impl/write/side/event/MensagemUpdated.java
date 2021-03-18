package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemAlterada;
import pt.min.saude.spms.hos.base.mensagem.backend.api.event.MensagemEvento;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemAtributosParciais;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemStateId;
import pt.min.saude.spms.hos.common.classes.backend.EntityEvent;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;
import pt.min.saude.spms.hos.service.utils.backend.ExecutionContext;

@Value
@With
public class MensagemUpdated implements MensagemEvent {
    EntityEvent.Header header;
    Stamp seloAuditoria;
    MensagemStateId mensagemStateId;
    MensagemAtributosParciais attributesUpdated;

    public MensagemUpdated(final Stamp seloAuditoria,
                           final MensagemStateId mensagemStateId,
                           final MensagemAtributosParciais attributesUpdated) {
        this(HEADER, seloAuditoria, mensagemStateId, attributesUpdated);
    }

    @JsonCreator
    private MensagemUpdated(final EntityEvent.Header header,
                            final Stamp seloAuditoria,
                            final MensagemStateId mensagemStateId,
                            final MensagemAtributosParciais attributesUpdated) {
        this.header = Preconditions.checkNotNull(header);
        this.seloAuditoria = Preconditions.checkNotNull(seloAuditoria);
        this.mensagemStateId = Preconditions.checkNotNull(mensagemStateId);
        this.attributesUpdated = Preconditions.checkNotNull(attributesUpdated);
    }

    @Override
    @JsonIgnore
    public String getID() {
        return mensagemStateId.toString();
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
        return new MensagemAlterada(
                ExecutionContext.getAppName(),
                seloAuditoria,
                mensagemStateId.convert(),
                attributesUpdated.convert()
        );
    }
}
