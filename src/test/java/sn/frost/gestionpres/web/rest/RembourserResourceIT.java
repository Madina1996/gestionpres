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
import sn.frost.gestionpres.domain.Rembourser;
import sn.frost.gestionpres.repository.RembourserRepository;

/**
 * Integration tests for the {@link RembourserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RembourserResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final String ENTITY_API_URL = "/api/remboursers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RembourserRepository rembourserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRembourserMockMvc;

    private Rembourser rembourser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rembourser createEntity(EntityManager em) {
        Rembourser rembourser = new Rembourser().libelle(DEFAULT_LIBELLE).date(DEFAULT_DATE).montant(DEFAULT_MONTANT);
        return rembourser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rembourser createUpdatedEntity(EntityManager em) {
        Rembourser rembourser = new Rembourser().libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);
        return rembourser;
    }

    @BeforeEach
    public void initTest() {
        rembourser = createEntity(em);
    }

    @Test
    @Transactional
    void createRembourser() throws Exception {
        int databaseSizeBeforeCreate = rembourserRepository.findAll().size();
        // Create the Rembourser
        restRembourserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rembourser)))
            .andExpect(status().isCreated());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeCreate + 1);
        Rembourser testRembourser = rembourserList.get(rembourserList.size() - 1);
        assertThat(testRembourser.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testRembourser.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRembourser.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createRembourserWithExistingId() throws Exception {
        // Create the Rembourser with an existing ID
        rembourser.setId(1L);

        int databaseSizeBeforeCreate = rembourserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRembourserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rembourser)))
            .andExpect(status().isBadRequest());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRemboursers() throws Exception {
        // Initialize the database
        rembourserRepository.saveAndFlush(rembourser);

        // Get all the rembourserList
        restRembourserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rembourser.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }

    @Test
    @Transactional
    void getRembourser() throws Exception {
        // Initialize the database
        rembourserRepository.saveAndFlush(rembourser);

        // Get the rembourser
        restRembourserMockMvc
            .perform(get(ENTITY_API_URL_ID, rembourser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rembourser.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingRembourser() throws Exception {
        // Get the rembourser
        restRembourserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRembourser() throws Exception {
        // Initialize the database
        rembourserRepository.saveAndFlush(rembourser);

        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();

        // Update the rembourser
        Rembourser updatedRembourser = rembourserRepository.findById(rembourser.getId()).get();
        // Disconnect from session so that the updates on updatedRembourser are not directly saved in db
        em.detach(updatedRembourser);
        updatedRembourser.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restRembourserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRembourser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRembourser))
            )
            .andExpect(status().isOk());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
        Rembourser testRembourser = rembourserList.get(rembourserList.size() - 1);
        assertThat(testRembourser.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testRembourser.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRembourser.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingRembourser() throws Exception {
        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();
        rembourser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRembourserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rembourser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rembourser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRembourser() throws Exception {
        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();
        rembourser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRembourserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rembourser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRembourser() throws Exception {
        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();
        rembourser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRembourserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rembourser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRembourserWithPatch() throws Exception {
        // Initialize the database
        rembourserRepository.saveAndFlush(rembourser);

        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();

        // Update the rembourser using partial update
        Rembourser partialUpdatedRembourser = new Rembourser();
        partialUpdatedRembourser.setId(rembourser.getId());

        partialUpdatedRembourser.montant(UPDATED_MONTANT);

        restRembourserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRembourser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRembourser))
            )
            .andExpect(status().isOk());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
        Rembourser testRembourser = rembourserList.get(rembourserList.size() - 1);
        assertThat(testRembourser.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testRembourser.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRembourser.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateRembourserWithPatch() throws Exception {
        // Initialize the database
        rembourserRepository.saveAndFlush(rembourser);

        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();

        // Update the rembourser using partial update
        Rembourser partialUpdatedRembourser = new Rembourser();
        partialUpdatedRembourser.setId(rembourser.getId());

        partialUpdatedRembourser.libelle(UPDATED_LIBELLE).date(UPDATED_DATE).montant(UPDATED_MONTANT);

        restRembourserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRembourser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRembourser))
            )
            .andExpect(status().isOk());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
        Rembourser testRembourser = rembourserList.get(rembourserList.size() - 1);
        assertThat(testRembourser.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testRembourser.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRembourser.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingRembourser() throws Exception {
        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();
        rembourser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRembourserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rembourser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rembourser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRembourser() throws Exception {
        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();
        rembourser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRembourserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rembourser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRembourser() throws Exception {
        int databaseSizeBeforeUpdate = rembourserRepository.findAll().size();
        rembourser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRembourserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rembourser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rembourser in the database
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRembourser() throws Exception {
        // Initialize the database
        rembourserRepository.saveAndFlush(rembourser);

        int databaseSizeBeforeDelete = rembourserRepository.findAll().size();

        // Delete the rembourser
        restRembourserMockMvc
            .perform(delete(ENTITY_API_URL_ID, rembourser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rembourser> rembourserList = rembourserRepository.findAll();
        assertThat(rembourserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
