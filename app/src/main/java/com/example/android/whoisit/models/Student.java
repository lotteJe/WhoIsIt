package com.example.android.whoisit.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Uid;
import io.objectbox.converter.PropertyConverter;

/**
 * Created by lottejespers on 29/12/17.
 */

@Entity
public class Student {

    @Id
    public long id;

    @Uid(5095328987682761616L)
    private String image;
    private String name;

    @Convert(converter = TraitsConverter.class, dbType = String.class)
    private List<String> traits;

    public Student() {
    }

    public Student(long id, String image, String name, List<String> traits) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.traits = traits;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public static class TraitsConverter implements PropertyConverter<List<String>, String> {

        @Override
        public List<String> convertToEntityProperty(String s) {
            if (TextUtils.isEmpty(s))
                return null;
            else {
                String[] items = s.split(",");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < items.length; i++) {
                    list.add(items[i]);
                }
                return list;
            }
        }

        @Override
        public String convertToDatabaseValue(List<String> strings) {
            if (strings != null && strings.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < strings.size(); i++) {
                    if (i > 0) builder.append(",");
                    builder.append(strings.get(i));
                }
                return builder.toString();
            }
            return null;
        }
    }
}
