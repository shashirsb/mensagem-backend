package pt.min.saude.spms.hos.base.mensagem.backend.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import lombok.Value;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;
import pt.min.saude.spms.hos.common.classes.backend.KafkaEvent;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;

@Value
public class MensagemCriada implements MensagemEvento {
    KafkaEvent.Header header;
    Stamp stamp;
    Mensagem mensagem;

    @JsonCreator
    private MensagemCriada(final KafkaEvent.Header header,
                           final Stamp stamp,
                           final Mensagem mensagem) {
        this.header = Preconditions.checkNotNull(header);
        this.stamp = Preconditions.checkNotNull(stamp);
        this.mensagem = Preconditions.checkNotNull(mensagem);
    }

    public MensagemCriada(final String installation, final Stamp stamp,
                                final Mensagem mensagem) {
        this.header = new KafkaEvent.Header(
                installation,
                "base",
                "mensagem",
               "1"// pt.min.saude.spms.hos.base.mensagem.backend.api.BuildInfo.version()
        );
        this.stamp = stamp;
        this.mensagem = mensagem;
    }

    @Override
    @JsonIgnore
    public String getPartitionKey() {
        return mensagem.getId().toString();
    }
}
