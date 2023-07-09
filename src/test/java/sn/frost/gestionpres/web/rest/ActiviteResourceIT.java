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
import sn.frost.gestionpres.domain.Activite;
import sn.frost.gestionpres.repository.ActiviteRepository;

/**
 * Integration tests for the {@link ActiviteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActiviteResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/activites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActiviteRepository activiteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiviteMockMvc;

    private Activite activite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createEntity(EntityManager em) {
        Activite activite = new Activite().libelle(DEFAULT_LIBELLE).date(DEFAULT_DATE);
        return activite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createUpdatedEntity(EntityManager em) {
        Activite activite = new Activite().libelle(UPDATED_LIBELLE).date(UPDATED_DATE);
        return activite;
    }

    @BeforeEach
    public void initTest() {
        activite = createEntity(em);
    }

    @Test
    @Transactional
    void createActivite() throws Exception {
        int databaseSizeBeforeCreate = activiteRepository.findAll().size();
        // Create the Activite
        restActiviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isCreated());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeCreate + 1);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testActivite.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createActiviteWithExistingId() throws Exception {
        // Create the Activite with an existing ID
        activite.setId(1L);

        int databaseSizeBeforeCreate = activiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivites() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    void getActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get the activite
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL_ID, activite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activite.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE));
    }

    @Test
    @Transactional
    void getNonExistingActivite() throws Exception {
        // Get the activite
        restActiviteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite
        Activite updatedActivite = activiteRepository.findById(activite.getId()).get();
        // Disconnect from session so that the updates on updatedActivite are not directly saved in db
        em.detach(updatedActivite);
        updatedActivite.libelle(UPDATED_LIBELLE).date(UPDATED_DATE);

        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testActivite.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActiviteWithPatch() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite using partial update
        Activite partialUpdatedActivite = new Activite();
        partialUpdatedActivite.setId(activite.getId());

        partialUpdatedActivite.date(UPDATED_DATE);

        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testActivite.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateActiviteWithPatch() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite using partial update
        Activite partialUpdatedActivite = new Activite();
        partialUpdatedActivite.setId(activite.getId());

        partialUpdatedActivite.libelle(UPDATED_LIBELLE).date(UPDATED_DATE);

        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testActivite.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeDelete = activiteRepository.findAll().size();

        // Delete the activite
        restActiviteMockMvc
            .perform(delete(ENTITY_API_URL_ID, activite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
