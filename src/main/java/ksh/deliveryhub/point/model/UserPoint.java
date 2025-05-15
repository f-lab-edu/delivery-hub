package ksh.deliveryhub.point.model;

import ksh.deliveryhub.point.entity.UserPointEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPoint {

    private Long id;
    private Integer balance;
    private Long userId;

    public static UserPoint from(UserPointEntity userPointEntity) {
        return UserPoint.builder()
            .id(userPointEntity.getId())
            .balance(userPointEntity.getBalance())
            .userId(userPointEntity.getUserId())
            .build();
    }

    public UserPointEntity toEntity() {
        return UserPointEntity.builder()
            .id(getId())
            .balance(getBalance())
            .userId(getUserId())
            .build();
    }
}
