package ksh.deliveryhub.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    private String description;

    private String imageUrl;

    private Long userId;

    private Long menuId;

    @Builder
    private Review(
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
