package hotelbao.backend.dto;

import hotelbao.backend.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    private Long id;
    @NotBlank(message = "Campo obrigatório.")
    private String nome;
    @Email(message = "Insira um e-mail válido.")
    private String email;
    @NotBlank(message = "Campo obrigatório.")
    private String login;
    private String telefone;
    private Set<RoleDTO> roles = new HashSet<>();

    public UsuarioDTO () {}

    public UsuarioDTO (Long id, String nome, String email, String telefone, String login) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.login = login;
    }

    public UsuarioDTO (Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.login = usuario.getLogin();

        usuario.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", telefone='" + telefone + '\'' +
                ", roles=" + roles +
                '}';
    }
}
