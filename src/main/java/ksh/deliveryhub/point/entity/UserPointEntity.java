package ksh.deliveryhub.point.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_point")
public class UserPointEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer balance;

    private Long userId;

    public void increaseBalance(int amount) {
        this.balance += amount;
    }

    public void decreaseBalance(Integer amount) {
        this.balance -= amount;
    }

    @Builder
    private UserPointEntity(
        Long id,
        Integer balance,
        Long userId
    ) {
        this.id = id;
        this.balance = balance;
        this.userId = userId;
    }
}
