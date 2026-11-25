// control contact list//
import java.util.ArrayList; 

public class ContactManager {
    private ArrayList<EmergencyContact> contacts; 
    public ContactManager() {
        contacts = new ArrayList<>(); 
    }
    public void addContacts(String name, String phoneNumber) {
        EmergencyContact contact = new EmergencyContact(name, phoneNumber);
        contacts.add(contact);
    }
    public ArrayList<EmergencyContact> getContacts() {
        return contacts;
    }
    public void removeContact(int index) {
        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index); 
        } else {
            System.out.println("Invalid contact index.");
        }
    }
}