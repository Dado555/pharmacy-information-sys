import { createStore } from 'vuex'

import getters from '@/store/getters'
import mutations from '@/store/mutations'
import actions from '@/store/actions'

export default createStore({
  state: {
    cart: {
      items: [],
      price: 0,
      date: Date.now(),
    },
    user: {},
    appointmentReport: {
      appointment: {id: -1},
      appointmentInfo: '',
      medicines: []
    },
    showReport: false,
    showMedicines: false
  },
  getters,
  mutations,
  actions,
  modules: {}
})
