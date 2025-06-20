package hotelbao.backend.repository;

import hotelbao.backend.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        SELECT u.login AS username,
               u.senha AS password,
               r.id as roleId,
               r.authority
        FROM usuario u
        INNER JOIN usuario_role ur ON u.id = ur.usuario_id
        INNER JOIN role r ON r.id = ur.role_id
        WHERE u.login = :username
    """)
    List<UserDetailsProjection> findUserAndRoleByLogin (String username);

    @Query(nativeQuery = true,
    value = """
        SELECT u.id, u.email, u.login, u.nome, u.senha, u.telefone
        FROM usuario u
        INNER JOIN usuario_role ur ON (u.id = ur.usuario_id)
        INNER JOIN role r ON (r.id = ur.role_id)
        WHERE ur.role_id = (SELECT id FROM role WHERE role.authority = 'ROLE_CLIENTE');
    """,
    countQuery = """
        SELECT COUNT(*)
        FROM usuario u
        INNER JOIN usuario_role ur ON (u.id = ur.usuario_id)
        INNER JOIN role r ON (r.id = ur.role_id)
        WHERE ur.role_id = (SELECT id FROM role WHERE role.authority = 'ROLE_CLIENTE'); 
    """)
    Page<Usuario> findAllClients (Pageable pageable);
}
