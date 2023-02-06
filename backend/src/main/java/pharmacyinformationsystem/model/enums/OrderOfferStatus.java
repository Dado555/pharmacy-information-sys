package pharmacyinformationsystem.model.enums;

public enum OrderOfferStatus {
    UNKNOWN(1), APPROVED(2), REJECTED(3);

    private int OrderOfferStatus;

    OrderOfferStatus(int i) {
        this.OrderOfferStatus = i;
    }

    public int getOrderOfferStatus() {
        return OrderOfferStatus;
    }

    public void setOrderOfferStatus(int orderOfferStatus) {
        OrderOfferStatus = orderOfferStatus;
    }
}
