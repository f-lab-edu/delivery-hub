package ksh.deliveryhub.rider.model;

import ksh.deliveryhub.rider.entity.RiderEntity;
import ksh.deliveryhub.rider.entity.RiderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Rider {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String location;
    private RiderStatus status;

    public static Rider from(RiderEntity riderEntity) {
        return Rider.builder()
            .id(riderEntity.getId())
            .email(riderEntity.getEmail())
            .password(riderEntity.getPassword())
            .name(riderEntity.getName())
            .phone(riderEntity.getPhone())
            .location(riderEntity.getLocation())
            .status(riderEntity.getStatus())
            .build();
    }

    public RiderEntity toEntity() {
        return RiderEntity.builder()
            .id(getId())
            .email(getEmail())
            .password(getPassword())
            .name(getName())
            .phone(getPhone())
            .location(getLocation())
            .status(getStatus())
            .build();
    }
}
