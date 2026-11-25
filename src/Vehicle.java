public class Vehicle {
    private String vehicleType;
    private String licensePlate;
    private String driverName;
    public Vehicle(String vehicleType, String licensePlate, String driverName) {
        this.vehicleType = vehicleType; 
        this.licensePlate = licensePlate; 
        this.driverName = driverName; 
    }
    public String getVehicleType() {
        return vehicleType; 
    }
    public String getLicensePlate() {
        return licensePlate; 
    }
    public String getDriverName() {
        return driverName; 
    }
}