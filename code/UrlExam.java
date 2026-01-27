package com.gpcoder.tcp;

import java.io.IOException;
import java.net.URL;

public class UrlExam {

	public static void main(String[] args) {
		try {
			@SuppressWarnings("deprecation")
			URL url = new URL("https://www.gpcoder.com:80/java/index.html?page=1&amp;amp;order=desc#java-core");
			System.out.println("URL " + url.toString());
			System.out.println("protocol " + url.getProtocol());
			System.out.println("Authority " + url.getAuthority());
			System.out.println("File name " + url.getFile());
			System.out.println("host " + url.getHost());
			System.out.println("Path " + url.getPath());
			System.out.println("port " + url.getPort());
			System.out.println("Query " + url.getQuery());
			System.out.println("DefaultPort " + url.getDefaultPort());
			System.out.println("ref " + url.getRef());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
