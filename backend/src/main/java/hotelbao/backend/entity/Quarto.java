package hotelbao.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "quarto")
public class Quarto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @Column(precision = 10, scale = 2)
    private BigDecimal valor;
    private String imagemUrl;

    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Estadia> estadias = new HashSet<>();

    public Quarto() {
    }

    public Quarto(String descricao, BigDecimal valor, String imagemUrl) {
        this.descricao = descricao;
        this.valor = valor;
        this.imagemUrl = imagemUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Set<Estadia> getEstadias() {
        return estadias;
    }

    public void setEstadias(Set<Estadia> estadias) {
        this.estadias = estadias;
    }

    @Override
    public String toString() {
        return "Quarto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", imagemUrl='" + imagemUrl + '\'' +
                ", estadias=" + estadias +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Quarto quarto)) return false;
        return Objects.equals(getId(), quarto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
