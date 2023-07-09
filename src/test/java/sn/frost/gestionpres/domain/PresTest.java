package sn.frost.gestionpres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.frost.gestionpres.web.rest.TestUtil;

class PresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pres.class);
        Pres pres1 = new Pres();
        pres1.setId(1L);
        Pres pres2 = new Pres();
        pres2.setId(pres1.getId());
        assertThat(pres1).isEqualTo(pres2);
        pres2.setId(2L);
        assertThat(pres1).isNotEqualTo(pres2);
        pres1.setId(null);
        assertThat(pres1).isNotEqualTo(pres2);
    }
}
