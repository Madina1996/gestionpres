package sn.frost.gestionpres.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sn.frost.gestionpres.domain.Depense;
import sn.frost.gestionpres.repository.DepenseRepository;
import sn.frost.gestionpres.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.frost.gestionpres.domain.Depense}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DepenseResource {

    private final Logger log = LoggerFactory.getLogger(DepenseResource.class);

    private static final String ENTITY_NAME = "depense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepenseRepository depenseRepository;

    public DepenseResource(DepenseRepository depenseRepository) {
        this.depenseRepository = depenseRepository;
    }

    /**
     * {@code POST  /depenses} : Create a new depense.
     *
     * @param depense the depense to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depense, or with status {@code 400 (Bad Request)} if the depense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depenses")
    public ResponseEntity<Depense> createDepense(@RequestBody Depense depense) throws URISyntaxException {
        log.debug("REST request to save Depense : {}", depense);
        if (depense.getId() != null) {
            throw new BadRequestAlertException("A new depense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Depense result = depenseRepository.save(depense);
        return ResponseEntity
            .created(new URI("/api/depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depenses/:id} : Updates an existing depense.
     *
     * @param id the id of the depense to save.
     * @param depense the depense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depense,
     * or with status {@code 400 (Bad Request)} if the depense is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depenses/{id}")
    public ResponseEntity<Depense> updateDepense(@PathVariable(value = "id", required = false) final Long id, @RequestBody Depense depense)
        throws URISyntaxException {
        log.debug("REST request to update Depense : {}, {}", id, depense);
        if (depense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Depense result = depenseRepository.save(depense);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depense.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depenses/:id} : Partial updates given fields of an existing depense, field will ignore if it is null
     *
     * @param id the id of the depense to save.
     * @param depense the depense to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depense,
     * or with status {@code 400 (Bad Request)} if the depense is not valid,
     * or with status {@code 404 (Not Found)} if the depense is not found,
     * or with status {@code 500 (Internal Server Error)} if the depense couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Depense> partialUpdateDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Depense depense
    ) throws URISyntaxException {
        log.debug("REST request to partial update Depense partially : {}, {}", id, depense);
        if (depense.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depense.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Depense> result = depenseRepository
            .findById(depense.getId())
            .map(existingDepense -> {
                if (depense.getLibelle() != null) {
                    existingDepense.setLibelle(depense.getLibelle());
                }
                if (depense.getDate() != null) {
                    existingDepense.setDate(depense.getDate());
                }
                if (depense.getMontant() != null) {
                    existingDepense.setMontant(depense.getMontant());
                }

                return existingDepense;
            })
            .map(depenseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depense.getId().toString())
        );
    }

    /**
     * {@code GET  /depenses} : get all the depenses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depenses in body.
     */
    @GetMapping("/depenses")
    public List<Depense> getAllDepenses() {
        log.debug("REST request to get all Depenses");
        return depenseRepository.findAll();
    }

    /**
     * {@code GET  /depenses/:id} : get the "id" depense.
     *
     * @param id the id of the depense to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depense, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depenses/{id}")
    public ResponseEntity<Depense> getDepense(@PathVariable Long id) {
        log.debug("REST request to get Depense : {}", id);
        Optional<Depense> depense = depenseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(depense);
    }

    /**
     * {@code DELETE  /depenses/:id} : delete the "id" depense.
     *
     * @param id the id of the depense to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depenses/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        log.debug("REST request to delete Depense : {}", id);
        depenseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
