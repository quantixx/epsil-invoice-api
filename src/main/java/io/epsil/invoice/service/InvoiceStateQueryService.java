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

import io.epsil.invoice.domain.InvoiceState;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.InvoiceStateRepository;
import io.epsil.invoice.service.dto.InvoiceStateCriteria;

import io.epsil.invoice.service.dto.InvoiceStateDTO;
import io.epsil.invoice.service.mapper.InvoiceStateMapper;
import io.epsil.invoice.domain.enumeration.InvoiceStatus;

/**
 * Service for executing complex queries for InvoiceState entities in the database.
 * The main input is a {@link InvoiceStateCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceStateDTO} or a {@link Page} of {@link InvoiceStateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceStateQueryService extends QueryService<InvoiceState> {

    private final Logger log = LoggerFactory.getLogger(InvoiceStateQueryService.class);


    private final InvoiceStateRepository invoiceStateRepository;

    private final InvoiceStateMapper invoiceStateMapper;

    public InvoiceStateQueryService(InvoiceStateRepository invoiceStateRepository, InvoiceStateMapper invoiceStateMapper) {
        this.invoiceStateRepository = invoiceStateRepository;
        this.invoiceStateMapper = invoiceStateMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceStateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceStateDTO> findByCriteria(InvoiceStateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<InvoiceState> specification = createSpecification(criteria);
        return invoiceStateMapper.toDto(invoiceStateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceStateDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceStateDTO> findByCriteria(InvoiceStateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<InvoiceState> specification = createSpecification(criteria);
        final Page<InvoiceState> result = invoiceStateRepository.findAll(specification, page);
        return result.map(invoiceStateMapper::toDto);
    }

    /**
     * Function to convert InvoiceStateCriteria to a {@link Specifications}
     */
    private Specifications<InvoiceState> createSpecification(InvoiceStateCriteria criteria) {
        Specifications<InvoiceState> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), InvoiceState_.id));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), InvoiceState_.status));
            }
            if (criteria.getStatusDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatusDate(), InvoiceState_.statusDate));
            }
            if (criteria.getInvoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceId(), InvoiceState_.invoice, Invoice_.id));
            }
        }
        return specification;
    }

}
