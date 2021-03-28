package pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side;

import akka.Done;
import akka.actor.ActorSystem;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.typesafe.config.Config;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.cassandra.utils.backend.BaseCassandraReadSide;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;

import slick.jdbc.OracleProfile.api.*;

@Singleton
public class MensagemCassandraReadSide extends BaseCassandraReadSide<MensagemState> {

    @Inject
    public MensagemCassandraReadSide(final Config configuration,
                                     final CassandraSession cassandraSession,
                                     final ActorSystem actorSystem) {
        super(
                LogBuilder.getLogger(MensagemCassandraReadSide.class),
                configuration,
                cassandraSession,
                Option.of("implementation.cassandra.read.side.keyspace.mensagem"),
                actorSystem,
                MensagemState.class
        );
    }

    //--------------------------------------------Operations------------------------------------------------------------

    @Override
    public Future<Boolean> isLatestVersion() {
        return null;
    }

    @Override
    public Future<Done> upgrade() {
        return null;
    }

    @Override
    public Future<Done> downgrade() {
        return null;
    }

    @Override
    public Future<Done> rollback() {
        return null;
    }

    @Override
    public Future<Done> clearRollbackData() {
        return null;
    }

}
