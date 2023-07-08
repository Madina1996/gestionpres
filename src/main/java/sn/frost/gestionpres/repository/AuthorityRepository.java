package sn.frost.gestionpres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.frost.gestionpres.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
