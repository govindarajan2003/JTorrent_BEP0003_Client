package com.govindarajan.jtorrent.network;

import com.govindarajan.jtorrent.bencoding.BencodeDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Implementation of TrackerClient using standard HTTP GET requests.
 * Compliant with Bittorent Protocol 1.0.
 * Supports:
 *  -> Announce URL parameter building.
 *  -> Bencoded response parsing.
 *  -> Compact Peer list extraction.
 */
public class HttpTrackerClient implements TrackerClient{
    private final String peerId;

    /**
     * Initializes the client and generates a unique Peer ID (format: -JT0001-...).
     */
    public HttpTrackerClient() {
        this.peerId = TrackerUtils.generatePeerId();
    }

    /**
     * Connects to the HTTP tracker and retrieves list of peers.
     * @param announceUrl The URL of the tracker(e.g,http://bittorrent.kali.org/announce)
     * @param infoHash The 20-byte SHA-1 hash of the torrent info
     * @param fileLength Total length of the file (for reporting "left")
     * @return A list of {@Link Peer} object containing IP Address and Port.
     * @throws RuntimeException if the connection fails or the response is invalid.
     */
    @Override
    public List<Peer> getPeers(String announceUrl, byte[] infoHash, Long fileLength){
        String encodedInfoHash = TrackerUtils.urlEncodeBytes(infoHash);
        try{
            // 1. Builds URL
            URL url = buildAnnounceURL(announceUrl, encodedInfoHash, fileLength);

            // 2. Makes connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 3. Handles Response
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                return parseResponse(inputStream);
            } else{
                handleError(connection);
                throw new Exception("Tracker returned with error code : "+responseCode);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * Constructs AnnounceURL with necessary query parameter.
     * @param base The base url of the tracker.
     * @param encodedInfoHash String parsed infohash.
     * @return The fully constructed URL.
     * @throws MalformedURLException When the url cannot be formed.
     */
    private URL buildAnnounceURL(String base, String encodedInfoHash, Long fileLength) throws MalformedURLException {
        return new URL(base + "?info_hash=" + encodedInfoHash +
                "&peer_id=" + peerId +
                "&port=6881" +
                "&uploaded=0&downloaded=0" +
                "&left=" + fileLength +
                "&compact=1");
    }

    /**
     * Parses the tracker response stream.
     * Extracts the "Peer" blob and parses it using TrackerUtils.
     * @param inputStream
     * @return A list of peer who are connected to the tracker.
     * @throws IOException
     */
    private List<Peer> parseResponse(InputStream inputStream) throws IOException {
        byte[] responseByte = inputStream.readAllBytes();
        BencodeDecoder decoder = new BencodeDecoder(responseByte);

        Map<String, Object> decodedResponse = (Map<String, Object>) decoder.decode();
        byte[] peerBlob = (byte[]) decodedResponse.get("peers");
        List<Peer> peers = TrackerUtils.parseCompactString(peerBlob);
        return peers;
    }

    /**
     * Reads the error stream from a failed connection for debugging.
     */
    private void handleError(HttpURLConnection connection){
        try (InputStream errorStream = connection.getErrorStream()) {
            if (errorStream != null) {
                System.out.println("Error Body: " + new String(errorStream.readAllBytes()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
