import java.util.*;
import java.io.*;

public class miniProject {

    public static void main(String[] args) {
        String dictPath = "dictionary.txt";
        String filePath = "testfile.txt";
        ArrayList<String> dictionary = new ArrayList<>();

        // Load the dictionary
        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) {
                dictionary.add(scanner.next().toLowerCase()); // ensure all words are lowercase for case insensitivity
            }
        } catch (FileNotFoundException e) { // in case file is not found
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }

        // Read and check each line of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.split("\\W+"); // split line into words using non-word characters

                for (String word : words) {
                    if (!word.isEmpty() && !isNumeric(word) && !binarySearch(dictionary, word.toLowerCase())) {
                        System.out.println("Misspelled word '" + word + "' found at line " + lineCount);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            System.exit(1);
        }
    }

    // method to check if the string is numeric 
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
    }

    // Implement binary search to find words in the dictionary and compaire to testing file
    public static boolean binarySearch(ArrayList<String> list, String key) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            String midVal = list.get(mid);

            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return true; // key found
            }
        }
        return false; // key not found
    }
}
