package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 18/7/17.
 */

public class Payment_details
{
    private String amount;

    private String id;

    private String updated_at;

    private String fk_package_id;

    private String txn_id;

    private String stripe_token;

    private String created_at;

    private String payment_method;

    private String payment_status;

    private String currency;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }


    public String getFk_package_id ()
    {
        return fk_package_id;
    }

    public void setFk_package_id (String fk_package_id)
    {
        this.fk_package_id = fk_package_id;
    }

    public String getTxn_id ()
    {
        return txn_id;
    }

    public void setTxn_id (String txn_id)
    {
        this.txn_id = txn_id;
    }

    public String getStripe_token ()
    {
        return stripe_token;
    }

    public void setStripe_token (String stripe_token)
    {
        this.stripe_token = stripe_token;
    }

    public String getUpdated_at()
    {
        return updated_at;
    }

    public void setUpdated_at(String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(String created_at)
    {
        this.created_at = created_at;
    }

    public String getPayment_method ()
    {
        return payment_method;
    }

    public void setPayment_method (String payment_method)
    {
        this.payment_method = payment_method;
    }

    public String getPayment_status ()
    {
        return payment_status;
    }

    public void setPayment_status (String payment_status)
    {
        this.payment_status = payment_status;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", id = "+id+", updated_at = "+updated_at+", fk_package_id = "+fk_package_id+", txn_id = "+txn_id+", stripe_token = "+stripe_token+", created_at = "+created_at+", payment_method = "+payment_method+", payment_status = "+payment_status+", currency = "+currency+"]";
    }
}

