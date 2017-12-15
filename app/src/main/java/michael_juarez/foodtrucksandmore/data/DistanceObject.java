package michael_juarez.foodtrucksandmore.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by user on 11/12/2017.
 */

public class DistanceObject {

    @SerializedName("destination_addresses")
    @Expose
    private List<String> destination_addresses;

    @SerializedName("origin_addresses")
    @Expose
    private List<String> origin_addresses;

    @SerializedName("rows")
    @Expose
    private List<DistanceObjectNoName> rows;

    @SerializedName("status")
    @Expose
    private String status;

    public DistanceObject() {
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public List<DistanceObjectNoName> getRows() {
        return rows;
    }

    public void setRows(List<DistanceObjectNoName> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class DistanceObjectNoName {
        @SerializedName("elements")
        @Expose
        private List<Elements> elements;

        public DistanceObjectNoName() {
        }

        public List<Elements> getElements() {
            return elements;
        }

        public void setElements(List<Elements> elements) {
            this.elements = elements;
        }


    }

    public class Elements {

        @SerializedName("distance")
        @Expose
        private Distance distance;

        @SerializedName("duration")
        @Expose
        private Duration duration;

        @SerializedName("status")
        @Expose
        private String status;

        public Elements() {
        }

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class Distance {
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private double value;

        public Distance() {
        }

        ;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    public class Duration {
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private double value;

        public Duration() {
        }

        ;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }


}
