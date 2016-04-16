package org.ea.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;

import com.msopentech.thali.toronionproxy.OnionProxyContext;
import com.msopentech.thali.toronionproxy.OnionProxyManager;
import com.msopentech.thali.toronionproxy.Utilities;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyManager;
import com.msopentech.thali.java.toronionproxy.JavaOnionProxyContext;

public class TestProxy {
    public static void main(String argv[]) throws Exception {
        String fileStorageLocation = "/server/torfiles";
        OnionProxyManager onionProxyManager = new JavaOnionProxyManager(
                new JavaOnionProxyContext(
                        Files.createTempDirectory(fileStorageLocation).toFile()));

        int totalSecondsPerTorStartup = 4 * 60;
        int totalTriesPerTorStartup = 5;

        // Start the Tor Onion Proxy
        if (onionProxyManager.startWithRepeat(totalSecondsPerTorStartup, totalTriesPerTorStartup) == false) {
            return;
        }

        // Start a hidden service listener
        int hiddenServicePort = 9797;
        int localPort = 80;
        String onionAddress = onionProxyManager.publishHiddenService(hiddenServicePort, localPort);

        System.out.println(onionAddress);
        
        
        Thread.sleep(5 * 60 * 1000);
    }
}
