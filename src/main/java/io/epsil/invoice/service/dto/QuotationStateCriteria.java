package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.epsil.invoice.domain.enumeration.QuotationStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the QuotationState entity. This class is used in QuotationStateResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /quotation-states?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuotationStateCriteria implements Serializable {
    /**
     * Class for filtering QuotationStatus
     */
    public static class QuotationStatusFilter extends Filter<QuotationStatus> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private QuotationStatusFilter status;

    private InstantFilter statusDate;

    public QuotationStateCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public QuotationStatusFilter getStatus() {
        return status;
    }

    public void setStatus(QuotationStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(InstantFilter statusDate) {
        this.statusDate = statusDate;
    }

    @Override
    public String toString() {
        return "QuotationStateCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (statusDate != null ? "statusDate=" + statusDate + ", " : "") +
            "}";
    }

}
