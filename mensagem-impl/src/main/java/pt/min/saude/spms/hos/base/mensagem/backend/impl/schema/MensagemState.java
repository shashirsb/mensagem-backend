package pt.min.saude.spms.hos.base.mensagem.backend.impl.schema;

import com.fasterxml.jackson.annotation.*;

import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;
import pt.min.saude.spms.hos.common.classes.backend.*;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SerializedTypeVersion;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
@JsonTypeName("MensagemState")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Constants.SERIALIZED_TYPE_ATTRIBUTE)
@Value
@With
public class MensagemState implements Jsonable {

    @JsonIgnore
    public static final int currentSchemaVersion = 1;
    @JsonIgnore
    public static final String versionTag = SerializedTypeVersion.build("MensagemState", currentSchemaVersion);

    MensagemStateId id;
    MensagemStateAtributos atributos;
    Stamp seloAuditoria;
    String userUltAtualiz;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO8601_DATE_FORMAT)
    ZonedDateTime dtUltAtualiz;

    @JsonCreator
    public MensagemState(MensagemStateId id,
                         MensagemStateAtributos atributos,
                         Stamp seloAuditoria,
                         String userUltAtualiz,
                         ZonedDateTime dtUltAtualiz) {
        this.id = id;
        this.atributos = atributos;
        this.seloAuditoria = seloAuditoria;
        this.userUltAtualiz = userUltAtualiz;
        this.dtUltAtualiz = dtUltAtualiz;
    }

    public static MensagemState initialState() {
        return new MensagemState(
                new MensagemStateId("", ""),
                new MensagemStateAtributos(
                        MensagemStateTipo.INFO,
                        "",
                        Option.none(),
                        false
                ), new AuditStamp("", "", "", Option.none()),
                "",
                ZonedDateTime.now()
        );
    }

    public Mensagem convert() {
        return new Mensagem(
                id.convert(),
                atributos.convert()
        );
    }
}
