package Game;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientV2 {
    public static void main(String[] args) {
        System.out.println("=== GAME ĐOÁN SỐ VERSION 2 ===");
        System.out.println("Nhiều lượt chơi - Có bảng xếp hạng");
        
        try {
            System.out.println("Đang kết nối đến server...");
            Socket socket = new Socket("localhost", 9999);
            System.out.println("Đã kết nối thành công!");
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            
            Thread messageReader = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Đã ngắt kết nối với server");
                }
            });
            
            messageReader.start();
            
            while (messageReader.isAlive()) {
                String input = scanner.nextLine();
                out.println(input);
                
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    break;
                }
            }
            
            scanner.close();
            socket.close();
            System.out.println("Đã thoát game!");
            
        } catch (IOException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}