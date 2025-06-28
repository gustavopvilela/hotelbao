package hotelbao.backend.menu;

import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        Scanner scanner = new Scanner(System.in);
        OpcoesMenuAdmin menuAdmin = new OpcoesMenuAdmin();
        OpcoesMenuCliente menuCliente = new OpcoesMenuCliente();

        AppRunner appRunner = new AppRunner(restTemplate, scanner, menuAdmin, menuCliente);

        appRunner.executarMenuPrincipal();

        System.out.println("\n\n\n==== SISTEMA ENCERRADO ====");
    }
}
