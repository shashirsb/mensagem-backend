package pt.min.saude.spms.hos.base.mensagem.backend.api;

import akka.Done;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import pt.min.saude.spms.hos.base.mensagem.backend.api.request.*;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.PesquisarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.response.RegistarMensagensResponse;
import pt.min.saude.spms.hos.base.mensagem.backend.api.schema.Mensagem;
import pt.min.saude.spms.hos.common.classes.backend.Principal;
import pt.min.saude.spms.hos.common.classes.backend.ValorDominio;

import java.util.Optional;

public interface MensagemBackendSelfServices {

    Future<Done> criarMensagem(Principal principal, CriarMensagemRequest criarMensagemRequest);

    Future<RegistarMensagensResponse> registarMensagens(Principal principal, RegistrarMensagensRequest registrarMensagensRequest);

    Future<Done> alterarMensagem(Principal principal, AlterarMensagemRequest alterarMensagemRequest, String chave, String idioma);

    Future<Mensagem> obterMensagem(Principal principal, String chave, String idioma);

    Future<List<Mensagem>> consultarMensagens(Principal principal, ConsultarMensagensRequest consultarMensagensRequest);

    Future<List<ValorDominio>> obterTipos(Principal principal);

    Future<PesquisarMensagensResponse> pesquisarMensagens(Principal principal,
                                                          PesquisarMensagensRequest pesquisarMensagensRequest,
                                                          Optional<String> vistaId, Optional<Boolean> inativos, Optional<Integer> limite, Optional<String> ordem);

}