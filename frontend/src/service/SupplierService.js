import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + '/suppliers'


class SupplierService {

    searchByCriteria(searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + 'search',
            data: searchParameters
        })
    }

    addSupplier(user) {
        return axios({
            method: 'POST',
            url: `${API_URL}`,
            data: user
        });
    }

    updateSupplier(id, data) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + id,
            data: data
        });
    }

    orders() {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + "orders"
        });
    }

    makeOrderOffer(id, data) {
        return axios({
            method: 'POST',
            url: `${API_URL}/` + id + "/make-order-offer",
            data: data
        });
    }

    suppliersPage() {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/page'
        })
    }

    orderOffers(id) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + id + "/order-offers"
        });
    }

    updateOrderOffer(data) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + "update-order-offer",
            data: data
        });
    }
}

export default new SupplierService();