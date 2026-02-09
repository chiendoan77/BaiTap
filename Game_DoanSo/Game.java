package Game;

import java.util.Random;

public class Game {
    
    public static int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
    
    public static boolean isValidGuess(int guess, int min, int max) {
        return guess >= min && guess <= max;
    }
    
    public static String compareNumbers(int guess, int target) {
        if (guess < target) {
            return "NHỎ HƠN";
        } else if (guess > target) {
            return "LỚN HƠN";
        } else {
            return "CHÍNH XÁC";
        }
    }
}