package com.driver.godel.response.getDriverDetails;

/**
 * Created by root on 23/3/18.
 */

public class Data
{
    private String message;

    private String id;

    private String driver_unique_id;

    private String code;

    private String driver_image;

    private String driver_phone;

    private String vehicle_color;

    private String vehicle_number;

    private String driver_app_notification;

    private String vehicle_type;

    private String driver_name;

    private String vehicle_ownership;

    private String vehicle_id_fk;

    private String driver_email;

    private String vehicle_name;

    public String getDriver_device_id() {
        return driver_device_id;
    }

    public void setDriver_device_id(String driver_device_id) {
        this.driver_device_id = driver_device_id;
    }

    public String getDriver_unique_id() {
        return driver_unique_id;
    }

    public void setDriver_unique_id(String driver_unique_id) {
        this.driver_unique_id = driver_unique_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String driver_device_id;

    public String getDriver_active_status() {
        return driver_active_status;
    }

    public void setDriver_active_status(String driver_active_status) {
        this.driver_active_status = driver_active_status;
    }

    private String driver_active_status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDriver_image ()
    {
        return driver_image;
    }

    public void setDriver_image (String driver_image)
    {
        this.driver_image = driver_image;
    }

    public String getDriver_phone ()
    {
        return driver_phone;
    }

    public void setDriver_phone (String driver_phone)
    {
        this.driver_phone = driver_phone;
    }

    public String getVehicle_color ()
    {
        return vehicle_color;
    }

    public void setVehicle_color (String vehicle_color)
    {
        this.vehicle_color = vehicle_color;
    }

    public String getVehicle_number ()
    {
        return vehicle_number;
    }

    public void setVehicle_number (String vehicle_number)
    {
        this.vehicle_number = vehicle_number;
    }

    public String getDriver_app_notification ()
    {
        return driver_app_notification;
    }

    public void setDriver_app_notification (String driver_app_notification)
    {
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

    public void setVehicle_ownership (String vehicle_ownership)
    {
        this.vehicle_ownership = vehicle_ownership;
    }

    public String getVehicle_id_fk ()
    {
        return vehicle_id_fk;
    }

    public void setVehicle_id_fk (String vehicle_id_fk)
    {
        this.vehicle_id_fk = vehicle_id_fk;
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

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", driver_image = "+driver_image+", code = "+code+", driver_unique_id = "+driver_unique_id+", driver_phone = "+driver_phone+", vehicle_color = "+vehicle_color+", vehicle_number = "+vehicle_number+", driver_app_notification = "+driver_app_notification+", vehicle_type = "+vehicle_type+", driver_name = "+driver_name+", vehicle_ownership = "+vehicle_ownership+", vehicle_id_fk = "+driver_device_id+", driver_email = "+driver_email+", vehicle_name = "+vehicle_name+"]";
    }
}
