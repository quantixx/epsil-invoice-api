package io.epsil.invoice.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A DTO for the Document entity.
 */
public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Size(max = 1024)
    private String description;

    private DocumentType documentType;

    private Integer documentSize;

    private String docusignEnvelopeId;

    @Size(max = 256)
    private String url;

    private String contentType;

    private Instant createdOn;

    private Long languageId;

    private String languageName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Integer getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(Integer documentSize) {
        this.documentSize = documentSize;
    }

    public String getDocusignEnvelopeId() {
        return docusignEnvelopeId;
    }

    public void setDocusignEnvelopeId(String docusignEnvelopeId) {
        this.docusignEnvelopeId = docusignEnvelopeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if(documentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", documentType='" + getDocumentType() + "'" +
            ", documentSize=" + getDocumentSize() +
            ", docusignEnvelopeId='" + getDocusignEnvelopeId() + "'" +
            ", url='" + getUrl() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
