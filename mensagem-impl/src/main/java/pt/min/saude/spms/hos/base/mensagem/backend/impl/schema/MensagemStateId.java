package pt.min.saude.spms.hos.base.mensagem.backend.impl.schema;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemId;
import pt.min.saude.spms.hos.common.classes.backend.Constants;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SerializedTypeVersion;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonTypeName("MensagemStateId")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Constants.SERIALIZED_TYPE_ATTRIBUTE)
@Value
@With
public class MensagemStateId implements Jsonable {

    @JsonIgnore
    public static final int currentSchemaVersion = 1;
    @JsonIgnore
    public static final String versionTag = SerializedTypeVersion.build("MensagemStateId", currentSchemaVersion);

    String chave;
    String idioma;

    @JsonIgnore
    private static final String SEPARATOR = "_MensagemId_";

    @JsonCreator
    public MensagemStateId(final String chave,
                           final String idioma) {
        Preconditions.checkArgument(!chave.contains(SEPARATOR));
        Preconditions.checkArgument(!idioma.contains(SEPARATOR));
        this.chave = Preconditions.checkNotNull(chave).toUpperCase();
        this.idioma = Preconditions.checkNotNull(idioma).toUpperCase();
    }

    @JsonIgnore
    public static MensagemStateId fromString(String string) {
        String[] tokens = string.split(SEPARATOR);
        return new MensagemStateId(
                tokens[0],
                tokens[1]
        );
    }

    @Override
    public String toString() {
        return chave + SEPARATOR + idioma;
    }

    public MensagemId convert() {
        return new MensagemId(
                chave,
                idioma
        );
    }
}
