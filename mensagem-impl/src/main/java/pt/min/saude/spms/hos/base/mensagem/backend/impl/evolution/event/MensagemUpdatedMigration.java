package pt.min.saude.spms.hos.base.mensagem.backend.impl.evolution.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lightbend.lagom.serialization.JacksonJsonMigration;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.evolution.schema.MensagemIdStateMigration;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.evolution.schema.MensagemPartialAttributesStateMigration;
import pt.min.saude.spms.hos.common.classes.backend.evolution.StampMigration;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SchemaMigrationException;

public class MensagemUpdatedMigration extends JacksonJsonMigration {
    @Override
    public int currentVersion() {
        return 1;
    }

    @Override
    public JsonNode transform(int fromVersion, JsonNode json) {

        try {

            StampMigration stampMigration = new StampMigration();
            MensagemIdStateMigration mensagemIdStateMigration = new MensagemIdStateMigration();
            MensagemPartialAttributesStateMigration mensagemPartialAttributesStateMigration = new MensagemPartialAttributesStateMigration();

            ObjectNode root = (ObjectNode) json;
            if (fromVersion <= 1) {

            }
            return root;

        } catch (Exception e) {
            throw new SchemaMigrationException("A migração do MensagemUpdatedMigration falhou", e);
        }

    }
}
