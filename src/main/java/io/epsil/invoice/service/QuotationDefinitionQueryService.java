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

import io.epsil.invoice.domain.QuotationDefinition;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.QuotationDefinitionRepository;
import io.epsil.invoice.service.dto.QuotationDefinitionCriteria;

import io.epsil.invoice.service.dto.QuotationDefinitionDTO;
import io.epsil.invoice.service.mapper.QuotationDefinitionMapper;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * Service for executing complex queries for QuotationDefinition entities in the database.
 * The main input is a {@link QuotationDefinitionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuotationDefinitionDTO} or a {@link Page} of {@link QuotationDefinitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationDefinitionQueryService extends QueryService<QuotationDefinition> {

    private final Logger log = LoggerFactory.getLogger(QuotationDefinitionQueryService.class);


    private final QuotationDefinitionRepository quotationDefinitionRepository;

    private final QuotationDefinitionMapper quotationDefinitionMapper;

    public QuotationDefinitionQueryService(QuotationDefinitionRepository quotationDefinitionRepository, QuotationDefinitionMapper quotationDefinitionMapper) {
        this.quotationDefinitionRepository = quotationDefinitionRepository;
        this.quotationDefinitionMapper = quotationDefinitionMapper;
    }

    /**
     * Return a {@link List} of {@link QuotationDefinitionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuotationDefinitionDTO> findByCriteria(QuotationDefinitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<QuotationDefinition> specification = createSpecification(criteria);
        return quotationDefinitionMapper.toDto(quotationDefinitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuotationDefinitionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationDefinitionDTO> findByCriteria(QuotationDefinitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<QuotationDefinition> specification = createSpecification(criteria);
        final Page<QuotationDefinition> result = quotationDefinitionRepository.findAll(specification, page);
        return result.map(quotationDefinitionMapper::toDto);
    }

    /**
     * Function to convert QuotationDefinitionCriteria to a {@link Specifications}
     */
    private Specifications<QuotationDefinition> createSpecification(QuotationDefinitionCriteria criteria) {
        Specifications<QuotationDefinition> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuotationDefinition_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), QuotationDefinition_.description));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocType(), QuotationDefinition_.docType));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), QuotationDefinition_.type));
            }
            if (criteria.getVatRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatRate(), QuotationDefinition_.vatRate));
            }
            if (criteria.getValidityTerms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValidityTerms(), QuotationDefinition_.validityTerms));
            }
            if (criteria.getAcceptation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAcceptation(), QuotationDefinition_.acceptation));
            }
            if (criteria.getFamilyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFamilyId(), QuotationDefinition_.family, Family_.id));
            }
            if (criteria.getLanguageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLanguageId(), QuotationDefinition_.language, Language_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), QuotationDefinition_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
