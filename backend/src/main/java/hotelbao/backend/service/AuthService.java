package hotelbao.backend.service;

import hotelbao.backend.dto.EmailDTO;
import hotelbao.backend.dto.NewPasswordDTO;
import hotelbao.backend.dto.RequestTokenDTO;
import hotelbao.backend.entity.PasswordRecover;
import hotelbao.backend.entity.Usuario;
import hotelbao.backend.exceptions.ResourceNotFound;
import hotelbao.backend.repository.PasswordRecoverRepository;
import hotelbao.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    @Value("${email.password-recover.token.minutes}")
    private int tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String uri;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createRecoverToken (RequestTokenDTO dto) {
        /* Busca o usuário pelo email */
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());

        if (usuario == null) throw new ResourceNotFound("E-mail inválido.");

        /* Gera o token */
        String token = UUID.randomUUID().toString();

        /* Insere no banco de dados */
        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setToken(token);
        passwordRecover.setEmail(dto.getEmail());
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));

        passwordRecoverRepository.save(passwordRecover);

        /* Envia o email com o token incluso no corpo da mensagem */
        String body = "Acesse o link para definir uma nova senha (válido por " + tokenMinutes + " minutos).\n\n" + uri + token;

        emailService.sendEmail(new EmailDTO(usuario.getEmail(), "Recuperação de senha", body));
    }

    public void saveNewPassword (NewPasswordDTO dto) {
        List<PasswordRecover> list = passwordRecoverRepository.findValidToken(dto.getToken(), Instant.now());

        if (list.isEmpty()) throw new ResourceNotFound("Token não encontrado.");

        Usuario usuario = usuarioRepository.findByEmail(list.getFirst().getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getNewPassword()));
        usuarioRepository.save(usuario);
    }
}
