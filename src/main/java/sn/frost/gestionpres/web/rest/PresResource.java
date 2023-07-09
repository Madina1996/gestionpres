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
import sn.frost.gestionpres.domain.Pres;
import sn.frost.gestionpres.repository.PresRepository;
import sn.frost.gestionpres.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.frost.gestionpres.domain.Pres}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PresResource {

    private final Logger log = LoggerFactory.getLogger(PresResource.class);

    private static final String ENTITY_NAME = "pres";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresRepository presRepository;

    public PresResource(PresRepository presRepository) {
        this.presRepository = presRepository;
    }

    /**
     * {@code POST  /pres} : Create a new pres.
     *
     * @param pres the pres to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pres, or with status {@code 400 (Bad Request)} if the pres has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pres")
    public ResponseEntity<Pres> createPres(@RequestBody Pres pres) throws URISyntaxException {
        log.debug("REST request to save Pres : {}", pres);
        if (pres.getId() != null) {
            throw new BadRequestAlertException("A new pres cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pres result = presRepository.save(pres);
        return ResponseEntity
            .created(new URI("/api/pres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pres/:id} : Updates an existing pres.
     *
     * @param id the id of the pres to save.
     * @param pres the pres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pres,
     * or with status {@code 400 (Bad Request)} if the pres is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pres/{id}")
    public ResponseEntity<Pres> updatePres(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pres pres)
        throws URISyntaxException {
        log.debug("REST request to update Pres : {}, {}", id, pres);
        if (pres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pres result = presRepository.save(pres);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pres.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pres/:id} : Partial updates given fields of an existing pres, field will ignore if it is null
     *
     * @param id the id of the pres to save.
     * @param pres the pres to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pres,
     * or with status {@code 400 (Bad Request)} if the pres is not valid,
     * or with status {@code 404 (Not Found)} if the pres is not found,
     * or with status {@code 500 (Internal Server Error)} if the pres couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pres> partialUpdatePres(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pres pres)
        throws URISyntaxException {
        log.debug("REST request to partial update Pres partially : {}, {}", id, pres);
        if (pres.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pres.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pres> result = presRepository
            .findById(pres.getId())
            .map(existingPres -> {
                if (pres.getLibelle() != null) {
                    existingPres.setLibelle(pres.getLibelle());
                }
                if (pres.getDate() != null) {
                    existingPres.setDate(pres.getDate());
                }
                if (pres.getMontant() != null) {
                    existingPres.setMontant(pres.getMontant());
                }

                return existingPres;
            })
            .map(presRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pres.getId().toString())
        );
    }

    /**
     * {@code GET  /pres} : get all the pres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pres in body.
     */
    @GetMapping("/pres")
    public List<Pres> getAllPres() {
        log.debug("REST request to get all Pres");
        return presRepository.findAll();
    }

    /**
     * {@code GET  /pres/:id} : get the "id" pres.
     *
     * @param id the id of the pres to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pres, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pres/{id}")
    public ResponseEntity<Pres> getPres(@PathVariable Long id) {
        log.debug("REST request to get Pres : {}", id);
        Optional<Pres> pres = presRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(pres);
    }

    /**
     * {@code DELETE  /pres/:id} : delete the "id" pres.
     *
     * @param id the id of the pres to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pres/{id}")
    public ResponseEntity<Void> deletePres(@PathVariable Long id) {
        log.debug("REST request to delete Pres : {}", id);
        presRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
