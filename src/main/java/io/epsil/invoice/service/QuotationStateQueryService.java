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

import io.epsil.invoice.domain.QuotationState;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.QuotationStateRepository;
import io.epsil.invoice.service.dto.QuotationStateCriteria;

import io.epsil.invoice.service.dto.QuotationStateDTO;
import io.epsil.invoice.service.mapper.QuotationStateMapper;
import io.epsil.invoice.domain.enumeration.QuotationStatus;

/**
 * Service for executing complex queries for QuotationState entities in the database.
 * The main input is a {@link QuotationStateCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuotationStateDTO} or a {@link Page} of {@link QuotationStateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationStateQueryService extends QueryService<QuotationState> {

    private final Logger log = LoggerFactory.getLogger(QuotationStateQueryService.class);


    private final QuotationStateRepository quotationStateRepository;

    private final QuotationStateMapper quotationStateMapper;

    public QuotationStateQueryService(QuotationStateRepository quotationStateRepository, QuotationStateMapper quotationStateMapper) {
        this.quotationStateRepository = quotationStateRepository;
        this.quotationStateMapper = quotationStateMapper;
    }

    /**
     * Return a {@link List} of {@link QuotationStateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuotationStateDTO> findByCriteria(QuotationStateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<QuotationState> specification = createSpecification(criteria);
        return quotationStateMapper.toDto(quotationStateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuotationStateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationStateDTO> findByCriteria(QuotationStateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<QuotationState> specification = createSpecification(criteria);
        final Page<QuotationState> result = quotationStateRepository.findAll(specification, page);
        return result.map(quotationStateMapper::toDto);
    }

    /**
     * Function to convert QuotationStateCriteria to a {@link Specifications}
     */
    private Specifications<QuotationState> createSpecification(QuotationStateCriteria criteria) {
        Specifications<QuotationState> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuotationState_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), QuotationState_.status));
            }
            if (criteria.getStatusDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatusDate(), QuotationState_.statusDate));
            }
        }
        return specification;
    }

}
