package pt.min.saude.spms.hos.base.mensagem.backend.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Preconditions;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Optional;

@Value
public class PesquisarMensagensRequest {
    Option<String> pesquisa;

    @JsonCreator
    public PesquisarMensagensRequest(final Option<String> pesquisa) {
        this.pesquisa = pesquisa;
    }

    @Value
    public static class Valid {
        Option<String> pesquisa;
        Option<Boolean> inativos;
        Option<String> vistaId;
        Option<Integer> limite;
        List<Tuple2<String, String>> ordem;

        @JsonCreator
        private Valid(Option<String> pesquisa,
                     Option<Boolean> inativos,
                     Option<String> vistaId,
                     Option<Integer> limite,
                     List<Tuple2<String, String>> ordem) {
            this.pesquisa = pesquisa;
            this.inativos = inativos;
            this.vistaId = vistaId;
            this.limite = limite;
            this.ordem = ordem;
        }

        private static final Pattern ordemRegex = Pattern.compile("" +
                "(atributos.ativo|atributos.tipo|id.idioma),(asc|desc)" +
                "(?:\\+(atributos.ativo|atributos.tipo|id.idioma),(asc|desc))*"
        );

        public static Try<PesquisarMensagensRequest.Valid> validate(PesquisarMensagensRequest body,
                                                                    Optional<String> vistaId,
                                                                    Optional<Boolean> inativos,
                                                                    Optional<Integer> limite,
                                                                    Optional<String> ordem) {
            return Try.of(
                    () -> {
                        Option<String> _pesquisa = Preconditions.checkNotNull(body.getPesquisa());
                        Option<String> _vistaId = Option.ofOptional(Preconditions.checkNotNull(vistaId));
                        Option<Boolean> _inativos = Option.ofOptional(Preconditions.checkNotNull(inativos));
                        Option<Integer> _limite = Option.ofOptional(Preconditions.checkNotNull(limite));

                        Option<Matcher> matcher = Option.ofOptional(ordem).map(ordemRegex::matcher);
                        matcher.peek(m -> Preconditions.checkArgument(m.matches()));

                        List<Tuple2<String, String>> _ordem = matcher.map(m ->
                                List.rangeBy(0, m.groupCount() - 1, 2).map(i ->
                                        Tuple.of(m.group(i), m.group(i + 1))
                                )
                        ).getOrElse(List.empty());

                        return new PesquisarMensagensRequest.Valid(
                                _pesquisa,
                                _inativos,
                                _vistaId,
                                _limite,
                                _ordem
                        );
                    }
            );
        }
    }
}
