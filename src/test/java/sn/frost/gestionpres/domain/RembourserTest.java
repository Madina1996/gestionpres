package sn.frost.gestionpres.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.frost.gestionpres.web.rest.TestUtil;

class RembourserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rembourser.class);
        Rembourser rembourser1 = new Rembourser();
        rembourser1.setId(1L);
        Rembourser rembourser2 = new Rembourser();
        rembourser2.setId(rembourser1.getId());
        assertThat(rembourser1).isEqualTo(rembourser2);
        rembourser2.setId(2L);
        assertThat(rembourser1).isNotEqualTo(rembourser2);
        rembourser1.setId(null);
        assertThat(rembourser1).isNotEqualTo(rembourser2);
    }
}
