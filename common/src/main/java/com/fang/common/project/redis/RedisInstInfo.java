package com.fang.common.project.redis;

/**
 *
 */
public class RedisInstInfo {
    private String room;
    private String role;
    private String addressAndPort;

    public RedisInstInfo(String room, String role, String addressAndPort) {
        this.room = room;
        this.role = role;
        this.addressAndPort = addressAndPort;
    }

    public String getRoom() {
        return room;
    }

    public String getRole() {
        return role;
    }

    public String getAddressAndPort() {
        return addressAndPort;
    }
}
