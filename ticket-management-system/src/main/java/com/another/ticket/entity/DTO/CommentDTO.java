package com.another.ticket.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String username;
    private String commentText;
    private String createDate;
    private String email;
    private Long chatId;
}
