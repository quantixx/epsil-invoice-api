package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Quotation;
import io.epsil.invoice.domain.Family;
import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.repository.QuotationRepository;
import io.epsil.invoice.service.QuotationService;
import io.epsil.invoice.service.dto.QuotationDTO;
import io.epsil.invoice.service.mapper.QuotationMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.QuotationCriteria;
import io.epsil.invoice.service.QuotationQueryService;

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
 * Test class for the QuotationResource REST controller.
 *
 * @see QuotationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class QuotationResourceIntTest {

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

    private static final String DEFAULT_ACCEPTIONATION = "AAAAAAAAAA";
    private static final String UPDATED_ACCEPTIONATION = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final Float DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT = 1F;
    private static final Float UPDATED_SUB_TOTAL_BEFORE_DISCOUNT = 2F;

    private static final Float DEFAULT_DISCOUNT_RATE = 1F;
    private static final Float UPDATED_DISCOUNT_RATE = 2F;

    private static final Float DEFAULT_DISCOUNT_AMOUNT = 1F;
    private static final Float UPDATED_DISCOUNT_AMOUNT = 2F;

    private static final Float DEFAULT_SUB_TOTAL = 1F;
    private static final Float UPDATED_SUB_TOTAL = 2F;

    private static final Float DEFAULT_VAT_AMOUNT = 1F;
    private static final Float UPDATED_VAT_AMOUNT = 2F;

    private static final Float DEFAULT_TOTAL = 1F;
    private static final Float UPDATED_TOTAL = 2F;

    private static final String DEFAULT_ADDITIONAL_INFORMATION = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFORMATION = "BBBBBBBBBB";

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private QuotationQueryService quotationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuotationMockMvc;

    private Quotation quotation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuotationResource quotationResource = new QuotationResource(quotationService, quotationQueryService);
        this.restQuotationMockMvc = MockMvcBuilders.standaloneSetup(quotationResource)
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
    public static Quotation createEntity(EntityManager em) {
        Quotation quotation = new Quotation()
            .description(DEFAULT_DESCRIPTION)
            .docType(DEFAULT_DOC_TYPE)
            .type(DEFAULT_TYPE)
            .vatRate(DEFAULT_VAT_RATE)
            .validityTerms(DEFAULT_VALIDITY_TERMS)
            .acceptionation(DEFAULT_ACCEPTIONATION)
            .title(DEFAULT_TITLE)
            .number(DEFAULT_NUMBER)
            .subTotalBeforeDiscount(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .subTotal(DEFAULT_SUB_TOTAL)
            .vatAmount(DEFAULT_VAT_AMOUNT)
            .total(DEFAULT_TOTAL)
            .additionalInformation(DEFAULT_ADDITIONAL_INFORMATION);
        // Add required entity
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        quotation.setFamily(family);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        quotation.setTenant(tenant);
        return quotation;
    }

    @Before
    public void initTest() {
        quotation = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotation() throws Exception {
        int databaseSizeBeforeCreate = quotationRepository.findAll().size();

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);
        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isCreated());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeCreate + 1);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuotation.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testQuotation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testQuotation.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testQuotation.getValidityTerms()).isEqualTo(DEFAULT_VALIDITY_TERMS);
        assertThat(testQuotation.getAcceptionation()).isEqualTo(DEFAULT_ACCEPTIONATION);
        assertThat(testQuotation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuotation.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testQuotation.getSubTotalBeforeDiscount()).isEqualTo(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT);
        assertThat(testQuotation.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testQuotation.getDiscountAmount()).isEqualTo(DEFAULT_DISCOUNT_AMOUNT);
        assertThat(testQuotation.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testQuotation.getVatAmount()).isEqualTo(DEFAULT_VAT_AMOUNT);
        assertThat(testQuotation.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testQuotation.getAdditionalInformation()).isEqualTo(DEFAULT_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void createQuotationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationRepository.findAll().size();

        // Create the Quotation with an existing ID
        quotation.setId(1L);
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setDocType(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setType(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setVatRate(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidityTermsIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setValidityTerms(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setNumber(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setSubTotal(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setVatAmount(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationRepository.findAll().size();
        // set the field null
        quotation.setTotal(null);

        // Create the Quotation, which fails.
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        restQuotationMockMvc.perform(post("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isBadRequest());

        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotations() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].validityTerms").value(hasItem(DEFAULT_VALIDITY_TERMS.toString())))
            .andExpect(jsonPath("$.[*].acceptionation").value(hasItem(DEFAULT_ACCEPTIONATION.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].subTotalBeforeDiscount").value(hasItem(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatAmount").value(hasItem(DEFAULT_VAT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalInformation").value(hasItem(DEFAULT_ADDITIONAL_INFORMATION.toString())));
    }

    @Test
    @Transactional
    public void getQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get the quotation
        restQuotationMockMvc.perform(get("/api/quotations/{id}", quotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotation.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.validityTerms").value(DEFAULT_VALIDITY_TERMS.toString()))
            .andExpect(jsonPath("$.acceptionation").value(DEFAULT_ACCEPTIONATION.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.subTotalBeforeDiscount").value(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.discountAmount").value(DEFAULT_DISCOUNT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.vatAmount").value(DEFAULT_VAT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.additionalInformation").value(DEFAULT_ADDITIONAL_INFORMATION.toString()));
    }

    @Test
    @Transactional
    public void getAllQuotationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where description equals to DEFAULT_DESCRIPTION
        defaultQuotationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the quotationList where description equals to UPDATED_DESCRIPTION
        defaultQuotationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQuotationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the quotationList where description equals to UPDATED_DESCRIPTION
        defaultQuotationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where description is not null
        defaultQuotationShouldBeFound("description.specified=true");

        // Get all the quotationList where description is null
        defaultQuotationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where docType equals to DEFAULT_DOC_TYPE
        defaultQuotationShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the quotationList where docType equals to UPDATED_DOC_TYPE
        defaultQuotationShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultQuotationShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the quotationList where docType equals to UPDATED_DOC_TYPE
        defaultQuotationShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where docType is not null
        defaultQuotationShouldBeFound("docType.specified=true");

        // Get all the quotationList where docType is null
        defaultQuotationShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where type equals to DEFAULT_TYPE
        defaultQuotationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the quotationList where type equals to UPDATED_TYPE
        defaultQuotationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultQuotationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the quotationList where type equals to UPDATED_TYPE
        defaultQuotationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where type is not null
        defaultQuotationShouldBeFound("type.specified=true");

        // Get all the quotationList where type is null
        defaultQuotationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByVatRateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where vatRate equals to DEFAULT_VAT_RATE
        defaultQuotationShouldBeFound("vatRate.equals=" + DEFAULT_VAT_RATE);

        // Get all the quotationList where vatRate equals to UPDATED_VAT_RATE
        defaultQuotationShouldNotBeFound("vatRate.equals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByVatRateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where vatRate in DEFAULT_VAT_RATE or UPDATED_VAT_RATE
        defaultQuotationShouldBeFound("vatRate.in=" + DEFAULT_VAT_RATE + "," + UPDATED_VAT_RATE);

        // Get all the quotationList where vatRate equals to UPDATED_VAT_RATE
        defaultQuotationShouldNotBeFound("vatRate.in=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByVatRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where vatRate is not null
        defaultQuotationShouldBeFound("vatRate.specified=true");

        // Get all the quotationList where vatRate is null
        defaultQuotationShouldNotBeFound("vatRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByValidityTermsIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validityTerms equals to DEFAULT_VALIDITY_TERMS
        defaultQuotationShouldBeFound("validityTerms.equals=" + DEFAULT_VALIDITY_TERMS);

        // Get all the quotationList where validityTerms equals to UPDATED_VALIDITY_TERMS
        defaultQuotationShouldNotBeFound("validityTerms.equals=" + UPDATED_VALIDITY_TERMS);
    }

    @Test
    @Transactional
    public void getAllQuotationsByValidityTermsIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validityTerms in DEFAULT_VALIDITY_TERMS or UPDATED_VALIDITY_TERMS
        defaultQuotationShouldBeFound("validityTerms.in=" + DEFAULT_VALIDITY_TERMS + "," + UPDATED_VALIDITY_TERMS);

        // Get all the quotationList where validityTerms equals to UPDATED_VALIDITY_TERMS
        defaultQuotationShouldNotBeFound("validityTerms.in=" + UPDATED_VALIDITY_TERMS);
    }

    @Test
    @Transactional
    public void getAllQuotationsByValidityTermsIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where validityTerms is not null
        defaultQuotationShouldBeFound("validityTerms.specified=true");

        // Get all the quotationList where validityTerms is null
        defaultQuotationShouldNotBeFound("validityTerms.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByAcceptionationIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where acceptionation equals to DEFAULT_ACCEPTIONATION
        defaultQuotationShouldBeFound("acceptionation.equals=" + DEFAULT_ACCEPTIONATION);

        // Get all the quotationList where acceptionation equals to UPDATED_ACCEPTIONATION
        defaultQuotationShouldNotBeFound("acceptionation.equals=" + UPDATED_ACCEPTIONATION);
    }

    @Test
    @Transactional
    public void getAllQuotationsByAcceptionationIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where acceptionation in DEFAULT_ACCEPTIONATION or UPDATED_ACCEPTIONATION
        defaultQuotationShouldBeFound("acceptionation.in=" + DEFAULT_ACCEPTIONATION + "," + UPDATED_ACCEPTIONATION);

        // Get all the quotationList where acceptionation equals to UPDATED_ACCEPTIONATION
        defaultQuotationShouldNotBeFound("acceptionation.in=" + UPDATED_ACCEPTIONATION);
    }

    @Test
    @Transactional
    public void getAllQuotationsByAcceptionationIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where acceptionation is not null
        defaultQuotationShouldBeFound("acceptionation.specified=true");

        // Get all the quotationList where acceptionation is null
        defaultQuotationShouldNotBeFound("acceptionation.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title equals to DEFAULT_TITLE
        defaultQuotationShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the quotationList where title equals to UPDATED_TITLE
        defaultQuotationShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultQuotationShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the quotationList where title equals to UPDATED_TITLE
        defaultQuotationShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where title is not null
        defaultQuotationShouldBeFound("title.specified=true");

        // Get all the quotationList where title is null
        defaultQuotationShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where number equals to DEFAULT_NUMBER
        defaultQuotationShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the quotationList where number equals to UPDATED_NUMBER
        defaultQuotationShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultQuotationShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the quotationList where number equals to UPDATED_NUMBER
        defaultQuotationShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllQuotationsByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where number is not null
        defaultQuotationShouldBeFound("number.specified=true");

        // Get all the quotationList where number is null
        defaultQuotationShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsBySubTotalBeforeDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotalBeforeDiscount equals to DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT
        defaultQuotationShouldBeFound("subTotalBeforeDiscount.equals=" + DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT);

        // Get all the quotationList where subTotalBeforeDiscount equals to UPDATED_SUB_TOTAL_BEFORE_DISCOUNT
        defaultQuotationShouldNotBeFound("subTotalBeforeDiscount.equals=" + UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsBySubTotalBeforeDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotalBeforeDiscount in DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT or UPDATED_SUB_TOTAL_BEFORE_DISCOUNT
        defaultQuotationShouldBeFound("subTotalBeforeDiscount.in=" + DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT + "," + UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);

        // Get all the quotationList where subTotalBeforeDiscount equals to UPDATED_SUB_TOTAL_BEFORE_DISCOUNT
        defaultQuotationShouldNotBeFound("subTotalBeforeDiscount.in=" + UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsBySubTotalBeforeDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotalBeforeDiscount is not null
        defaultQuotationShouldBeFound("subTotalBeforeDiscount.specified=true");

        // Get all the quotationList where subTotalBeforeDiscount is null
        defaultQuotationShouldNotBeFound("subTotalBeforeDiscount.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultQuotationShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the quotationList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultQuotationShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultQuotationShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the quotationList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultQuotationShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountRate is not null
        defaultQuotationShouldBeFound("discountRate.specified=true");

        // Get all the quotationList where discountRate is null
        defaultQuotationShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountAmount equals to DEFAULT_DISCOUNT_AMOUNT
        defaultQuotationShouldBeFound("discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the quotationList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultQuotationShouldNotBeFound("discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountAmount in DEFAULT_DISCOUNT_AMOUNT or UPDATED_DISCOUNT_AMOUNT
        defaultQuotationShouldBeFound("discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT);

        // Get all the quotationList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultQuotationShouldNotBeFound("discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where discountAmount is not null
        defaultQuotationShouldBeFound("discountAmount.specified=true");

        // Get all the quotationList where discountAmount is null
        defaultQuotationShouldNotBeFound("discountAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationList where subTotal equals to UPDATED_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllQuotationsBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultQuotationShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the quotationList where subTotal equals to UPDATED_SUB_TOTAL
        defaultQuotationShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllQuotationsBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where subTotal is not null
        defaultQuotationShouldBeFound("subTotal.specified=true");

        // Get all the quotationList where subTotal is null
        defaultQuotationShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByVatAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where vatAmount equals to DEFAULT_VAT_AMOUNT
        defaultQuotationShouldBeFound("vatAmount.equals=" + DEFAULT_VAT_AMOUNT);

        // Get all the quotationList where vatAmount equals to UPDATED_VAT_AMOUNT
        defaultQuotationShouldNotBeFound("vatAmount.equals=" + UPDATED_VAT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByVatAmountIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where vatAmount in DEFAULT_VAT_AMOUNT or UPDATED_VAT_AMOUNT
        defaultQuotationShouldBeFound("vatAmount.in=" + DEFAULT_VAT_AMOUNT + "," + UPDATED_VAT_AMOUNT);

        // Get all the quotationList where vatAmount equals to UPDATED_VAT_AMOUNT
        defaultQuotationShouldNotBeFound("vatAmount.in=" + UPDATED_VAT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllQuotationsByVatAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where vatAmount is not null
        defaultQuotationShouldBeFound("vatAmount.specified=true");

        // Get all the quotationList where vatAmount is null
        defaultQuotationShouldNotBeFound("vatAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where total equals to DEFAULT_TOTAL
        defaultQuotationShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the quotationList where total equals to UPDATED_TOTAL
        defaultQuotationShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllQuotationsByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultQuotationShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the quotationList where total equals to UPDATED_TOTAL
        defaultQuotationShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllQuotationsByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where total is not null
        defaultQuotationShouldBeFound("total.specified=true");

        // Get all the quotationList where total is null
        defaultQuotationShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByAdditionalInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where additionalInformation equals to DEFAULT_ADDITIONAL_INFORMATION
        defaultQuotationShouldBeFound("additionalInformation.equals=" + DEFAULT_ADDITIONAL_INFORMATION);

        // Get all the quotationList where additionalInformation equals to UPDATED_ADDITIONAL_INFORMATION
        defaultQuotationShouldNotBeFound("additionalInformation.equals=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllQuotationsByAdditionalInformationIsInShouldWork() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where additionalInformation in DEFAULT_ADDITIONAL_INFORMATION or UPDATED_ADDITIONAL_INFORMATION
        defaultQuotationShouldBeFound("additionalInformation.in=" + DEFAULT_ADDITIONAL_INFORMATION + "," + UPDATED_ADDITIONAL_INFORMATION);

        // Get all the quotationList where additionalInformation equals to UPDATED_ADDITIONAL_INFORMATION
        defaultQuotationShouldNotBeFound("additionalInformation.in=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllQuotationsByAdditionalInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);

        // Get all the quotationList where additionalInformation is not null
        defaultQuotationShouldBeFound("additionalInformation.specified=true");

        // Get all the quotationList where additionalInformation is null
        defaultQuotationShouldNotBeFound("additionalInformation.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationsByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        quotation.setFamily(family);
        quotationRepository.saveAndFlush(quotation);
        Long familyId = family.getId();

        // Get all the quotationList where family equals to familyId
        defaultQuotationShouldBeFound("familyId.equals=" + familyId);

        // Get all the quotationList where family equals to familyId + 1
        defaultQuotationShouldNotBeFound("familyId.equals=" + (familyId + 1));
    }


    @Test
    @Transactional
    public void getAllQuotationsByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        quotation.setInvoice(invoice);
        invoice.setQuotation(quotation);
        quotationRepository.saveAndFlush(quotation);
        Long invoiceId = invoice.getId();

        // Get all the quotationList where invoice equals to invoiceId
        defaultQuotationShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the quotationList where invoice equals to invoiceId + 1
        defaultQuotationShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }


    @Test
    @Transactional
    public void getAllQuotationsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        quotation.setTenant(tenant);
        quotationRepository.saveAndFlush(quotation);
        Long tenantId = tenant.getId();

        // Get all the quotationList where tenant equals to tenantId
        defaultQuotationShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the quotationList where tenant equals to tenantId + 1
        defaultQuotationShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuotationShouldBeFound(String filter) throws Exception {
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].validityTerms").value(hasItem(DEFAULT_VALIDITY_TERMS.toString())))
            .andExpect(jsonPath("$.[*].acceptionation").value(hasItem(DEFAULT_ACCEPTIONATION.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].subTotalBeforeDiscount").value(hasItem(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].discountAmount").value(hasItem(DEFAULT_DISCOUNT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].vatAmount").value(hasItem(DEFAULT_VAT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].additionalInformation").value(hasItem(DEFAULT_ADDITIONAL_INFORMATION.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuotationShouldNotBeFound(String filter) throws Exception {
        restQuotationMockMvc.perform(get("/api/quotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingQuotation() throws Exception {
        // Get the quotation
        restQuotationMockMvc.perform(get("/api/quotations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Update the quotation
        Quotation updatedQuotation = quotationRepository.findOne(quotation.getId());
        // Disconnect from session so that the updates on updatedQuotation are not directly saved in db
        em.detach(updatedQuotation);
        updatedQuotation
            .description(UPDATED_DESCRIPTION)
            .docType(UPDATED_DOC_TYPE)
            .type(UPDATED_TYPE)
            .vatRate(UPDATED_VAT_RATE)
            .validityTerms(UPDATED_VALIDITY_TERMS)
            .acceptionation(UPDATED_ACCEPTIONATION)
            .title(UPDATED_TITLE)
            .number(UPDATED_NUMBER)
            .subTotalBeforeDiscount(UPDATED_SUB_TOTAL_BEFORE_DISCOUNT)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .subTotal(UPDATED_SUB_TOTAL)
            .vatAmount(UPDATED_VAT_AMOUNT)
            .total(UPDATED_TOTAL)
            .additionalInformation(UPDATED_ADDITIONAL_INFORMATION);
        QuotationDTO quotationDTO = quotationMapper.toDto(updatedQuotation);

        restQuotationMockMvc.perform(put("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isOk());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate);
        Quotation testQuotation = quotationList.get(quotationList.size() - 1);
        assertThat(testQuotation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuotation.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testQuotation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testQuotation.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testQuotation.getValidityTerms()).isEqualTo(UPDATED_VALIDITY_TERMS);
        assertThat(testQuotation.getAcceptionation()).isEqualTo(UPDATED_ACCEPTIONATION);
        assertThat(testQuotation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuotation.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testQuotation.getSubTotalBeforeDiscount()).isEqualTo(UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);
        assertThat(testQuotation.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testQuotation.getDiscountAmount()).isEqualTo(UPDATED_DISCOUNT_AMOUNT);
        assertThat(testQuotation.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testQuotation.getVatAmount()).isEqualTo(UPDATED_VAT_AMOUNT);
        assertThat(testQuotation.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testQuotation.getAdditionalInformation()).isEqualTo(UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotation() throws Exception {
        int databaseSizeBeforeUpdate = quotationRepository.findAll().size();

        // Create the Quotation
        QuotationDTO quotationDTO = quotationMapper.toDto(quotation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuotationMockMvc.perform(put("/api/quotations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationDTO)))
            .andExpect(status().isCreated());

        // Validate the Quotation in the database
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuotation() throws Exception {
        // Initialize the database
        quotationRepository.saveAndFlush(quotation);
        int databaseSizeBeforeDelete = quotationRepository.findAll().size();

        // Get the quotation
        restQuotationMockMvc.perform(delete("/api/quotations/{id}", quotation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Quotation> quotationList = quotationRepository.findAll();
        assertThat(quotationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quotation.class);
        Quotation quotation1 = new Quotation();
        quotation1.setId(1L);
        Quotation quotation2 = new Quotation();
        quotation2.setId(quotation1.getId());
        assertThat(quotation1).isEqualTo(quotation2);
        quotation2.setId(2L);
        assertThat(quotation1).isNotEqualTo(quotation2);
        quotation1.setId(null);
        assertThat(quotation1).isNotEqualTo(quotation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationDTO.class);
        QuotationDTO quotationDTO1 = new QuotationDTO();
        quotationDTO1.setId(1L);
        QuotationDTO quotationDTO2 = new QuotationDTO();
        assertThat(quotationDTO1).isNotEqualTo(quotationDTO2);
        quotationDTO2.setId(quotationDTO1.getId());
        assertThat(quotationDTO1).isEqualTo(quotationDTO2);
        quotationDTO2.setId(2L);
        assertThat(quotationDTO1).isNotEqualTo(quotationDTO2);
        quotationDTO1.setId(null);
        assertThat(quotationDTO1).isNotEqualTo(quotationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(quotationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(quotationMapper.fromId(null)).isNull();
    }
}
