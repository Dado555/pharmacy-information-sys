package pharmacyinformationsystem.model.enums;

public enum OrderStatus {
    IN_PROGRESS(1), WAIT_DECISION(2), DONE(3);

    private int OrderStatus;
    OrderStatus(int i) { this.OrderStatus = i;}

    public int getOrderStatus() {
        return OrderStatus;
    }
    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

}
