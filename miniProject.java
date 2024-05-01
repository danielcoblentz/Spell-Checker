import java.util.*;
import java.io.*;

public class miniProject {

    public static void main(String[] args) {
        String dictPath = "dictionary.txt";// Paths to the dictionary and file to be spell-checked.
        String filePath = "testfile.txt";
        ArrayList<String> dictionary = new ArrayList<>(); //arraylist to hold the dictionary words for access

        // Load the dictionary from the file
        try (Scanner scanner = new Scanner(new File(dictPath))) {
            while (scanner.hasNext()) {
                dictionary.add(scanner.next().toLowerCase()); // add each word  to the dictionary after converting it to lowercase
            }
        } catch (FileNotFoundException e) { // in case file is not found print an error message
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }



        // Process the file to be checked for spelling errors.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0; // Line counter to track the line number of potential spelling errors.


            //read the file line by line
            while ((line = reader.readLine()) != null) { //reads all of the lines in the file
                lineCount++;
                String[] words = line.split("\\W+"); // split line into individual words to be checked using non-word characters


// Check each word against the dictionary.
                for (String word : words) {
                    if (!word.isEmpty() && !binarySearch(dictionary, word.toLowerCase())) {
                        System.out.println("Misspelled word '" + word + "' found at line " + lineCount);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            System.exit(1);
        }
    }

  
    

    // Binary search method to check if a word exists in the dictionary.
    public static boolean binarySearch(ArrayList<String> list, String key) {
        int low = 0; //starting index
        int high = list.size() - 1; //ending index of the search range

        while (low <= high) {
            int mid = low + (high - low) / 2; //calculate the middle index
            String midVal = list.get(mid); // retrieve the middle index

            int cmp = midVal.compareTo(key); // compair the mid value with the key
//adjust search range based on result above

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
