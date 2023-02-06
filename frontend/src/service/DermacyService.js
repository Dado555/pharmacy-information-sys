import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + "/dermacy/";


class DermacyService {

    findEmployableDermatologists(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}` + 'can-employ-pharmacy=' + pharmacyId
        })
    }

    getDermatologistSchedule(dermatologistId) {
        return axios({
            method: 'GET',
            url: `${API_URL}` + 'dermatologist=' + dermatologistId + "-schedule"
        })
    }

    hireDermatologist(workSchedule) {
        return axios({
            method: 'PUT',
            data: workSchedule,
            url: `${API_URL}` + 'hire-dermatologist'
        })
    }

    fireDermatologist(dermatologistId, pharmacyId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}` + dermatologistId + "/" + pharmacyId
        })
    }
}

export default new DermacyService();