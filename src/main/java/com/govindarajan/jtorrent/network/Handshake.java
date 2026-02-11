package com.govindarajan.jtorrent.network;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
    // Structure: [Protocol(19)][Reserved(8)][InfoHash(20)][PeerId(20)]
    public byte[] getBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(68);
        buffer.put((byte) 19);
        buffer.put("BitTorrent protocol".getBytes(StandardCharsets.UTF_8));
        buffer.put(new byte[8]);
        buffer.put(infoHash);
        buffer.put(peerId);
        return buffer.array();
    }
    // Structure: [Protocol(19)][Reserved(8)][InfoHash(20)][PeerId(20)]
    public static Handshake parse(byte[] response){
        if(response.length != 68)
            throw new RuntimeException("Invalid handshake length"+ response.length);
        if(response[0] != 19)
            throw new RuntimeException("Unknown protocol length");
        // In networking, ISO_8859_1 is a very "safe" encoding.
        // It maps bytes 1-to-1 with characters.
        // If you used UTF-8, and there was some weird binary garbage
        // immediately after the name, Java might get confused and try
        // to combine bytes. ISO_8859_1 ensures that 1 byte equals exactly 1 character,
        // which keeps the data clean.
        String protocol = new String(response, 1, 19,StandardCharsets.ISO_8859_1);
        if(!"BitTorrent protocol".equals(protocol))
            throw new RuntimeException("Unkown Protocol");
        // 28(inclusive) 48(excluded)
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
