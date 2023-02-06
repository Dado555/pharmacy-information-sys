import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/dermatologists'


class DermatologistService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`,
        });
    }

    addDermatologist(user) {
        return axios({
            method: 'POST',
            url: `${API_URL}`,
            data: user
        });
    }

    dermatologistsPage() {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + "page",
        });
    }

    search(data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + "search",
            data: data
        });
    }

    searchPage(pageNumber, data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + "search?page=" + pageNumber,
            data: data
        });
    }
}

export default new DermatologistService();