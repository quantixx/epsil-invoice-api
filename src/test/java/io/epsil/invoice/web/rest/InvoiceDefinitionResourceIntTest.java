package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.InvoiceDefinition;
import io.epsil.invoice.domain.Family;
import io.epsil.invoice.domain.Language;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.repository.InvoiceDefinitionRepository;
import io.epsil.invoice.service.InvoiceDefinitionService;
import io.epsil.invoice.service.dto.InvoiceDefinitionDTO;
import io.epsil.invoice.service.mapper.InvoiceDefinitionMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.InvoiceDefinitionCriteria;
import io.epsil.invoice.service.InvoiceDefinitionQueryService;

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

import io.epsil.invoice.domain.enumeration.DocumentType;
/**
 * Test class for the InvoiceDefinitionResource REST controller.
 *
 * @see InvoiceDefinitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class InvoiceDefinitionResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentType DEFAULT_DOC_TYPE = DocumentType.TENANT_LOGO;
    private static final DocumentType UPDATED_DOC_TYPE = DocumentType.INVOICE;

    private static final Float DEFAULT_VAT_RATE = 1F;
    private static final Float UPDATED_VAT_RATE = 2F;

    private static final String DEFAULT_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_TERMS = "BBBBBBBBBB";

    private static final String DEFAULT_PENALTIES = "AAAAAAAAAA";
    private static final String UPDATED_PENALTIES = "BBBBBBBBBB";

    @Autowired
    private InvoiceDefinitionRepository invoiceDefinitionRepository;

    @Autowired
    private InvoiceDefinitionMapper invoiceDefinitionMapper;

    @Autowired
    private InvoiceDefinitionService invoiceDefinitionService;

    @Autowired
    private InvoiceDefinitionQueryService invoiceDefinitionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceDefinitionMockMvc;

    private InvoiceDefinition invoiceDefinition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceDefinitionResource invoiceDefinitionResource = new InvoiceDefinitionResource(invoiceDefinitionService, invoiceDefinitionQueryService);
        this.restInvoiceDefinitionMockMvc = MockMvcBuilders.standaloneSetup(invoiceDefinitionResource)
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
    public static InvoiceDefinition createEntity(EntityManager em) {
        InvoiceDefinition invoiceDefinition = new InvoiceDefinition()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .docType(DEFAULT_DOC_TYPE)
            .vatRate(DEFAULT_VAT_RATE)
            .terms(DEFAULT_TERMS)
            .penalties(DEFAULT_PENALTIES);
        // Add required entity
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        invoiceDefinition.setFamily(family);
        // Add required entity
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        invoiceDefinition.setLanguage(language);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        invoiceDefinition.setTenant(tenant);
        return invoiceDefinition;
    }

    @Before
    public void initTest() {
        invoiceDefinition = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceDefinition() throws Exception {
        int databaseSizeBeforeCreate = invoiceDefinitionRepository.findAll().size();

        // Create the InvoiceDefinition
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);
        restInvoiceDefinitionMockMvc.perform(post("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceDefinition in the database
        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceDefinition testInvoiceDefinition = invoiceDefinitionList.get(invoiceDefinitionList.size() - 1);
        assertThat(testInvoiceDefinition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInvoiceDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceDefinition.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testInvoiceDefinition.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testInvoiceDefinition.getTerms()).isEqualTo(DEFAULT_TERMS);
        assertThat(testInvoiceDefinition.getPenalties()).isEqualTo(DEFAULT_PENALTIES);
    }

    @Test
    @Transactional
    public void createInvoiceDefinitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceDefinitionRepository.findAll().size();

        // Create the InvoiceDefinition with an existing ID
        invoiceDefinition.setId(1L);
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceDefinitionMockMvc.perform(post("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceDefinition in the database
        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceDefinitionRepository.findAll().size();
        // set the field null
        invoiceDefinition.setDocType(null);

        // Create the InvoiceDefinition, which fails.
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);

        restInvoiceDefinitionMockMvc.perform(post("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceDefinitionRepository.findAll().size();
        // set the field null
        invoiceDefinition.setVatRate(null);

        // Create the InvoiceDefinition, which fails.
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);

        restInvoiceDefinitionMockMvc.perform(post("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTermsIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceDefinitionRepository.findAll().size();
        // set the field null
        invoiceDefinition.setTerms(null);

        // Create the InvoiceDefinition, which fails.
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);

        restInvoiceDefinitionMockMvc.perform(post("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPenaltiesIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceDefinitionRepository.findAll().size();
        // set the field null
        invoiceDefinition.setPenalties(null);

        // Create the InvoiceDefinition, which fails.
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);

        restInvoiceDefinitionMockMvc.perform(post("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitions() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList
        restInvoiceDefinitionMockMvc.perform(get("/api/invoice-definitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].terms").value(hasItem(DEFAULT_TERMS.toString())))
            .andExpect(jsonPath("$.[*].penalties").value(hasItem(DEFAULT_PENALTIES.toString())));
    }

    @Test
    @Transactional
    public void getInvoiceDefinition() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get the invoiceDefinition
        restInvoiceDefinitionMockMvc.perform(get("/api/invoice-definitions/{id}", invoiceDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceDefinition.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.terms").value(DEFAULT_TERMS.toString()))
            .andExpect(jsonPath("$.penalties").value(DEFAULT_PENALTIES.toString()));
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where title equals to DEFAULT_TITLE
        defaultInvoiceDefinitionShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the invoiceDefinitionList where title equals to UPDATED_TITLE
        defaultInvoiceDefinitionShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultInvoiceDefinitionShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the invoiceDefinitionList where title equals to UPDATED_TITLE
        defaultInvoiceDefinitionShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where title is not null
        defaultInvoiceDefinitionShouldBeFound("title.specified=true");

        // Get all the invoiceDefinitionList where title is null
        defaultInvoiceDefinitionShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where description equals to DEFAULT_DESCRIPTION
        defaultInvoiceDefinitionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the invoiceDefinitionList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceDefinitionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInvoiceDefinitionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the invoiceDefinitionList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceDefinitionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where description is not null
        defaultInvoiceDefinitionShouldBeFound("description.specified=true");

        // Get all the invoiceDefinitionList where description is null
        defaultInvoiceDefinitionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where docType equals to DEFAULT_DOC_TYPE
        defaultInvoiceDefinitionShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the invoiceDefinitionList where docType equals to UPDATED_DOC_TYPE
        defaultInvoiceDefinitionShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultInvoiceDefinitionShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the invoiceDefinitionList where docType equals to UPDATED_DOC_TYPE
        defaultInvoiceDefinitionShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where docType is not null
        defaultInvoiceDefinitionShouldBeFound("docType.specified=true");

        // Get all the invoiceDefinitionList where docType is null
        defaultInvoiceDefinitionShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByVatRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where vatRate equals to DEFAULT_VAT_RATE
        defaultInvoiceDefinitionShouldBeFound("vatRate.equals=" + DEFAULT_VAT_RATE);

        // Get all the invoiceDefinitionList where vatRate equals to UPDATED_VAT_RATE
        defaultInvoiceDefinitionShouldNotBeFound("vatRate.equals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByVatRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where vatRate in DEFAULT_VAT_RATE or UPDATED_VAT_RATE
        defaultInvoiceDefinitionShouldBeFound("vatRate.in=" + DEFAULT_VAT_RATE + "," + UPDATED_VAT_RATE);

        // Get all the invoiceDefinitionList where vatRate equals to UPDATED_VAT_RATE
        defaultInvoiceDefinitionShouldNotBeFound("vatRate.in=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByVatRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where vatRate is not null
        defaultInvoiceDefinitionShouldBeFound("vatRate.specified=true");

        // Get all the invoiceDefinitionList where vatRate is null
        defaultInvoiceDefinitionShouldNotBeFound("vatRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTermsIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where terms equals to DEFAULT_TERMS
        defaultInvoiceDefinitionShouldBeFound("terms.equals=" + DEFAULT_TERMS);

        // Get all the invoiceDefinitionList where terms equals to UPDATED_TERMS
        defaultInvoiceDefinitionShouldNotBeFound("terms.equals=" + UPDATED_TERMS);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTermsIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where terms in DEFAULT_TERMS or UPDATED_TERMS
        defaultInvoiceDefinitionShouldBeFound("terms.in=" + DEFAULT_TERMS + "," + UPDATED_TERMS);

        // Get all the invoiceDefinitionList where terms equals to UPDATED_TERMS
        defaultInvoiceDefinitionShouldNotBeFound("terms.in=" + UPDATED_TERMS);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTermsIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where terms is not null
        defaultInvoiceDefinitionShouldBeFound("terms.specified=true");

        // Get all the invoiceDefinitionList where terms is null
        defaultInvoiceDefinitionShouldNotBeFound("terms.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByPenaltiesIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where penalties equals to DEFAULT_PENALTIES
        defaultInvoiceDefinitionShouldBeFound("penalties.equals=" + DEFAULT_PENALTIES);

        // Get all the invoiceDefinitionList where penalties equals to UPDATED_PENALTIES
        defaultInvoiceDefinitionShouldNotBeFound("penalties.equals=" + UPDATED_PENALTIES);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByPenaltiesIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where penalties in DEFAULT_PENALTIES or UPDATED_PENALTIES
        defaultInvoiceDefinitionShouldBeFound("penalties.in=" + DEFAULT_PENALTIES + "," + UPDATED_PENALTIES);

        // Get all the invoiceDefinitionList where penalties equals to UPDATED_PENALTIES
        defaultInvoiceDefinitionShouldNotBeFound("penalties.in=" + UPDATED_PENALTIES);
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByPenaltiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);

        // Get all the invoiceDefinitionList where penalties is not null
        defaultInvoiceDefinitionShouldBeFound("penalties.specified=true");

        // Get all the invoiceDefinitionList where penalties is null
        defaultInvoiceDefinitionShouldNotBeFound("penalties.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        invoiceDefinition.setFamily(family);
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);
        Long familyId = family.getId();

        // Get all the invoiceDefinitionList where family equals to familyId
        defaultInvoiceDefinitionShouldBeFound("familyId.equals=" + familyId);

        // Get all the invoiceDefinitionList where family equals to familyId + 1
        defaultInvoiceDefinitionShouldNotBeFound("familyId.equals=" + (familyId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        invoiceDefinition.setLanguage(language);
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);
        Long languageId = language.getId();

        // Get all the invoiceDefinitionList where language equals to languageId
        defaultInvoiceDefinitionShouldBeFound("languageId.equals=" + languageId);

        // Get all the invoiceDefinitionList where language equals to languageId + 1
        defaultInvoiceDefinitionShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoiceDefinitionsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        invoiceDefinition.setTenant(tenant);
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);
        Long tenantId = tenant.getId();

        // Get all the invoiceDefinitionList where tenant equals to tenantId
        defaultInvoiceDefinitionShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceDefinitionList where tenant equals to tenantId + 1
        defaultInvoiceDefinitionShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceDefinitionShouldBeFound(String filter) throws Exception {
        restInvoiceDefinitionMockMvc.perform(get("/api/invoice-definitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].terms").value(hasItem(DEFAULT_TERMS.toString())))
            .andExpect(jsonPath("$.[*].penalties").value(hasItem(DEFAULT_PENALTIES.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceDefinitionShouldNotBeFound(String filter) throws Exception {
        restInvoiceDefinitionMockMvc.perform(get("/api/invoice-definitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvoiceDefinition() throws Exception {
        // Get the invoiceDefinition
        restInvoiceDefinitionMockMvc.perform(get("/api/invoice-definitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceDefinition() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);
        int databaseSizeBeforeUpdate = invoiceDefinitionRepository.findAll().size();

        // Update the invoiceDefinition
        InvoiceDefinition updatedInvoiceDefinition = invoiceDefinitionRepository.findOne(invoiceDefinition.getId());
        // Disconnect from session so that the updates on updatedInvoiceDefinition are not directly saved in db
        em.detach(updatedInvoiceDefinition);
        updatedInvoiceDefinition
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .docType(UPDATED_DOC_TYPE)
            .vatRate(UPDATED_VAT_RATE)
            .terms(UPDATED_TERMS)
            .penalties(UPDATED_PENALTIES);
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(updatedInvoiceDefinition);

        restInvoiceDefinitionMockMvc.perform(put("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceDefinition in the database
        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeUpdate);
        InvoiceDefinition testInvoiceDefinition = invoiceDefinitionList.get(invoiceDefinitionList.size() - 1);
        assertThat(testInvoiceDefinition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInvoiceDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceDefinition.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testInvoiceDefinition.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testInvoiceDefinition.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testInvoiceDefinition.getPenalties()).isEqualTo(UPDATED_PENALTIES);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceDefinition() throws Exception {
        int databaseSizeBeforeUpdate = invoiceDefinitionRepository.findAll().size();

        // Create the InvoiceDefinition
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionMapper.toDto(invoiceDefinition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceDefinitionMockMvc.perform(put("/api/invoice-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDefinitionDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceDefinition in the database
        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoiceDefinition() throws Exception {
        // Initialize the database
        invoiceDefinitionRepository.saveAndFlush(invoiceDefinition);
        int databaseSizeBeforeDelete = invoiceDefinitionRepository.findAll().size();

        // Get the invoiceDefinition
        restInvoiceDefinitionMockMvc.perform(delete("/api/invoice-definitions/{id}", invoiceDefinition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceDefinition> invoiceDefinitionList = invoiceDefinitionRepository.findAll();
        assertThat(invoiceDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDefinition.class);
        InvoiceDefinition invoiceDefinition1 = new InvoiceDefinition();
        invoiceDefinition1.setId(1L);
        InvoiceDefinition invoiceDefinition2 = new InvoiceDefinition();
        invoiceDefinition2.setId(invoiceDefinition1.getId());
        assertThat(invoiceDefinition1).isEqualTo(invoiceDefinition2);
        invoiceDefinition2.setId(2L);
        assertThat(invoiceDefinition1).isNotEqualTo(invoiceDefinition2);
        invoiceDefinition1.setId(null);
        assertThat(invoiceDefinition1).isNotEqualTo(invoiceDefinition2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDefinitionDTO.class);
        InvoiceDefinitionDTO invoiceDefinitionDTO1 = new InvoiceDefinitionDTO();
        invoiceDefinitionDTO1.setId(1L);
        InvoiceDefinitionDTO invoiceDefinitionDTO2 = new InvoiceDefinitionDTO();
        assertThat(invoiceDefinitionDTO1).isNotEqualTo(invoiceDefinitionDTO2);
        invoiceDefinitionDTO2.setId(invoiceDefinitionDTO1.getId());
        assertThat(invoiceDefinitionDTO1).isEqualTo(invoiceDefinitionDTO2);
        invoiceDefinitionDTO2.setId(2L);
        assertThat(invoiceDefinitionDTO1).isNotEqualTo(invoiceDefinitionDTO2);
        invoiceDefinitionDTO1.setId(null);
        assertThat(invoiceDefinitionDTO1).isNotEqualTo(invoiceDefinitionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceDefinitionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceDefinitionMapper.fromId(null)).isNull();
    }
}
