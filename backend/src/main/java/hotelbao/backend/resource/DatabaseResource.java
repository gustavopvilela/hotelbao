package hotelbao.backend.resource;

import hotelbao.backend.service.DatabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
@Tag(name = "Banco de dados", description = "Controller para manipulação do banco de dados")
public class DatabaseResource {
    @Autowired
    private DatabaseService databaseService;


    @DeleteMapping("/clear")
    @Operation(
            description = "Deleta todos os dados: usuários, roles, quartos, estadias e tokens de recuperação de senha",
            summary = "Limpa o banco de dados",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> limparBancoDeDados () {
        databaseService.limparBancoDeDados();
        return ResponseEntity.noContent().build();
    }
}
