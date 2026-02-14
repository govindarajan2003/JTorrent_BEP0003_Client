package com.govindarajan.jtorrent.network;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Responsible for creating Handshake request and parsing the Handshake response.
 */
public class Handshake {
    byte[] infoHash;
    byte[] peerId;

    public Handshake(byte[] infoHash, byte[] peerId){
        if(infoHash.length != 20)
            throw new IllegalArgumentException("Info-hash cannot have length other than 20");
        if(peerId.length != 20)
            throw new IllegalArgumentException("Peer-ID must be length 20");
        this.infoHash = infoHash;
        this.peerId = peerId;
    }

    /**
     * Serializes the handshake into a byte array.
     * Format: [19][Protocol String][Reserved(8)][InfoHash(20)][PeerId(20)]
     * @return The 68-byte array ready to be sent.
     */
    public byte[] getBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(68);
        buffer.put((byte) 19);
        buffer.put("BitTorrent protocol".getBytes(StandardCharsets.UTF_8));
        buffer.put(new byte[8]);
        buffer.put(infoHash);
        buffer.put(peerId);
        return buffer.array();
    }

    /**
     * Parses a received handshake from a peer.
     * @param response The 68-byte array received from the network.
     * @return A Handshake object containing the peer's Info Hash and Peer ID.
     * @throws RuntimeException if protocol string or length is invalid.
     */
    public static Handshake parse(byte[] response){
        if(response.length != 68)
            throw new RuntimeException("Invalid handshake length"+ response.length);
        if(response[0] != 19)
            throw new RuntimeException("Unknown protocol length");
        /* ISO_8859_1 ensures that 1 byte equals exactly 1 character,
           which keeps the data clean. UTF-8 may insert garbage data.
         */
        String protocol = new String(response, 1, 19,StandardCharsets.ISO_8859_1);
        if(!"BitTorrent protocol".equals(protocol))
            throw new RuntimeException("Unkown Protocol");
        // CopyOfRange includes 28 and excludes 48
        byte[] infoHash = Arrays.copyOfRange(response, 28, 48);
        byte[] peerId = Arrays.copyOfRange(response,48,68);
        return new Handshake(infoHash, peerId);
    }

    public byte[] getInfoHash() {
        return infoHash;
    }

    public byte[] getPeerId() {
        return peerId;
    }
}
