package io.epsil.invoice.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * Document
 */
@ApiModel(description = "Document")
@Entity
@Table(name = "inv_document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "document_size")
    private Integer documentSize;

    @Column(name = "docusign_envelope_id")
    private String docusignEnvelopeId;

    @Size(max = 256)
    @Column(name = "url", length = 256)
    private String url;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "created_on")
    private Instant createdOn;

    @ManyToOne
    private Language language;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Document name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Document description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public Document documentType(DocumentType documentType) {
        this.documentType = documentType;
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Integer getDocumentSize() {
        return documentSize;
    }

    public Document documentSize(Integer documentSize) {
        this.documentSize = documentSize;
        return this;
    }

    public void setDocumentSize(Integer documentSize) {
        this.documentSize = documentSize;
    }

    public String getDocusignEnvelopeId() {
        return docusignEnvelopeId;
    }

    public Document docusignEnvelopeId(String docusignEnvelopeId) {
        this.docusignEnvelopeId = docusignEnvelopeId;
        return this;
    }

    public void setDocusignEnvelopeId(String docusignEnvelopeId) {
        this.docusignEnvelopeId = docusignEnvelopeId;
    }

    public String getUrl() {
        return url;
    }

    public Document url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public Document contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Document createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Language getLanguage() {
        return language;
    }

    public Document language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Document document = (Document) o;
        if (document.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), document.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Document{" +
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
