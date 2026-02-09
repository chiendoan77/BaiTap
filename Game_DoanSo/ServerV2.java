package Game;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ServerV2 {
    private static final int PORT = 9999;
    private static final int MAX_ROUNDS = 5;
    private static Map<String, Integer> playerScores = new ConcurrentHashMap<>();
    private static List<Integer> secretNumbers = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        System.out.println("=== GAME ĐOÁN SỐ VERSION 2 ===");
        System.out.println("Nhiều lượt chơi - Có bảng xếp hạng");
        System.out.println("Đang khởi động server...");
        
        Random rand = new Random();
        for (int i = 0; i < MAX_ROUNDS; i++) {
            int secret = rand.nextInt(100) + 1;
            secretNumbers.add(secret);
            System.out.println("Lượt " + (i + 1) + ": Số bí mật = " + secret);
        }
        
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server đang chờ client kết nối trên cổng " + PORT + "...");
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client mới kết nối: " + clientSocket.getInetAddress());
            
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
    
    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String playerName;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                out.println("Nhập tên của bạn:");
                playerName = in.readLine();
                System.out.println("Người chơi '" + playerName + "' đã tham gia");
                
                playerScores.put(playerName, 0);
                int totalScore = 0;
                
                out.println("Xin chào " + playerName + "! Bắt đầu " + MAX_ROUNDS + " lượt chơi.");
                out.println("QUY TẮC ĐIỂM:");
                out.println("- Đoán chính xác: +2 điểm");
                out.println("- Đoán nhỏ hơn: +1 điểm");
                out.println("- Đoán lớn hơn: +0 điểm");
                
                for (int round = 1; round <= MAX_ROUNDS; round++) {
                    out.println("\n=== LƯỢT " + round + " / " + MAX_ROUNDS + " ===");
                    out.println("Hãy đoán số từ 1-100");
                    
                    int secretNumber = secretNumbers.get(round - 1);
                    boolean guessedCorrectly = false;
                    
                    while (!guessedCorrectly) {
                        String guessStr = in.readLine();
                        if (guessStr == null) break;
                        
                        try {
                            int guess = Integer.parseInt(guessStr);
                            
                            if (guess < 1 || guess > 100) {
                                out.println("Vui lòng nhập số từ 1-100!");
                                continue;
                            }
                            
                            System.out.println(playerName + " (Lượt " + round + "): Đoán số " + guess);
                            
                            if (guess < secretNumber) {
                                out.println("Số của bạn NHỎ HƠN số bí mật");
                                totalScore += 1;
                                System.out.println(playerName + " +1 điểm (Tổng: " + totalScore + ")");
                            } else if (guess > secretNumber) {
                                out.println("Số của bạn LỚN HƠN số bí mật");
                            } else {
                                out.println("CHÍNH XÁC! Số bí mật là " + secretNumber);
                                totalScore += 2;
                                guessedCorrectly = true;
                                System.out.println(playerName + " +2 điểm (Tổng: " + totalScore + ")");
                            }
                            
                            playerScores.put(playerName, totalScore);
                            out.println("Điểm hiện tại: " + totalScore);
                            
                        } catch (NumberFormatException e) {
                            out.println("Vui lòng nhập số hợp lệ!");
                        }
                    }
                    
                    out.println("Kết thúc lượt " + round);
                }
                
                out.println("\n=== KẾT THÚC GAME ===");
                out.println("Tổng điểm của bạn: " + totalScore);
                
                out.println(getScoreboard());
                out.println("Cảm ơn bạn đã chơi, " + playerName + "!");
                
                System.out.println(playerName + " đã hoàn thành game với " + totalScore + " điểm");
                
            } catch (IOException e) {
                System.out.println("Lỗi với client " + playerName + ": " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private String getScoreboard() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== BẢNG XẾP HẠNG ===\n");
            
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(playerScores.entrySet());
            sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            int rank = 1;
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                sb.append(String.format("%2d. %-15s: %3d điểm\n", 
                    rank++, entry.getKey(), entry.getValue()));
            }
            
            return sb.toString();
        }
    }
}