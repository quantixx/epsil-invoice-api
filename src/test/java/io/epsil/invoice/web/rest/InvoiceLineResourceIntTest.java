package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.InvoiceLine;
import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.repository.InvoiceLineRepository;
import io.epsil.invoice.service.InvoiceLineService;
import io.epsil.invoice.service.dto.InvoiceLineDTO;
import io.epsil.invoice.service.mapper.InvoiceLineMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.InvoiceLineCriteria;
import io.epsil.invoice.service.InvoiceLineQueryService;

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
 * Test class for the InvoiceLineResource REST controller.
 *
 * @see InvoiceLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class InvoiceLineResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Float DEFAULT_UNIT_COST = 1F;
    private static final Float UPDATED_UNIT_COST = 2F;

    private static final Float DEFAULT_SUB_TOTAL = 1F;
    private static final Float UPDATED_SUB_TOTAL = 2F;

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    @Autowired
    private InvoiceLineMapper invoiceLineMapper;

    @Autowired
    private InvoiceLineService invoiceLineService;

    @Autowired
    private InvoiceLineQueryService invoiceLineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceLineMockMvc;

    private InvoiceLine invoiceLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceLineResource invoiceLineResource = new InvoiceLineResource(invoiceLineService, invoiceLineQueryService);
        this.restInvoiceLineMockMvc = MockMvcBuilders.standaloneSetup(invoiceLineResource)
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
    public static InvoiceLine createEntity(EntityManager em) {
        InvoiceLine invoiceLine = new InvoiceLine()
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .unitCost(DEFAULT_UNIT_COST)
            .subTotal(DEFAULT_SUB_TOTAL);
        // Add required entity
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoiceLine.setInvoice(invoice);
        return invoiceLine;
    }

    @Before
    public void initTest() {
        invoiceLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceLine() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineRepository.findAll().size();

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);
        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceLine testInvoiceLine = invoiceLineList.get(invoiceLineList.size() - 1);
        assertThat(testInvoiceLine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceLine.getUnitCost()).isEqualTo(DEFAULT_UNIT_COST);
        assertThat(testInvoiceLine.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void createInvoiceLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineRepository.findAll().size();

        // Create the InvoiceLine with an existing ID
        invoiceLine.setId(1L);
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setDescription(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setQuantity(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setUnitCost(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceLineRepository.findAll().size();
        // set the field null
        invoiceLine.setSubTotal(null);

        // Create the InvoiceLine, which fails.
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        restInvoiceLineMockMvc.perform(post("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceLines() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(DEFAULT_UNIT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines/{id}", invoiceLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceLine.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitCost").value(DEFAULT_UNIT_COST.doubleValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where description equals to DEFAULT_DESCRIPTION
        defaultInvoiceLineShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the invoiceLineList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceLineShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInvoiceLineShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the invoiceLineList where description equals to UPDATED_DESCRIPTION
        defaultInvoiceLineShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where description is not null
        defaultInvoiceLineShouldBeFound("description.specified=true");

        // Get all the invoiceLineList where description is null
        defaultInvoiceLineShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where quantity equals to DEFAULT_QUANTITY
        defaultInvoiceLineShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the invoiceLineList where quantity equals to UPDATED_QUANTITY
        defaultInvoiceLineShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultInvoiceLineShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the invoiceLineList where quantity equals to UPDATED_QUANTITY
        defaultInvoiceLineShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where quantity is not null
        defaultInvoiceLineShouldBeFound("quantity.specified=true");

        // Get all the invoiceLineList where quantity is null
        defaultInvoiceLineShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultInvoiceLineShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the invoiceLineList where quantity greater than or equals to UPDATED_QUANTITY
        defaultInvoiceLineShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where quantity less than or equals to DEFAULT_QUANTITY
        defaultInvoiceLineShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the invoiceLineList where quantity less than or equals to UPDATED_QUANTITY
        defaultInvoiceLineShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllInvoiceLinesByUnitCostIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where unitCost equals to DEFAULT_UNIT_COST
        defaultInvoiceLineShouldBeFound("unitCost.equals=" + DEFAULT_UNIT_COST);

        // Get all the invoiceLineList where unitCost equals to UPDATED_UNIT_COST
        defaultInvoiceLineShouldNotBeFound("unitCost.equals=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByUnitCostIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where unitCost in DEFAULT_UNIT_COST or UPDATED_UNIT_COST
        defaultInvoiceLineShouldBeFound("unitCost.in=" + DEFAULT_UNIT_COST + "," + UPDATED_UNIT_COST);

        // Get all the invoiceLineList where unitCost equals to UPDATED_UNIT_COST
        defaultInvoiceLineShouldNotBeFound("unitCost.in=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByUnitCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where unitCost is not null
        defaultInvoiceLineShouldBeFound("unitCost.specified=true");

        // Get all the invoiceLineList where unitCost is null
        defaultInvoiceLineShouldNotBeFound("unitCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultInvoiceLineShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the invoiceLineList where subTotal equals to UPDATED_SUB_TOTAL
        defaultInvoiceLineShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultInvoiceLineShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the invoiceLineList where subTotal equals to UPDATED_SUB_TOTAL
        defaultInvoiceLineShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList where subTotal is not null
        defaultInvoiceLineShouldBeFound("subTotal.specified=true");

        // Get all the invoiceLineList where subTotal is null
        defaultInvoiceLineShouldNotBeFound("subTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceLinesByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoiceLine.setInvoice(invoice);
        invoiceLineRepository.saveAndFlush(invoiceLine);
        Long invoiceId = invoice.getId();

        // Get all the invoiceLineList where invoice equals to invoiceId
        defaultInvoiceLineShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the invoiceLineList where invoice equals to invoiceId + 1
        defaultInvoiceLineShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceLineShouldBeFound(String filter) throws Exception {
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(DEFAULT_UNIT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceLineShouldNotBeFound(String filter) throws Exception {
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvoiceLine() throws Exception {
        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(get("/api/invoice-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);
        int databaseSizeBeforeUpdate = invoiceLineRepository.findAll().size();

        // Update the invoiceLine
        InvoiceLine updatedInvoiceLine = invoiceLineRepository.findOne(invoiceLine.getId());
        // Disconnect from session so that the updates on updatedInvoiceLine are not directly saved in db
        em.detach(updatedInvoiceLine);
        updatedInvoiceLine
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unitCost(UPDATED_UNIT_COST)
            .subTotal(UPDATED_SUB_TOTAL);
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(updatedInvoiceLine);

        restInvoiceLineMockMvc.perform(put("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeUpdate);
        InvoiceLine testInvoiceLine = invoiceLineList.get(invoiceLineList.size() - 1);
        assertThat(testInvoiceLine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceLine.getUnitCost()).isEqualTo(UPDATED_UNIT_COST);
        assertThat(testInvoiceLine.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceLine() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineRepository.findAll().size();

        // Create the InvoiceLine
        InvoiceLineDTO invoiceLineDTO = invoiceLineMapper.toDto(invoiceLine);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceLineMockMvc.perform(put("/api/invoice-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceLineDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceLine in the database
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoiceLine() throws Exception {
        // Initialize the database
        invoiceLineRepository.saveAndFlush(invoiceLine);
        int databaseSizeBeforeDelete = invoiceLineRepository.findAll().size();

        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(delete("/api/invoice-lines/{id}", invoiceLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceLine> invoiceLineList = invoiceLineRepository.findAll();
        assertThat(invoiceLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLine.class);
        InvoiceLine invoiceLine1 = new InvoiceLine();
        invoiceLine1.setId(1L);
        InvoiceLine invoiceLine2 = new InvoiceLine();
        invoiceLine2.setId(invoiceLine1.getId());
        assertThat(invoiceLine1).isEqualTo(invoiceLine2);
        invoiceLine2.setId(2L);
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);
        invoiceLine1.setId(null);
        assertThat(invoiceLine1).isNotEqualTo(invoiceLine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceLineDTO.class);
        InvoiceLineDTO invoiceLineDTO1 = new InvoiceLineDTO();
        invoiceLineDTO1.setId(1L);
        InvoiceLineDTO invoiceLineDTO2 = new InvoiceLineDTO();
        assertThat(invoiceLineDTO1).isNotEqualTo(invoiceLineDTO2);
        invoiceLineDTO2.setId(invoiceLineDTO1.getId());
        assertThat(invoiceLineDTO1).isEqualTo(invoiceLineDTO2);
        invoiceLineDTO2.setId(2L);
        assertThat(invoiceLineDTO1).isNotEqualTo(invoiceLineDTO2);
        invoiceLineDTO1.setId(null);
        assertThat(invoiceLineDTO1).isNotEqualTo(invoiceLineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceLineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceLineMapper.fromId(null)).isNull();
    }
}
