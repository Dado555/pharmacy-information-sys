import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + "/pharmacyAdmins/";


class PharmacyAdminService {

    findOne(adminId) {
        return axios({
            method: 'GET',
            url: `${API_URL}` + adminId
        })
    }

    updateUser(adminId, updated) {
        return axios({
            method: 'PUT',
            url: `${API_URL}` + adminId,
            data: updated
        })
    }

    addPharmacyAdmin(user, id) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + id,
            data: user
        });
    }

    pharmacyAdminPage() {
        return axios({
            method: 'GET',
            url: `${API_URL}` + "page",
        });
    }
}

export default new PharmacyAdminService();