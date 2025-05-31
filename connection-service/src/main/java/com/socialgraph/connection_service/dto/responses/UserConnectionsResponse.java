package com.socialgraph.connection_service.dto.responses;

import java.util.*;

import com.socialgraph.connection_service.model.ConnectedUserList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserConnectionsResponse {
	private UUID userId;
    private List<ConnectedUserList> connections;
}
