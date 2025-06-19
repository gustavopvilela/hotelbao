package hotelbao.backend.resource;

import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.dto.UsuarioInsertDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import hotelbao.backend.service.UsuarioService;

import java.net.URI;

@RestController
@RequestMapping(value = "/usuario")
@Tag(name = "Usuário", description = "Controller para usuário")
public class UsuarioResource {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(produces = "application/json")
    @Operation(
        description = "Retorna todos os usuários de forma paginada",
        summary = "Retorna todos os usuários",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200")
        }
    )
    public ResponseEntity<Page<UsuarioDTO>> findAll (Pageable pageable) {
        Page<UsuarioDTO> usuarios = usuarioService.findAll(pageable);
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(
        description = "Retorna um único usuário baseado em seu ID",
        summary = "Retorna um usuário",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Not found", responseCode = "404")
        }
    )
    public ResponseEntity<UsuarioDTO> findById (@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping(produces = "application/json")
    @Operation(
        description = "Cria um novo usuário no banco de dados",
        summary = "Cria um novo usuário",
        responses = {
            @ApiResponse(description = "Created", responseCode = "201"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403")
        }
    )
    public ResponseEntity<UsuarioDTO> insert (@Valid @RequestBody UsuarioInsertDTO dto) {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        UsuarioDTO usuario = usuarioService.insert(dto);
        return ResponseEntity.created(uri).body(usuario);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(
        description = "Atualiza os dados de um usuário com base em seu ID",
        summary = "Atualiza um usuário",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403"),
            @ApiResponse(description = "Not found", responseCode = "404")
        }
    )
    public ResponseEntity<UsuarioDTO> update (@PathVariable Long id, @Valid @RequestBody UsuarioInsertDTO dto) {
        UsuarioDTO usuario = usuarioService.update(id, dto);
        return ResponseEntity.ok().body(usuario);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
        description = "Deleta um usuário do banco de dados baseado em seu ID",
        summary = "Deleta um usuário",
        responses = {
            @ApiResponse(description = "OK", responseCode = "200"),
            @ApiResponse(description = "Bad Request", responseCode = "400"),
            @ApiResponse(description = "Unauthorized", responseCode = "401"),
            @ApiResponse(description = "Forbidden", responseCode = "403"),
            @ApiResponse(description = "Not found", responseCode = "404")
        }
    )
    public ResponseEntity<Void> delete (@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
