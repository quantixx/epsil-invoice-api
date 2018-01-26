package io.epsil.invoice.service;

import io.epsil.invoice.domain.Quotation;
import io.epsil.invoice.repository.QuotationRepository;
import io.epsil.invoice.service.dto.QuotationDTO;
import io.epsil.invoice.service.mapper.QuotationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Quotation.
 */
@Service
@Transactional
public class QuotationService {

    private final Logger log = LoggerFactory.getLogger(QuotationService.class);

    private final QuotationRepository quotationRepository;

    private final QuotationMapper quotationMapper;

    public QuotationService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    /**
     * Save a quotation.
     *
     * @param quotationDTO the entity to save
     * @return the persisted entity
     */
    public QuotationDTO save(QuotationDTO quotationDTO) {
        log.debug("Request to save Quotation : {}", quotationDTO);
        Quotation quotation = quotationMapper.toEntity(quotationDTO);
        quotation = quotationRepository.save(quotation);
        return quotationMapper.toDto(quotation);
    }

    /**
     * Get all the quotations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuotationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Quotations");
        return quotationRepository.findAll(pageable)
            .map(quotationMapper::toDto);
    }


    /**
     *  get all the quotations where Invoice is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<QuotationDTO> findAllWhereInvoiceIsNull() {
        log.debug("Request to get all quotations where Invoice is null");
        return StreamSupport
            .stream(quotationRepository.findAll().spliterator(), false)
            .filter(quotation -> quotation.getInvoice() == null)
            .map(quotationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one quotation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public QuotationDTO findOne(Long id) {
        log.debug("Request to get Quotation : {}", id);
        Quotation quotation = quotationRepository.findOne(id);
        return quotationMapper.toDto(quotation);
    }

    /**
     * Delete the quotation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Quotation : {}", id);
        quotationRepository.delete(id);
    }
}
