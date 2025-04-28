package ksh.deliveryhub.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Builder
    private UserEntity(
        String email,
        String password,
        String name,
        String phone,
        UserType userType
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userType = userType;
    }
}
