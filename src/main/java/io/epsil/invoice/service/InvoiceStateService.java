package io.epsil.invoice.service;

import io.epsil.invoice.domain.InvoiceState;
import io.epsil.invoice.repository.InvoiceStateRepository;
import io.epsil.invoice.service.dto.InvoiceStateDTO;
import io.epsil.invoice.service.mapper.InvoiceStateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing InvoiceState.
 */
@Service
@Transactional
public class InvoiceStateService {

    private final Logger log = LoggerFactory.getLogger(InvoiceStateService.class);

    private final InvoiceStateRepository invoiceStateRepository;

    private final InvoiceStateMapper invoiceStateMapper;

    public InvoiceStateService(InvoiceStateRepository invoiceStateRepository, InvoiceStateMapper invoiceStateMapper) {
        this.invoiceStateRepository = invoiceStateRepository;
        this.invoiceStateMapper = invoiceStateMapper;
    }

    /**
     * Save a invoiceState.
     *
     * @param invoiceStateDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceStateDTO save(InvoiceStateDTO invoiceStateDTO) {
        log.debug("Request to save InvoiceState : {}", invoiceStateDTO);
        InvoiceState invoiceState = invoiceStateMapper.toEntity(invoiceStateDTO);
        invoiceState = invoiceStateRepository.save(invoiceState);
        return invoiceStateMapper.toDto(invoiceState);
    }

    /**
     * Get all the invoiceStates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceStateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceStates");
        return invoiceStateRepository.findAll(pageable)
            .map(invoiceStateMapper::toDto);
    }

    /**
     * Get one invoiceState by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public InvoiceStateDTO findOne(Long id) {
        log.debug("Request to get InvoiceState : {}", id);
        InvoiceState invoiceState = invoiceStateRepository.findOne(id);
        return invoiceStateMapper.toDto(invoiceState);
    }

    /**
     * Delete the invoiceState by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceState : {}", id);
        invoiceStateRepository.delete(id);
    }
}
