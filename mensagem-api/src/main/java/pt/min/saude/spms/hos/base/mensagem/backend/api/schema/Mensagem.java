package pt.min.saude.spms.hos.base.mensagem.backend.api.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import lombok.With;

@Value
@With
public class Mensagem implements Jsonable {
    MensagemId id;
    MensagemAtributos atributos;

    @JsonCreator
    public Mensagem(final MensagemId id,
                    final MensagemAtributos atributos) {

        this.id = Preconditions.checkNotNull(id);
        this.atributos = Preconditions.checkNotNull(atributos);
    }
}

