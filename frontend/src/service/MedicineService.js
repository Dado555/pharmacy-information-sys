import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/medicines'


class MedicineService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`,
        });
    }

    findOne(medicineId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/`+ medicineId
        });
    }

    addMedicine(medicine) {
        return axios({
            method: 'POST',
            url: `${API_URL}`,
            data: medicine
        });
    }

    newMedicine(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}`+ '/pharmacy/' + pharmacyId + '/can-add'
        });
    }

    registerPharmacyMedicine(pharmacyId, medicineId) {
        return axios({
            method: 'POST',
            url: `${API_URL}`+ '/register-pharmacy=' + pharmacyId + '-medicine=' + medicineId
        });
    }

    searchMedicines(searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + 'search',
            data: searchParameters
        })
    }

    updatePage(page, searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + 'search?page=' + page,
            data: searchParameters
        })
    }

    searchPharmacyMedicines(searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + 'pharmacy-search',
            data: searchParameters
        })
    }

    updatePagePharmacyMedicines(searchParameters, pageNumber) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + 'pharmacy-search?page=' + pageNumber,
            data: searchParameters
        })
    }

    deletePharmacyMedicine(medicineId, pharmacyId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}/` + 'pharmacy' + pharmacyId + '/medicine' + medicineId,
        })
    }

    updatePharmacyMedicine(updated) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + 'pharmacy-medicine',
            data: updated
        })
    }

    makeMedicinePromotion(pharmacyId, medicineEdit) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + pharmacyId + '/pharmacy-medicine/action-price',
            data: medicineEdit
        })
    }

    medicineSoldStats(medicineId, period) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'pharmacy-medicine=' + medicineId + '/sold-' + period
        })
    }
}

export default new MedicineService();