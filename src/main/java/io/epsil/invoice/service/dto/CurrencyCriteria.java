package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Currency entity. This class is used in CurrencyResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /currencies?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CurrencyCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter entity;

    private StringFilter currency;

    private StringFilter alphabeticCode;

    private IntegerFilter numericCode;

    private IntegerFilter minorUnit;

    private StringFilter symbol;

    private BooleanFilter activated;

    public CurrencyCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEntity() {
        return entity;
    }

    public void setEntity(StringFilter entity) {
        this.entity = entity;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getAlphabeticCode() {
        return alphabeticCode;
    }

    public void setAlphabeticCode(StringFilter alphabeticCode) {
        this.alphabeticCode = alphabeticCode;
    }

    public IntegerFilter getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(IntegerFilter numericCode) {
        this.numericCode = numericCode;
    }

    public IntegerFilter getMinorUnit() {
        return minorUnit;
    }

    public void setMinorUnit(IntegerFilter minorUnit) {
        this.minorUnit = minorUnit;
    }

    public StringFilter getSymbol() {
        return symbol;
    }

    public void setSymbol(StringFilter symbol) {
        this.symbol = symbol;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "CurrencyCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (entity != null ? "entity=" + entity + ", " : "") +
                (currency != null ? "currency=" + currency + ", " : "") +
                (alphabeticCode != null ? "alphabeticCode=" + alphabeticCode + ", " : "") +
                (numericCode != null ? "numericCode=" + numericCode + ", " : "") +
                (minorUnit != null ? "minorUnit=" + minorUnit + ", " : "") +
                (symbol != null ? "symbol=" + symbol + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
            "}";
    }

}
