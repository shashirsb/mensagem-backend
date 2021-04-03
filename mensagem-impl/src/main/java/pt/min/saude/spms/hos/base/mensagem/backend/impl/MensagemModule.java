package pt.min.saude.spms.hos.base.mensagem.backend.impl;

//import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import pt.min.saude.spms.hos.acesso.AcessoServiceSK;
//import pt.min.saude.spms.hos.base.local.dominio.backend.api.DominiosLocalService;
import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemBackendSelfServices;
import pt.min.saude.spms.hos.base.mensagem.backend.api.MensagemService;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.provision.Provision;
import pt.min.saude.spms.hos.service.utils.backend.BaseModule;
import pt.min.saude.spms.hos.service.utils.backend.DefaultMessages;
import pt.min.saude.spms.hos.service.utils.backend.provision.ProvisionManager;

import com.lightbend.lagom.internal.javadsl.persistence.jdbc.SlickProvider;
import com.lightbend.lagom.internal.javadsl.persistence.jdbc.JavadslJdbcOffsetStore;
import com.lightbend.lagom.internal.javadsl.persistence.jdbc.JdbcReadSideImpl;
import com.lightbend.lagom.internal.javadsl.persistence.jdbc.JdbcSessionImpl;
import com.lightbend.lagom.internal.javadsl.persistence.jdbc.SlickProvider;
import com.lightbend.lagom.internal.persistence.jdbc.SlickOffsetStore;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcReadSide;
import com.lightbend.lagom.javadsl.persistence.jdbc.JdbcSession;

public class MensagemModule extends BaseModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        super.configure();

        bind(MensagemBackendSelfServices.class).to(MensagemBackendSelfServicesImpl.class);
        bind(DefaultMessages.class).to(MensagemSystemMessages.class);
        bind(ProvisionManager.class).to(Provision.class);
        bind(MensagemLoader.class).asEagerSingleton();
//        bindClient(AcessoServiceSK.class);
        bindService(MensagemService.class, MensagemServiceImpl.class);
        bind(SlickProvider.class).toProvider(GuiceSlickProvider.class);
        bind(SlickOffsetStore.class).to(JavadslJdbcOffsetStore.class);
    }
}
