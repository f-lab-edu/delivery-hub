package ksh.deliveryhub.rider.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class Location {

    private String city;
    private String district;
    private String subDistrict;

    public static Location of(String city, String district, String subDistrict) {
        return Location.builder()
            .city(city)
            .district(district)
            .subDistrict(subDistrict)
            .build();
    }

    @Builder
    private Location(String city, String district, String subDistrict) {
        this.city = city;
        this.district = district;
        this.subDistrict = subDistrict;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(city, location.city) && Objects.equals(district, location.district) && Objects.equals(subDistrict, location.subDistrict);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, district, subDistrict);
    }
}
