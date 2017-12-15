package michael_juarez.foodtrucksandmore.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 11/10/2017.
 */

public class FoodSpot implements Comparable<FoodSpot>, Serializable{

    private String description;
    private String address;
    private String city;
    private List<String> close_time;
    private String email;
    private String food_type;
    private String image_location;
    private String menu_location;
    private String name;
    private List<String> open_time;
    private String state;
    private String website;
    private String zip;

    private double lat;
    private double lng;
    private String type;
    private double distance;
    private String distanceText;
    private boolean favorite;
    private int id_local;
    private String key_geofire;

    public FoodSpot(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getClose_time() {
        return close_time;
    }

    public void setClose_time(List<String> close_time) {
        this.close_time = close_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }

    public String getMenu_location() {
        return menu_location;
    }

    public void setMenu_location(String menu_location) {
        this.menu_location = menu_location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOpen_time() {
        return open_time;
    }

    public void setOpen_time(List<String> open_time) {
        this.open_time = open_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double longitude) {
        this.lng = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public void setDistanceText(String distanceText) {
        this.distanceText = distanceText;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getKey_geofire() {
        return key_geofire;
    }

    public void setKey_geofire(String key_geofire) {
        this.key_geofire = key_geofire;
    }

    @Override
    public int compareTo(FoodSpot fs) {
        if (this.getDistance() == fs.getDistance())
            return 0;

        return (this.getDistance() > fs.getDistance())
                ? 1
                : -1;
    }


    /*@Override
    public int describeContents() {
        return 0;
    }

    private FoodSpot(Parcel in) {
        address = in.readString();
        city = in.readString();
        close_time = in.readStringArray();
        email = in.readString();
        food_type = in.readString();
        image_location = in.readString();
        menu_location = in.readString();
        name = in.readString();
        state = in.readString();
        website = in.readString();
        zip = in.readString();

        lat = in.readDouble();
        lng = in.readDouble();
        owner_id = in.readString();
        type = in.readString();
        distance = in.readDouble();
        distanceText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(address);
        out.writeString(city);
        out.writeString(email);
        out.writeString(food_type);
        out.writeString(image_location);
        out.writeString(menu_location);
        out.writeString(name);
        out.writeString(state);
        out.writeString(website);
        out.writeString(zip);

        out.writeDouble(lat);
        out.writeDouble(lng);
        out.writeString(owner_id);
        out.writeString(type);
        out.writeDouble(distance);
        out.writeString(distanceText);
    }

    public static final Parcelable.Creator<FoodSpot> CREATOR = new Parcelable.Creator<FoodSpot>() {
        public FoodSpot createFromParcel(Parcel in) {
            return new FoodSpot(in);
        }

        public FoodSpot[] newArray(int size) {
            return new FoodSpot[size];
        }
    };*/
}
