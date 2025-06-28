package hotelbao.backend.menu;

import hotelbao.backend.BackendApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class CLI {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(BackendApplication.class)
                .web(WebApplicationType.NONE) // Não inicia o servidor web, aqui roda só o menu
                .bannerMode(Banner.Mode.OFF) // Tira aquele banner enorme do Spring
                .run(args);

        // Pega o componente AppRunner
        AppRunner appRunner = context.getBean(AppRunner.class);

        // Executa o menu principal
        appRunner.executarMenuPrincipal();

        // Quando terminar, fecha o contexto fornecido pelo Spring
        context.close();
    }
}
