package hotelbao.backend.service;

import hotelbao.backend.dto.QuartoDTO;
import hotelbao.backend.entity.Quarto;

import hotelbao.backend.exceptions.DatabaseException;
import hotelbao.backend.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotelbao.backend.repository.QuartoRepository;
import hotelbao.backend.resources.QuartoResource;


import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class QuartoService {

    @Autowired
    private QuartoRepository quartoRepository;

    @Transactional(readOnly = true)
    public Page<QuartoDTO> findAll(Pageable pageable) {
        Page<Quarto> list = quartoRepository.findAll(pageable);
        return list.map(
                u -> new QuartoDTO(u)
                        .add(linkTo(methodOn(QuartoResource.class).findAll(null)).withSelfRel())
                        .add(linkTo(methodOn(QuartoResource.class).findById(u.getId())).withRel("Informações do quarto"))
        );
    }

    @Transactional(readOnly = true)
    public QuartoDTO findById(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Usuário não encontrado"));
        return new QuartoDTO(quarto)
                .add(linkTo(methodOn(QuartoResource.class).findById(quarto.getId())).withSelfRel())
                .add(linkTo(methodOn(QuartoResource.class).findAll(null)).withRel("Todos os usuários"))
                .add(linkTo(methodOn(QuartoResource.class).update(quarto.getId(), null)).withRel("Atualizar quarto"))
                .add(linkTo(methodOn(QuartoResource.class).delete(quarto.getId())).withRel("Deletar quarto"));
    }

    @Transactional
    public QuartoDTO update(Long id, QuartoDTO dto) {
        Quarto entity = quartoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Quarto não encontrado: ID " + id));

        entity.setDescricao(dto.getDescricao());
        entity.setValor(dto.getValor());
        entity.setImagemUrl(dto.getImagemUrl());

        entity = quartoRepository.save(entity);

        return new QuartoDTO(entity)
                .add(linkTo(methodOn(QuartoResource.class).findById(entity.getId())).withRel("Encontrar quarto por ID"))
                .add(linkTo(methodOn(QuartoResource.class).findAll(null)).withRel("Todos os quartos"))
                .add(linkTo(methodOn(QuartoResource.class).delete(entity.getId())).withRel("Deletar quarto"));
    }


    @Transactional
    public QuartoDTO save(Quarto entity) {
        return new QuartoDTO(quartoRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!quartoRepository.existsById(id)) {
            throw new ResourceNotFound("Usuário não encontrado: ID" + id);
        }
        try {
            quartoRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            throw new DatabaseException("Violação de integridade: " + ex.getMessage());
        }
    }

    public boolean existsById(Long id) {
        return quartoRepository.existsById(id);
    }
}
