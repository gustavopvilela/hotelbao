package hotelbao.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BackendApplication {

	private static ConfigurableApplicationContext contexto;

	public static void main(String[] args) {
		// Inicia o contexto Spring em outra thread
		Thread thread = new Thread(() -> {
			contexto = SpringApplication.run(BackendApplication.class, args);
		});
		thread.start();

		// Aguarda contexto ficar pronto
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// Exibe clientId e clientSecret
//		String clientId = contexto.getEnvironment().getProperty("security.client-id");
//		String clientSecret = contexto.getEnvironment().getProperty("security.client-secret");
//		System.out.println(clientId + ", " + clientSecret);

		// Obtém o runner do contexto e executa o menu
		AppRunner runner = contexto.getBean(AppRunner.class);
		runner.executarMenuPrincipal();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Scanner scanner() {
		return new Scanner(System.in);
	}

	@Component
	public static class AppRunner implements CommandLineRunner {

		private final RestTemplate restTemplate;
		private final Scanner scanner;

		@Value("${security.client-id}")
		private String clientId;

		@Value("${security.client-secret}")
		private String clientSecret;

		private static final String URL_BASE = "http://localhost:8080";
		private Usuario usuarioLogado = new Usuario();
		private String jwtToken;

		public AppRunner(RestTemplate restTemplate, Scanner scanner) {
			this.restTemplate = restTemplate;
			this.scanner = scanner;
		}

		@Override
		public void run(String... args) {
			// No-op: usamos executarMenuPrincipal() manualmente após inicialização
		}

		public void executarMenuPrincipal() {
			boolean emExecucao = true;

			while (emExecucao) {
				if (usuarioLogado.getId() == null) {
					mostrarPrimeiroMenu();
					System.out.print("Digite a opção: ");
					int opcao = lerOpcao();

					switch (opcao) {
						case 1 -> {
							// TODO: listar quartos
						}
						case 2 -> menuLogin();
						case 0 -> emExecucao = false;
						default -> {
							System.out.println("=== OPÇÃO INVÁLIDA ===");
							pausar();
						}
					}

				} else if (usuarioLogado.hasRole("ROLE_CLIENTE")) {
					menuCliente();
					int opcao = lerOpcao();
					// TODO: executar opções de cliente

				} else if (usuarioLogado.hasRole("ROLE_ADMIN")) {
					menuAdmin();
					int opcao = lerOpcao();
					// TODO: executar opções de admin
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
			System.out.println("0 - Sair");
		}

		private void menuLogin() {
			System.out.println("===============================================");
			System.out.println("LOGIN");
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
					ResponseEntity<Usuario> userResp = restTemplate.exchange(
							URL_BASE + "/usuario/findByLogin/" + username, HttpMethod.GET, authRequest, Usuario.class);

					usuarioLogado = userResp.getBody();
					System.out.println(usuarioLogado);

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
			System.out.println("9 - Relatório - Maior valor da estadia do cliente");
			System.out.println("10 - Relatório - Menor valor da estadia do cliente");
			System.out.println("11 - Relatório - Totalizar as estadias do cliente");
			System.out.println("0 - Sair");
		}

		private void menuCliente() {
			System.out.println("=============== Menu de opções (Cliente) ===============");
			System.out.println("1 - Fazer uma reserva");
			System.out.println("2 - Relatório - Maior valor da estadia");
			System.out.println("3 - Relatório - Menor valor da estadia");
			System.out.println("4 - Relatório - Totalizar as estadias");
			System.out.println("5 - Recuperar senha");
			System.out.println("0 - Sair");
		}
	}
}
