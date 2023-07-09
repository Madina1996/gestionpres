package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.frost.gestionpres.domain.Fournisseur;

/**
 * Spring Data JPA repository for the Fournisseur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {}
