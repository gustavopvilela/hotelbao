package hotelbao.backend.dto;

import hotelbao.backend.entity.Estadia;
import hotelbao.backend.entity.Quarto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class EstadiaDTO extends RepresentationModel<EstadiaDTO> {
    private Long id;
    @Valid
    @NotNull(message = "A data de entrada é obrigatória")
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    @Valid
    @NotNull(message = "Cliente deve ser inserido obrigatoriamente")
    private UsuarioDTO usuario;
    @Valid
    @NotNull(message = "Quarto deve ser inserido obrigatoriamente")
    private QuartoDTO quarto;

    public EstadiaDTO() {}

    public EstadiaDTO(Long id, LocalDate dataEntrada, LocalDate dataSaida, UsuarioDTO usuario, QuartoDTO quarto) {
        this.id = id;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.usuario = usuario;
        this.quarto = quarto;
    }

    public EstadiaDTO (Estadia estadia) {
        this.id = estadia.getId();
        this.dataEntrada = estadia.getDataEntrada();
        this.dataSaida = estadia.getDataSaida();
        this.usuario = new UsuarioDTO(estadia.getCliente());
        this.quarto = new QuartoDTO(estadia.getQuarto());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public UsuarioDTO getCliente() {
        return usuario;
    }

    public void setCliente(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public QuartoDTO getQuarto() {
        return quarto;
    }

    public void setQuarto(QuartoDTO quarto) {
        this.quarto = quarto;
    }

    @Override
    public String toString() {
        return "EstadiaDTO{" +
                "id=" + id +
                ", dataEntrada=" + dataEntrada +
                ", dataSaida=" + dataSaida +
                ", usuario=" + usuario +
                ", quarto=" + quarto +
                '}';
    }
}
