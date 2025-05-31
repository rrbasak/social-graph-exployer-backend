package com.socialgraph.connection_service.dto.requests;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequest {
	private UUID requesterId;
    private UUID receiverId;
}
