package org.ea.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;

import com.sun.net.httpserver.HttpExchange; 
import com.sun.net.httpserver.HttpHandler; 
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

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

        HttpServer server = HttpServer.create(new InetSocketAddress(65000), 0);
     		  server.createContext("/", new MyHandler());
     		  server.setExecutor(null);
     		  
     		  server.start();

        int hiddenServicePort = 9797;
        int localPort = 65000;
        String onionAddress = onionProxyManager.publishHiddenService(hiddenServicePort, localPort);

        System.out.println(onionAddress);
        
        Thread.sleep(30 * 60 * 1000);
    }
     		 
    static class MyHandler implements HttpHandler {
      @Override public void handle(HttpExchange t) throws IOException {
      	String response = "";
      	t.sendResponseHeaders(200, response.length());
      	OutputStream os = t.getResponseBody(); os.write(response.getBytes()); os.close();
      	
      	}
   }
}