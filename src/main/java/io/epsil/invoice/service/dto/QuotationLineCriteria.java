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
 * Criteria class for the QuotationLine entity. This class is used in QuotationLineResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /quotation-lines?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuotationLineCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter description;

    private IntegerFilter quantity;

    private FloatFilter unitCost;

    private FloatFilter subTotal;

    public QuotationLineCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public FloatFilter getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(FloatFilter unitCost) {
        this.unitCost = unitCost;
    }

    public FloatFilter getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(FloatFilter subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return "QuotationLineCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (unitCost != null ? "unitCost=" + unitCost + ", " : "") +
                (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
            "}";
    }

}
