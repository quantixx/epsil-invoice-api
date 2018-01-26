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

import io.epsil.invoice.domain.Country;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.CountryRepository;
import io.epsil.invoice.service.dto.CountryCriteria;

import io.epsil.invoice.service.dto.CountryDTO;
import io.epsil.invoice.service.mapper.CountryMapper;
import io.epsil.invoice.domain.enumeration.Continent;

/**
 * Service for executing complex queries for Country entities in the database.
 * The main input is a {@link CountryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountryDTO} or a {@link Page} of {@link CountryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountryQueryService extends QueryService<Country> {

    private final Logger log = LoggerFactory.getLogger(CountryQueryService.class);


    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    public CountryQueryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    /**
     * Return a {@link List} of {@link CountryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountryDTO> findByCriteria(CountryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Country> specification = createSpecification(criteria);
        return countryMapper.toDto(countryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryDTO> findByCriteria(CountryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Country> specification = createSpecification(criteria);
        final Page<Country> result = countryRepository.findAll(specification, page);
        return result.map(countryMapper::toDto);
    }

    /**
     * Function to convert CountryCriteria to a {@link Specifications}
     */
    private Specifications<Country> createSpecification(CountryCriteria criteria) {
        Specifications<Country> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Country_.id));
            }
            if (criteria.getIso2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIso2(), Country_.iso2));
            }
            if (criteria.getIso3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIso3(), Country_.iso3));
            }
            if (criteria.getm49() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getm49(), Country_.m49));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Country_.name));
            }
            if (criteria.getOfficialNameEn() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficialNameEn(), Country_.officialNameEn));
            }
            if (criteria.getOfficialNameFr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfficialNameFr(), Country_.officialNameFr));
            }
            if (criteria.getDial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDial(), Country_.dial));
            }
            if (criteria.getContinent() != null) {
                specification = specification.and(buildSpecification(criteria.getContinent(), Country_.continent));
            }
            if (criteria.getTld() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTld(), Country_.tld));
            }
            if (criteria.getFlag32() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlag32(), Country_.flag32));
            }
            if (criteria.getFlag128() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlag128(), Country_.flag128));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLatitude(), Country_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLongitude(), Country_.longitude));
            }
            if (criteria.getZoom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZoom(), Country_.zoom));
            }
        }
        return specification;
    }

}
