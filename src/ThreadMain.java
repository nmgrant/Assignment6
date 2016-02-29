
import java.util.concurrent.locks.Condition;
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
      
      // Condition to be associated with the shared lock
      Condition condition = lock.newCondition();
      
      // Keeps track of the threads created by main method
      ArrayThread[] threadArray = new ArrayThread[NUM_OF_THREADS];
      
      // The randomly generated pool of strings used to populate the ARRAY
      // and generate strings to search for and replace with
      String[] POOL = new String[110];
      
      // The array of strings used by the threads to search for and replace
      // strings
      String[] ARRAY = new String[100];
      
      // Populate the POOL array with randomly generated strings
      for (int i = 0; i < POOL.length; i++) {
         POOL[i] = generateRandomString();
      }
      
      // Populate the ARRAY array with strings randomly chosen from the POOL
      for (int i = 0; i < ARRAY.length; i++) {
         ARRAY[i] = POOL[new Random().nextInt(POOL.length)];
      }
      
      // Populate the thread array with instances of our custom thread class.
      // The threads take in the lock, ARRAY, and POOL, effectively making them
      // shared variables
      for (int i = 0; i < threadArray.length; i++) {
         threadArray[i] = new ArrayThread(i, lock, ARRAY, POOL);
      }
      
      // Start each thread in the thread array
      for (ArrayThread thread : threadArray) {
         thread.start();
      }
      
      // Wait until all threads have finished by joining on each one
      for (ArrayThread thread : threadArray) {
         try {
            thread.join();
         } catch (InterruptedException ex) {
            System.out.println(ex);
         }
      }
      
      // Print an empty line for formatting purposes
      System.out.println();
      
      // Print out the average search times and standard deviation of the
      // search times for each thread
      for (ArrayThread thread : threadArray) {
         thread.printWaitTimes();
      }
   }
   
   // Used to generate a random string of uppercase letters with length
   // between 5 and 20
   public static String generateRandomString() {
      final int LENGTH_UPPER_BOUND = 16;
      final int LENGTH_LOWER_BOUND = 5;
      final int LETTER_UPPER_BOUND = 26;
      
      // Generates a random integer value between 5 and 20
      //for the length of the random string
      int lengthOfString = 
       new Random().nextInt(LENGTH_UPPER_BOUND) + LENGTH_LOWER_BOUND;
      
      // Initialize an empty string to append to
      String randomString = new String();
      
      // Adds lengthOfString randomly generated uppercase letters 
      // to our randomString
      for (int i = 0; i < lengthOfString; i++) {
         randomString += 
          (char) (new Random().nextInt(LETTER_UPPER_BOUND) + 'A');
      }
      
      // Return the randomString
      return randomString;
   }
}
