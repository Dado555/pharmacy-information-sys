import axios from 'axios'

export default {

    // getMedicalW  orker({ commit }) {
    //     let token = "asd";
    //     axios({
    //         method: 'GET',
    //         url: 'http://localhost:8081/api/dermatologists/10',
    //         headers: {
    //             'Authorization': 'Bearer ' + token,
    //           }
    //     }).then((response) => {
    //         commit('updateMedicalWorker', response.data);
    //     });
    // },
    // getPatient({ commit }) {
    //     axios({
    //         method: 'GET',
    //         url: 'http://localhost:8081/api/patients/8'
    //     }).then((response) => {
    //         commit('updatePatient', response.data);
    //     });
    // },
    getUser({ commit }) {
        if(!localStorage.getItem('token'))
            return ;

        let token = localStorage.getItem('token').substring(1, localStorage.getItem('token').length-1);
        axios({
            method: 'GET',
            url: 'https://pis-back.herokuapp.com/api/auth/authority',
            headers: {
                'Authorization': 'Bearer ' + token,
              }
        }).then((response) => {
            let arr = response.data.role.split('_');
            if(arr.length > 2)
                response.data.role = arr.slice(1).join("_");
            else
                response.data.role = arr[1];
            console.log(response.data.role);
            commit('updateUser', response.data);
        });
    },
    getUserRole({ getters }) {
        return getters.getUser.role;
    },
    getPharmacyAdminId({getters}) {
        return getters.getUser.id;
    },
    addItemToCart({ commit }, { pharmacyMedicineDTO, amount }) {
        commit('addToCart', { pharmacyMedicineDTO, amount });
    }
}