package com.gpcoder.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EchoClient {

	public final static String SERVER_IP = "127.0.0.1";
	public final static int SERVER_PORT = 7;
	public final static byte[] BUFFER = new byte[4096];

	public static void main(String[] args) {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(); // Tạo DatagramSocket
			System.out.println("Client started ");

			InetAddress server = InetAddress.getByName(SERVER_IP);
			while (true) {
				System.out.println("Enter your message: ");
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);
				String theString = br.readLine();
				byte[] data = theString.getBytes();

				DatagramPacket dp = new DatagramPacket(data, data.length, server, SERVER_PORT);
				ds.send(dp);

				DatagramPacket incoming = new DatagramPacket(BUFFER, BUFFER.length);
				ds.receive(incoming);

				System.out.println("Received: " + new String(incoming.getData(), 0, incoming.getLength()));
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
}