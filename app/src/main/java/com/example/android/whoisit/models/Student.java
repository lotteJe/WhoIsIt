package com.example.android.whoisit.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lottejespers on 29/12/17.
 */

public class Student implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
    private int image;
    private String name;
    private List<String> traits;

    public Student(int image, String name, List<String> traits) {
        this.image = image;
        this.name = name;
        this.traits = traits;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTraits() {
        return traits;
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }

    // Parcelling part
    public Student(Parcel in){
        this.image = in.readInt();
        this.name = in.readString();
        in.readStringList(this.traits);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.image);
        dest.writeString(this.name);
        dest.writeList(this.traits);
    }
}
