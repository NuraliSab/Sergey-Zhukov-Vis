import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;

// TODO: check for numbers and other symbols -> done

public class Main {
    public static void main(String[] args) {
        Start();
    }

    static String randomWord;
    private static final Scanner inp = new Scanner(System.in);
    private static HashSet<Character> usedLetters = new HashSet<>();


    static int countOfGuessedLetters = 0;
    static int countOfMistakes = 0;
    static String[] closedLetters;


    public static void Start(){
//        while(choosesOptionAndStart()){
            resetGame();
            gameloop(wordToCharArray());
//        }
    }

    public static String pickWordFromFile() {
        String randomWord;
        ArrayList<String> words;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
            words = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.toLowerCase());
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Random random = new Random();
        randomWord = words.get(random.nextInt(words.size()));
        words.remove(randomWord);
        return randomWord;

    }

    public static void resetGame(){
        usedLetters.clear();
        uniqueLetters.clear();
        countOfGuessedLetters = 0;
        countOfMistakes = 0;
    }


    public static OptionsToStart choosesOptionAndStart(){
        System.out.println("[N]ew game or [E]xit");
        String start = inp.nextLine().toLowerCase();

            switch(start){
                case "n":
                    return OptionsToStart.NEW_GAME;
                case "e":
                    return OptionsToStart.EXIT;

                default:
                    System.out.println("Write n or e");
                    break;
            }

        }
        if (start.equals("n")){
            return true;
        }
        return false;

    }

    public static ArrayList<String> wordSegment(){
        randomWord = generateWord();
        closedLetters = new String[randomWord.length()];
        Arrays.fill(closedLetters, "#");
        ArrayList<String> randomWordByLetter = new ArrayList<>();
        for (char letter: randomWord.toCharArray()){
            randomWordByLetter.add(String.valueOf(letter));
            uniqueLetters.add(String.valueOf(letter));
        }
        return randomWordByLetter;
    }


    public static void gameloop(char[] wordSegment){
        while (countOfMistakes < 5){
            printCurrentStateOfWord();

            char typedLetter = getInputLetter();

            checkingStateOfWord(typedLetter, wordSegment);




            if (isWordGuessed()){
                System.out.println("Congratulations!!!  You won");
                System.out.println("The word was: " + randomWord);
                break;
            }
            if (countOfMistakes == 5){
                System.out.println("You Lost");
                System.out.println("The word was: " + randomWord);
            }
        }

    }

    public static String getInputLetter() {
        System.out.println("Type a letter");
        boolean isUsed = true;
        String letter = "";
        while (isUsed) {
            letter = inp.nextLine().toLowerCase().strip();
            while (letter.length() != 1 || !letter.matches("[a-z]")){
                System.out.println("Type only letter");
                letter = inp.nextLine().toLowerCase().strip();
            }
            if (!usedLetters.contains(letter)) {
                usedLetters.add(letter);
                isUsed = false;

            } else {
                System.out.println("This letter is already used");
                System.out.println("Type another letter");
            }


        }
        return letter;
    }



    public static void checkingStateOfWord(String letter, ArrayList<String> wordSegment){
        int exists = 0;
        for (int i = 0; i <wordSegment.size(); i++) {
            if (letter.equals(wordSegment.get(i))){
                closedLetters[i] = letter;
                exists ++;

            }

        }
        if (exists > 0){
            countOfGuessedLetters += exists;
            System.out.println("Right you guessed a letter");
        }
        else{
            System.out.println("This letter doesn't exist");
            String[] pictureOfVis = printCurrentStateOfVis();
            System.out.println(pictureOfVis[countOfMistakes]);
            countOfMistakes ++;
        }
        System.out.println("Attempts to mistake left: " + (5 - countOfMistakes) + "/5");

    }



    public static boolean isWordGuessed(){
        return !Arrays.asList(closedLetters).contains("#");
    }

    public static void printCurrentStateOfWord(){
        System.out.println(Arrays.toString(closedLetters).replaceAll("[^a-z#]",""));
    }


    public static String[] printCurrentStateOfVis(){
        String[] elementsOfVis = {
                " |\n |",
                " |\n |\n( )",
                " |\n |\n( )\n |",
                " |\n |\n( )\n/|\\",
                " |\n |\n( )\n/|\\\n |\n /\\",
        };
        return elementsOfVis;
    }

}