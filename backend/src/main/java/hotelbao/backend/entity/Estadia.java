package hotelbao.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "estadia")
public class Estadia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* FK para Cliente. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario usuario;

    /* FK para Quarto. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    private LocalDate dataEntrada;
    private LocalDate dataSaida; /* Sempre 1 dia depois da entrada. */

    public Estadia() {
    }

    public Estadia(Long id, Usuario usuario, Quarto quarto, LocalDate dataEntrada) {
        this.id = id;
        this.usuario = usuario;
        this.quarto = quarto;
        this.dataEntrada = dataEntrada;
        this.setDataSaida(dataEntrada);
    }

    public Estadia (Estadia entity) {
        this.id = entity.getId();
        this.usuario = entity.getCliente();
        this.quarto = entity.getQuarto();
        this.dataEntrada = entity.getDataEntrada();
        this.dataSaida = entity.getDataSaida();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return usuario;
    }

    public void setCliente(Usuario usuario) {
        this.usuario = usuario;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
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

    public void setDataSaida(LocalDate dataEntrada) {
        this.dataSaida = dataEntrada.plusDays(1);
    }
}
