package hotelbao.backend.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.dto.EstadiaDTO;
import hotelbao.backend.dto.NotaFiscalDTO;
import hotelbao.backend.dto.QuartoDTO;
import hotelbao.backend.dto.RoleDTO;
import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.menu.OpcoesMenuAdmin;
import hotelbao.backend.menu.OpcoesMenuCliente;
import hotelbao.backend.service.EstadiaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadiaResource.class)
class EstadiaResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadiaService estadiaService;

    @MockBean private OpcoesMenuAdmin menuAdmin;
    @MockBean private OpcoesMenuCliente menuCliente;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO clienteDTO;
    private QuartoDTO quartoDTO;
    private EstadiaDTO estadiaDTO;
    private NotaFiscalDTO notaFiscalDTO;
    private RoleDTO roleDTO;

    @BeforeEach
    public void setUp() {
        // Setup Role
        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setAuthority("ROLE_CLIENTE");

        Set<RoleDTO> roles = new HashSet<>();
        roles.add(roleDTO);

        // Setup Cliente
        clienteDTO = new UsuarioDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNome("João Silva");
        clienteDTO.setEmail("joao.silva@email.com");
        clienteDTO.setLogin("joao.silva");
        clienteDTO.setTelefone("(11) 99999-9999");
        clienteDTO.setRoles(roles);

        // Setup Quarto
        quartoDTO = new QuartoDTO();
        quartoDTO.setId(1L);
        quartoDTO.setDescricao("Quarto Standard");
        quartoDTO.setValor(BigDecimal.valueOf(150.00));
        quartoDTO.setImagemUrl("https://exemplo.com/quarto1.jpg");

        // Setup Estadia
        estadiaDTO = new EstadiaDTO();
        estadiaDTO.setId(1L);
        estadiaDTO.setDataEntrada(LocalDate.of(2025, 7, 1));
        estadiaDTO.setDataSaida(LocalDate.of(2025, 7, 5));
        estadiaDTO.setCliente(clienteDTO);
        estadiaDTO.setQuarto(quartoDTO);

        // Setup Nota Fiscal
        notaFiscalDTO = new NotaFiscalDTO();
        notaFiscalDTO.setCliente(clienteDTO);
        notaFiscalDTO.setEstadias(Collections.singletonList(estadiaDTO));
        notaFiscalDTO.setTotal(BigDecimal.valueOf(600L));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void findAllShouldReturnListOfAllStays() throws Exception {
        // Arrange
        Page<EstadiaDTO> page = new PageImpl<>(Arrays.asList(estadiaDTO), PageRequest.of(0, 10), 1);
        when(estadiaService.findAll(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/estadia")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].cliente.nome").value("João Silva"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void findByIdSohuldReturnOneStay() throws Exception {
        // Arrange
        when(estadiaService.findById(1L)).thenReturn(estadiaDTO);

        // Act & Assert
        mockMvc.perform(get("/estadia/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cliente.nome").value("João Silva"))
                .andExpect(jsonPath("$.quarto.descricao").value("Quarto Standard"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_CLIENTE")
    void insertShouldReturnInsertedStay() throws Exception {
        // Arrange
        EstadiaDTO estadiaInput = new EstadiaDTO();
        estadiaInput.setDataEntrada(LocalDate.of(2025, 8, 1));
        estadiaInput.setDataSaida(LocalDate.of(2025, 8, 3));
        estadiaInput.setCliente(clienteDTO);
        estadiaInput.setQuarto(quartoDTO);

        EstadiaDTO estadiaOutput = new EstadiaDTO();
        estadiaOutput.setId(2L);
        estadiaOutput.setDataEntrada(LocalDate.of(2025, 8, 1));
        estadiaOutput.setDataSaida(LocalDate.of(2025, 8, 3));
        estadiaOutput.setCliente(clienteDTO);
        estadiaOutput.setQuarto(quartoDTO);

        when(estadiaService.insert(any(EstadiaDTO.class))).thenReturn(estadiaOutput);

        // Act & Assert
        mockMvc.perform(post("/estadia")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadiaInput)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.cliente.email").value("joao.silva@email.com"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateShouldReturnUpdatedStay() throws Exception {
        // Arrange
        EstadiaDTO estadiaInput = new EstadiaDTO();
        estadiaInput.setDataEntrada(LocalDate.of(2025, 7, 2));
        estadiaInput.setDataSaida(LocalDate.of(2025, 7, 6));
        estadiaInput.setCliente(clienteDTO);
        estadiaInput.setQuarto(quartoDTO);

        EstadiaDTO estadiaOutput = new EstadiaDTO();
        estadiaOutput.setId(1L);
        estadiaOutput.setDataEntrada(LocalDate.of(2025, 7, 2));
        estadiaOutput.setDataSaida(LocalDate.of(2025, 7, 6));
        estadiaOutput.setCliente(clienteDTO);
        estadiaOutput.setQuarto(quartoDTO);

        when(estadiaService.update(eq(1L), any(EstadiaDTO.class))).thenReturn(estadiaOutput);

        // Act & Assert
        mockMvc.perform(put("/estadia/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadiaInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cliente.login").value("joao.silva"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteShouldReturnNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/estadia/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ROLE_CLIENTE")
    void findByClientIdShouldReturnAnArrayOfStays() throws Exception {
        // Arrange
        EstadiaDTO estadia2 = new EstadiaDTO();
        estadia2.setId(2L);
        estadia2.setDataEntrada(LocalDate.of(2025, 8, 1));
        estadia2.setDataSaida(LocalDate.of(2025, 8, 3));
        estadia2.setCliente(clienteDTO);
        estadia2.setQuarto(quartoDTO);

        List<EstadiaDTO> estadias = Arrays.asList(estadiaDTO, estadia2);
        when(estadiaService.findByClienteId(1L)).thenReturn(estadias);

        // Act & Assert
        mockMvc.perform(get("/estadia/cliente/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cliente.telefone").value("(11) 99999-9999"))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @WithMockUser(authorities = "ROLE_CLIENTE")
    void findMostExpensiveStayShouldReturnOneValue() throws Exception {
        // Arrange
        EstadiaDTO estadiaMaior = new EstadiaDTO();
        estadiaMaior.setId(3L);
        estadiaMaior.setDataEntrada(LocalDate.of(2025, 9, 1));
        estadiaMaior.setDataSaida(LocalDate.of(2025, 9, 8));
        estadiaMaior.setCliente(clienteDTO);

        QuartoDTO quartoLuxo = new QuartoDTO();
        quartoLuxo.setId(2L);
        quartoLuxo.setDescricao("Quarto de Luxo");
        quartoLuxo.setValor(BigDecimal.valueOf(200.00));
        quartoLuxo.setImagemUrl("https://exemplo.com/quarto-luxo.jpg");
        estadiaMaior.setQuarto(quartoLuxo);

        when(estadiaService.findEstadiaDeMaiorValorByClienteId(1L)).thenReturn(estadiaMaior);

        // Act & Assert
        mockMvc.perform(get("/estadia/maior/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.quarto.descricao").value("Quarto de Luxo"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_CLIENTE")
    void findCheapestStayShouldReturnOneValue() throws Exception {
        // Arrange
        EstadiaDTO estadiaMenor = new EstadiaDTO();
        estadiaMenor.setId(4L);
        estadiaMenor.setDataEntrada(LocalDate.of(2025, 6, 15));
        estadiaMenor.setDataSaida(LocalDate.of(2025, 6, 16));
        estadiaMenor.setCliente(clienteDTO);

        QuartoDTO quartoSimples = new QuartoDTO();
        quartoSimples.setId(3L);
        quartoSimples.setDescricao("Quarto Econômico");
        quartoSimples.setValor(BigDecimal.valueOf(100.00));
        quartoSimples.setImagemUrl("https://exemplo.com/quarto-economico.jpg");
        estadiaMenor.setQuarto(quartoSimples);

        when(estadiaService.findEstadiaDeMenorValorByClienteId(1L)).thenReturn(estadiaMenor);

        // Act & Assert
        mockMvc.perform(get("/estadia/menor/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4L))
                .andExpect(jsonPath("$.quarto.descricao").value("Quarto Econômico"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_CLIENTE")
    void totalOfClientStaysShouldReturnOneValue() throws Exception {
        // Arrange
        when(estadiaService.totalEstadiasCliente(1L)).thenReturn(BigDecimal.valueOf(2500L));

        // Act & Assert
        mockMvc.perform(get("/estadia/total/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor_total_estadias").value(2500L));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void invoiceShouldReturnAllStaysData() throws Exception {
        // Arrange
        EstadiaDTO estadia2 = new EstadiaDTO();
        estadia2.setId(2L);
        estadia2.setDataEntrada(LocalDate.of(2025, 8, 1));
        estadia2.setDataSaida(LocalDate.of(2025, 8, 3));
        estadia2.setCliente(clienteDTO);
        estadia2.setQuarto(quartoDTO);

        NotaFiscalDTO notaFiscalCompleta = new NotaFiscalDTO();
        notaFiscalCompleta.setCliente(clienteDTO);
        notaFiscalCompleta.setEstadias(Arrays.asList(estadiaDTO, estadia2));
        notaFiscalCompleta.setTotal(BigDecimal.valueOf(900L));

        when(estadiaService.emitirNotaFiscal(1L)).thenReturn(notaFiscalCompleta);

        // Act & Assert
        mockMvc.perform(get("/estadia/nota-fiscal/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente.nome").value("João Silva"))
                .andExpect(jsonPath("$.cliente.email").value("joao.silva@email.com"))
                .andExpect(jsonPath("$.total").value(900L))
                .andExpect(jsonPath("$.estadias").isArray())
                .andExpect(jsonPath("$.estadias.length()").value(2));
    }
}
