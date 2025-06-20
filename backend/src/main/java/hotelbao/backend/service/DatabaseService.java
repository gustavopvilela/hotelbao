package hotelbao.backend.service;

import hotelbao.backend.repository.PasswordRecoverRepository;
import hotelbao.backend.repository.RoleRepository;
import hotelbao.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordRecoverRepository passwordRecoverRepository;
    /* TODO: quando adicionar os outros repositories, colocar aqui. */

    @Transactional
    public void limparBancoDeDados () {
        usuarioRepository.deleteAll();
        roleRepository.deleteAll();
        passwordRecoverRepository.deleteAll();
    }
}
