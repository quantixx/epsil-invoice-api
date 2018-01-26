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

import io.epsil.invoice.domain.Family;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.FamilyRepository;
import io.epsil.invoice.service.dto.FamilyCriteria;

import io.epsil.invoice.service.dto.FamilyDTO;
import io.epsil.invoice.service.mapper.FamilyMapper;

/**
 * Service for executing complex queries for Family entities in the database.
 * The main input is a {@link FamilyCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FamilyDTO} or a {@link Page} of {@link FamilyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilyQueryService extends QueryService<Family> {

    private final Logger log = LoggerFactory.getLogger(FamilyQueryService.class);


    private final FamilyRepository familyRepository;

    private final FamilyMapper familyMapper;

    public FamilyQueryService(FamilyRepository familyRepository, FamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
    }

    /**
     * Return a {@link List} of {@link FamilyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FamilyDTO> findByCriteria(FamilyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Family> specification = createSpecification(criteria);
        return familyMapper.toDto(familyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FamilyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FamilyDTO> findByCriteria(FamilyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Family> specification = createSpecification(criteria);
        final Page<Family> result = familyRepository.findAll(specification, page);
        return result.map(familyMapper::toDto);
    }

    /**
     * Function to convert FamilyCriteria to a {@link Specifications}
     */
    private Specifications<Family> createSpecification(FamilyCriteria criteria) {
        Specifications<Family> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Family_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Family_.slug));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Family_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Family_.description));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), Family_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
