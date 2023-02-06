import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + ':' + config.apiPort + config.apiUrlPrefix + '/systemAdmins'


class SystemAdminService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`,
        });
    }

    findOne(id) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + id
        });
    }

    addSystemAdmin(user) {
        return axios({
            method: 'POST',
            url: `${API_URL}`,
            data: user
        });
    }

    getComplaints(id) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + id + "/complaints"
        });
    }

    getAllComplaints() {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + "complaints"
        });
    }

    updateComplaint(id, data) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + "update_complaint/" + id,
            data: data
        });
    }

    loadComplaints(id) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + id + "/complaints"
        });
    }

    systemAdminsPage() {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/page',
        })
    }
}

export default new SystemAdminService();