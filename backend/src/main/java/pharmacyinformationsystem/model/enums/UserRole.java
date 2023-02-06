package pharmacyinformationsystem.model.enums;

public enum UserRole {
    SYSTEM_ADMIN("System admin"), PHARMACY_ADMIN("Pharmacy admin"), SUPPLIER("Supplier"), DERMATOLOGIST("Dermatologist"), PHARMACIST("Pharmacist"), PATIENT("Patient");

    private String userRole;

    UserRole(String s) {
        this.userRole = s;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
