package pt.min.saude.spms.hos.base.mensagem.backend.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemAtributos;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.MensagemTipo;

@Value
public class AlterarMensagemRequest {
    Option<MensagemTipo> tipo;
    Option<String> conteudo;
    Option<String> recurso;
    Option<Boolean> ativo;

    @JsonCreator
    public AlterarMensagemRequest(final Option<MensagemTipo> tipo,
                                  final Option<String> conteudo,
                                  final Option<String> recurso,
                                  final Option<Boolean> ativo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.recurso = recurso;
        this.ativo = ativo;
    }

    @Value
    public static class Valid {
        Option<MensagemTipo> tipo;
        Option<String> conteudo;
        Option<String> recurso;
        Option<Boolean> ativo;
        String chave;
        String idioma;

        @JsonCreator
        private Valid(final Option<MensagemTipo> tipo,
                      final Option<String> conteudo,
                      final Option<String> recurso,
                      final Option<Boolean> ativo,
                      final String chave,
                      final String idioma) {
            this.tipo = tipo;
            this.conteudo = conteudo;
            this.recurso = recurso;
            this.ativo = ativo;
            this.chave = chave;
            this.idioma = idioma;
        }

        public static Try<AlterarMensagemRequest.Valid> validate(AlterarMensagemRequest body, String chave, String idioma) {
            return Try.of(
                    () -> {
                        Option<MensagemTipo> _tipo = Preconditions.checkNotNull(body.getTipo());
                        Option<String> _conteudo = Preconditions.checkNotNull(body.getConteudo());
                        Option<String> _recurso = Preconditions.checkNotNull(body.getRecurso());
                        Option<Boolean> _ativo = Preconditions.checkNotNull(body.getAtivo());
                        String _chave = Preconditions.checkNotNull(chave);
                        String _idioma = Preconditions.checkNotNull(idioma);
                        Preconditions.checkArgument(
                                body.getConteudo().map(s -> s.length() < 500).getOrElse(true)
                        );
                        Preconditions.checkArgument(
                                body.getRecurso().map(s -> s.length() < 500).getOrElse(true)
                        );
                        return new AlterarMensagemRequest.Valid(
                                _tipo,
                                _conteudo,
                                _recurso,
                                _ativo,
                                _chave,
                                _idioma
                        );
                    }
            );
        }
    }
}
