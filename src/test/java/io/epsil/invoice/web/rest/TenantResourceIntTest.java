package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.domain.Address;
import io.epsil.invoice.domain.Bank;
import io.epsil.invoice.domain.Document;
import io.epsil.invoice.domain.Contact;
import io.epsil.invoice.domain.Event;
import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.domain.InvoiceDefinition;
import io.epsil.invoice.domain.Quotation;
import io.epsil.invoice.domain.QuotationDefinition;
import io.epsil.invoice.domain.Family;
import io.epsil.invoice.repository.TenantRepository;
import io.epsil.invoice.service.TenantService;
import io.epsil.invoice.service.dto.TenantDTO;
import io.epsil.invoice.service.mapper.TenantMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.TenantCriteria;
import io.epsil.invoice.service.TenantQueryService;

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
 * Test class for the TenantResource REST controller.
 *
 * @see TenantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class TenantResourceIntTest {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VAT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_VAT_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_REGISTRY = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_REGISTRY = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_YEAR_FROM = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_YEAR_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_YEAR_TO = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_YEAR_TO = "BBBBBBBBBB";

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantMapper tenantMapper;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantQueryService tenantQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTenantMockMvc;

    private Tenant tenant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TenantResource tenantResource = new TenantResource(tenantService, tenantQueryService);
        this.restTenantMockMvc = MockMvcBuilders.standaloneSetup(tenantResource)
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
    public static Tenant createEntity(EntityManager em) {
        Tenant tenant = new Tenant()
            .slug(DEFAULT_SLUG)
            .name(DEFAULT_NAME)
            .companyName(DEFAULT_COMPANY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .vatIdentification(DEFAULT_VAT_IDENTIFICATION)
            .businessIdentification(DEFAULT_BUSINESS_IDENTIFICATION)
            .businessRegistry(DEFAULT_BUSINESS_REGISTRY)
            .businessCode(DEFAULT_BUSINESS_CODE)
            .financialYearFrom(DEFAULT_FINANCIAL_YEAR_FROM)
            .financialYearTo(DEFAULT_FINANCIAL_YEAR_TO);
        // Add required entity
        Address invoiceAddress = AddressResourceIntTest.createEntity(em);
        em.persist(invoiceAddress);
        em.flush();
        tenant.setInvoiceAddress(invoiceAddress);
        // Add required entity
        Bank bank = BankResourceIntTest.createEntity(em);
        em.persist(bank);
        em.flush();
        tenant.setBank(bank);
        return tenant;
    }

    @Before
    public void initTest() {
        tenant = createEntity(em);
    }

    @Test
    @Transactional
    public void createTenant() throws Exception {
        int databaseSizeBeforeCreate = tenantRepository.findAll().size();

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);
        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isCreated());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeCreate + 1);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testTenant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTenant.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testTenant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTenant.getVatIdentification()).isEqualTo(DEFAULT_VAT_IDENTIFICATION);
        assertThat(testTenant.getBusinessIdentification()).isEqualTo(DEFAULT_BUSINESS_IDENTIFICATION);
        assertThat(testTenant.getBusinessRegistry()).isEqualTo(DEFAULT_BUSINESS_REGISTRY);
        assertThat(testTenant.getBusinessCode()).isEqualTo(DEFAULT_BUSINESS_CODE);
        assertThat(testTenant.getFinancialYearFrom()).isEqualTo(DEFAULT_FINANCIAL_YEAR_FROM);
        assertThat(testTenant.getFinancialYearTo()).isEqualTo(DEFAULT_FINANCIAL_YEAR_TO);
    }

    @Test
    @Transactional
    public void createTenantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tenantRepository.findAll().size();

        // Create the Tenant with an existing ID
        tenant.setId(1L);
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setSlug(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantRepository.findAll().size();
        // set the field null
        tenant.setName(null);

        // Create the Tenant, which fails.
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        restTenantMockMvc.perform(post("/api/tenants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isBadRequest());

        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTenants() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].vatIdentification").value(hasItem(DEFAULT_VAT_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].businessIdentification").value(hasItem(DEFAULT_BUSINESS_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].businessRegistry").value(hasItem(DEFAULT_BUSINESS_REGISTRY.toString())))
            .andExpect(jsonPath("$.[*].businessCode").value(hasItem(DEFAULT_BUSINESS_CODE.toString())))
            .andExpect(jsonPath("$.[*].financialYearFrom").value(hasItem(DEFAULT_FINANCIAL_YEAR_FROM.toString())))
            .andExpect(jsonPath("$.[*].financialYearTo").value(hasItem(DEFAULT_FINANCIAL_YEAR_TO.toString())));
    }

    @Test
    @Transactional
    public void getTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get the tenant
        restTenantMockMvc.perform(get("/api/tenants/{id}", tenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tenant.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.vatIdentification").value(DEFAULT_VAT_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.businessIdentification").value(DEFAULT_BUSINESS_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.businessRegistry").value(DEFAULT_BUSINESS_REGISTRY.toString()))
            .andExpect(jsonPath("$.businessCode").value(DEFAULT_BUSINESS_CODE.toString()))
            .andExpect(jsonPath("$.financialYearFrom").value(DEFAULT_FINANCIAL_YEAR_FROM.toString()))
            .andExpect(jsonPath("$.financialYearTo").value(DEFAULT_FINANCIAL_YEAR_TO.toString()));
    }

    @Test
    @Transactional
    public void getAllTenantsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where slug equals to DEFAULT_SLUG
        defaultTenantShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the tenantList where slug equals to UPDATED_SLUG
        defaultTenantShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllTenantsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultTenantShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the tenantList where slug equals to UPDATED_SLUG
        defaultTenantShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllTenantsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where slug is not null
        defaultTenantShouldBeFound("slug.specified=true");

        // Get all the tenantList where slug is null
        defaultTenantShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name equals to DEFAULT_NAME
        defaultTenantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the tenantList where name equals to UPDATED_NAME
        defaultTenantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTenantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the tenantList where name equals to UPDATED_NAME
        defaultTenantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where name is not null
        defaultTenantShouldBeFound("name.specified=true");

        // Get all the tenantList where name is null
        defaultTenantShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName equals to DEFAULT_COMPANY_NAME
        defaultTenantShouldBeFound("companyName.equals=" + DEFAULT_COMPANY_NAME);

        // Get all the tenantList where companyName equals to UPDATED_COMPANY_NAME
        defaultTenantShouldNotBeFound("companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName in DEFAULT_COMPANY_NAME or UPDATED_COMPANY_NAME
        defaultTenantShouldBeFound("companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME);

        // Get all the tenantList where companyName equals to UPDATED_COMPANY_NAME
        defaultTenantShouldNotBeFound("companyName.in=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    public void getAllTenantsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where companyName is not null
        defaultTenantShouldBeFound("companyName.specified=true");

        // Get all the tenantList where companyName is null
        defaultTenantShouldNotBeFound("companyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where description equals to DEFAULT_DESCRIPTION
        defaultTenantShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the tenantList where description equals to UPDATED_DESCRIPTION
        defaultTenantShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTenantsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTenantShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the tenantList where description equals to UPDATED_DESCRIPTION
        defaultTenantShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTenantsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where description is not null
        defaultTenantShouldBeFound("description.specified=true");

        // Get all the tenantList where description is null
        defaultTenantShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByVatIdentificationIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where vatIdentification equals to DEFAULT_VAT_IDENTIFICATION
        defaultTenantShouldBeFound("vatIdentification.equals=" + DEFAULT_VAT_IDENTIFICATION);

        // Get all the tenantList where vatIdentification equals to UPDATED_VAT_IDENTIFICATION
        defaultTenantShouldNotBeFound("vatIdentification.equals=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllTenantsByVatIdentificationIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where vatIdentification in DEFAULT_VAT_IDENTIFICATION or UPDATED_VAT_IDENTIFICATION
        defaultTenantShouldBeFound("vatIdentification.in=" + DEFAULT_VAT_IDENTIFICATION + "," + UPDATED_VAT_IDENTIFICATION);

        // Get all the tenantList where vatIdentification equals to UPDATED_VAT_IDENTIFICATION
        defaultTenantShouldNotBeFound("vatIdentification.in=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllTenantsByVatIdentificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where vatIdentification is not null
        defaultTenantShouldBeFound("vatIdentification.specified=true");

        // Get all the tenantList where vatIdentification is null
        defaultTenantShouldNotBeFound("vatIdentification.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessIdentificationIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessIdentification equals to DEFAULT_BUSINESS_IDENTIFICATION
        defaultTenantShouldBeFound("businessIdentification.equals=" + DEFAULT_BUSINESS_IDENTIFICATION);

        // Get all the tenantList where businessIdentification equals to UPDATED_BUSINESS_IDENTIFICATION
        defaultTenantShouldNotBeFound("businessIdentification.equals=" + UPDATED_BUSINESS_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessIdentificationIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessIdentification in DEFAULT_BUSINESS_IDENTIFICATION or UPDATED_BUSINESS_IDENTIFICATION
        defaultTenantShouldBeFound("businessIdentification.in=" + DEFAULT_BUSINESS_IDENTIFICATION + "," + UPDATED_BUSINESS_IDENTIFICATION);

        // Get all the tenantList where businessIdentification equals to UPDATED_BUSINESS_IDENTIFICATION
        defaultTenantShouldNotBeFound("businessIdentification.in=" + UPDATED_BUSINESS_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessIdentificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessIdentification is not null
        defaultTenantShouldBeFound("businessIdentification.specified=true");

        // Get all the tenantList where businessIdentification is null
        defaultTenantShouldNotBeFound("businessIdentification.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessRegistryIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessRegistry equals to DEFAULT_BUSINESS_REGISTRY
        defaultTenantShouldBeFound("businessRegistry.equals=" + DEFAULT_BUSINESS_REGISTRY);

        // Get all the tenantList where businessRegistry equals to UPDATED_BUSINESS_REGISTRY
        defaultTenantShouldNotBeFound("businessRegistry.equals=" + UPDATED_BUSINESS_REGISTRY);
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessRegistryIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessRegistry in DEFAULT_BUSINESS_REGISTRY or UPDATED_BUSINESS_REGISTRY
        defaultTenantShouldBeFound("businessRegistry.in=" + DEFAULT_BUSINESS_REGISTRY + "," + UPDATED_BUSINESS_REGISTRY);

        // Get all the tenantList where businessRegistry equals to UPDATED_BUSINESS_REGISTRY
        defaultTenantShouldNotBeFound("businessRegistry.in=" + UPDATED_BUSINESS_REGISTRY);
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessRegistryIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessRegistry is not null
        defaultTenantShouldBeFound("businessRegistry.specified=true");

        // Get all the tenantList where businessRegistry is null
        defaultTenantShouldNotBeFound("businessRegistry.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessCode equals to DEFAULT_BUSINESS_CODE
        defaultTenantShouldBeFound("businessCode.equals=" + DEFAULT_BUSINESS_CODE);

        // Get all the tenantList where businessCode equals to UPDATED_BUSINESS_CODE
        defaultTenantShouldNotBeFound("businessCode.equals=" + UPDATED_BUSINESS_CODE);
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessCodeIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessCode in DEFAULT_BUSINESS_CODE or UPDATED_BUSINESS_CODE
        defaultTenantShouldBeFound("businessCode.in=" + DEFAULT_BUSINESS_CODE + "," + UPDATED_BUSINESS_CODE);

        // Get all the tenantList where businessCode equals to UPDATED_BUSINESS_CODE
        defaultTenantShouldNotBeFound("businessCode.in=" + UPDATED_BUSINESS_CODE);
    }

    @Test
    @Transactional
    public void getAllTenantsByBusinessCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where businessCode is not null
        defaultTenantShouldBeFound("businessCode.specified=true");

        // Get all the tenantList where businessCode is null
        defaultTenantShouldNotBeFound("businessCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByFinancialYearFromIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where financialYearFrom equals to DEFAULT_FINANCIAL_YEAR_FROM
        defaultTenantShouldBeFound("financialYearFrom.equals=" + DEFAULT_FINANCIAL_YEAR_FROM);

        // Get all the tenantList where financialYearFrom equals to UPDATED_FINANCIAL_YEAR_FROM
        defaultTenantShouldNotBeFound("financialYearFrom.equals=" + UPDATED_FINANCIAL_YEAR_FROM);
    }

    @Test
    @Transactional
    public void getAllTenantsByFinancialYearFromIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where financialYearFrom in DEFAULT_FINANCIAL_YEAR_FROM or UPDATED_FINANCIAL_YEAR_FROM
        defaultTenantShouldBeFound("financialYearFrom.in=" + DEFAULT_FINANCIAL_YEAR_FROM + "," + UPDATED_FINANCIAL_YEAR_FROM);

        // Get all the tenantList where financialYearFrom equals to UPDATED_FINANCIAL_YEAR_FROM
        defaultTenantShouldNotBeFound("financialYearFrom.in=" + UPDATED_FINANCIAL_YEAR_FROM);
    }

    @Test
    @Transactional
    public void getAllTenantsByFinancialYearFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where financialYearFrom is not null
        defaultTenantShouldBeFound("financialYearFrom.specified=true");

        // Get all the tenantList where financialYearFrom is null
        defaultTenantShouldNotBeFound("financialYearFrom.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByFinancialYearToIsEqualToSomething() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where financialYearTo equals to DEFAULT_FINANCIAL_YEAR_TO
        defaultTenantShouldBeFound("financialYearTo.equals=" + DEFAULT_FINANCIAL_YEAR_TO);

        // Get all the tenantList where financialYearTo equals to UPDATED_FINANCIAL_YEAR_TO
        defaultTenantShouldNotBeFound("financialYearTo.equals=" + UPDATED_FINANCIAL_YEAR_TO);
    }

    @Test
    @Transactional
    public void getAllTenantsByFinancialYearToIsInShouldWork() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where financialYearTo in DEFAULT_FINANCIAL_YEAR_TO or UPDATED_FINANCIAL_YEAR_TO
        defaultTenantShouldBeFound("financialYearTo.in=" + DEFAULT_FINANCIAL_YEAR_TO + "," + UPDATED_FINANCIAL_YEAR_TO);

        // Get all the tenantList where financialYearTo equals to UPDATED_FINANCIAL_YEAR_TO
        defaultTenantShouldNotBeFound("financialYearTo.in=" + UPDATED_FINANCIAL_YEAR_TO);
    }

    @Test
    @Transactional
    public void getAllTenantsByFinancialYearToIsNullOrNotNull() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);

        // Get all the tenantList where financialYearTo is not null
        defaultTenantShouldBeFound("financialYearTo.specified=true");

        // Get all the tenantList where financialYearTo is null
        defaultTenantShouldNotBeFound("financialYearTo.specified=false");
    }

    @Test
    @Transactional
    public void getAllTenantsByInvoiceAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        Address invoiceAddress = AddressResourceIntTest.createEntity(em);
        em.persist(invoiceAddress);
        em.flush();
        tenant.setInvoiceAddress(invoiceAddress);
        tenantRepository.saveAndFlush(tenant);
        Long invoiceAddressId = invoiceAddress.getId();

        // Get all the tenantList where invoiceAddress equals to invoiceAddressId
        defaultTenantShouldBeFound("invoiceAddressId.equals=" + invoiceAddressId);

        // Get all the tenantList where invoiceAddress equals to invoiceAddressId + 1
        defaultTenantShouldNotBeFound("invoiceAddressId.equals=" + (invoiceAddressId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByBankIsEqualToSomething() throws Exception {
        // Initialize the database
        Bank bank = BankResourceIntTest.createEntity(em);
        em.persist(bank);
        em.flush();
        tenant.setBank(bank);
        tenantRepository.saveAndFlush(tenant);
        Long bankId = bank.getId();

        // Get all the tenantList where bank equals to bankId
        defaultTenantShouldBeFound("bankId.equals=" + bankId);

        // Get all the tenantList where bank equals to bankId + 1
        defaultTenantShouldNotBeFound("bankId.equals=" + (bankId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByLogoIsEqualToSomething() throws Exception {
        // Initialize the database
        Document logo = DocumentResourceIntTest.createEntity(em);
        em.persist(logo);
        em.flush();
        tenant.setLogo(logo);
        tenantRepository.saveAndFlush(tenant);
        Long logoId = logo.getId();

        // Get all the tenantList where logo equals to logoId
        defaultTenantShouldBeFound("logoId.equals=" + logoId);

        // Get all the tenantList where logo equals to logoId + 1
        defaultTenantShouldNotBeFound("logoId.equals=" + (logoId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        Contact contact = ContactResourceIntTest.createEntity(em);
        em.persist(contact);
        em.flush();
        tenant.setContact(contact);
        tenantRepository.saveAndFlush(tenant);
        Long contactId = contact.getId();

        // Get all the tenantList where contact equals to contactId
        defaultTenantShouldBeFound("contactId.equals=" + contactId);

        // Get all the tenantList where contact equals to contactId + 1
        defaultTenantShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByEventsIsEqualToSomething() throws Exception {
        // Initialize the database
        Event events = EventResourceIntTest.createEntity(em);
        em.persist(events);
        em.flush();
        tenant.addEvents(events);
        tenantRepository.saveAndFlush(tenant);
        Long eventsId = events.getId();

        // Get all the tenantList where events equals to eventsId
        defaultTenantShouldBeFound("eventsId.equals=" + eventsId);

        // Get all the tenantList where events equals to eventsId + 1
        defaultTenantShouldNotBeFound("eventsId.equals=" + (eventsId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByInvoicesIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoices = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoices);
        em.flush();
        tenant.addInvoices(invoices);
        tenantRepository.saveAndFlush(tenant);
        Long invoicesId = invoices.getId();

        // Get all the tenantList where invoices equals to invoicesId
        defaultTenantShouldBeFound("invoicesId.equals=" + invoicesId);

        // Get all the tenantList where invoices equals to invoicesId + 1
        defaultTenantShouldNotBeFound("invoicesId.equals=" + (invoicesId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByInvoiceDefinitionsIsEqualToSomething() throws Exception {
        // Initialize the database
        InvoiceDefinition invoiceDefinitions = InvoiceDefinitionResourceIntTest.createEntity(em);
        em.persist(invoiceDefinitions);
        em.flush();
        tenant.addInvoiceDefinitions(invoiceDefinitions);
        tenantRepository.saveAndFlush(tenant);
        Long invoiceDefinitionsId = invoiceDefinitions.getId();

        // Get all the tenantList where invoiceDefinitions equals to invoiceDefinitionsId
        defaultTenantShouldBeFound("invoiceDefinitionsId.equals=" + invoiceDefinitionsId);

        // Get all the tenantList where invoiceDefinitions equals to invoiceDefinitionsId + 1
        defaultTenantShouldNotBeFound("invoiceDefinitionsId.equals=" + (invoiceDefinitionsId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByQuotationsIsEqualToSomething() throws Exception {
        // Initialize the database
        Quotation quotations = QuotationResourceIntTest.createEntity(em);
        em.persist(quotations);
        em.flush();
        tenant.addQuotations(quotations);
        tenantRepository.saveAndFlush(tenant);
        Long quotationsId = quotations.getId();

        // Get all the tenantList where quotations equals to quotationsId
        defaultTenantShouldBeFound("quotationsId.equals=" + quotationsId);

        // Get all the tenantList where quotations equals to quotationsId + 1
        defaultTenantShouldNotBeFound("quotationsId.equals=" + (quotationsId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByQuotationDefinitionsIsEqualToSomething() throws Exception {
        // Initialize the database
        QuotationDefinition quotationDefinitions = QuotationDefinitionResourceIntTest.createEntity(em);
        em.persist(quotationDefinitions);
        em.flush();
        tenant.addQuotationDefinitions(quotationDefinitions);
        tenantRepository.saveAndFlush(tenant);
        Long quotationDefinitionsId = quotationDefinitions.getId();

        // Get all the tenantList where quotationDefinitions equals to quotationDefinitionsId
        defaultTenantShouldBeFound("quotationDefinitionsId.equals=" + quotationDefinitionsId);

        // Get all the tenantList where quotationDefinitions equals to quotationDefinitionsId + 1
        defaultTenantShouldNotBeFound("quotationDefinitionsId.equals=" + (quotationDefinitionsId + 1));
    }


    @Test
    @Transactional
    public void getAllTenantsByFamiliesIsEqualToSomething() throws Exception {
        // Initialize the database
        Family families = FamilyResourceIntTest.createEntity(em);
        em.persist(families);
        em.flush();
        tenant.addFamilies(families);
        tenantRepository.saveAndFlush(tenant);
        Long familiesId = families.getId();

        // Get all the tenantList where families equals to familiesId
        defaultTenantShouldBeFound("familiesId.equals=" + familiesId);

        // Get all the tenantList where families equals to familiesId + 1
        defaultTenantShouldNotBeFound("familiesId.equals=" + (familiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTenantShouldBeFound(String filter) throws Exception {
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].vatIdentification").value(hasItem(DEFAULT_VAT_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].businessIdentification").value(hasItem(DEFAULT_BUSINESS_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].businessRegistry").value(hasItem(DEFAULT_BUSINESS_REGISTRY.toString())))
            .andExpect(jsonPath("$.[*].businessCode").value(hasItem(DEFAULT_BUSINESS_CODE.toString())))
            .andExpect(jsonPath("$.[*].financialYearFrom").value(hasItem(DEFAULT_FINANCIAL_YEAR_FROM.toString())))
            .andExpect(jsonPath("$.[*].financialYearTo").value(hasItem(DEFAULT_FINANCIAL_YEAR_TO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTenantShouldNotBeFound(String filter) throws Exception {
        restTenantMockMvc.perform(get("/api/tenants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTenant() throws Exception {
        // Get the tenant
        restTenantMockMvc.perform(get("/api/tenants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Update the tenant
        Tenant updatedTenant = tenantRepository.findOne(tenant.getId());
        // Disconnect from session so that the updates on updatedTenant are not directly saved in db
        em.detach(updatedTenant);
        updatedTenant
            .slug(UPDATED_SLUG)
            .name(UPDATED_NAME)
            .companyName(UPDATED_COMPANY_NAME)
            .description(UPDATED_DESCRIPTION)
            .vatIdentification(UPDATED_VAT_IDENTIFICATION)
            .businessIdentification(UPDATED_BUSINESS_IDENTIFICATION)
            .businessRegistry(UPDATED_BUSINESS_REGISTRY)
            .businessCode(UPDATED_BUSINESS_CODE)
            .financialYearFrom(UPDATED_FINANCIAL_YEAR_FROM)
            .financialYearTo(UPDATED_FINANCIAL_YEAR_TO);
        TenantDTO tenantDTO = tenantMapper.toDto(updatedTenant);

        restTenantMockMvc.perform(put("/api/tenants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isOk());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate);
        Tenant testTenant = tenantList.get(tenantList.size() - 1);
        assertThat(testTenant.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testTenant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTenant.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testTenant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTenant.getVatIdentification()).isEqualTo(UPDATED_VAT_IDENTIFICATION);
        assertThat(testTenant.getBusinessIdentification()).isEqualTo(UPDATED_BUSINESS_IDENTIFICATION);
        assertThat(testTenant.getBusinessRegistry()).isEqualTo(UPDATED_BUSINESS_REGISTRY);
        assertThat(testTenant.getBusinessCode()).isEqualTo(UPDATED_BUSINESS_CODE);
        assertThat(testTenant.getFinancialYearFrom()).isEqualTo(UPDATED_FINANCIAL_YEAR_FROM);
        assertThat(testTenant.getFinancialYearTo()).isEqualTo(UPDATED_FINANCIAL_YEAR_TO);
    }

    @Test
    @Transactional
    public void updateNonExistingTenant() throws Exception {
        int databaseSizeBeforeUpdate = tenantRepository.findAll().size();

        // Create the Tenant
        TenantDTO tenantDTO = tenantMapper.toDto(tenant);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTenantMockMvc.perform(put("/api/tenants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tenantDTO)))
            .andExpect(status().isCreated());

        // Validate the Tenant in the database
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTenant() throws Exception {
        // Initialize the database
        tenantRepository.saveAndFlush(tenant);
        int databaseSizeBeforeDelete = tenantRepository.findAll().size();

        // Get the tenant
        restTenantMockMvc.perform(delete("/api/tenants/{id}", tenant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tenant> tenantList = tenantRepository.findAll();
        assertThat(tenantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tenant.class);
        Tenant tenant1 = new Tenant();
        tenant1.setId(1L);
        Tenant tenant2 = new Tenant();
        tenant2.setId(tenant1.getId());
        assertThat(tenant1).isEqualTo(tenant2);
        tenant2.setId(2L);
        assertThat(tenant1).isNotEqualTo(tenant2);
        tenant1.setId(null);
        assertThat(tenant1).isNotEqualTo(tenant2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantDTO.class);
        TenantDTO tenantDTO1 = new TenantDTO();
        tenantDTO1.setId(1L);
        TenantDTO tenantDTO2 = new TenantDTO();
        assertThat(tenantDTO1).isNotEqualTo(tenantDTO2);
        tenantDTO2.setId(tenantDTO1.getId());
        assertThat(tenantDTO1).isEqualTo(tenantDTO2);
        tenantDTO2.setId(2L);
        assertThat(tenantDTO1).isNotEqualTo(tenantDTO2);
        tenantDTO1.setId(null);
        assertThat(tenantDTO1).isNotEqualTo(tenantDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tenantMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tenantMapper.fromId(null)).isNull();
    }
}
