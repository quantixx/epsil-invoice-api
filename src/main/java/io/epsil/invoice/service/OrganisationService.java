package io.epsil.invoice.service;

import io.epsil.invoice.domain.Organisation;
import io.epsil.invoice.repository.OrganisationRepository;
import io.epsil.invoice.service.dto.OrganisationDTO;
import io.epsil.invoice.service.mapper.OrganisationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Organisation.
 */
@Service
@Transactional
public class OrganisationService {

    private final Logger log = LoggerFactory.getLogger(OrganisationService.class);

    private final OrganisationRepository organisationRepository;

    private final OrganisationMapper organisationMapper;

    public OrganisationService(OrganisationRepository organisationRepository, OrganisationMapper organisationMapper) {
        this.organisationRepository = organisationRepository;
        this.organisationMapper = organisationMapper;
    }

    /**
     * Save a organisation.
     *
     * @param organisationDTO the entity to save
     * @return the persisted entity
     */
    public OrganisationDTO save(OrganisationDTO organisationDTO) {
        log.debug("Request to save Organisation : {}", organisationDTO);
        Organisation organisation = organisationMapper.toEntity(organisationDTO);
        organisation = organisationRepository.save(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Get all the organisations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OrganisationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Organisations");
        return organisationRepository.findAll(pageable)
            .map(organisationMapper::toDto);
    }

    /**
     * Get one organisation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public OrganisationDTO findOne(Long id) {
        log.debug("Request to get Organisation : {}", id);
        Organisation organisation = organisationRepository.findOne(id);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Delete the organisation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Organisation : {}", id);
        organisationRepository.delete(id);
    }
}
