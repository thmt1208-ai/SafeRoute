import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TripHistory {
    private final String filePath = "data/trip_history.txt";

    public void saveTrip(Trip trip, String status, int elapsedTime) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filePath, true));

            writer.println("===== Trip Information =====");
            writer.println("Start: " + trip.getStartLocation());
            writer.println("Destination: " + trip.getDestination());
            writer.println("Planned route: " + trip.getPlannedRoute());

            writer.println(""); 

            writer.println("Vehicle type: " + trip.getVehicle().getVehicleType());
            writer.println("License plate: " + trip.getVehicle().getLicensePlate());
            writer.println("Driver name: " + trip.getVehicle().getDriverName());

            writer.println(""); 

            writer.println("Status: " + status);
            writer.println("Elapsed time: " + elapsedTime + " minutes");
            writer.println();

            writer.println(""); 

            writer.close();

            System.out.println("Trip saved successfully.");
        } catch (IOException e) {
            System.out.println("Unable to save trip information.");
        }
    }

    public void viewHistory() {
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
    
            System.out.println("\n===== Trip History =====");
            System.out.println(""); 
    
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
    
            reader.close();
    
        } catch (FileNotFoundException e) {
            System.out.println("No trip history available.");
        }
    }
}
