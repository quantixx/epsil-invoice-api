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

import io.epsil.invoice.domain.Bank;
import io.epsil.invoice.domain.*; // for static metamodels
import io.epsil.invoice.repository.BankRepository;
import io.epsil.invoice.service.dto.BankCriteria;

import io.epsil.invoice.service.dto.BankDTO;
import io.epsil.invoice.service.mapper.BankMapper;

/**
 * Service for executing complex queries for Bank entities in the database.
 * The main input is a {@link BankCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BankDTO} or a {@link Page} of {@link BankDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BankQueryService extends QueryService<Bank> {

    private final Logger log = LoggerFactory.getLogger(BankQueryService.class);


    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    public BankQueryService(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    /**
     * Return a {@link List} of {@link BankDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BankDTO> findByCriteria(BankCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Bank> specification = createSpecification(criteria);
        return bankMapper.toDto(bankRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BankDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BankDTO> findByCriteria(BankCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Bank> specification = createSpecification(criteria);
        final Page<Bank> result = bankRepository.findAll(specification, page);
        return result.map(bankMapper::toDto);
    }

    /**
     * Function to convert BankCriteria to a {@link Specifications}
     */
    private Specifications<Bank> createSpecification(BankCriteria criteria) {
        Specifications<Bank> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Bank_.id));
            }
            if (criteria.getBankName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankName(), Bank_.bankName));
            }
            if (criteria.getAgencyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgencyName(), Bank_.agencyName));
            }
            if (criteria.getBankAccount() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankAccount(), Bank_.bankAccount));
            }
            if (criteria.getIban() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIban(), Bank_.iban));
            }
            if (criteria.getBic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBic(), Bank_.bic));
            }
            if (criteria.getPhoneArea() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneArea(), Bank_.phoneArea));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Bank_.phoneNumber));
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getAddressId(), Bank_.address, Address_.id));
            }
        }
        return specification;
    }

}
