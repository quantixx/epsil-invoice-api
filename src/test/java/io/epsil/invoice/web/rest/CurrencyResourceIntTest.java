package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Currency;
import io.epsil.invoice.repository.CurrencyRepository;
import io.epsil.invoice.service.CurrencyService;
import io.epsil.invoice.service.dto.CurrencyDTO;
import io.epsil.invoice.service.mapper.CurrencyMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.CurrencyCriteria;
import io.epsil.invoice.service.CurrencyQueryService;

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
 * Test class for the CurrencyResource REST controller.
 *
 * @see CurrencyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class CurrencyResourceIntTest {

    private static final String DEFAULT_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_ALPHABETIC_CODE = "AAA";
    private static final String UPDATED_ALPHABETIC_CODE = "BBB";

    private static final Integer DEFAULT_NUMERIC_CODE = 1;
    private static final Integer UPDATED_NUMERIC_CODE = 2;

    private static final Integer DEFAULT_MINOR_UNIT = 1;
    private static final Integer UPDATED_MINOR_UNIT = 2;

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyQueryService currencyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCurrencyMockMvc;

    private Currency currency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrencyResource currencyResource = new CurrencyResource(currencyService, currencyQueryService);
        this.restCurrencyMockMvc = MockMvcBuilders.standaloneSetup(currencyResource)
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
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency()
            .entity(DEFAULT_ENTITY)
            .currency(DEFAULT_CURRENCY)
            .alphabeticCode(DEFAULT_ALPHABETIC_CODE)
            .numericCode(DEFAULT_NUMERIC_CODE)
            .minorUnit(DEFAULT_MINOR_UNIT)
            .symbol(DEFAULT_SYMBOL)
            .activated(DEFAULT_ACTIVATED);
        return currency;
    }

    @Before
    public void initTest() {
        currency = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);
        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getEntity()).isEqualTo(DEFAULT_ENTITY);
        assertThat(testCurrency.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testCurrency.getAlphabeticCode()).isEqualTo(DEFAULT_ALPHABETIC_CODE);
        assertThat(testCurrency.getNumericCode()).isEqualTo(DEFAULT_NUMERIC_CODE);
        assertThat(testCurrency.getMinorUnit()).isEqualTo(DEFAULT_MINOR_UNIT);
        assertThat(testCurrency.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testCurrency.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
    }

    @Test
    @Transactional
    public void createCurrencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().size();

        // Create the Currency with an existing ID
        currency.setId(1L);
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setEntity(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setCurrency(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAlphabeticCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setAlphabeticCode(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumericCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setNumericCode(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMinorUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().size();
        // set the field null
        currency.setMinorUnit(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        restCurrencyMockMvc.perform(post("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isBadRequest());

        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCurrencies() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList
        restCurrencyMockMvc.perform(get("/api/currencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].alphabeticCode").value(hasItem(DEFAULT_ALPHABETIC_CODE.toString())))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    public void getCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencies/{id}", currency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currency.getId().intValue()))
            .andExpect(jsonPath("$.entity").value(DEFAULT_ENTITY.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.alphabeticCode").value(DEFAULT_ALPHABETIC_CODE.toString()))
            .andExpect(jsonPath("$.numericCode").value(DEFAULT_NUMERIC_CODE))
            .andExpect(jsonPath("$.minorUnit").value(DEFAULT_MINOR_UNIT))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllCurrenciesByEntityIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where entity equals to DEFAULT_ENTITY
        defaultCurrencyShouldBeFound("entity.equals=" + DEFAULT_ENTITY);

        // Get all the currencyList where entity equals to UPDATED_ENTITY
        defaultCurrencyShouldNotBeFound("entity.equals=" + UPDATED_ENTITY);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByEntityIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where entity in DEFAULT_ENTITY or UPDATED_ENTITY
        defaultCurrencyShouldBeFound("entity.in=" + DEFAULT_ENTITY + "," + UPDATED_ENTITY);

        // Get all the currencyList where entity equals to UPDATED_ENTITY
        defaultCurrencyShouldNotBeFound("entity.in=" + UPDATED_ENTITY);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByEntityIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where entity is not null
        defaultCurrencyShouldBeFound("entity.specified=true");

        // Get all the currencyList where entity is null
        defaultCurrencyShouldNotBeFound("entity.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrenciesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currency equals to DEFAULT_CURRENCY
        defaultCurrencyShouldBeFound("currency.equals=" + DEFAULT_CURRENCY);

        // Get all the currencyList where currency equals to UPDATED_CURRENCY
        defaultCurrencyShouldNotBeFound("currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currency in DEFAULT_CURRENCY or UPDATED_CURRENCY
        defaultCurrencyShouldBeFound("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY);

        // Get all the currencyList where currency equals to UPDATED_CURRENCY
        defaultCurrencyShouldNotBeFound("currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where currency is not null
        defaultCurrencyShouldBeFound("currency.specified=true");

        // Get all the currencyList where currency is null
        defaultCurrencyShouldNotBeFound("currency.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrenciesByAlphabeticCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where alphabeticCode equals to DEFAULT_ALPHABETIC_CODE
        defaultCurrencyShouldBeFound("alphabeticCode.equals=" + DEFAULT_ALPHABETIC_CODE);

        // Get all the currencyList where alphabeticCode equals to UPDATED_ALPHABETIC_CODE
        defaultCurrencyShouldNotBeFound("alphabeticCode.equals=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByAlphabeticCodeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where alphabeticCode in DEFAULT_ALPHABETIC_CODE or UPDATED_ALPHABETIC_CODE
        defaultCurrencyShouldBeFound("alphabeticCode.in=" + DEFAULT_ALPHABETIC_CODE + "," + UPDATED_ALPHABETIC_CODE);

        // Get all the currencyList where alphabeticCode equals to UPDATED_ALPHABETIC_CODE
        defaultCurrencyShouldNotBeFound("alphabeticCode.in=" + UPDATED_ALPHABETIC_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByAlphabeticCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where alphabeticCode is not null
        defaultCurrencyShouldBeFound("alphabeticCode.specified=true");

        // Get all the currencyList where alphabeticCode is null
        defaultCurrencyShouldNotBeFound("alphabeticCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrenciesByNumericCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where numericCode equals to DEFAULT_NUMERIC_CODE
        defaultCurrencyShouldBeFound("numericCode.equals=" + DEFAULT_NUMERIC_CODE);

        // Get all the currencyList where numericCode equals to UPDATED_NUMERIC_CODE
        defaultCurrencyShouldNotBeFound("numericCode.equals=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByNumericCodeIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where numericCode in DEFAULT_NUMERIC_CODE or UPDATED_NUMERIC_CODE
        defaultCurrencyShouldBeFound("numericCode.in=" + DEFAULT_NUMERIC_CODE + "," + UPDATED_NUMERIC_CODE);

        // Get all the currencyList where numericCode equals to UPDATED_NUMERIC_CODE
        defaultCurrencyShouldNotBeFound("numericCode.in=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByNumericCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where numericCode is not null
        defaultCurrencyShouldBeFound("numericCode.specified=true");

        // Get all the currencyList where numericCode is null
        defaultCurrencyShouldNotBeFound("numericCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrenciesByNumericCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where numericCode greater than or equals to DEFAULT_NUMERIC_CODE
        defaultCurrencyShouldBeFound("numericCode.greaterOrEqualThan=" + DEFAULT_NUMERIC_CODE);

        // Get all the currencyList where numericCode greater than or equals to UPDATED_NUMERIC_CODE
        defaultCurrencyShouldNotBeFound("numericCode.greaterOrEqualThan=" + UPDATED_NUMERIC_CODE);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByNumericCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where numericCode less than or equals to DEFAULT_NUMERIC_CODE
        defaultCurrencyShouldNotBeFound("numericCode.lessThan=" + DEFAULT_NUMERIC_CODE);

        // Get all the currencyList where numericCode less than or equals to UPDATED_NUMERIC_CODE
        defaultCurrencyShouldBeFound("numericCode.lessThan=" + UPDATED_NUMERIC_CODE);
    }


    @Test
    @Transactional
    public void getAllCurrenciesByMinorUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where minorUnit equals to DEFAULT_MINOR_UNIT
        defaultCurrencyShouldBeFound("minorUnit.equals=" + DEFAULT_MINOR_UNIT);

        // Get all the currencyList where minorUnit equals to UPDATED_MINOR_UNIT
        defaultCurrencyShouldNotBeFound("minorUnit.equals=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByMinorUnitIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where minorUnit in DEFAULT_MINOR_UNIT or UPDATED_MINOR_UNIT
        defaultCurrencyShouldBeFound("minorUnit.in=" + DEFAULT_MINOR_UNIT + "," + UPDATED_MINOR_UNIT);

        // Get all the currencyList where minorUnit equals to UPDATED_MINOR_UNIT
        defaultCurrencyShouldNotBeFound("minorUnit.in=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByMinorUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where minorUnit is not null
        defaultCurrencyShouldBeFound("minorUnit.specified=true");

        // Get all the currencyList where minorUnit is null
        defaultCurrencyShouldNotBeFound("minorUnit.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrenciesByMinorUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where minorUnit greater than or equals to DEFAULT_MINOR_UNIT
        defaultCurrencyShouldBeFound("minorUnit.greaterOrEqualThan=" + DEFAULT_MINOR_UNIT);

        // Get all the currencyList where minorUnit greater than or equals to UPDATED_MINOR_UNIT
        defaultCurrencyShouldNotBeFound("minorUnit.greaterOrEqualThan=" + UPDATED_MINOR_UNIT);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByMinorUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where minorUnit less than or equals to DEFAULT_MINOR_UNIT
        defaultCurrencyShouldNotBeFound("minorUnit.lessThan=" + DEFAULT_MINOR_UNIT);

        // Get all the currencyList where minorUnit less than or equals to UPDATED_MINOR_UNIT
        defaultCurrencyShouldBeFound("minorUnit.lessThan=" + UPDATED_MINOR_UNIT);
    }


    @Test
    @Transactional
    public void getAllCurrenciesBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol equals to DEFAULT_SYMBOL
        defaultCurrencyShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the currencyList where symbol equals to UPDATED_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCurrenciesBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultCurrencyShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the currencyList where symbol equals to UPDATED_SYMBOL
        defaultCurrencyShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllCurrenciesBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where symbol is not null
        defaultCurrencyShouldBeFound("symbol.specified=true");

        // Get all the currencyList where symbol is null
        defaultCurrencyShouldNotBeFound("symbol.specified=false");
    }

    @Test
    @Transactional
    public void getAllCurrenciesByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where activated equals to DEFAULT_ACTIVATED
        defaultCurrencyShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the currencyList where activated equals to UPDATED_ACTIVATED
        defaultCurrencyShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultCurrencyShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the currencyList where activated equals to UPDATED_ACTIVATED
        defaultCurrencyShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllCurrenciesByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);

        // Get all the currencyList where activated is not null
        defaultCurrencyShouldBeFound("activated.specified=true");

        // Get all the currencyList where activated is null
        defaultCurrencyShouldNotBeFound("activated.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCurrencyShouldBeFound(String filter) throws Exception {
        restCurrencyMockMvc.perform(get("/api/currencies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currency.getId().intValue())))
            .andExpect(jsonPath("$.[*].entity").value(hasItem(DEFAULT_ENTITY.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].alphabeticCode").value(hasItem(DEFAULT_ALPHABETIC_CODE.toString())))
            .andExpect(jsonPath("$.[*].numericCode").value(hasItem(DEFAULT_NUMERIC_CODE)))
            .andExpect(jsonPath("$.[*].minorUnit").value(hasItem(DEFAULT_MINOR_UNIT)))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCurrencyShouldNotBeFound(String filter) throws Exception {
        restCurrencyMockMvc.perform(get("/api/currencies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCurrency() throws Exception {
        // Get the currency
        restCurrencyMockMvc.perform(get("/api/currencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Update the currency
        Currency updatedCurrency = currencyRepository.findOne(currency.getId());
        // Disconnect from session so that the updates on updatedCurrency are not directly saved in db
        em.detach(updatedCurrency);
        updatedCurrency
            .entity(UPDATED_ENTITY)
            .currency(UPDATED_CURRENCY)
            .alphabeticCode(UPDATED_ALPHABETIC_CODE)
            .numericCode(UPDATED_NUMERIC_CODE)
            .minorUnit(UPDATED_MINOR_UNIT)
            .symbol(UPDATED_SYMBOL)
            .activated(UPDATED_ACTIVATED);
        CurrencyDTO currencyDTO = currencyMapper.toDto(updatedCurrency);

        restCurrencyMockMvc.perform(put("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isOk());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getEntity()).isEqualTo(UPDATED_ENTITY);
        assertThat(testCurrency.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testCurrency.getAlphabeticCode()).isEqualTo(UPDATED_ALPHABETIC_CODE);
        assertThat(testCurrency.getNumericCode()).isEqualTo(UPDATED_NUMERIC_CODE);
        assertThat(testCurrency.getMinorUnit()).isEqualTo(UPDATED_MINOR_UNIT);
        assertThat(testCurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCurrency.isActivated()).isEqualTo(UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().size();

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCurrencyMockMvc.perform(put("/api/currencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currencyDTO)))
            .andExpect(status().isCreated());

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCurrency() throws Exception {
        // Initialize the database
        currencyRepository.saveAndFlush(currency);
        int databaseSizeBeforeDelete = currencyRepository.findAll().size();

        // Get the currency
        restCurrencyMockMvc.perform(delete("/api/currencies/{id}", currency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Currency> currencyList = currencyRepository.findAll();
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Currency.class);
        Currency currency1 = new Currency();
        currency1.setId(1L);
        Currency currency2 = new Currency();
        currency2.setId(currency1.getId());
        assertThat(currency1).isEqualTo(currency2);
        currency2.setId(2L);
        assertThat(currency1).isNotEqualTo(currency2);
        currency1.setId(null);
        assertThat(currency1).isNotEqualTo(currency2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyDTO.class);
        CurrencyDTO currencyDTO1 = new CurrencyDTO();
        currencyDTO1.setId(1L);
        CurrencyDTO currencyDTO2 = new CurrencyDTO();
        assertThat(currencyDTO1).isNotEqualTo(currencyDTO2);
        currencyDTO2.setId(currencyDTO1.getId());
        assertThat(currencyDTO1).isEqualTo(currencyDTO2);
        currencyDTO2.setId(2L);
        assertThat(currencyDTO1).isNotEqualTo(currencyDTO2);
        currencyDTO1.setId(null);
        assertThat(currencyDTO1).isNotEqualTo(currencyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(currencyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(currencyMapper.fromId(null)).isNull();
    }
}
