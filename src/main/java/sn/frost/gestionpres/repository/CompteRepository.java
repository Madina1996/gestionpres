package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.frost.gestionpres.domain.Compte;

/**
 * Spring Data JPA repository for the Compte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {}
