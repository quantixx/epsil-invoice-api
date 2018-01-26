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
 * Criteria class for the Tenant entity. This class is used in TenantResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /tenants?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TenantCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter slug;

    private StringFilter name;

    private StringFilter companyName;

    private StringFilter description;

    private StringFilter vatIdentification;

    private StringFilter businessIdentification;

    private StringFilter businessRegistry;

    private StringFilter businessCode;

    private StringFilter financialYearFrom;

    private StringFilter financialYearTo;

    private LongFilter invoiceAddressId;

    private LongFilter bankId;

    private LongFilter logoId;

    private LongFilter contactId;

    private LongFilter eventsId;

    private LongFilter invoicesId;

    private LongFilter invoiceDefinitionsId;

    private LongFilter quotationsId;

    private LongFilter quotationDefinitionsId;

    private LongFilter familiesId;

    public TenantCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCompanyName() {
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getVatIdentification() {
        return vatIdentification;
    }

    public void setVatIdentification(StringFilter vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public StringFilter getBusinessIdentification() {
        return businessIdentification;
    }

    public void setBusinessIdentification(StringFilter businessIdentification) {
        this.businessIdentification = businessIdentification;
    }

    public StringFilter getBusinessRegistry() {
        return businessRegistry;
    }

    public void setBusinessRegistry(StringFilter businessRegistry) {
        this.businessRegistry = businessRegistry;
    }

    public StringFilter getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(StringFilter businessCode) {
        this.businessCode = businessCode;
    }

    public StringFilter getFinancialYearFrom() {
        return financialYearFrom;
    }

    public void setFinancialYearFrom(StringFilter financialYearFrom) {
        this.financialYearFrom = financialYearFrom;
    }

    public StringFilter getFinancialYearTo() {
        return financialYearTo;
    }

    public void setFinancialYearTo(StringFilter financialYearTo) {
        this.financialYearTo = financialYearTo;
    }

    public LongFilter getInvoiceAddressId() {
        return invoiceAddressId;
    }

    public void setInvoiceAddressId(LongFilter invoiceAddressId) {
        this.invoiceAddressId = invoiceAddressId;
    }

    public LongFilter getBankId() {
        return bankId;
    }

    public void setBankId(LongFilter bankId) {
        this.bankId = bankId;
    }

    public LongFilter getLogoId() {
        return logoId;
    }

    public void setLogoId(LongFilter logoId) {
        this.logoId = logoId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getEventsId() {
        return eventsId;
    }

    public void setEventsId(LongFilter eventsId) {
        this.eventsId = eventsId;
    }

    public LongFilter getInvoicesId() {
        return invoicesId;
    }

    public void setInvoicesId(LongFilter invoicesId) {
        this.invoicesId = invoicesId;
    }

    public LongFilter getInvoiceDefinitionsId() {
        return invoiceDefinitionsId;
    }

    public void setInvoiceDefinitionsId(LongFilter invoiceDefinitionsId) {
        this.invoiceDefinitionsId = invoiceDefinitionsId;
    }

    public LongFilter getQuotationsId() {
        return quotationsId;
    }

    public void setQuotationsId(LongFilter quotationsId) {
        this.quotationsId = quotationsId;
    }

    public LongFilter getQuotationDefinitionsId() {
        return quotationDefinitionsId;
    }

    public void setQuotationDefinitionsId(LongFilter quotationDefinitionsId) {
        this.quotationDefinitionsId = quotationDefinitionsId;
    }

    public LongFilter getFamiliesId() {
        return familiesId;
    }

    public void setFamiliesId(LongFilter familiesId) {
        this.familiesId = familiesId;
    }

    @Override
    public String toString() {
        return "TenantCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (companyName != null ? "companyName=" + companyName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (vatIdentification != null ? "vatIdentification=" + vatIdentification + ", " : "") +
                (businessIdentification != null ? "businessIdentification=" + businessIdentification + ", " : "") +
                (businessRegistry != null ? "businessRegistry=" + businessRegistry + ", " : "") +
                (businessCode != null ? "businessCode=" + businessCode + ", " : "") +
                (financialYearFrom != null ? "financialYearFrom=" + financialYearFrom + ", " : "") +
                (financialYearTo != null ? "financialYearTo=" + financialYearTo + ", " : "") +
                (invoiceAddressId != null ? "invoiceAddressId=" + invoiceAddressId + ", " : "") +
                (bankId != null ? "bankId=" + bankId + ", " : "") +
                (logoId != null ? "logoId=" + logoId + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
                (eventsId != null ? "eventsId=" + eventsId + ", " : "") +
                (invoicesId != null ? "invoicesId=" + invoicesId + ", " : "") +
                (invoiceDefinitionsId != null ? "invoiceDefinitionsId=" + invoiceDefinitionsId + ", " : "") +
                (quotationsId != null ? "quotationsId=" + quotationsId + ", " : "") +
                (quotationDefinitionsId != null ? "quotationDefinitionsId=" + quotationDefinitionsId + ", " : "") +
                (familiesId != null ? "familiesId=" + familiesId + ", " : "") +
            "}";
    }

}
