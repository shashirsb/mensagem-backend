package pt.min.saude.spms.hos.base.mensagem.backend.api.schema;

import com.lightbend.lagom.serialization.Jsonable;

public enum MensagemTipo implements Jsonable {
    INFO("I"),
    WARN("W"),
    ERROR("E");

    private final String type;

    MensagemTipo(String s){
        type = s;
    }

    public boolean equalsType(String otherMensagemTipo) {return type.equals(otherMensagemTipo); }

    public String toString() { return this.type; }

}
