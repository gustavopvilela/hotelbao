package hotelbao.backend.repository;

import hotelbao.backend.entity.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Long> {
    void deleteAll ();

    @Query(nativeQuery = true, value = """
        SELECT e.id,
               e.data_entrada,
               e.data_saida,
               q.id AS quarto_id,
               q.valor AS valor_por_noite,
               DATEDIFF('DAY', e.data_entrada, e.data_saida) AS noites,
               (DATEDIFF('DAY', e.data_entrada, e.data_saida) * q.valor) AS valor_total
        FROM estadia e
        JOIN quarto q ON e.quarto_id = q.id
        WHERE e.cliente_id = :id
        ORDER BY valor_total DESC
        LIMIT 1;
    """)
    Estadia findClientMostValuableStay (Long id); /* ID do cliente */

    @Query(nativeQuery = true, value = """
        SELECT e.id,
               e.data_entrada,
               e.data_saida,
               q.id AS quarto_id,
               q.valor AS valor_por_noite,
               DATEDIFF('DAY', e.data_entrada, e.data_saida) AS noites,
               (DATEDIFF('DAY', e.data_entrada, e.data_saida) * q.valor) AS valor_total
        FROM estadia e
        JOIN quarto q ON e.quarto_id = q.id
        WHERE e.cliente_id = :id
        ORDER BY valor_total ASC
        LIMIT 1;
    """)
    Estadia findClientLeastValuableStay (Long id); /* ID do cliente */

    @Query(nativeQuery = true, value = """
        SELECT SUM(DATEDIFF('DAY', e.data_entrada, e.data_saida) * q.valor) AS valor_total_estadias
        FROM estadia e
        INNER JOIN quarto q ON e.quarto_id = q.id
        WHERE e.cliente_id = :id;
    """)
    Long findSumOfAllClientStays (Long id); /* ID do cliente */
}
