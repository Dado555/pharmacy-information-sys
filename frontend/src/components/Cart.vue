<template>
<Navbar></Navbar>
<div class="container cart">
  <table>
      <tr>
          <th>Medicine</th>
          <th>Pharmacy</th>
          <th>Manufacture</th>
          <th>Type</th>
          <th>Form</th>
          <th>Structure</th>
          <th>Amount</th>
          <th>Price</th>
          <th>Discount</th>
          <th>&nbsp;</th>
      </tr>
      <tr v-for="item in cart.items" :key="item.id">
          <td>
            <div class="cart-info">
                <img src="../assets/pharmacy.png">
                <div>
                    <h5>{{ item.pharmacyMedicineDTO.medicineDTO.name }}</h5>
                    <small>Price: {{ item.pharmacyMedicineDTO.price }}$</small>
                    <br>
                    <a href="">Remove</a>
                </div>
            </div>
          </td>
          <td> {{ item.pharmacyMedicineDTO.pharmacyDTO.name }} </td>
          <td> {{ item.pharmacyMedicineDTO.medicineDTO.manufacture }} </td>
          <td> {{ item.pharmacyMedicineDTO.medicineDTO.type }} </td>
          <td> {{ item.pharmacyMedicineDTO.medicineDTO.medicineForm }} </td>
          <td> {{ item.pharmacyMedicineDTO.medicineDTO.structure }} </td>
          <td><input type="number" :value="item.amount" readonly></td>
          <td> {{ item.amount * item.pharmacyMedicineDTO.price }}$ </td>
          <td> {{ item.amount * (item.pharmacyMedicineDTO.price * user.discount / 100) }}$ </td>
      </tr>
  </table>
  <div class="total-price">
      <table>
          <tr>
              <td>Price:</td>
              <td> {{ cartPrice }}$</td>
          </tr>
          <tr>
              <td>Discount:</td>
              <td>{{ discount }}$</td>
          </tr>
          <tr>
              <td>Total:</td>
              <td>{{ cartTotalPrice }}$</td>
          </tr>
          <tr>
              <td>Choose reservation date: </td>
              <td><datepicker inputFormat="dd.MM.yyy." v-model="date" :lowerLimit="Date.now() + 172800000" /></td>
           </tr>
          <tr><button href='#' @click=submit type="button" class="btn btn-outline-primary" value="Submit">Reserve</button><td></td></tr>
      </table>
  </div>
</div>
</template>

<script>
import PatientService from '@/service/PatientService';
import Navbar from '@/components/Navbar.vue';

import Datepicker from 'vue3-datepicker';
import { ref } from 'vue';

export default {
  data() {
      return {
          date: Date.now() + 172800000
      }
  },
  name: "Cart",
  components: { Navbar, Datepicker },
  computed: {
    user() {
        return this.$store.getters.getUser;
    },
    cart() {
        return this.$store.getters.cart;
    },
    cartTotalPrice() {
        return this.$store.getters.cartTotalPrice;
    },
    discount() {
        return this.$store.getters.discount;
    },
    cartPrice() {
        return this.$store.getters.cartPrice;
    },

    medicineReservationDTO() {
        let date = new Date(this.date);
        this.$store.getters.cart.date = date.getTime();
        return this.$store.getters.medicineReservationDTO;
    }
  },
  methods: {
      submit() {
        if (this.medicineReservationDTO.medicineReservationItemDTOList.length == 0) {
            this.toast("Cart is empty!", 'info');
            return;
        }

        PatientService.addMedicineReservation(this.user.id, this.medicineReservationDTO)
        .then((response) => {
            this.cart.items = [];
            this.cart.price = 0;
            this.toast('Reservation items reserved successfully! Check mail for confirmation.', 'info');
        }).catch((error) => { 
            this.toast(error.response.data.message, 'error'); 
        });
      },

      toast(message, type) {
        this.$toast.show(message, {
            type: type,
            position: 'top',
            duration: 2500
        })
      }
  }
};
</script>

<style scoped>

.cart {
    margin: 80px auto;
    text-align: left;
}

.cart table {
    width: 100%;
    border-collapse: collapse;
}

.cart-info {
    display: flex;
    flex-wrap: wrap;
}

.cart th {
    text-align: left;
    padding: 5px;
    color: #fff;
    background: #0275d8;
    font-weight: normal;
}

.cart td {
    padding: 10px 5px;
}

.cart td input {
    width: 50px;
    height: 30px;
    padding: 5px;
}

.cart td a {
    color: #0275d8;
    font-size: 12px;
}

.cart td img {
    width: 80px;
    height: 80px;
    margin-right: 10px;
}

.total-price {
    display: flex;
    justify-content: flex-end;
}

.total-price table {
    border-top: 3px solid #0275d8;
    margin-top: 5px;
    width: 100%;
    max-width: 400px;
}

td:last-child {
    text-align: right;
}

th:last-child {
    text-align: right;
}
</style>