package com.govindarajan.jtorrent;

import com.govindarajan.jtorrent.bencoding.BencodeDecoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String absolutePath = "C:\\Users\\gardm\\Downloads\\ubuntu-25.10-desktop-amd64.iso.torrent";
        Path path = Paths.get(absolutePath);
        byte[] fileByte = Files.readAllBytes(path);
        BencodeDecoder bencoder = new BencodeDecoder(fileByte);
        Map<String, Object> result = (Map<String, Object>) bencoder.decode();
        System.out.println(result.keySet());
    }
}
