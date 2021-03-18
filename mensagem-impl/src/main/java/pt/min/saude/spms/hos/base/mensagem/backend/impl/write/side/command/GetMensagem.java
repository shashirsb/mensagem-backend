package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

@Value
@With
public class GetMensagem implements MensagemCommand, PersistentEntity.ReplyType<MensagemState> {

    Stamp stamp;
    Option<Principal> principal;


    @JsonCreator
    public GetMensagem(Stamp stamp, Option<Principal> principal) {
        this.stamp = Preconditions.checkNotNull(stamp);
        this.principal = Preconditions.checkNotNull(principal);
    }

    @JsonIgnore
    public GetMensagem(Principal principal) {
        this(
                Preconditions.checkNotNull(principal.getSeloAuditoria()),
                Option.of(principal)
        );
    }

    @JsonIgnore
    public GetMensagem(Stamp stamp) {
        this(
                Preconditions.checkNotNull(stamp),
                Option.none()
        );
    }


}
