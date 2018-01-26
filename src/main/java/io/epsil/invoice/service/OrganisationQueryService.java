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

import io.epsil.invoice.domain.Organisation;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.OrganisationRepository;
import io.epsil.invoice.service.dto.OrganisationCriteria;

import io.epsil.invoice.service.dto.OrganisationDTO;
import io.epsil.invoice.service.mapper.OrganisationMapper;

/**
 * Service for executing complex queries for Organisation entities in the database.
 * The main input is a {@link OrganisationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganisationDTO} or a {@link Page} of {@link OrganisationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganisationQueryService extends QueryService<Organisation> {

    private final Logger log = LoggerFactory.getLogger(OrganisationQueryService.class);


    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    public OrganisationQueryService(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
    }

    /**
     * Return a {@link List} of {@link OrganisationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganisationDTO> findByCriteria(OrganisationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Organisation> specification = createSpecification(criteria);
        return organisationMapper.toDto(organisationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrganisationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganisationDTO> findByCriteria(OrganisationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Organisation> specification = createSpecification(criteria);
        final Page<Organisation> result = organisationRepository.findAll(specification, page);
        return result.map(organisationMapper::toDto);
    }

    /**
     * Function to convert OrganisationCriteria to a {@link Specifications}
     */
    private Specifications<Organisation> createSpecification(OrganisationCriteria criteria) {
        Specifications<Organisation> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Organisation_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Organisation_.slug));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Organisation_.name));
            }
            if (criteria.getBusinessIdentification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessIdentification(), Organisation_.businessIdentification));
            }
            if (criteria.getCorporateName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorporateName(), Organisation_.corporateName));
            }
            if (criteria.getVatIdentification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVatIdentification(), Organisation_.vatIdentification));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Organisation_.activated));
            }
            if (criteria.getCreatedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedOn(), Organisation_.createdOn));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAddressId(), Organisation_.address, Address_.id));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactId(), Organisation_.contact, Contact_.id));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), Organisation_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
