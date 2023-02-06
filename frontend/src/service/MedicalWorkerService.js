import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix


class MedicalWorkerService {

    getReservation(reservationNumber, pharmacistId) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + 'medicine-reservations?id=' + reservationNumber + '&pharmacistId=' + pharmacistId,
        });
    }

    confirmReservation(reservation) {
        return axios({
            method: 'PUT',
            data: {id: reservation.id},
            url: `${API_URL}/` + 'medicine-reservations/medicine-issued',
        });
    }

    sendTimeOffRequest(startDate, endDate, content, medicalWorkerId, role) {
        return axios({
            method: 'POST',
            data: {startDate: startDate, endDate: endDate, content: content },
            url: `${API_URL}/` + 'time-off-requests/' + medicalWorkerId + '/' + role,
        });
    }

    getDoneAppointments(medicalWorkerId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'appointments/done/' + medicalWorkerId,
        });
    }

    saveAppointmentReport(appointmentReport) {
        return axios({
            method: 'POST',
            data: {appointmentId: appointmentReport.appointment.id, patientId: appointmentReport.appointment.patientId, appointmentInfo: appointmentReport.appointmentInfo, medicines: appointmentReport.medicines},
            url: `${API_URL}/` + 'appointment-reports/',
        });
    }

    loadFreeAppointments(userId, pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'appointments/free/' + userId + '/' + pharmacyId
        });
    }

    freeToReservedAppointment(appointmentId, patientId) {
        return axios({
            method: 'PUT',
            data: {patientId: patientId},
            url: `${API_URL}/` + 'appointment-reports/appointments/' + appointmentId + '/reserve'
        });
    }

    setBusy(medicalWorkerId) {
        return axios({
            method: 'PUT',
            data: {id: medicalWorkerId},
            url: `${API_URL}/` + 'medical-workers/' + medicalWorkerId + '/busy'
        });
    }

    setFree(medicalWorkerId) {
        return axios({
            method: 'PUT',
            data: {id: medicalWorkerId},
            url: `${API_URL}/` + 'medical-workers/' + medicalWorkerId + '/free'
        });
    }

    scheduleNewAppointment(startDate, endDate, patientId, workerId, pharmacyId) {
        return axios({
            method: 'POST',
            data: {title: 'COUNSELING', appointmentStatus: 'RESERVED', start: startDate, end: endDate, patientId: patientId, workerId: workerId, pharmacyId: pharmacyId},
            url: `${API_URL}/` + 'appointment-reports/new-appointment'
        });
    }

    updateMedicalWorkerInfo(updated) {
        return axios({
            method: 'PUT',
            data: updated,
            url: `${API_URL}/` + updated.role.toLocaleLowerCase().trim() + 's/' + updated.id
        });
    }

    prescribeMedicine(patientId, amount, pharmacyMedicineId) {
        return axios({
            method: 'PUT',
            data: {patientId: patientId, amount: amount},
            url: `${API_URL}/` + 'pharmacy-medicines/' + pharmacyMedicineId
        });
    }

    removeMedicine(amount, pharmacyMedicineId) {
        return axios({
            method: 'PUT',
            data: {amount: amount},
            url: `${API_URL}/` + 'pharmacy-medicines/' + pharmacyMedicineId + '/remove'
        });
    }

    getReplacementMedicines(pharmacyMedicineId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'pharmacy-medicines/' + pharmacyMedicineId + '/replacement-medicines'
        });
    }

    getPharmacyMedicines(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'pharmacies/' + pharmacyId + '/medicines'
        });
    }

    cancelAppointment(eventId, patientId) {
        return axios({
            method: 'PUT',
            data: {appointmentId: eventId},
            url: `${API_URL}/` + 'patients/' + patientId + '/penalty'
        });
    }

    getMedicalWorkerAppointments(role, userId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + role.toLocaleLowerCase().trim() + 's/' + userId + '/appointments',
        });
    }

    patientSearch(searchInput, userId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'patients?input=' + searchInput + '&id=' + userId,
        });
    }

    getAppointments(role, workerId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + role + 's/' + workerId + '/appointments'
        });
    }

    setAppointment(newAppointment, role) {
        return axios({
            method: 'POST',
            data: newAppointment,
            url: `${API_URL}/` + role + 's/new-appointment'
        });
    }

    getUnemployedPharmacists() {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'pharmacists/unemployed'
        });
    }

    hirePharmacist(pharmacistId, pharmacyId, start, end) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + 'pharmacists/' + pharmacistId + '/pharmacy=' + pharmacyId +
                '/start=' + start + '/end=' + end
        });
    }

    searchWorker(role, searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + role + 's/search',
            data: searchParameters
        });
    }

    updatePageSearchWorker(role, pageNumber, searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + role + 's/search?page=' + pageNumber,
            data: searchParameters
        });
    }

    getWorkerSchedule(role, workerId, pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + role + 's/' + workerId + '/work-schedule/' + pharmacyId
        });
    }

    getFutureAppointments(role, workerId, pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + role + 's/appointments-reserved-future/worker=' + workerId + '/pharmacy=' + pharmacyId
        });
    }

    firePharmacist(pharmacistId, pharmacyId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}/` + 'pharmacists/' + pharmacistId + "/" + pharmacyId
        })
    }

    workerRatings(role, workerId, period) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + role + 's/' + workerId + '/ratings-' + period
        })
    }

    workerAppointmentsStats(role, workerId, period) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + role + 's/' + workerId + '/appointments-' + period
        })
    }
}

export default new MedicalWorkerService();