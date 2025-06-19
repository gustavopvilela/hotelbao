package hotelbao.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioInsertDTO extends UsuarioDTO {
    @NotBlank
    @Size(min = 8, max = 64)
    private String senha;

    public UsuarioInsertDTO () {
        super();
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
