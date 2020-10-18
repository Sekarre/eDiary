package com.ediary.DTO;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {

    private Long id;

    private String title;
    private String content;
    private Date date;

    //Author
    private Long authorId;
    private String authorName;

}
