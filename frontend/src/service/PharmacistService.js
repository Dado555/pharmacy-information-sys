import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/pharmacists'


class PharmacistService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`,
        });
    }

    searchPharmacists(searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + '/search',
            data: searchParameters
        })
    }

    pharmacistsPage() {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/page',
        })
    }

    updatePage(page, searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + '/search?page=' + page,
            data: searchParameters
        })
    }
}

export default new PharmacistService();