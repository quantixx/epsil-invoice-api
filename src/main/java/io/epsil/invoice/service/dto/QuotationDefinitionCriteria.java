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
 * Criteria class for the QuotationDefinition entity. This class is used in QuotationDefinitionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /quotation-definitions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuotationDefinitionCriteria implements Serializable {
    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter description;

    private DocumentTypeFilter docType;

    private StringFilter type;

    private FloatFilter vatRate;

    private StringFilter validityTerms;

    private StringFilter acceptation;

    private LongFilter familyId;

    private LongFilter languageId;

    private LongFilter tenantId;

    public QuotationDefinitionCriteria() {
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

    public DocumentTypeFilter getDocType() {
        return docType;
    }

    public void setDocType(DocumentTypeFilter docType) {
        this.docType = docType;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public FloatFilter getVatRate() {
        return vatRate;
    }

    public void setVatRate(FloatFilter vatRate) {
        this.vatRate = vatRate;
    }

    public StringFilter getValidityTerms() {
        return validityTerms;
    }

    public void setValidityTerms(StringFilter validityTerms) {
        this.validityTerms = validityTerms;
    }

    public StringFilter getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(StringFilter acceptation) {
        this.acceptation = acceptation;
    }

    public LongFilter getFamilyId() {
        return familyId;
    }

    public void setFamilyId(LongFilter familyId) {
        this.familyId = familyId;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "QuotationDefinitionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (docType != null ? "docType=" + docType + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (vatRate != null ? "vatRate=" + vatRate + ", " : "") +
                (validityTerms != null ? "validityTerms=" + validityTerms + ", " : "") +
                (acceptation != null ? "acceptation=" + acceptation + ", " : "") +
                (familyId != null ? "familyId=" + familyId + ", " : "") +
                (languageId != null ? "languageId=" + languageId + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            "}";
    }

}
