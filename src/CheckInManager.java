import java.util.concurrent.atomic.AtomicBoolean;
public class CheckInManager {
    private final AtomicBoolean checkInPending = new AtomicBoolean(false);
    private int requestTime = 0;
    private int responseTimeout = 10;
    private Thread timer;

    public synchronized void requestCheckIn( int currentTime, int timeout, Runnable onTimeout) {

        if (checkInPending.compareAndSet(false, true)) {
            requestTime = currentTime;
            responseTimeout = timeout;

            System.out.println("Safety check-in required.");
            System.out.println("Please confirm that you are safe.");
            startTimer(timeout, onTimeout);
        }
    }
    public boolean isTimedOut(int currentTime) {
        return checkInPending.get()&& currentTime - requestTime >= responseTimeout;
    }
    public synchronized boolean confirmSafety() {
        if (checkInPending.compareAndSet(true, false)) {
            stopTimer();
            return true;
        }
        return false;
    }

    public boolean isCheckInPending() {
        return checkInPending.get();
    }

    public void resetCheckIn() {
        checkInPending.set(false);
        requestTime = 0;
    }

    private void startTimer(int timeout, Runnable onTimeout) {
        timer = new Thread(() -> {
            try {
                Thread.sleep(timeout * 1000L);
                synchronized (this) {
                    if (Thread.currentThread() == timer && checkInPending.compareAndSet(true, false)) {
                        onTimeout.run();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }); 
        timer.start();
    }
    private void stopTimer() {
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
        }
    }
}