package hotelbao.backend.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.dto.QuartoDTO;
import hotelbao.backend.entity.Quarto;
import hotelbao.backend.exceptions.ResourceNotFound;
import hotelbao.backend.menu.OpcoesMenuAdmin;
import hotelbao.backend.menu.OpcoesMenuCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import hotelbao.backend.service.QuartoService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = QuartoResource.class)
@ComponentScan(basePackages = "resources")
class QuartoResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuartoService quartoService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuartoDTO quartoDTO;
    private Quarto quarto;

    @MockBean private OpcoesMenuAdmin menuAdmin;
    @MockBean private OpcoesMenuCliente menuCliente;

    @BeforeEach
    void setUp() {
        quartoDTO = new QuartoDTO();
        quartoDTO.setId(1L);
        quartoDTO.setDescricao("Quarto Standard com vista para o mar");
        quartoDTO.setValor(new BigDecimal("150.00"));
        quartoDTO.setImagemUrl("https://exemplo.com/quarto1.jpg");

        quarto = new Quarto();
        quarto.setId(1L);
        quarto.setDescricao("Quarto Standard com vista para o mar");
        quarto.setValor(new BigDecimal("150.00"));
        quarto.setImagemUrl("https://exemplo.com/quarto1.jpg");
    }

    @Test
    @WithMockUser(username = "teste", roles = {})
    @DisplayName("GET /quarto - Deve retornar uma lista paginada de usuários com status 200")
    void findAllShouldReturnPagedQuartos() throws Exception {
        List<QuartoDTO> quartos = Arrays.asList(quartoDTO);
        Page<QuartoDTO> page = new PageImpl<>(quartos);

        when(quartoService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/quarto")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].descricao").value("Quarto Standard com vista para o mar"))
                .andExpect(jsonPath("$.content[0].valor").value(150.00))
                .andExpect(jsonPath("$.content[0].imagemUrl").value("https://exemplo.com/quarto1.jpg"));

        verify(quartoService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "teste", roles = {})
    @DisplayName("GET /quarto - Deve retornar uma lista paginada vazia quando não houver quartos")
    void findAllShouldReturnEmptyPageWhenNoQuartosExist() throws Exception {
        Page<QuartoDTO> emptyPage = new PageImpl<>(List.of());
        when(quartoService.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/quarto")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(quartoService, times(1)).findAll(any(Pageable.class));
    }

    @DisplayName("GET /quarto/{id} - Deve retornar quarto pelo ID quando existir")
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @Test
    void findByIdShouldReturnQuartoWhenIdExists() throws Exception {
        when(quartoService.findById(1L)).thenReturn(quartoDTO);

        mockMvc.perform(get("/quarto/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Quarto Standard com vista para o mar"))
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.imagemUrl").value("https://exemplo.com/quarto1.jpg"));

        verify(quartoService, times(1)).findById(1L);
    }

    @DisplayName("GET /quarto/{id} - Deve retornar 404 quando quarto não for encontrado")
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @Test
    void findByIdShouldReturnErrorWhenQuartoNotFound() throws Exception {
        when(quartoService.findById(1L)).thenThrow(new ResourceNotFound("Quarto não encontrado"));
        mockMvc.perform(get("/quarto/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(quartoService, times(1)).findById(1L);
    }


    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("POST /quarto - Deve criar novo quarto com sucesso e retornar 201")
    @Test
    void inserShouldReturnCreatedQuarto() throws Exception {
        when(quartoService.save(any(Quarto.class))).thenReturn(quartoDTO);

        String quartoJson = objectMapper.writeValueAsString(quarto);
        mockMvc.perform(post("/quarto")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quartoJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "/quartos/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Quarto Standard com vista para o mar"))
                .andExpect(jsonPath("$.valor").value(150.00))
                .andExpect(jsonPath("$.imagemUrl").value("https://exemplo.com/quarto1.jpg"));

        verify(quartoService, times(1)).save(any(Quarto.class));
    }


    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("POST /quarto - Deve retornar 400 quando dados inválidos forem enviados")
    @Test
    void insertShouldReturnErrorWhenInvalidData() throws Exception {
        Quarto quartoInvalido = new Quarto();
        quartoInvalido.setDescricao(""); // Descrição vazia
        quartoInvalido.setValor(BigDecimal.valueOf(-50.00)); // Valor negativo
        quartoInvalido.setImagemUrl("url-invalida"); // URL inválida

        String quartoJson = objectMapper.writeValueAsString(quartoInvalido);

        when(quartoService.save(any(Quarto.class)))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        mockMvc.perform(post("/quarto")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quartoJson))
                .andExpect(status().isUnprocessableEntity());

        verify(quartoService, times(0)).save(any(Quarto.class));
    }


    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("PUT /quarto/{id} - Deve atualizar quarto com sucesso quando ID existir")
    @Test
    void updateShouldReturnUpdatedQuartoWhenIdExists() throws Exception {
        QuartoDTO updatedQuarto = new QuartoDTO();
        updatedQuarto.setId(1L);
        updatedQuarto.setDescricao("Quarto Deluxe com vista para a montanha");
        updatedQuarto.setValor(new BigDecimal("200.00"));
        updatedQuarto.setImagemUrl("https://exemplo.com/quarto-deluxe.jpg");

        when(quartoService.update(eq(1L), any(QuartoDTO.class))).thenReturn(updatedQuarto);

        String quartoJson = objectMapper.writeValueAsString(updatedQuarto);

        mockMvc.perform(put("/quarto/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quartoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Quarto Deluxe com vista para a montanha"))
                .andExpect(jsonPath("$.valor").value(200.00))
                .andExpect(jsonPath("$.imagemUrl").value("https://exemplo.com/quarto-deluxe.jpg"));

        verify(quartoService, times(1)).update(eq(1L), any(QuartoDTO.class));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("PUT /quarto/{id} - Deve atualizar somente o campo valor do quarto")
    void updateShouldUpdateOnlyValueField() throws Exception {

        QuartoDTO quartoAtualizarValor = new QuartoDTO();
        quartoAtualizarValor.setId(1L);
        quartoAtualizarValor.setDescricao("Quarto Standard com vista para o mar");
        quartoAtualizarValor.setValor(new BigDecimal("175.50"));
        quartoAtualizarValor.setImagemUrl("https://exemplo.com/quarto1.jpg");

        when(quartoService.update(eq(1L), any(QuartoDTO.class))).thenReturn(quartoAtualizarValor);

        String quartoJson = objectMapper.writeValueAsString(quartoAtualizarValor);

        mockMvc.perform(put("/quarto/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(quartoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(175.50));

        verify(quartoService, times(1)).update(eq(1L), any(QuartoDTO.class));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("DELETE /quarto/{id} - Deve deletar quarto com sucesso quando ID existir")
    void deleteShouldReturnNoContentWhenQuartoExists() throws Exception {
        when(quartoService.existsById(1L)).thenReturn(true);
        doNothing().when(quartoService).delete(1L);

        mockMvc.perform(delete("/quarto/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(quartoService, times(1)).existsById(1L);
        verify(quartoService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("DELETE /quarto/{id} - Deve retornar 404 ao tentar deletar quarto inexistente")
    void deleteShouldReturnNotFoundWhenQuartoDoesNotExist() throws Exception {
        when(quartoService.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/quarto/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(quartoService, times(1)).existsById(999L);
        verify(quartoService, never()).delete(anyLong());
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("GET /quarto/{id} - Deve retornar 400 quando ID informado for inválido (não numérico)")
    void findByIdShouldReturnBadRequestWhenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/quarto/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(quartoService, never()).findById(anyLong());
    }
}
