package sn.frost.gestionpres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.frost.gestionpres.web.rest.TestUtil;

class CompteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compte.class);
        Compte compte1 = new Compte();
        compte1.setId(1L);
        Compte compte2 = new Compte();
        compte2.setId(compte1.getId());
        assertThat(compte1).isEqualTo(compte2);
        compte2.setId(2L);
        assertThat(compte1).isNotEqualTo(compte2);
        compte1.setId(null);
        assertThat(compte1).isNotEqualTo(compte2);
    }
}
