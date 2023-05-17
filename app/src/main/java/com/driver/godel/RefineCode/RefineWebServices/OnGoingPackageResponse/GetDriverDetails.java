package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 30/1/18.
 */

public class GetDriverDetails implements Parcelable
{
    private String driver_phone;

    private String isVerified;

    private String driver_image;

    private String vehicle_color;

    private String driver_app_notification;

    private String vehicle_type;

    private String driver_name;

    private String vehicle_ownership;

    private String driver_status_change_time;

    private String driver_lat;

    private String driver_email;

    private String vehicle_name;

    private String id;

    private String last_recieve_package_date_time;

    private String vehicle_number;

    private String driver_lng;

    private String driver_status;

    protected GetDriverDetails(Parcel in) {
        driver_phone = in.readString();
        isVerified = in.readString();
        driver_image = in.readString();
        vehicle_color = in.readString();
        driver_app_notification = in.readString();
        vehicle_type = in.readString();
        driver_name = in.readString();
        vehicle_ownership = in.readString();
        driver_status_change_time = in.readString();
        driver_lat = in.readString();
        driver_email = in.readString();
        vehicle_name = in.readString();
        id = in.readString();
        last_recieve_package_date_time = in.readString();
        vehicle_number = in.readString();
        driver_lng = in.readString();
        driver_status = in.readString();
    }

    public static final Creator<GetDriverDetails> CREATOR = new Creator<GetDriverDetails>() {
        @Override
        public GetDriverDetails createFromParcel(Parcel in) {
            return new GetDriverDetails(in);
        }

        @Override
        public GetDriverDetails[] newArray(int size) {
            return new GetDriverDetails[size];
        }
    };

    public String getDriver_phone ()
    {
        return driver_phone;
    }

    public void setDriver_phone (String driver_phone)
    {
        this.driver_phone = driver_phone;
    }

    public String getIsVerified ()
    {
        return isVerified;
    }

    public void setIsVerified (String isVerified)
    {
        this.isVerified = isVerified;
    }

    public String getDriver_image ()
    {
        return driver_image;
    }

    public void setDriver_image (String driver_image)
    {
        this.driver_image = driver_image;
    }

    public String getVehicle_color ()
    {
        return vehicle_color;
    }

    public void setVehicle_color (String vehicle_color)
    {
        this.vehicle_color = vehicle_color;
    }

    public String getDriver_app_notification ()
    {
        return driver_app_notification;
    }

    public void setDriver_app_notification (String driver_app_notification)    {
        this.driver_app_notification = driver_app_notification;
    }

    public String getVehicle_type ()
    {
        return vehicle_type;
    }

    public void setVehicle_type (String vehicle_type)
    {
        this.vehicle_type = vehicle_type;
    }

    public String getDriver_name ()
    {
        return driver_name;
    }

    public void setDriver_name (String driver_name)
    {
        this.driver_name = driver_name;
    }

    public String getVehicle_ownership ()
    {
        return vehicle_ownership;
    }

    public void setVehicle_ownership (String vehicle_ownership)    {
        this.vehicle_ownership = vehicle_ownership;
    }

    public String getDriver_status_change_time ()
    {
        return driver_status_change_time;
    }

    public void setDriver_status_change_time (String driver_status_change_time)    {
        this.driver_status_change_time = driver_status_change_time;
    }

    public String getDriver_lat ()
    {
        return driver_lat;
    }

    public void setDriver_lat (String driver_lat)
    {
        this.driver_lat = driver_lat;
    }

    public String getDriver_email ()
    {
        return driver_email;
    }

    public void setDriver_email (String driver_email)
    {
        this.driver_email = driver_email;
    }

    public String getVehicle_name ()
    {
        return vehicle_name;
    }

    public void setVehicle_name (String vehicle_name)
    {
        this.vehicle_name = vehicle_name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLast_recieve_package_date_time ()
    {
        return last_recieve_package_date_time;
    }

    public void setLast_recieve_package_date_time (String last_recieve_package_date_time)    {
        this.last_recieve_package_date_time = last_recieve_package_date_time;
    }

    public String getVehicle_number ()
    {
        return vehicle_number;
    }

    public void setVehicle_number (String vehicle_number)
    {
        this.vehicle_number = vehicle_number;
    }

    public String getDriver_lng ()
    {
        return driver_lng;
    }

    public void setDriver_lng (String driver_lng)
    {
        this.driver_lng = driver_lng;
    }

    public String getDriver_status ()
    {
        return driver_status;
    }

    public void setDriver_status (String driver_status)
    {
        this.driver_status = driver_status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [driver_phone = "+driver_phone+", isVerified = "+isVerified+", driver_image = "+driver_image+", vehicle_color = "+vehicle_color+", driver_app_notification = "+driver_app_notification+", vehicle_type = "+vehicle_type+", driver_name = "+driver_name+", vehicle_ownership = "+vehicle_ownership+", driver_status_change_time = "+driver_status_change_time+", driver_lat = "+driver_lat+", driver_email = "+driver_email+", vehicle_name = "+vehicle_name+", id = "+id+", last_recieve_package_date_time = "+last_recieve_package_date_time+", vehicle_number = "+vehicle_number+", driver_lng = "+driver_lng+", driver_status = "+driver_status+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driver_phone);
        dest.writeString(isVerified);
        dest.writeString(driver_image);
        dest.writeString(vehicle_color);
        dest.writeString(driver_app_notification);
        dest.writeString(vehicle_type);
        dest.writeString(driver_name);
        dest.writeString(vehicle_ownership);
        dest.writeString(driver_status_change_time);
        dest.writeString(driver_lat);
        dest.writeString(driver_email);
        dest.writeString(vehicle_name);
        dest.writeString(id);
        dest.writeString(last_recieve_package_date_time);
        dest.writeString(vehicle_number);
        dest.writeString(driver_lng);
        dest.writeString(driver_status);
    }
}
