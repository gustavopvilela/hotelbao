package hotelbao.backend.resource;

import hotelbao.backend.dto.EstadiaDTO;
import hotelbao.backend.dto.NotaFiscalDTO;
import hotelbao.backend.service.EstadiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/estadia")
@Tag(name = "Estadia", description = "Controller para as estadias")
public class EstadiaResource {
    @Autowired
    private EstadiaService estadiaService;

    @GetMapping(produces = "application/json")
    @Operation(
        description = "Retorna todas as estadias e reservas feitas pelos clientes",
        summary = "Retorna todas as estadias",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403")
        }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<EstadiaDTO>> findAll (Pageable pageable) {
        Page<EstadiaDTO> estadias = estadiaService.findAll(pageable);
        return ResponseEntity.ok().body(estadias);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Retorna as informações de uma estadia com base em seu ID",
            summary = "Retorna uma estadia",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<EstadiaDTO> findById (@PathVariable Long id) {
        EstadiaDTO estadia = estadiaService.findById(id);
        return ResponseEntity.ok().body(estadia);
    }

    @PostMapping(produces = "application/json")
    @Operation(
            description = "Cria uma nova estadia no banco de dados",
            summary = "Cria uma nova estadia",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<EstadiaDTO> insert (@Valid @RequestBody EstadiaDTO dto) {
        EstadiaDTO estadia = estadiaService.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(estadia.getId())
                .toUri();

        return ResponseEntity.created(uri).body(estadia);
    }

    @PutMapping(value = "/{id}")
    @Operation(
            description = "Atualiza os dados de uma estadia com base em seu ID",
            summary = "Atualiza uma estadia",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<EstadiaDTO> update (@PathVariable Long id, @Valid @RequestBody EstadiaDTO estadiaDTO) {
        EstadiaDTO estadia = estadiaService.update(id, estadiaDTO);
        return ResponseEntity.ok().body(estadia);
    }

    @DeleteMapping("/{id}")
    @Operation(
            description = "Deleta uma estadia do banco de dados baseado em seu ID",
            summary = "Deleta uma estadia",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        estadiaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/cliente/{id}")
    @Operation(
            description = "Retorna todas as informações de todas as estadias baseado no ID do cliente",
            summary = "Retorna as estadias de um cliente",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<List<EstadiaDTO>> findByClienteId (@PathVariable Long id) {
        List<EstadiaDTO> estadias = estadiaService.findByClienteId(id);
        return ResponseEntity.ok().body(estadias);
    }

    @GetMapping("/maior/{id}")
    @Operation(
            description = "Retorna a estadia mais cara que um cliente já ficou",
            summary = "Retorna a estadia mais cara de um cliente",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<EstadiaDTO> findEstadiaDeMaiorValorByClienteId (@PathVariable Long id) {
        EstadiaDTO estadia = estadiaService.findEstadiaDeMaiorValorByClienteId(id);
        return ResponseEntity.ok().body(estadia);
    }

    @GetMapping("/menor/{id}")
    @GetMapping("/maior/{id}")
    @Operation(
            description = "Retorna a estadia mais barata que um cliente já ficou",
            summary = "Retorna a estadia mais barata de um cliente",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<EstadiaDTO> findEstadiaDeMenorValorByClienteId (@PathVariable Long id) {
        EstadiaDTO estadia = estadiaService.findEstadiaDeMenorValorByClienteId(id);
        return ResponseEntity.ok().body(estadia);
    }


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

    @GetMapping("/nota-fiscal/{id}")
    @Operation(
            description = "Retorna uma nota fiscal com os seguintes dados: cliente, todas as estadias que ficou e valor total dessas estadias",
            summary = "Emite uma nota fiscal para o cliente",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_CLIENTE')")
    public ResponseEntity<NotaFiscalDTO> emitirNotaFiscal (@PathVariable Long id) {
        NotaFiscalDTO notaFiscal = estadiaService.emitirNotaFiscal(id);
        return ResponseEntity.ok().body(notaFiscal);
    }
}
