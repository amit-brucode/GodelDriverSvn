package com.driver.godel.response;

/**
 * Created by QSYS\simarjot.singh on 30/6/17.
 */

public class Package_details
{
    private String package_height;

    private String handle_by;

    private String fk_booking_id;

    private String package_quantity;

    private String package_status;

    private Payment_details payment_details;

    private String feedback;

    private String updated_at;

    private String driver_device_token;

    private String created_at;

    private String package_id;

    private String package_width;

    private String driver_id;

    private String rating;

    private String package_weight;

    private String signature;

    private String sign_name;

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public String getSign_name()
    {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public String getHandle_by() {
        return handle_by;
    }

    public void setHandle_by(String handle_by) {
        this.handle_by = handle_by;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public Payment_details getPayment_details()
    {
        return payment_details;
    }

    public void setPayment_details(Payment_details payment_details) {
        this.payment_details = payment_details;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDriver_device_token() {
        return driver_device_token;
    }

    public void setDriver_device_token(String driver_device_token) {
        this.driver_device_token = driver_device_token;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPackage_height ()
    {
        return package_height;
    }

    public void setPackage_height (String package_height)
    {
        this.package_height = package_height;
    }

    public String getFk_booking_id ()
    {
        return fk_booking_id;
    }

    public void setFk_booking_id (String fk_booking_id)
    {
        this.fk_booking_id = fk_booking_id;
    }

    public String getPackage_quantity ()
    {
        return package_quantity;
    }

    public void setPackage_quantity (String package_quantity)
    {
        this.package_quantity = package_quantity;
    }


    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }


    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getPackage_id ()
    {
        return package_id;
    }

    public void setPackage_id (String package_id)
    {
        this.package_id = package_id;
    }

    public String getPackage_width ()
    {
        return package_width;
    }

    public void setPackage_width (String package_width)
    {
        this.package_width = package_width;
    }

    public String getDriver_id ()
    {
        return driver_id;
    }

    public void setDriver_id (String driver_id)
    {
        this.driver_id = driver_id;
    }


    public String getPackage_weight ()
    {
        return package_weight;
    }

    public void setPackage_weight (String package_weight)
    {
        this.package_weight = package_weight;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [package_height = "+package_height+", handle_by = "+handle_by+", fk_booking_id = "+fk_booking_id+", package_quantity = "+package_quantity+", package_status = "+package_status+", payment_details = "+payment_details+", feedback = "+feedback+", updated_at = "+updated_at+", driver_device_token = "+driver_device_token+", created_at = "+created_at+", package_id = "+package_id+", package_width = "+package_width+", driver_id = "+driver_id+", rating = "+rating+", package_weight = "+package_weight+"]";
    }
}

