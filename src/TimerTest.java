import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
public class TimerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AtomicBoolean checkInPending = new AtomicBoolean(true);
        Thread timer = new Thread(() -> {
            try {
                Thread.sleep(10000);
                if (checkInPending.compareAndSet(true, false)) {
                    System.out.println("\nTime is up!");
                }
            } catch (InterruptedException e) {
                System.out.println("Timer cancelled.");
            }
        });
        timer.start();

        System.out.println("Are you safe?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.println("Waiting for response...");

        String response = scanner.nextLine();
        if (response.equals("1") || response.equals("2")) {
            if (checkInPending.compareAndSet(true, false)) {
                timer.interrupt();
                System.out.println("Response received.");
            } else {
                System.out.println("Check-in has already expired.");
            }
        }
    }
}