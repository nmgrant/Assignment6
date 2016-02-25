
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ArrayThread extends Thread {
    private final int id;
    private final Lock lock;
    private final Condition condition;
    private final int NUM_OPERATIONS = 50;
    private String[] array, pool;
    ArrayList<Double> searchWaitTime = new ArrayList<>();
    ArrayList<Double> replaceWaitTime = new ArrayList<>();

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
        searchWaitTime.add((endTime - startTime) / 1000000000.000000000);
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
        replaceWaitTime.add((endTime - startTime) / 1000000000.000000000);
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
        
        double averageSearchTime = 0;
        for (double searchTime : searchWaitTime) {
            averageSearchTime += searchTime;
        }
        averageSearchTime = (averageSearchTime / searchWaitTime.size());
        
        double averageReplaceTime = 0;
        for (double replaceTime : replaceWaitTime) {
            averageReplaceTime += replaceTime;
        }
        averageReplaceTime = (averageReplaceTime / replaceWaitTime.size());
        
        double searchStandardDev = 0;
        for (double searchTime : searchWaitTime) {
            searchStandardDev += Math.pow(searchTime - averageSearchTime, 2);
        }
        searchStandardDev /= searchWaitTime.size();
        searchStandardDev = Math.sqrt(searchStandardDev);
        
        double replaceStandardDev = 0;
        for (double replaceTime : replaceWaitTime) {
            replaceStandardDev += Math.pow(replaceTime - averageReplaceTime, 2);
        }
        replaceStandardDev /= replaceWaitTime.size();
        replaceStandardDev = Math.sqrt(replaceStandardDev);
        
        System.out.println("Thread " + id + ": Average search wait time: " 
         + (averageSearchTime)
         + " seconds \n Average replace wait time: "
         + (averageReplaceTime)
         + " seconds \n Search Time Standard Deviation: "
         + (searchStandardDev) + "\n Replace Time Standard Deviation: "
         + (replaceStandardDev) + "\n");        
    }
}
