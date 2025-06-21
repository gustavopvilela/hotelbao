package hotelbao.backend.service;

import hotelbao.backend.dto.RoleDTO;
import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.dto.UsuarioInsertDTO;
import hotelbao.backend.entity.Role;
import hotelbao.backend.entity.Usuario;
import hotelbao.backend.exceptions.DatabaseException;
import hotelbao.backend.exceptions.ResourceNotFound;
import hotelbao.backend.resource.UsuarioResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotelbao.backend.projection.UserDetailsProjection;
import hotelbao.backend.repository.RoleRepository;
import hotelbao.backend.repository.UsuarioRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> findAll (Pageable pageable) {
        Page<Usuario> list = usuarioRepository.findAll(pageable);
        return list.map(
                u -> new UsuarioDTO(u)
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withSelfRel())
                .add(linkTo(methodOn(UsuarioResource.class).findById(u.getId())).withRel("Informações do usuário"))
        );
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById (Long id) {
        Optional<Usuario> opt = usuarioRepository.findById(id);
        Usuario usuario = opt.orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));
        return new UsuarioDTO(usuario)
                .add(linkTo(methodOn(UsuarioResource.class).findById(usuario.getId())).withSelfRel())
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(UsuarioResource.class).update(usuario.getId(), null)).withRel("Atualizar usuário"))
                .add(linkTo(methodOn(UsuarioResource.class).delete(usuario.getId())).withRel("Deletar usuário"));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findByLogin (String login) {
        Optional<Usuario> opt = usuarioRepository.findByLogin(login);
        Usuario usuario = opt.orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));
        return new UsuarioDTO(usuario)
                .add(linkTo(methodOn(UsuarioResource.class).findById(usuario.getId())).withSelfRel())
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(UsuarioResource.class).update(usuario.getId(), null)).withRel("Atualizar usuário"))
                .add(linkTo(methodOn(UsuarioResource.class).delete(usuario.getId())).withRel("Deletar usuário"));
    }

    @Transactional
    public UsuarioDTO insert (UsuarioInsertDTO dto) {
        Usuario entity = new Usuario();
        this.copiarDTOParaEntidade(dto, entity);
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        //entity.setSenha(dto.getSenha());
        Usuario novo = usuarioRepository.save(entity);
        return new UsuarioDTO(novo)
                .add(linkTo(methodOn(UsuarioResource.class).findById(entity.getId())).withRel("Encontrar usuário por ID"))
                .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(UsuarioResource.class).update(entity.getId(), null)).withRel("Atualizar usuário"))
                .add(linkTo(methodOn(UsuarioResource.class).delete(entity.getId())).withRel("Deletar usuário"));
    }

    @Transactional
    public UsuarioDTO update (Long id, UsuarioInsertDTO dto) {
        try {
            Usuario entity = usuarioRepository.getReferenceById(id);
            this.copiarDTOParaEntidade(dto, entity);
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
            //entity.setSenha(dto.getSenha());
            entity = usuarioRepository.save(entity);
            return new UsuarioDTO(entity)
                    .add(linkTo(methodOn(UsuarioResource.class).findById(dto.getId())).withRel("Encontrar usuário por ID"))
                    .add(linkTo(methodOn(UsuarioResource.class).findAll(null)).withRel("Todos os usuários"))
                    .add(linkTo(methodOn(UsuarioResource.class).delete(dto.getId())).withRel("Deletar usuário"));
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

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = usuarioRepository.findUserAndRoleByLogin(username);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        Usuario usuario = new Usuario();
        usuario.setLogin(result.get(0).getUsername());
        usuario.setSenha(result.get(0).getPassword());
        for (UserDetailsProjection p : result) {
            usuario.addRole(new Role(p.getRoleId(), p.getAuthority()));
        }

        System.out.println(usuario);

        return usuario;
    }

    public UsuarioDTO signUp (UsuarioInsertDTO dto) {
        Usuario entity = new Usuario();
        this.copiarDTOParaEntidade(dto, entity);

        Role role = roleRepository.findByAuthority("ROLE_CLIENTE"); // Papel: cliente

        entity.getRoles().clear();
        entity.getRoles().add(role);
        entity.setSenha(passwordEncoder.encode(dto.getTelefone())); // Senha padrão: telefone

        Usuario novo = usuarioRepository.save(entity);
        return new UsuarioDTO(novo);
    }

    @Transactional
    public Page<UsuarioDTO> findAllClients (Pageable pageable) {
        Page<Usuario> list = usuarioRepository.findAllClients(pageable);
        return list.map(
                u -> new UsuarioDTO(u)
                        .add(linkTo(methodOn(UsuarioResource.class).findAllClients(null)).withSelfRel())
                        .add(linkTo(methodOn(UsuarioResource.class).findById(u.getId())).withRel("Informações do cliente"))
        );
    }


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