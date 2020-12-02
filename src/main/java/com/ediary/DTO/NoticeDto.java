package com.ediary.DTO;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {

    private Long id;

    @Size(min = 2, max = 20)
    private String title;

    @Size(min = 3, max = 255)
    private String content;
    private Date date;

    //Author
    private Long authorId;
    private String authorName;

}
