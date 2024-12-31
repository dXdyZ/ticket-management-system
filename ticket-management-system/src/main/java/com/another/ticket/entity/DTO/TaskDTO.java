package com.another.ticket.entity.DTO;

import com.another.ticket.entity.Priority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    @NotNull
    @Size(min = 5, max = 120, message = "Topic name must maximum 120 character")
    private String topic;

    @NotNull
    @Size(min = 10, message = "Description must minimum 10 character")
    private String description;

    private Priority priority;
}
