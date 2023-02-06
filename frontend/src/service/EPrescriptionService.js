import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/e-prescriptions'


class EPrescriptionService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`,
        });
    }

    findMedicinesForEPrescription(id) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + id + '/medicines',
        })
    }

}

export default new EPrescriptionService();