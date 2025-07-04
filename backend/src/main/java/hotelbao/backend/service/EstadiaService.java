package hotelbao.backend.service;

import hotelbao.backend.dto.EstadiaDTO;
import hotelbao.backend.dto.NotaFiscalDTO;
import hotelbao.backend.dto.UsuarioDTO;
import hotelbao.backend.entity.Estadia;
import hotelbao.backend.entity.Quarto;
import hotelbao.backend.entity.Usuario;
import hotelbao.backend.exceptions.DatabaseException;
import hotelbao.backend.exceptions.ResourceNotFound;
import hotelbao.backend.exceptions.StayException;
import hotelbao.backend.repository.EstadiaRepository;
import hotelbao.backend.repository.QuartoRepository;
import hotelbao.backend.repository.UsuarioRepository;
import hotelbao.backend.resource.EstadiaResource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EstadiaService {
    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private QuartoRepository quartoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public Page<EstadiaDTO> findAll (Pageable pageable) {
        Page<Estadia> list = estadiaRepository.findAll(pageable);
        return list.map(
            e -> new EstadiaDTO(e)
                    .add(linkTo(methodOn(EstadiaResource.class).findAll(null)).withSelfRel())
                    .add(linkTo(methodOn(EstadiaResource.class).findById(e.getId())).withRel("Informações da estadia"))
        );
    }

    @Transactional(readOnly = true)
    public EstadiaDTO findById (Long id) {
        Optional<Estadia> opt = estadiaRepository.findById(id);
        Estadia estadia = opt.orElseThrow(() -> new ResourceNotFound("Estadia não encontrada"));
        return new EstadiaDTO(estadia)
                .add(linkTo(methodOn(EstadiaResource.class).findById(estadia.getId())).withSelfRel())
                .add(linkTo(methodOn(EstadiaResource.class).findAll(null)).withRel("Todos as estadias"))
                .add(linkTo(methodOn(EstadiaResource.class).update(estadia.getId(), null)).withRel("Atualizar estadia"))
                .add(linkTo(methodOn(EstadiaResource.class).delete(estadia.getId())).withRel("Deletar estadia"));
    }

    @Transactional
    public EstadiaDTO insert (EstadiaDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (dto.getDataSaida() != null && dto.getDataSaida().isBefore(dto.getDataEntrada())) {
            throw new StayException("A data de saída não pode ser anterior à data de entrada.");
        }

        if (this.estadiaExisteEmDadaData(dto.getDataEntrada(), dto.getQuarto().getId())) {
            throw new StayException("O quarto de ID " + dto.getQuarto().getId() + " já está reservado para o dia " + dto.getDataEntrada().format(formatter));
        }

        if (dto.getDataEntrada().isBefore(LocalDate.now())) {
            throw new StayException("A data de entrada não pode ser anterior a hoje.");
        }

        Estadia entity = new Estadia();
        this.copiarDTOParaEntidade(dto, entity);
        Estadia nova = estadiaRepository.save(entity);
        return new EstadiaDTO(nova)
                .add(linkTo(methodOn(EstadiaResource.class).findById(nova.getId())).withRel("Encontrar estadia por ID"))
                .add(linkTo(methodOn(EstadiaResource.class).findAll(null)).withRel("Todos as estadias"))
                .add(linkTo(methodOn(EstadiaResource.class).update(nova.getId(), null)).withRel("Atualizar estadia"))
                .add(linkTo(methodOn(EstadiaResource.class).delete(nova.getId())).withRel("Deletar estadia"));
    }

    @Transactional
    public EstadiaDTO update (Long id, EstadiaDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (dto.getDataSaida() != null && dto.getDataSaida().isBefore(dto.getDataEntrada())) {
            throw new StayException("A data de saída não pode ser anterior à data de entrada.");
        }

        if (this.estadiaExisteEmDadaData(dto.getDataEntrada(), dto.getQuarto().getId())) {
            throw new StayException("O quarto de ID " + dto.getQuarto().getId() + " já está reservado para o dia " + dto.getDataEntrada().format(formatter));
        }

        if (dto.getDataEntrada().isBefore(LocalDate.now())) {
            throw new StayException("A data de entrada não pode ser anterior a hoje.");
        }

        try {
            Estadia entity = estadiaRepository.getReferenceById(id);
            this.copiarDTOParaEntidade(dto, entity);
            entity = estadiaRepository.save(entity);
            return new EstadiaDTO(entity)
                    .add(linkTo(methodOn(EstadiaResource.class).findById(entity.getId())).withRel("Encontrar estadia por ID"))
                    .add(linkTo(methodOn(EstadiaResource.class).update(entity.getId(), null)).withRel("Atualizar estadia"))
                    .add(linkTo(methodOn(EstadiaResource.class).delete(entity.getId())).withRel("Deletar estadia"));
        }
        catch (EntityNotFoundException ex) {
            throw new ResourceNotFound("Estadia não encontrada: ID " + id);
        }
    }

    @Transactional
    public void delete (Long id) {
        if (!estadiaRepository.existsById(id)) {
            throw new ResourceNotFound("Estadia não encontrada: ID " + id);
        }

        try {
            estadiaRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Violação de integridade: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal totalEstadiasCliente (Long id) {
        Optional<BigDecimal> opt = estadiaRepository.findSumOfAllClientStays(id);
        return opt.orElseThrow(() -> new ResourceNotFound("Não há estadias"));
    }

    @Transactional(readOnly = true)
    public List<EstadiaDTO> findByClienteId (Long id) {
        List<Estadia> estadias = estadiaRepository.findByUsuarioId(id);
        return estadias.stream().map(
                e -> new EstadiaDTO(e)
                        .add(linkTo(methodOn(EstadiaResource.class).findByClienteId(id)).withSelfRel())
                        .add(linkTo(methodOn(EstadiaResource.class).findById(e.getId())).withRel("Informações da estadia"))
        ).toList();
    }

    @Transactional(readOnly = true)
    public EstadiaDTO findEstadiaDeMaiorValorByClienteId (Long id) {
        Optional<Estadia> opt = estadiaRepository.findClientMostValuableStay(id);
        Estadia estadia = opt.orElseThrow(() -> new ResourceNotFound("Estadia não encontrada"));
        return new EstadiaDTO(estadia)
                .add(linkTo(methodOn(EstadiaResource.class).findById(estadia.getId())).withRel("Encontrar estadia por ID"))
                .add(linkTo(methodOn(EstadiaResource.class).findAll(null)).withRel("Todos as estadias"))
                .add(linkTo(methodOn(EstadiaResource.class).update(estadia.getId(), null)).withRel("Atualizar estadia"))
                .add(linkTo(methodOn(EstadiaResource.class).delete(estadia.getId())).withRel("Deletar estadia"));
    }

    @Transactional(readOnly = true)
    public EstadiaDTO findEstadiaDeMenorValorByClienteId (Long id) {
        Optional<Estadia> opt = estadiaRepository.findClientLeastValuableStay(id);
        Estadia estadia = opt.orElseThrow(() -> new ResourceNotFound("Estadia não encontrada"));
        return new EstadiaDTO(estadia)
                .add(linkTo(methodOn(EstadiaResource.class).findById(estadia.getId())).withRel("Encontrar estadia por ID"))
                .add(linkTo(methodOn(EstadiaResource.class).findAll(null)).withRel("Todos as estadias"))
                .add(linkTo(methodOn(EstadiaResource.class).update(estadia.getId(), null)).withRel("Atualizar estadia"))
                .add(linkTo(methodOn(EstadiaResource.class).delete(estadia.getId())).withRel("Deletar estadia"));
    }

    @Transactional(readOnly = true)
    public Boolean estadiaExisteEmDadaData (LocalDate data, Long quarto_id) {
        return estadiaRepository.existsStayInGivenDate(data, quarto_id);
    }

    @Transactional(readOnly = true)
    public NotaFiscalDTO emitirNotaFiscal (Long id) {
        List<EstadiaDTO> estadias = this.findByClienteId(id);
        UsuarioDTO cliente = usuarioService.findById(id);
        BigDecimal total = this.totalEstadiasCliente(id);

        return new NotaFiscalDTO(cliente, estadias, total);
    }

    private void copiarDTOParaEntidade (EstadiaDTO dto, Estadia entity) {

        Usuario cliente = usuarioRepository.findById(dto.getUsuario().getId()).orElseThrow(()-> new ResourceNotFound("Usuário não encontrado."));
        Quarto quarto = quartoRepository.findById(dto.getQuarto().getId()).orElseThrow(()-> new ResourceNotFound("Quarto não encontrado."));

        entity.setDataEntrada(dto.getDataEntrada());
        entity.setDataSaida(dto.getDataEntrada()); // Essa função da entidade já adiciona um dia à data de entrada
        entity.setCliente(cliente);
        entity.setQuarto(quarto);
    }
}
