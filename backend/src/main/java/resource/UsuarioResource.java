package resource;

import dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UsuarioService;

@RestController
@RequestMapping(value = "/usuario")
@Tag(name = "Usuário", description = "Controller para usuário")
public class UsuarioResource {
    @Autowired
    private UsuarioService usuarioService;

    public ResponseEntity<UsuarioDTO> findAll () {
        return null;
    }

    public ResponseEntity<UsuarioDTO> findById (Long id) {
        return null;
    }

    public ResponseEntity<UsuarioDTO> insert () {
        return null;
    }

    public ResponseEntity<UsuarioDTO> update () {
        return null;
    }

    public ResponseEntity<Void> delete (Long id) {
        return null;
    }
}
