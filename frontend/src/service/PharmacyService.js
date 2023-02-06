import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix + "/pharmacies";


class PharmacyService {

    findAll() {
        return axios({
            method: 'GET',
            url: `${API_URL}`
        })
    }

    findOne(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId
        })
    }

    addPharmacy(pharmacy) {
        return axios({
            method: 'POST',
            url: `${API_URL}`,
            data: pharmacy
        });
    }

    getEarningsAmount(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + "/earnings-all"
        })
    }

    findAllByDateAndTimeCriteria(startDate, startTime, endTime) {
        return axios({
            method: 'GET',
            url: `${API_URL}` + '/free?startDate=' + startDate + '&startTime=' + startTime + "&endTime=" + endTime
        })
    }

    findPharmacistsByDateAndTimeCriteria(pharmacyId, start, end) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/free?start=' + start + "&end=" + end
        })
    }

    searchPharmacies(searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + '/search',
            data: searchParameters
        })
    }

    updatePage(page, searchParameters) {
        return axios({
            method: 'POST',
            url: `${API_URL}` + '/search?page=' + page,
            data: searchParameters
        })
    }

    getMedicineInquiries(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/medicine-inquiries'
        })
    }

    resolveMedicineInquiry(inqId) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + 'medicine-inquiry-' + inqId + '/resolve'
        })
    }

    getPharmacyMedicines(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/medicines'
        })
    }

    getAppointments(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/appointments'
        })
    }

    getEmployedWorkers(pharmacyId, role) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId +'/' + role + 's'
        })
    }

    getOrders(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/orders'
        })
    }

    getOffersForOrder(orderId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'order-'+ orderId + '/offers'
        })
    }

    deleteOrder(orderId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}/` + 'order-'+ orderId + '/delete-order'
        })
    }

    filterOrders(pharmacyId, filterBy) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/orders/status-' + filterBy
        })
    }

    getTimeOffRequests(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/time-off-requests'
        })
    }

    approveTimeOff(requestId, status) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + 'time-off-request-' + requestId + '/status-' + status
        })
    }

    addOrderItem(orderId, itemDTO) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + orderId + '/new-order-item',
            data: itemDTO
        })
    }

    changePharmacyName(pharmacyId, name) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + pharmacyId + '/name-' + name
        })
    }

    changePharmacyAddress(pharmacyId, addressEdit) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + pharmacyId + '/new-address',
            data: addressEdit
        })
    }

    pharmacyMonthlyRating(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/ratings-monthly'
        })
    }

    pharmacyYearlyRating(pharmacyId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/ratings-yearly'
        })
    }

    newOrder(order) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + 'make-order',
            data: order
        })
    }

    getOrderOffers(orderId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + 'order-' + orderId + '/get-offers'
        })
    }

    acceptOrderOffer(orderId, offerId) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + 'order-' + orderId + '/accept-offer-' + offerId
        })
    }

    getOneOrder(pharmacyId, orderId) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + '/order-' + orderId
        })
    }

    updateOrderItem(orderId, itemId, item) {
        return axios({
            method: 'PUT',
            url: `${API_URL}/` + orderId + '/update-order-item-' + itemId,
            data: item
        })
    }

    deleteOrderItem(orderId, itemId) {
        return axios({
            method: 'DELETE',
            url: `${API_URL}/` + orderId + '/delete-order-item-' + itemId
        })
    }

    getEarningsStats(pharmacyId, period) {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + pharmacyId + "/earnings-" + period
        })
    }

    getAllPharmacies() {
        return axios({
            method: 'GET',
            url: `${API_URL}/` + "all"
        })
    }
}

export default new PharmacyService();