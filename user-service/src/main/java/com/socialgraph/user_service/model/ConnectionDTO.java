package com.socialgraph.user_service.model;

import java.time.LocalDateTime;
import java.util.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {
    private UUID connectionId;
    private UUID requesterId;
    private UUID receiverId;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime updatedAt;
}
