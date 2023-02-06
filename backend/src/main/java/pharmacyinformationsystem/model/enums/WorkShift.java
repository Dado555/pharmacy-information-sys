package pharmacyinformationsystem.model.enums;

public enum WorkShift {
    FIRST_SHIFT(1), SECOND_SHIFT(2);

    private int workShift;

    WorkShift(int i) {
        this.workShift = i;
    }

    public int getWorkShift() {
        return workShift;
    }

    public void setWorkShift(int workShift) {
        this.workShift = workShift;
    }
}
