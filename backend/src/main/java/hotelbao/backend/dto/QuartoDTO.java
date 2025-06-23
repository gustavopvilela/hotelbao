package hotelbao.backend.dto;

import hotelbao.backend.entity.Quarto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

public class QuartoDTO extends RepresentationModel<QuartoDTO> {

    private Long id;
    @NotBlank(message = "Insira a descrição do quarto.")
    private String descricao;
    @NotNull(message = "Inisira o valor da diária do quarto.")
    @Positive(message = "O valor da diária deve ser positivo")
    private BigDecimal valor;
    private String imagemUrl;

    public QuartoDTO() {}

    public QuartoDTO(Long id, String descricao, BigDecimal valor, String imagemUrl) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.imagemUrl = imagemUrl;
    }

    public QuartoDTO(Quarto entity) {
        this.id = entity.getId();
        this.descricao = entity.getDescricao();
        this.valor = entity.getValor();
        this.imagemUrl = entity.getImagemUrl();
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

    @Override
    public String toString() {
        return "QuartoDTO{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", imagemUrl='" + imagemUrl + '\'' +
                '}';
    }
}
