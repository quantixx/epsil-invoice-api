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

import io.epsil.invoice.domain.Contact;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.ContactRepository;
import io.epsil.invoice.service.dto.ContactCriteria;

import io.epsil.invoice.service.dto.ContactDTO;
import io.epsil.invoice.service.mapper.ContactMapper;

/**
 * Service for executing complex queries for Contact entities in the database.
 * The main input is a {@link ContactCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactDTO} or a {@link Page} of {@link ContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactQueryService extends QueryService<Contact> {

    private final Logger log = LoggerFactory.getLogger(ContactQueryService.class);


    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactQueryService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Return a {@link List} of {@link ContactDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> findByCriteria(ContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Contact> specification = createSpecification(criteria);
        return contactMapper.toDto(contactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactDTO> findByCriteria(ContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Contact> specification = createSpecification(criteria);
        final Page<Contact> result = contactRepository.findAll(specification, page);
        return result.map(contactMapper::toDto);
    }

    /**
     * Function to convert ContactCriteria to a {@link Specifications}
     */
    private Specifications<Contact> createSpecification(ContactCriteria criteria) {
        Specifications<Contact> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Contact_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Contact_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Contact_.lastName));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Contact_.activated));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Contact_.email));
            }
            if (criteria.getPhoneArea() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneArea(), Contact_.phoneArea));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Contact_.phoneNumber));
            }
        }
        return specification;
    }

}
