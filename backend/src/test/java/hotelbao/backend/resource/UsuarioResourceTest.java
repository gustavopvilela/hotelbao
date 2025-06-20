package hotelbao.backend.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import hotelbao.backend.dto.RoleDTO;
import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.dto.UsuarioInsertDTO;
import hotelbao.backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UsuarioResource.class)
public class UsuarioResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDTO usuarioDTO;
    private UsuarioInsertDTO usuarioInsertDTO;

    @BeforeEach
    public void setUp () {
        Set<RoleDTO> roles = new HashSet<>();
        roles.add(new RoleDTO(1L, "ADMIN"));
        roles.add(new RoleDTO(2L, "CLIENTE"));

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("Bruno");
        usuarioDTO.setEmail("bruno@email.com");
        usuarioDTO.setLogin("bruninho");
        usuarioDTO.setTelefone("5555555555");
        usuarioDTO.setRoles(roles);

        usuarioInsertDTO = new UsuarioInsertDTO();
        usuarioInsertDTO.setId(1L);
        usuarioInsertDTO.setNome("Bruno");
        usuarioInsertDTO.setEmail("bruno@email.com");
        usuarioInsertDTO.setLogin("bruninho");
        usuarioInsertDTO.setSenha("12345678");
        usuarioInsertDTO.setTelefone("5555555555");
        usuarioInsertDTO.setRoles(roles);
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("GET /usuario - Deve retornar uma lista paginada de usuários com status 200")
    void findAllShouldReturnPagedUsers () throws Exception {
        Page<UsuarioDTO> page = new PageImpl<>(Collections.singletonList(usuarioDTO), PageRequest.of(0, 10), 1);
        when(usuarioService.findAll(any())).thenReturn(page);

        mockMvc.perform(
                get("/usuario")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Bruno"))
                .andExpect(jsonPath("$.content[0].email").value("bruno@email.com"));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("GET /usuario/{id} - Deve retornar um usuário existente com status 200")
    void findByIdShouldReturnUserWhenExists () throws Exception {
        when(usuarioService.findById(any())).thenReturn(usuarioDTO);

        mockMvc.perform(
                get("/usuario/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Bruno"))
                .andExpect(jsonPath("$.email").value("bruno@email.com"));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("POST /usuario -  Deve criar usuário e retornar status 201 com o body sendo o usuário criado")
    void insertShouldReturnCreatedUser () throws Exception {
        when(usuarioService.insert(any(UsuarioInsertDTO.class))).thenReturn(usuarioDTO);

        String json = objectMapper.writeValueAsString(usuarioInsertDTO);

        mockMvc.perform(
                post("/usuario")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/usuario/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Bruno"));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("PUT /usuario/{id} - Deve atualizar usuário e retornar status 200")
    void updateShouldReturnUpdatedUser () throws Exception {
        when(usuarioService.update(eq(1L), any(UsuarioInsertDTO.class))).thenReturn(usuarioDTO);

        String json = objectMapper.writeValueAsString(usuarioInsertDTO);

        mockMvc.perform(
                put("/usuario/{id}", 1L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Bruno"));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("DELETE /usuario/{id} - Deve deletar usuário e retornar status 204")
    void deleteShouldReturnNoContent () throws Exception {
        doNothing().when(usuarioService).delete(1L);

        mockMvc.perform(
                delete("/usuario/{id}", 1L)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username ="teste", roles = {}) // Usuário sem roles para simular um não autenticado
    @DisplayName("POST /usuario/signup - Deve criar usuário e retornar 201 com o body sendo o usuário criado")
    void signupShouldReturnCreatedUser () throws Exception {
        when(usuarioService.signUp(any(UsuarioInsertDTO.class))).thenReturn(usuarioDTO);

        String json = objectMapper.writeValueAsString(usuarioInsertDTO);

        mockMvc.perform(
                post("/usuario/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/usuario/signup/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Bruno"));
    }

    @Test
    @WithMockUser(username = "teste", roles = {"ADMIN"})
    @DisplayName("GET /usuario/clientes - Deve retornar uma lista paginada de clientes com status 200")
    void findAllClientsShouldReturnClientsPaged () throws Exception {
        Page<UsuarioDTO> page = new PageImpl<>(Collections.singletonList(usuarioDTO), PageRequest.of(0, 10), 1);
        when(usuarioService.findAllClients(any())).thenReturn(page);

        mockMvc.perform(
                        get("/usuario/clientes")
                                .param("page", "0")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Bruno"))
                .andExpect(jsonPath("$.content[0].email").value("bruno@email.com"));
    }
}
