package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.frost.gestionpres.domain.Depense;

/**
 * Spring Data JPA repository for the Depense entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {}
