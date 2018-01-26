package io.epsil.invoice.service;

import io.epsil.invoice.domain.Bank;
import io.epsil.invoice.repository.BankRepository;
import io.epsil.invoice.service.dto.BankDTO;
import io.epsil.invoice.service.mapper.BankMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Bank.
 */
@Service
@Transactional
public class BankService {

    private final Logger log = LoggerFactory.getLogger(BankService.class);

    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    public BankService(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    /**
     * Save a bank.
     *
     * @param bankDTO the entity to save
     * @return the persisted entity
     */
    public BankDTO save(BankDTO bankDTO) {
        log.debug("Request to save Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    /**
     * Get all the banks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BankDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banks");
        return bankRepository.findAll(pageable)
            .map(bankMapper::toDto);
    }

    /**
     * Get one bank by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public BankDTO findOne(Long id) {
        log.debug("Request to get Bank : {}", id);
        Bank bank = bankRepository.findOne(id);
        return bankMapper.toDto(bank);
    }

    /**
     * Delete the bank by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Bank : {}", id);
        bankRepository.delete(id);
    }
}
