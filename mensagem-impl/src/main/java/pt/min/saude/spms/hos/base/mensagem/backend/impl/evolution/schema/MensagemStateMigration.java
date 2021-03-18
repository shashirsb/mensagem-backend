package pt.min.saude.spms.hos.base.mensagem.backend.impl.evolution.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemState;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.BaseVersionableJacksonJsonMigration;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SchemaMigrationException;

public class MensagemStateMigration extends BaseVersionableJacksonJsonMigration {
    @Override
    public int currentVersion() {
        return MensagemState.currentSchemaVersion;
    }

    @Override
    public JsonNode transform(int fromVersion, JsonNode json) {

        try {

            MensagemIdStateMigration mensagemIdStateMigration = new MensagemIdStateMigration();
            MensagemAttributesStateMigration mensagemAttributesStateMigration = new MensagemAttributesStateMigration();

            ObjectNode root = (ObjectNode) json;
            if (fromVersion <= 1) {

            }
            return root;

        } catch (Exception e) {
            throw new SchemaMigrationException("A migração do MensagemStateMigration falhou", e);
        }

    }
}
