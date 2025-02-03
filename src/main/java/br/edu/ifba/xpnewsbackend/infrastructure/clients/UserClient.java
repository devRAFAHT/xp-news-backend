package br.edu.ifba.xpnewsbackend.infrastructure.clients;

import br.edu.ifba.xpnewsbackend.infrastructure.dto.PageableDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserClient {

    private final WebClient webClient;

    /**
     * Método para buscar todos os usuários, realizando uma requisição GET ao endpoint "/find-all".
     *
     * @return PageableDto com os dados dos usuários e informações de paginação.
     */
    public PageableDto findAll() {
        log.info("Iniciando requisição para buscar todos os usuários.");

        // Realiza a chamada GET ao endpoint "/find-all" e espera a resposta
        PageableDto response = webClient.get()
                .uri("/find-all")
                .retrieve()  // Realiza a requisição e prepara a resposta
                .bodyToMono(PageableDto.class)  // Converte o corpo da resposta para o tipo PageableDto
                .block();  // Bloqueia até que a resposta seja recebida

        log.info("Requisição para buscar todos os usuários concluída.");

        return response;
    }
}
