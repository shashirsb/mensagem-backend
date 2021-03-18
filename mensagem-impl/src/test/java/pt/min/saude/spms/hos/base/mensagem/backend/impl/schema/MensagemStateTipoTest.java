package pt.min.saude.spms.hos.base.mensagem.backend.impl.schema;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MensagemStateTipoTest {

    @Test
    void fromAPISchema() {
    }

    @Test
    void equalsType() {
        assertEquals(MensagemStateTipo.ERROR.equalsType("E"), true);
        assertEquals(MensagemStateTipo.INFO.equalsType("I"), true);
        assertEquals(MensagemStateTipo.WARN.equalsType("W"), true);
    }


}