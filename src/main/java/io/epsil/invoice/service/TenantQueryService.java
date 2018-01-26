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

import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.TenantRepository;
import io.epsil.invoice.service.dto.TenantCriteria;

import io.epsil.invoice.service.dto.TenantDTO;
import io.epsil.invoice.service.mapper.TenantMapper;

/**
 * Service for executing complex queries for Tenant entities in the database.
 * The main input is a {@link TenantCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TenantDTO} or a {@link Page} of {@link TenantDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantQueryService extends QueryService<Tenant> {

    private final Logger log = LoggerFactory.getLogger(TenantQueryService.class);


    private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    public TenantQueryService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    /**
     * Return a {@link List} of {@link TenantDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TenantDTO> findByCriteria(TenantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Tenant> specification = createSpecification(criteria);
        return tenantMapper.toDto(tenantRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TenantDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantDTO> findByCriteria(TenantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Tenant> specification = createSpecification(criteria);
        final Page<Tenant> result = tenantRepository.findAll(specification, page);
        return result.map(tenantMapper::toDto);
    }

    /**
     * Function to convert TenantCriteria to a {@link Specifications}
     */
    private Specifications<Tenant> createSpecification(TenantCriteria criteria) {
        Specifications<Tenant> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Tenant_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Tenant_.slug));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Tenant_.name));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), Tenant_.companyName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Tenant_.description));
            }
            if (criteria.getVatIdentification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVatIdentification(), Tenant_.vatIdentification));
            }
            if (criteria.getBusinessIdentification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessIdentification(), Tenant_.businessIdentification));
            }
            if (criteria.getBusinessRegistry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessRegistry(), Tenant_.businessRegistry));
            }
            if (criteria.getBusinessCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessCode(), Tenant_.businessCode));
            }
            if (criteria.getFinancialYearFrom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFinancialYearFrom(), Tenant_.financialYearFrom));
            }
            if (criteria.getFinancialYearTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFinancialYearTo(), Tenant_.financialYearTo));
            }
            if (criteria.getInvoiceAddressId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceAddressId(), Tenant_.invoiceAddress, Address_.id));
            }
            if (criteria.getBankId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getBankId(), Tenant_.bank, Bank_.id));
            }
            if (criteria.getLogoId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLogoId(), Tenant_.logo, Document_.id));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactId(), Tenant_.contact, Contact_.id));
            }
            if (criteria.getEventsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getEventsId(), Tenant_.events, Event_.id));
            }
            if (criteria.getInvoicesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoicesId(), Tenant_.invoices, Invoice_.id));
            }
            if (criteria.getInvoiceDefinitionsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInvoiceDefinitionsId(), Tenant_.invoiceDefinitions, InvoiceDefinition_.id));
            }
            if (criteria.getQuotationsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getQuotationsId(), Tenant_.quotations, Quotation_.id));
            }
            if (criteria.getQuotationDefinitionsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getQuotationDefinitionsId(), Tenant_.quotationDefinitions, QuotationDefinition_.id));
            }
            if (criteria.getFamiliesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFamiliesId(), Tenant_.families, Family_.id));
            }
        }
        return specification;
    }

}
