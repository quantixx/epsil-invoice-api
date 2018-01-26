package io.epsil.invoice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A DTO for the Sequence entity.
 */
public class SequenceDTO implements Serializable {

    private Long id;

    @NotNull
    private DocumentType docType;

    @NotNull
    private Integer next;

    private Long tenantId;

    private String tenantName;

    private Long familyId;

    private String familyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SequenceDTO sequenceDTO = (SequenceDTO) o;
        if(sequenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sequenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SequenceDTO{" +
            "id=" + getId() +
            ", docType='" + getDocType() + "'" +
            ", next=" + getNext() +
            "}";
    }
}
