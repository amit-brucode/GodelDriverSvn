package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by baljit.kaur on 6/8/2018.
 */

public class Package_tracking_details implements Parcelable {
    private String id;

    private String updated_at;

    private String status;

    private String warehouse_id;

    private String package_code;

    private String created_at;

    private String pof_image;

    private String driver_id;

    private String assign_to;

    private String vehicle_id;

    private String pop_image;

    private String assigned_on;

    private String comments;

    private String pod_image;

    protected Package_tracking_details(Parcel in) {
        id = in.readString();
        updated_at = in.readString();
        status = in.readString();
        warehouse_id = in.readString();
        package_code = in.readString();
        created_at = in.readString();
        pof_image = in.readString();
        driver_id = in.readString();
        assign_to = in.readString();
        vehicle_id = in.readString();
        pop_image = in.readString();
        assigned_on = in.readString();
        comments = in.readString();
        pod_image = in.readString();
    }

    public static final Creator<Package_tracking_details> CREATOR = new Creator<Package_tracking_details>() {
        @Override
        public Package_tracking_details createFromParcel(Parcel in) {
            return new Package_tracking_details(in);
        }

        @Override
        public Package_tracking_details[] newArray(int size) {
            return new Package_tracking_details[size];
        }
    };

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
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

    public String getDriver_id ()
    {
        return driver_id;
    }

    public void setDriver_id (String driver_id)
    {
        this.driver_id = driver_id;
    }

    public String getAssign_to ()
    {
        return assign_to;
    }

    public void setAssign_to (String assign_to)
    {
        this.assign_to = assign_to;
    }

    public String getVehicle_id ()
    {
        return vehicle_id;
    }

    public void setVehicle_id (String vehicle_id)
    {
        this.vehicle_id = vehicle_id;
    }

    public String getPop_image ()
    {
        return pop_image;
    }

    public void setPop_image (String pop_image)
    {
        this.pop_image = pop_image;
    }

    public String getAssigned_on ()
    {
        return assigned_on;
    }

    public void setAssigned_on (String assigned_on)
    {
        this.assigned_on = assigned_on;
    }

    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getPof_image() {
        return pof_image;
    }

    public void setPof_image(String pof_image) {
        this.pof_image = pof_image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPod_image() {
        return pod_image;
    }

    public void setPod_image(String pod_image) {
        this.pod_image = pod_image;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", updated_at = "+updated_at+", status = "+status+", warehouse_id = "+warehouse_id+", package_code = "+package_code+", created_at = "+created_at+", pof_image = "+pof_image+", driver_id = "+driver_id+", assign_to = "+assign_to+", vehicle_id = "+vehicle_id+", pop_image = "+pop_image+", assigned_on = "+assigned_on+", comments = "+comments+", pod_image = "+pod_image+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(updated_at);
        dest.writeString(status);
        dest.writeString(warehouse_id);
        dest.writeString(package_code);
        dest.writeString(created_at);
        dest.writeString(pof_image);
        dest.writeString(driver_id);
        dest.writeString(assign_to);
        dest.writeString(vehicle_id);
        dest.writeString(pop_image);
        dest.writeString(assigned_on);
        dest.writeString(comments);
        dest.writeString(pod_image);
    }
}

