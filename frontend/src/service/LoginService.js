import axios from 'axios';
import config from '@/config';

const API_URL = config.apiHost + config.apiUrlPrefix


class LoginService {

    login(user) {
        return axios({
            method: 'POST',
            data: user,
            url: `${API_URL}/` + 'auth/login'
        })
    }

    changePassword(user) {
        return axios({
            method: 'PUT',
            data: user,
            url: `${API_URL}/` + 'auth/login/' + user.email
        })
    }

}

export default new LoginService();