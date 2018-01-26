package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.epsil.invoice.domain.enumeration.DocumentType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Sequence entity. This class is used in SequenceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sequences?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SequenceCriteria implements Serializable {
    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private DocumentTypeFilter docType;

    private IntegerFilter next;

    private LongFilter tenantId;

    private LongFilter familyId;

    public SequenceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DocumentTypeFilter getDocType() {
        return docType;
    }

    public void setDocType(DocumentTypeFilter docType) {
        this.docType = docType;
    }

    public IntegerFilter getNext() {
        return next;
    }

    public void setNext(IntegerFilter next) {
        this.next = next;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    public LongFilter getFamilyId() {
        return familyId;
    }

    public void setFamilyId(LongFilter familyId) {
        this.familyId = familyId;
    }

    @Override
    public String toString() {
        return "SequenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (docType != null ? "docType=" + docType + ", " : "") +
                (next != null ? "next=" + next + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
                (familyId != null ? "familyId=" + familyId + ", " : "") +
            "}";
    }

}
