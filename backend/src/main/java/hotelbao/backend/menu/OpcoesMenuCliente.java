package hotelbao.backend.menu;

import hotelbao.backend.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class OpcoesMenuCliente {
    private OpcoesMenuAdmin menuAdmin = new OpcoesMenuAdmin();

    private final String ESTADIA_URL_PATH = "/estadia";

    public void fazerReserva (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        /* Se essa ação vier do cliente, não precisamos pedir o login, mas se vier do admin, precisamos pegar os dados do cliente */
        UsuarioDTO c = cliente;

        if (c == null) {
            System.out.print("Digite o login do cliente para fazer a reserva de estadia: ");
            String login = scanner.nextLine();

            c = menuAdmin.getCliente(login, jwtToken, urlBase, restTemplate);
            if (c == null) {
                System.out.println("Usuário não existe/problema na requisição.");
                return;
            }
        }
        LocalDate data = null;
        boolean reservou = false;
        QuartoDTO q = new QuartoDTO();
        while (!reservou) {
            System.out.println("Digite o ID do quarto que deseja reservar: ");
            Long id = scanner.nextLong();
            q = menuAdmin.getQuarto(id, jwtToken, urlBase, restTemplate);
            scanner.nextLine();
            q.setId(id);

            String entrada = "";

            boolean dataCerta = false;
            try{
                do{
                    System.out.println("Digite a data que deseja reservar (YYYY-MM-DD): ");
                    entrada = scanner.nextLine();
                    data = LocalDate.parse(entrada);
                    if (data.isBefore(LocalDate.now())) {
                        System.out.println("A data não pode ser antes de hoje.");
                    } else {
                        dataCerta = true;
                    }
                } while (!dataCerta);
            } catch (DateTimeParseException d){
                System.out.println("A data foi escrita no formato errado.");
            }

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(jwtToken);
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Void> requisicao = new HttpEntity<>(headers);

                String uri = urlBase + ESTADIA_URL_PATH + "/" + data + "/" + id;
                ResponseEntity<Boolean> resposta = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        requisicao,
                        Boolean.class
                );

                if (resposta.getStatusCode() == HttpStatus.OK) {
                    if (resposta.getBody() == true) {
                        System.out.println("A data" + entrada + " no quarto " + id + "não está disponível para reservas.");
                        System.out.println("Deseja escolher uma nova data? (S/N): ");
                        String escolha = scanner.nextLine();
                        if(escolha.equalsIgnoreCase("n")) {
                            return;
                        }
                    } else {
                        reservou = true;
                    }
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

        }
        EstadiaDTO novaEstadia = new EstadiaDTO();

        assert data != null;
        novaEstadia.setDataEntrada(data);
        novaEstadia.setDataSaida(data.plusDays(1));
        novaEstadia.setQuarto(q);
        novaEstadia.setUsuario(c);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EstadiaDTO> requisicao = new HttpEntity<>(novaEstadia, headers);

            String uri = urlBase + ESTADIA_URL_PATH;
            ResponseEntity<EstadiaDTO> resposta = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requisicao,
                    EstadiaDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("=== Reserva criada com sucesso! ===");
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
    }

    public void listarReservasCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("================================================");
        System.out.println("LISTAGEM DE ESTADIAS CADASTRADAS DE CLIENTE");
        System.out.println("================================================");
        System.out.println("Deseja realmente imprimir o relatório? (S/N): ");
        String imprimir = scanner.nextLine();

        if (!imprimir.equalsIgnoreCase("S")) return;

        /* Se essa ação vier do cliente, não precisamos pedir o login, mas se vier do admin, precisamos pegar os dados do cliente */
        UsuarioDTO c = cliente;

        if (cliente == null) {
            System.out.print("Digite o login do cliente para fazer a reserva de estadia: ");
            String login = scanner.nextLine();

            c = menuAdmin.getCliente(login, jwtToken, urlBase, restTemplate);
            if (c == null) {
                System.out.println("Usuário não existe/problema na requisição.");
                return;
            }
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = String.format("%s/%s/cliente/%d", urlBase, ESTADIA_URL_PATH, c.getId());

            ResponseEntity<List<EstadiaDTO>> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, new ParameterizedTypeReference<List<EstadiaDTO>>() {}
            );

            if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                List<EstadiaDTO> todasEstadias = resposta.getBody();

                if (todasEstadias.isEmpty()) {
                    System.out.println("Não existem estadias cadastradas no sistema para esse cliente!");
                } else {
                    System.out.printf(
                            "%-40s %-15s %-15s%n",
                            "Quarto", "Entrada", "Saída"
                    );
                    System.out.println("---------------------------------------------------------------------------------------");

                    todasEstadias.forEach(estadia -> {
                        System.out.printf(
                                "%-40s %-15s %-15s%n",
                                estadia.getQuarto().getDescricao(),
                                estadia.getDataEntrada().format(formatter),
                                estadia.getDataSaida().format(formatter)
                        );
                    });
                }
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

    public void estadiaMaiorValorCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Locale real = Locale.of("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(real);

        /* Se essa ação vier do cliente, não precisamos pedir o login, mas se vier do admin, precisamos pegar os dados do cliente */
        UsuarioDTO c = cliente;

        if (cliente == null) {
            System.out.print("Digite o login do cliente para fazer a reserva de estadia: ");
            String login = scanner.nextLine();

            c = menuAdmin.getCliente(login, jwtToken, urlBase, restTemplate);
            if (c == null) {
                System.out.println("Usuário não existe/problema na requisição.");
                return;
            }
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = String.format("%s/%s/maior/%d", urlBase, ESTADIA_URL_PATH, c.getId());

            ResponseEntity<EstadiaDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, EstadiaDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                EstadiaDTO estadia = resposta.getBody();

                System.out.printf(
                        "%-40s %-15s %-15s %-10s%n",
                        "Quarto", "Entrada", "Saída", "Valor"
                );
                System.out.println("---------------------------------------------------------------------------------------");

                System.out.printf(
                        "%-40s %-15s %-15s %-10s%n",
                        estadia.getQuarto().getDescricao(),
                        estadia.getDataEntrada().format(formatter),
                        estadia.getDataSaida().format(formatter),
                        nf.format(estadia.getQuarto().getValor())
                );
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

    public void estadiaMenorValorCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Locale real = Locale.of("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(real);

        /* Se essa ação vier do cliente, não precisamos pedir o login, mas se vier do admin, precisamos pegar os dados do cliente */
        UsuarioDTO c = cliente;

        if (cliente == null) {
            System.out.print("Digite o login do cliente para fazer a reserva de estadia: ");
            String login = scanner.nextLine();

            c = menuAdmin.getCliente(login, jwtToken, urlBase, restTemplate);
            if (c == null) {
                System.out.println("Usuário não existe/problema na requisição.");
                return;
            }
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = String.format("%s/%s/menor/%d", urlBase, ESTADIA_URL_PATH, c.getId());

            ResponseEntity<EstadiaDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, EstadiaDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                EstadiaDTO estadia = resposta.getBody();

                System.out.printf(
                        "%-40s %-15s %-15s %-10s%n",
                        "Quarto", "Entrada", "Saída", "Valor"
                );
                System.out.println("---------------------------------------------------------------------------------------");

                System.out.printf(
                        "%-40s %-15s %-15s %-10s%n",
                        estadia.getQuarto().getDescricao(),
                        estadia.getDataEntrada().format(formatter),
                        estadia.getDataSaida().format(formatter),
                        nf.format(estadia.getQuarto().getValor())
                );
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

    public void totalEstadiasCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Locale real = Locale.of("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(real);

        /* Se essa ação vier do cliente, não precisamos pedir o login, mas se vier do admin, precisamos pegar os dados do cliente */
        UsuarioDTO c = cliente;

        if (cliente == null) {
            System.out.print("Digite o login do cliente para fazer a reserva de estadia: ");
            String login = scanner.nextLine();

            c = menuAdmin.getCliente(login, jwtToken, urlBase, restTemplate);
            if (c == null) {
                System.out.println("Usuário não existe/problema na requisição.");
                return;
            }
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = String.format("%s/%s/total/%d", urlBase, ESTADIA_URL_PATH, c.getId());

            ResponseEntity<Map<String, BigDecimal>> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, new ParameterizedTypeReference<Map<String, BigDecimal>>() {}
            );

            if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                Map<String, BigDecimal> total = resposta.getBody();

                System.out.printf(
                        "%-50s %-20s%n",
                        "Valor total das estadias de " + c.getNome(), nf.format(total.get("valor_total_estadias"))
                );
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
