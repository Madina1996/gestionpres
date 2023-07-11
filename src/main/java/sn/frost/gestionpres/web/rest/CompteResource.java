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
import sn.frost.gestionpres.domain.Compte;
import sn.frost.gestionpres.repository.CompteRepository;
import sn.frost.gestionpres.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.frost.gestionpres.domain.Compte}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CompteResource {

    private final Logger log = LoggerFactory.getLogger(CompteResource.class);

    private static final String ENTITY_NAME = "compte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompteRepository compteRepository;

    public CompteResource(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    /**
     * {@code POST  /comptes} : Create a new compte.
     *
     * @param compte the compte to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compte, or with status {@code 400 (Bad Request)} if the compte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comptes")
    public ResponseEntity<Compte> createCompte(@RequestBody Compte compte) throws URISyntaxException {
        log.debug("REST request to save Compte : {}", compte);
        if (compte.getId() != null) {
            throw new BadRequestAlertException("A new compte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Compte result = compteRepository.save(compte);
        return ResponseEntity
            .created(new URI("/api/comptes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comptes/:id} : Updates an existing compte.
     *
     * @param id the id of the compte to save.
     * @param compte the compte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compte,
     * or with status {@code 400 (Bad Request)} if the compte is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comptes/{id}")
    public ResponseEntity<Compte> updateCompte(@PathVariable(value = "id", required = false) final Long id, @RequestBody Compte compte)
        throws URISyntaxException {
        log.debug("REST request to update Compte : {}, {}", id, compte);
        if (compte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Compte result = compteRepository.save(compte);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compte.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /comptes/:id} : Partial updates given fields of an existing compte, field will ignore if it is null
     *
     * @param id the id of the compte to save.
     * @param compte the compte to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compte,
     * or with status {@code 400 (Bad Request)} if the compte is not valid,
     * or with status {@code 404 (Not Found)} if the compte is not found,
     * or with status {@code 500 (Internal Server Error)} if the compte couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/comptes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Compte> partialUpdateCompte(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Compte compte
    ) throws URISyntaxException {
        log.debug("REST request to partial update Compte partially : {}, {}", id, compte);
        if (compte.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compte.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Compte> result = compteRepository
            .findById(compte.getId())
            .map(existingCompte -> {
                if (compte.getRestant() != null) {
                    existingCompte.setRestant(compte.getRestant());
                }
                if (compte.getPayer() != null) {
                    existingCompte.setPayer(compte.getPayer());
                }

                return existingCompte;
            })
            .map(compteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, compte.getId().toString())
        );
    }

    /**
     * {@code GET  /comptes} : get all the comptes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comptes in body.
     */
    @GetMapping("/comptes")
    public List<Compte> getAllComptes() {
        log.debug("REST request to get all Comptes");
        return compteRepository.findAll();
    }

    /**
     * {@code GET  /comptes/:id} : get the "id" compte.
     *
     * @param id the id of the compte to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compte, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comptes/{id}")
    public ResponseEntity<Compte> getCompte(@PathVariable Long id) {
        log.debug("REST request to get Compte : {}", id);
        Optional<Compte> compte = compteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(compte);
    }

    @GetMapping("/comptes/getunique")
    public ResponseEntity<Compte> getunique() {
        log.debug("REST request to get Compte : {}");
        List<Compte> lc = compteRepository.findAll();
        if (lc.size() == 0) {
            this.initCompte();
            lc = compteRepository.findAll();
        }
        Compte c = new Compte();
        for (Compte compte : lc) {
            c = compte;
        }

        return ResponseUtil.wrapOrNotFound(Optional.of(c));
    }

    /**
     * {@code DELETE  /comptes/:id} : delete the "id" compte.
     *
     * @param id the id of the compte to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comptes/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
        log.debug("REST request to delete Compte : {}", id);
        compteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    private void initCompte() {
        List<Compte> lc = compteRepository.findAll();
        if (lc.size() > 0) {
            return;
        }

        Compte compte = new Compte();
        compte.setPayer(Double.valueOf(0));
        compte.setRestant(Double.valueOf(0));

        compteRepository.save(compte);

        return;
    }
}
