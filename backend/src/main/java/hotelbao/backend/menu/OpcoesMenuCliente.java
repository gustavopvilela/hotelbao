package hotelbao.backend.menu;

import hotelbao.backend.dto.NewPasswordDTO;
import hotelbao.backend.dto.RequestTokenDTO;
import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.dto.UsuarioInsertDTO;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

@Component
public class OpcoesMenuCliente {
    @Autowired
    private OpcoesMenuAdmin menuAdmin;

    public void fazerReserva (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void listarReservasCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void estadiaMaiorValorCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void estadiaMenorValorCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void totalEstadiasCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void recuperarSenha (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        System.out.println("=== O sistema entrará em timeout para o envio do e-mail, favor esperar!");
        System.out.println("Digite o seu login para recuperar a senha: ");
        String login = scanner.nextLine();

        UsuarioDTO cliente = menuAdmin.getCliente(login, jwtToken, urlBase, restTemplate);
        if (cliente == null) return;

        RequestTokenDTO requestTokenDTO = new RequestTokenDTO();
        requestTokenDTO.setEmail(cliente.getEmail());

        try {
            HttpHeaders headers = new HttpHeaders();
            //headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RequestTokenDTO> requisicao = new HttpEntity<>(requestTokenDTO, headers);
            String uri = String.format("%s/auth/recover-token", urlBase);

            ResponseEntity<Void> resposta = restTemplate.exchange(
                    uri, HttpMethod.POST, requisicao, Void.class
            );

            if (resposta.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("\n=== Código de recuperação de senha enviado para o seu e-mail. Redirecionando para reset de senha...\n");
                resetarSenha(scanner, jwtToken, urlBase, restTemplate);
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: " + ex.getMessage());
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: " + ex.getMessage());
        }
        catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("401: Bad Request: " + ex.getMessage());
        }
        catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found: " + ex.getMessage());
        }
    }

    public void resetarSenha (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        NewPasswordDTO newPasswordDTO = new NewPasswordDTO();

        System.out.println("Digite o token enviado: ");
        newPasswordDTO.setToken(scanner.nextLine());

        System.out.println("Digite a sua nova senha: ");
        newPasswordDTO.setNewPassword(scanner.nextLine());

        try {
            HttpHeaders headers = new HttpHeaders();
            //headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<NewPasswordDTO> requisicao = new HttpEntity<>(newPasswordDTO, headers);
            String uri = String.format("%s/auth/new-password", urlBase);

            ResponseEntity<Void> resposta = restTemplate.exchange(
                    uri, HttpMethod.POST, requisicao, Void.class
            );

            if (resposta.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("\n=== Senha resetada com sucesso!\n");
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: " + ex.getMessage());
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: " + ex.getMessage());
        }
        catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("401: Bad Request: " + ex.getMessage());
        }
        catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found: " + ex.getMessage());
        }
    }
}
