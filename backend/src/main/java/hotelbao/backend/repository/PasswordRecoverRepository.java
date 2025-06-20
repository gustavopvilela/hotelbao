package hotelbao.backend.repository;

import hotelbao.backend.entity.PasswordRecover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {
    @Query("SELECT obj FROM PasswordRecover obj WHERE (obj.token = :token) AND (obj.expiration > :now)")
    List<PasswordRecover> findValidToken (String token, Instant now);

    void deleteAll ();
}
