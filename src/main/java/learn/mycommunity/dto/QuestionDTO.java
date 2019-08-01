package learn.mycommunity.dto;

import learn.mycommunity.model.User;
import lombok.Data;

/**
 * @author nauh
 */
@Data
public class QuestionDTO {
    private Integer id, creator, viewCount, commentCount, likeCount;
    private String title, description, tag;
    private Long gmtCreate, gmtModified;
    private User user;
}
