
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ArrayThread extends Thread {
    private final int id;
    private final Lock lock;
    private final Condition condition;
    private final int NUM_OPERATIONS = 50;
    private String[] array, pool;
    private long searchWaitTime = 0;
    private long replaceWaitTime = 0;

    public ArrayThread(int id, Lock lock, Condition condition, String[] array,
            String[] pool) {
        this.id = id;
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
        long startTime = System.nanoTime();
        lock.lock();
        long endTime = System.nanoTime();
        searchWaitTime += (endTime - startTime);
        try {
            for (int i = array.length - 1; i > 0; i--) {
                if (array[i].equals(poolString)) {
                    System.out.printf("The last occurrence of %s is at index %d"
                     + "\n", poolString, i);
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void replaceString() {
        int index = new Random().nextInt(array.length);

        long startTime = System.nanoTime();
        lock.lock();
        long endTime = System.nanoTime();
        replaceWaitTime += (endTime - startTime);
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
    
    public void printWaitTimes() {
        System.out.println("Thread " + id + ": Average search wait time: " 
         + ((searchWaitTime / 1000000000.00000) / (long)NUM_OPERATIONS)
         + " seconds \n Average replace wait time: "
         + ((replaceWaitTime / 1000000000.00000) / (long)NUM_OPERATIONS)
         + " seconds \n");
    }
}
