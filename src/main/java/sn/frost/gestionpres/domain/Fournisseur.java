package sn.frost.gestionpres.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "prenom_nom")
    private String prenomNom;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "service_offert")
    private String serviceOffert;

    @Column(name = "solde")
    private Double solde;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "fournisseur" }, allowSetters = true)
    private Set<Pres> pres = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "fournisseur" }, allowSetters = true)
    private Set<Rembourser> remboursers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fournisseur")
    @JsonIgnoreProperties(value = { "fournisseur" }, allowSetters = true)
    private Set<Depense> depenses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fournisseur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenomNom() {
        return this.prenomNom;
    }

    public Fournisseur prenomNom(String prenomNom) {
        this.setPrenomNom(prenomNom);
        return this;
    }

    public void setPrenomNom(String prenomNom) {
        this.prenomNom = prenomNom;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Fournisseur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getServiceOffert() {
        return this.serviceOffert;
    }

    public Fournisseur serviceOffert(String serviceOffert) {
        this.setServiceOffert(serviceOffert);
        return this;
    }

    public void setServiceOffert(String serviceOffert) {
        this.serviceOffert = serviceOffert;
    }

    public Double getSolde() {
        return this.solde;
    }

    public Fournisseur solde(Double solde) {
        this.setSolde(solde);
        return this;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public Set<Pres> getPres() {
        return this.pres;
    }

    public void setPres(Set<Pres> pres) {
        if (this.pres != null) {
            this.pres.forEach(i -> i.setFournisseur(null));
        }
        if (pres != null) {
            pres.forEach(i -> i.setFournisseur(this));
        }
        this.pres = pres;
    }

    public Fournisseur pres(Set<Pres> pres) {
        this.setPres(pres);
        return this;
    }

    public Fournisseur addPres(Pres pres) {
        this.pres.add(pres);
        pres.setFournisseur(this);
        return this;
    }

    public Fournisseur removePres(Pres pres) {
        this.pres.remove(pres);
        pres.setFournisseur(null);
        return this;
    }

    public Set<Rembourser> getRemboursers() {
        return this.remboursers;
    }

    public void setRemboursers(Set<Rembourser> remboursers) {
        if (this.remboursers != null) {
            this.remboursers.forEach(i -> i.setFournisseur(null));
        }
        if (remboursers != null) {
            remboursers.forEach(i -> i.setFournisseur(this));
        }
        this.remboursers = remboursers;
    }

    public Fournisseur remboursers(Set<Rembourser> remboursers) {
        this.setRemboursers(remboursers);
        return this;
    }

    public Fournisseur addRembourser(Rembourser rembourser) {
        this.remboursers.add(rembourser);
        rembourser.setFournisseur(this);
        return this;
    }

    public Fournisseur removeRembourser(Rembourser rembourser) {
        this.remboursers.remove(rembourser);
        rembourser.setFournisseur(null);
        return this;
    }

    public Set<Depense> getDepenses() {
        return this.depenses;
    }

    public void setDepenses(Set<Depense> depenses) {
        if (this.depenses != null) {
            this.depenses.forEach(i -> i.setFournisseur(null));
        }
        if (depenses != null) {
            depenses.forEach(i -> i.setFournisseur(this));
        }
        this.depenses = depenses;
    }

    public Fournisseur depenses(Set<Depense> depenses) {
        this.setDepenses(depenses);
        return this;
    }

    public Fournisseur addDepense(Depense depense) {
        this.depenses.add(depense);
        depense.setFournisseur(this);
        return this;
    }

    public Fournisseur removeDepense(Depense depense) {
        this.depenses.remove(depense);
        depense.setFournisseur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", prenomNom='" + getPrenomNom() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", serviceOffert='" + getServiceOffert() + "'" +
            ", solde=" + getSolde() +
            "}";
    }
}
