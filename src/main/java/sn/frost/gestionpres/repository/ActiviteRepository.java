package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.frost.gestionpres.domain.Activite;

/**
 * Spring Data JPA repository for the Activite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {}
