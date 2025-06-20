package hotelbao.backend.resource;

import hotelbao.backend.dto.NewPasswordDTO;
import hotelbao.backend.dto.RequestTokenDTO;
import hotelbao.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@Tag(name = "Autenticação", description = "Controller para autenticação")
public class AuthResource {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/recover-token")
    @Operation(
            description = "Envia um e-mail ao usuário contendo um token para recuperação de senha",
            summary = "Gera um token para recuperar a senha",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<Void> createRecoverToken (@Valid @RequestBody RequestTokenDTO dto) {
        authService.createRecoverToken(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/new-password")
    @Operation(
            description = "Valida e persiste a nova senha enviada pelo usuário usando o token de recuperação",
            summary = "Salva a nova senha do usuário",
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<Void> saveNewPassword (@Valid @RequestBody NewPasswordDTO dto) {
        authService.saveNewPassword(dto);
        return ResponseEntity.noContent().build();
    }
}
