package io.epsil.invoice.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.QuotationStatus;

/**
 * A DTO for the QuotationState entity.
 */
public class QuotationStateDTO implements Serializable {

    private Long id;

    @NotNull
    private QuotationStatus status;

    @NotNull
    private Instant statusDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuotationStatus getStatus() {
        return status;
    }

    public void setStatus(QuotationStatus status) {
        this.status = status;
    }

    public Instant getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Instant statusDate) {
        this.statusDate = statusDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuotationStateDTO quotationStateDTO = (QuotationStateDTO) o;
        if(quotationStateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotationStateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuotationStateDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", statusDate='" + getStatusDate() + "'" +
            "}";
    }
}
