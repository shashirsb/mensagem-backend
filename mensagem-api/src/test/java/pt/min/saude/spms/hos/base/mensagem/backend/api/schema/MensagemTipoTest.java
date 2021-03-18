package pt.min.saude.spms.hos.base.mensagem.backend.api.schema;

import static org.junit.jupiter.api.Assertions.*;

class MensagemTipoTest {

    @org.junit.jupiter.api.Test
    void equalsType() {
        assertEquals(MensagemTipo.ERROR.equalsType("E"), true);
        assertEquals(MensagemTipo.INFO.equalsType("I"), true);
        assertEquals(MensagemTipo.WARN.equalsType("W"), true);

    }

    @org.junit.jupiter.api.Test
    void String(){
        assertEquals(MensagemTipo.WARN.toString(), "W");
        assertEquals(MensagemTipo.INFO.toString(), "I");
        assertEquals(MensagemTipo.ERROR.toString(), "E");
    }

    @org.junit.jupiter.api.Test
    void name(){
        assertEquals(MensagemTipo.WARN.name(), "WARN");
        assertEquals(MensagemTipo.INFO.name(), "INFO");
        assertEquals(MensagemTipo.ERROR.name(), "ERROR");
    }
}