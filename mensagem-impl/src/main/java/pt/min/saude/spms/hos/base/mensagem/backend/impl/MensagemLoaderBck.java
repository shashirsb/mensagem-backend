package pt.min.saude.spms.hos.base.mensagem.backend.impl;

import akka.actor.ActorSystem;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.typesafe.config.Config;
import pt.min.saude.spms.hos.acesso.AcessoServiceSK;
import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemBackendSelfServices;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemEventProcessor;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.MensagemEntity;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.security.utils.TokenUtils;
import pt.min.saude.spms.hos.service.utils.backend.*;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MensagemLoaderBck {

    LogBuilder log = LogBuilder.getLogger(MensagemLoaderBck.class);

    @Inject
    public MensagemLoaderBck(final Config configuration,
                             final ActorSystem actorSystem,
                             final DefaultMessages defaultMessages,
                             final PersistentEntityRegistry writeSideRegistry,
                             final ReadSide readSideRegistry,
                             final MensagemBackendSelfServices mensagemBackendSelfServices,
                             final CoreServicesClients coreServiceClients) {
        try {
            ExecutionContext.initialize(configuration, actorSystem, coreServiceClients).get();
            TokenUtils.init(
                    ExecutionContext.getObjectMapper(),
                    configuration.getString(ConfigurationKeys.CRYPTO_PRIVATE_KEY),
                    configuration.getString(ConfigurationKeys.CRYPTO_PUBLIC_KEY),
                    configuration.getString(ConfigurationKeys.CRYPTO_PRIVATE_KEY_INT_CALL),
                    configuration.getString(ConfigurationKeys.CRYPTO_PUBLIC_KEY_INT_CALL),
                    configuration.getInt(ConfigurationKeys.TOKEN_MAX_AGE),
                    configuration.getString(ConfigurationKeys.NOME_INSTALACAO)
            );

            writeSideRegistry.register(MensagemEntity.class);
            readSideRegistry.register(MensagemEventProcessor.class);

//            TranslatorMensagemBackend.initialize(
//                    mensagemBackendSelfServices,
//                    defaultMessages
//            ).get();
//
//            TranslatorMensagemBackend.registerBusinessMessages();

        } catch (Throwable t) {
            //avoid throwing errors in this stage to avoid non standard errors escaping outside
            //problems in this initialization will be handled within service call
            log.of(LogBuilder.Type.ERROR, "MensagemLoader constructor fatal error. check attached exception", t);
        }
    }
}
