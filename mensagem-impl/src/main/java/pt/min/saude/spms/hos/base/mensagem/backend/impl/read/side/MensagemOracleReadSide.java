package pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.lightbend.lagom.javadsl.persistence.jpa.JpaSession;
import com.typesafe.config.Config;

import akka.Done;
import akka.actor.ActorSystem;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.oracle.utils.backend.BaseOracleReadSide;


@Singleton
public class MensagemOracleReadSide extends BaseOracleReadSide<MensagemState> {

    @Inject
    public MensagemOracleReadSide(final Config configuration,
                                     final JpaSession jpaSession,
                                     final ActorSystem actorSystem) {
        super(
            
                LogBuilder.getLogger(MensagemOracleReadSide.class),
                configuration,
                jpaSession,
                Option.of("implementation.oracle.read.side.keyspace.mensagem"),
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
