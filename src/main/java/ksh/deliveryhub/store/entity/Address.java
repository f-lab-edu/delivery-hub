package ksh.deliveryhub.store.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
public class Address {

    private String city;
    private String district;
    private String subdistrict;
    private String street;
    private String building;

    public static Address of (String city, String district, String subdistrict, String street, String building) {
        return Address.builder()
            .city(city)
            .district(district)
            .subdistrict(subdistrict)
            .street(street)
            .building(building)
            .build();
    }

    @Override
    public String toString() {
        return city + ", " + district + ", " + subdistrict + ", " + street + ", " + building;
    }

    @Builder
    private Address(String city, String district, String subdistrict, String street, String building) {
        this.city = city;
        this.district = district;
        this.subdistrict = subdistrict;
        this.street = street;
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(district, address.district) && Objects.equals(subdistrict, address.subdistrict) && Objects.equals(street, address.street) && Objects.equals(building, address.building);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, district, subdistrict, street, building);
    }
}
