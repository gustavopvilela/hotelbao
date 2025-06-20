package hotelbao.backend.service;

import hotelbao.backend.repository.EstadiaRepository;
import hotelbao.backend.repository.PasswordRecoverRepository;
import hotelbao.backend.repository.RoleRepository;
import hotelbao.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordRecoverRepository passwordRecoverRepository;
    @Autowired private EstadiaRepository estadiaRepository;
    /* TODO: adicionar o de quartos aqui. */

    @Transactional
    public void limparBancoDeDados () {
        usuarioRepository.deleteAll();
        roleRepository.deleteAll();
        passwordRecoverRepository.deleteAll();
        estadiaRepository.deleteAll();
    }
}
