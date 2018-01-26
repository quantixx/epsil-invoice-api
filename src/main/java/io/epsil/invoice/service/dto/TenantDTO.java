package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Tenant entity.
 */
public class TenantDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    private String slug;

    @NotNull
    @Size(max = 50)
    private String name;

    private String companyName;

    @Size(max = 1024)
    private String description;

    private String vatIdentification;

    private String businessIdentification;

    private String businessRegistry;

    private String businessCode;

    private String financialYearFrom;

    private String financialYearTo;

    private Long invoiceAddressId;

    private Long bankId;

    private String bankBankName;

    private Long logoId;

    private String logoName;

    private Long contactId;

    private String contactLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVatIdentification() {
        return vatIdentification;
    }

    public void setVatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public String getBusinessIdentification() {
        return businessIdentification;
    }

    public void setBusinessIdentification(String businessIdentification) {
        this.businessIdentification = businessIdentification;
    }

    public String getBusinessRegistry() {
        return businessRegistry;
    }

    public void setBusinessRegistry(String businessRegistry) {
        this.businessRegistry = businessRegistry;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getFinancialYearFrom() {
        return financialYearFrom;
    }

    public void setFinancialYearFrom(String financialYearFrom) {
        this.financialYearFrom = financialYearFrom;
    }

    public String getFinancialYearTo() {
        return financialYearTo;
    }

    public void setFinancialYearTo(String financialYearTo) {
        this.financialYearTo = financialYearTo;
    }

    public Long getInvoiceAddressId() {
        return invoiceAddressId;
    }

    public void setInvoiceAddressId(Long addressId) {
        this.invoiceAddressId = addressId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankBankName() {
        return bankBankName;
    }

    public void setBankBankName(String bankBankName) {
        this.bankBankName = bankBankName;
    }

    public Long getLogoId() {
        return logoId;
    }

    public void setLogoId(Long documentId) {
        this.logoId = documentId;
    }

    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String documentName) {
        this.logoName = documentName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TenantDTO tenantDTO = (TenantDTO) o;
        if(tenantDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tenantDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TenantDTO{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", name='" + getName() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", description='" + getDescription() + "'" +
            ", vatIdentification='" + getVatIdentification() + "'" +
            ", businessIdentification='" + getBusinessIdentification() + "'" +
            ", businessRegistry='" + getBusinessRegistry() + "'" +
            ", businessCode='" + getBusinessCode() + "'" +
            ", financialYearFrom='" + getFinancialYearFrom() + "'" +
            ", financialYearTo='" + getFinancialYearTo() + "'" +
            "}";
    }
}
