package learn.mycommunity.model;

import lombok.Data;

@Data
public class Question {
    private Integer id, creator, viewCount, commentCount, likeCount;
    private String title, description, tag;
    private Long gmtCreate, gmtModified;
}
