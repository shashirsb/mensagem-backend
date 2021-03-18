package pt.min.saude.spms.hos.base.mensagem.backend.impl.schema;

import com.fasterxml.jackson.annotation.*;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemAtributos;
import pt.min.saude.spms.hos.common.classes.backend.Constants;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SerializedTypeVersion;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonTypeName("MensagemAtributosParciais")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Constants.SERIALIZED_TYPE_ATTRIBUTE)
@Value
@With
public class MensagemAtributosParciais implements Jsonable {

    @JsonIgnore
    public static final int currentSchemaVersion = 1;
    @JsonIgnore
    public static final String versionTag = SerializedTypeVersion.build("MensagemAtributosParciais", currentSchemaVersion);

    Option<MensagemStateTipo> tipo;
    Option<String> conteudo;
    Option<String> recurso;
    Option<Boolean> ativo;

    @JsonCreator
    public MensagemAtributosParciais(final Option<MensagemStateTipo> tipo,
                                     final Option<String> conteudo,
                                     final Option<String> recurso,
                                     final Option<Boolean> ativo) {
        this.tipo = Preconditions.checkNotNull(tipo);
        this.conteudo = Preconditions.checkNotNull(conteudo);
        this.recurso = Preconditions.checkNotNull(recurso);
        this.ativo = Preconditions.checkNotNull(ativo);
    }

    public MensagemStateAtributos complement(MensagemStateAtributos other) {
        return new MensagemStateAtributos(
                this.tipo.getOrElse(other.getTipo()),
                this.conteudo.getOrElse(other.getConteudo()),
                this.recurso.orElse(other.getRecurso()),
                this.ativo.getOrElse(other.getAtivo())
        );
    }

    public MensagemAtributos.Parcial convert() {
        return new MensagemAtributos.Parcial(
                tipo.map(MensagemStateTipo::fromAPISchema),
                conteudo,
                recurso,
                ativo
        );
    }
}
