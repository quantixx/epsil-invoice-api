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

/**
 * A Tenant.
 */
@Entity
@Table(name = "inv_tenant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "slug", length = 15, nullable = false)
    private String slug;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "company_name")
    private String companyName;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "vat_identification")
    private String vatIdentification;

    @Column(name = "business_identification")
    private String businessIdentification;

    @Column(name = "business_registry")
    private String businessRegistry;

    @Column(name = "business_code")
    private String businessCode;

    @Column(name = "financial_year_from")
    private String financialYearFrom;

    @Column(name = "financial_year_to")
    private String financialYearTo;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Address invoiceAddress;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Bank bank;

    @OneToOne
    @JoinColumn(unique = true)
    private Document logo;

    @OneToOne
    @JoinColumn(unique = true)
    private Contact contact;

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Invoice> invoices = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvoiceDefinition> invoiceDefinitions = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Quotation> quotations = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuotationDefinition> quotationDefinitions = new HashSet<>();

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Family> families = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public Tenant slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public Tenant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Tenant companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public Tenant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVatIdentification() {
        return vatIdentification;
    }

    public Tenant vatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
        return this;
    }

    public void setVatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public String getBusinessIdentification() {
        return businessIdentification;
    }

    public Tenant businessIdentification(String businessIdentification) {
        this.businessIdentification = businessIdentification;
        return this;
    }

    public void setBusinessIdentification(String businessIdentification) {
        this.businessIdentification = businessIdentification;
    }

    public String getBusinessRegistry() {
        return businessRegistry;
    }

    public Tenant businessRegistry(String businessRegistry) {
        this.businessRegistry = businessRegistry;
        return this;
    }

    public void setBusinessRegistry(String businessRegistry) {
        this.businessRegistry = businessRegistry;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public Tenant businessCode(String businessCode) {
        this.businessCode = businessCode;
        return this;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getFinancialYearFrom() {
        return financialYearFrom;
    }

    public Tenant financialYearFrom(String financialYearFrom) {
        this.financialYearFrom = financialYearFrom;
        return this;
    }

    public void setFinancialYearFrom(String financialYearFrom) {
        this.financialYearFrom = financialYearFrom;
    }

    public String getFinancialYearTo() {
        return financialYearTo;
    }

    public Tenant financialYearTo(String financialYearTo) {
        this.financialYearTo = financialYearTo;
        return this;
    }

    public void setFinancialYearTo(String financialYearTo) {
        this.financialYearTo = financialYearTo;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public Tenant invoiceAddress(Address address) {
        this.invoiceAddress = address;
        return this;
    }

    public void setInvoiceAddress(Address address) {
        this.invoiceAddress = address;
    }

    public Bank getBank() {
        return bank;
    }

    public Tenant bank(Bank bank) {
        this.bank = bank;
        return this;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Document getLogo() {
        return logo;
    }

    public Tenant logo(Document document) {
        this.logo = document;
        return this;
    }

    public void setLogo(Document document) {
        this.logo = document;
    }

    public Contact getContact() {
        return contact;
    }

    public Tenant contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Tenant events(Set<Event> events) {
        this.events = events;
        return this;
    }

    public Tenant addEvents(Event event) {
        this.events.add(event);
        event.setTenant(this);
        return this;
    }

    public Tenant removeEvents(Event event) {
        this.events.remove(event);
        event.setTenant(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public Tenant invoices(Set<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

    public Tenant addInvoices(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setTenant(this);
        return this;
    }

    public Tenant removeInvoices(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setTenant(null);
        return this;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Set<InvoiceDefinition> getInvoiceDefinitions() {
        return invoiceDefinitions;
    }

    public Tenant invoiceDefinitions(Set<InvoiceDefinition> invoiceDefinitions) {
        this.invoiceDefinitions = invoiceDefinitions;
        return this;
    }

    public Tenant addInvoiceDefinitions(InvoiceDefinition invoiceDefinition) {
        this.invoiceDefinitions.add(invoiceDefinition);
        invoiceDefinition.setTenant(this);
        return this;
    }

    public Tenant removeInvoiceDefinitions(InvoiceDefinition invoiceDefinition) {
        this.invoiceDefinitions.remove(invoiceDefinition);
        invoiceDefinition.setTenant(null);
        return this;
    }

    public void setInvoiceDefinitions(Set<InvoiceDefinition> invoiceDefinitions) {
        this.invoiceDefinitions = invoiceDefinitions;
    }

    public Set<Quotation> getQuotations() {
        return quotations;
    }

    public Tenant quotations(Set<Quotation> quotations) {
        this.quotations = quotations;
        return this;
    }

    public Tenant addQuotations(Quotation quotation) {
        this.quotations.add(quotation);
        quotation.setTenant(this);
        return this;
    }

    public Tenant removeQuotations(Quotation quotation) {
        this.quotations.remove(quotation);
        quotation.setTenant(null);
        return this;
    }

    public void setQuotations(Set<Quotation> quotations) {
        this.quotations = quotations;
    }

    public Set<QuotationDefinition> getQuotationDefinitions() {
        return quotationDefinitions;
    }

    public Tenant quotationDefinitions(Set<QuotationDefinition> quotationDefinitions) {
        this.quotationDefinitions = quotationDefinitions;
        return this;
    }

    public Tenant addQuotationDefinitions(QuotationDefinition quotationDefinition) {
        this.quotationDefinitions.add(quotationDefinition);
        quotationDefinition.setTenant(this);
        return this;
    }

    public Tenant removeQuotationDefinitions(QuotationDefinition quotationDefinition) {
        this.quotationDefinitions.remove(quotationDefinition);
        quotationDefinition.setTenant(null);
        return this;
    }

    public void setQuotationDefinitions(Set<QuotationDefinition> quotationDefinitions) {
        this.quotationDefinitions = quotationDefinitions;
    }

    public Set<Family> getFamilies() {
        return families;
    }

    public Tenant families(Set<Family> families) {
        this.families = families;
        return this;
    }

    public Tenant addFamilies(Family family) {
        this.families.add(family);
        family.setTenant(this);
        return this;
    }

    public Tenant removeFamilies(Family family) {
        this.families.remove(family);
        family.setTenant(null);
        return this;
    }

    public void setFamilies(Set<Family> families) {
        this.families = families;
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
        Tenant tenant = (Tenant) o;
        if (tenant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tenant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tenant{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", name='" + getName() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", description='" + getDescription() + "'" +
            ", vatIdentification='" + getVatIdentification() + "'" +
            ", businessIdentification='" + getBusinessIdentification() + "'" +
            ", businessRegistry='" + getBusinessRegistry() + "'" +
            ", businessCode='" + getBusinessCode() + "'" +
            ", financialYearFrom='" + getFinancialYearFrom() + "'" +
            ", financialYearTo='" + getFinancialYearTo() + "'" +
            "}";
    }
}
