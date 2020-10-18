package com.ediary.DTO;

import com.ediary.domain.Message;
import lombok.*;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    private Long id;
    private String title;
    private String content;


    private Message.Status status;

    //Senders
    private Long sendersId;
    private String sendersName;

    //Readers
    private List<String> readersName;
    private List<Long> readersId;
}
