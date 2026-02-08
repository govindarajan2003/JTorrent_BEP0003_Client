package com.govindarajan.jtorrent.network;

import java.util.List;

public interface TrackerClient {
    /**
     * Connects to the tracker and returns a list of peers.
     * @param announceUrl The URL of the tracker
     * @param infoHash The 20-byte SHA-1 hash of the torrent info
     * @param fileLength Total length of the file (for reporting "left")
     * @return List of Peer objects
     */
    List<Peer> getPeers(String announceUrl, byte[] infoHash, Long fileLength) throws Exception;
}
