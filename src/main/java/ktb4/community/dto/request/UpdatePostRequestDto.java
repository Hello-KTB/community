package ktb4.community.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdatePostRequestDto {
    private String title;
    private String content;
    private String image;
}
