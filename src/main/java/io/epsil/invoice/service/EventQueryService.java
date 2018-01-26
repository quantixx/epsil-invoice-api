package io.epsil.invoice.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.epsil.invoice.domain.Event;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.EventRepository;
import io.epsil.invoice.service.dto.EventCriteria;

import io.epsil.invoice.service.dto.EventDTO;
import io.epsil.invoice.service.mapper.EventMapper;

/**
 * Service for executing complex queries for Event entities in the database.
 * The main input is a {@link EventCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventDTO} or a {@link Page} of {@link EventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventQueryService extends QueryService<Event> {

    private final Logger log = LoggerFactory.getLogger(EventQueryService.class);


    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    public EventQueryService(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    /**
     * Return a {@link List} of {@link EventDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventDTO> findByCriteria(EventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Event> specification = createSpecification(criteria);
        return eventMapper.toDto(eventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EventDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventDTO> findByCriteria(EventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Event> specification = createSpecification(criteria);
        final Page<Event> result = eventRepository.findAll(specification, page);
        return result.map(eventMapper::toDto);
    }

    /**
     * Function to convert EventCriteria to a {@link Specifications}
     */
    private Specifications<Event> createSpecification(EventCriteria criteria) {
        Specifications<Event> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Event_.id));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Event_.slug));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Event_.name));
            }
            if (criteria.getVirtual() != null) {
                specification = specification.and(buildSpecification(criteria.getVirtual(), Event_.virtual));
            }
            if (criteria.getStartsOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartsOn(), Event_.startsOn));
            }
            if (criteria.getEndsOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndsOn(), Event_.endsOn));
            }
            if (criteria.getTenantId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTenantId(), Event_.tenant, Tenant_.id));
            }
        }
        return specification;
    }

}
