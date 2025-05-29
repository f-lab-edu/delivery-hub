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

    @Enumerated(EnumType.STRING)
    private RiderStatus status;

    public void startWork() {
        this.status = RiderStatus.IDLE;
    }

    @Builder
    private RiderEntity(
        Long id,
        String email,
        String password,
        String name,
        String phone,
        Location location,
        RiderStatus status
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.status = status;
    }
}
