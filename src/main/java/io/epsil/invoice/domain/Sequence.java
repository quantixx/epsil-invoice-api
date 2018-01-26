package io.epsil.invoice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A Sequence.
 */
@Entity
@Table(name = "inv_sequence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sequence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocumentType docType;

    @NotNull
    @Column(name = "inv_next", nullable = false)
    private Integer next;

    @ManyToOne(optional = false)
    @NotNull
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    private Family family;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public Sequence docType(DocumentType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Integer getNext() {
        return next;
    }

    public Sequence next(Integer next) {
        this.next = next;
        return this;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Sequence tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Family getFamily() {
        return family;
    }

    public Sequence family(Family family) {
        this.family = family;
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
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
        Sequence sequence = (Sequence) o;
        if (sequence.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sequence.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sequence{" +
            "id=" + getId() +
            ", docType='" + getDocType() + "'" +
            ", next=" + getNext() +
            "}";
    }
}
