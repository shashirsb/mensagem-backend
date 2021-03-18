package pt.min.saude.spms.hos.base.mensagem.backend.impl.evolution.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import pt.min.saude.spms.hos.base.mensagem.backend.impl.schema.MensagemStateTipo;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.BaseVersionableJacksonJsonMigration;
import pt.min.saude.spms.hos.common.classes.backend.evolution.core.SchemaMigrationException;

public class MensagemTipoStateMigration extends BaseVersionableJacksonJsonMigration {
    @Override
    public int currentVersion() {
        return MensagemStateTipo.currentSchemaVersion;
    }

    @Override
    public JsonNode transform(int fromVersion, JsonNode json) {

        try {

            ObjectNode root = (ObjectNode) json;
            if (fromVersion <= 1) {

            }
            return root;

        } catch (Exception e) {
            throw new SchemaMigrationException("A migração do MensagemTipoStateMigration falhou", e);
        }

    }
}
