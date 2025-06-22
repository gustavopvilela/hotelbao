package hotelbao.backend.menu;

import hotelbao.backend.dto.UsuarioDTO;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class OpcoesMenuCliente {
    public void fazerReserva (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void listarReservasCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void estadiaMaiorValorCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void estadiaMenorValorCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void totalEstadiasCliente (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void recuperarSenha (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}

    public void resetarSenha (UsuarioDTO cliente, Scanner scanner, String jwtToken, String urlBase, RestTemplate restTemplate) {}
}
