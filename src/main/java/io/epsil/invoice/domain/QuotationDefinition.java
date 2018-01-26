package io.epsil.invoice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A QuotationDefinition.
 */
@Entity
@Table(name = "inv_quotation_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuotationDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocumentType docType;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "inv_type", length = 15, nullable = false)
    private String type;

    @Column(name = "vat_rate")
    private Float vatRate;

    @NotNull
    @Size(max = 32)
    @Column(name = "validity_terms", length = 32, nullable = false)
    private String validityTerms;

    @Size(max = 512)
    @Column(name = "acceptation", length = 512)
    private String acceptation;

    @ManyToOne(optional = false)
    @NotNull
    private Family family;

    @ManyToOne(optional = false)
    @NotNull
    private Language language;

    @ManyToOne(optional = false)
    @NotNull
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public QuotationDefinition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public QuotationDefinition docType(DocumentType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public String getType() {
        return type;
    }

    public QuotationDefinition type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public QuotationDefinition vatRate(Float vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(Float vatRate) {
        this.vatRate = vatRate;
    }

    public String getValidityTerms() {
        return validityTerms;
    }

    public QuotationDefinition validityTerms(String validityTerms) {
        this.validityTerms = validityTerms;
        return this;
    }

    public void setValidityTerms(String validityTerms) {
        this.validityTerms = validityTerms;
    }

    public String getAcceptation() {
        return acceptation;
    }

    public QuotationDefinition acceptation(String acceptation) {
        this.acceptation = acceptation;
        return this;
    }

    public void setAcceptation(String acceptation) {
        this.acceptation = acceptation;
    }

    public Family getFamily() {
        return family;
    }

    public QuotationDefinition family(Family family) {
        this.family = family;
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Language getLanguage() {
        return language;
    }

    public QuotationDefinition language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public QuotationDefinition tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuotationDefinition quotationDefinition = (QuotationDefinition) o;
        if (quotationDefinition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotationDefinition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuotationDefinition{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", type='" + getType() + "'" +
            ", vatRate=" + getVatRate() +
            ", validityTerms='" + getValidityTerms() + "'" +
            ", acceptation='" + getAcceptation() + "'" +
            "}";
    }
}
