package ksh.deliveryhub.review.entity;

import jakarta.persistence.*;
import ksh.deliveryhub.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    private String description;

    private String imageUrl;

    private Long userId;

    private Long menuId;

    @Builder
    private ReviewEntity(
        Integer score,
        String description,
        String imageUrl,
        Long userId,
        Long menuId
    ) {
        this.score = score;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.menuId = menuId;
    }
}
