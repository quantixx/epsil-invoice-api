package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Organisation;
import io.epsil.invoice.domain.Address;
import io.epsil.invoice.domain.Contact;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.repository.OrganisationRepository;
import io.epsil.invoice.service.OrganisationService;
import io.epsil.invoice.service.dto.OrganisationDTO;
import io.epsil.invoice.service.mapper.OrganisationMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.OrganisationCriteria;
import io.epsil.invoice.service.OrganisationQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.epsil.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrganisationResource REST controller.
 *
 * @see OrganisationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class OrganisationResourceIntTest {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_CORPORATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CORPORATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VAT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_VAT_IDENTIFICATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private OrganisationMapper organisationMapper;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private OrganisationQueryService organisationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrganisationMockMvc;

    private Organisation organisation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganisationResource organisationResource = new OrganisationResource(organisationService, organisationQueryService);
        this.restOrganisationMockMvc = MockMvcBuilders.standaloneSetup(organisationResource)
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
    public static Organisation createEntity(EntityManager em) {
        Organisation organisation = new Organisation()
            .slug(DEFAULT_SLUG)
            .name(DEFAULT_NAME)
            .businessIdentification(DEFAULT_BUSINESS_IDENTIFICATION)
            .corporateName(DEFAULT_CORPORATE_NAME)
            .vatIdentification(DEFAULT_VAT_IDENTIFICATION)
            .activated(DEFAULT_ACTIVATED)
            .createdOn(DEFAULT_CREATED_ON);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        organisation.setTenant(tenant);
        return organisation;
    }

    @Before
    public void initTest() {
        organisation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganisation() throws Exception {
        int databaseSizeBeforeCreate = organisationRepository.findAll().size();

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);
        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isCreated());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeCreate + 1);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testOrganisation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisation.getBusinessIdentification()).isEqualTo(DEFAULT_BUSINESS_IDENTIFICATION);
        assertThat(testOrganisation.getCorporateName()).isEqualTo(DEFAULT_CORPORATE_NAME);
        assertThat(testOrganisation.getVatIdentification()).isEqualTo(DEFAULT_VAT_IDENTIFICATION);
        assertThat(testOrganisation.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testOrganisation.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    public void createOrganisationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organisationRepository.findAll().size();

        // Create the Organisation with an existing ID
        organisation.setId(1L);
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationRepository.findAll().size();
        // set the field null
        organisation.setSlug(null);

        // Create the Organisation, which fails.
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organisationRepository.findAll().size();
        // set the field null
        organisation.setName(null);

        // Create the Organisation, which fails.
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        restOrganisationMockMvc.perform(post("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isBadRequest());

        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOrganisations() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].businessIdentification").value(hasItem(DEFAULT_BUSINESS_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].corporateName").value(hasItem(DEFAULT_CORPORATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].vatIdentification").value(hasItem(DEFAULT_VAT_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @Test
    @Transactional
    public void getOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get the organisation
        restOrganisationMockMvc.perform(get("/api/organisations/{id}", organisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organisation.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.businessIdentification").value(DEFAULT_BUSINESS_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.corporateName").value(DEFAULT_CORPORATE_NAME.toString()))
            .andExpect(jsonPath("$.vatIdentification").value(DEFAULT_VAT_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    public void getAllOrganisationsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where slug equals to DEFAULT_SLUG
        defaultOrganisationShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the organisationList where slug equals to UPDATED_SLUG
        defaultOrganisationShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllOrganisationsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultOrganisationShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the organisationList where slug equals to UPDATED_SLUG
        defaultOrganisationShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllOrganisationsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where slug is not null
        defaultOrganisationShouldBeFound("slug.specified=true");

        // Get all the organisationList where slug is null
        defaultOrganisationShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where name equals to DEFAULT_NAME
        defaultOrganisationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organisationList where name equals to UPDATED_NAME
        defaultOrganisationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganisationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organisationList where name equals to UPDATED_NAME
        defaultOrganisationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where name is not null
        defaultOrganisationShouldBeFound("name.specified=true");

        // Get all the organisationList where name is null
        defaultOrganisationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByBusinessIdentificationIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where businessIdentification equals to DEFAULT_BUSINESS_IDENTIFICATION
        defaultOrganisationShouldBeFound("businessIdentification.equals=" + DEFAULT_BUSINESS_IDENTIFICATION);

        // Get all the organisationList where businessIdentification equals to UPDATED_BUSINESS_IDENTIFICATION
        defaultOrganisationShouldNotBeFound("businessIdentification.equals=" + UPDATED_BUSINESS_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByBusinessIdentificationIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where businessIdentification in DEFAULT_BUSINESS_IDENTIFICATION or UPDATED_BUSINESS_IDENTIFICATION
        defaultOrganisationShouldBeFound("businessIdentification.in=" + DEFAULT_BUSINESS_IDENTIFICATION + "," + UPDATED_BUSINESS_IDENTIFICATION);

        // Get all the organisationList where businessIdentification equals to UPDATED_BUSINESS_IDENTIFICATION
        defaultOrganisationShouldNotBeFound("businessIdentification.in=" + UPDATED_BUSINESS_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByBusinessIdentificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where businessIdentification is not null
        defaultOrganisationShouldBeFound("businessIdentification.specified=true");

        // Get all the organisationList where businessIdentification is null
        defaultOrganisationShouldNotBeFound("businessIdentification.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByCorporateNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where corporateName equals to DEFAULT_CORPORATE_NAME
        defaultOrganisationShouldBeFound("corporateName.equals=" + DEFAULT_CORPORATE_NAME);

        // Get all the organisationList where corporateName equals to UPDATED_CORPORATE_NAME
        defaultOrganisationShouldNotBeFound("corporateName.equals=" + UPDATED_CORPORATE_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByCorporateNameIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where corporateName in DEFAULT_CORPORATE_NAME or UPDATED_CORPORATE_NAME
        defaultOrganisationShouldBeFound("corporateName.in=" + DEFAULT_CORPORATE_NAME + "," + UPDATED_CORPORATE_NAME);

        // Get all the organisationList where corporateName equals to UPDATED_CORPORATE_NAME
        defaultOrganisationShouldNotBeFound("corporateName.in=" + UPDATED_CORPORATE_NAME);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByCorporateNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where corporateName is not null
        defaultOrganisationShouldBeFound("corporateName.specified=true");

        // Get all the organisationList where corporateName is null
        defaultOrganisationShouldNotBeFound("corporateName.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByVatIdentificationIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where vatIdentification equals to DEFAULT_VAT_IDENTIFICATION
        defaultOrganisationShouldBeFound("vatIdentification.equals=" + DEFAULT_VAT_IDENTIFICATION);

        // Get all the organisationList where vatIdentification equals to UPDATED_VAT_IDENTIFICATION
        defaultOrganisationShouldNotBeFound("vatIdentification.equals=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByVatIdentificationIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where vatIdentification in DEFAULT_VAT_IDENTIFICATION or UPDATED_VAT_IDENTIFICATION
        defaultOrganisationShouldBeFound("vatIdentification.in=" + DEFAULT_VAT_IDENTIFICATION + "," + UPDATED_VAT_IDENTIFICATION);

        // Get all the organisationList where vatIdentification equals to UPDATED_VAT_IDENTIFICATION
        defaultOrganisationShouldNotBeFound("vatIdentification.in=" + UPDATED_VAT_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByVatIdentificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where vatIdentification is not null
        defaultOrganisationShouldBeFound("vatIdentification.specified=true");

        // Get all the organisationList where vatIdentification is null
        defaultOrganisationShouldNotBeFound("vatIdentification.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByActivatedIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where activated equals to DEFAULT_ACTIVATED
        defaultOrganisationShouldBeFound("activated.equals=" + DEFAULT_ACTIVATED);

        // Get all the organisationList where activated equals to UPDATED_ACTIVATED
        defaultOrganisationShouldNotBeFound("activated.equals=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByActivatedIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where activated in DEFAULT_ACTIVATED or UPDATED_ACTIVATED
        defaultOrganisationShouldBeFound("activated.in=" + DEFAULT_ACTIVATED + "," + UPDATED_ACTIVATED);

        // Get all the organisationList where activated equals to UPDATED_ACTIVATED
        defaultOrganisationShouldNotBeFound("activated.in=" + UPDATED_ACTIVATED);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByActivatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where activated is not null
        defaultOrganisationShouldBeFound("activated.specified=true");

        // Get all the organisationList where activated is null
        defaultOrganisationShouldNotBeFound("activated.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where createdOn equals to DEFAULT_CREATED_ON
        defaultOrganisationShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the organisationList where createdOn equals to UPDATED_CREATED_ON
        defaultOrganisationShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultOrganisationShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the organisationList where createdOn equals to UPDATED_CREATED_ON
        defaultOrganisationShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllOrganisationsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);

        // Get all the organisationList where createdOn is not null
        defaultOrganisationShouldBeFound("createdOn.specified=true");

        // Get all the organisationList where createdOn is null
        defaultOrganisationShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllOrganisationsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        Address address = AddressResourceIntTest.createEntity(em);
        em.persist(address);
        em.flush();
        organisation.setAddress(address);
        organisationRepository.saveAndFlush(organisation);
        Long addressId = address.getId();

        // Get all the organisationList where address equals to addressId
        defaultOrganisationShouldBeFound("addressId.equals=" + addressId);

        // Get all the organisationList where address equals to addressId + 1
        defaultOrganisationShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        Contact contact = ContactResourceIntTest.createEntity(em);
        em.persist(contact);
        em.flush();
        organisation.setContact(contact);
        organisationRepository.saveAndFlush(organisation);
        Long contactId = contact.getId();

        // Get all the organisationList where contact equals to contactId
        defaultOrganisationShouldBeFound("contactId.equals=" + contactId);

        // Get all the organisationList where contact equals to contactId + 1
        defaultOrganisationShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }


    @Test
    @Transactional
    public void getAllOrganisationsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        organisation.setTenant(tenant);
        organisationRepository.saveAndFlush(organisation);
        Long tenantId = tenant.getId();

        // Get all the organisationList where tenant equals to tenantId
        defaultOrganisationShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the organisationList where tenant equals to tenantId + 1
        defaultOrganisationShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultOrganisationShouldBeFound(String filter) throws Exception {
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organisation.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].businessIdentification").value(hasItem(DEFAULT_BUSINESS_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].corporateName").value(hasItem(DEFAULT_CORPORATE_NAME.toString())))
            .andExpect(jsonPath("$.[*].vatIdentification").value(hasItem(DEFAULT_VAT_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultOrganisationShouldNotBeFound(String filter) throws Exception {
        restOrganisationMockMvc.perform(get("/api/organisations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingOrganisation() throws Exception {
        // Get the organisation
        restOrganisationMockMvc.perform(get("/api/organisations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);
        int databaseSizeBeforeUpdate = organisationRepository.findAll().size();

        // Update the organisation
        Organisation updatedOrganisation = organisationRepository.findOne(organisation.getId());
        // Disconnect from session so that the updates on updatedOrganisation are not directly saved in db
        em.detach(updatedOrganisation);
        updatedOrganisation
            .slug(UPDATED_SLUG)
            .name(UPDATED_NAME)
            .businessIdentification(UPDATED_BUSINESS_IDENTIFICATION)
            .corporateName(UPDATED_CORPORATE_NAME)
            .vatIdentification(UPDATED_VAT_IDENTIFICATION)
            .activated(UPDATED_ACTIVATED)
            .createdOn(UPDATED_CREATED_ON);
        OrganisationDTO organisationDTO = organisationMapper.toDto(updatedOrganisation);

        restOrganisationMockMvc.perform(put("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isOk());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate);
        Organisation testOrganisation = organisationList.get(organisationList.size() - 1);
        assertThat(testOrganisation.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testOrganisation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisation.getBusinessIdentification()).isEqualTo(UPDATED_BUSINESS_IDENTIFICATION);
        assertThat(testOrganisation.getCorporateName()).isEqualTo(UPDATED_CORPORATE_NAME);
        assertThat(testOrganisation.getVatIdentification()).isEqualTo(UPDATED_VAT_IDENTIFICATION);
        assertThat(testOrganisation.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testOrganisation.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganisation() throws Exception {
        int databaseSizeBeforeUpdate = organisationRepository.findAll().size();

        // Create the Organisation
        OrganisationDTO organisationDTO = organisationMapper.toDto(organisation);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrganisationMockMvc.perform(put("/api/organisations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organisationDTO)))
            .andExpect(status().isCreated());

        // Validate the Organisation in the database
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrganisation() throws Exception {
        // Initialize the database
        organisationRepository.saveAndFlush(organisation);
        int databaseSizeBeforeDelete = organisationRepository.findAll().size();

        // Get the organisation
        restOrganisationMockMvc.perform(delete("/api/organisations/{id}", organisation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Organisation> organisationList = organisationRepository.findAll();
        assertThat(organisationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = new Organisation();
        organisation1.setId(1L);
        Organisation organisation2 = new Organisation();
        organisation2.setId(organisation1.getId());
        assertThat(organisation1).isEqualTo(organisation2);
        organisation2.setId(2L);
        assertThat(organisation1).isNotEqualTo(organisation2);
        organisation1.setId(null);
        assertThat(organisation1).isNotEqualTo(organisation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationDTO.class);
        OrganisationDTO organisationDTO1 = new OrganisationDTO();
        organisationDTO1.setId(1L);
        OrganisationDTO organisationDTO2 = new OrganisationDTO();
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO2.setId(organisationDTO1.getId());
        assertThat(organisationDTO1).isEqualTo(organisationDTO2);
        organisationDTO2.setId(2L);
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO1.setId(null);
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(organisationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(organisationMapper.fromId(null)).isNull();
    }
}
