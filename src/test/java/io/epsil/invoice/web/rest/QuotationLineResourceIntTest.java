package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.QuotationLine;
import io.epsil.invoice.repository.QuotationLineRepository;
import io.epsil.invoice.service.QuotationLineService;
import io.epsil.invoice.service.dto.QuotationLineDTO;
import io.epsil.invoice.service.mapper.QuotationLineMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.QuotationLineCriteria;
import io.epsil.invoice.service.QuotationLineQueryService;

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
 * Test class for the QuotationLineResource REST controller.
 *
 * @see QuotationLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class QuotationLineResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Float DEFAULT_UNIT_COST = 1F;
    private static final Float UPDATED_UNIT_COST = 2F;

    private static final Float DEFAULT_SUB_TOTAL = 1F;
    private static final Float UPDATED_SUB_TOTAL = 2F;

    @Autowired
    private QuotationLineRepository quotationLineRepository;

    @Autowired
    private QuotationLineMapper quotationLineMapper;

    @Autowired
    private QuotationLineService quotationLineService;

    @Autowired
    private QuotationLineQueryService quotationLineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuotationLineMockMvc;

    private QuotationLine quotationLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuotationLineResource quotationLineResource = new QuotationLineResource(quotationLineService, quotationLineQueryService);
        this.restQuotationLineMockMvc = MockMvcBuilders.standaloneSetup(quotationLineResource)
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
    public static QuotationLine createEntity(EntityManager em) {
        QuotationLine quotationLine = new QuotationLine()
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .unitCost(DEFAULT_UNIT_COST)
            .subTotal(DEFAULT_SUB_TOTAL);
        return quotationLine;
    }

    @Before
    public void initTest() {
        quotationLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotationLine() throws Exception {
        int databaseSizeBeforeCreate = quotationLineRepository.findAll().size();

        // Create the QuotationLine
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);
        restQuotationLineMockMvc.perform(post("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isCreated());

        // Validate the QuotationLine in the database
        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeCreate + 1);
        QuotationLine testQuotationLine = quotationLineList.get(quotationLineList.size() - 1);
        assertThat(testQuotationLine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuotationLine.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testQuotationLine.getUnitCost()).isEqualTo(DEFAULT_UNIT_COST);
        assertThat(testQuotationLine.getSubTotal()).isEqualTo(DEFAULT_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void createQuotationLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationLineRepository.findAll().size();

        // Create the QuotationLine with an existing ID
        quotationLine.setId(1L);
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationLineMockMvc.perform(post("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationLine in the database
        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationLineRepository.findAll().size();
        // set the field null
        quotationLine.setDescription(null);

        // Create the QuotationLine, which fails.
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);

        restQuotationLineMockMvc.perform(post("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationLineRepository.findAll().size();
        // set the field null
        quotationLine.setQuantity(null);

        // Create the QuotationLine, which fails.
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);

        restQuotationLineMockMvc.perform(post("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationLineRepository.findAll().size();
        // set the field null
        quotationLine.setUnitCost(null);

        // Create the QuotationLine, which fails.
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);

        restQuotationLineMockMvc.perform(post("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationLineRepository.findAll().size();
        // set the field null
        quotationLine.setSubTotal(null);

        // Create the QuotationLine, which fails.
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);

        restQuotationLineMockMvc.perform(post("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotationLines() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList
        restQuotationLineMockMvc.perform(get("/api/quotation-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(DEFAULT_UNIT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getQuotationLine() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get the quotationLine
        restQuotationLineMockMvc.perform(get("/api/quotation-lines/{id}", quotationLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotationLine.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitCost").value(DEFAULT_UNIT_COST.doubleValue()))
            .andExpect(jsonPath("$.subTotal").value(DEFAULT_SUB_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where description equals to DEFAULT_DESCRIPTION
        defaultQuotationLineShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the quotationLineList where description equals to UPDATED_DESCRIPTION
        defaultQuotationLineShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQuotationLineShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the quotationLineList where description equals to UPDATED_DESCRIPTION
        defaultQuotationLineShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where description is not null
        defaultQuotationLineShouldBeFound("description.specified=true");

        // Get all the quotationLineList where description is null
        defaultQuotationLineShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where quantity equals to DEFAULT_QUANTITY
        defaultQuotationLineShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the quotationLineList where quantity equals to UPDATED_QUANTITY
        defaultQuotationLineShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultQuotationLineShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the quotationLineList where quantity equals to UPDATED_QUANTITY
        defaultQuotationLineShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where quantity is not null
        defaultQuotationLineShouldBeFound("quantity.specified=true");

        // Get all the quotationLineList where quantity is null
        defaultQuotationLineShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultQuotationLineShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the quotationLineList where quantity greater than or equals to UPDATED_QUANTITY
        defaultQuotationLineShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where quantity less than or equals to DEFAULT_QUANTITY
        defaultQuotationLineShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the quotationLineList where quantity less than or equals to UPDATED_QUANTITY
        defaultQuotationLineShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllQuotationLinesByUnitCostIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where unitCost equals to DEFAULT_UNIT_COST
        defaultQuotationLineShouldBeFound("unitCost.equals=" + DEFAULT_UNIT_COST);

        // Get all the quotationLineList where unitCost equals to UPDATED_UNIT_COST
        defaultQuotationLineShouldNotBeFound("unitCost.equals=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByUnitCostIsInShouldWork() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where unitCost in DEFAULT_UNIT_COST or UPDATED_UNIT_COST
        defaultQuotationLineShouldBeFound("unitCost.in=" + DEFAULT_UNIT_COST + "," + UPDATED_UNIT_COST);

        // Get all the quotationLineList where unitCost equals to UPDATED_UNIT_COST
        defaultQuotationLineShouldNotBeFound("unitCost.in=" + UPDATED_UNIT_COST);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesByUnitCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where unitCost is not null
        defaultQuotationLineShouldBeFound("unitCost.specified=true");

        // Get all the quotationLineList where unitCost is null
        defaultQuotationLineShouldNotBeFound("unitCost.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationLinesBySubTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where subTotal equals to DEFAULT_SUB_TOTAL
        defaultQuotationLineShouldBeFound("subTotal.equals=" + DEFAULT_SUB_TOTAL);

        // Get all the quotationLineList where subTotal equals to UPDATED_SUB_TOTAL
        defaultQuotationLineShouldNotBeFound("subTotal.equals=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesBySubTotalIsInShouldWork() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where subTotal in DEFAULT_SUB_TOTAL or UPDATED_SUB_TOTAL
        defaultQuotationLineShouldBeFound("subTotal.in=" + DEFAULT_SUB_TOTAL + "," + UPDATED_SUB_TOTAL);

        // Get all the quotationLineList where subTotal equals to UPDATED_SUB_TOTAL
        defaultQuotationLineShouldNotBeFound("subTotal.in=" + UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void getAllQuotationLinesBySubTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);

        // Get all the quotationLineList where subTotal is not null
        defaultQuotationLineShouldBeFound("subTotal.specified=true");

        // Get all the quotationLineList where subTotal is null
        defaultQuotationLineShouldNotBeFound("subTotal.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuotationLineShouldBeFound(String filter) throws Exception {
        restQuotationLineMockMvc.perform(get("/api/quotation-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(DEFAULT_UNIT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].subTotal").value(hasItem(DEFAULT_SUB_TOTAL.doubleValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuotationLineShouldNotBeFound(String filter) throws Exception {
        restQuotationLineMockMvc.perform(get("/api/quotation-lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingQuotationLine() throws Exception {
        // Get the quotationLine
        restQuotationLineMockMvc.perform(get("/api/quotation-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotationLine() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);
        int databaseSizeBeforeUpdate = quotationLineRepository.findAll().size();

        // Update the quotationLine
        QuotationLine updatedQuotationLine = quotationLineRepository.findOne(quotationLine.getId());
        // Disconnect from session so that the updates on updatedQuotationLine are not directly saved in db
        em.detach(updatedQuotationLine);
        updatedQuotationLine
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .unitCost(UPDATED_UNIT_COST)
            .subTotal(UPDATED_SUB_TOTAL);
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(updatedQuotationLine);

        restQuotationLineMockMvc.perform(put("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isOk());

        // Validate the QuotationLine in the database
        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeUpdate);
        QuotationLine testQuotationLine = quotationLineList.get(quotationLineList.size() - 1);
        assertThat(testQuotationLine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuotationLine.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testQuotationLine.getUnitCost()).isEqualTo(UPDATED_UNIT_COST);
        assertThat(testQuotationLine.getSubTotal()).isEqualTo(UPDATED_SUB_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotationLine() throws Exception {
        int databaseSizeBeforeUpdate = quotationLineRepository.findAll().size();

        // Create the QuotationLine
        QuotationLineDTO quotationLineDTO = quotationLineMapper.toDto(quotationLine);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuotationLineMockMvc.perform(put("/api/quotation-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationLineDTO)))
            .andExpect(status().isCreated());

        // Validate the QuotationLine in the database
        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuotationLine() throws Exception {
        // Initialize the database
        quotationLineRepository.saveAndFlush(quotationLine);
        int databaseSizeBeforeDelete = quotationLineRepository.findAll().size();

        // Get the quotationLine
        restQuotationLineMockMvc.perform(delete("/api/quotation-lines/{id}", quotationLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuotationLine> quotationLineList = quotationLineRepository.findAll();
        assertThat(quotationLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationLine.class);
        QuotationLine quotationLine1 = new QuotationLine();
        quotationLine1.setId(1L);
        QuotationLine quotationLine2 = new QuotationLine();
        quotationLine2.setId(quotationLine1.getId());
        assertThat(quotationLine1).isEqualTo(quotationLine2);
        quotationLine2.setId(2L);
        assertThat(quotationLine1).isNotEqualTo(quotationLine2);
        quotationLine1.setId(null);
        assertThat(quotationLine1).isNotEqualTo(quotationLine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationLineDTO.class);
        QuotationLineDTO quotationLineDTO1 = new QuotationLineDTO();
        quotationLineDTO1.setId(1L);
        QuotationLineDTO quotationLineDTO2 = new QuotationLineDTO();
        assertThat(quotationLineDTO1).isNotEqualTo(quotationLineDTO2);
        quotationLineDTO2.setId(quotationLineDTO1.getId());
        assertThat(quotationLineDTO1).isEqualTo(quotationLineDTO2);
        quotationLineDTO2.setId(2L);
        assertThat(quotationLineDTO1).isNotEqualTo(quotationLineDTO2);
        quotationLineDTO1.setId(null);
        assertThat(quotationLineDTO1).isNotEqualTo(quotationLineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(quotationLineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(quotationLineMapper.fromId(null)).isNull();
    }
}
