export default {

    updateMedicalWorker(state, medicalWorker) {
        state.medicalWorker = medicalWorker;
    },
    updatePatient(state, patient) {
        state.patient = patient;
    },
    updateUser(state, user) {
        state.user = user;
    },
    addToCart(state, { pharmacyMedicineDTO, amount }) {
        let itemInCart = state.cart.items.find(item => { return item.pharmacyMedicineDTO.id === pharmacyMedicineDTO.id; } )
        if (itemInCart) {
            itemInCart.amount = parseInt(itemInCart.amount, 10) + parseInt(amount, 10);
            return;
        }
        
        state.cart.items.push( { pharmacyMedicineDTO, amount } );
    },
    updateAppointmentReport(state, appointment) {
        state.appointmentReport.appointmentInfo = '';
        state.appointmentReport.medicines = [];
        state.appointmentReport.appointment = appointment;
    },
    prescribeMedicine(state, medicine) {
        let update =  state.appointmentReport.medicines.find(t => t.id === medicine.id);
        if (update) {
            update.amount += medicine.amount;
            update.therapy += medicine.therapy;
        } else {
            state.appointmentReport.medicines.push(medicine);
        }
    },
    removeMedicine(state, medicine) {
        let i = state.appointmentReport.medicines.map(item => item.id).indexOf(medicine.id);
        state.appointmentReport.medicines.splice(i, 1);
    },
    updateShowReport(state, show) {
        state.showReport = show;
    },
    updateShowMedicines(state, show) {
        state.showMedicines = show;
    },
    updateAppointmentStatus(state, status) {
        state.appointmentReport.appointment.status = status;
    }
}