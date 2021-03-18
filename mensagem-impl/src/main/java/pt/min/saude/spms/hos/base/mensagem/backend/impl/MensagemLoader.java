package pt.min.saude.spms.hos.base.mensagem.backend.impl;

import akka.actor.ActorSystem;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.typesafe.config.Config;
//import pt.min.saude.spms.hos.acesso.AcessoServiceSK;
//import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemBackendSelfServices;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.read.side.MensagemEventProcessor;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.MensagemEntity;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
//import pt.min.saude.spms.hos.security.utils.TokenUtils;
import pt.min.saude.spms.hos.service.utils.backend.*;
import pt.min.saude.spms.hos.service.utils.backend.provision.ProvisionManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MensagemLoader extends BaseServiceLoader {

    LogBuilder log = LogBuilder.getLogger(MensagemLoader.class);
    private final ReadSide readSideRegistry;
    private final PersistentEntityRegistry writeSideRegistry;

    @Inject
    public MensagemLoader(Config configuration,
                          ActorSystem actorSystem,
                          DefaultMessages defaultMessages,
                          ProvisionManager provisionManager,
                          PersistentEntityRegistry writeSideRegistry,
                          ReadSide readSideRegistry,
//                          MensagemBackendSelfServices mensagemBackendSelfServices,
                          CoreServicesClients coreServiceClients) {

        super(configuration,
                actorSystem,
                coreServiceClients,
                defaultMessages,
                provisionManager);

            this.writeSideRegistry = writeSideRegistry;
            this.readSideRegistry = readSideRegistry;

            startLoader();

//            TranslatorMensagemBackend.initialize(
//                    mensagemBackendSelfServices,
//                    defaultMessages
//            ).get();
//
//            TranslatorMensagemBackend.registerBusinessMessages();

    }

    @Override
    public void registerEntities() {
        writeSideRegistry.register(MensagemEntity.class);
        readSideRegistry.register(MensagemEventProcessor.class);
    }

    @Override
    public String originSystem() {
        return "MensagemLoader@mensagem-backend@S3";
    }
}
