package sn.frost.gestionpres.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Compte.
 */
@Entity
@Table(name = "compte")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "restant")
    private Double restant;

    @Column(name = "payer")
    private Double payer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Compte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRestant() {
        return this.restant;
    }

    public Compte restant(Double restant) {
        this.setRestant(restant);
        return this;
    }

    public void setRestant(Double restant) {
        this.restant = restant;
    }

    public Double getPayer() {
        return this.payer;
    }

    public Compte payer(Double payer) {
        this.setPayer(payer);
        return this;
    }

    public void setPayer(Double payer) {
        this.payer = payer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Compte)) {
            return false;
        }
        return id != null && id.equals(((Compte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Compte{" +
            "id=" + getId() +
            ", restant=" + getRestant() +
            ", payer=" + getPayer() +
            "}";
    }
}
