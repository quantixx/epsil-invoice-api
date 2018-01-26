package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.epsil.invoice.domain.enumeration.DocumentType;
import io.epsil.invoice.domain.enumeration.InvoiceType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Invoice entity. This class is used in InvoiceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /invoices?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InvoiceCriteria implements Serializable {
    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {
    }

    /**
     * Class for filtering InvoiceType
     */
    public static class InvoiceTypeFilter extends Filter<InvoiceType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private DocumentTypeFilter docType;

    private FloatFilter vatRate;

    private StringFilter terms;

    private StringFilter penalties;

    private StringFilter number;

    private InvoiceTypeFilter invoiceType;

    private StringFilter poNumber;

    private FloatFilter subTotalBeforeDiscount;

    private FloatFilter discountRate;

    private FloatFilter discountAmount;

    private FloatFilter subTotal;

    private FloatFilter vatAmount;

    private FloatFilter total;

    private StringFilter additionalInformation;

    private LongFilter linkedId;

    private LongFilter organisationId;

    private LongFilter quotationId;

    private LongFilter documentId;

    private LongFilter statesId;

    private LongFilter linesId;

    private LongFilter languageId;

    private LongFilter currencyId;

    private LongFilter familyId;

    private LongFilter tenantId;

    public InvoiceCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
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

    public FloatFilter getVatRate() {
        return vatRate;
    }

    public void setVatRate(FloatFilter vatRate) {
        this.vatRate = vatRate;
    }

    public StringFilter getTerms() {
        return terms;
    }

    public void setTerms(StringFilter terms) {
        this.terms = terms;
    }

    public StringFilter getPenalties() {
        return penalties;
    }

    public void setPenalties(StringFilter penalties) {
        this.penalties = penalties;
    }

    public StringFilter getNumber() {
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
    }

    public InvoiceTypeFilter getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceTypeFilter invoiceType) {
        this.invoiceType = invoiceType;
    }

    public StringFilter getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(StringFilter poNumber) {
        this.poNumber = poNumber;
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

    public LongFilter getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(LongFilter linkedId) {
        this.linkedId = linkedId;
    }

    public LongFilter getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(LongFilter organisationId) {
        this.organisationId = organisationId;
    }

    public LongFilter getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(LongFilter quotationId) {
        this.quotationId = quotationId;
    }

    public LongFilter getDocumentId() {
        return documentId;
    }

    public void setDocumentId(LongFilter documentId) {
        this.documentId = documentId;
    }

    public LongFilter getStatesId() {
        return statesId;
    }

    public void setStatesId(LongFilter statesId) {
        this.statesId = statesId;
    }

    public LongFilter getLinesId() {
        return linesId;
    }

    public void setLinesId(LongFilter linesId) {
        this.linesId = linesId;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    public LongFilter getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(LongFilter currencyId) {
        this.currencyId = currencyId;
    }

    public LongFilter getFamilyId() {
        return familyId;
    }

    public void setFamilyId(LongFilter familyId) {
        this.familyId = familyId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "InvoiceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (docType != null ? "docType=" + docType + ", " : "") +
                (vatRate != null ? "vatRate=" + vatRate + ", " : "") +
                (terms != null ? "terms=" + terms + ", " : "") +
                (penalties != null ? "penalties=" + penalties + ", " : "") +
                (number != null ? "number=" + number + ", " : "") +
                (invoiceType != null ? "invoiceType=" + invoiceType + ", " : "") +
                (poNumber != null ? "poNumber=" + poNumber + ", " : "") +
                (subTotalBeforeDiscount != null ? "subTotalBeforeDiscount=" + subTotalBeforeDiscount + ", " : "") +
                (discountRate != null ? "discountRate=" + discountRate + ", " : "") +
                (discountAmount != null ? "discountAmount=" + discountAmount + ", " : "") +
                (subTotal != null ? "subTotal=" + subTotal + ", " : "") +
                (vatAmount != null ? "vatAmount=" + vatAmount + ", " : "") +
                (total != null ? "total=" + total + ", " : "") +
                (additionalInformation != null ? "additionalInformation=" + additionalInformation + ", " : "") +
                (linkedId != null ? "linkedId=" + linkedId + ", " : "") +
                (organisationId != null ? "organisationId=" + organisationId + ", " : "") +
                (quotationId != null ? "quotationId=" + quotationId + ", " : "") +
                (documentId != null ? "documentId=" + documentId + ", " : "") +
                (statesId != null ? "statesId=" + statesId + ", " : "") +
                (linesId != null ? "linesId=" + linesId + ", " : "") +
                (languageId != null ? "languageId=" + languageId + ", " : "") +
                (currencyId != null ? "currencyId=" + currencyId + ", " : "") +
                (familyId != null ? "familyId=" + familyId + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            "}";
    }

}
