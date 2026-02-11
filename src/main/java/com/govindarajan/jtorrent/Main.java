package com.govindarajan.jtorrent;

import com.govindarajan.jtorrent.bencoding.BencodeDecoder;
import com.govindarajan.jtorrent.network.HttpTrackerClient;
import com.govindarajan.jtorrent.network.Peer;
import com.govindarajan.jtorrent.network.TrackerClient;
import com.govindarajan.jtorrent.network.TrackerUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        String torrentFilePath = "C:\\Users\\gardm\\Downloads\\kali-linux-2025.4-installer-amd64.iso.torrent";
        Path path = Paths.get(torrentFilePath);

        byte[] torrentBytes = Files.readAllBytes(path);
        BencodeDecoder bencoder = new BencodeDecoder(torrentBytes);

        Map<String, Object> metaInfo = (Map<String, Object>) bencoder.decode();
        Map<String, Object> infoMap = (Map<String, Object>) metaInfo.get("info");
        System.out.println(infoMap.keySet());

        byte[] infoHash = TrackerUtils.calculateInfoHash(infoMap);
        List<String> urls = TrackerUtils.getAnnounceList(metaInfo);


        Long fileLength = (Long) infoMap.get("length");
        TrackerClient client = new HttpTrackerClient();
       // List<Peer> peers = client.getPeers(urls.get(0),infoHash,fileLength);
    }
}
