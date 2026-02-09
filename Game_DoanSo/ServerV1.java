package Game;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerV1 {
	private static final int PORT = 1765;
	private static int An_So;
	private static boolean Run = true;
	
	public static void main(String[] agr) {
		System.out.println("Game đoán số Version 1");
		System.out.println("Loading...");
		
		An_So = new Random().nextInt(100)+1;
		
		try (ServerSocket svSocket = new ServerSocket(PORT) ){
			System.out.println("Đang kết nối đến cổng" + PORT);
			
			while (Run) {
				Socket clientSocket  = svSocket.accept();
				System.out.println("Client kết nối" + clientSocket.getInetAddress());
				new Thread(new ClientHandlerV1(clientSocket)).start();
			}
		}catch (IOException e) {
			System.out.println("Lỗi_"+e.getMessage());
		}
	}
	static class ClientHandlerV1 implements Runnable {
		private Socket clientSocket;
		
		ClientHandlerV1(Socket socket) {
			this.clientSocket = socket;
		}
		@Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                out.println("Chào mừng đến với Game Đoán Số! Đoán số từ 1-100");
                
                String clientMessage;
                while ((clientMessage = in.readLine()) != null) {
                    try {
                        int guess = Integer.parseInt(clientMessage);
                        System.out.println("Client " + clientSocket.getInetAddress() + " đoán: " + guess);
                        
                        if (guess < An_So) {
                            out.println("Số của bạn NHỎ hơn số bí mật");
                        } else if (guess > An_So) {
                            out.println("Số của bạn LỚN hơn số bí mật");
                        } else {
                            out.println("CHÍNH XÁC! Bạn đã đoán đúng số " + An_So);
                            System.out.println("Client " + clientSocket.getInetAddress() + " đã đoán đúng!");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        out.println("Vui lòng nhập số nguyên hợp lệ!");
                    }
                }
            } catch (IOException e) {
                System.out.println("Lỗi xử lý client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
