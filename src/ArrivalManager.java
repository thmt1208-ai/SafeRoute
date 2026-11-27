public class ArrivalManager {
    private boolean arrivalPending = false;
    private Thread timer;
    
    public synchronized void startArrivalCheck(int timeout, Runnable onTimeout) {
        
        if (arrivalPending) {
        return;
    }

    arrivalPending = true;

    timer = new Thread(() -> {
        try {
            while (isArrivalPending()) {
                Thread.sleep(timeout * 1000L);

                synchronized (this) {
                    if (arrivalPending && Thread.currentThread() == timer) {
                        onTimeout.run();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });

    timer.start();
    }

    public synchronized boolean isArrivalPending() {
        return arrivalPending;
    }

    public synchronized void stopArrivalCheck() {
        arrivalPending = false;
    
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
        }
    }
}