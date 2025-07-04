package hotelbao.backend.menu;

import hotelbao.backend.dto.*;
import hotelbao.backend.entity.Role;
import hotelbao.backend.exceptions.ResourceNotFound;
import hotelbao.backend.resource.EstadiaResource;
import hotelbao.backend.resource.UsuarioResource;
import hotelbao.backend.util.RestResponsePage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OpcoesMenuAdmin {
    private final String USUARIO_URL_PATH = "/usuario";
    private final String ESTADIA_URL_PATH = "/estadia";

    public OpcoesMenuAdmin () {}

    public void listarTodosClientes(Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        int page = 0, size = 50;
        List<UsuarioDTO> todosClientes = new ArrayList<>();
        boolean temMaisPaginas = true;

        try {
            /* Colocando o bearer token na requisição */
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<String> requisicao = new HttpEntity<>(headers);

            System.out.println("================================");
            System.out.println("LISTAGEM DE CLIENTES CADASTRADOS");
            System.out.println("================================");
            System.out.println("Deseja realmente imprimir o relatório? (S/N): ");
            String apagar = scanner.nextLine();

            if (!apagar.equalsIgnoreCase("S")) return;

            while (temMaisPaginas) {
                String uri = String.format("%s/%s/clientes?page=%d&size=%d", urlBase, USUARIO_URL_PATH, page, size);

                /* Faz a chamada da requisição */
                ResponseEntity<RestResponsePage<UsuarioDTO>> resposta = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        requisicao,
                        new ParameterizedTypeReference<RestResponsePage<UsuarioDTO>>() {}
                );

                if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                    Page<UsuarioDTO> clientes = resposta.getBody();

                    // Adiciona os clientes da página atual à lista completa
                    todosClientes.addAll(clientes.getContent());

                    // Verifica se há próxima página
                    temMaisPaginas = clientes.hasNext();
                    page++; // Incrementa para a próxima página

                    PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                            clientes.getSize(),
                            clientes.getNumber(),
                            clientes.getTotalElements(),
                            clientes.getTotalPages()
                    );

                    List<EntityModel<UsuarioDTO>> conteudo = clientes.stream()
                            .map(EntityModel::of)
                            .toList();

                    PagedModel<EntityModel<UsuarioDTO>> pagedModel = PagedModel.of(conteudo, metadata);

                    pagedModel.add(
                            linkTo(methodOn(UsuarioResource.class)
                                    .findAllClients(PageRequest.of(clientes.getNumber(), clientes.getSize()))
                            ).withSelfRel()
                    );

                    if (clientes.hasNext()) {
                        Pageable next = clientes.nextPageable();
                        pagedModel.add(linkTo(methodOn(UsuarioResource.class).findAllClients(next)).withRel("next"));
                    }
                    if (clientes.hasPrevious()) {
                        Pageable prev = clientes.previousPageable();
                        pagedModel.add(linkTo(methodOn(UsuarioResource.class).findAllClients(prev)).withRel("prev"));
                    }
                } else {
                    temMaisPaginas = false;
                }
            }

            /* Imprimindo o conteúdo de TODOS os clientes */
            if (todosClientes.isEmpty()) {
                System.out.println("Não existem clientes cadastrados no sistema!");
            } else {
                System.out.printf(
                        "%-7s %-40s %-20s%n",
                        "ID", "Nome", "Telefone"
                );
                System.out.println("---------------------------------------------------------------------------------------");

                todosClientes.forEach(cliente -> {
                    System.out.printf(
                            "%-7s %-40s %-20s%n",
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getTelefone()
                    );
                });
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: " + ex.getMessage());
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: " + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("ERRO: " + ex.getMessage());
        }
    }

    public void listarTodosQuartos(Scanner scanner, String urlBase, RestTemplate restTemplate) {
        int page = 0, size = 50;
        List<QuartoDTO> todosQuartos = new ArrayList<>();
        boolean temMaisPaginas = true;

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requisicao = new HttpEntity<>(headers);

            System.out.println("================================");
            System.out.println("LISTAGEM DE QUARTOS CADASTRADOS");
            System.out.println("================================");
            System.out.println("Deseja realmente imprimir o relatório? (S/N): ");
            String apagar = scanner.nextLine();

            if (!apagar.equalsIgnoreCase("S")) return;

            while (temMaisPaginas) {
                String uri = String.format("%s/%s?page=%d&size=%d", urlBase, "/quarto", page, size);

                /* Faz a chamada da requisição */
                ResponseEntity<RestResponsePage<QuartoDTO>> resposta = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        requisicao,
                        new ParameterizedTypeReference<RestResponsePage<QuartoDTO>>() {}
                );

                if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                    Page<QuartoDTO> quartos = resposta.getBody();

                    // Adiciona os clientes da página atual à lista completa
                    todosQuartos.addAll(quartos.getContent());

                    // Verifica se há próxima página
                    temMaisPaginas = quartos.hasNext();
                    page++; // Incrementa para a próxima página

                    PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                            quartos.getSize(),
                            quartos.getNumber(),
                            quartos.getTotalElements(),
                            quartos.getTotalPages()
                    );

                    List<EntityModel<QuartoDTO>> conteudo = quartos.stream()
                            .map(EntityModel::of)
                            .toList();

                    PagedModel<EntityModel<QuartoDTO>> pagedModel = PagedModel.of(conteudo, metadata);

                    pagedModel.add(
                            linkTo(methodOn(UsuarioResource.class)
                                    .findAllClients(PageRequest.of(quartos.getNumber(), quartos.getSize()))
                            ).withSelfRel()
                    );

                    if (quartos.hasNext()) {
                        Pageable next = quartos.nextPageable();
                        pagedModel.add(linkTo(methodOn(UsuarioResource.class).findAllClients(next)).withRel("next"));
                    }
                    if (quartos.hasPrevious()) {
                        Pageable prev = quartos.previousPageable();
                        pagedModel.add(linkTo(methodOn(UsuarioResource.class).findAllClients(prev)).withRel("prev"));
                    }
                } else {
                    temMaisPaginas = false;
                }
            }

            /* Imprimindo o conteúdo de TODOS os clientes */
            if (todosQuartos.isEmpty()) {
                System.out.println("Não existem quartos cadastrados no sistema!");
            } else {
                System.out.printf(
                        "%-15s %-50s %-15s%n",
                        "ID", "Descrição", "Valor"
                );
                System.out.println("---------------------------------------------------------------------------------------");
                todosQuartos.forEach(  quarto -> {
                    System.out.printf(
                            "%-15s %-50s %-15s%n",
                            quarto.getId(),
                            quarto.getDescricao(),
                            quarto.getValor()
                    );
                });
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: " + ex.getMessage());
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: " + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("ERRO: " + ex.getMessage());
        }
    }

    public void listarTodasEstadias(Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        int page = 0, size = 50;
        List<EstadiaDTO> todasEstadias = new ArrayList<>();
        boolean temMaisPaginas = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            /* Colocando o bearer token na requisição */
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<String> requisicao = new HttpEntity<>(headers);

            System.out.println("================================");
            System.out.println("LISTAGEM DE ESTADIAS CADASTRADAS");
            System.out.println("================================");
            System.out.println("Deseja realmente imprimir o relatório? (S/N): ");
            String imprimir = scanner.nextLine();

            if (!imprimir.equalsIgnoreCase("S")) return;

            while (temMaisPaginas) {
                String uri = String.format("%s/%s?page=%d&size=%d", urlBase, ESTADIA_URL_PATH, page, size);

                /* Faz a chamada da requisição */
                ResponseEntity<RestResponsePage<EstadiaDTO>> resposta = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        requisicao,
                        new ParameterizedTypeReference<RestResponsePage<EstadiaDTO>>() {}
                );

                if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                    Page<EstadiaDTO> estadias = resposta.getBody();

                    // Adiciona os clientes da página atual à lista completa
                    todasEstadias.addAll(estadias.getContent());

                    // Verifica se há próxima página
                    temMaisPaginas = estadias.hasNext();
                    page++; // Incrementa para a próxima página

                    PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                            estadias.getSize(),
                            estadias.getNumber(),
                            estadias.getTotalElements(),
                            estadias.getTotalPages()
                    );

                    List<EntityModel<EstadiaDTO>> conteudo = estadias.stream()
                            .map(EntityModel::of)
                            .toList();

                    PagedModel<EntityModel<EstadiaDTO>> pagedModel = PagedModel.of(conteudo, metadata);

                    pagedModel.add(
                            linkTo(methodOn(EstadiaResource.class)
                                    .findAll(PageRequest.of(estadias.getNumber(), estadias.getSize()))
                            ).withSelfRel()
                    );

                    if (estadias.hasNext()) {
                        Pageable next = estadias.nextPageable();
                        pagedModel.add(linkTo(methodOn(EstadiaResource.class).findAll(next)).withRel("next"));
                    }
                    if (estadias.hasPrevious()) {
                        Pageable prev = estadias.previousPageable();
                        pagedModel.add(linkTo(methodOn(EstadiaResource.class).findAll(prev)).withRel("prev"));
                    }
                } else {
                    temMaisPaginas = false;
                }
            }

            /* Imprimindo o conteúdo de TODOS os clientes */
            if (todasEstadias.isEmpty()) {
                System.out.println("Não existem estadias cadastradas no sistema!");
            } else {
                System.out.printf(
                        "%-20s %-40s %-15s %-15s%n",
                        "Cliente", "Quarto", "Entrada", "Saída"
                );
                System.out.println("---------------------------------------------------------------------------------------");

                todasEstadias.forEach(estadia -> {
                    System.out.printf(
                            "%-20s %-40s %-15s %-15s%n",
                            estadia.getUsuario().getNome(),
                            estadia.getQuarto().getDescricao(),
                            estadia.getDataEntrada().format(formatter),
                            estadia.getDataSaida().format(formatter)
                    );
                });
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: " + ex.getMessage());
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: " + ex.getMessage());
        }
        catch (Exception ex) {
            System.out.println("ERRO: " + ex.getMessage());
        }
    }

    public void limparBancoDeDados (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        System.out.println("=========================");
        System.out.println("DELETAR BANCO DE DADOS");
        System.out.println("=========================");

        System.out.println("Deseja realmente APAGAR todos os dados cadastrados? (S/N): ");
        String apagar = scanner.nextLine();

        if (!apagar.equalsIgnoreCase("S")) return;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            ResponseEntity<Void> resposta = restTemplate.exchange(
                    urlBase + "/database/clear",
                    HttpMethod.DELETE,
                    requisicao,
                    Void.class
            );

            if (resposta.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("Banco de dados deletado com sucesso.");
            }
            else {
                System.out.println("Falha ao limpar banco de dados: " + resposta.getStatusCode());
            }
        } catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden");
        } catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized");
        } catch (Exception ex) {
            System.out.println("Erro ao chamar requisição: " + ex.getMessage());
        }
    }

    public void inserirCliente (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        UsuarioInsertDTO novoCliente = new UsuarioInsertDTO();
        Role role = new Role(2L, "ROLE_CLIENTE");
        Set<RoleDTO> roles = new HashSet<>();
        roles.add(new RoleDTO(role));

        System.out.print("Informe o nome do cliente: ");
        novoCliente.setNome(scanner.nextLine());

        System.out.print("Informe o e-mail do cliente: ");
        novoCliente.setEmail(scanner.nextLine());

        System.out.print("Informe o telefone do cliente: ");
        novoCliente.setTelefone(scanner.nextLine());

        System.out.print("Informe o login de acesso do cliente: ");
        novoCliente.setLogin(scanner.nextLine());

        /* A senha padrão é o telefone */
        novoCliente.setSenha(novoCliente.getTelefone());
        novoCliente.setRoles(roles);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioInsertDTO> requisicao = new HttpEntity<>(novoCliente, headers);

            String uri = urlBase + USUARIO_URL_PATH;
            ResponseEntity<UsuarioDTO> resposta = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requisicao,
                    UsuarioDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("=== Cliente criado com sucesso! ===");
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

    public void deletarCliente (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        String deletar;

        System.out.print("Digite o login do cliente que deseja deletar: ");
        String login = scanner.nextLine();

        UsuarioDTO cliente = getCliente(login, jwtToken, urlBase, restTemplate);
        if (cliente == null) {
            System.out.println("Usuário não existe/problema na requisição.");
            return;
        }

        if (cliente.hasRole("ROLE_ADMIN")) {
            System.out.print("O usuário " + cliente.getLogin() + " é um administrador, quer mesmo excluir? (S/N): ");
            deletar = scanner.nextLine();
            if (!deletar.equalsIgnoreCase("S")) return;
        }
        else if (cliente.hasRole("ROLE_CLIENTE")) {
            System.out.print("Tem certeza que deseja excluir o cliente: " + cliente.getLogin() + "? (S/N): ");
            deletar = scanner.nextLine();
            if (!deletar.equalsIgnoreCase("S")) return;
        }

        /* Fazendo a requisição */
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = String.format("%s/%s/%d", urlBase, USUARIO_URL_PATH, cliente.getId());

            ResponseEntity<Void> resposta = restTemplate.exchange(
                    uri, HttpMethod.DELETE, requisicao, Void.class
            );

            if (resposta.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("=== Usuário deletado com sucesso! ===");
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

    public void alterarCliente (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        System.out.println("Digite o login do cliente que deseja modificar: ");
        String login = scanner.nextLine();

        UsuarioDTO cliente = getCliente(login, jwtToken, urlBase, restTemplate);
        if (cliente == null) {
            System.out.println("Usuário não existe/problema na requisição.");
            return;
        }

        /* Aqui, presumo que o administrador não tenha permissão de trocar
         * a senha do cliente a não ser que ele altere o telefone. */
        System.out.println("\n=== AVISO: deixe os campos em branco caso os dados não tiverem sido alterados.\n");

        System.out.println("Informe o nome do cliente: ");
        String nome = scanner.nextLine();
        cliente.setNome(nome.trim().isEmpty() ? cliente.getNome() : nome);

        System.out.print("Informe o e-mail do cliente: ");
        String email = scanner.nextLine();
        cliente.setEmail(email.trim().isEmpty() ? cliente.getEmail() : email);

        System.out.print("Informe o telefone do cliente: ");
        String telefone = scanner.nextLine();
        cliente.setTelefone(telefone.trim().isEmpty() ? cliente.getTelefone() : telefone);

        System.out.print("Informe o login de acesso do cliente: ");
        String novoLogin = scanner.nextLine();
        cliente.setLogin(novoLogin.trim().isEmpty() ? cliente.getLogin() : novoLogin);

        /* Transformando em um UsuarioInsertDTO para inserir */
        UsuarioInsertDTO clienteInserir = new UsuarioInsertDTO(cliente);

        clienteInserir.setSenha(clienteInserir.getTelefone());

        /* Fazendo a requisição */
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioInsertDTO> requisicao = new HttpEntity<>(clienteInserir, headers);

            String uri = String.format("%s/%s/%d", urlBase, USUARIO_URL_PATH, cliente.getId());
            ResponseEntity<UsuarioDTO> resposta = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requisicao,
                    UsuarioDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK) {
                System.out.println("=== Cliente atualizado com sucesso! ===");
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

    public UsuarioDTO getCliente (String login, String jwtToken, String urlBase, RestTemplate restTemplate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (jwtToken != null) headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = urlBase + USUARIO_URL_PATH + "/findByLogin/" + login;

            ResponseEntity<UsuarioDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, UsuarioDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK) {
                return resposta.getBody();
            }
            else return null;
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

        /* Se não achou nada */
        return null;
    }

    public void inserirQuarto (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {

        QuartoDTO novoQuarto = new QuartoDTO();

        System.out.print("Informe a descrição do quarto: ");
        novoQuarto.setDescricao(scanner.nextLine());

        System.out.print("Informe o valor da diária: ");
        novoQuarto.setValor(new BigDecimal(scanner.nextLine()));

        System.out.print("Insira a URL de uma imagem do quarto: ");
        novoQuarto.setImagemUrl(scanner.nextLine());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<QuartoDTO> requisicao = new HttpEntity<>(novoQuarto, headers);

            String uri = urlBase + "/quarto";
            ResponseEntity<QuartoDTO> resposta = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requisicao,
                    QuartoDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("=== Quarto criado com sucesso! ===");
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

    public void deletarQuarto(Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        System.out.print("Digite o id do quarto que deseja deletar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        QuartoDTO quarto = getQuarto(id, jwtToken, urlBase, restTemplate);

        if (quarto == null) {
            System.out.println("Quarto não existe ou houve problema na requisição.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir o quarto: " + quarto.getDescricao() + "? (S/N): ");
        String deletar = scanner.nextLine();

        if (!deletar.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = urlBase + "/quarto/" + quarto.getId();

            ResponseEntity<Void> resposta = restTemplate.exchange(
                    uri, HttpMethod.DELETE, requisicao, Void.class
            );

            if (resposta.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("=== Quarto deletado com sucesso! ===");
            }
        } catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: Sem permissão para deletar.");
        } catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: Token inválido ou ausente.");
        } catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("400: Bad Request: Verifique os dados enviados.");
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found: Quarto com ID " + id + " não encontrado.");
        }
    }

    public void alterarQuarto(Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        System.out.print("Digite o ID do quarto que deseja modificar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        QuartoDTO quarto = getQuarto(id, jwtToken, urlBase, restTemplate);

        if (quarto == null) {
            System.out.println("Quarto não encontrado ou erro na requisição.");
            return;
        }

        System.out.println("\n=== AVISO: Deixe os campos em branco para manter os valores atuais. ===");

        System.out.print("Descrição atual: " + quarto.getDescricao() + "\nNova descrição: ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.trim().isEmpty()) {
            quarto.setDescricao(novaDescricao);
        }

        System.out.print("Valor atual: R$" + quarto.getValor() + "\nNovo valor (apenas número): ");
        String novoValorStr = scanner.nextLine();
        if (!novoValorStr.trim().isEmpty()) {
            try {
                quarto.setValor(new BigDecimal(novoValorStr));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Mantendo valor anterior.");
            }
        }

        System.out.print("URL da imagem atual: " + quarto.getImagemUrl() + "\nNova URL da imagem: ");
        String novaImagemUrl = scanner.nextLine();
        if (!novaImagemUrl.trim().isEmpty()) {
            quarto.setImagemUrl(novaImagemUrl);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<QuartoDTO> requisicao = new HttpEntity<>(quarto, headers);

            String uri = urlBase + "/quarto/" + id;

            ResponseEntity<QuartoDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.PUT, requisicao, QuartoDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK) {
                System.out.println("=== Quarto atualizado com sucesso! ===");
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden - Sem permissão para atualizar.");
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized - Token inválido ou expirado.");
        }
        catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("400: Bad Request - Erro na requisição.");
        }
        catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found - Quarto não encontrado.");
        }
    }

    public QuartoDTO getQuarto (Long id, String jwtToken, String urlBase, RestTemplate restTemplate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (jwtToken != null) headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = urlBase + "/quarto/" + id;

            ResponseEntity<QuartoDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, QuartoDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK) {
                return resposta.getBody();
            } else {
                System.out.println("Erro inesperado: status " + resposta.getStatusCode());
                return null;
            }
        } catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden - Sem permissão para acessar esse recurso.");
        } catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized - Token inválido ou ausente.");
        } catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("400: Bad Request - Verifique os dados enviados.");
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found - Quarto com ID " + id + " não encontrado.");
        }

        return null;
    }

    public void alterarEstadia (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {

        long id;
        EstadiaDTO estadia;

        while (true) {

            System.out.println("Digite o id da estadia que deseja modificar: ");
            id = scanner.nextLong();
            scanner.nextLine();

            estadia = getEstadia(id, jwtToken, urlBase, restTemplate);
            if (estadia == null) {
                System.out.println("Esse id não existe.\nDeseja digitar outro id? (S/N)");
                String escolha = scanner.nextLine();
                if (escolha.equalsIgnoreCase("n")) {
                    return;
                }
            } else  {
                break;
            }
        }

        System.out.println("Digite 0 para manter os dados atuais");

        while (true){
            System.out.print("Id do quarto atual: " + estadia.getQuarto().getId() + "\nId do novo quarto: ");
            long novoQuarto = scanner.nextLong();
            scanner.nextLine();
            if (novoQuarto == 0) {
               break;
            }

            QuartoDTO quarto = getQuarto(novoQuarto, jwtToken, urlBase, restTemplate);
            if (quarto != null) {
                estadia.setQuarto(quarto);
                break;
            } else {
                System.out.println("Quarto não existe, deseja escolher outro quarto? (S/N)");
                if (scanner.nextLine().equalsIgnoreCase("n")) {
                    return;
                }
            }
        }

        while (true){
            System.out.print("Data de entrada atual: " + estadia.getDataEntrada() + "\nNova data de entrada: ");
            String entrada = scanner.nextLine();

            if (entrada.equals("0")){
                break;
            }

            LocalDate dataEntrada;
            try{
                dataEntrada = LocalDate.parse(entrada);
                if(dataEntrada.isBefore(LocalDate.now())){
                    System.out.println("A data de entrada não pode ser anterior a hoje.");
                    continue;
                }
            } catch (DateTimeParseException d){
                System.out.println("A data foi escrita no formato errado.");
                continue;
            }

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(jwtToken);
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Void> requisicao = new HttpEntity<>(headers);

                Long idQuarto = estadia.getQuarto().getId();

                String uri = urlBase + ESTADIA_URL_PATH + "/" + dataEntrada + "/" + idQuarto;
                ResponseEntity<Boolean> resposta = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        requisicao,
                        Boolean.class
                );

                if (resposta.getStatusCode() == HttpStatus.OK) {
                    if (Boolean.TRUE.equals(resposta.getBody())) {
                        System.out.println("A data" + entrada + " no quarto " + idQuarto + "não está disponível para reservas.");
                        System.out.println("Deseja escolher uma nova data? (S/N): ");
                        String escolha = scanner.nextLine();
                        if(escolha.equalsIgnoreCase("n")) {
                            return;
                        }
                    } else {
                        estadia.setDataEntrada(dataEntrada);
                        estadia.setDataSaida(dataEntrada.plusDays(1));
                        break;
                    }
                }

            } catch (HttpClientErrorException.Forbidden ex) {
                System.out.println("403: Forbidden: " + ex.getMessage());
            }
            catch (HttpClientErrorException.Unauthorized ex) {
                System.out.println("401: Unauthorized: " + ex.getMessage());
            }
            catch (HttpClientErrorException.BadRequest ex) {
                System.out.println("401: Bad Request: " + ex.getMessage());
            }

        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EstadiaDTO> requisicao = new HttpEntity<>(estadia, headers);

            String uri = urlBase + "/estadia/" + id;

            ResponseEntity<EstadiaDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.PUT, requisicao, EstadiaDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK) {
                System.out.println("=== Estadia atualizada com sucesso! ===");
            }
        }
        catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden - Sem permissão para atualizar.");
        }
        catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized - Token inválido ou expirado.");
        }
        catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("400: Bad Request - Erro na requisição.");
        }
        catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found - Estadia não encontrada.");
        }

    }

    public void deletarEstadia (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        System.out.print("Digite o id da estadia que deseja deletar: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        EstadiaDTO estadia = getEstadia(id, jwtToken, urlBase, restTemplate);

        if (estadia == null) {
            System.out.println("Estadia não existe ou houve problema na requisição.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir a estadia: " + estadia.getId() + "? (S/N): ");
        String deletar = scanner.nextLine();

        if (!deletar.equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = urlBase + "/estadia/" + estadia.getId();

            ResponseEntity<Void> resposta = restTemplate.exchange(
                    uri, HttpMethod.DELETE, requisicao, Void.class
            );

            if (resposta.getStatusCode() == HttpStatus.NO_CONTENT) {
                System.out.println("=== Estadia deletada com sucesso! ===");
            }
        } catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden: Sem permissão para deletar.");
        } catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized: Token inválido ou ausente.");
        } catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("400: Bad Request: Verifique os dados enviados.");
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found: Estadia com ID " + id + " não encontrado.");
        }
    }

    public void visualizarEstadia (Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {

        EstadiaDTO estadia;
        while (true){
            System.out.println("Informe o Id da estadia que deseja visualizar: ");
            Long id = scanner.nextLong();
            scanner.nextLine();
            estadia = getEstadia(id, jwtToken, urlBase, restTemplate);
            if (estadia == null) {
                System.out.println("Esse id não existe.\nDeseja digitar outro id? (S/N)");
                String escolha = scanner.nextLine();
                if (escolha.equalsIgnoreCase("n")) {
                    return;
                }

            } else  {
                break;
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.printf(
                "%-20s %-40s %-15s %-15s%n",
                "Cliente", "Quarto", "Entrada", "Saída"
        );
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.printf(
                "%-20s %-40s %-15s %-15s%n",
                estadia.getUsuario().getNome(),
                estadia.getQuarto().getDescricao(),
                estadia.getDataEntrada().format(formatter),
                estadia.getDataSaida().format(formatter)
        );

    }

    public EstadiaDTO getEstadia (Long id, String jwtToken, String urlBase, RestTemplate restTemplate) {
        try {
            HttpHeaders headers = new HttpHeaders();
            if (jwtToken != null) headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = urlBase + ESTADIA_URL_PATH + "/" + id;

            ResponseEntity<EstadiaDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, EstadiaDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK) {
                return resposta.getBody();
            } else {
                System.out.println("Erro inesperado: status " + resposta.getStatusCode());
                return null;
            }
        } catch (HttpClientErrorException.Forbidden ex) {
            System.out.println("403: Forbidden - Sem permissão para acessar esse recurso.");
        } catch (HttpClientErrorException.Unauthorized ex) {
            System.out.println("401: Unauthorized - Token inválido ou ausente.");
        } catch (HttpClientErrorException.BadRequest ex) {
            System.out.println("400: Bad Request - Verifique os dados enviados.");
        } catch (HttpClientErrorException.NotFound ex) {
            System.out.println("404: Not Found - Estadia com ID " + id + " não encontrado.");
        }

        return null;
    }

    public void emitirNotaFiscal(Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {
        Locale real = Locale.of("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(real);

        System.out.print("Digite o login do cliente que deseja emitir a nota fiscal: ");
        String login = scanner.nextLine();

        UsuarioDTO cliente = getCliente(login, jwtToken, urlBase, restTemplate);
        if (cliente == null) {
            System.out.println("Usuário não existe/problema na requisição.");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requisicao = new HttpEntity<>(headers);

            String uri = String.format("%s/%s/nota-fiscal/%d", urlBase, ESTADIA_URL_PATH, cliente.getId());

            ResponseEntity<NotaFiscalDTO> resposta = restTemplate.exchange(
                    uri, HttpMethod.GET, requisicao, NotaFiscalDTO.class
            );

            if (resposta.getStatusCode() == HttpStatus.OK && resposta.getBody() != null) {
                NotaFiscalDTO notaFiscal = resposta.getBody();

                System.out.println("=================================================");
                System.out.println("NOTA FISCAL");
                System.out.println("=================================================");
                System.out.println("Nome: " + notaFiscal.getCliente().getNome());
                System.out.println("Telefone: " + notaFiscal.getCliente().getTelefone());
                System.out.println("=================================================");
                System.out.println("ESTADIAS");
                System.out.println("=================================================");

                System.out.printf(
                        "%-40s %-8s%n",
                        "Quarto", "Valor"
                );
                System.out.println("-------------------------------------------------");

                for (EstadiaDTO estadia : notaFiscal.getEstadias()) {
                    System.out.printf(
                            "%-40s %-8s%n",
                            estadia.getQuarto().getDescricao(),
                            nf.format(estadia.getQuarto().getValor())
                    );
                }

                System.out.println("=================================================");
                System.out.println("Total:\t" + nf.format(notaFiscal.getTotal()));
                System.out.println("=================================================");
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
        catch (HttpClientErrorException.NotFound | ResourceNotFound ex) {
            System.out.println("404: Not Found: " + ex.getMessage());
        }
    }
}
