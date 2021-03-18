package pt.min.saude.spms.hos.base.mensagem.backend.impl.write.side.exception;

import com.google.common.base.Preconditions;
import pt.min.saude.spms.hos.service.utils.backend.Message;


public class ConflitoNoEstadoException extends RuntimeException {

    public ConflitoNoEstadoException(Message message) {
        super(message.getKey());
        Preconditions.checkNotNull(message);
    }

}
