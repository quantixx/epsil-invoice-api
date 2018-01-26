package io.epsil.invoice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Currency.
 */
@Entity
@Table(name = "inv_currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "entity", nullable = false)
    private String entity;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Size(max = 3)
    @Column(name = "alphabetic_code", length = 3, nullable = false)
    private String alphabeticCode;

    @NotNull
    @Column(name = "numeric_code", nullable = false)
    private Integer numericCode;

    @NotNull
    @Column(name = "minor_unit", nullable = false)
    private Integer minorUnit;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "activated")
    private Boolean activated;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public Currency entity(String entity) {
        this.entity = entity;
        return this;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getCurrency() {
        return currency;
    }

    public Currency currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAlphabeticCode() {
        return alphabeticCode;
    }

    public Currency alphabeticCode(String alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
        return this;
    }

    public void setAlphabeticCode(String alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public Currency numericCode(Integer numericCode) {
        this.numericCode = numericCode;
        return this;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    public Integer getMinorUnit() {
        return minorUnit;
    }

    public Currency minorUnit(Integer minorUnit) {
        this.minorUnit = minorUnit;
        return this;
    }

    public void setMinorUnit(Integer minorUnit) {
        this.minorUnit = minorUnit;
    }

    public String getSymbol() {
        return symbol;
    }

    public Currency symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Currency activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
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
        Currency currency = (Currency) o;
        if (currency.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currency.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Currency{" +
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
