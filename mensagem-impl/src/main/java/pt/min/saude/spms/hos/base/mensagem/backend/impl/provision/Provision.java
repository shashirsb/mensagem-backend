package pt.min.saude.spms.hos.base.mensagem.backend.impl.provision;

import io.vavr.collection.List;
import pt.min.saude.spms.hos.acesso.componente.backend.api.sk.request.CriarComponenteRequest;
import pt.min.saude.spms.hos.common.classes.backend.LogBuilder;
import pt.min.saude.spms.hos.service.utils.backend.provision.ProvisionManager;

import java.util.UUID;

public class Provision implements ProvisionManager {

    private static final LogBuilder log = LogBuilder.getLogger(Provision.class);


    @Override
    public Boolean executeProvisionComponents() {
        return false;
    }


    @Override
    public List<CriarComponenteRequest> provisionComponents() {
        return List.empty();
    }

    @Override
    public List<UUID> getComponentsIds() {
        return List.empty();
    }


}
