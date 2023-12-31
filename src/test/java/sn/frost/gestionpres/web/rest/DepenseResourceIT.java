package sn.frost.gestionpres.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.frost.gestionpres.IntegrationTest;
import sn.frost.gestionpres.domain.Depense;
import sn.frost.gestionpres.repository.DepenseRepository;

/**
 * Integration tests for the {@link DepenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepenseResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String ENTITY_API_URL = "/api/depenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepenseMockMvc;

    private Depense depense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createEntity(EntityManager em) {
        Depense depense = new Depense().libelle(DEFAULT_LIBELLE).date(DEFAULT_DATE).montant(DEFAULT_MONTANT);
        return depense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createUpdatedEntity(EntityManager em) {
        Depense depense = new Depense().libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);
        return depense;
    }

    @BeforeEach
    public void initTest() {
        depense = createEntity(em);
    }

    @Test
    @Transactional
    void createDepense() throws Exception {
        int databaseSizeBeforeCreate = depenseRepository.findAll().size();
        // Create the Depense
        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isCreated());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate + 1);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDepense.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDepense.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createDepenseWithExistingId() throws Exception {
        // Create the Depense with an existing ID
        depense.setId(1L);

        int databaseSizeBeforeCreate = depenseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepenses() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get all the depenseList
        restDepenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depense.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    void getDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get the depense
        restDepenseMockMvc
            .perform(get(ENTITY_API_URL_ID, depense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depense.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDepense() throws Exception {
        // Get the depense
        restDepenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense
        Depense updatedDepense = depenseRepository.findById(depense.getId()).get();
        // Disconnect from session so that the updates on updatedDepense are not directly saved in db
        em.detach(updatedDepense);
        updatedDepense.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDepense.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepenseWithPatch() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense using partial update
        Depense partialUpdatedDepense = new Depense();
        partialUpdatedDepense.setId(depense.getId());

        partialUpdatedDepense.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDepense.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateDepenseWithPatch() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense using partial update
        Depense partialUpdatedDepense = new Depense();
        partialUpdatedDepense.setId(depense.getId());

        partialUpdatedDepense.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDepense.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(depense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeDelete = depenseRepository.findAll().size();

        // Delete the depense
        restDepenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, depense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
