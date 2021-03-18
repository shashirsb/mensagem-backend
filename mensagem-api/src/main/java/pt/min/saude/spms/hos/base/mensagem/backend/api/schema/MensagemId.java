package pt.min.saude.spms.hos.base.mensagem.backend.api.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import lombok.With;

@Value
@With
public class MensagemId implements Jsonable {

    @JsonIgnore
    private static final String SEPARATOR = "_MensagemId_";

    String chave;
    String idioma;

    @JsonCreator
    public MensagemId(final String chave,
                      final String idioma) {

        this.chave = Preconditions.checkNotNull(chave);
        this.idioma = Preconditions.checkNotNull(idioma);
    }

    @Override
    public String toString() {
        return chave + SEPARATOR + idioma;
    }
}
