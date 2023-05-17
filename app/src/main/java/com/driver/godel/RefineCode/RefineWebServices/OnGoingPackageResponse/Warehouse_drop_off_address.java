package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 26/2/18.
 */

public class Warehouse_drop_off_address implements Parcelable
{
    private String id;

    private String state_id;

    private String phone;

    private String updated_at;

    private String address;

    private String name;

    private String created_at;

    private String longtitude;

    private String latitude;

    private String user_email;

    protected Warehouse_drop_off_address(Parcel in) {
        id = in.readString();
        state_id = in.readString();
        phone = in.readString();
        updated_at = in.readString();
        address = in.readString();
        name = in.readString();
        created_at = in.readString();
        longtitude = in.readString();
        latitude = in.readString();
        user_email = in.readString();
    }

    public static final Creator<Warehouse_drop_off_address> CREATOR = new Creator<Warehouse_drop_off_address>() {
        @Override
        public Warehouse_drop_off_address createFromParcel(Parcel in) {
            return new Warehouse_drop_off_address(in);
        }

        @Override
        public Warehouse_drop_off_address[] newArray(int size) {
            return new Warehouse_drop_off_address[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(state_id);
        dest.writeString(phone);
        dest.writeString(updated_at);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeString(longtitude);
        dest.writeString(latitude);
        dest.writeString(user_email);
    }
}
