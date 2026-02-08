package com.govindarajan.jtorrent.network;

import com.govindarajan.jtorrent.bencoding.BencodeEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Utility Class for tracker-related operations.
 * Handles: Peer ID generation, URL encoding, Compact Peer Parsing and Hashing.
 */
public class TrackerUtils {

    private TrackerUtils(){}

    /**
     * Generates a random 20-byte Peer ID.
     * Format: -JT0001- followed by 12 random digit
     * @return Peer ID string
     */
    public static String generatePeerId(){
        Random random = new Random();
        StringBuilder peerId = new StringBuilder();

        peerId.append("-JT0001-");
        for(int i = 0; i < 12; i++)
            peerId.append(random.nextInt(10));

        return peerId.toString();
    }

    /**
     * Encodes a byte array into a URL-Safe String
     * Example: [0x12, 0xFF] -> "%12%ff"
     * @param infoHashBytes the raw byte to encode.
     * @return The url-encoded String.
     */
    public static String urlEncodeBytes(byte[] infoHashBytes){
        StringBuilder url = new StringBuilder();
        // byte -> %hexcode
        for(byte b: infoHashBytes){
            url.append("%");
            // %02 format '2' -> length, x-> hex
            url.append(String.format("%02x",b));
        }
        return url.toString();
    }

    /**
     * parses a "Compact" peerlist (Binary Blob).
     * Every 6 bytes represents one peer: [IP(4)] [Port(2)]
     * @param peerBytes The raw bytes of peerlist.
     * @return List of parsed Peer objects containing IP and Port of peers.
     */
    public static List<Peer> parseCompactString(byte[] peerBytes){
        List<Peer> peers = new ArrayList<>();
        if(peerBytes.length % 6 != 0)
            throw new RuntimeException("Invalid peer list length");

        for(int i = 0; i < peerBytes.length; i+=6){
            String ip = String.format(
                    "%d.%d.%d.%d",
                    peerBytes[i] & 0xFF,
                    peerBytes[i+1] & 0xFF,
                    peerBytes[i+2] & 0xFF,
                    peerBytes[i+3] & 0xFF
            );
            int firstPart = peerBytes[i+4] & 0xFF;
            int secondPart = peerBytes[i+5] & 0xFF;
            // (Full Groups of 256 or How many times 256 was crossed) + (Leftover items)
            int port = (firstPart * 256) + secondPart;
            Peer peer = new Peer(ip,port);
            peers.add(peer);
        }
        return peers;
    }

    /**
     * Calculates SHA-1 hash of the Info Dictionary.
     * @param infoMap the 'info' map from the decoded metainfo.
     * @return The 20-byte SHA-1 hash.
     */
    public static byte[] calculateInfoHash(Map<String,Object> infoMap){
        try{
            byte[] infoBytes = BencodeEncoder.encode(infoMap);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] infoHash = md.digest(infoBytes);
            return infoHash;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Gets parsed announce and announce list as a list of String.
     * @param metaInfo
     * @return announce as 1st value and other items follows.
     */
    public static List<String> getAnnounceList(Map<String, Object> metaInfo){
        List<String> urls = new ArrayList<>();

        // Announce string.
        byte[] announceUrl = (byte[]) metaInfo.get("announce");
        urls.add(new String(announceUrl, StandardCharsets.UTF_8));

        // Announce List.
        if(metaInfo.containsKey("announce-list")){
            List<List<byte[]>> announceList = (List<List<byte[]>>) metaInfo.get("announce-list");

            for(List<byte[]> tier: announceList){
                for(byte[] urlBytes: tier){
                    urls.add(new String(urlBytes,StandardCharsets.UTF_8));
                }
            }
        }
        return urls;
    }
}
