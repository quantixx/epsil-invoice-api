package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Document;
import io.epsil.invoice.domain.Language;
import io.epsil.invoice.repository.DocumentRepository;
import io.epsil.invoice.service.DocumentService;
import io.epsil.invoice.service.dto.DocumentDTO;
import io.epsil.invoice.service.mapper.DocumentMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.DocumentCriteria;
import io.epsil.invoice.service.DocumentQueryService;

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

import io.epsil.invoice.domain.enumeration.DocumentType;
/**
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class DocumentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.TENANT_LOGO;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.INVOICE;

    private static final Integer DEFAULT_DOCUMENT_SIZE = 1;
    private static final Integer UPDATED_DOCUMENT_SIZE = 2;

    private static final String DEFAULT_DOCUSIGN_ENVELOPE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DOCUSIGN_ENVELOPE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentQueryService documentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService, documentQueryService);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
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
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .documentSize(DEFAULT_DOCUMENT_SIZE)
            .docusignEnvelopeId(DEFAULT_DOCUSIGN_ENVELOPE_ID)
            .url(DEFAULT_URL)
            .contentType(DEFAULT_CONTENT_TYPE)
            .createdOn(DEFAULT_CREATED_ON);
        return document;
    }

    @Before
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testDocument.getDocumentSize()).isEqualTo(DEFAULT_DOCUMENT_SIZE);
        assertThat(testDocument.getDocusignEnvelopeId()).isEqualTo(DEFAULT_DOCUSIGN_ENVELOPE_ID);
        assertThat(testDocument.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testDocument.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testDocument.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setName(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].documentSize").value(hasItem(DEFAULT_DOCUMENT_SIZE)))
            .andExpect(jsonPath("$.[*].docusignEnvelopeId").value(hasItem(DEFAULT_DOCUSIGN_ENVELOPE_ID.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.documentSize").value(DEFAULT_DOCUMENT_SIZE))
            .andExpect(jsonPath("$.docusignEnvelopeId").value(DEFAULT_DOCUSIGN_ENVELOPE_ID.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()));
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name equals to DEFAULT_NAME
        defaultDocumentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the documentList where name equals to UPDATED_NAME
        defaultDocumentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDocumentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the documentList where name equals to UPDATED_NAME
        defaultDocumentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDocumentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where name is not null
        defaultDocumentShouldBeFound("name.specified=true");

        // Get all the documentList where name is null
        defaultDocumentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description equals to DEFAULT_DESCRIPTION
        defaultDocumentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the documentList where description equals to UPDATED_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDocumentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the documentList where description equals to UPDATED_DESCRIPTION
        defaultDocumentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where description is not null
        defaultDocumentShouldBeFound("description.specified=true");

        // Get all the documentList where description is null
        defaultDocumentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentType equals to DEFAULT_DOCUMENT_TYPE
        defaultDocumentShouldBeFound("documentType.equals=" + DEFAULT_DOCUMENT_TYPE);

        // Get all the documentList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultDocumentShouldNotBeFound("documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentType in DEFAULT_DOCUMENT_TYPE or UPDATED_DOCUMENT_TYPE
        defaultDocumentShouldBeFound("documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE);

        // Get all the documentList where documentType equals to UPDATED_DOCUMENT_TYPE
        defaultDocumentShouldNotBeFound("documentType.in=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentType is not null
        defaultDocumentShouldBeFound("documentType.specified=true");

        // Get all the documentList where documentType is null
        defaultDocumentShouldNotBeFound("documentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentSize equals to DEFAULT_DOCUMENT_SIZE
        defaultDocumentShouldBeFound("documentSize.equals=" + DEFAULT_DOCUMENT_SIZE);

        // Get all the documentList where documentSize equals to UPDATED_DOCUMENT_SIZE
        defaultDocumentShouldNotBeFound("documentSize.equals=" + UPDATED_DOCUMENT_SIZE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentSizeIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentSize in DEFAULT_DOCUMENT_SIZE or UPDATED_DOCUMENT_SIZE
        defaultDocumentShouldBeFound("documentSize.in=" + DEFAULT_DOCUMENT_SIZE + "," + UPDATED_DOCUMENT_SIZE);

        // Get all the documentList where documentSize equals to UPDATED_DOCUMENT_SIZE
        defaultDocumentShouldNotBeFound("documentSize.in=" + UPDATED_DOCUMENT_SIZE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentSize is not null
        defaultDocumentShouldBeFound("documentSize.specified=true");

        // Get all the documentList where documentSize is null
        defaultDocumentShouldNotBeFound("documentSize.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentSize greater than or equals to DEFAULT_DOCUMENT_SIZE
        defaultDocumentShouldBeFound("documentSize.greaterOrEqualThan=" + DEFAULT_DOCUMENT_SIZE);

        // Get all the documentList where documentSize greater than or equals to UPDATED_DOCUMENT_SIZE
        defaultDocumentShouldNotBeFound("documentSize.greaterOrEqualThan=" + UPDATED_DOCUMENT_SIZE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocumentSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where documentSize less than or equals to DEFAULT_DOCUMENT_SIZE
        defaultDocumentShouldNotBeFound("documentSize.lessThan=" + DEFAULT_DOCUMENT_SIZE);

        // Get all the documentList where documentSize less than or equals to UPDATED_DOCUMENT_SIZE
        defaultDocumentShouldBeFound("documentSize.lessThan=" + UPDATED_DOCUMENT_SIZE);
    }


    @Test
    @Transactional
    public void getAllDocumentsByDocusignEnvelopeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docusignEnvelopeId equals to DEFAULT_DOCUSIGN_ENVELOPE_ID
        defaultDocumentShouldBeFound("docusignEnvelopeId.equals=" + DEFAULT_DOCUSIGN_ENVELOPE_ID);

        // Get all the documentList where docusignEnvelopeId equals to UPDATED_DOCUSIGN_ENVELOPE_ID
        defaultDocumentShouldNotBeFound("docusignEnvelopeId.equals=" + UPDATED_DOCUSIGN_ENVELOPE_ID);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocusignEnvelopeIdIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docusignEnvelopeId in DEFAULT_DOCUSIGN_ENVELOPE_ID or UPDATED_DOCUSIGN_ENVELOPE_ID
        defaultDocumentShouldBeFound("docusignEnvelopeId.in=" + DEFAULT_DOCUSIGN_ENVELOPE_ID + "," + UPDATED_DOCUSIGN_ENVELOPE_ID);

        // Get all the documentList where docusignEnvelopeId equals to UPDATED_DOCUSIGN_ENVELOPE_ID
        defaultDocumentShouldNotBeFound("docusignEnvelopeId.in=" + UPDATED_DOCUSIGN_ENVELOPE_ID);
    }

    @Test
    @Transactional
    public void getAllDocumentsByDocusignEnvelopeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docusignEnvelopeId is not null
        defaultDocumentShouldBeFound("docusignEnvelopeId.specified=true");

        // Get all the documentList where docusignEnvelopeId is null
        defaultDocumentShouldNotBeFound("docusignEnvelopeId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where url equals to DEFAULT_URL
        defaultDocumentShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the documentList where url equals to UPDATED_URL
        defaultDocumentShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where url in DEFAULT_URL or UPDATED_URL
        defaultDocumentShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the documentList where url equals to UPDATED_URL
        defaultDocumentShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllDocumentsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where url is not null
        defaultDocumentShouldBeFound("url.specified=true");

        // Get all the documentList where url is null
        defaultDocumentShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where contentType equals to DEFAULT_CONTENT_TYPE
        defaultDocumentShouldBeFound("contentType.equals=" + DEFAULT_CONTENT_TYPE);

        // Get all the documentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultDocumentShouldNotBeFound("contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where contentType in DEFAULT_CONTENT_TYPE or UPDATED_CONTENT_TYPE
        defaultDocumentShouldBeFound("contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE);

        // Get all the documentList where contentType equals to UPDATED_CONTENT_TYPE
        defaultDocumentShouldNotBeFound("contentType.in=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllDocumentsByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where contentType is not null
        defaultDocumentShouldBeFound("contentType.specified=true");

        // Get all the documentList where contentType is null
        defaultDocumentShouldNotBeFound("contentType.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByCreatedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where createdOn equals to DEFAULT_CREATED_ON
        defaultDocumentShouldBeFound("createdOn.equals=" + DEFAULT_CREATED_ON);

        // Get all the documentList where createdOn equals to UPDATED_CREATED_ON
        defaultDocumentShouldNotBeFound("createdOn.equals=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllDocumentsByCreatedOnIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where createdOn in DEFAULT_CREATED_ON or UPDATED_CREATED_ON
        defaultDocumentShouldBeFound("createdOn.in=" + DEFAULT_CREATED_ON + "," + UPDATED_CREATED_ON);

        // Get all the documentList where createdOn equals to UPDATED_CREATED_ON
        defaultDocumentShouldNotBeFound("createdOn.in=" + UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void getAllDocumentsByCreatedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where createdOn is not null
        defaultDocumentShouldBeFound("createdOn.specified=true");

        // Get all the documentList where createdOn is null
        defaultDocumentShouldNotBeFound("createdOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllDocumentsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        Language language = LanguageResourceIntTest.createEntity(em);
        em.persist(language);
        em.flush();
        document.setLanguage(language);
        documentRepository.saveAndFlush(document);
        Long languageId = language.getId();

        // Get all the documentList where language equals to languageId
        defaultDocumentShouldBeFound("languageId.equals=" + languageId);

        // Get all the documentList where language equals to languageId + 1
        defaultDocumentShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].documentSize").value(hasItem(DEFAULT_DOCUMENT_SIZE)))
            .andExpect(jsonPath("$.[*].docusignEnvelopeId").value(hasItem(DEFAULT_DOCUSIGN_ENVELOPE_ID.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findOne(document.getId());
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentSize(UPDATED_DOCUMENT_SIZE)
            .docusignEnvelopeId(UPDATED_DOCUSIGN_ENVELOPE_ID)
            .url(UPDATED_URL)
            .contentType(UPDATED_CONTENT_TYPE)
            .createdOn(UPDATED_CREATED_ON);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testDocument.getDocumentSize()).isEqualTo(UPDATED_DOCUMENT_SIZE);
        assertThat(testDocument.getDocusignEnvelopeId()).isEqualTo(UPDATED_DOCUSIGN_ENVELOPE_ID);
        assertThat(testDocument.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testDocument.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testDocument.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Get the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = new Document();
        document1.setId(1L);
        Document document2 = new Document();
        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);
        document2.setId(2L);
        assertThat(document1).isNotEqualTo(document2);
        document1.setId(null);
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentDTO.class);
        DocumentDTO documentDTO1 = new DocumentDTO();
        documentDTO1.setId(1L);
        DocumentDTO documentDTO2 = new DocumentDTO();
        assertThat(documentDTO1).isNotEqualTo(documentDTO2);
        documentDTO2.setId(documentDTO1.getId());
        assertThat(documentDTO1).isEqualTo(documentDTO2);
        documentDTO2.setId(2L);
        assertThat(documentDTO1).isNotEqualTo(documentDTO2);
        documentDTO1.setId(null);
        assertThat(documentDTO1).isNotEqualTo(documentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(documentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(documentMapper.fromId(null)).isNull();
    }
}
