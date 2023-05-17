package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 24/1/18.
 */

public class GetPackageDetails implements Parcelable
{
    private String package_type;

    private String handle_by;

    private String final_price;

    private String package_status;

    private String package_description;

    private String pop_image;

    private String payment_status;

    private String feedback;

    private String package_code;

    private String driver_device_token;

    private String created_at;

    private String driver_id;

    private String partner_user_id;

    private String signature;

    private String package_height;

    private String package_depth;

    private String fk_booking_id;

    private String package_quantity;

    private String warehouse_user_id;

    private String pod_image;

    private String sign_name;

    private String estimate_price;

    private String updated_at;

    private String package_est_value;

    private String package_width;

    private String package_id;

    private String send_selected_driver;

    private String package_cancel;

    private String rating;

    private String package_length;

    private String package_weight;

    private String is_cash_on_pickup;
    private String pending_amount;


    private String is_round_trip;
    private String is_first_round_completed;



    protected GetPackageDetails(Parcel in) {
        package_type = in.readString();
        handle_by = in.readString();
        final_price = in.readString();
        package_status = in.readString();
        package_description = in.readString();
        pop_image = in.readString();
        payment_status = in.readString();
        feedback = in.readString();
        package_code = in.readString();
        driver_device_token = in.readString();
        created_at = in.readString();
        driver_id = in.readString();
        partner_user_id = in.readString();
        signature = in.readString();
        package_height = in.readString();
        package_depth = in.readString();
        fk_booking_id = in.readString();
        package_quantity = in.readString();
        warehouse_user_id = in.readString();
        pod_image = in.readString();
        sign_name = in.readString();
        estimate_price = in.readString();
        updated_at = in.readString();
        package_est_value = in.readString();
        package_width = in.readString();
        package_id = in.readString();
        send_selected_driver = in.readString();
        package_cancel = in.readString();
        rating = in.readString();
        package_length = in.readString();
        package_weight = in.readString();
        is_cash_on_pickup = in.readString();
        pending_amount = in.readString();
        is_first_round_completed = in.readString();
        is_round_trip = in.readString();



    }

    public static final Creator<GetPackageDetails> CREATOR = new Creator<GetPackageDetails>() {
        @Override
        public GetPackageDetails createFromParcel(Parcel in) {
            return new GetPackageDetails(in);
        }

        @Override
        public GetPackageDetails[] newArray(int size) {
            return new GetPackageDetails[size];
        }
    };

    public String getIs_cash_on_pickup() {
        return is_cash_on_pickup;
    }

    public void setIs_cash_on_pickup(String is_cash_on_pickup) {
        this.is_cash_on_pickup = is_cash_on_pickup;
    }
    public String getPending_amount() {
        return pending_amount;
    }

    public void setPending_amount(String pending_amount) {
        this.pending_amount = pending_amount;
    }
    public String getPackage_type()
    {
        return package_type;
    }

    public void setPackage_type(String package_type)
    {
        this.package_type = package_type;
    }
    public String getIs_round_trip() {
        return is_round_trip;
    }

    public void setIs_round_trip(String is_round_trip) {
        this.is_round_trip = is_round_trip;
    }

    public String getIs_first_round_completed() {
        return is_first_round_completed;
    }

    public void setIs_first_round_completed(String is_first_round_completed) {
        this.is_first_round_completed = is_first_round_completed;
    }

    public String getPackage_status ()
    {
        return package_status;
    }

    public void setPackage_status (String package_status)
    {
        this.package_status = package_status;
    }

    public String getPackage_description ()
    {
        return package_description;
    }

    public void setPackage_description (String package_description) {
        this.package_description = package_description;
    }

    public String getPayment_status ()
    {
        return payment_status;
    }

    public void setPayment_status (String payment_status)
    {
        this.payment_status = payment_status;
    }

    public String getPackage_code ()
    {
        return package_code;
    }

    public void setPackage_code (String package_code)
    {
        this.package_code = package_code;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getPackage_depth ()
    {
        return package_depth;
    }

    public void setPackage_depth (String package_depth)
    {
        this.package_depth = package_depth;
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

    public void setPackage_quantity (String package_quantity)    {
        this.package_quantity = package_quantity;
    }

    public String getEstimate_price ()
    {
        return estimate_price;
    }

    public void setEstimate_price (String estimate_price)
    {
        this.estimate_price = estimate_price;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getPackage_est_value ()
    {
        return package_est_value;
    }

    public void setPackage_est_value (String package_est_value)    {
        this.package_est_value = package_est_value;
    }

    public String getPackage_width ()
    {
        return package_width;
    }

    public void setPackage_width (String package_width)
    {
        this.package_width = package_width;
    }

    public String getPackage_id ()
    {
        return package_id;
    }

    public void setPackage_id (String package_id)
    {
        this.package_id = package_id;
    }

    public String getSend_selected_driver ()
    {
        return send_selected_driver;
    }

    public void setSend_selected_driver (String send_selected_driver)    {
        this.send_selected_driver = send_selected_driver;
    }

    public String getPackage_length ()
    {
        return package_length;
    }

    public void setPackage_length (String package_length)
    {
        this.package_length = package_length;
    }

    public String getPackage_weight ()
    {
        return package_weight;
    }

    public void setPackage_weight (String package_weight)
    {
        this.package_weight = package_weight;
    }

    public String getHandle_by() {
        return handle_by;
    }

    public void setHandle_by(String handle_by) {
        this.handle_by = handle_by;
    }

    public String getFinal_price() {
        return final_price;
    }

    public void setFinal_price(String final_price) {
        this.final_price = final_price;
    }

    public String getPop_image() {
        return pop_image;
    }

    public void setPop_image(String pop_image) {
        this.pop_image = pop_image;
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

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public void setPartner_user_id(String partner_user_id) {
        this.partner_user_id = partner_user_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPackage_height() {
        return package_height;
    }

    public void setPackage_height(String package_height) {
        this.package_height = package_height;
    }

    public String getWarehouse_user_id() {
        return warehouse_user_id;
    }

    public void setWarehouse_user_id(String warehouse_user_id) {
        this.warehouse_user_id = warehouse_user_id;
    }

    public String getPod_image() {
        return pod_image;
    }

    public void setPod_image(String pod_image) {
        this.pod_image = pod_image;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public String getPackage_cancel() {
        return package_cancel;
    }

    public void setPackage_cancel(String package_cancel) {
        this.package_cancel = package_cancel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [handle_by = "+handle_by+", final_price = "+final_price+", package_status = "+package_status+", package_description = "+package_description+", pop_image = "+pop_image+", payment_status = "+payment_status+", feedback = "+feedback+", package_code = "+package_code+", driver_device_token = "+driver_device_token+", created_at = "+created_at+", driver_id = "+driver_id+", partner_user_id = "+partner_user_id+", signature = "+signature+", package_height = "+package_height+", package_depth = "+package_depth+", fk_booking_id = "+fk_booking_id+", package_quantity = "+package_quantity+", warehouse_user_id = "+warehouse_user_id+", pod_image = "+pod_image+", sign_name = "+sign_name+", estimate_price = "+estimate_price+", updated_at = "+updated_at+", package_est_value = "+package_est_value+", package_width = "+package_width+", package_id = "+package_id+", send_selected_driver = "+send_selected_driver+", package_cancel = "+package_cancel+", rating = "+rating+", package_length = "+package_length+", package_weight = "+package_weight+", is_first_round_completed = "+is_first_round_completed+", is_round_trip = "+is_round_trip+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(package_type);
        dest.writeString(handle_by);
        dest.writeString(final_price);
        dest.writeString(package_status);
        dest.writeString(package_description);
        dest.writeString(pop_image);
        dest.writeString(payment_status);
        dest.writeString(feedback);
        dest.writeString(package_code);
        dest.writeString(driver_device_token);
        dest.writeString(created_at);
        dest.writeString(driver_id);
        dest.writeString(partner_user_id);
        dest.writeString(signature);
        dest.writeString(package_height);
        dest.writeString(package_depth);
        dest.writeString(fk_booking_id);
        dest.writeString(package_quantity);
        dest.writeString(warehouse_user_id);
        dest.writeString(pod_image);
        dest.writeString(sign_name);
        dest.writeString(estimate_price);
        dest.writeString(updated_at);
        dest.writeString(package_est_value);
        dest.writeString(package_width);
        dest.writeString(package_id);
        dest.writeString(send_selected_driver);
        dest.writeString(package_cancel);
        dest.writeString(rating);
        dest.writeString(package_length);
        dest.writeString(package_weight);
        dest.writeString(is_cash_on_pickup);
        dest.writeString(pending_amount);
        dest.writeString(is_first_round_completed);
        dest.writeString(is_round_trip);

    }
}
