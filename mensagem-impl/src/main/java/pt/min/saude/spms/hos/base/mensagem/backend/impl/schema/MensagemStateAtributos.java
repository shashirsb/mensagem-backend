package pt.min.saude.spms.hos.base.mensagem.backend.impl.schema;

import com.fasterxml.jackson.annotation.*;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemAtributos;
import pt.min.saude.spms.hos.common.classes.backend.Constants;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SerializedTypeVersion;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonTypeName("MensagemStateAtributos")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Constants.SERIALIZED_TYPE_ATTRIBUTE)
@Value
@With
public class MensagemStateAtributos implements Jsonable {

    @JsonIgnore
    public static final int currentSchemaVersion = 1;
    @JsonIgnore
    public static final String versionTag = SerializedTypeVersion.build("MensagemStateAtributos", currentSchemaVersion);

    MensagemStateTipo tipo;
    String conteudo;
    Option<String> recurso;
    Boolean ativo;

    @JsonCreator
    public MensagemStateAtributos(final MensagemStateTipo tipo,
                                  final String conteudo,
                                  final Option<String> recurso,
                                  final Boolean ativo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.recurso = recurso;
        this.ativo = ativo;
    }

    public MensagemAtributos convert() {
        return new MensagemAtributos(
                tipo.fromAPISchema(),
                conteudo,
                recurso,
                ativo
        );
    }

}
