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

import io.epsil.invoice.domain.InvoiceDefinition;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.InvoiceDefinitionRepository;
import io.epsil.invoice.service.dto.InvoiceDefinitionCriteria;

import io.epsil.invoice.service.dto.InvoiceDefinitionDTO;
import io.epsil.invoice.service.mapper.InvoiceDefinitionMapper;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * Service for executing complex queries for InvoiceDefinition entities in the database.
 * The main input is a {@link InvoiceDefinitionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceDefinitionDTO} or a {@link Page} of {@link InvoiceDefinitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceDefinitionQueryService extends QueryService<InvoiceDefinition> {

    private final Logger log = LoggerFactory.getLogger(InvoiceDefinitionQueryService.class);


    private final InvoiceDefinitionRepository invoiceDefinitionRepository;

    private final InvoiceDefinitionMapper invoiceDefinitionMapper;

    public InvoiceDefinitionQueryService(InvoiceDefinitionRepository invoiceDefinitionRepository, InvoiceDefinitionMapper invoiceDefinitionMapper) {
        this.invoiceDefinitionRepository = invoiceDefinitionRepository;
        this.invoiceDefinitionMapper = invoiceDefinitionMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceDefinitionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceDefinitionDTO> findByCriteria(InvoiceDefinitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<InvoiceDefinition> specification = createSpecification(criteria);
        return invoiceDefinitionMapper.toDto(invoiceDefinitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceDefinitionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDefinitionDTO> findByCriteria(InvoiceDefinitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<InvoiceDefinition> specification = createSpecification(criteria);
        final Page<InvoiceDefinition> result = invoiceDefinitionRepository.findAll(specification, page);
        return result.map(invoiceDefinitionMapper::toDto);
    }

    /**
     * Function to convert InvoiceDefinitionCriteria to a {@link Specifications}
     */
    private Specifications<InvoiceDefinition> createSpecification(InvoiceDefinitionCriteria criteria) {
        Specifications<InvoiceDefinition> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InvoiceDefinition_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), InvoiceDefinition_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), InvoiceDefinition_.description));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocType(), InvoiceDefinition_.docType));
            }
            if (criteria.getVatRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatRate(), InvoiceDefinition_.vatRate));
            }
            if (criteria.getTerms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTerms(), InvoiceDefinition_.terms));
            }
            if (criteria.getPenalties() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPenalties(), InvoiceDefinition_.penalties));
            }
            if (criteria.getFamilyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFamilyId(), InvoiceDefinition_.family, Family_.id));
            }
            if (criteria.getLanguageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLanguageId(), InvoiceDefinition_.language, Language_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), InvoiceDefinition_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
