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
    }
}
