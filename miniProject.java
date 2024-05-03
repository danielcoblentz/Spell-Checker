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
                dictionary.add(scanner.next().toLowerCase()); //converts to lowercase characters for case sensentity
            }
        } catch (FileNotFoundException e) { 
            System.out.println("Dictionary file not found.");
            System.exit(1);
        }



        // Process the file to be checked for spelling errors.
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0; // Line counter to track the line number of potential spelling errors to print out later to user


            //read the file line by line
            while ((line = reader.readLine()) != null) { //reads all of the lines in the file
                lineCount++;
                String[] words = line.split("\\W+"); // split line into individual words to be checked using non-word characters


// Check each word against the dictionary.
//!word.isempty() ensures that the word is a real string with characters
for (int i = 0; i < words.length; i++) {
    String word = words[i];
    if (!word.isEmpty() && !binarySearch(dictionary, word.toLowerCase())) { // if they word is not mepty and is not found in the dictionary then prints out the following
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
            int mid = low + (high - low) / 2; //calculate the middle index of the search
            String midVal = list.get(mid); // retrieve the middle index

            int cmp = midVal.compareTo(key); // compare to the mid value with the word(key)

            if (cmp < 0) { // search range to upper half
                low = mid + 1;
            } else if (cmp > 0) {  // search range to lower half
                high = mid - 1;
            } else {
                return true; // key found then the word is spelled correcly
            }
        }
        return false; // key not found should mark if a word is spelled incorrectly
    }
}


//If the result is negative, it means that midVal is less than key, so the search range is narrowed to the upper half by updating low = mid + 1.
//If the result is positive, it means that midVal is greater than key, so the search range is narrowed to the lower half by updating high = mid - 1
//If the comparison result is zero, it means that midVal is equal to key, so the key is found, and the method returns true