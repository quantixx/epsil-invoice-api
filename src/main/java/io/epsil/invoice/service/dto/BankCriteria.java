package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Bank entity. This class is used in BankResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /banks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BankCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter bankName;

    private StringFilter agencyName;

    private StringFilter bankAccount;

    private StringFilter iban;

    private StringFilter bic;

    private StringFilter phoneArea;

    private StringFilter phoneNumber;

    private LongFilter addressId;

    public BankCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBankName() {
        return bankName;
    }

    public void setBankName(StringFilter bankName) {
        this.bankName = bankName;
    }

    public StringFilter getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(StringFilter agencyName) {
        this.agencyName = agencyName;
    }

    public StringFilter getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(StringFilter bankAccount) {
        this.bankAccount = bankAccount;
    }

    public StringFilter getIban() {
        return iban;
    }

    public void setIban(StringFilter iban) {
        this.iban = iban;
    }

    public StringFilter getBic() {
        return bic;
    }

    public void setBic(StringFilter bic) {
        this.bic = bic;
    }

    public StringFilter getPhoneArea() {
        return phoneArea;
    }

    public void setPhoneArea(StringFilter phoneArea) {
        this.phoneArea = phoneArea;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "BankCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bankName != null ? "bankName=" + bankName + ", " : "") +
                (agencyName != null ? "agencyName=" + agencyName + ", " : "") +
                (bankAccount != null ? "bankAccount=" + bankAccount + ", " : "") +
                (iban != null ? "iban=" + iban + ", " : "") +
                (bic != null ? "bic=" + bic + ", " : "") +
                (phoneArea != null ? "phoneArea=" + phoneArea + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
            "}";
    }

}
