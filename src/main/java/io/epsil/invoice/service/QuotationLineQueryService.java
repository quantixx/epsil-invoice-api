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

import io.epsil.invoice.domain.QuotationLine;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.QuotationLineRepository;
import io.epsil.invoice.service.dto.QuotationLineCriteria;

import io.epsil.invoice.service.dto.QuotationLineDTO;
import io.epsil.invoice.service.mapper.QuotationLineMapper;

/**
 * Service for executing complex queries for QuotationLine entities in the database.
 * The main input is a {@link QuotationLineCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuotationLineDTO} or a {@link Page} of {@link QuotationLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationLineQueryService extends QueryService<QuotationLine> {

    private final Logger log = LoggerFactory.getLogger(QuotationLineQueryService.class);


    private final QuotationLineRepository quotationLineRepository;

    private final QuotationLineMapper quotationLineMapper;

    public QuotationLineQueryService(QuotationLineRepository quotationLineRepository, QuotationLineMapper quotationLineMapper) {
        this.quotationLineRepository = quotationLineRepository;
        this.quotationLineMapper = quotationLineMapper;
    }

    /**
     * Return a {@link List} of {@link QuotationLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuotationLineDTO> findByCriteria(QuotationLineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<QuotationLine> specification = createSpecification(criteria);
        return quotationLineMapper.toDto(quotationLineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuotationLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationLineDTO> findByCriteria(QuotationLineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<QuotationLine> specification = createSpecification(criteria);
        final Page<QuotationLine> result = quotationLineRepository.findAll(specification, page);
        return result.map(quotationLineMapper::toDto);
    }

    /**
     * Function to convert QuotationLineCriteria to a {@link Specifications}
     */
    private Specifications<QuotationLine> createSpecification(QuotationLineCriteria criteria) {
        Specifications<QuotationLine> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), QuotationLine_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), QuotationLine_.description));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), QuotationLine_.quantity));
            }
            if (criteria.getUnitCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitCost(), QuotationLine_.unitCost));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), QuotationLine_.subTotal));
            }
        }
        return specification;
    }

}
