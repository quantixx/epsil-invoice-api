package io.epsil.invoice.service;

import io.epsil.invoice.domain.QuotationLine;
import io.epsil.invoice.repository.QuotationLineRepository;
import io.epsil.invoice.service.dto.QuotationLineDTO;
import io.epsil.invoice.service.mapper.QuotationLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing QuotationLine.
 */
@Service
@Transactional
public class QuotationLineService {

    private final Logger log = LoggerFactory.getLogger(QuotationLineService.class);

    private final QuotationLineRepository quotationLineRepository;

    private final QuotationLineMapper quotationLineMapper;

    public QuotationLineService(QuotationLineRepository quotationLineRepository, QuotationLineMapper quotationLineMapper) {
        this.quotationLineRepository = quotationLineRepository;
        this.quotationLineMapper = quotationLineMapper;
    }

    /**
     * Save a quotationLine.
     *
     * @param quotationLineDTO the entity to save
     * @return the persisted entity
     */
    public QuotationLineDTO save(QuotationLineDTO quotationLineDTO) {
        log.debug("Request to save QuotationLine : {}", quotationLineDTO);
        QuotationLine quotationLine = quotationLineMapper.toEntity(quotationLineDTO);
        quotationLine = quotationLineRepository.save(quotationLine);
        return quotationLineMapper.toDto(quotationLine);
    }

    /**
     * Get all the quotationLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QuotationLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QuotationLines");
        return quotationLineRepository.findAll(pageable)
            .map(quotationLineMapper::toDto);
    }

    /**
     * Get one quotationLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public QuotationLineDTO findOne(Long id) {
        log.debug("Request to get QuotationLine : {}", id);
        QuotationLine quotationLine = quotationLineRepository.findOne(id);
        return quotationLineMapper.toDto(quotationLine);
    }

    /**
     * Delete the quotationLine by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QuotationLine : {}", id);
        quotationLineRepository.delete(id);
    }
}
