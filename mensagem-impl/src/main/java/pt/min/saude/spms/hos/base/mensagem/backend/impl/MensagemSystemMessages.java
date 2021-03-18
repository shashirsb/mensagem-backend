package pt.min.saude.spms.hos.base.mensagem.backend.impl;

import io.vavr.collection.List;
import io.vavr.control.Option;
import pt.min.saude.spms.hos.service.utils.backend.DefaultMessages;
import pt.min.saude.spms.hos.service.utils.backend.Message;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MensagemSystemMessages implements DefaultMessages {

    public static Message ERR_UNABLE_CREATE_MESSAGE = Message.error(
            "HOS-BASE-MENSAGEM-E0001",
            "A mensagem pedida não foi encontrada");
    public static Message ERR_UNABLE_REGISTER_MESSAGE = Message.error(
            "HOS-BASE-MENSAGEM-E0002",
            "A mensagem pedida não foi encontrada");
    public static Message ERR_UNABLE_UPDATE_MESSAGE = Message.error(
            "HOS-BASE-MENSAGEM-E0003",
            "A mensagem pedida não foi encontrada");
    public static Message ERR_UNABLE_GET_MESSAGE = Message.error(
            "HOS-BASE-MENSAGEM-E0004",
            "A mensagem pedida não foi encontrada");
    public static Message ERR_UNABLE_CONSULT_MESSAGES = Message.error(
            "HOS-BASE-MENSAGEM-E0005",
            "A mensagem pedida não foi encontrada");
    public static Message ERR_UNABLE_SEARCH_LOCALLY = Message.error(
            "HOS-BASE-MENSAGEM-E0006",
            "A mensagem pedida não foi encontrada");
    public static Message MENSAGEM_NAO_ENCONTRADA = Message.error(
            "HOS-BASE-MENSAGEM-E0007",
            "A mensagem pedida não foi encontrada");
    public static Message MENSAGEM_ALREADY_EXISTS = Message.error(
            "HOS-BASE-MENSAGEM-E0008",
            "A mensagem pedida já existe no sistema");


    @Override
    public List<Message> getMessages() {
        return List.of(
                ERR_UNABLE_CREATE_MESSAGE,
                ERR_UNABLE_REGISTER_MESSAGE,
                ERR_UNABLE_UPDATE_MESSAGE,
                ERR_UNABLE_GET_MESSAGE,
                ERR_UNABLE_CONSULT_MESSAGES,
                ERR_UNABLE_SEARCH_LOCALLY,
                MENSAGEM_NAO_ENCONTRADA,
                MENSAGEM_ALREADY_EXISTS
        );
    }

    @Inject
    public MensagemSystemMessages() {
    }
}
