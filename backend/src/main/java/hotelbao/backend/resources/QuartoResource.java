package hotelbao.backend.resources;

import hotelbao.backend.dto.QuartoDTO;
import hotelbao.backend.entity.Quarto;
import hotelbao.backend.exceptions.ResourceNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import hotelbao.backend.service.QuartoService;

import java.net.URI;

@RestController
@RequestMapping(value = "/quarto")
@Tag(name = "Quarto", description = "Controller para quarto")
public class QuartoResource {

    @Autowired
    private QuartoService quartoService;

    @GetMapping(produces = "application/json")
    @Operation(
            description = "Retorna todos os quartos de forma paginada",
            summary = "Retorna todos os quartos",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200")
            }
    )
    public ResponseEntity<Page<QuartoDTO>> findAll(Pageable pageable) {
        Page<QuartoDTO> quartos = quartoService.findAll(pageable);
        return ResponseEntity.ok().body(quartos);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Retorna um quarto a partir de seu ID",
            summary = "Retorna um quarto",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<QuartoDTO> findById(@PathVariable Long id) {
        try {
            QuartoDTO quartoDTO = quartoService.findById(id);
            return ResponseEntity.ok(quartoDTO);
        } catch (ResourceNotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(produces = "application/json")
    @Operation(
            description = "Insere um novo quarto no banco de dados.",
            summary = "Insere um novo quarto",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    public ResponseEntity<QuartoDTO> insert(@Valid @RequestBody Quarto dto) {
        Quarto entity = new Quarto();
        entity.setDescricao(dto.getDescricao());
        entity.setValor(dto.getValor());
        entity.setImagemUrl(dto.getImagemUrl());

        QuartoDTO savedDto = quartoService.save(entity);
        return ResponseEntity.created(URI.create("/quartos/" + savedDto.getId())).body(savedDto);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Atualiza os dados de um quarto com base em seu ID",
            summary = "Atualiza um quarto",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<QuartoDTO> update(@PathVariable Long id, @RequestBody QuartoDTO dto) {
        try {
            QuartoDTO updated = quartoService.update(id, dto);
            return ResponseEntity.ok(updated);
        }  catch (ResourceNotFound ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(value = "/{id}", produces = "application/json")
    @Operation(
            description = "Deleta um quarto do banco de dados com base em seu ID",
            summary = "Deleta um quarto",
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200"),
                    @ApiResponse(description = "Bad Request", responseCode = "400"),
                    @ApiResponse(description = "Unauthorized", responseCode = "401"),
                    @ApiResponse(description = "Forbidden", responseCode = "403"),
                    @ApiResponse(description = "Not found", responseCode = "404")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!quartoService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        quartoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

