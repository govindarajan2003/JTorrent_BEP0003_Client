package com.govindarajan.jtorrent.network;

/**
 * Represents a remote peer in the swarm.
 * Immutable class holding IP and Port.
 */
public class Peer {
    private final String ip;
    private final int port;

    /**
     * @param ip   IPv4 address (e.g., "192.168.1.1")
     * @param port Port number (e.g., 6881)
     */
    public Peer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
    public String toString(){
        return ip+":"+port;
    }
}
