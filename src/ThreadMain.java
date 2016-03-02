
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Random;

public class ThreadMain {

   public static void main(String[] args) {

      // Number of threads to be run for the main method
      final int NUM_OF_THREADS = 20;
      // The lock to be shared by each thread for limiting access to the 
      // ARRAY array
      Lock lock = new ReentrantLock();
      // Keeps track of the threads created by main method
      ArrayThread[] threadArray = new ArrayThread[NUM_OF_THREADS];
      // The randomly generated pool of strings used to populate the ARRAY
      // and generate strings to search for and replace with
      String[] POOL = new String[110];
      // The array of strings used by the threads to search for and replace
      // strings
      String[] ARRAY = new String[100];

      // Populate the POOL array with randomly generated strings
      populatePool(POOL);

      // Populate the ARRAY array with strings randomly chosen from the POOL
      populateArray(ARRAY, POOL);

      // Populate the thread array with instances of our custom thread class.
      // The threads take in the lock, ARRAY, and POOL, effectively making them
      // shared variables
      populateThreadArray(ARRAY, POOL, threadArray, lock);

      // Start each thread in the thread array
      startThreads(threadArray);

      // Wait until all threads have finished by joining on each one
      waitForThreads(threadArray);

      // Print out the average search times and standard deviation of the
      // search times for each thread
      printAllWaitTimes(threadArray);
   }

   // Used to generate a random string of uppercase letters with length
   // between 5 and 20
   public static String generateRandomString() {
      final int LENGTH_UPPER_BOUND = 16;
      final int LENGTH_LOWER_BOUND = 5;
      final int LETTER_UPPER_BOUND = 26;

      // Generates a random integer value between 5 and 20
      //for the length of the random string
      int lengthOfString
              = new Random().nextInt(LENGTH_UPPER_BOUND) + LENGTH_LOWER_BOUND;

      // Initialize an empty string to append to
      String randomString = new String();

      // Adds lengthOfString randomly generated uppercase letters 
      // to our randomString
      for (int i = 0; i < lengthOfString; i++) {
         randomString
                 += (char) (new Random().nextInt(LETTER_UPPER_BOUND) + 'A');
      }

      // Return the randomString
      return randomString;
   }

   // Iterates over the length of the POOL array and
   // initializes each index with a random string of uppercase letters
   public static void populatePool(String[] POOL) {
      for (int i = 0; i < POOL.length; i++) {
         POOL[i] = generateRandomString();
      }
   }

   // Iterates over the length of the ARRAY array and takes a random
   // string from the POOL array
   public static void populateArray(String[] ARRAY, String[] POOL) {
      for (int i = 0; i < ARRAY.length; i++) {
         ARRAY[i] = POOL[new Random().nextInt(POOL.length)];
      }
   }

   // Iterates over the length of the threadArray array and initializes
   // a new thread in each index
   public static void populateThreadArray(String[] ARRAY, String[] POOL,
           ArrayThread[] threadArray, Lock lock) {
      for (int i = 0; i < threadArray.length; i++) {
         threadArray[i] = new ArrayThread(i, lock, ARRAY, POOL);
      }
   }

   // Iterates over the threadArray array and starts each thread
   public static void startThreads(ArrayThread[] threadArray) {
      for (ArrayThread thread : threadArray) {
         thread.start();
      }
   }

   // Prints the wait time statistics for each thread in the threadArray
   public static void printAllWaitTimes(ArrayThread[] threadArray) {
      // Print an empty line for formatting purposes
      System.out.println();

      for (ArrayThread thread : threadArray) {
         thread.printWaitTimes();
      }
   }

   // Pauses execution of the main thread until all other threads have finished
   public static void waitForThreads(ArrayThread[] threadArray) {
      for (ArrayThread thread : threadArray) {
         try {
            thread.join();
         } catch (InterruptedException ex) {
            System.out.println(ex);
         }
      }
   }
}
