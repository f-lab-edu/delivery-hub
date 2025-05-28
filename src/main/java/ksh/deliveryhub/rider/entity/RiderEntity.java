package ksh.deliveryhub.rider.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rider")
public class RiderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phone;

    private String location;

    @Enumerated(EnumType.STRING)
    private RiderStatus status;

    public void startWork(String location) {
        this.status = RiderStatus.IDLE;
        this.location = location;
    }

    @Builder
    private RiderEntity(
        Long id,
        String email,
        String password,
        String name,
        String phone,
        String location,
        RiderStatus status
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.status = status;
    }
}
