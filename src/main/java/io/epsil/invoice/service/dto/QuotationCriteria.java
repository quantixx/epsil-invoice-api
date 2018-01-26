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
 * Criteria class for the Quotation entity. This class is used in QuotationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /quotations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuotationCriteria implements Serializable {
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

    private StringFilter acceptionation;

    private StringFilter title;

    private StringFilter number;

    private FloatFilter subTotalBeforeDiscount;

    private FloatFilter discountRate;

    private FloatFilter discountAmount;

    private FloatFilter subTotal;

    private FloatFilter vatAmount;

    private FloatFilter total;

    private StringFilter additionalInformation;

    private LongFilter familyId;

    private LongFilter invoiceId;

    private LongFilter tenantId;

    public QuotationCriteria() {
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

    public StringFilter getAcceptionation() {
        return acceptionation;
    }

    public void setAcceptionation(StringFilter acceptionation) {
        this.acceptionation = acceptionation;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public FloatFilter getSubTotalBeforeDiscount() {
        return subTotalBeforeDiscount;
    }

    public void setSubTotalBeforeDiscount(FloatFilter subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
    }

    public FloatFilter getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(FloatFilter discountRate) {
        this.discountRate = discountRate;
    }

    public FloatFilter getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(FloatFilter discountAmount) {
        this.discountAmount = discountAmount;
    }

    public FloatFilter getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(FloatFilter subTotal) {
        this.subTotal = subTotal;
    }

    public FloatFilter getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(FloatFilter vatAmount) {
        this.vatAmount = vatAmount;
    }

    public FloatFilter getTotal() {
        return total;
    }

    public void setTotal(FloatFilter total) {
        this.total = total;
    }

    public StringFilter getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(StringFilter additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public LongFilter getFamilyId() {
        return familyId;
    }

    public void setFamilyId(LongFilter familyId) {
        this.familyId = familyId;
    }

    public LongFilter getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(LongFilter invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "QuotationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (docType != null ? "docType=" + docType + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (vatRate != null ? "vatRate=" + vatRate + ", " : "") +
                (validityTerms != null ? "validityTerms=" + validityTerms + ", " : "") +
                (acceptionation != null ? "acceptionation=" + acceptionation + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (subTotalBeforeDiscount != null ? "subTotalBeforeDiscount=" + subTotalBeforeDiscount + ", " : "") +
                (discountRate != null ? "discountRate=" + discountRate + ", " : "") +
                (discountAmount != null ? "discountAmount=" + discountAmount + ", " : "") +
                (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
                (vatAmount != null ? "vatAmount=" + vatAmount + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (additionalInformation != null ? "additionalInformation=" + additionalInformation + ", " : "") +
                (familyId != null ? "familyId=" + familyId + ", " : "") +
                (invoiceId != null ? "invoiceId=" + invoiceId + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            "}";
    }

}
