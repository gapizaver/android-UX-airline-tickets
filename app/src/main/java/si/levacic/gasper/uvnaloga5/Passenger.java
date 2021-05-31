package si.levacic.gasper.uvnaloga5;

import android.os.Parcel;
import android.os.Parcelable;

public class Passenger implements Parcelable {
    private String name;
    private String surname;
    private String sex;
    private int ageCategory;

    public Passenger(String name, String surname, String sex, int age_category){
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.ageCategory = age_category;
    }

    public Passenger(Parcel in) {
        this.name = in.readString();
        this.surname = in.readString();
        this.sex = in.readString();
        this.ageCategory = in.readInt();
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getSex() {
        return this.sex;
    }

    public int getAgeCategory() {
        return this.ageCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.sex);
        dest.writeInt(this.ageCategory);
    }

    @SuppressWarnings("unchecked")
    public static final Parcelable.Creator<Passenger> CREATOR = new Parcelable.Creator<Passenger>(){
        public Passenger createFromParcel(Parcel in) {
            return new Passenger(in);
        }

        public Passenger[] newArray(int size) {
            return new Passenger[size];
        }
    };
}
