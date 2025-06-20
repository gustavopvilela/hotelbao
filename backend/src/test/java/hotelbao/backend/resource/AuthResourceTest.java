package hotelbao.backend.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.dto.NewPasswordDTO;
import hotelbao.backend.dto.RequestTokenDTO;
import hotelbao.backend.service.AuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AuthResource.class)
public class AuthResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestTokenDTO requestTokenDTO;
    private NewPasswordDTO newPasswordDTO;

    @BeforeEach
    void setUp () {
        requestTokenDTO = new RequestTokenDTO();
        requestTokenDTO.setEmail("usuario@teste.com");

        newPasswordDTO = new NewPasswordDTO();
        newPasswordDTO.setNewPassword("12345678");
        newPasswordDTO.setToken("54abee49-d13d-4b0d-bbb6-5fc8d9cfe600");
    }

    @Test
    @WithMockUser(username = "teste", roles = {})
    @DisplayName("POST /auth/recover-token - Deve executar o serviço e retornar No Content")
    void createRecoverTokenShouldReturnNoContent () throws Exception {
        doNothing().when(authService).createRecoverToken(any(RequestTokenDTO.class));

        mockMvc.perform(
                post("/auth/recover-token")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestTokenDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "teste", roles = {})
    @DisplayName("POST /auth/new-password - Deve executar o serviço e retornar No Content")
    void createNewPasswordShouldReturnNoContent () throws Exception {
        doNothing().when(authService).saveNewPassword(any(NewPasswordDTO.class));

        mockMvc.perform(
                post("/auth/new-password")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPasswordDTO)))
                .andExpect(status().isNoContent());
    }
}
