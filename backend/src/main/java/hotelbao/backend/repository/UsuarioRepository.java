package hotelbao.backend.repository;

import hotelbao.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import hotelbao.backend.projection.UserDetailsProjection;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByLogin (String login);
    Usuario findByEmail (String email);
    Usuario findByLoginAndSenha (String login, String senha);
    Usuario findByEmailAndSenha (String email, String senha);

    @Query(nativeQuery = true, value = """
        SELECT u.login as username,
               u.senha,
               r.id as roleId,
               r.authority
        FROM usuario u
        INNER JOIN usuario_role ur ON u.id = ur.usuario_id
        INNER JOIN role r ON r.id = ur.role_id
        WHERE u.login = :username
    """)
    List<UserDetailsProjection> findUserAndRoleByLogin (String username);
}
