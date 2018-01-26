package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Bank;
import io.epsil.invoice.domain.Address;
import io.epsil.invoice.repository.BankRepository;
import io.epsil.invoice.service.BankService;
import io.epsil.invoice.service.dto.BankDTO;
import io.epsil.invoice.service.mapper.BankMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.BankCriteria;
import io.epsil.invoice.service.BankQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static io.epsil.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BankResource REST controller.
 *
 * @see BankResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class BankResourceIntTest {

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AGENCY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_BIC = "AAAAAAAAAA";
    private static final String UPDATED_BIC = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_AREA = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankQueryService bankQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBankMockMvc;

    private Bank bank;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BankResource bankResource = new BankResource(bankService, bankQueryService);
        this.restBankMockMvc = MockMvcBuilders.standaloneSetup(bankResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createEntity(EntityManager em) {
        Bank bank = new Bank()
            .bankName(DEFAULT_BANK_NAME)
            .agencyName(DEFAULT_AGENCY_NAME)
            .bankAccount(DEFAULT_BANK_ACCOUNT)
            .iban(DEFAULT_IBAN)
            .bic(DEFAULT_BIC)
            .phoneArea(DEFAULT_PHONE_AREA)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return bank;
    }

    @Before
    public void initTest() {
        bank = createEntity(em);
    }

    @Test
    @Transactional
    public void createBank() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);
        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate + 1);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testBank.getAgencyName()).isEqualTo(DEFAULT_AGENCY_NAME);
        assertThat(testBank.getBankAccount()).isEqualTo(DEFAULT_BANK_ACCOUNT);
        assertThat(testBank.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testBank.getBic()).isEqualTo(DEFAULT_BIC);
        assertThat(testBank.getPhoneArea()).isEqualTo(DEFAULT_PHONE_AREA);
        assertThat(testBank.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createBankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank with an existing ID
        bank.setId(1L);
        BankDTO bankDTO = bankMapper.toDto(bank);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBanks() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList
        restBankMockMvc.perform(get("/api/banks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].agencyName").value(hasItem(DEFAULT_AGENCY_NAME.toString())))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN.toString())))
            .andExpect(jsonPath("$.[*].bic").value(hasItem(DEFAULT_BIC.toString())))
            .andExpect(jsonPath("$.[*].phoneArea").value(hasItem(DEFAULT_PHONE_AREA.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME.toString()))
            .andExpect(jsonPath("$.agencyName").value(DEFAULT_AGENCY_NAME.toString()))
            .andExpect(jsonPath("$.bankAccount").value(DEFAULT_BANK_ACCOUNT.toString()))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN.toString()))
            .andExpect(jsonPath("$.bic").value(DEFAULT_BIC.toString()))
            .andExpect(jsonPath("$.phoneArea").value(DEFAULT_PHONE_AREA.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName equals to DEFAULT_BANK_NAME
        defaultBankShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the bankList where bankName equals to UPDATED_BANK_NAME
        defaultBankShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultBankShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the bankList where bankName equals to UPDATED_BANK_NAME
        defaultBankShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankName is not null
        defaultBankShouldBeFound("bankName.specified=true");

        // Get all the bankList where bankName is null
        defaultBankShouldNotBeFound("bankName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByAgencyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where agencyName equals to DEFAULT_AGENCY_NAME
        defaultBankShouldBeFound("agencyName.equals=" + DEFAULT_AGENCY_NAME);

        // Get all the bankList where agencyName equals to UPDATED_AGENCY_NAME
        defaultBankShouldNotBeFound("agencyName.equals=" + UPDATED_AGENCY_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByAgencyNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where agencyName in DEFAULT_AGENCY_NAME or UPDATED_AGENCY_NAME
        defaultBankShouldBeFound("agencyName.in=" + DEFAULT_AGENCY_NAME + "," + UPDATED_AGENCY_NAME);

        // Get all the bankList where agencyName equals to UPDATED_AGENCY_NAME
        defaultBankShouldNotBeFound("agencyName.in=" + UPDATED_AGENCY_NAME);
    }

    @Test
    @Transactional
    public void getAllBanksByAgencyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where agencyName is not null
        defaultBankShouldBeFound("agencyName.specified=true");

        // Get all the bankList where agencyName is null
        defaultBankShouldNotBeFound("agencyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByBankAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankAccount equals to DEFAULT_BANK_ACCOUNT
        defaultBankShouldBeFound("bankAccount.equals=" + DEFAULT_BANK_ACCOUNT);

        // Get all the bankList where bankAccount equals to UPDATED_BANK_ACCOUNT
        defaultBankShouldNotBeFound("bankAccount.equals=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllBanksByBankAccountIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankAccount in DEFAULT_BANK_ACCOUNT or UPDATED_BANK_ACCOUNT
        defaultBankShouldBeFound("bankAccount.in=" + DEFAULT_BANK_ACCOUNT + "," + UPDATED_BANK_ACCOUNT);

        // Get all the bankList where bankAccount equals to UPDATED_BANK_ACCOUNT
        defaultBankShouldNotBeFound("bankAccount.in=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void getAllBanksByBankAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bankAccount is not null
        defaultBankShouldBeFound("bankAccount.specified=true");

        // Get all the bankList where bankAccount is null
        defaultBankShouldNotBeFound("bankAccount.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByIbanIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where iban equals to DEFAULT_IBAN
        defaultBankShouldBeFound("iban.equals=" + DEFAULT_IBAN);

        // Get all the bankList where iban equals to UPDATED_IBAN
        defaultBankShouldNotBeFound("iban.equals=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    public void getAllBanksByIbanIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where iban in DEFAULT_IBAN or UPDATED_IBAN
        defaultBankShouldBeFound("iban.in=" + DEFAULT_IBAN + "," + UPDATED_IBAN);

        // Get all the bankList where iban equals to UPDATED_IBAN
        defaultBankShouldNotBeFound("iban.in=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    public void getAllBanksByIbanIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where iban is not null
        defaultBankShouldBeFound("iban.specified=true");

        // Get all the bankList where iban is null
        defaultBankShouldNotBeFound("iban.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByBicIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bic equals to DEFAULT_BIC
        defaultBankShouldBeFound("bic.equals=" + DEFAULT_BIC);

        // Get all the bankList where bic equals to UPDATED_BIC
        defaultBankShouldNotBeFound("bic.equals=" + UPDATED_BIC);
    }

    @Test
    @Transactional
    public void getAllBanksByBicIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bic in DEFAULT_BIC or UPDATED_BIC
        defaultBankShouldBeFound("bic.in=" + DEFAULT_BIC + "," + UPDATED_BIC);

        // Get all the bankList where bic equals to UPDATED_BIC
        defaultBankShouldNotBeFound("bic.in=" + UPDATED_BIC);
    }

    @Test
    @Transactional
    public void getAllBanksByBicIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where bic is not null
        defaultBankShouldBeFound("bic.specified=true");

        // Get all the bankList where bic is null
        defaultBankShouldNotBeFound("bic.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByPhoneAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where phoneArea equals to DEFAULT_PHONE_AREA
        defaultBankShouldBeFound("phoneArea.equals=" + DEFAULT_PHONE_AREA);

        // Get all the bankList where phoneArea equals to UPDATED_PHONE_AREA
        defaultBankShouldNotBeFound("phoneArea.equals=" + UPDATED_PHONE_AREA);
    }

    @Test
    @Transactional
    public void getAllBanksByPhoneAreaIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where phoneArea in DEFAULT_PHONE_AREA or UPDATED_PHONE_AREA
        defaultBankShouldBeFound("phoneArea.in=" + DEFAULT_PHONE_AREA + "," + UPDATED_PHONE_AREA);

        // Get all the bankList where phoneArea equals to UPDATED_PHONE_AREA
        defaultBankShouldNotBeFound("phoneArea.in=" + UPDATED_PHONE_AREA);
    }

    @Test
    @Transactional
    public void getAllBanksByPhoneAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where phoneArea is not null
        defaultBankShouldBeFound("phoneArea.specified=true");

        // Get all the bankList where phoneArea is null
        defaultBankShouldNotBeFound("phoneArea.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultBankShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the bankList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultBankShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBanksByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultBankShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the bankList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultBankShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBanksByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList where phoneNumber is not null
        defaultBankShouldBeFound("phoneNumber.specified=true");

        // Get all the bankList where phoneNumber is null
        defaultBankShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllBanksByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        Address address = AddressResourceIntTest.createEntity(em);
        em.persist(address);
        em.flush();
        bank.setAddress(address);
        bankRepository.saveAndFlush(bank);
        Long addressId = address.getId();

        // Get all the bankList where address equals to addressId
        defaultBankShouldBeFound("addressId.equals=" + addressId);

        // Get all the bankList where address equals to addressId + 1
        defaultBankShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBankShouldBeFound(String filter) throws Exception {
        restBankMockMvc.perform(get("/api/banks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].agencyName").value(hasItem(DEFAULT_AGENCY_NAME.toString())))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN.toString())))
            .andExpect(jsonPath("$.[*].bic").value(hasItem(DEFAULT_BIC.toString())))
            .andExpect(jsonPath("$.[*].phoneArea").value(hasItem(DEFAULT_PHONE_AREA.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBankShouldNotBeFound(String filter) throws Exception {
        restBankMockMvc.perform(get("/api/banks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Update the bank
        Bank updatedBank = bankRepository.findOne(bank.getId());
        // Disconnect from session so that the updates on updatedBank are not directly saved in db
        em.detach(updatedBank);
        updatedBank
            .bankName(UPDATED_BANK_NAME)
            .agencyName(UPDATED_AGENCY_NAME)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .iban(UPDATED_IBAN)
            .bic(UPDATED_BIC)
            .phoneArea(UPDATED_PHONE_AREA)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        BankDTO bankDTO = bankMapper.toDto(updatedBank);

        restBankMockMvc.perform(put("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isOk());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBank.getAgencyName()).isEqualTo(UPDATED_AGENCY_NAME);
        assertThat(testBank.getBankAccount()).isEqualTo(UPDATED_BANK_ACCOUNT);
        assertThat(testBank.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testBank.getBic()).isEqualTo(UPDATED_BIC);
        assertThat(testBank.getPhoneArea()).isEqualTo(UPDATED_PHONE_AREA);
        assertThat(testBank.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingBank() throws Exception {
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBankMockMvc.perform(put("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);
        int databaseSizeBeforeDelete = bankRepository.findAll().size();

        // Get the bank
        restBankMockMvc.perform(delete("/api/banks/{id}", bank.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
        Bank bank1 = new Bank();
        bank1.setId(1L);
        Bank bank2 = new Bank();
        bank2.setId(bank1.getId());
        assertThat(bank1).isEqualTo(bank2);
        bank2.setId(2L);
        assertThat(bank1).isNotEqualTo(bank2);
        bank1.setId(null);
        assertThat(bank1).isNotEqualTo(bank2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankDTO.class);
        BankDTO bankDTO1 = new BankDTO();
        bankDTO1.setId(1L);
        BankDTO bankDTO2 = new BankDTO();
        assertThat(bankDTO1).isNotEqualTo(bankDTO2);
        bankDTO2.setId(bankDTO1.getId());
        assertThat(bankDTO1).isEqualTo(bankDTO2);
        bankDTO2.setId(2L);
        assertThat(bankDTO1).isNotEqualTo(bankDTO2);
        bankDTO1.setId(null);
        assertThat(bankDTO1).isNotEqualTo(bankDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bankMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bankMapper.fromId(null)).isNull();
    }
}
