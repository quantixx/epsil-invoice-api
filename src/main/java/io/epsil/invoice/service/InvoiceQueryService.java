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

import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.InvoiceRepository;
import io.epsil.invoice.service.dto.InvoiceCriteria;

import io.epsil.invoice.service.dto.InvoiceDTO;
import io.epsil.invoice.service.mapper.InvoiceMapper;
import io.epsil.invoice.domain.enumeration.DocumentType;
import io.epsil.invoice.domain.enumeration.InvoiceType;

/**
 * Service for executing complex queries for Invoice entities in the database.
 * The main input is a {@link InvoiceCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InvoiceDTO} or a {@link Page} of {@link InvoiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InvoiceQueryService extends QueryService<Invoice> {

    private final Logger log = LoggerFactory.getLogger(InvoiceQueryService.class);


    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public InvoiceQueryService(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    /**
     * Return a {@link List} of {@link InvoiceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InvoiceDTO> findByCriteria(InvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Invoice> specification = createSpecification(criteria);
        return invoiceMapper.toDto(invoiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InvoiceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findByCriteria(InvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Invoice> specification = createSpecification(criteria);
        final Page<Invoice> result = invoiceRepository.findAll(specification, page);
        return result.map(invoiceMapper::toDto);
    }

    /**
     * Function to convert InvoiceCriteria to a {@link Specifications}
     */
    private Specifications<Invoice> createSpecification(InvoiceCriteria criteria) {
        Specifications<Invoice> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Invoice_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Invoice_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Invoice_.description));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocType(), Invoice_.docType));
            }
            if (criteria.getVatRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatRate(), Invoice_.vatRate));
            }
            if (criteria.getTerms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTerms(), Invoice_.terms));
            }
            if (criteria.getPenalties() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPenalties(), Invoice_.penalties));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Invoice_.number));
            }
            if (criteria.getInvoiceType() != null) {
                specification = specification.and(buildSpecification(criteria.getInvoiceType(), Invoice_.invoiceType));
            }
            if (criteria.getPoNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPoNumber(), Invoice_.poNumber));
            }
            if (criteria.getSubTotalBeforeDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotalBeforeDiscount(), Invoice_.subTotalBeforeDiscount));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), Invoice_.discountRate));
            }
            if (criteria.getDiscountAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountAmount(), Invoice_.discountAmount));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), Invoice_.subTotal));
            }
            if (criteria.getVatAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatAmount(), Invoice_.vatAmount));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Invoice_.total));
            }
            if (criteria.getAdditionalInformation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdditionalInformation(), Invoice_.additionalInformation));
            }
            if (criteria.getLinkedId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinkedId(), Invoice_.linked, Invoice_.id));
            }
            if (criteria.getOrganisationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getOrganisationId(), Invoice_.organisation, Organisation_.id));
            }
            if (criteria.getQuotationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getQuotationId(), Invoice_.quotation, Quotation_.id));
            }
            if (criteria.getDocumentId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getDocumentId(), Invoice_.document, Document_.id));
            }
            if (criteria.getStatesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getStatesId(), Invoice_.states, InvoiceState_.id));
            }
            if (criteria.getLinesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLinesId(), Invoice_.lines, InvoiceLine_.id));
            }
            if (criteria.getLanguageId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLanguageId(), Invoice_.language, Language_.id));
            }
            if (criteria.getCurrencyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCurrencyId(), Invoice_.currency, Currency_.id));
            }
            if (criteria.getFamilyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFamilyId(), Invoice_.family, Family_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), Invoice_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
