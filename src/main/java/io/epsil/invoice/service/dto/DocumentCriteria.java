package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.epsil.invoice.domain.enumeration.DocumentType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Document entity. This class is used in DocumentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /documents?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DocumentCriteria implements Serializable {
    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private DocumentTypeFilter documentType;

    private IntegerFilter documentSize;

    private StringFilter docusignEnvelopeId;

    private StringFilter url;

    private StringFilter contentType;

    private InstantFilter createdOn;

    private LongFilter languageId;

    public DocumentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DocumentTypeFilter getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentTypeFilter documentType) {
        this.documentType = documentType;
    }

    public IntegerFilter getDocumentSize() {
        return documentSize;
    }

    public void setDocumentSize(IntegerFilter documentSize) {
        this.documentSize = documentSize;
    }

    public StringFilter getDocusignEnvelopeId() {
        return docusignEnvelopeId;
    }

    public void setDocusignEnvelopeId(StringFilter docusignEnvelopeId) {
        this.docusignEnvelopeId = docusignEnvelopeId;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
    }

    @Override
    public String toString() {
        return "DocumentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (documentType != null ? "documentType=" + documentType + ", " : "") +
                (documentSize != null ? "documentSize=" + documentSize + ", " : "") +
                (docusignEnvelopeId != null ? "docusignEnvelopeId=" + docusignEnvelopeId + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (contentType != null ? "contentType=" + contentType + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (languageId != null ? "languageId=" + languageId + ", " : "") +
            "}";
    }

}
