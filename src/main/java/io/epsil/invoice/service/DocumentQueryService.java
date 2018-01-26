package io.epsil.invoice.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.epsil.invoice.domain.Document;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.DocumentRepository;
import io.epsil.invoice.service.dto.DocumentCriteria;

import io.epsil.invoice.service.dto.DocumentDTO;
import io.epsil.invoice.service.mapper.DocumentMapper;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * Service for executing complex queries for Document entities in the database.
 * The main input is a {@link DocumentCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DocumentDTO} or a {@link Page} of {@link DocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentQueryService extends QueryService<Document> {

    private final Logger log = LoggerFactory.getLogger(DocumentQueryService.class);


    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    public DocumentQueryService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    /**
     * Return a {@link List} of {@link DocumentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentDTO> findByCriteria(DocumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Document> specification = createSpecification(criteria);
        return documentMapper.toDto(documentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DocumentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Document> specification = createSpecification(criteria);
        final Page<Document> result = documentRepository.findAll(specification, page);
        return result.map(documentMapper::toDto);
    }

    /**
     * Function to convert DocumentCriteria to a {@link Specifications}
     */
    private Specifications<Document> createSpecification(DocumentCriteria criteria) {
        Specifications<Document> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Document_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Document_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Document_.description));
            }
            if (criteria.getDocumentType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocumentType(), Document_.documentType));
            }
            if (criteria.getDocumentSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDocumentSize(), Document_.documentSize));
            }
            if (criteria.getDocusignEnvelopeId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocusignEnvelopeId(), Document_.docusignEnvelopeId));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Document_.url));
            }
            if (criteria.getContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentType(), Document_.contentType));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Document_.createdOn));
            }
            if (criteria.getLanguageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLanguageId(), Document_.language, Language_.id));
            }
        }
        return specification;
    }

}
