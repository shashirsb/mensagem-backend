package pt.min.saude.spms.hos.base.mensagem.backend.api.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.Jsonable;
import io.vavr.control.Option;
import lombok.Value;
import lombok.With;

@Value
@With
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class MensagemAtributos implements Jsonable {
    MensagemTipo tipo;
    String conteudo;
    Option<String> recurso;
    Boolean ativo;

    @JsonCreator
    public MensagemAtributos(final MensagemTipo tipo,
                             final String conteudo,
                             final Option<String> recurso,
                             final Boolean ativo) {

        this.tipo = Preconditions.checkNotNull(tipo);
        this.conteudo = Preconditions.checkNotNull(conteudo);
        this.recurso = Preconditions.checkNotNull(recurso);
        this.ativo = Preconditions.checkNotNull(ativo);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Value
    @With
    public static class Parcial implements Jsonable {
        Option<MensagemTipo> tipo;
        Option<String> conteudo;
        Option<String> recurso;
        Option<Boolean> ativo;

        @JsonCreator
        public Parcial(final Option<MensagemTipo> tipo,
                       final Option<String> conteudo,
                       final Option<String> recurso,
                       final Option<Boolean> ativo) {

            this.tipo = Preconditions.checkNotNull(tipo);
            this.conteudo = Preconditions.checkNotNull(conteudo);
            this.recurso = Preconditions.checkNotNull(recurso);
            this.ativo = Preconditions.checkNotNull(ativo);
        }
    }

}
