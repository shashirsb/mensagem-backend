package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command;

import akka.Done;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemStateAtributos;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

@Value
@With
public class CreateMensagem implements MensagemCommand, PersistentEntity.ReplyType<Done> {
    MensagemStateAtributos attributes;
    Stamp stamp;
    Option<Principal> principal;

    @JsonCreator
    public CreateMensagem(MensagemStateAtributos attributes, Stamp stamp, Option<Principal> principal) {
        this.attributes = Preconditions.checkNotNull(attributes);
        this.stamp = Preconditions.checkNotNull(stamp);
        this.principal = Preconditions.checkNotNull(principal);
    }

    @JsonIgnore
    public CreateMensagem(MensagemStateAtributos attributes, Principal principal) {
        this(
                Preconditions.checkNotNull(attributes),
                Preconditions.checkNotNull(principal.getSeloAuditoria()),
                Option.of(principal)
        );
    }

    @JsonIgnore
    public CreateMensagem(MensagemStateAtributos attributes, Stamp stamp) {
        this(
                Preconditions.checkNotNull(attributes),
                Preconditions.checkNotNull(stamp),
                Option.none()
        );
    }
}
