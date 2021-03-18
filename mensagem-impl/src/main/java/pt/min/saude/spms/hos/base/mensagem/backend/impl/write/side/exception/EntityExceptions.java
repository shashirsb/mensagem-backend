package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.exception;

import pt.min.saude.spms.hos.service.utils.backend.Message;


public class EntityExceptions {

    public EntityExceptions() {
    }

    public static ConflitoNoEstadoException ConflitoNoEstado(Message message) {
        return new ConflitoNoEstadoException(message);
    }

    public static InexistenciaEntidadeException InexistenciaEntidade(Message message) {
        return new InexistenciaEntidadeException(message);
    }

}
