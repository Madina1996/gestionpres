package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.frost.gestionpres.domain.Rembourser;

/**
 * Spring Data JPA repository for the Rembourser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RembourserRepository extends JpaRepository<Rembourser, Long> {}
