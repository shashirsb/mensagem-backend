package pt.min.saude.spms.hos.base.mensagem.backend.impl.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lightbend.lagom.serialization.Jsonable;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemTipo;
import pt.min.saude.spms.hos.common.classes.backend.Constants;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SerializedTypeVersion;

@JsonTypeName("MensagemStateTipo")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Constants.SERIALIZED_TYPE_ATTRIBUTE)
public enum MensagemStateTipo implements Jsonable {
    INFO("I"),
    WARN("W"),
    ERROR("E");

    @JsonIgnore
    public static final int currentSchemaVersion = 1;
    @JsonIgnore
    public static final String versionTag = SerializedTypeVersion.build("MensagemStateTipo", currentSchemaVersion);

    MensagemTipo fromAPISchema() {
        return  MensagemTipo.valueOf(this.name());
    }

    private final String type;

    MensagemStateTipo(String s){
        type = s;
    }

    public boolean equalsType(String otherMensagemStateTipo) {return type.equals(otherMensagemStateTipo); }

    public String toString() { return this.type; }

}
