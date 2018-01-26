package io.epsil.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.DocumentType;

import io.epsil.invoice.domain.enumeration.InvoiceType;

/**
 * A Invoice.
 */
@Entity
@Table(name = "inv_invoice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(name = "title", length = 50)
    private String title;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocumentType docType;

    @NotNull
    @Column(name = "vat_rate", nullable = false)
    private Float vatRate;

    @NotNull
    @Size(max = 32)
    @Column(name = "terms", length = 32, nullable = false)
    private String terms;

    @NotNull
    @Size(max = 512)
    @Column(name = "penalties", length = 512, nullable = false)
    private String penalties;

    @NotNull
    @Column(name = "inv_number", nullable = false)
    private String number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", nullable = false)
    private InvoiceType invoiceType;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "sub_total_before_discount")
    private Float subTotalBeforeDiscount;

    @Column(name = "discount_rate")
    private Float discountRate;

    @Column(name = "discount_amount")
    private Float discountAmount;

    @NotNull
    @Column(name = "sub_total", nullable = false)
    private Float subTotal;

    @NotNull
    @Column(name = "vat_amount", nullable = false)
    private Float vatAmount;

    @NotNull
    @Column(name = "total", nullable = false)
    private Float total;

    @Column(name = "additional_information")
    private String additionalInformation;

    @OneToOne
    @JoinColumn(unique = true)
    private Invoice linked;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Organisation organisation;

    @OneToOne
    @JoinColumn(unique = true)
    private Quotation quotation;

    @OneToOne
    @JoinColumn(unique = true)
    private Document document;

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvoiceState> states = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvoiceLine> lines = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Language language;

    @ManyToOne(optional = false)
    @NotNull
    private Currency currency;

    @ManyToOne(optional = false)
    @NotNull
    private Family family;

    @ManyToOne(optional = false)
    @NotNull
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Invoice title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Invoice description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public Invoice docType(DocumentType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public Invoice vatRate(Float vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(Float vatRate) {
        this.vatRate = vatRate;
    }

    public String getTerms() {
        return terms;
    }

    public Invoice terms(String terms) {
        this.terms = terms;
        return this;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPenalties() {
        return penalties;
    }

    public Invoice penalties(String penalties) {
        this.penalties = penalties;
        return this;
    }

    public void setPenalties(String penalties) {
        this.penalties = penalties;
    }

    public String getNumber() {
        return number;
    }

    public Invoice number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public Invoice invoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
        return this;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public Invoice poNumber(String poNumber) {
        this.poNumber = poNumber;
        return this;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public Float getSubTotalBeforeDiscount() {
        return subTotalBeforeDiscount;
    }

    public Invoice subTotalBeforeDiscount(Float subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
        return this;
    }

    public void setSubTotalBeforeDiscount(Float subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
    }

    public Float getDiscountRate() {
        return discountRate;
    }

    public Invoice discountRate(Float discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Float discountRate) {
        this.discountRate = discountRate;
    }

    public Float getDiscountAmount() {
        return discountAmount;
    }

    public Invoice discountAmount(Float discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(Float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public Invoice subTotal(Float subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Float getVatAmount() {
        return vatAmount;
    }

    public Invoice vatAmount(Float vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public void setVatAmount(Float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Float getTotal() {
        return total;
    }

    public Invoice total(Float total) {
        this.total = total;
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public Invoice additionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Invoice getLinked() {
        return linked;
    }

    public Invoice linked(Invoice invoice) {
        this.linked = invoice;
        return this;
    }

    public void setLinked(Invoice invoice) {
        this.linked = invoice;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public Invoice organisation(Organisation organisation) {
        this.organisation = organisation;
        return this;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public Invoice quotation(Quotation quotation) {
        this.quotation = quotation;
        return this;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    public Document getDocument() {
        return document;
    }

    public Invoice document(Document document) {
        this.document = document;
        return this;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Set<InvoiceState> getStates() {
        return states;
    }

    public Invoice states(Set<InvoiceState> invoiceStates) {
        this.states = invoiceStates;
        return this;
    }

    public Invoice addStates(InvoiceState invoiceState) {
        this.states.add(invoiceState);
        invoiceState.setInvoice(this);
        return this;
    }

    public Invoice removeStates(InvoiceState invoiceState) {
        this.states.remove(invoiceState);
        invoiceState.setInvoice(null);
        return this;
    }

    public void setStates(Set<InvoiceState> invoiceStates) {
        this.states = invoiceStates;
    }

    public Set<InvoiceLine> getLines() {
        return lines;
    }

    public Invoice lines(Set<InvoiceLine> invoiceLines) {
        this.lines = invoiceLines;
        return this;
    }

    public Invoice addLines(InvoiceLine invoiceLine) {
        this.lines.add(invoiceLine);
        invoiceLine.setInvoice(this);
        return this;
    }

    public Invoice removeLines(InvoiceLine invoiceLine) {
        this.lines.remove(invoiceLine);
        invoiceLine.setInvoice(null);
        return this;
    }

    public void setLines(Set<InvoiceLine> invoiceLines) {
        this.lines = invoiceLines;
    }

    public Language getLanguage() {
        return language;
    }

    public Invoice language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Invoice currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Family getFamily() {
        return family;
    }

    public Invoice family(Family family) {
        this.family = family;
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Invoice tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Invoice invoice = (Invoice) o;
        if (invoice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), invoice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", vatRate=" + getVatRate() +
            ", terms='" + getTerms() + "'" +
            ", penalties='" + getPenalties() + "'" +
            ", number='" + getNumber() + "'" +
            ", invoiceType='" + getInvoiceType() + "'" +
            ", poNumber='" + getPoNumber() + "'" +
            ", subTotalBeforeDiscount=" + getSubTotalBeforeDiscount() +
            ", discountRate=" + getDiscountRate() +
            ", discountAmount=" + getDiscountAmount() +
            ", subTotal=" + getSubTotal() +
            ", vatAmount=" + getVatAmount() +
            ", total=" + getTotal() +
            ", additionalInformation='" + getAdditionalInformation() + "'" +
            "}";
    }
}
