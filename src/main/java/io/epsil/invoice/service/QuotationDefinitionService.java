package io.epsil.invoice.service;

import io.epsil.invoice.domain.QuotationDefinition;
import io.epsil.invoice.repository.QuotationDefinitionRepository;
import io.epsil.invoice.service.dto.QuotationDefinitionDTO;
import io.epsil.invoice.service.mapper.QuotationDefinitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuotationDefinition.
 */
@Service
@Transactional
public class QuotationDefinitionService {

    private final Logger log = LoggerFactory.getLogger(QuotationDefinitionService.class);

    private final QuotationDefinitionRepository quotationDefinitionRepository;

    private final QuotationDefinitionMapper quotationDefinitionMapper;

    public QuotationDefinitionService(QuotationDefinitionRepository quotationDefinitionRepository, QuotationDefinitionMapper quotationDefinitionMapper) {
        this.quotationDefinitionRepository = quotationDefinitionRepository;
        this.quotationDefinitionMapper = quotationDefinitionMapper;
    }

    /**
     * Save a quotationDefinition.
     *
     * @param quotationDefinitionDTO the entity to save
     * @return the persisted entity
     */
    public QuotationDefinitionDTO save(QuotationDefinitionDTO quotationDefinitionDTO) {
        log.debug("Request to save QuotationDefinition : {}", quotationDefinitionDTO);
        QuotationDefinition quotationDefinition = quotationDefinitionMapper.toEntity(quotationDefinitionDTO);
        quotationDefinition = quotationDefinitionRepository.save(quotationDefinition);
        return quotationDefinitionMapper.toDto(quotationDefinition);
    }

    /**
     * Get all the quotationDefinitions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuotationDefinitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuotationDefinitions");
        return quotationDefinitionRepository.findAll(pageable)
            .map(quotationDefinitionMapper::toDto);
    }

    /**
     * Get one quotationDefinition by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public QuotationDefinitionDTO findOne(Long id) {
        log.debug("Request to get QuotationDefinition : {}", id);
        QuotationDefinition quotationDefinition = quotationDefinitionRepository.findOne(id);
        return quotationDefinitionMapper.toDto(quotationDefinition);
    }

    /**
     * Delete the quotationDefinition by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QuotationDefinition : {}", id);
        quotationDefinitionRepository.delete(id);
    }
}
