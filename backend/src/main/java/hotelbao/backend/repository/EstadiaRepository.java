package hotelbao.backend.repository;

import hotelbao.backend.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {
    void deleteAll ();

    @Query(nativeQuery = true, value = """
        SELECT e.*
        FROM estadia e
        JOIN quarto q ON e.quarto_id = q.id
        WHERE e.cliente_id = :id
        ORDER BY (DATEDIFF('DAY', e.data_entrada, e.data_saida) * q.valor) DESC
        LIMIT 1
    """)
    Optional<Estadia> findClientMostValuableStay (Long id); /* ID do cliente */

    @Query(nativeQuery = true, value = """
        SELECT e.*
        FROM estadia e
        JOIN quarto q ON e.quarto_id = q.id
        WHERE e.cliente_id = :id
        ORDER BY (DATEDIFF('DAY', e.data_entrada, e.data_saida) * q.valor) ASC 
        LIMIT 1
    """)
    Optional<Estadia> findClientLeastValuableStay (Long id); /* ID do cliente */

    @Query(nativeQuery = true, value = """
        SELECT SUM(DATEDIFF('DAY', e.data_entrada, e.data_saida) * q.valor) AS valor_total_estadias
        FROM estadia e
        INNER JOIN quarto q ON e.quarto_id = q.id
        WHERE e.cliente_id = :id;
    """)
    Optional<BigDecimal> findSumOfAllClientStays (Long id); /* ID do cliente */

    List<Estadia> findByUsuarioId (Long id);
}
