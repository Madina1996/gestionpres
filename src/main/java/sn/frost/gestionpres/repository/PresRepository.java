package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.frost.gestionpres.domain.Pres;

/**
 * Spring Data JPA repository for the Pres entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresRepository extends JpaRepository<Pres, Long> {}
