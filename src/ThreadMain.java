/**
 * Evan McNaughtan and Nicholas Grant
 * March 21, 2016
 * CECS 327 (MW 10:00)
 * Assignment #6
 * evan4james@yahoo.com
 * ngrant40@gmail.com
 * 
 * This program is an exercise in utilizing locks to ensure only one process
 * enters a critical section at any given time. The critical section in this 
 * program involves accessing a shared memory array. Each thread can either 
 * search for a string within the array or replace a string. Therefore, in order
 * to ensure that the shared memory array is properly synchronized between all 
 * threads, a lock mechanism is used to ensure mutual exclusion. This 
 * implementation uses coarse-grained synchronization, that is the entire array 
 * is locked when a thread wishes to access it. Wait time data for both 
 * searching and replacing is measured for each thread operation. In addition, 
 * average wait time and standard deviation for both searching and replacing is 
 * calculated and displayed. A criticism of this implementation is that the 
 * coarse-grained synchronization may result in higher wait times on average as 
 * a bottleneck is created. To fix this, fine-grained synchronization or an 
 * optimistic locking mechanism could be used instead.
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class ThreadMain {

   public static void main(String[] args) {

      // Number of threads to be run for the main method
      final int NUM_OF_THREADS = 20;
      
      final int NUM_OF_POOL_STRINGS = 110;
      
      final int NUM_OF_ARRAY_STRINGS = 100;
      // The lock to be shared by each thread for limiting access to the 
      // ARRAY array
      Lock lock = new ReentrantLock();
      // Keeps track of the threads created by main method
      ArrayThread[] threadArray = new ArrayThread[NUM_OF_THREADS];
      // The randomly generated pool of strings used to populate the ARRAY
      // and generate strings to search for and replace with
      String[] POOL = new String[NUM_OF_POOL_STRINGS];
      // The array of strings used by the threads to search for and replace
      // strings
      String[] ARRAY = new String[NUM_OF_ARRAY_STRINGS];

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
}
