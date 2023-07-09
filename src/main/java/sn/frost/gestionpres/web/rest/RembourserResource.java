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
import sn.frost.gestionpres.domain.Rembourser;
import sn.frost.gestionpres.repository.RembourserRepository;
import sn.frost.gestionpres.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.frost.gestionpres.domain.Rembourser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RembourserResource {

    private final Logger log = LoggerFactory.getLogger(RembourserResource.class);

    private static final String ENTITY_NAME = "rembourser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RembourserRepository rembourserRepository;

    public RembourserResource(RembourserRepository rembourserRepository) {
        this.rembourserRepository = rembourserRepository;
    }

    /**
     * {@code POST  /remboursers} : Create a new rembourser.
     *
     * @param rembourser the rembourser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rembourser, or with status {@code 400 (Bad Request)} if the rembourser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/remboursers")
    public ResponseEntity<Rembourser> createRembourser(@RequestBody Rembourser rembourser) throws URISyntaxException {
        log.debug("REST request to save Rembourser : {}", rembourser);
        if (rembourser.getId() != null) {
            throw new BadRequestAlertException("A new rembourser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rembourser result = rembourserRepository.save(rembourser);
        return ResponseEntity
            .created(new URI("/api/remboursers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /remboursers/:id} : Updates an existing rembourser.
     *
     * @param id the id of the rembourser to save.
     * @param rembourser the rembourser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rembourser,
     * or with status {@code 400 (Bad Request)} if the rembourser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rembourser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/remboursers/{id}")
    public ResponseEntity<Rembourser> updateRembourser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Rembourser rembourser
    ) throws URISyntaxException {
        log.debug("REST request to update Rembourser : {}, {}", id, rembourser);
        if (rembourser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rembourser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rembourserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rembourser result = rembourserRepository.save(rembourser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rembourser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /remboursers/:id} : Partial updates given fields of an existing rembourser, field will ignore if it is null
     *
     * @param id the id of the rembourser to save.
     * @param rembourser the rembourser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rembourser,
     * or with status {@code 400 (Bad Request)} if the rembourser is not valid,
     * or with status {@code 404 (Not Found)} if the rembourser is not found,
     * or with status {@code 500 (Internal Server Error)} if the rembourser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/remboursers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rembourser> partialUpdateRembourser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Rembourser rembourser
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rembourser partially : {}, {}", id, rembourser);
        if (rembourser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rembourser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rembourserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rembourser> result = rembourserRepository
            .findById(rembourser.getId())
            .map(existingRembourser -> {
                if (rembourser.getLibelle() != null) {
                    existingRembourser.setLibelle(rembourser.getLibelle());
                }
                if (rembourser.getDate() != null) {
                    existingRembourser.setDate(rembourser.getDate());
                }
                if (rembourser.getMontant() != null) {
                    existingRembourser.setMontant(rembourser.getMontant());
                }

                return existingRembourser;
            })
            .map(rembourserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rembourser.getId().toString())
        );
    }

    /**
     * {@code GET  /remboursers} : get all the remboursers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of remboursers in body.
     */
    @GetMapping("/remboursers")
    public List<Rembourser> getAllRemboursers() {
        log.debug("REST request to get all Remboursers");
        return rembourserRepository.findAll();
    }

    /**
     * {@code GET  /remboursers/:id} : get the "id" rembourser.
     *
     * @param id the id of the rembourser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rembourser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/remboursers/{id}")
    public ResponseEntity<Rembourser> getRembourser(@PathVariable Long id) {
        log.debug("REST request to get Rembourser : {}", id);
        Optional<Rembourser> rembourser = rembourserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rembourser);
    }

    /**
     * {@code DELETE  /remboursers/:id} : delete the "id" rembourser.
     *
     * @param id the id of the rembourser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/remboursers/{id}")
    public ResponseEntity<Void> deleteRembourser(@PathVariable Long id) {
        log.debug("REST request to delete Rembourser : {}", id);
        rembourserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
