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

import io.epsil.invoice.domain.Language;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.LanguageRepository;
import io.epsil.invoice.service.dto.LanguageCriteria;

import io.epsil.invoice.service.dto.LanguageDTO;
import io.epsil.invoice.service.mapper.LanguageMapper;

/**
 * Service for executing complex queries for Language entities in the database.
 * The main input is a {@link LanguageCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LanguageDTO} or a {@link Page} of {@link LanguageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LanguageQueryService extends QueryService<Language> {

    private final Logger log = LoggerFactory.getLogger(LanguageQueryService.class);


    private final LanguageRepository languageRepository;

    private final LanguageMapper languageMapper;

    public LanguageQueryService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    /**
     * Return a {@link List} of {@link LanguageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LanguageDTO> findByCriteria(LanguageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Language> specification = createSpecification(criteria);
        return languageMapper.toDto(languageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LanguageDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LanguageDTO> findByCriteria(LanguageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Language> specification = createSpecification(criteria);
        final Page<Language> result = languageRepository.findAll(specification, page);
        return result.map(languageMapper::toDto);
    }

    /**
     * Function to convert LanguageCriteria to a {@link Specifications}
     */
    private Specifications<Language> createSpecification(LanguageCriteria criteria) {
        Specifications<Language> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Language_.id));
            }
            if (criteria.getAlpha3b() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlpha3b(), Language_.alpha3b));
            }
            if (criteria.getAlpha2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlpha2(), Language_.alpha2));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Language_.name));
            }
            if (criteria.getFlag32() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlag32(), Language_.flag32));
            }
            if (criteria.getFlag128() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlag128(), Language_.flag128));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Language_.activated));
            }
        }
        return specification;
    }

}
