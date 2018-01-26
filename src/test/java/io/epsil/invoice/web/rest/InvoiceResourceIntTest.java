package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.domain.Organisation;
import io.epsil.invoice.domain.Quotation;
import io.epsil.invoice.domain.Document;
import io.epsil.invoice.domain.InvoiceState;
import io.epsil.invoice.domain.InvoiceLine;
import io.epsil.invoice.domain.Language;
import io.epsil.invoice.domain.Currency;
import io.epsil.invoice.domain.Family;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.repository.InvoiceRepository;
import io.epsil.invoice.service.InvoiceService;
import io.epsil.invoice.service.dto.InvoiceDTO;
import io.epsil.invoice.service.mapper.InvoiceMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.InvoiceCriteria;
import io.epsil.invoice.service.InvoiceQueryService;

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
import io.epsil.invoice.domain.enumeration.InvoiceType;
/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class InvoiceResourceIntTest {

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

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final InvoiceType DEFAULT_INVOICE_TYPE = InvoiceType.INVOICE;
    private static final InvoiceType UPDATED_INVOICE_TYPE = InvoiceType.CREDIT_INVOICE;

    private static final String DEFAULT_PO_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PO_NUMBER = "BBBBBBBBBB";

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
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceQueryService invoiceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceResource invoiceResource = new InvoiceResource(invoiceService, invoiceQueryService);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
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
    public static Invoice createEntity(EntityManager em) {
        Invoice invoice = new Invoice()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .docType(DEFAULT_DOC_TYPE)
            .vatRate(DEFAULT_VAT_RATE)
            .terms(DEFAULT_TERMS)
            .penalties(DEFAULT_PENALTIES)
            .number(DEFAULT_NUMBER)
            .invoiceType(DEFAULT_INVOICE_TYPE)
            .poNumber(DEFAULT_PO_NUMBER)
            .subTotalBeforeDiscount(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .discountAmount(DEFAULT_DISCOUNT_AMOUNT)
            .subTotal(DEFAULT_SUB_TOTAL)
            .vatAmount(DEFAULT_VAT_AMOUNT)
            .total(DEFAULT_TOTAL)
            .additionalInformation(DEFAULT_ADDITIONAL_INFORMATION);
        // Add required entity
        Organisation organisation = OrganisationResourceIntTest.createEntity(em);
        em.persist(organisation);
        em.flush();
        invoice.setOrganisation(organisation);
        // Add required entity
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        invoice.setLanguage(language);
        // Add required entity
        Currency currency = CurrencyResourceIntTest.createEntity(em);
        em.persist(currency);
        em.flush();
        invoice.setCurrency(currency);
        // Add required entity
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        invoice.setFamily(family);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        invoice.setTenant(tenant);
        return invoice;
    }

    @Before
    public void initTest() {
        invoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInvoice.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoice.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testInvoice.getVatRate()).isEqualTo(DEFAULT_VAT_RATE);
        assertThat(testInvoice.getTerms()).isEqualTo(DEFAULT_TERMS);
        assertThat(testInvoice.getPenalties()).isEqualTo(DEFAULT_PENALTIES);
        assertThat(testInvoice.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testInvoice.getInvoiceType()).isEqualTo(DEFAULT_INVOICE_TYPE);
        assertThat(testInvoice.getPoNumber()).isEqualTo(DEFAULT_PO_NUMBER);
        assertThat(testInvoice.getSubTotalBeforeDiscount()).isEqualTo(DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT);
        assertThat(testInvoice.getDiscountRate()).isEqualTo(DEFAULT_DISCOUNT_RATE);
        assertThat(testInvoice.getDiscountAmount()).isEqualTo(DEFAULT_DISCOUNT_AMOUNT);
        assertThat(testInvoice.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
        assertThat(testInvoice.getVatAmount()).isEqualTo(DEFAULT_VAT_AMOUNT);
        assertThat(testInvoice.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testInvoice.getAdditionalInformation()).isEqualTo(DEFAULT_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void createInvoiceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice with an existing ID
        invoice.setId(1L);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setDocType(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setVatRate(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTermsIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTerms(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPenaltiesIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setPenalties(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setNumber(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvoiceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceType(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setSubTotal(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVatAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setVatAmount(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTotal(null);

        // Create the Invoice, which fails.
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        restInvoiceMockMvc.perform(post("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].terms").value(hasItem(DEFAULT_TERMS.toString())))
            .andExpect(jsonPath("$.[*].penalties").value(hasItem(DEFAULT_PENALTIES.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].invoiceType").value(hasItem(DEFAULT_INVOICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER.toString())))
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
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.vatRate").value(DEFAULT_VAT_RATE.doubleValue()))
            .andExpect(jsonPath("$.terms").value(DEFAULT_TERMS.toString()))
            .andExpect(jsonPath("$.penalties").value(DEFAULT_PENALTIES.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.invoiceType").value(DEFAULT_INVOICE_TYPE.toString()))
            .andExpect(jsonPath("$.poNumber").value(DEFAULT_PO_NUMBER.toString()))
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
    public void getAllInvoicesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where title equals to DEFAULT_TITLE
        defaultInvoiceShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the invoiceList where title equals to UPDATED_TITLE
        defaultInvoiceShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultInvoiceShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the invoiceList where title equals to UPDATED_TITLE
        defaultInvoiceShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where title is not null
        defaultInvoiceShouldBeFound("title.specified=true");

        // Get all the invoiceList where title is null
        defaultInvoiceShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where description equals to DEFAULT_DESCRIPTION
        defaultInvoiceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the invoiceList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInvoiceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the invoiceList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where description is not null
        defaultInvoiceShouldBeFound("description.specified=true");

        // Get all the invoiceList where description is null
        defaultInvoiceShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where docType equals to DEFAULT_DOC_TYPE
        defaultInvoiceShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the invoiceList where docType equals to UPDATED_DOC_TYPE
        defaultInvoiceShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultInvoiceShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the invoiceList where docType equals to UPDATED_DOC_TYPE
        defaultInvoiceShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where docType is not null
        defaultInvoiceShouldBeFound("docType.specified=true");

        // Get all the invoiceList where docType is null
        defaultInvoiceShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByVatRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatRate equals to DEFAULT_VAT_RATE
        defaultInvoiceShouldBeFound("vatRate.equals=" + DEFAULT_VAT_RATE);

        // Get all the invoiceList where vatRate equals to UPDATED_VAT_RATE
        defaultInvoiceShouldNotBeFound("vatRate.equals=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByVatRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatRate in DEFAULT_VAT_RATE or UPDATED_VAT_RATE
        defaultInvoiceShouldBeFound("vatRate.in=" + DEFAULT_VAT_RATE + "," + UPDATED_VAT_RATE);

        // Get all the invoiceList where vatRate equals to UPDATED_VAT_RATE
        defaultInvoiceShouldNotBeFound("vatRate.in=" + UPDATED_VAT_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByVatRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatRate is not null
        defaultInvoiceShouldBeFound("vatRate.specified=true");

        // Get all the invoiceList where vatRate is null
        defaultInvoiceShouldNotBeFound("vatRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByTermsIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where terms equals to DEFAULT_TERMS
        defaultInvoiceShouldBeFound("terms.equals=" + DEFAULT_TERMS);

        // Get all the invoiceList where terms equals to UPDATED_TERMS
        defaultInvoiceShouldNotBeFound("terms.equals=" + UPDATED_TERMS);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTermsIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where terms in DEFAULT_TERMS or UPDATED_TERMS
        defaultInvoiceShouldBeFound("terms.in=" + DEFAULT_TERMS + "," + UPDATED_TERMS);

        // Get all the invoiceList where terms equals to UPDATED_TERMS
        defaultInvoiceShouldNotBeFound("terms.in=" + UPDATED_TERMS);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTermsIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where terms is not null
        defaultInvoiceShouldBeFound("terms.specified=true");

        // Get all the invoiceList where terms is null
        defaultInvoiceShouldNotBeFound("terms.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByPenaltiesIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where penalties equals to DEFAULT_PENALTIES
        defaultInvoiceShouldBeFound("penalties.equals=" + DEFAULT_PENALTIES);

        // Get all the invoiceList where penalties equals to UPDATED_PENALTIES
        defaultInvoiceShouldNotBeFound("penalties.equals=" + UPDATED_PENALTIES);
    }

    @Test
    @Transactional
    public void getAllInvoicesByPenaltiesIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where penalties in DEFAULT_PENALTIES or UPDATED_PENALTIES
        defaultInvoiceShouldBeFound("penalties.in=" + DEFAULT_PENALTIES + "," + UPDATED_PENALTIES);

        // Get all the invoiceList where penalties equals to UPDATED_PENALTIES
        defaultInvoiceShouldNotBeFound("penalties.in=" + UPDATED_PENALTIES);
    }

    @Test
    @Transactional
    public void getAllInvoicesByPenaltiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where penalties is not null
        defaultInvoiceShouldBeFound("penalties.specified=true");

        // Get all the invoiceList where penalties is null
        defaultInvoiceShouldNotBeFound("penalties.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number equals to DEFAULT_NUMBER
        defaultInvoiceShouldBeFound("number.equals=" + DEFAULT_NUMBER);

        // Get all the invoiceList where number equals to UPDATED_NUMBER
        defaultInvoiceShouldNotBeFound("number.equals=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number in DEFAULT_NUMBER or UPDATED_NUMBER
        defaultInvoiceShouldBeFound("number.in=" + DEFAULT_NUMBER + "," + UPDATED_NUMBER);

        // Get all the invoiceList where number equals to UPDATED_NUMBER
        defaultInvoiceShouldNotBeFound("number.in=" + UPDATED_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where number is not null
        defaultInvoiceShouldBeFound("number.specified=true");

        // Get all the invoiceList where number is null
        defaultInvoiceShouldNotBeFound("number.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceType equals to DEFAULT_INVOICE_TYPE
        defaultInvoiceShouldBeFound("invoiceType.equals=" + DEFAULT_INVOICE_TYPE);

        // Get all the invoiceList where invoiceType equals to UPDATED_INVOICE_TYPE
        defaultInvoiceShouldNotBeFound("invoiceType.equals=" + UPDATED_INVOICE_TYPE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceType in DEFAULT_INVOICE_TYPE or UPDATED_INVOICE_TYPE
        defaultInvoiceShouldBeFound("invoiceType.in=" + DEFAULT_INVOICE_TYPE + "," + UPDATED_INVOICE_TYPE);

        // Get all the invoiceList where invoiceType equals to UPDATED_INVOICE_TYPE
        defaultInvoiceShouldNotBeFound("invoiceType.in=" + UPDATED_INVOICE_TYPE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByInvoiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where invoiceType is not null
        defaultInvoiceShouldBeFound("invoiceType.specified=true");

        // Get all the invoiceList where invoiceType is null
        defaultInvoiceShouldNotBeFound("invoiceType.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByPoNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where poNumber equals to DEFAULT_PO_NUMBER
        defaultInvoiceShouldBeFound("poNumber.equals=" + DEFAULT_PO_NUMBER);

        // Get all the invoiceList where poNumber equals to UPDATED_PO_NUMBER
        defaultInvoiceShouldNotBeFound("poNumber.equals=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByPoNumberIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where poNumber in DEFAULT_PO_NUMBER or UPDATED_PO_NUMBER
        defaultInvoiceShouldBeFound("poNumber.in=" + DEFAULT_PO_NUMBER + "," + UPDATED_PO_NUMBER);

        // Get all the invoiceList where poNumber equals to UPDATED_PO_NUMBER
        defaultInvoiceShouldNotBeFound("poNumber.in=" + UPDATED_PO_NUMBER);
    }

    @Test
    @Transactional
    public void getAllInvoicesByPoNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where poNumber is not null
        defaultInvoiceShouldBeFound("poNumber.specified=true");

        // Get all the invoiceList where poNumber is null
        defaultInvoiceShouldNotBeFound("poNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesBySubTotalBeforeDiscountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotalBeforeDiscount equals to DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT
        defaultInvoiceShouldBeFound("subTotalBeforeDiscount.equals=" + DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT);

        // Get all the invoiceList where subTotalBeforeDiscount equals to UPDATED_SUB_TOTAL_BEFORE_DISCOUNT
        defaultInvoiceShouldNotBeFound("subTotalBeforeDiscount.equals=" + UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesBySubTotalBeforeDiscountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotalBeforeDiscount in DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT or UPDATED_SUB_TOTAL_BEFORE_DISCOUNT
        defaultInvoiceShouldBeFound("subTotalBeforeDiscount.in=" + DEFAULT_SUB_TOTAL_BEFORE_DISCOUNT + "," + UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);

        // Get all the invoiceList where subTotalBeforeDiscount equals to UPDATED_SUB_TOTAL_BEFORE_DISCOUNT
        defaultInvoiceShouldNotBeFound("subTotalBeforeDiscount.in=" + UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesBySubTotalBeforeDiscountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotalBeforeDiscount is not null
        defaultInvoiceShouldBeFound("subTotalBeforeDiscount.specified=true");

        // Get all the invoiceList where subTotalBeforeDiscount is null
        defaultInvoiceShouldNotBeFound("subTotalBeforeDiscount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDiscountRateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate equals to DEFAULT_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.equals=" + DEFAULT_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.equals=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDiscountRateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate in DEFAULT_DISCOUNT_RATE or UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldBeFound("discountRate.in=" + DEFAULT_DISCOUNT_RATE + "," + UPDATED_DISCOUNT_RATE);

        // Get all the invoiceList where discountRate equals to UPDATED_DISCOUNT_RATE
        defaultInvoiceShouldNotBeFound("discountRate.in=" + UPDATED_DISCOUNT_RATE);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDiscountRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountRate is not null
        defaultInvoiceShouldBeFound("discountRate.specified=true");

        // Get all the invoiceList where discountRate is null
        defaultInvoiceShouldNotBeFound("discountRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByDiscountAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountAmount equals to DEFAULT_DISCOUNT_AMOUNT
        defaultInvoiceShouldBeFound("discountAmount.equals=" + DEFAULT_DISCOUNT_AMOUNT);

        // Get all the invoiceList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultInvoiceShouldNotBeFound("discountAmount.equals=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDiscountAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountAmount in DEFAULT_DISCOUNT_AMOUNT or UPDATED_DISCOUNT_AMOUNT
        defaultInvoiceShouldBeFound("discountAmount.in=" + DEFAULT_DISCOUNT_AMOUNT + "," + UPDATED_DISCOUNT_AMOUNT);

        // Get all the invoiceList where discountAmount equals to UPDATED_DISCOUNT_AMOUNT
        defaultInvoiceShouldNotBeFound("discountAmount.in=" + UPDATED_DISCOUNT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByDiscountAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where discountAmount is not null
        defaultInvoiceShouldBeFound("discountAmount.specified=true");

        // Get all the invoiceList where discountAmount is null
        defaultInvoiceShouldNotBeFound("discountAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceList where subTotal equals to UPDATED_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultInvoiceShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the invoiceList where subTotal equals to UPDATED_SUB_TOTAL
        defaultInvoiceShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where subTotal is not null
        defaultInvoiceShouldBeFound("subTotal.specified=true");

        // Get all the invoiceList where subTotal is null
        defaultInvoiceShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByVatAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatAmount equals to DEFAULT_VAT_AMOUNT
        defaultInvoiceShouldBeFound("vatAmount.equals=" + DEFAULT_VAT_AMOUNT);

        // Get all the invoiceList where vatAmount equals to UPDATED_VAT_AMOUNT
        defaultInvoiceShouldNotBeFound("vatAmount.equals=" + UPDATED_VAT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByVatAmountIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatAmount in DEFAULT_VAT_AMOUNT or UPDATED_VAT_AMOUNT
        defaultInvoiceShouldBeFound("vatAmount.in=" + DEFAULT_VAT_AMOUNT + "," + UPDATED_VAT_AMOUNT);

        // Get all the invoiceList where vatAmount equals to UPDATED_VAT_AMOUNT
        defaultInvoiceShouldNotBeFound("vatAmount.in=" + UPDATED_VAT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllInvoicesByVatAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where vatAmount is not null
        defaultInvoiceShouldBeFound("vatAmount.specified=true");

        // Get all the invoiceList where vatAmount is null
        defaultInvoiceShouldNotBeFound("vatAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total equals to DEFAULT_TOTAL
        defaultInvoiceShouldBeFound("total.equals=" + DEFAULT_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total in DEFAULT_TOTAL or UPDATED_TOTAL
        defaultInvoiceShouldBeFound("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL);

        // Get all the invoiceList where total equals to UPDATED_TOTAL
        defaultInvoiceShouldNotBeFound("total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoicesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where total is not null
        defaultInvoiceShouldBeFound("total.specified=true");

        // Get all the invoiceList where total is null
        defaultInvoiceShouldNotBeFound("total.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByAdditionalInformationIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where additionalInformation equals to DEFAULT_ADDITIONAL_INFORMATION
        defaultInvoiceShouldBeFound("additionalInformation.equals=" + DEFAULT_ADDITIONAL_INFORMATION);

        // Get all the invoiceList where additionalInformation equals to UPDATED_ADDITIONAL_INFORMATION
        defaultInvoiceShouldNotBeFound("additionalInformation.equals=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllInvoicesByAdditionalInformationIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where additionalInformation in DEFAULT_ADDITIONAL_INFORMATION or UPDATED_ADDITIONAL_INFORMATION
        defaultInvoiceShouldBeFound("additionalInformation.in=" + DEFAULT_ADDITIONAL_INFORMATION + "," + UPDATED_ADDITIONAL_INFORMATION);

        // Get all the invoiceList where additionalInformation equals to UPDATED_ADDITIONAL_INFORMATION
        defaultInvoiceShouldNotBeFound("additionalInformation.in=" + UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void getAllInvoicesByAdditionalInformationIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoiceList where additionalInformation is not null
        defaultInvoiceShouldBeFound("additionalInformation.specified=true");

        // Get all the invoiceList where additionalInformation is null
        defaultInvoiceShouldNotBeFound("additionalInformation.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoicesByLinkedIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice linked = InvoiceResourceIntTest.createEntity(em);
        em.persist(linked);
        em.flush();
        invoice.setLinked(linked);
        invoiceRepository.saveAndFlush(invoice);
        Long linkedId = linked.getId();

        // Get all the invoiceList where linked equals to linkedId
        defaultInvoiceShouldBeFound("linkedId.equals=" + linkedId);

        // Get all the invoiceList where linked equals to linkedId + 1
        defaultInvoiceShouldNotBeFound("linkedId.equals=" + (linkedId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByOrganisationIsEqualToSomething() throws Exception {
        // Initialize the database
        Organisation organisation = OrganisationResourceIntTest.createEntity(em);
        em.persist(organisation);
        em.flush();
        invoice.setOrganisation(organisation);
        invoiceRepository.saveAndFlush(invoice);
        Long organisationId = organisation.getId();

        // Get all the invoiceList where organisation equals to organisationId
        defaultInvoiceShouldBeFound("organisationId.equals=" + organisationId);

        // Get all the invoiceList where organisation equals to organisationId + 1
        defaultInvoiceShouldNotBeFound("organisationId.equals=" + (organisationId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByQuotationIsEqualToSomething() throws Exception {
        // Initialize the database
        Quotation quotation = QuotationResourceIntTest.createEntity(em);
        em.persist(quotation);
        em.flush();
        invoice.setQuotation(quotation);
        invoiceRepository.saveAndFlush(invoice);
        Long quotationId = quotation.getId();

        // Get all the invoiceList where quotation equals to quotationId
        defaultInvoiceShouldBeFound("quotationId.equals=" + quotationId);

        // Get all the invoiceList where quotation equals to quotationId + 1
        defaultInvoiceShouldNotBeFound("quotationId.equals=" + (quotationId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        Document document = DocumentResourceIntTest.createEntity(em);
        em.persist(document);
        em.flush();
        invoice.setDocument(document);
        invoiceRepository.saveAndFlush(invoice);
        Long documentId = document.getId();

        // Get all the invoiceList where document equals to documentId
        defaultInvoiceShouldBeFound("documentId.equals=" + documentId);

        // Get all the invoiceList where document equals to documentId + 1
        defaultInvoiceShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByStatesIsEqualToSomething() throws Exception {
        // Initialize the database
        InvoiceState states = InvoiceStateResourceIntTest.createEntity(em);
        em.persist(states);
        em.flush();
        invoice.addStates(states);
        invoiceRepository.saveAndFlush(invoice);
        Long statesId = states.getId();

        // Get all the invoiceList where states equals to statesId
        defaultInvoiceShouldBeFound("statesId.equals=" + statesId);

        // Get all the invoiceList where states equals to statesId + 1
        defaultInvoiceShouldNotBeFound("statesId.equals=" + (statesId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByLinesIsEqualToSomething() throws Exception {
        // Initialize the database
        InvoiceLine lines = InvoiceLineResourceIntTest.createEntity(em);
        em.persist(lines);
        em.flush();
        invoice.addLines(lines);
        invoiceRepository.saveAndFlush(invoice);
        Long linesId = lines.getId();

        // Get all the invoiceList where lines equals to linesId
        defaultInvoiceShouldBeFound("linesId.equals=" + linesId);

        // Get all the invoiceList where lines equals to linesId + 1
        defaultInvoiceShouldNotBeFound("linesId.equals=" + (linesId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        invoice.setLanguage(language);
        invoiceRepository.saveAndFlush(invoice);
        Long languageId = language.getId();

        // Get all the invoiceList where language equals to languageId
        defaultInvoiceShouldBeFound("languageId.equals=" + languageId);

        // Get all the invoiceList where language equals to languageId + 1
        defaultInvoiceShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        Currency currency = CurrencyResourceIntTest.createEntity(em);
        em.persist(currency);
        em.flush();
        invoice.setCurrency(currency);
        invoiceRepository.saveAndFlush(invoice);
        Long currencyId = currency.getId();

        // Get all the invoiceList where currency equals to currencyId
        defaultInvoiceShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the invoiceList where currency equals to currencyId + 1
        defaultInvoiceShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        invoice.setFamily(family);
        invoiceRepository.saveAndFlush(invoice);
        Long familyId = family.getId();

        // Get all the invoiceList where family equals to familyId
        defaultInvoiceShouldBeFound("familyId.equals=" + familyId);

        // Get all the invoiceList where family equals to familyId + 1
        defaultInvoiceShouldNotBeFound("familyId.equals=" + (familyId + 1));
    }


    @Test
    @Transactional
    public void getAllInvoicesByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        invoice.setTenant(tenant);
        invoiceRepository.saveAndFlush(invoice);
        Long tenantId = tenant.getId();

        // Get all the invoiceList where tenant equals to tenantId
        defaultInvoiceShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the invoiceList where tenant equals to tenantId + 1
        defaultInvoiceShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceShouldBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vatRate").value(hasItem(DEFAULT_VAT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].terms").value(hasItem(DEFAULT_TERMS.toString())))
            .andExpect(jsonPath("$.[*].penalties").value(hasItem(DEFAULT_PENALTIES.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].invoiceType").value(hasItem(DEFAULT_INVOICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].poNumber").value(hasItem(DEFAULT_PO_NUMBER.toString())))
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
    private void defaultInvoiceShouldNotBeFound(String filter) throws Exception {
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findOne(invoice.getId());
        // Disconnect from session so that the updates on updatedInvoice are not directly saved in db
        em.detach(updatedInvoice);
        updatedInvoice
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .docType(UPDATED_DOC_TYPE)
            .vatRate(UPDATED_VAT_RATE)
            .terms(UPDATED_TERMS)
            .penalties(UPDATED_PENALTIES)
            .number(UPDATED_NUMBER)
            .invoiceType(UPDATED_INVOICE_TYPE)
            .poNumber(UPDATED_PO_NUMBER)
            .subTotalBeforeDiscount(UPDATED_SUB_TOTAL_BEFORE_DISCOUNT)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .discountAmount(UPDATED_DISCOUNT_AMOUNT)
            .subTotal(UPDATED_SUB_TOTAL)
            .vatAmount(UPDATED_VAT_AMOUNT)
            .total(UPDATED_TOTAL)
            .additionalInformation(UPDATED_ADDITIONAL_INFORMATION);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInvoice.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoice.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testInvoice.getVatRate()).isEqualTo(UPDATED_VAT_RATE);
        assertThat(testInvoice.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testInvoice.getPenalties()).isEqualTo(UPDATED_PENALTIES);
        assertThat(testInvoice.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testInvoice.getInvoiceType()).isEqualTo(UPDATED_INVOICE_TYPE);
        assertThat(testInvoice.getPoNumber()).isEqualTo(UPDATED_PO_NUMBER);
        assertThat(testInvoice.getSubTotalBeforeDiscount()).isEqualTo(UPDATED_SUB_TOTAL_BEFORE_DISCOUNT);
        assertThat(testInvoice.getDiscountRate()).isEqualTo(UPDATED_DISCOUNT_RATE);
        assertThat(testInvoice.getDiscountAmount()).isEqualTo(UPDATED_DISCOUNT_AMOUNT);
        assertThat(testInvoice.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
        assertThat(testInvoice.getVatAmount()).isEqualTo(UPDATED_VAT_AMOUNT);
        assertThat(testInvoice.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testInvoice.getAdditionalInformation()).isEqualTo(UPDATED_ADDITIONAL_INFORMATION);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceMockMvc.perform(put("/api/invoices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = new Invoice();
        invoice1.setId(1L);
        Invoice invoice2 = new Invoice();
        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);
        invoice2.setId(2L);
        assertThat(invoice1).isNotEqualTo(invoice2);
        invoice1.setId(null);
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceDTO.class);
        InvoiceDTO invoiceDTO1 = new InvoiceDTO();
        invoiceDTO1.setId(1L);
        InvoiceDTO invoiceDTO2 = new InvoiceDTO();
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO2.setId(invoiceDTO1.getId());
        assertThat(invoiceDTO1).isEqualTo(invoiceDTO2);
        invoiceDTO2.setId(2L);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
        invoiceDTO1.setId(null);
        assertThat(invoiceDTO1).isNotEqualTo(invoiceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceMapper.fromId(null)).isNull();
    }
}
