package io.epsil.invoice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.DocumentType;

/**
 * A Quotation.
 */
@Entity
@Table(name = "inv_quotation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", nullable = false)
    private DocumentType docType;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "inv_type", length = 15, nullable = false)
    private String type;

    @NotNull
    @Column(name = "vat_rate", nullable = false)
    private Float vatRate;

    @NotNull
    @Size(max = 32)
    @Column(name = "validity_terms", length = 32, nullable = false)
    private String validityTerms;

    @Size(max = 512)
    @Column(name = "acceptionation", length = 512)
    private String acceptionation;

    @Size(max = 50)
    @Column(name = "title", length = 50)
    private String title;

    @NotNull
    @Column(name = "inv_number", nullable = false)
    private String number;

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

    @ManyToOne(optional = false)
    @NotNull
    private Family family;

    @OneToOne(mappedBy = "quotation")
    @JsonIgnore
    private Invoice invoice;

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

    public String getDescription() {
        return description;
    }

    public Quotation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentType getDocType() {
        return docType;
    }

    public Quotation docType(DocumentType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }

    public String getType() {
        return type;
    }

    public Quotation type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getVatRate() {
        return vatRate;
    }

    public Quotation vatRate(Float vatRate) {
        this.vatRate = vatRate;
        return this;
    }

    public void setVatRate(Float vatRate) {
        this.vatRate = vatRate;
    }

    public String getValidityTerms() {
        return validityTerms;
    }

    public Quotation validityTerms(String validityTerms) {
        this.validityTerms = validityTerms;
        return this;
    }

    public void setValidityTerms(String validityTerms) {
        this.validityTerms = validityTerms;
    }

    public String getAcceptionation() {
        return acceptionation;
    }

    public Quotation acceptionation(String acceptionation) {
        this.acceptionation = acceptionation;
        return this;
    }

    public void setAcceptionation(String acceptionation) {
        this.acceptionation = acceptionation;
    }

    public String getTitle() {
        return title;
    }

    public Quotation title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public Quotation number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Float getSubTotalBeforeDiscount() {
        return subTotalBeforeDiscount;
    }

    public Quotation subTotalBeforeDiscount(Float subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
        return this;
    }

    public void setSubTotalBeforeDiscount(Float subTotalBeforeDiscount) {
        this.subTotalBeforeDiscount = subTotalBeforeDiscount;
    }

    public Float getDiscountRate() {
        return discountRate;
    }

    public Quotation discountRate(Float discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Float discountRate) {
        this.discountRate = discountRate;
    }

    public Float getDiscountAmount() {
        return discountAmount;
    }

    public Quotation discountAmount(Float discountAmount) {
        this.discountAmount = discountAmount;
        return this;
    }

    public void setDiscountAmount(Float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public Quotation subTotal(Float subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Float getVatAmount() {
        return vatAmount;
    }

    public Quotation vatAmount(Float vatAmount) {
        this.vatAmount = vatAmount;
        return this;
    }

    public void setVatAmount(Float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Float getTotal() {
        return total;
    }

    public Quotation total(Float total) {
        this.total = total;
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public Quotation additionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Family getFamily() {
        return family;
    }

    public Quotation family(Family family) {
        this.family = family;
        return this;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Quotation invoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Quotation tenant(Tenant tenant) {
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
        Quotation quotation = (Quotation) o;
        if (quotation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quotation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Quotation{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", docType='" + getDocType() + "'" +
            ", type='" + getType() + "'" +
            ", vatRate=" + getVatRate() +
            ", validityTerms='" + getValidityTerms() + "'" +
            ", acceptionation='" + getAcceptionation() + "'" +
            ", title='" + getTitle() + "'" +
            ", number='" + getNumber() + "'" +
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
