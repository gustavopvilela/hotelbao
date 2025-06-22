package hotelbao.backend.resource;

import hotelbao.backend.menu.OpcoesMenuAdmin;
import hotelbao.backend.menu.OpcoesMenuCliente;
import hotelbao.backend.service.EstadiaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Testes para EstadiaResource")
@WebMvcTest(EstadiaResource.class)
class EstadiaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private EstadiaService estadiaService;
    @MockBean private OpcoesMenuAdmin menuAdmin;
    @MockBean private OpcoesMenuCliente menuCliente;

    @Test
    @DisplayName("GET /estadia/{id} - Deve retornar total de estadias do cliente com sucesso com ROLE_ADMIN")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void WithAdminRoleShouldReturnTotalOfTheStaysSuccessfully() throws Exception {
        Long clienteId = 1L;
        Long valorTotalEsperado = 380L;

        when(estadiaService.totalEstadiasCliente(clienteId)).thenReturn(valorTotalEsperado);

        mockMvc.perform(get("/estadia/total/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valor_total_estadias").value(380));
    }

    @Test
    @DisplayName("GET /estadia/{id} - Deve retornar total de estadias do cliente com sucesso com ROLE_CLIENTE")
    @WithMockUser(authorities = "ROLE_CLIENTE")
    void WithClientRoleShouldReturnTotalOfTheStaysSuccessfully() throws Exception {
        Long clienteId = 2L;
        Long valorTotalEsperado = 1250L;

        when(estadiaService.totalEstadiasCliente(clienteId)).thenReturn(valorTotalEsperado);

        mockMvc.perform(get("/estadia/total/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valor_total_estadias").value(1250));
    }

    @Test
    @DisplayName("GET /estadia/{id} - Deve retornar 0 (zero) quando cliente não tem estadias")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void ShouldReturnZeroWhenClientHasNoStays() throws Exception {
        Long clienteId = 3L;
        Long valorTotalEsperado = 0L;

        when(estadiaService.totalEstadiasCliente(clienteId)).thenReturn(valorTotalEsperado);

        mockMvc.perform(get("/estadia/total/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valor_total_estadias").value(0));
    }

    @Test
    @DisplayName("GET /estadia/{id} - Deve retornar Unauthorized quando usuário não está autenticado")
    void ShouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/estadia/total/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /estadia/{id} - Deve retornar Bad Request quando ID é inválido")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void ShouldReturnBadRequestWhenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/estadia/total/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}