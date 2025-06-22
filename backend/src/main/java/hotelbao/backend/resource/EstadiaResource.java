package hotelbao.backend.resource;

import hotelbao.backend.service.EstadiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/estadia")
@Tag(name = "Estadia", description = "Controller para as estadias")
public class EstadiaResource {
    @Autowired
    private EstadiaService estadiaService;

    @GetMapping(value = "/total/{id}", produces = "application/json")
    @Operation(
        description = "Retorna a soma de todas as estadias já feitas pelo cliente, incluindo as que estão reservadas para datas futuras",
        summary = "Valor total das estadias de um cliente",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403"),
            @ApiResponse(description = "Not found", responseCode = "404")
        }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<Map<String, Long>> totalEstadiasCliente (@PathVariable Long id) {
        Long total = estadiaService.totalEstadiasCliente(id);
        Map<String, Long> resposta = Map.of("valor_total_estadias", total);
        return ResponseEntity.ok().body(resposta);
    }
}
