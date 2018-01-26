package io.epsil.invoice.service;

import io.epsil.invoice.domain.Family;
import io.epsil.invoice.repository.FamilyRepository;
import io.epsil.invoice.service.dto.FamilyDTO;
import io.epsil.invoice.service.mapper.FamilyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Family.
 */
@Service
@Transactional
public class FamilyService {

    private final Logger log = LoggerFactory.getLogger(FamilyService.class);

    private final FamilyRepository familyRepository;

    private final FamilyMapper familyMapper;

    public FamilyService(FamilyRepository familyRepository, FamilyMapper familyMapper) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
    }

    /**
     * Save a family.
     *
     * @param familyDTO the entity to save
     * @return the persisted entity
     */
    public FamilyDTO save(FamilyDTO familyDTO) {
        log.debug("Request to save Family : {}", familyDTO);
        Family family = familyMapper.toEntity(familyDTO);
        family = familyRepository.save(family);
        return familyMapper.toDto(family);
    }

    /**
     * Get all the families.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FamilyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Families");
        return familyRepository.findAll(pageable)
            .map(familyMapper::toDto);
    }

    /**
     * Get one family by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FamilyDTO findOne(Long id) {
        log.debug("Request to get Family : {}", id);
        Family family = familyRepository.findOne(id);
        return familyMapper.toDto(family);
    }

    /**
     * Delete the family by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Family : {}", id);
        familyRepository.delete(id);
    }
}
