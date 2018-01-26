package io.epsil.invoice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.InvoiceStatus;

/**
 * A InvoiceState.
 */
@Entity
@Table(name = "inv_invoice_state")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvoiceState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;

    @NotNull
    @Column(name = "status_date", nullable = false)
    private Instant statusDate;

    @ManyToOne(optional = false)
    @NotNull
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public InvoiceState status(InvoiceStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Instant getStatusDate() {
        return statusDate;
    }

    public InvoiceState statusDate(Instant statusDate) {
        this.statusDate = statusDate;
        return this;
    }

    public void setStatusDate(Instant statusDate) {
        this.statusDate = statusDate;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public InvoiceState invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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
        InvoiceState invoiceState = (InvoiceState) o;
        if (invoiceState.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceState.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceState{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", statusDate='" + getStatusDate() + "'" +
            "}";
    }
}
