package ksh.deliveryhub.rider.model;

import ksh.deliveryhub.rider.entity.RiderEntity;
import ksh.deliveryhub.rider.entity.RiderStatus;
import ksh.deliveryhub.user.entity.UserEntity;
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
    private RiderStatus status;

    public static Rider from(RiderEntity riderEntity) {
        return Rider.builder()
            .id(riderEntity.getId())
            .email(riderEntity.getEmail())
            .password(riderEntity.getPassword())
            .name(riderEntity.getName())
            .phone(riderEntity.getPhone())
            .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
            .id(getId())
            .email(getEmail())
            .password(getPassword())
            .name(getName())
            .phone(getPhone())
            .build();
    }
}
