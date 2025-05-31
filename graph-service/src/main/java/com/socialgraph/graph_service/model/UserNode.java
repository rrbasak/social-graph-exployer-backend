package com.socialgraph.graph_service.model;


import org.springframework.data.neo4j.core.schema.*;
import java.util.*;

@Node("User")
public class UserNode {

    @Id
    private UUID id;

    @Relationship(type = "FRIEND", direction = Relationship.Direction.OUTGOING)
    private Set<UserNode> friends = new HashSet<>();

    public UserNode() {}

    public UserNode(UUID id) {
        this.id = id;
    }

    public UUID getId() { return id; }

    public Set<UserNode> getFriends() { return friends; }

    public void addFriend(UserNode friend) {
        this.friends.add(friend);
    }

    public void removeFriend(UserNode friend) {
        this.friends.remove(friend);
    }
}
