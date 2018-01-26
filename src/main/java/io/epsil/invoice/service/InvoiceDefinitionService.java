package io.epsil.invoice.service;

import io.epsil.invoice.domain.InvoiceDefinition;
import io.epsil.invoice.repository.InvoiceDefinitionRepository;
import io.epsil.invoice.service.dto.InvoiceDefinitionDTO;
import io.epsil.invoice.service.mapper.InvoiceDefinitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing InvoiceDefinition.
 */
@Service
@Transactional
public class InvoiceDefinitionService {

    private final Logger log = LoggerFactory.getLogger(InvoiceDefinitionService.class);

    private final InvoiceDefinitionRepository invoiceDefinitionRepository;

    private final InvoiceDefinitionMapper invoiceDefinitionMapper;

    public InvoiceDefinitionService(InvoiceDefinitionRepository invoiceDefinitionRepository, InvoiceDefinitionMapper invoiceDefinitionMapper) {
        this.invoiceDefinitionRepository = invoiceDefinitionRepository;
        this.invoiceDefinitionMapper = invoiceDefinitionMapper;
    }

    /**
     * Save a invoiceDefinition.
     *
     * @param invoiceDefinitionDTO the entity to save
     * @return the persisted entity
     */
    public InvoiceDefinitionDTO save(InvoiceDefinitionDTO invoiceDefinitionDTO) {
        log.debug("Request to save InvoiceDefinition : {}", invoiceDefinitionDTO);
        InvoiceDefinition invoiceDefinition = invoiceDefinitionMapper.toEntity(invoiceDefinitionDTO);
        invoiceDefinition = invoiceDefinitionRepository.save(invoiceDefinition);
        return invoiceDefinitionMapper.toDto(invoiceDefinition);
    }

    /**
     * Get all the invoiceDefinitions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InvoiceDefinitionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceDefinitions");
        return invoiceDefinitionRepository.findAll(pageable)
            .map(invoiceDefinitionMapper::toDto);
    }

    /**
     * Get one invoiceDefinition by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public InvoiceDefinitionDTO findOne(Long id) {
        log.debug("Request to get InvoiceDefinition : {}", id);
        InvoiceDefinition invoiceDefinition = invoiceDefinitionRepository.findOne(id);
        return invoiceDefinitionMapper.toDto(invoiceDefinition);
    }

    /**
     * Delete the invoiceDefinition by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceDefinition : {}", id);
        invoiceDefinitionRepository.delete(id);
    }
}
