package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A DTO for the InvoiceDefinition entity.
 */
public class InvoiceDefinitionDTO implements Serializable {

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

        InvoiceDefinitionDTO invoiceDefinitionDTO = (InvoiceDefinitionDTO) o;
        if(invoiceDefinitionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoiceDefinitionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InvoiceDefinitionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", vatRate=" + getVatRate() +
            ", terms='" + getTerms() + "'" +
            ", penalties='" + getPenalties() + "'" +
            "}";
    }
}
