package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;

import android.os.Parcel;
import android.os.Parcelable;

public class User_details implements Parcelable
{
    private String is_verified_by_admin;

    private String user_unique_id;

    private String remember_token;

    private String vehicle_type;

    private String user_app_notification;

    private String user_email;

    private String type;

    private String phone_verify;

    private String password;

    private String day_limit_check;

    private String id;

    private String trading_address;

    private String day_limit;

    private String credit_limit;

    private String company_name;

    private String trading_licence_no;

    private String token;

    private String email_verify;

    private String name;

    private String created_at;

    private String gender;

    private String user_image;

    private String sinch_code;

    private String login_type;

    private String company_registration_no;

    private String user_status;

    private String agent_id;

    private String credit_limit_check;

    private String add_by_agent;

    private String nature_of_business;

    private String updated_at;

    private String validate_code;

    private String source;

    private String user_phone;

    private String dob;

    private String last_name;

    private String secondary_contact_no;

    private String facebook_id;
    private String country_code;

    protected User_details(Parcel in) {
        is_verified_by_admin = in.readString();
        user_unique_id = in.readString();
        remember_token = in.readString();
        vehicle_type = in.readString();
        user_app_notification = in.readString();
        user_email = in.readString();
        type = in.readString();
        phone_verify = in.readString();
        password = in.readString();
        day_limit_check = in.readString();
        id = in.readString();
        trading_address = in.readString();
        day_limit = in.readString();
        credit_limit = in.readString();
        company_name = in.readString();
        trading_licence_no = in.readString();
        token = in.readString();
        email_verify = in.readString();
        name = in.readString();
        created_at = in.readString();
        gender = in.readString();
        user_image = in.readString();
        sinch_code = in.readString();
        login_type = in.readString();
        company_registration_no = in.readString();
        user_status = in.readString();
        agent_id = in.readString();
        credit_limit_check = in.readString();
        add_by_agent = in.readString();
        nature_of_business = in.readString();
        updated_at = in.readString();
        validate_code = in.readString();
        source = in.readString();
        user_phone = in.readString();
        dob = in.readString();
        last_name = in.readString();
        secondary_contact_no = in.readString();
        facebook_id = in.readString();
        country_code=in.readString();

    }

    public static final Creator<User_details> CREATOR = new Creator<User_details>() {
        @Override
        public User_details createFromParcel(Parcel in) {
            return new User_details(in);
        }

        @Override
        public User_details[] newArray(int size) {
            return new User_details[size];
        }
    };

    public String getIs_verified_by_admin() {
        return is_verified_by_admin;
    }

    public void setIs_verified_by_admin(String is_verified_by_admin) {
        this.is_verified_by_admin = is_verified_by_admin;
    }

    public String getUser_unique_id() {
        return user_unique_id;
    }

    public void setUser_unique_id(String user_unique_id) {
        this.user_unique_id = user_unique_id;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getUser_app_notification() {
        return user_app_notification;
    }

    public void setUser_app_notification(String user_app_notification) {
        this.user_app_notification = user_app_notification;
    }
    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }


    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone_verify() {
        return phone_verify;
    }

    public void setPhone_verify(String phone_verify) {
        this.phone_verify = phone_verify;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDay_limit_check() {
        return day_limit_check;
    }

    public void setDay_limit_check(String day_limit_check) {
        this.day_limit_check = day_limit_check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrading_address() {
        return trading_address;
    }

    public void setTrading_address(String trading_address) {
        this.trading_address = trading_address;
    }

    public String getDay_limit() {
        return day_limit;
    }

    public void setDay_limit(String day_limit) {
        this.day_limit = day_limit;
    }

    public String getCredit_limit() {
        return credit_limit;
    }

    public void setCredit_limit(String credit_limit) {
        this.credit_limit = credit_limit;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTrading_licence_no() {
        return trading_licence_no;
    }

    public void setTrading_licence_no(String trading_licence_no) {
        this.trading_licence_no = trading_licence_no;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(String email_verify) {
        this.email_verify = email_verify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getSinch_code() {
        return sinch_code;
    }

    public void setSinch_code(String sinch_code) {
        this.sinch_code = sinch_code;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getCompany_registration_no() {
        return company_registration_no;
    }

    public void setCompany_registration_no(String company_registration_no) {
        this.company_registration_no = company_registration_no;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getCredit_limit_check() {
        return credit_limit_check;
    }

    public void setCredit_limit_check(String credit_limit_check) {
        this.credit_limit_check = credit_limit_check;
    }

    public String getAdd_by_agent() {
        return add_by_agent;
    }

    public void setAdd_by_agent(String add_by_agent) {
        this.add_by_agent = add_by_agent;
    }

    public String getNature_of_business() {
        return nature_of_business;
    }

    public void setNature_of_business(String nature_of_business) {
        this.nature_of_business = nature_of_business;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getValidate_code() {
        return validate_code;
    }

    public void setValidate_code(String validate_code) {
        this.validate_code = validate_code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSecondary_contact_no() {
        return secondary_contact_no;
    }

    public void setSecondary_contact_no(String secondary_contact_no) {
        this.secondary_contact_no = secondary_contact_no;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(is_verified_by_admin);
        dest.writeString(user_unique_id);
        dest.writeString(remember_token);
        dest.writeString(vehicle_type);
        dest.writeString(user_app_notification);
        dest.writeString(user_email);
        dest.writeString(type);
        dest.writeString(phone_verify);
        dest.writeString(password);
        dest.writeString(day_limit_check);
        dest.writeString(id);
        dest.writeString(trading_address);
        dest.writeString(day_limit);
        dest.writeString(credit_limit);
        dest.writeString(company_name);
        dest.writeString(trading_licence_no);
        dest.writeString(token);
        dest.writeString(email_verify);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(gender);
        dest.writeString(user_image);
        dest.writeString(sinch_code);
        dest.writeString(login_type);
        dest.writeString(company_registration_no);
        dest.writeString(user_status);
        dest.writeString(agent_id);
        dest.writeString(credit_limit_check);
        dest.writeString(add_by_agent);
        dest.writeString(nature_of_business);
        dest.writeString(updated_at);
        dest.writeString(validate_code);
        dest.writeString(source);
        dest.writeString(user_phone);
        dest.writeString(dob);
        dest.writeString(last_name);
        dest.writeString(secondary_contact_no);
        dest.writeString(facebook_id);
        dest.writeString(country_code);
    }
}
