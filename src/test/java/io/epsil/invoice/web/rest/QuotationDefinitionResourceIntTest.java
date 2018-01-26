package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.QuotationDefinition;
import io.epsil.invoice.domain.Family;
import io.epsil.invoice.domain.Language;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.repository.QuotationDefinitionRepository;
import io.epsil.invoice.service.QuotationDefinitionService;
import io.epsil.invoice.service.dto.QuotationDefinitionDTO;
import io.epsil.invoice.service.mapper.QuotationDefinitionMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.QuotationDefinitionCriteria;
import io.epsil.invoice.service.QuotationDefinitionQueryService;

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
 * Test class for the QuotationDefinitionResource REST controller.
 *
 * @see QuotationDefinitionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class QuotationDefinitionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentType DEFAULT_DOC_TYPE = DocumentType.TENANT_LOGO;
    private static final DocumentType UPDATED_DOC_TYPE = DocumentType.INVOICE;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Float DEFAULT_VAT_RATE = 1F;
    private static final Float UPDATED_VAT_RATE = 2F;

    private static final String DEFAULT_VALIDITY_TERMS = "AAAAAAAAAA";
    private static final String UPDATED_VALIDITY_TERMS = "BBBBBBBBBB";

    private static final String DEFAULT_ACCEPTATION = "AAAAAAAAAA";
    private static final String UPDATED_ACCEPTATION = "BBBBBBBBBB";

    @Autowired
    private QuotationDefinitionRepository quotationDefinitionRepository;

    @Autowired
    private QuotationDefinitionMapper quotationDefinitionMapper;

    @Autowired
    private QuotationDefinitionService quotationDefinitionService;

    @Autowired
    private QuotationDefinitionQueryService quotationDefinitionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuotationDefinitionMockMvc;

    private QuotationDefinition quotationDefinition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuotationDefinitionResource quotationDefinitionResource = new QuotationDefinitionResource(quotationDefinitionService, quotationDefinitionQueryService);
        this.restQuotationDefinitionMockMvc = MockMvcBuilders.standaloneSetup(quotationDefinitionResource)
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
    public static QuotationDefinition createEntity(EntityManager em) {
        QuotationDefinition quotationDefinition = new QuotationDefinition()
            .description(DEFAULT_DESCRIPTION)
            .docType(DEFAULT_DOC_TYPE)
            .type(DEFAULT_TYPE)
            .vatRate(DEFAULT_VAT_RATE)
            .validityTerms(DEFAULT_VALIDITY_TERMS)
            .acceptation(DEFAULT_ACCEPTATION);
        // Add required entity
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        quotationDefinition.setFamily(family);
        // Add required entity
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        quotationDefinition.setLanguage(language);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        quotationDefinition.setTenant(tenant);
        return quotationDefinition;
    }

    @Before
    public void initTest() {
        quotationDefinition = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotationDefinition() throws Exception {
        int databaseSizeBeforeCreate = quotationDefinitionRepository.findAll().size();

        // Create the QuotationDefinition
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(quotationDefinition);
        restQuotationDefinitionMockMvc.perform(post("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isCreated());

        // Validate the QuotationDefinition in the database
        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        QuotationDefinition testQuotationDefinition = quotationDefinitionList.get(quotationDefinitionList.size() - 1);
        assertThat(testQuotationDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuotationDefinition.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testQuotationDefinition.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuotationDefinition.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testQuotationDefinition.getValidityTerms()).isEqualTo(DEFAULT_VALIDITY_TERMS);
        assertThat(testQuotationDefinition.getAcceptation()).isEqualTo(DEFAULT_ACCEPTATION);
    }

    @Test
    @Transactional
    public void createQuotationDefinitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationDefinitionRepository.findAll().size();

        // Create the QuotationDefinition with an existing ID
        quotationDefinition.setId(1L);
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(quotationDefinition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationDefinitionMockMvc.perform(post("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationDefinition in the database
        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationDefinitionRepository.findAll().size();
        // set the field null
        quotationDefinition.setDocType(null);

        // Create the QuotationDefinition, which fails.
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(quotationDefinition);

        restQuotationDefinitionMockMvc.perform(post("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationDefinitionRepository.findAll().size();
        // set the field null
        quotationDefinition.setType(null);

        // Create the QuotationDefinition, which fails.
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(quotationDefinition);

        restQuotationDefinitionMockMvc.perform(post("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidityTermsIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationDefinitionRepository.findAll().size();
        // set the field null
        quotationDefinition.setValidityTerms(null);

        // Create the QuotationDefinition, which fails.
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(quotationDefinition);

        restQuotationDefinitionMockMvc.perform(post("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitions() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList
        restQuotationDefinitionMockMvc.perform(get("/api/quotation-definitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].validityTerms").value(hasItem(DEFAULT_VALIDITY_TERMS.toString())))
            .andExpect(jsonPath("$.[*].acceptation").value(hasItem(DEFAULT_ACCEPTATION.toString())));
    }

    @Test
    @Transactional
    public void getQuotationDefinition() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get the quotationDefinition
        restQuotationDefinitionMockMvc.perform(get("/api/quotation-definitions/{id}", quotationDefinition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotationDefinition.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.validityTerms").value(DEFAULT_VALIDITY_TERMS.toString()))
            .andExpect(jsonPath("$.acceptation").value(DEFAULT_ACCEPTATION.toString()));
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where description equals to DEFAULT_DESCRIPTION
        defaultQuotationDefinitionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the quotationDefinitionList where description equals to UPDATED_DESCRIPTION
        defaultQuotationDefinitionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQuotationDefinitionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the quotationDefinitionList where description equals to UPDATED_DESCRIPTION
        defaultQuotationDefinitionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where description is not null
        defaultQuotationDefinitionShouldBeFound("description.specified=true");

        // Get all the quotationDefinitionList where description is null
        defaultQuotationDefinitionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where docType equals to DEFAULT_DOC_TYPE
        defaultQuotationDefinitionShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the quotationDefinitionList where docType equals to UPDATED_DOC_TYPE
        defaultQuotationDefinitionShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultQuotationDefinitionShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the quotationDefinitionList where docType equals to UPDATED_DOC_TYPE
        defaultQuotationDefinitionShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where docType is not null
        defaultQuotationDefinitionShouldBeFound("docType.specified=true");

        // Get all the quotationDefinitionList where docType is null
        defaultQuotationDefinitionShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where type equals to DEFAULT_TYPE
        defaultQuotationDefinitionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the quotationDefinitionList where type equals to UPDATED_TYPE
        defaultQuotationDefinitionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultQuotationDefinitionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the quotationDefinitionList where type equals to UPDATED_TYPE
        defaultQuotationDefinitionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where type is not null
        defaultQuotationDefinitionShouldBeFound("type.specified=true");

        // Get all the quotationDefinitionList where type is null
        defaultQuotationDefinitionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByVatRateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where vatRate equals to DEFAULT_VAT_RATE
        defaultQuotationDefinitionShouldBeFound("vatRate.equals=" + DEFAULT_VAT_RATE);

        // Get all the quotationDefinitionList where vatRate equals to UPDATED_VAT_RATE
        defaultQuotationDefinitionShouldNotBeFound("vatRate.equals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByVatRateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where vatRate in DEFAULT_VAT_RATE or UPDATED_VAT_RATE
        defaultQuotationDefinitionShouldBeFound("vatRate.in=" + DEFAULT_VAT_RATE + "," + UPDATED_VAT_RATE);

        // Get all the quotationDefinitionList where vatRate equals to UPDATED_VAT_RATE
        defaultQuotationDefinitionShouldNotBeFound("vatRate.in=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByVatRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where vatRate is not null
        defaultQuotationDefinitionShouldBeFound("vatRate.specified=true");

        // Get all the quotationDefinitionList where vatRate is null
        defaultQuotationDefinitionShouldNotBeFound("vatRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByValidityTermsIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where validityTerms equals to DEFAULT_VALIDITY_TERMS
        defaultQuotationDefinitionShouldBeFound("validityTerms.equals=" + DEFAULT_VALIDITY_TERMS);

        // Get all the quotationDefinitionList where validityTerms equals to UPDATED_VALIDITY_TERMS
        defaultQuotationDefinitionShouldNotBeFound("validityTerms.equals=" + UPDATED_VALIDITY_TERMS);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByValidityTermsIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where validityTerms in DEFAULT_VALIDITY_TERMS or UPDATED_VALIDITY_TERMS
        defaultQuotationDefinitionShouldBeFound("validityTerms.in=" + DEFAULT_VALIDITY_TERMS + "," + UPDATED_VALIDITY_TERMS);

        // Get all the quotationDefinitionList where validityTerms equals to UPDATED_VALIDITY_TERMS
        defaultQuotationDefinitionShouldNotBeFound("validityTerms.in=" + UPDATED_VALIDITY_TERMS);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByValidityTermsIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where validityTerms is not null
        defaultQuotationDefinitionShouldBeFound("validityTerms.specified=true");

        // Get all the quotationDefinitionList where validityTerms is null
        defaultQuotationDefinitionShouldNotBeFound("validityTerms.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByAcceptationIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where acceptation equals to DEFAULT_ACCEPTATION
        defaultQuotationDefinitionShouldBeFound("acceptation.equals=" + DEFAULT_ACCEPTATION);

        // Get all the quotationDefinitionList where acceptation equals to UPDATED_ACCEPTATION
        defaultQuotationDefinitionShouldNotBeFound("acceptation.equals=" + UPDATED_ACCEPTATION);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByAcceptationIsInShouldWork() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where acceptation in DEFAULT_ACCEPTATION or UPDATED_ACCEPTATION
        defaultQuotationDefinitionShouldBeFound("acceptation.in=" + DEFAULT_ACCEPTATION + "," + UPDATED_ACCEPTATION);

        // Get all the quotationDefinitionList where acceptation equals to UPDATED_ACCEPTATION
        defaultQuotationDefinitionShouldNotBeFound("acceptation.in=" + UPDATED_ACCEPTATION);
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByAcceptationIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);

        // Get all the quotationDefinitionList where acceptation is not null
        defaultQuotationDefinitionShouldBeFound("acceptation.specified=true");

        // Get all the quotationDefinitionList where acceptation is null
        defaultQuotationDefinitionShouldNotBeFound("acceptation.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationDefinitionsByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        quotationDefinition.setFamily(family);
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);
        Long familyId = family.getId();

        // Get all the quotationDefinitionList where family equals to familyId
        defaultQuotationDefinitionShouldBeFound("familyId.equals=" + familyId);

        // Get all the quotationDefinitionList where family equals to familyId + 1
        defaultQuotationDefinitionShouldNotBeFound("familyId.equals=" + (familyId + 1));
    }


    @Test
    @Transactional
    public void getAllQuotationDefinitionsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        quotationDefinition.setLanguage(language);
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);
        Long languageId = language.getId();

        // Get all the quotationDefinitionList where language equals to languageId
        defaultQuotationDefinitionShouldBeFound("languageId.equals=" + languageId);

        // Get all the quotationDefinitionList where language equals to languageId + 1
        defaultQuotationDefinitionShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }


    @Test
    @Transactional
    public void getAllQuotationDefinitionsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        quotationDefinition.setTenant(tenant);
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);
        Long tenantId = tenant.getId();

        // Get all the quotationDefinitionList where tenant equals to tenantId
        defaultQuotationDefinitionShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the quotationDefinitionList where tenant equals to tenantId + 1
        defaultQuotationDefinitionShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuotationDefinitionShouldBeFound(String filter) throws Exception {
        restQuotationDefinitionMockMvc.perform(get("/api/quotation-definitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationDefinition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].validityTerms").value(hasItem(DEFAULT_VALIDITY_TERMS.toString())))
            .andExpect(jsonPath("$.[*].acceptation").value(hasItem(DEFAULT_ACCEPTATION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuotationDefinitionShouldNotBeFound(String filter) throws Exception {
        restQuotationDefinitionMockMvc.perform(get("/api/quotation-definitions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingQuotationDefinition() throws Exception {
        // Get the quotationDefinition
        restQuotationDefinitionMockMvc.perform(get("/api/quotation-definitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotationDefinition() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);
        int databaseSizeBeforeUpdate = quotationDefinitionRepository.findAll().size();

        // Update the quotationDefinition
        QuotationDefinition updatedQuotationDefinition = quotationDefinitionRepository.findOne(quotationDefinition.getId());
        // Disconnect from session so that the updates on updatedQuotationDefinition are not directly saved in db
        em.detach(updatedQuotationDefinition);
        updatedQuotationDefinition
            .description(UPDATED_DESCRIPTION)
            .docType(UPDATED_DOC_TYPE)
            .type(UPDATED_TYPE)
            .vatRate(UPDATED_VAT_RATE)
            .validityTerms(UPDATED_VALIDITY_TERMS)
            .acceptation(UPDATED_ACCEPTATION);
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(updatedQuotationDefinition);

        restQuotationDefinitionMockMvc.perform(put("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isOk());

        // Validate the QuotationDefinition in the database
        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeUpdate);
        QuotationDefinition testQuotationDefinition = quotationDefinitionList.get(quotationDefinitionList.size() - 1);
        assertThat(testQuotationDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuotationDefinition.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testQuotationDefinition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuotationDefinition.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testQuotationDefinition.getValidityTerms()).isEqualTo(UPDATED_VALIDITY_TERMS);
        assertThat(testQuotationDefinition.getAcceptation()).isEqualTo(UPDATED_ACCEPTATION);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotationDefinition() throws Exception {
        int databaseSizeBeforeUpdate = quotationDefinitionRepository.findAll().size();

        // Create the QuotationDefinition
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionMapper.toDto(quotationDefinition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuotationDefinitionMockMvc.perform(put("/api/quotation-definitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDefinitionDTO)))
            .andExpect(status().isCreated());

        // Validate the QuotationDefinition in the database
        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuotationDefinition() throws Exception {
        // Initialize the database
        quotationDefinitionRepository.saveAndFlush(quotationDefinition);
        int databaseSizeBeforeDelete = quotationDefinitionRepository.findAll().size();

        // Get the quotationDefinition
        restQuotationDefinitionMockMvc.perform(delete("/api/quotation-definitions/{id}", quotationDefinition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuotationDefinition> quotationDefinitionList = quotationDefinitionRepository.findAll();
        assertThat(quotationDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationDefinition.class);
        QuotationDefinition quotationDefinition1 = new QuotationDefinition();
        quotationDefinition1.setId(1L);
        QuotationDefinition quotationDefinition2 = new QuotationDefinition();
        quotationDefinition2.setId(quotationDefinition1.getId());
        assertThat(quotationDefinition1).isEqualTo(quotationDefinition2);
        quotationDefinition2.setId(2L);
        assertThat(quotationDefinition1).isNotEqualTo(quotationDefinition2);
        quotationDefinition1.setId(null);
        assertThat(quotationDefinition1).isNotEqualTo(quotationDefinition2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationDefinitionDTO.class);
        QuotationDefinitionDTO quotationDefinitionDTO1 = new QuotationDefinitionDTO();
        quotationDefinitionDTO1.setId(1L);
        QuotationDefinitionDTO quotationDefinitionDTO2 = new QuotationDefinitionDTO();
        assertThat(quotationDefinitionDTO1).isNotEqualTo(quotationDefinitionDTO2);
        quotationDefinitionDTO2.setId(quotationDefinitionDTO1.getId());
        assertThat(quotationDefinitionDTO1).isEqualTo(quotationDefinitionDTO2);
        quotationDefinitionDTO2.setId(2L);
        assertThat(quotationDefinitionDTO1).isNotEqualTo(quotationDefinitionDTO2);
        quotationDefinitionDTO1.setId(null);
        assertThat(quotationDefinitionDTO1).isNotEqualTo(quotationDefinitionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(quotationDefinitionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(quotationDefinitionMapper.fromId(null)).isNull();
    }
}
