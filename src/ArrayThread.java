
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ArrayThread extends Thread {

   private final Lock lock;
   private final Condition condition;
   private final int NUM_OPERATIONS = 50;
   private String[] array, pool;

   public ArrayThread(Lock lock, Condition condition, String[] array,
   String[] pool) {
      this.lock = lock;
      this.condition = condition;
      this.array = array;
      this.pool = pool;
   }

   public void performTask(int operation) {
      if (operation >= 0 && operation <= 999) {
         searchForString();
      } else {
         replaceString();
      }
   }

   public void searchForString() {
      String poolString = pool[new Random().nextInt(pool.length)];
      lock.lock();
      try {
         for (int i = array.length - 1; i > 0; i--) {
            if (array[i].equals(poolString)) {
               System.out.printf("The last occurrence of %s is at index %d",
               poolString, i);
               break;
            }
         }
      } finally {
         lock.unlock();
      }
   }

   public void replaceString() {
      int index = new Random().nextInt(array.length);

      lock.lock();
      try {
         array[index] = pool[new Random().nextInt(pool.length)];
      } finally {
         lock.unlock();
      }
   }

   public void run() {
      for (int i = 0; i < NUM_OPERATIONS; i++) {
         performTask(new Random().nextInt(1101));
      }
   }
}
