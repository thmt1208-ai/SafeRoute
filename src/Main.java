import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        Trip currentTrip = null;
        ContactManager contactManager = new ContactManager();
        EmergencyManager emergencyManager = new EmergencyManager();
        GPSSimulator gpsSimulator = null;
        SafetyMonitor safetyMonitor = new SafetyMonitor();
        SafetySetting safetySetting = new SafetySetting(); 
        CheckInManager checkInManager = new CheckInManager(); 
        ArrivalManager arrivalManager = new ArrivalManager();
        int currentTime = 0;
        boolean warningIssued = false;
        do { 
            System.out.println("\n===== SafeRoute =====");
            System.out.println("1. Start a new trip");
            System.out.println("2. View trip information");
            System.out.println("3. Manage emergency contacts");
            System.out.println("4. View trip history");
            System.out.println("5. Emergency menu");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                choice = 0;
                continue;
            }

        if (choice == 1) {
            System.out.print("Enter starting location: ");
            String start = scanner.nextLine();

            System.out.print("Enter destination: ");
            String destination = scanner.nextLine();

            currentTrip = new Trip(start, destination);
            currentTrip.addLocation(start);

            gpsSimulator = new GPSSimulator(start);
            safetyMonitor.resetRisk();
            checkInManager.resetCheckIn();
            arrivalManager.stopArrivalCheck();
            currentTime = 0;
            warningIssued =false;


            System.out.println("Enter route stops (type 'done' to finish): ");

            while (true) {
                String location = scanner.nextLine();

                if (location.equalsIgnoreCase("done")) {
                    break;
                }

                currentTrip.addLocation(location);
            }

            currentTrip.addLocation(destination);
            
            System.out.println("\n===== Vehicle Information =====");
            System.out.print("Enter vehicle type: ");
            String vehicleType = scanner.nextLine();
            System.out.print("Enter license plate: ");
            String licensePlate = scanner.nextLine();
            System.out.print("Enter driver name: ");
            String driverName = scanner.nextLine();
            Vehicle vehicle = new Vehicle(vehicleType, licensePlate, driverName);
            currentTrip.setVehicle(vehicle);
            System.out.println("Trip created successfully!");
            System.out.println("Safety monitoring activated (simulation).");

            String tripChoice;
            do {
                System.out.println("\n===== Active Trip =====");
                System.out.println("1. Update simulated location");
                System.out.println("2. View safety status");
                System.out.println("3. Simulate emergency alert");
                System.out.println("4. End trip");

                if (gpsSimulator.getCurrentLocation().equalsIgnoreCase(currentTrip.getDestination())) {
                    System.out.println("Type ARRIVED to confirm safe arrival.");
                }

                if (checkInManager.isCheckInPending()) {
                    System.out.print("Enter YES, NO, or a menu choice: ");
                } else {
                    System.out.print("Enter your choice: ");
                }

                tripChoice = scanner.nextLine().trim();

                if (tripChoice.equalsIgnoreCase("ARRIVED")) {
                    if (gpsSimulator.getCurrentLocation().equalsIgnoreCase( currentTrip.getDestination())) {
                        arrivalManager.stopArrivalCheck();
                        checkInManager.resetCheckIn();
                        safetyMonitor.clearArrivalRisk();
                
                        System.out.println("\nSafe arrival confirmed.");
                        System.out.println("Trip completed successfully.");
                        System.out.println("Returning to main menu...");
                
                        break;
                    } else {
                        System.out.println("You have not reached your destination.");
                    }
                    continue;
                }
                
                if (tripChoice.equalsIgnoreCase("yes")) {
                    if (checkInManager.confirmSafety()) {
                        safetyMonitor.clearCheckInRisk();
                        System.out.println("Safety confirmed successfully.");
                    } else {
                        System.out.println("No active safety check-in.");
                    }
                    continue;
                }
                
                if (tripChoice.equalsIgnoreCase("no")) {
                    if (checkInManager.confirmSafety()) {
                        safetyMonitor.activateEmergencyRisk();
                        emergencyManager.sendAlert(contactManager);
                    } else {
                        System.out.println("No active safety check-in.");
                    }
                    continue;
                }

                if (tripChoice.equals("1")) {
                    System.out.print("Enter current location: ");
                    String newLocation = scanner.nextLine();

                    gpsSimulator.updateLocation(newLocation);
                    currentTime += safetySetting.getRouteCheckInterval();
                    safetyMonitor.updateRouteRisk(currentTrip, gpsSimulator.getCurrentLocation()); 

                    if (gpsSimulator.getCurrentLocation().equalsIgnoreCase(
                        currentTrip.getDestination())) {
                
                        if (!arrivalManager.isArrivalPending()) {
                            System.out.println("\n===== DESTINATION REACHED =====");
                            System.out.println("Have you arrived safely?");
                            System.out.println("Type ARRIVED to confirm.");
                            System.out.println("Please respond within 10 seconds .");

                            Runnable onArrivalTimeout = () -> {
                                safetyMonitor.increaseArrivalRisk();
                            
                                System.out.println("\nArrival confirmation missed.");
                                System.out.println("Risk score: " + safetyMonitor.getRiskScore());
                                if (safetyMonitor.getRiskScore() >= 70) {
                                    emergencyManager.sendAlert(contactManager);
                                    arrivalManager.stopArrivalCheck();
                                } else {
                                    System.out.println("Have you arrived safely?");
                                    System.out.println("Type ARRIVED to confirm.");
                                }
                            };
                            
                            arrivalManager.startArrivalCheck(10, onArrivalTimeout);
                        }
                    }

                    System.out.println("Current location: " + gpsSimulator.getCurrentLocation());
                    System.out.println("Elapsed time: " + currentTime + " minutes");
                    System.out.println("Risk score: " + safetyMonitor.getRiskScore());
                    System.out.println("Risk level: " + safetyMonitor.getRiskLevel());

                    if ((safetyMonitor.getRiskScore() >= safetySetting.getWarningThreshold()) && !warningIssued && !checkInManager.isCheckInPending()) {
                        Trip monitoredTrip = currentTrip;
                        GPSSimulator monitoredGPS = gpsSimulator;
                        Runnable onTimeout = () -> {
                            System.out.println("\nCheck-in timeout.");
                            System.out.println("Checking GPS location again...");
                        
                            if (safetyMonitor.isOffRoute(monitoredTrip, monitoredGPS.getCurrentLocation())) {
                                safetyMonitor.markCheckInMissed();
                        
                                System.out.println("User is still off route.");
                                System.out.println("Risk score: " + safetyMonitor.getRiskScore());
                        
                                emergencyManager.sendAlert(contactManager);
                        
                            } else {
                                System.out.println("User is back on the planned route.");
                                System.out.println("No emergency alert needed.");
                            }
                        };
                        checkInManager.requestCheckIn(currentTime, safetySetting.getCheckInTimeout(), onTimeout);
                        warningIssued = true; 

                        System.out.println("\n===== SAFETY CHECK-IN =====");
                        System.out.println("Are you safe?");
                        System.out.println("Type YES to confirm your safety.");
                        System.out.println("Type NO if you need help.");
                        System.out.println("Response timeout: "+ safetySetting.getCheckInTimeout() + " seconds (demo).");

                    }

                    if (safetyMonitor.getRiskScore() < safetySetting.getWarningThreshold()) {
                        warningIssued = false;
                    }
                } else if (tripChoice.equals("2")) {
                    System.out.println("\n===== Safety Status =====");
                    System.out.println("Current location: " + gpsSimulator.getCurrentLocation());     
                    System.out.println("Elapsed time: " + currentTime + " minutes");        
                    System.out.println("Risk score: " + safetyMonitor.getRiskScore());
                    System.out.println("Risk level: " + safetyMonitor.getRiskLevel());
                    System.out.println("Check-in pending: " + checkInManager.isCheckInPending());
                } else if (tripChoice.equals("3")) {
                    checkInManager.resetCheckIn();
                    safetyMonitor.activateEmergencyRisk();
                    System.out.println("Emergency mode activated.");
                    System.out.println("Risk score: " + safetyMonitor.getRiskScore());
                    emergencyManager.sendAlert(contactManager);
                } else if (tripChoice.equals("4")) {
                    arrivalManager.stopArrivalCheck();
                    checkInManager.resetCheckIn();
                    System.out.println("Trip ended successfully.");
                    System.out.println("Returning to main menu...");
                } else {
                    System.out.println("Invalid choice. Try again.");
                }

            } while (!tripChoice.equals("4"));


        }
        else if (choice == 2) {
            if (currentTrip == null) {
                System.out.println("No trip has been created.");
            } else {
            System.out.println("Start: " + currentTrip.getStartLocation());
            System.out.println("Destination: " +currentTrip.getDestination()); 
            System.out.println("Planned route: " +currentTrip.getPlannedRoute());
            System.out.println("Vehicle type: " +currentTrip.getVehicle().getVehicleType());
            System.out.println("License plate: " +currentTrip.getVehicle().getLicensePlate());
            System.out.println("Driver name: " +currentTrip.getVehicle().getDriverName());
            }
        }
        else if (choice == 3) {
            int contactChoice; 
            do { 
                System.out.println("\n===== Emergency Contacts =====");
                System.out.println("1. Add emergency contact");
                System.out.println("2. View emergency contacts");
                System.out.println("3. Remove emergency contact");
                System.out.println("4. Back to main menu");
                System.out.print("Enter your choice: ");
                try {
                    contactChoice = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input.");
                    scanner.nextLine();
                    contactChoice = 0;
                    continue; 
                }
                if (contactChoice == 1) {
                    System.out.print("Enter contact name: ");
                    String name = scanner.nextLine();
                    while (true) {
                        System.out.print("Enter phone number: ");
                        String phoneNumber = scanner.nextLine();
                        if (phoneNumber.matches("\\+?[0-9]{10,15}")) {
                            contactManager.addContacts(name, phoneNumber);
                            System.out.println("Contact added successfully!");
                            break;
                        } else {
                            System.out.println("Invalid phone number.");
                        }
                    }
                } else if (contactChoice == 2) {
                    if (contactManager.getContacts().isEmpty()) {
                        System.out.println("No emergency contacts.");
                    } else {
                        for (int i = 0; i < contactManager.getContacts().size(); i++) {
                            EmergencyContact contact = contactManager.getContacts().get(i);

                            System.out.println((i + 1) + ". " + contact.getName() + " - "  + contact.getPhoneNumber());
                        }
                    }
                } else if (contactChoice == 3) {
                    if (contactManager.getContacts().isEmpty()) {
                        System.out.println("No contacts to remove.");

                    } else {
                        for (int i = 0; i < contactManager.getContacts().size(); i++) {
                            EmergencyContact contact = contactManager.getContacts().get(i);
                            System.out.println((i + 1) + ". " + contact.getName());
                        }
                        System.out.print("Enter contact number to remove: ");

                        try {
                            int index = scanner.nextInt() - 1;
                            scanner.nextLine();

                            if (index >= 0 && index < contactManager.getContacts().size()) {
                                contactManager.removeContact(index);
                                System.out.println("Contact removed successfully!");
                            } else {
                                System.out.println("Invalid contact number.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input.");
                            scanner.nextLine();
                        }
                    }
                }   
            } while (contactChoice != 4);
        }
        else if (choice == 5) {
            String emergencyChoice; 
            do {
                System.out.println("\n===== Emergency Menu =====");
                System.out.println("1. Simulate emergency alert");
                System.out.println("2. Back to main menu");
                System.out.print("Enter your choice: ");

                emergencyChoice = scanner.nextLine();
                if (emergencyChoice.equals("1")) {
                    emergencyManager.sendAlert(contactManager);
                } else if (emergencyChoice.equals("2")) {
                    System.out.println("Returning to main menu...");
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } while (!emergencyChoice.equals("2"));
        }
        } while (choice != 6);
        System.out.println("Thank you for using SafeRoute!");
        scanner.close();
    }
}
