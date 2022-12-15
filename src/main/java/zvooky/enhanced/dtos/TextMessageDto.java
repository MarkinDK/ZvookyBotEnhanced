package zvooky.enhanced.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TextMessageDto {
    private String chat_id;
    private String text;
}
