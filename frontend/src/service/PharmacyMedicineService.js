import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/pharmacy-medicines'


class PharmacyMedicineService {

    findMedicinesFromPharmacy(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId
        })
    }

    findPharmaciesForMedicine(medicineId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/pharmacies/` + medicineId
        })
    }
}

export default new PharmacyMedicineService();