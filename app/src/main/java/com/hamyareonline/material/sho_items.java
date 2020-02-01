package com.hamyareonline.material;

import android.os.Parcel;
import android.os.Parcelable;

public class sho_items implements Parcelable {

    private int id;
    private String title;
    private String subjcet;
    private String img_adrs;
    private String content;

    public sho_items() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String fa_title) {
        this.title = fa_title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String fa_cnt) {
        this.content = fa_cnt;
    }
    public void setSubjcet(String subjcet) {
        this.subjcet = subjcet;
    }
    public String getImg_adrs() {
        return img_adrs;
    }
    public void setImg_adrs(String en_title) {
        this.img_adrs = en_title;
    }
    @Override
    public String toString() {
        return title;
    }
    public sho_items(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        subjcet = in.readString();
        img_adrs = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(subjcet);
        dest.writeString(img_adrs);
    }

    public static final Creator<sho_items> CREATOR = new Creator<sho_items>() {

        @Override
        public sho_items createFromParcel(Parcel source) {
            return new sho_items(source);
        }

        @Override
        public sho_items[] newArray(int size) {
            return new sho_items[size];
        }

    };
}
