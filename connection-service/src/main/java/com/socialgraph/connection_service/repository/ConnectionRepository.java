package com.socialgraph.connection_service.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.socialgraph.connection_service.model.Connection;
import com.socialgraph.connection_service.model.ConnectionStatus;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
	Optional<Connection> findByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);

	Optional<Connection> findByRequesterIdAndReceiverIdOrReceiverIdAndRequesterId(UUID requesterId, UUID receiverId,
			UUID receiverId2, UUID requesterId2);

	List<Connection> findAllByStatusAndRequesterIdOrStatusAndReceiverId(ConnectionStatus status1, UUID userId1,
			ConnectionStatus status2, UUID userId2);

	List<Connection> findByReceiverIdAndStatus(UUID receiverId, ConnectionStatus status);

	List<Connection> findByRequesterIdAndStatus(UUID requesterId, ConnectionStatus status);

	void deleteByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);

	int countByReceiverIdAndStatus(UUID receiverId, ConnectionStatus status);

	Optional<Connection> findByRequesterIdAndReceiverIdAndStatus(UUID requesterId, UUID receiverId,
			ConnectionStatus status);

//    @Modifying
//    @Query(value="DELETE FROM connections.connections c WHERE c.requester_id = :userId OR c.receiver_id = :userId",nativeQuery=true)
//    void deleteAllByUserId(@Param("userId") UUID userId);
	@Modifying
	@Query(value = "DELETE FROM connections c WHERE c.requester_id = :userId OR c.receiver_id = :userId", nativeQuery = true)
	void deleteAllByUserId(@Param("userId") UUID userId);
}
