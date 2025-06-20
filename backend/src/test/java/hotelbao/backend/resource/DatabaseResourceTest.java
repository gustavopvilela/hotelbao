package hotelbao.backend.resource;

import hotelbao.backend.service.DatabaseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.net.URI;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseService databaseService;

    @Test
    @DisplayName("DELETE /database/clear - autorizado como ADMIN deve retornar No Content")
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    void clearDatabaseBeingAnAdminShouldReturnNoContent () throws Exception {
        mockMvc.perform(
                delete(URI.create("/database/clear"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(databaseService, times(1)).limparBancoDeDados();
    }

    @Test
    @DisplayName("DELETE /database/clear - autorizado como CLIENTE deve retornar Forbidden")
    @WithMockUser(username = "teste", roles = {"CLIENTE"})
    void clearDatabaseBeingAClintShouldReturnForbidden () throws Exception {
        mockMvc.perform(
                delete("/database/clear")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // Verifica que o serviço não foi chamado
        verify(databaseService, never()).limparBancoDeDados();
    }

    @Test
    @DisplayName("DELETE /database/clear - sem autenticação deve retornar 401 Unauthorized")
    void debugSecurityConfiguration() throws Exception {
        mockMvc.perform(
                delete("/database/clear")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
