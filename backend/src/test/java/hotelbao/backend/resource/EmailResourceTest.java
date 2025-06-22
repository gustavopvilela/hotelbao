package hotelbao.backend.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.dto.EmailDTO;
import hotelbao.backend.menu.OpcoesMenuAdmin;
import hotelbao.backend.menu.OpcoesMenuCliente;
import hotelbao.backend.service.EmailService;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(EmailResource.class)
public class EmailResourceTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean private EmailService emailService;
    @MockBean private OpcoesMenuAdmin menuAdmin;
    @MockBean private OpcoesMenuCliente menuCliente;
    
    @Autowired
    private ObjectMapper objectMapper;

    private EmailDTO emailDTOValido;
    private EmailDTO emailDTOInvalido;
    
    @BeforeEach
    public void setUp () {
        emailDTOValido = new EmailDTO();
        emailDTOValido.setTo("cliente@exemplo.com");
        emailDTOValido.setSubject("Assunto do e-mail");
        emailDTOValido.setBody("Corpo do e-mail");

        emailDTOInvalido = new EmailDTO();
        emailDTOInvalido.setTo(null);
        emailDTOInvalido.setSubject("Assunto do e-mail inválido");
        emailDTOInvalido.setBody("Corpo do e-mail inválido");
    }
    
    @Test
    @WithMockUser(username = "teste", roles = {})
    @DisplayName("POST /email - Deve executar o serviço e retornar No Content")
    void sendEmailShouldReturnNoContent () throws Exception {
        doNothing().when(emailService).sendEmail(any(EmailDTO.class));
        
        mockMvc.perform(
                post("/email")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailDTOValido)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "teste", roles = {})
    @DisplayName("POST /email - Deve retornar bad request quando o e-mail é inválido")
    void sendEmailShouldReturnBadRequestWhenInvalid () throws Exception {
        mockMvc.perform(
                post("/email")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTOInvalido))
        ).andExpect(status().isUnprocessableEntity());
    }
}
