package com.socialgraph.graph_service.repository;

import com.socialgraph.graph_service.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.*;

public interface UserNodeRepository extends Neo4jRepository<UserNode, UUID> {

    @Query("""
        MATCH (a:User {id: $user1})-[:FRIEND]->(mutual:User)<-[:FRIEND]-(b:User {id: $user2})
        RETURN mutual.id AS id
    """)
    List<UUID> findMutualFriends(UUID user1, UUID user2);

    @Query("""
        MATCH (a:User {id: $user1}), (b:User {id: $user2}),
              p = shortestPath((a)-[:FRIEND*]-(b))
        RETURN length(p) AS hops
    """)
    Integer findShortestPathLength(UUID user1, UUID user2);

    @Query("""
        MATCH (u:User {id: $userId})-[:FRIEND]->(f1)-[:FRIEND]->(f2)
        WHERE NOT (u)-[:FRIEND]->(f2) AND f2.id <> $userId
        RETURN DISTINCT f2.id AS id
        LIMIT 10
    """)
    List<UUID> suggestFriends(UUID userId);
    
    @Query("MATCH (a:User {id: $id1})-[r:FRIEND]->(b:User {id: $id2}) DELETE r")
    void removeFriendship(UUID id1, UUID id2);
    
    @Query("MATCH (u:User {id:$id}) DETACH DELETE u")
    void deleteUserNodeAndRelationships(UUID id);
}