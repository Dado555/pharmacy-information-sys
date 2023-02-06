import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/appointments'

class AppointmentService {

    findScheduledAppointments() {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/scheduled'
        });
    }

    findAppointmentHistory() {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/history'
        });
    }

    searchAppointments(searchParameters) {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/history/search?sortBy=' + searchParameters
        });
    } 

    addAppointmentCounseling(appointmentData) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + '/scheduled/counseling',
            data: appointmentData
        });
    }

    addAppointmentExamination(appointmentId) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + '/scheduled/examination?id=' + appointmentId,
        });
    }

    cancelAppointmentCounseling(appointmentId) {
        return axios({
            method: 'PUT',
            url: `${API_URL}` + '/scheduled/counseling/' + appointmentId + '/cancel',
        });
    }

    cancelAppointmentExamination(appointmentId) {
        return axios({
            method: 'PUT',
            url: `${API_URL}` + '/scheduled/examination/' + appointmentId + '/cancel',
        });
    }

}

export default new AppointmentService();