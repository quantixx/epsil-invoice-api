package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A DTO for the QuotationDefinition entity.
 */
public class QuotationDefinitionDTO implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String description;

    @NotNull
    private DocumentType docType;

    @NotNull
    @Size(min = 1, max = 15)
    private String type;

    private Float vatRate;

    @NotNull
    @Size(max = 32)
    private String validityTerms;

    @Size(max = 512)
    private String acceptation;

    private Long familyId;

    private String familyName;

    private Long languageId;

    private String languageName;

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

    public String getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(String acceptation) {
        this.acceptation = acceptation;
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

        QuotationDefinitionDTO quotationDefinitionDTO = (QuotationDefinitionDTO) o;
        if(quotationDefinitionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotationDefinitionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuotationDefinitionDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", type='" + getType() + "'" +
            ", vatRate=" + getVatRate() +
            ", validityTerms='" + getValidityTerms() + "'" +
            ", acceptation='" + getAcceptation() + "'" +
            "}";
    }
}
