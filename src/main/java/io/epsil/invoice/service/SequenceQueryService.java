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

import io.epsil.invoice.domain.Sequence;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.SequenceRepository;
import io.epsil.invoice.service.dto.SequenceCriteria;

import io.epsil.invoice.service.dto.SequenceDTO;
import io.epsil.invoice.service.mapper.SequenceMapper;
import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * Service for executing complex queries for Sequence entities in the database.
 * The main input is a {@link SequenceCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SequenceDTO} or a {@link Page} of {@link SequenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SequenceQueryService extends QueryService<Sequence> {

    private final Logger log = LoggerFactory.getLogger(SequenceQueryService.class);


    private final SequenceRepository sequenceRepository;

    private final SequenceMapper sequenceMapper;

    public SequenceQueryService(SequenceRepository sequenceRepository, SequenceMapper sequenceMapper) {
        this.sequenceRepository = sequenceRepository;
        this.sequenceMapper = sequenceMapper;
    }

    /**
     * Return a {@link List} of {@link SequenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SequenceDTO> findByCriteria(SequenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Sequence> specification = createSpecification(criteria);
        return sequenceMapper.toDto(sequenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SequenceDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SequenceDTO> findByCriteria(SequenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Sequence> specification = createSpecification(criteria);
        final Page<Sequence> result = sequenceRepository.findAll(specification, page);
        return result.map(sequenceMapper::toDto);
    }

    /**
     * Function to convert SequenceCriteria to a {@link Specifications}
     */
    private Specifications<Sequence> createSpecification(SequenceCriteria criteria) {
        Specifications<Sequence> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sequence_.id));
            }
            if (criteria.getDocType() != null) {
                specification = specification.and(buildSpecification(criteria.getDocType(), Sequence_.docType));
            }
            if (criteria.getNext() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNext(), Sequence_.next));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), Sequence_.tenant, Tenant_.id));
            }
            if (criteria.getFamilyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFamilyId(), Sequence_.family, Family_.id));
            }
        }
        return specification;
    }

}
