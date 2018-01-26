package io.epsil.invoice.service;

import io.epsil.invoice.domain.InvoiceLine;
import io.epsil.invoice.repository.InvoiceLineRepository;
import io.epsil.invoice.service.dto.InvoiceLineDTO;
import io.epsil.invoice.service.mapper.InvoiceLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing InvoiceLine.
 */
@Service
@Transactional
public class InvoiceLineService {

    private final Logger log = LoggerFactory.getLogger(InvoiceLineService.class);

    private final InvoiceLineRepository invoiceLineRepository;

    private final InvoiceLineMapper invoiceLineMapper;

    public InvoiceLineService(InvoiceLineRepository invoiceLineRepository, InvoiceLineMapper invoiceLineMapper) {
        this.invoiceLineRepository = invoiceLineRepository;
        this.invoiceLineMapper = invoiceLineMapper;
    }

    /**
     * Save a invoiceLine.
     *
     * @param invoiceLineDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceLineDTO save(InvoiceLineDTO invoiceLineDTO) {
        log.debug("Request to save InvoiceLine : {}", invoiceLineDTO);
        InvoiceLine invoiceLine = invoiceLineMapper.toEntity(invoiceLineDTO);
        invoiceLine = invoiceLineRepository.save(invoiceLine);
        return invoiceLineMapper.toDto(invoiceLine);
    }

    /**
     * Get all the invoiceLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceLines");
        return invoiceLineRepository.findAll(pageable)
            .map(invoiceLineMapper::toDto);
    }

    /**
     * Get one invoiceLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public InvoiceLineDTO findOne(Long id) {
        log.debug("Request to get InvoiceLine : {}", id);
        InvoiceLine invoiceLine = invoiceLineRepository.findOne(id);
        return invoiceLineMapper.toDto(invoiceLine);
    }

    /**
     * Delete the invoiceLine by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceLine : {}", id);
        invoiceLineRepository.delete(id);
    }
}
