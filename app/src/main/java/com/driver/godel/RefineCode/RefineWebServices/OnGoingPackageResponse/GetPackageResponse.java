package com.driver.godel.RefineCode.RefineWebServices.OnGoingPackageResponse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 24/1/18.
 */

public class GetPackageResponse implements Parcelable
{
    private int response;

    private PackageData data;

    protected GetPackageResponse(Parcel in) {
        response = in.readInt();
        data = in.readParcelable(PackageData.class.getClassLoader());
    }

    public static final Creator<GetPackageResponse> CREATOR = new Creator<GetPackageResponse>() {
        @Override
        public GetPackageResponse createFromParcel(Parcel in) {
            return new GetPackageResponse(in);
        }

        @Override
        public GetPackageResponse[] newArray(int size) {
            return new GetPackageResponse[size];
        }
    };

    public int getResponse ()
    {
        return response;
    }

    public void setResponse (int response)
    {
        this.response = response;
    }

    public PackageData getData() {
        return data;
    }

    public void setData(PackageData data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", data = "+data+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(response);
        dest.writeParcelable(data, flags);
    }
}
