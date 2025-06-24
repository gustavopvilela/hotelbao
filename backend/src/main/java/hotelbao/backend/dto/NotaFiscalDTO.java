package hotelbao.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class NotaFiscalDTO {
    UsuarioDTO cliente;
    List<EstadiaDTO> estadias;
    private BigDecimal total;

    public NotaFiscalDTO(UsuarioDTO cliente, List<EstadiaDTO> estadias, BigDecimal total) {
        this.cliente = cliente;
        this.estadias = estadias;
        this.total = total;
    }

    public NotaFiscalDTO() {}

    public UsuarioDTO getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioDTO cliente) {
        this.cliente = cliente;
    }

    public List<EstadiaDTO> getEstadias() {
        return estadias;
    }

    public void setEstadias(List<EstadiaDTO> estadias) {
        this.estadias = estadias;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "NotaFiscalDTO{" +
                "cliente=" + cliente +
                ", estadias=" + estadias +
                '}';
    }
}
