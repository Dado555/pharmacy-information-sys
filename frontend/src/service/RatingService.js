import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/ratings'

class RatingService {

    getAverageRatingForPharmacy(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/pharmacy/` + pharmacyId
        })
    }

    getAverageRatingForMedicine(medicineId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/medicine/` + medicineId
        })
    }

    getAverageRatingForMedicalWorker(medicalWorkerId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/medical-worker/` + medicalWorkerId
        })
    }

    addPharmacyRating(pharmacyId, rating) {
        return axios({
            method: 'POST',
            url: `${API_URL}/pharmacy/` + pharmacyId + "?value=" + rating
        });
    }

    addMedicineRating(medicineId, rating) {
        return axios({
            method: 'POST',
            url: `${API_URL}/medicine/` + medicineId + "?value=" + rating
        });
    }

    addMedicalWorkerRating(medicalWorkerId, rating) {
        return axios({
            method: 'POST',
            url: `${API_URL}/medical-worker/` + medicalWorkerId + "?value=" + rating
        });
    }

}

export default new RatingService();