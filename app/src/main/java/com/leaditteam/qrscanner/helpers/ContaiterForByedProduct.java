package com.leaditteam.qrscanner.helpers;

/**
 * Created by leaditteam on 13.04.17.
 */

public class ContaiterForByedProduct {
    String DATE;
    String EMAIL;
    String NAME_USER;
    String PRODUCT;
    int SUM;

    public ContaiterForByedProduct(String DATE, String EMAIL, String NAME_USER, String PRODUCT, int SUM) {
        this.DATE = DATE;
        this.EMAIL = EMAIL;
        this.NAME_USER = NAME_USER;
        this.PRODUCT = PRODUCT;
        this.SUM = SUM;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public void setNAME_USER(String NAME_USER) {
        this.NAME_USER = NAME_USER;
    }

    public void setPRODUCT(String PRODUCT) {
        this.PRODUCT = PRODUCT;
    }

    public void setSUM(int SUM) {
        this.SUM = SUM;
    }

    public String getDATE() {
        return DATE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getNAME_USER() {
        return NAME_USER;
    }

    public String getPRODUCT() {
        return PRODUCT;
    }

    public int getSUM() {
        return SUM;
    }
}
