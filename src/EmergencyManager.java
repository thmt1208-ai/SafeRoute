// simulate alert// 
public class EmergencyManager {
    public void sendAlert(ContactManager contactManager) {
        if (contactManager.getContacts().isEmpty()) {
            System.out.println("No emergency contacts available.");
            return;
        }
        System.out.println("\n===== Simulated Emergency Alert =====");
        for (EmergencyContact contact : contactManager.getContacts()) {
             System.out.println("Simulated alert to: " + contact.getName());
        }

        System.out.println("Alert simulation completed.");
    }
}