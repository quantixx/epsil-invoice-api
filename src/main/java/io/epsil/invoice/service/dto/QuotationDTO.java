package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A DTO for the Quotation entity.
 */
public class QuotationDTO implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String description;

    @NotNull
    private DocumentType docType;

    @NotNull
    @Size(min = 1, max = 15)
    private String type;

    @NotNull
    private Float vatRate;

    @NotNull
    @Size(max = 32)
    private String validityTerms;

    @Size(max = 512)
    private String acceptionation;

    @Size(max = 50)
    private String title;

    @NotNull
    private String number;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public void setVatRate(Float vatRate) {
        this.vatRate = vatRate;
    }

    public String getValidityTerms() {
        return validityTerms;
    }

    public void setValidityTerms(String validityTerms) {
        this.validityTerms = validityTerms;
    }

    public String getAcceptionation() {
        return acceptionation;
    }

    public void setAcceptionation(String acceptionation) {
        this.acceptionation = acceptionation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

        QuotationDTO quotationDTO = (QuotationDTO) o;
        if(quotationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuotationDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", type='" + getType() + "'" +
            ", vatRate=" + getVatRate() +
            ", validityTerms='" + getValidityTerms() + "'" +
            ", acceptionation='" + getAcceptionation() + "'" +
            ", title='" + getTitle() + "'" +
            ", number='" + getNumber() + "'" +
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
