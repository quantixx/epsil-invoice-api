package io.epsil.invoice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A InvoiceDefinition.
 */
@Entity
@Table(name = "inv_invoice_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvoiceDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(name = "title", length = 50)
    private String title;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocumentType docType;

    @NotNull
    @Column(name = "vat_rate", nullable = false)
    private Float vatRate;

    @NotNull
    @Size(max = 32)
    @Column(name = "terms", length = 32, nullable = false)
    private String terms;

    @NotNull
    @Size(max = 512)
    @Column(name = "penalties", length = 512, nullable = false)
    private String penalties;

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

    public String getTitle() {
        return title;
    }

    public InvoiceDefinition title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public InvoiceDefinition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public InvoiceDefinition docType(DocumentType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public InvoiceDefinition vatRate(Float vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(Float vatRate) {
        this.vatRate = vatRate;
    }

    public String getTerms() {
        return terms;
    }

    public InvoiceDefinition terms(String terms) {
        this.terms = terms;
        return this;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPenalties() {
        return penalties;
    }

    public InvoiceDefinition penalties(String penalties) {
        this.penalties = penalties;
        return this;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public Family getFamily() {
        return family;
    }

    public InvoiceDefinition family(Family family) {
        this.family = family;
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Language getLanguage() {
        return language;
    }

    public InvoiceDefinition language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public InvoiceDefinition tenant(Tenant tenant) {
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
        InvoiceDefinition invoiceDefinition = (InvoiceDefinition) o;
        if (invoiceDefinition.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDefinition.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDefinition{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", vatRate=" + getVatRate() +
            ", terms='" + getTerms() + "'" +
            ", penalties='" + getPenalties() + "'" +
            "}";
    }
}
