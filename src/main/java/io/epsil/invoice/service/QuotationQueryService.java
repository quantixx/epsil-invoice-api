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

import io.epsil.invoice.domain.Quotation;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.QuotationRepository;
import io.epsil.invoice.service.dto.QuotationCriteria;

import io.epsil.invoice.service.dto.QuotationDTO;
import io.epsil.invoice.service.mapper.QuotationMapper;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * Service for executing complex queries for Quotation entities in the database.
 * The main input is a {@link QuotationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuotationDTO} or a {@link Page} of {@link QuotationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationQueryService extends QueryService<Quotation> {

    private final Logger log = LoggerFactory.getLogger(QuotationQueryService.class);


    private final QuotationRepository quotationRepository;

    private final QuotationMapper quotationMapper;

    public QuotationQueryService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    /**
     * Return a {@link List} of {@link QuotationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuotationDTO> findByCriteria(QuotationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Quotation> specification = createSpecification(criteria);
        return quotationMapper.toDto(quotationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuotationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationDTO> findByCriteria(QuotationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Quotation> specification = createSpecification(criteria);
        final Page<Quotation> result = quotationRepository.findAll(specification, page);
        return result.map(quotationMapper::toDto);
    }

    /**
     * Function to convert QuotationCriteria to a {@link Specifications}
     */
    private Specifications<Quotation> createSpecification(QuotationCriteria criteria) {
        Specifications<Quotation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Quotation_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Quotation_.description));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocType(), Quotation_.docType));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Quotation_.type));
            }
            if (criteria.getVatRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatRate(), Quotation_.vatRate));
            }
            if (criteria.getValidityTerms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValidityTerms(), Quotation_.validityTerms));
            }
            if (criteria.getAcceptionation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAcceptionation(), Quotation_.acceptionation));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Quotation_.title));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumber(), Quotation_.number));
            }
            if (criteria.getSubTotalBeforeDiscount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotalBeforeDiscount(), Quotation_.subTotalBeforeDiscount));
            }
            if (criteria.getDiscountRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountRate(), Quotation_.discountRate));
            }
            if (criteria.getDiscountAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountAmount(), Quotation_.discountAmount));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), Quotation_.subTotal));
            }
            if (criteria.getVatAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVatAmount(), Quotation_.vatAmount));
            }
            if (criteria.getTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotal(), Quotation_.total));
            }
            if (criteria.getAdditionalInformation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdditionalInformation(), Quotation_.additionalInformation));
            }
            if (criteria.getFamilyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFamilyId(), Quotation_.family, Family_.id));
            }
            if (criteria.getInvoiceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceId(), Quotation_.invoice, Invoice_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), Quotation_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
