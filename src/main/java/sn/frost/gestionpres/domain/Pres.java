package sn.frost.gestionpres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Pres.
 */
@Entity
@Table(name = "pres")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pres implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "date")
    private String date;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "restant")
    private Double restant;

    @Column(name = "payer")
    private Double payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pres", "remboursers", "depenses" }, allowSetters = true)
    private Fournisseur fournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pres id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Pres libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDate() {
        return this.date;
    }

    public Pres date(String date) {
        this.setDate(date);
        return this;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getMontant() {
        return this.montant;
    }

    public Pres montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Pres fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pres)) {
            return false;
        }
        return id != null && id.equals(((Pres) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Pres{" +
            "id=" +
            id +
            ", libelle='" +
            libelle +
            '\'' +
            ", date='" +
            date +
            '\'' +
            ", montant=" +
            montant +
            ", restant=" +
            restant +
            ", payer=" +
            payer +
            ", fournisseur=" +
            fournisseur +
            '}'
        );
    }

    public Double getRestant() {
        return restant;
    }

    public void setRestant(Double restant) {
        this.restant = restant;
    }

    public Double getPayer() {
        return payer;
    }

    public void setPayer(Double payer) {
        this.payer = payer;
    }
}
