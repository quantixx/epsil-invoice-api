package io.epsil.invoice.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.InvoiceStatus;

/**
 * A DTO for the InvoiceState entity.
 */
public class InvoiceStateDTO implements Serializable {

    private Long id;

    @NotNull
    private InvoiceStatus status;

    @NotNull
    private Instant statusDate;

    private Long invoiceId;

    private String invoiceNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Instant getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Instant statusDate) {
        this.statusDate = statusDate;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceStateDTO invoiceStateDTO = (InvoiceStateDTO) o;
        if(invoiceStateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceStateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceStateDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", statusDate='" + getStatusDate() + "'" +
            "}";
    }
}
