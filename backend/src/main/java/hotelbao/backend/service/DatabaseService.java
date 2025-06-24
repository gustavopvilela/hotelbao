package hotelbao.backend.service;

import hotelbao.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatabaseService {
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordRecoverRepository passwordRecoverRepository;
    @Autowired private EstadiaRepository estadiaRepository;
    @Autowired private QuartoRepository quartoRepository;

    @Transactional
    public void limparBancoDeDados () {
        usuarioRepository.deleteAll();
        roleRepository.deleteAll();
        passwordRecoverRepository.deleteAll();
        estadiaRepository.deleteAll();
        quartoRepository.deleteAll();
    }
}
