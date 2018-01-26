package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Currency entity.
 */
public class CurrencyDTO implements Serializable {

    private Long id;

    @NotNull
    private String entity;

    @NotNull
    private String currency;

    @NotNull
    @Size(max = 3)
    private String alphabeticCode;

    @NotNull
    private Integer numericCode;

    @NotNull
    private Integer minorUnit;

    private String symbol;

    private Boolean activated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAlphabeticCode() {
        return alphabeticCode;
    }

    public void setAlphabeticCode(String alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    public Integer getMinorUnit() {
        return minorUnit;
    }

    public void setMinorUnit(Integer minorUnit) {
        this.minorUnit = minorUnit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyDTO currencyDTO = (CurrencyDTO) o;
        if(currencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
            "id=" + getId() +
            ", entity='" + getEntity() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", alphabeticCode='" + getAlphabeticCode() + "'" +
            ", numericCode=" + getNumericCode() +
            ", minorUnit=" + getMinorUnit() +
            ", symbol='" + getSymbol() + "'" +
            ", activated='" + isActivated() + "'" +
            "}";
    }
}
