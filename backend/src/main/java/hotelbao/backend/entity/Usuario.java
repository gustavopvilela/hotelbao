package hotelbao.backend.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario")
public class Usuario /* implements UserDetails */ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String login;
    private String telefone;

    /* Relação com Estadia. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Estadia> estadias = new HashSet<>();

    /* Relação com as roles. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "usuario_role",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String login, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.login = login;
        this.telefone = telefone;
    }

    public Usuario (Usuario entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        this.senha = entity.getSenha();
        this.login = entity.getLogin();
        this.telefone = entity.getTelefone();
    }

    public Usuario (Usuario usuario, Set<Role> roles) {
        this(usuario);
        this.roles = roles;
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

    public String getSenha() {
        return senha;
    }

    //@Override
    public String getPassword() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    //@Override
    public String getUsername() {
        return login;
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

    public Set<Estadia> getEstadias() {
        return estadias;
    }

    public void setEstadias(Set<Estadia> estadias) {
        this.estadias = estadias;
    }

    //@Override
//    public Collection<? extends GrantedAuthority> getAuthorities () {
//        return roles;
//    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole (Role role) {
        this.roles.add(role);
    }

    public boolean hasRole (String roleName) {
        return !roles
                .stream()
                .filter(r -> r.getAuthority().equals(roleName))
                .toList()
                .isEmpty();
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", login='" + login + '\'' +
                ", telefone='" + telefone + '\'' +
                ", estadias=" + estadias +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
