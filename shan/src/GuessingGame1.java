import java.util.*;

public class GuessingGame1 {

    private static String computerNumber;
    private static String playerName;
    private static int guessCount;
    private static double startTime;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database.connect();
        Database.createTable();

        System.out.println("Welcome to the Guessing Number Game!");
        System.out.print("Enter your name: ");
        playerName = scanner.nextLine();

        boolean playAgain = true;
        while (playAgain) {
            startGame();
            System.out.print("Do you want to play again (y/n)? ");
            playAgain = scanner.nextLine().equalsIgnoreCase("y");
        }

        Database.showBestScore();
        Database.close();
    }

    public static void startGame() {

        computerNumber = generateRandomNumber();
        guessCount = 0;
        startTime = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        String guess;
        String result;

        System.out.println("A 4-digit number has been selected. Try to guess it!");
        do {
            System.out.print("Enter your guess (4 digits): ");
            guess = scanner.nextLine();

            if (guess.length() != 4 || !guess.matches("\\d{4}")) {
                System.out.println("Invalid input. Please enter exactly 4 digits.");
                continue;
            }
            result = evaluateGuess(guess);
            System.out.println("Result: " + result);
            guessCount++;
        } while (!guess.equals(computerNumber));

        double endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;
        System.out.println("Congratulations! You've guessed the number!");
        System.out.println("Time: " + elapsedTime + " seconds");
        System.out.println("Guesses: " + guessCount);

        Database.insertResult(playerName, elapsedTime, guessCount);
    }

    public static String generateRandomNumber() {
        Random rand = new Random();
        Set<Integer> digits = new HashSet<>();
        StringBuilder number = new StringBuilder();

        while (digits.size() < 4) {
            int digit = rand.nextInt(10);
            if (!digits.contains(digit)) {
                digits.add(digit);
                number.append(digit);
            }
        }

        return number.toString();
    }

    public static String evaluateGuess(String guess) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (guess.charAt(i) == computerNumber.charAt(i)) {
                result.append("+");
            } else if (computerNumber.contains(String.valueOf(guess.charAt(i)))) {
                result.append("-");
            }
        }
        return result.toString();
    }
}
