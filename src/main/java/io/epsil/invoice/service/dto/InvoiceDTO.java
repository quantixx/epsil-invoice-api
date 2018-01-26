package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.DocumentType;
import io.epsil.invoice.domain.enumeration.InvoiceType;

/**
 * A DTO for the Invoice entity.
 */
public class InvoiceDTO implements Serializable {

    private Long id;

    @Size(max = 50)
    private String title;

    @Size(max = 1024)
    private String description;

    @NotNull
    private DocumentType docType;

    @NotNull
    private Float vatRate;

    @NotNull
    @Size(max = 32)
    private String terms;

    @NotNull
    @Size(max = 512)
    private String penalties;

    @NotNull
    private String number;

    @NotNull
    private InvoiceType invoiceType;

    private String poNumber;

    private Float subTotalBeforeDiscount;

    private Float discountRate;

    private Float discountAmount;

    @NotNull
    private Float subTotal;

    @NotNull
    private Float vatAmount;

    @NotNull
    private Float total;

    private String additionalInformation;

    private Long linkedId;

    private String linkedNumber;

    private Long organisationId;

    private String organisationName;

    private Long quotationId;

    private String quotationNumber;

    private Long documentId;

    private String documentName;

    private Long languageId;

    private String languageName;

    private Long currencyId;

    private String currencyCurrency;

    private Long familyId;

    private String familyName;

    private Long tenantId;

    private String tenantName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public void setVatRate(Float vatRate) {
        this.vatRate = vatRate;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPenalties() {
        return penalties;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Float getSubTotalBeforeDiscount() {
        return subTotalBeforeDiscount;
    }

    public void setSubTotalBeforeDiscount(Float subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
    }

    public Float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Float discountRate) {
        this.discountRate = discountRate;
    }

    public Float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(Float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Long getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(Long invoiceId) {
        this.linkedId = invoiceId;
    }

    public String getLinkedNumber() {
        return linkedNumber;
    }

    public void setLinkedNumber(String invoiceNumber) {
        this.linkedNumber = invoiceNumber;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCurrency() {
        return currencyCurrency;
    }

    public void setCurrencyCurrency(String currencyCurrency) {
        this.currencyCurrency = currencyCurrency;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if(invoiceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", vatRate=" + getVatRate() +
            ", terms='" + getTerms() + "'" +
            ", penalties='" + getPenalties() + "'" +
            ", number='" + getNumber() + "'" +
            ", invoiceType='" + getInvoiceType() + "'" +
            ", poNumber='" + getPoNumber() + "'" +
            ", subTotalBeforeDiscount=" + getSubTotalBeforeDiscount() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", subTotal=" + getSubTotal() +
            ", vatAmount=" + getVatAmount() +
            ", total=" + getTotal() +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            "}";
    }
}
