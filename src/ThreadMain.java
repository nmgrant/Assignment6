
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Random;

public class ThreadMain {

   public static void main(String[] args) {
      final int NUM_OF_THREADS = 20;
      
      Lock lock = new ReentrantLock();
      Condition condition = lock.newCondition();
      ArrayThread[] threadArray = new ArrayThread[NUM_OF_THREADS];

      String[] POOL = new String[110];
      String[] ARRAY= new String[100];

      for (int i = 0; i < POOL.length; i++) {
         POOL[i] = generateRandomString();
      }
      
      for (int i = 0; i < ARRAY.length; i++) {
         ARRAY[i] = POOL[new Random().nextInt(POOL.length)];
      }
      
      for (int i = 0; i < threadArray.length; i++) {
         threadArray[i] = new ArrayThread(i, lock, condition, ARRAY, POOL);
      }
      
      for (ArrayThread thread : threadArray) {
          thread.start();
      }
      
      for (ArrayThread thread : threadArray) {
          try {
              thread.join();
          } catch (InterruptedException ex) {
              System.out.println(ex);
          }
      }
      
      for (ArrayThread thread : threadArray) {
          thread.printWaitTimes();
      }
   }

   public static String generateRandomString() {
      int lengthOfString = new Random().nextInt(9) + 1;
      String randomString = "";
      
      for (int i = 0; i < lengthOfString; i++) {
         randomString += (char)(new Random().nextInt(26) + 'A');
      }
      return randomString;
   }
}
