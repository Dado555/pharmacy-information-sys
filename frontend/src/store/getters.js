export default {

    getMedicalWorker: state => {
        return state.medicalWorker;
    },
    getPatient: state => {
        return state.patient;
    },
    getUser: state => {
      return state.user;
    },
    cart: state => {
        return state.cart;
    },
    cartItemCount: state => {
        return state.cart.length;
    },
    cartPrice: state => {
        let total = 0;
        state.cart.items.forEach(item => {
            total += item.pharmacyMedicineDTO.price * item.amount;
        })
        return total;
    },
    cartTotalPrice: state => {
        let total = 0;
        state.cart.items.forEach(item => {
            total += (item.pharmacyMedicineDTO.price - item.pharmacyMedicineDTO.price * state.user.discount / 100) * item.amount;
        })
        return total;
    },
    discount: state => {
        let total = 0;
        state.cart.items.forEach(item => {
            total += (item.pharmacyMedicineDTO.price * state.user.discount / 100) * item.amount
        })
        return total;
    },
    medicineReservationDTO: (state, getters) => {
        return  { 
                    "medicineReservationItemDTOList": state.cart.items, 
                    "price": getters.cartTotalPrice,
                    "discount": getters.discount,
                    "dateAndTime": state.cart.date
                };
    },
    getAppointmentReport: state => {
        return state.appointmentReport;
    },
    showReport: state => {
        return state.showReport;
    },
    showMedicines: state => {
        return state.showMedicines;
    }
}