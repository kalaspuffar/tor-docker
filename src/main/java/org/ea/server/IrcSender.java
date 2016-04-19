package org.ea.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class IrcSender { 
	public static void send() throws Exception { 	
		// The server to connect to and our details. 
		String server = "irc.freenode.net";
		String nick = "simple_bot"; 
		String login = "simple_bot"; 
		// The channel which the bot will join.
		String channel = "#mysimpleirchacks";
		// Connect directly to the IRC server. 
		Socket socket = new Socket(server, 6667); 
		BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream( )));
		BufferedReader reader = new BufferedReader( new InputStreamReader(socket.getInputStream( )));
		// Log on to the server.
		writer.write("NICK " + nick + "\r\n"); 
		writer.write("USER " + login + " 8 * : Java IRC Hacks Bot\r\n");

		writer.flush( );
		// Read lines from the server until it tells us we have connected.
		String line = null;
		while ((line = reader.readLine( )) != null) {
			if (line.indexOf("004") >= 0) { 
				// We are now logged in.
				break;
			} else if (line.indexOf("433") >= 0) { 
				System.out.println("Nickname is already in use.");
				return;
			} 

		} 
		// Join the channel.
		writer.write("JOIN " + channel + "\r\n"); 
		writer.flush( ); 
		// Keep reading lines from the server.
		while ((line = reader.readLine( )) != null) {
			String writeLine = "PRIVMSG simple_bot :";
			if (line.indexOf(writeLine) != -1) {
				writer.write("PRIVMSG " + channel + " : Someone said: " + line.substring(line.indexOf(writeLine) + writeLine.length()) + " \r\n");
				writer.flush( );				
			} else if (line.indexOf("PING ") != -1) { 
				// We must respond to PINGs to avoid being disconnected.
				writer.write("PONG " + line.substring(line.indexOf("PING ") + 5) + "\r\n");
				writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
				writer.flush( ); 

			} else { 
				// Print the raw line received by the bot.
				System.out.println(line);
			}
		}
	}
}
