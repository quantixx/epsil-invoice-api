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

import io.epsil.invoice.domain.InvoiceLine;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.InvoiceLineRepository;
import io.epsil.invoice.service.dto.InvoiceLineCriteria;

import io.epsil.invoice.service.dto.InvoiceLineDTO;
import io.epsil.invoice.service.mapper.InvoiceLineMapper;

/**
 * Service for executing complex queries for InvoiceLine entities in the database.
 * The main input is a {@link InvoiceLineCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceLineDTO} or a {@link Page} of {@link InvoiceLineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceLineQueryService extends QueryService<InvoiceLine> {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineQueryService.class);


    private final InvoiceLineRepository invoiceLineRepository;

    private final InvoiceLineMapper invoiceLineMapper;

    public InvoiceLineQueryService(InvoiceLineRepository invoiceLineRepository, InvoiceLineMapper invoiceLineMapper) {
        this.invoiceLineRepository = invoiceLineRepository;
        this.invoiceLineMapper = invoiceLineMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceLineDTO> findByCriteria(InvoiceLineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<InvoiceLine> specification = createSpecification(criteria);
        return invoiceLineMapper.toDto(invoiceLineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceLineDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceLineDTO> findByCriteria(InvoiceLineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<InvoiceLine> specification = createSpecification(criteria);
        final Page<InvoiceLine> result = invoiceLineRepository.findAll(specification, page);
        return result.map(invoiceLineMapper::toDto);
    }

    /**
     * Function to convert InvoiceLineCriteria to a {@link Specifications}
     */
    private Specifications<InvoiceLine> createSpecification(InvoiceLineCriteria criteria) {
        Specifications<InvoiceLine> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InvoiceLine_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), InvoiceLine_.description));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), InvoiceLine_.quantity));
            }
            if (criteria.getUnitCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitCost(), InvoiceLine_.unitCost));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), InvoiceLine_.subTotal));
            }
            if (criteria.getInvoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceId(), InvoiceLine_.invoice, Invoice_.id));
            }
        }
        return specification;
    }

}
