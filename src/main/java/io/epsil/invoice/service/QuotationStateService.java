package io.epsil.invoice.service;

import io.epsil.invoice.domain.QuotationState;
import io.epsil.invoice.repository.QuotationStateRepository;
import io.epsil.invoice.service.dto.QuotationStateDTO;
import io.epsil.invoice.service.mapper.QuotationStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuotationState.
 */
@Service
@Transactional
public class QuotationStateService {

    private final Logger log = LoggerFactory.getLogger(QuotationStateService.class);

    private final QuotationStateRepository quotationStateRepository;

    private final QuotationStateMapper quotationStateMapper;

    public QuotationStateService(QuotationStateRepository quotationStateRepository, QuotationStateMapper quotationStateMapper) {
        this.quotationStateRepository = quotationStateRepository;
        this.quotationStateMapper = quotationStateMapper;
    }

    /**
     * Save a quotationState.
     *
     * @param quotationStateDTO the entity to save
     * @return the persisted entity
     */
    public QuotationStateDTO save(QuotationStateDTO quotationStateDTO) {
        log.debug("Request to save QuotationState : {}", quotationStateDTO);
        QuotationState quotationState = quotationStateMapper.toEntity(quotationStateDTO);
        quotationState = quotationStateRepository.save(quotationState);
        return quotationStateMapper.toDto(quotationState);
    }

    /**
     * Get all the quotationStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuotationStateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuotationStates");
        return quotationStateRepository.findAll(pageable)
            .map(quotationStateMapper::toDto);
    }

    /**
     * Get one quotationState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public QuotationStateDTO findOne(Long id) {
        log.debug("Request to get QuotationState : {}", id);
        QuotationState quotationState = quotationStateRepository.findOne(id);
        return quotationStateMapper.toDto(quotationState);
    }

    /**
     * Delete the quotationState by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QuotationState : {}", id);
        quotationStateRepository.delete(id);
    }
}
