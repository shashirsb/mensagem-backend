package pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side;

import akka.Done;
import akka.actor.ActorSystem;
//import akka.stream.alpakka.elasticsearch.ElasticsearchSinkSettings;
//import akka.stream.alpakka.elasticsearch.ElasticsearchSourceSettings;
//import akka.stream.alpakka.elasticsearch.IncomingMessage;
//import akka.stream.alpakka.elasticsearch.OutgoingMessage;
//import akka.stream.alpakka.elasticsearch.javadsl.ElasticsearchSink;
//import akka.stream.alpakka.elasticsearch.javadsl.ElasticsearchSource;
//import akka.stream.javadsl.Sink;
//import akka.stream.javadsl.Source;
import com.typesafe.config.Config;
import io.vavr.Lazy;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.common.classes.backend.Stamp;
import pt.min.saude.spms.hos.elastic.utils.backend.BaseElasticReadSide;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.StringJoiner;

import static pt.min.saude.spms.hos.common.classes.backend.LogBuilder.Type.WARN;

@Singleton
public class MensagemElasticReadSide extends BaseElasticReadSide {

    private final int maxSizeSearch = 100;
    private static final TimeValue maxScrollLife = TimeValue.timeValueMinutes(10);

    @Inject
    public MensagemElasticReadSide(final Config configuration, final ActorSystem actorSystem) {
        super(
                LogBuilder.getLogger(MensagemElasticReadSide.class),
                configuration,
                actorSystem,
                Option.of("implementation.elasticsearch.index.mensagem"),
                Option.of("implementation.elasticsearch.mapping.mensagem"),
                "mensagem_index.json"
        );
    }

    //--------------------------------------------Services--------------------------------------------------------------

    public Future<List<MensagemState>> getMensagens(List<String> keysMensagens,
                                                    Option<Stamp> stamp) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemElasticReadSide.getMensagens(keysMensagens: %s, stampId: %s)",
                keysMensagens == null ? "Null" : keysMensagens.length(),
                stamp.map(Stamp::getStampID)
        ));

        return Future
                .of(() -> {
                    String keys = keysMensagens.foldLeft(
                            new StringJoiner(
                                    ",",  // delimiter
                                    "[", // prefix
                                    "]"  // suffix
                            ),
                            (acc, key) -> acc.add("\"" + key + "\"")
                    ).toString();

                    // language=JSON
                    final String query = "{\"query\":{\"bool\":{\"filter\":[{\"terms\":{\"id.chave\":" + keys + " }},{\"term\":{\"atributos.ativo\":true}}]}}}";
                                        //{ "query" :{ "bool" :{  "filter":[{ "terms ":{ "id.chave" : ["SON-2200"]}},{ "term" :{ "atributos.ativo" :true}}]}}}

                    System.out.println(query);

                    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                                    .query(QueryBuilders
                                            .queryStringQuery(query)
                                            .defaultOperator(Operator.AND)
                                    )
                                    .size(keysMensagens.length());

                    SearchRequest searchRequest = new SearchRequest()
                            .indices(elasticsearchIndex)
                            .types(elasticsearchMapping)
                            .source(searchSourceBuilder);

                    return super.search(searchSourceBuilder, MensagemState.class)
                            .map(x -> x._1)
                            .onFailure(failure ->
                                    log.of(LogBuilder.Type.ERROR, "Não foi possível obter as mensagens pedidas", context, stamp, Option.none(), failure)
                            );

//                    return Future
//                            .fromCompletableFuture(
//                                    ElasticsearchSource
//                                            .typed(
//                                                    elasticsearchIndex,
//                                                    elasticsearchMapping,
//                                                    query,
//                                                    ElasticsearchSourceSettings.Default(),
//                                                    elasticsearchClient.getLowLevelClient(),
//                                                    MensagemState.class,
//                                                    objectMapper
//                                            )
//                                            .map(OutgoingMessage::source)
//                                            .runWith(
//                                                    Sink.seq(),
//                                                    actorMaterializer
//                                            )
//                                            .thenApply(List::ofAll)
//                                            .toCompletableFuture()
//                            );
                })
                .flatMap(future -> future);
    }


    public Future<Tuple2<List<MensagemState>, String>> searchMensagem(Option<String> query,
                                                                      Boolean inativos,
                                                                      Option<Integer> limite,
                                                                      List<Tuple2<String, SortOrder>> ordenacao,
                                                                      Option<Stamp> stamp) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemElasticReadSide.searchMensagem(query: %s, inativos: %s, limite: %s, stampId: %s)",
                query,
                inativos,
                limite,
                stamp.map(Stamp::getStampID)
        ));

        SearchSourceBuilder searchSourceBuilder = ordenacao.foldLeft(
                new SearchSourceBuilder()
                        .query(QueryBuilders
                                .queryStringQuery(
                                        "(all_mensagem:" + "*" + query.getOrElse("") + "*" + ")" + (inativos ? "" : " AND (atributos.ativo:true)")
                                )
                                .defaultOperator(Operator.AND)
                        )
                        .size(limite.getOrElse(maxSizeSearch)),
                (searchBuilder, order) -> searchBuilder.sort(order._1, order._2)
        );

        SearchRequest searchRequest = new SearchRequest()
                .indices(elasticsearchIndex)
                .types(elasticsearchMapping)
                .source(searchSourceBuilder);

        return super.search(searchSourceBuilder, MensagemState.class)
                .onFailure(failure ->
                        log.of(LogBuilder.Type.ERROR, "Não foi possível pesquisar mensagens", context, stamp, Option.none(), failure)
                );

    }

    public Future<Tuple2<List<MensagemState>, String>> searchMensagem(String vistaId,
                                                                      Option<Stamp> stamp) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemElasticReadSide.searchMensagem(vistaId: %s, stampId: %s)",
                vistaId == null ? "Null" : "Ok",
                stamp.map(Stamp::getStampID)
        ));

        return super.searchGetMore(vistaId, MensagemState.class)
                .onFailure(failure ->
                        log.of(LogBuilder.Type.ERROR, "Não foi possível continuar pesquisa de domínios", context, stamp, Option.none(), failure)
                );

    }

    Future<Done> upsert(MensagemState mensagemState, Option<Stamp> stamp) {
        Lazy<String> context = Lazy.of(() -> String.format(
                "MensagemElasticReadSide.upsert(mensagemState: %s, stampId: %s)",
                mensagemState.getId(),
                stamp.map(Stamp::getStampID)
        ));

        return super.upsert(mensagemState, mensagemState.getId().toString(), context);

    }

    //--------------------------------------------Operations------------------------------------------------------------

    @Override
    public Future<Boolean> isLatestVersion() {
        return null;
    }

    @Override
    public Future<Done> upgrade() { //TODO elastic upgrade
        return Future.successful(Done.getInstance());
    }

    @Override
    public Future<Done> downgrade() { //TODO elastic downgrade
        return Future.successful(Done.getInstance());
    }

    @Override
    public Future<Done> rollback() { //TODO elastic rollback
        return Future.successful(Done.getInstance());
    }

    @Override
    public Future<Done> clearRollbackData() { //TODO elastic clearRollbackData
        return Future.successful(Done.getInstance());
    }

    //----------------------------------------------Exception-----------------------------------------------------------

    public class ElasticShardsException extends RuntimeException {
        public ElasticShardsException(ShardSearchFailure[] failures) {
            super(
                    List.of(failures).foldLeft("\n", (acc, failure) ->
                            acc +
                                    "ShardId: " + failure.shardId() + "  Status: " + failure.status().getStatus() + "\n" +
                                    "Reason: " + failure.reason() + "\n" +
                                    "Cause:" + failure.getCause().getMessage() + "\n"
                    )
            );
        }
    }

    public class ScrollIdException extends RuntimeException {
        public ScrollIdException(String errorMessage) {
            super(errorMessage);
        }
    }

}
