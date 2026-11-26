import java.util.concurrent.atomic.AtomicBoolean;
public class CheckInManager {
    private final AtomicBoolean checkInPending = new AtomicBoolean(false);
    private int requestTime = 0;
    private int responseTimeout = 10;

    public void requestCheckIn(int currentTime, int timeout) {
        if (checkInPending.compareAndSet(false, true)) {
            requestTime = currentTime;
            responseTimeout = timeout;

            System.out.println("Safety check-in required.");
            System.out.println("Please confirm that you are safe.");
        }
    }
    public boolean isTimedOut(int currentTime) {
        return checkInPending.get()&& currentTime - requestTime >= responseTimeout;
    }
    public void confirmSafety() {
        checkInPending.set(false);
    }

    public boolean isCheckInPending() {
        return checkInPending.get();
    }

    public void resetCheckIn() {
        checkInPending.set(false);
        requestTime = 0;
    }
}