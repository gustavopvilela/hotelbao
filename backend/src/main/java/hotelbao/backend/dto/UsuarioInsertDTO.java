package hotelbao.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioInsertDTO extends UsuarioDTO {
    @Size(min = 2, max = 64)
    private String senha;

    public UsuarioInsertDTO () {
        super();
    }

    public UsuarioInsertDTO (UsuarioDTO usuario) {
        this.setId(usuario.getId());
        this.setNome(usuario.getNome());
        this.setLogin(usuario.getLogin());
        this.setEmail(usuario.getEmail());
        this.setTelefone(usuario.getTelefone());
        this.setRoles(usuario.getRoles());
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
