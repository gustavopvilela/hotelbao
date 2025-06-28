package hotelbao.backend.menu;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.dto.UsuarioDTO;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class AppRunner {
    private final RestTemplate restTemplate;
    private final Scanner scanner;
    private final OpcoesMenuAdmin menuAdmin;
    private final OpcoesMenuCliente menuCliente;
    private final String clientId = "myclientid";
    private final String clientSecret = "myclientsecret";
    
    private static final String URL_BASE = "http://localhost:8080";
    private UsuarioDTO usuarioLogado = new UsuarioDTO();
    private String jwtToken;
    
    public AppRunner (RestTemplate restTemplate, Scanner scanner, OpcoesMenuAdmin menuAdmin, OpcoesMenuCliente menuCliente) {
        this.restTemplate = restTemplate;
        this.scanner = scanner;
        this.menuAdmin = menuAdmin;
        this.menuCliente = menuCliente;
    }

    public void executarMenuPrincipal() {
        boolean emExecucao = true;
        int opcao;
        int opcaoInterna = 0;

        while (emExecucao) {
            if (usuarioLogado.getId() == null) {
                mostrarPrimeiroMenu();
                System.out.print("Digite a opção: ");
                opcao = lerOpcao();

                switch (opcao) {
                    case 1 -> {
                        menuAdmin.listarTodosQuartos(scanner, URL_BASE, restTemplate);
                    }
                    case 2 -> menuLogin();
                    case 3 -> {
                        menuCliente.recuperarSenha(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 0 -> emExecucao = false;
                    default -> {
                        System.out.println("=== OPÇÃO INVÁLIDA ===");
                        pausar();
                    }
                }

            } else if (usuarioLogado.hasRole("ROLE_CLIENTE")) {
                menuCliente();
                opcao = lerOpcao();

                switch (opcao) {
                    case 1 -> {
                        menuCliente.fazerReserva(usuarioLogado, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 2 -> {
                        menuCliente.listarReservasCliente(usuarioLogado, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 3 -> { /* Estadia de maior valor do cliente */
                        menuCliente.estadiaMaiorValorCliente(usuarioLogado, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 4 -> { /* Estadia de menor valor do cliente */
                        menuCliente.estadiaMenorValorCliente(usuarioLogado, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 5 -> { /* Valor total de estadias do cliente */
                        menuCliente.totalEstadiasCliente(usuarioLogado, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 6 -> {
                        menuAdmin.emitirNotaFiscal(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 7 -> {
                        menuCliente.recuperarSenha(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 0 -> emExecucao = false;
                    default -> {
                        System.out.println("=== OPÇÃO INVÁLIDA ===");
                        pausar();
                    }
                }

            } else if (usuarioLogado.hasRole("ROLE_ADMIN")) {
                menuAdmin();
                opcao = lerOpcao();

                switch (opcao) {
                    case 1 -> {
                        do {
                            menuInternoCliente();
                            System.out.print("Digite a opção: ");
                            opcaoInterna = lerOpcao();

                            switch (opcaoInterna) {
                                case 1 -> {
                                    menuAdmin.inserirCliente(scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 2 -> {
                                    menuAdmin.deletarCliente(scanner, jwtToken, URL_BASE, restTemplate);
                                }
                                case 3 -> {
                                    menuAdmin.alterarCliente(scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 0 -> {}
                                default -> {
                                    System.out.println("=== OPÇÃO INVÁLIDA ===");
                                    pausar();
                                }
                            }
                        }
                        while (opcaoInterna != 0);
                    }
                    case 2 -> {
                        do {
                            menuInternoQuarto();
                            System.out.print("Digite a opção: ");
                            opcaoInterna = lerOpcao();

                            switch (opcaoInterna) {
                                case 1 -> {
                                    menuAdmin.inserirQuarto(scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 2 -> {
                                    menuAdmin.deletarQuarto(scanner, jwtToken, URL_BASE, restTemplate);
                                }
                                case 3 -> {
                                    menuAdmin.alterarQuarto(scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 0 -> {}
                                default -> {
                                    System.out.println("=== OPÇÃO INVÁLIDA ===");
                                    pausar();
                                }
                            }
                        }
                        while (opcaoInterna != 0);
                    }
                    case 3 -> {
                        do {
                            menuInternoEstadia();
                            System.out.print("Digite a opção: ");
                            opcaoInterna = lerOpcao();

                            switch (opcaoInterna) {
                                case 1 -> {
                                    menuCliente.fazerReserva(null, scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 2 -> {
                                    menuAdmin.deletarEstadia(scanner, jwtToken, URL_BASE, restTemplate);
                                }
                                case 3 -> {
                                    menuAdmin.alterarEstadia(scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 4 -> {
                                    menuAdmin.visualizarEstadia(scanner, jwtToken, URL_BASE, restTemplate);
                                    pausar();
                                }
                                case 0 -> {}
                                default -> {
                                    System.out.println("=== OPÇÃO INVÁLIDA ===");
                                    pausar();
                                }
                            }
                        }
                        while (opcaoInterna != 0);
                    }
                    case 4 -> { /* Listar todos os clientes */
                        menuAdmin.listarTodosClientes(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 5 -> { /* Listar todos os quartos */
                        menuAdmin.listarTodosQuartos(scanner, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 6 -> { /* Listar todas as estadias */
                        menuAdmin.listarTodasEstadias(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 7 -> { /* Emitir nota fiscal */
                        menuAdmin.emitirNotaFiscal(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 8 -> { /* Limpar banco de dados */
                        menuAdmin.limparBancoDeDados(scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 9 -> { /* Estadia de maior valor do cliente */
                        menuCliente.estadiaMaiorValorCliente(null, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 10 -> { /* Estadia de menor valor do cliente */
                        menuCliente.estadiaMenorValorCliente(null, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 11 -> { /* Valor total de estadias do cliente */
                        menuCliente.totalEstadiasCliente(null, scanner, jwtToken, URL_BASE, restTemplate);
                        pausar();
                    }
                    case 0 -> System.exit(0);
                    default -> {
                        System.out.println("=== OPÇÃO INVÁLIDA ===");
                        pausar();
                    }
                }
            }
        }
    }

    private void pausar() {
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }

    private int lerOpcao() {
        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void mostrarPrimeiroMenu() {
        System.out.println("===============================================");
        System.out.println("Bem-vindo ao Sistema do Hotel BAO");
        System.out.println("===============================================");
        System.out.println("1 - Visualizar quartos do hotel");
        System.out.println("2 - Login");
        System.out.println("3 - Recuperar senha");
        System.out.println("0 - Sair");
    }

    private void menuLogin() {
        System.out.println("===============================================");
        System.out.println("Bem-vindo ao Sistema do Hotel BAO");
        System.out.println("===============================================");
        System.out.print("Usuário (login): ");
        String username = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (validarLogin(username, senha)) {
            System.out.println("Login bem-sucedido!");
        } else {
            System.out.println("Falha no login.");
            pausar();
        }
    }

    private boolean validarLogin(String username, String senha) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientId, clientSecret);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("username", username);
            body.add("password", senha);
            body.add("grant_type", "password");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URL_BASE + "/oauth2/token", request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(response.getBody());
                jwtToken = json.get("access_token").asText();

                HttpHeaders authHeaders = new HttpHeaders();
                authHeaders.setBearerAuth(jwtToken);
                HttpEntity<Void> authRequest = new HttpEntity<>(authHeaders);
                ResponseEntity<UsuarioDTO> userResp = restTemplate.exchange(
                        URL_BASE + "/usuario/findByLogin/" + username,
                        HttpMethod.GET,
                        authRequest,
                        UsuarioDTO.class
                );

                usuarioLogado = userResp.getBody();

                return true;
            } else {
                System.out.println("Erro ao autenticar: " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    private void menuAdmin() {
        System.out.println("=============== Menu de opções (Admin) ===============");
        System.out.println("1 - Cadastro de cliente");
        System.out.println("2 - Cadastro de quarto");
        System.out.println("3 - Lançamento de estadias");
        System.out.println("4 - Listar dados dos clientes");
        System.out.println("5 - Listar dados dos quartos");
        System.out.println("6 - Listar estadias cadastradas");
        System.out.println("7 - Emitir nota fiscal");
        System.out.println("8 - Limpar banco de dados");
        System.out.println("9 - Relatório - Estadia de maior valor do cliente");
        System.out.println("10 - Relatório - Estadia de menor valor do cliente");
        System.out.println("11 - Relatório - Totalizar as estadias do cliente");
        System.out.println("0 - Sair");
    }

    private void menuInternoCliente () {
        System.out.println("=============== Menu de opções do cliente ===============");
        System.out.println("1 - Inserir cliente");
        System.out.println("2 - Deletar cliente");
        System.out.println("3 - Alterar cliente");
        System.out.println("0 - Voltar ao menu anterior");
    }

    private void menuInternoQuarto () {
        System.out.println("=============== Menu de opções do cliente ===============");
        System.out.println("1 - Inserir quarto");
        System.out.println("2 - Deletar quarto");
        System.out.println("3 - Alterar quarto");
        System.out.println("0 - Voltar ao menu anterior");
    }

    private void menuInternoEstadia () {
        System.out.println("=============== Menu de opções do cliente ===============");
        System.out.println("1 - Inserir estadia");
        System.out.println("2 - Deletar estadia");
        System.out.println("3 - Alterar estadia");
        System.out.println("4 - Visualizar estadia");
        System.out.println("0 - Voltar ao menu anterior");
    }

    private void menuCliente() {
        System.out.println("=============== Menu de opções (Cliente) ===============");
        System.out.println("1 - Fazer uma reserva");
        System.out.println("2 - Minhas reservas");
        System.out.println("3 - Relatório - Estadia de maior valor");
        System.out.println("4 - Relatório - Estadia de menor valor");
        System.out.println("5 - Relatório - Totalizar as estadias");
        System.out.println("6 - Emitir nota fiscal");
        System.out.println("7 - Recuperar senha");
        System.out.println("0 - Sair");
    }
}
