package Game;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientV1 {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 1765;
    
    public static void main(String[] args) {
        System.out.println("=== CLIENT VERSION 1 - GAME ĐOÁN SỐ ===");
        
        try (
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Đã kết nối đến server!");
            
            // Nhận thông báo chào mừng
            String serverMessage = in.readLine();
            System.out.println("Server: " + serverMessage);
            
            while (true) {
                System.out.print("\nNhập số dự đoán (1-100) hoặc 'quit' để thoát: ");
                String input = scanner.nextLine();
                
                if (input.equalsIgnoreCase("quit")) {
                    System.out.println("Kết thúc chương trình!");
                    break;
                }
                
                // Gửi dự đoán đến server
                out.println(input);
                
                // Nhận phản hồi từ server
                String response = in.readLine();
                System.out.println("Server: " + response);
                
                if (response.contains("CHÍNH XÁC")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}