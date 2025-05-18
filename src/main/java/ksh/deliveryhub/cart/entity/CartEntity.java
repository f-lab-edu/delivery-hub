package ksh.deliveryhub.cart.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private CartStatus status;

    private LocalDateTime expireAt;

    private Long userId;

    public void updateStatus(CartStatus status) {
        this.status = status;
    }

    @Builder
    private CartEntity(
        Long id,
        CartStatus status,
        LocalDateTime expireAt,
        Long userId
    ) {
        this.id = id;
        this.status = status;
        this.expireAt = expireAt;
        this.userId = userId;
    }
}
