package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the QuotationLine entity.
 */
public class QuotationLineDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private Float unitCost;

    @NotNull
    private Float subTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Float unitCost) {
        this.unitCost = unitCost;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuotationLineDTO quotationLineDTO = (QuotationLineDTO) o;
        if(quotationLineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotationLineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuotationLineDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", unitCost=" + getUnitCost() +
            ", subTotal=" + getSubTotal() +
            "}";
    }
}
