
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class ArrayThread extends Thread {

   // Used to index each thread for the main method
   private final int id;
   // Used to coordinate access to the shared memory array
   private final Lock lock;
   // The number of operations each thread will complete
   private final int NUM_OPERATIONS = 50;
   // Used to generate a random number between 0 and 1100 for choosing
   // a random number
   private final int MAX_OPERATION_VALUE = 1101;
   // Shared memory arrays provided by main method. Array is used for search
   // and replace operations. Pool provides strings to replace array values
   private String[] array, pool;
   // Used to average the amount each thread has to wait before searching
   // or replacing a string
   ArrayList<Double> searchWaitTimes = new ArrayList<>();
   ArrayList<Double> replaceWaitTimes = new ArrayList<>();

   // ArrayThread constructor taking an id, a lock on the entire array, and the
   // shared memory arrays array and pool
   public ArrayThread(int id, Lock lock, String[] array,
   String[] pool) {
      this.id = id;
      this.lock = lock;
      this.array = array;
      this.pool = pool;
   }

   // Takes a randomly generated integer value and performs a task
   // according to that value
   public void performTask(int operation) {
      if (operation >= 0 && operation <= 999) {
         searchForString();
      } else {
         replaceString();
      }
   }

   // Searches for the last occurrence of a string, randomly taken from the
   // pool, in the ARRAY array. The thread locks upon accessing the ARRAY,
   // ensuring that other threads do not access it at the same time.
   public void searchForString() {

      // Generate a random string from the pool
      String poolString = pool[new Random().nextInt(pool.length)];
      // Get the initial time for when the thread starts waiting for the lock
      double startTime = System.nanoTime();
      // Lock on the shared lock
      lock.lock();
      // Get the time for when the thread stops waiting for the lock
      double endTime = System.nanoTime();

      // Add the wait time in seconds to searchWaitTimes ArrayList
      searchWaitTimes.add((endTime - startTime) / 1000000000);

      try {
         // Iterate over the array starting at the last element
         for (int i = array.length - 1; i > 0; i--) {

            // First occurrence of the string in reverse-order iteration
            // is the last occurrence of the string. Print the index upon
            // finding the string and break from the for loop
            if (array[i].equals(poolString)) {
               System.out.printf("Thread %d: The last occurrence of %s is"
               + " at index %d" + "\n", (id + 1), poolString, i);
               break;
            }
         }
      } finally {
         // Unlock the shared lock
         lock.unlock();
      }
   }

   // Replaces a string within the ARRAY array with a randomly chosen
   // string from the POOL array. The thread locks upon accessing the 
   // ARRAY array.
   public void replaceString() {
      // Generate a random integer value to determine which ARRAY value
      // to replace
      int index = new Random().nextInt(array.length);
      // Get the initial time for when the thread starts waiting for the lock
      double startTime = System.nanoTime();
      // Lock on the shared 
      lock.lock();

      // Get the time for when the thread stops waiting for the lock
      double endTime = System.nanoTime();

      // Add the wait time in seconds to the replaceWaitTimes ArrayList
      replaceWaitTimes.add((endTime - startTime) / 1000000000);
      try {
         // Generate a random string from the pool to replace the
         // ARRAY string with
         String replaceString = pool[new Random().nextInt(pool.length)];

         // Print out the string replacement
         System.out.println("Thread " + (id + 1) + ": Replacing " + array[index]
         + " with " + replaceString);

         // Replace the ARRAY string with the chosen POOL string
         array[index] = replaceString;
      } finally {
         // Unlock the shared lock
         lock.unlock();
      }
   }

   // Run upon starting the thread. Performs 50 random operations.
   @Override
   public void run() {
      // Perform a random operation 50 times
      for (int i = 0; i < NUM_OPERATIONS; i++) {
         performTask(new Random().nextInt(MAX_OPERATION_VALUE));
      }
   }

   // Used to print the average and standard deviation of the search
   // and replace wait times for the thread
   public void printWaitTimes() {

      // Average the search and replace wait time ArrayLists
      double averageSearchTime = average(searchWaitTimes);
      double averageReplaceTime = average(replaceWaitTimes);

      // Take the standard deviation of the search and wait time
      // ArrayLists
      double searchStandardDev
      = standardDeviation(searchWaitTimes, averageSearchTime);
      double replaceStandardDev
      = standardDeviation(replaceWaitTimes, averageReplaceTime);

      // Print out the thread's id and the average and standard deviation
      // wait times
      System.out.println("Thread " + (id + 1) + ": Average search wait time: "
      + (averageSearchTime) + " seconds \n Average replace wait time: "
      + (averageReplaceTime) + " seconds \n Search Time Standard Deviation: "
      + (searchStandardDev) + " seconds \n Replace Time Standard Deviation: "
      + (replaceStandardDev) + " seconds \n");
   }

   // Used to find the average of the wait times for search and replace
   private double average(ArrayList<Double> waitTime) {
      double averageTime = 0;

      // Sum the wait times in the given ArrayList
      for (double time : waitTime) {
         averageTime += time;
      }
      // Return the sum of the values divided by the ArrayList size
      return (averageTime / waitTime.size());
   }

   // Used to find the standard deviation of the wait times for search and 
   // replace
   private double standardDeviation(ArrayList<Double> waitTime, double avg) {

      double standardDev = 0;

      // Find the summation for each value minus the avverage in the given 
      // waitTime ArrayList
      for (double time : waitTime) {
         standardDev += Math.pow(time - avg, 2);
      }

      // Divide the summation by the size of the waitTime ArrayList to find 
      // the variance
      standardDev /= waitTime.size();

      // Square the variance to give the standard deviation for the
      // values of the ArrayList
      standardDev = Math.sqrt(standardDev);

      // Return the standard deviation
      return standardDev;
   }
}
