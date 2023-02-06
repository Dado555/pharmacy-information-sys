import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/patients'


class PatientService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`,
        });
    }

    findOne(id) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + id
        });
    }

    registerUser(user) {
        return axios({
            method: 'POST',
            url: `${API_URL}`,
            data: user
        });
    }

    findAllergicMedicines(patientId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/`+ patientId + '/allergic-medicines'
        });
    }

    findReservedMedicines(patientId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/`+ patientId + '/reserved-medicines'
        });
    }

    findEPrescriptions(patientId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + patientId + '/e-prescriptions'
        });
    }

    findComplaints(patientId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + patientId + '/complaints'
        })
    }

    updatePatient(patientId, data) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + patientId,
            data: data
        });
    }

    searchEPrescriptions(patientId, searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/e-prescriptions/search',
            data: searchParameters
        });
    }

    addComplaint(patientId, data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/complaints',
            data: data
        });
    }

    addMedicineReservation(patientId, data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/reserved-medicines',
            data: data
        });
    }

    cancelMedicineReservation(patientId, reservationId) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + patientId + '/reserved-medicines/' + reservationId
        });
    }

    addAllergicMedicines(patientId, data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/allergic-medicines',
            data: data
        });
    }

    deleteAllergicMedicine(patientId, medicineId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}/` + patientId + '/allergic-medicines?medicineId=' + medicineId
        });
    }

    loadPharmaciesAfterQRCode(patientId, data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/upload_qrcode',
            data: data
        });
    }

    buyMedicinesAfterQRCode(patientId, qrCodeResultId) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/buy_after_qrcode/' + qrCodeResultId
        });
    }

    setAppointment(appointmentDTO) {
        return axios({
            method: 'PUT',
            url: `${API_URL}` + '/set-appointment',
            data: appointmentDTO
        });
    }

    isSubForPharmacy(patientId, pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + patientId + '/is-sub-pharmacy=' + pharmacyId
        });
    }

    subInPharmacy(patientId, pharmacyId) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + patientId + '/subscribe-pharmacy=' + pharmacyId
        });
    }

    unsubInPharmacy(patientId, pharmacyId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}/` + patientId + '/unsubscribe-pharmacy=' + pharmacyId
        });
    }

    loadSubs(patientId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + patientId + '/subscriptions'
        });
    }
}

export default new PatientService();