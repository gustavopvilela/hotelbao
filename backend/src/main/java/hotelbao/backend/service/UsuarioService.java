package hotelbao.backend.service;

import hotelbao.backend.dto.RoleDTO;
import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.dto.UsuarioInsertDTO;
import hotelbao.backend.entity.Role;
import hotelbao.backend.entity.Usuario;
import hotelbao.backend.exceptions.DatabaseException;
import hotelbao.backend.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import projection.UserDetailsProjection;
import hotelbao.backend.repository.RoleRepository;
import hotelbao.backend.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class UsuarioService /*implements UserDetailsService*/ {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findAll (Pageable pageable) {
        Page<Usuario> list = usuarioRepository.findAll(pageable);
        return list.map(UsuarioDTO::new);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById (Long id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        Usuario usuario = opt.orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));
        return new UsuarioDTO(usuario);
    }

    @Transactional
    public UsuarioDTO insert (UsuarioInsertDTO dto) {
        Usuario entity = new Usuario();
        this.copiarDTOParaEntidade(dto, entity);
        //entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        entity.setSenha(dto.getSenha());
        Usuario novo = usuarioRepository.save(entity);
        return new UsuarioDTO(novo);
    }

    @Transactional
    public UsuarioDTO update (Long id, UsuarioInsertDTO dto) {
        try {
            Usuario entity = usuarioRepository.getReferenceById(id);
            this.copiarDTOParaEntidade(dto, entity);
            //entity.setSenha(passwordEncoder.encode(dto.getSenha()));
            entity.setSenha(dto.getSenha());
            entity = usuarioRepository.save(entity);
            return new UsuarioDTO(entity);
        }
        catch (EntityNotFoundException ex) {
            throw new ResourceNotFound("Usuário não encontrado: ID" + id);
        }
    }

    @Transactional
    public void delete (Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFound("Usuário não encontrado: ID" + id);
        }

        try {
            usuarioRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Violação de integridade: " + ex.getMessage());
        }
    }

//    @Override
//    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
//        List<UserDetailsProjection> result = usuarioRepository.findUserAndRoleByLogin(username);
//
//        if (result.isEmpty()) {
//            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
//        }
//
//        Usuario usuario = new Usuario();
//        usuario.setLogin(result.get(0).getUsername());
//        usuario.setSenha(result.get(0).getPassword());
//        for (UserDetailsProjection p : result) {
//            usuario.addRole(new Role(p.getRoleId(), p.getAuthority()));
//        }
//
//        return usuario;
//    }


    private void copiarDTOParaEntidade (UsuarioDTO dto, Usuario entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setLogin(dto.getLogin());
        entity.setTelefone(dto.getTelefone());

        entity.getRoles().clear();
        for (RoleDTO role : dto.getRoles()) {
            Role r = roleRepository.getReferenceById(role.getId());
            entity.getRoles().add(r);
        }
    }
}