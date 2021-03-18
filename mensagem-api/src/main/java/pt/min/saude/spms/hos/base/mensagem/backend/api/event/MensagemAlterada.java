package pt.min.saude.spms.hos.base.mensagem.backend.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import lombok.Value;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemAtributos;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemId;
import pt.min.saude.spms.hos.common.classes.backend.KafkaEvent;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

@Value
public class MensagemAlterada implements MensagemEvento {
    KafkaEvent.Header header;
    Stamp stamp;
    MensagemId id;
    MensagemAtributos.Parcial atributosAlterados;

    @JsonCreator
    private MensagemAlterada(final KafkaEvent.Header cabecalho,
                             final Stamp stamp,
                             final MensagemId id,
                             final MensagemAtributos.Parcial atributosAlterados) {
        this.header = Preconditions.checkNotNull(cabecalho);
        this.stamp = Preconditions.checkNotNull(stamp);
        this.id = Preconditions.checkNotNull(id);
        this.atributosAlterados = Preconditions.checkNotNull(atributosAlterados);
    }

    public MensagemAlterada(final String installation,
                                  final Stamp stamp,
                                  final MensagemId id,
                                  final MensagemAtributos.Parcial atributosAlterados) {
        this.header = new KafkaEvent.Header(
                installation,
                "base",
                "mensagem",
                "1"//pt.min.saude.spms.hos.base.mensagem.backend.api.BuildInfo.version()
        );
        this.stamp = stamp;
        this.id = id;
        this.atributosAlterados = atributosAlterados;
    }

    @Override
    @JsonIgnore
    public String getPartitionKey() {
        return id.toString();
    }

}