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
import sn.frost.gestionpres.domain.Pres;
import sn.frost.gestionpres.repository.PresRepository;

/**
 * Integration tests for the {@link PresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PresResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String ENTITY_API_URL = "/api/pres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresRepository presRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresMockMvc;

    private Pres pres;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pres createEntity(EntityManager em) {
        Pres pres = new Pres().libelle(DEFAULT_LIBELLE).date(DEFAULT_DATE).montant(DEFAULT_MONTANT);
        return pres;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pres createUpdatedEntity(EntityManager em) {
        Pres pres = new Pres().libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);
        return pres;
    }

    @BeforeEach
    public void initTest() {
        pres = createEntity(em);
    }

    @Test
    @Transactional
    void createPres() throws Exception {
        int databaseSizeBeforeCreate = presRepository.findAll().size();
        // Create the Pres
        restPresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pres)))
            .andExpect(status().isCreated());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeCreate + 1);
        Pres testPres = presList.get(presList.size() - 1);
        assertThat(testPres.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPres.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPres.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createPresWithExistingId() throws Exception {
        // Create the Pres with an existing ID
        pres.setId(1L);

        int databaseSizeBeforeCreate = presRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pres)))
            .andExpect(status().isBadRequest());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPres() throws Exception {
        // Initialize the database
        presRepository.saveAndFlush(pres);

        // Get all the presList
        restPresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pres.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    void getPres() throws Exception {
        // Initialize the database
        presRepository.saveAndFlush(pres);

        // Get the pres
        restPresMockMvc
            .perform(get(ENTITY_API_URL_ID, pres.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pres.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPres() throws Exception {
        // Get the pres
        restPresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPres() throws Exception {
        // Initialize the database
        presRepository.saveAndFlush(pres);

        int databaseSizeBeforeUpdate = presRepository.findAll().size();

        // Update the pres
        Pres updatedPres = presRepository.findById(pres.getId()).get();
        // Disconnect from session so that the updates on updatedPres are not directly saved in db
        em.detach(updatedPres);
        updatedPres.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restPresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPres))
            )
            .andExpect(status().isOk());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
        Pres testPres = presList.get(presList.size() - 1);
        assertThat(testPres.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPres.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPres.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingPres() throws Exception {
        int databaseSizeBeforeUpdate = presRepository.findAll().size();
        pres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pres.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPres() throws Exception {
        int databaseSizeBeforeUpdate = presRepository.findAll().size();
        pres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPres() throws Exception {
        int databaseSizeBeforeUpdate = presRepository.findAll().size();
        pres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pres)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresWithPatch() throws Exception {
        // Initialize the database
        presRepository.saveAndFlush(pres);

        int databaseSizeBeforeUpdate = presRepository.findAll().size();

        // Update the pres using partial update
        Pres partialUpdatedPres = new Pres();
        partialUpdatedPres.setId(pres.getId());

        partialUpdatedPres.libelle(UPDATED_LIBELLE).montant(UPDATED_MONTANT);

        restPresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPres))
            )
            .andExpect(status().isOk());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
        Pres testPres = presList.get(presList.size() - 1);
        assertThat(testPres.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPres.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPres.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdatePresWithPatch() throws Exception {
        // Initialize the database
        presRepository.saveAndFlush(pres);

        int databaseSizeBeforeUpdate = presRepository.findAll().size();

        // Update the pres using partial update
        Pres partialUpdatedPres = new Pres();
        partialUpdatedPres.setId(pres.getId());

        partialUpdatedPres.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restPresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPres))
            )
            .andExpect(status().isOk());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
        Pres testPres = presList.get(presList.size() - 1);
        assertThat(testPres.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPres.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPres.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingPres() throws Exception {
        int databaseSizeBeforeUpdate = presRepository.findAll().size();
        pres.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pres.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPres() throws Exception {
        int databaseSizeBeforeUpdate = presRepository.findAll().size();
        pres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pres))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPres() throws Exception {
        int databaseSizeBeforeUpdate = presRepository.findAll().size();
        pres.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pres)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pres in the database
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePres() throws Exception {
        // Initialize the database
        presRepository.saveAndFlush(pres);

        int databaseSizeBeforeDelete = presRepository.findAll().size();

        // Delete the pres
        restPresMockMvc
            .perform(delete(ENTITY_API_URL_ID, pres.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pres> presList = presRepository.findAll();
        assertThat(presList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
