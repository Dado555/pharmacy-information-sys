<template>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

  <Navbar></Navbar>
  <div class="modal-header">
    <qr-capture @decode="onDecode" class="mb"></qr-capture>
    <div class="result">
      EPrescription: {{prescription}}
    </div>
  </div>

  <div class="container" v-if="show === 1">
    <div class="d-flex justify-content-between">
      <div class="sort-filter">
        <h3>Sort by: </h3>
        <select class="search-select" v-model="sortBy" @change=sortPharmacies($event)>
          <option value="">Select one</option>
          <option value="price-ascending">Price  &#8593</option>
          <option value="price-descending">Price  &#8595</option>
          <option value="name-ascending">Name  &#8593</option>
          <option value="name-descending">Name  &#8595</option>
          <option value="city-ascending">City  &#8593</option>
          <option value="city-descending">City  &#8595</option>
          <option value="rating-ascending">Rating  &#8593</option>
          <option value="rating-descending">Rating  &#8595</option>
        </select>
      </div>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Pharmacy name</span></th>
                <th><span>Total price</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="qrCodeResult in qrCodeResults" :key="qrCodeResult.id">
                <td>
                  <img src="../../public/pharmacy.png" alt="">
                  <a href="#" class="user-link"> {{ qrCodeResult.pharmacyDTO.name }}</a>
                  <span class="user-subhead">{{ "Rating: " + qrCodeResult.pharmacyDTO.rating }}</span>
                </td>
                <td><span>{{ qrCodeResult.totalPrice }}</span></td>
                <td style="width: 20%;">
                  <a href="#" class="table-link" @click="buyMedicines(qrCodeResult.id)">
                    <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-cart4" viewBox="0 0 16 16">
                      <path d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5zM3.14 5l.5 2H5V5H3.14zM6 5v2h2V5H6zm3 0v2h2V5H9zm3 0v2h1.36l.5-2H12zm1.11 3H12v2h.61l.5-2zM11 8H9v2h2V8zM8 8H6v2h2V8zM5 8H3.89l.5 2H5V8zm0 5a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0zm9-1a1 1 0 1 0 0 2 1 1 0 0 0 0-2zm-2 1a2 2 0 1 1 4 0 2 2 0 0 1-4 0z"/>
                    </svg>
                  </a>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import {QrCapture} from 'vue3-qr-reader';
import Navbar from '@/components/Navbar.vue'
import PatientService from "@/service/PatientService";
import PharmacyService from "@/service/PharmacyService";

export default {
  name: 'QRCode',
  components: { QrCapture, Navbar },
  data() {
    return {
      qrCodeResults: [],
      show: 0,
      input: "",
      qrCodeMedicineDTO: {
        name: ""
      },
      sortBy: "",
      prescription: ""
    }
  },
  methods: {
    onDecode(input) {
      let data = this;
      this.input = input;
      this.qrCodeMedicineDTO.name = this.input;
      data.prescription = "";
      PatientService.loadPharmaciesAfterQRCode(this.$store.getters.getUser.id, this.qrCodeMedicineDTO)
          .then((response) => {
            data.qrCodeResults = response.data;
            if (data.qrCodeResults.length === 0) {
              this.$toast.show('EPrescription already processed!', {
                type: 'error',
                position: 'top',
                duration: 3000
              });
            } else {
              this.show = 1;
              let x;
              for (x of data.qrCodeResults[0].eprescriptionDTO.eprescriptionMedicineDTOList) {
                data.prescription += x.name + "-" + x.amount + "\n";
              }
            }
      });
    },
    sortPharmacies() {
      if (this.sortBy === "")
        return;
      if (this.sortBy === "price-ascending")
        this.qrCodeResults.sort((a1, a2) => a1.totalPrice - a2.totalPrice)
      else if (this.sortBy === "price-descending")
        this.qrCodeResults.sort((a1, a2) => a2.totalPrice - a1.totalPrice)
    },
    buyMedicines(id) {
      let data = this;
      PatientService.buyMedicinesAfterQRCode(this.$store.getters.getUser.id, id);
      data.qrCodeResults = null;
      this.$toast.show('Medicines bought!', {
        type: 'info',
        position: 'top',
        duration: 2000
      });
    }
  }
};
</script>

<style scoped>

/* USER LIST TABLE */
.user-list tbody td > img {
  position: relative;
  max-width: 50px;
  float: left;
  margin-right: 15px;
}
.user-list tbody td .user-link {
  display: block;
  font-size: 1.25em;
  padding-top: 3px;
  margin-left: 60px;
}
.user-list tbody td .user-subhead {
  font-size: 0.875em;
  font-style: italic;
}

/* TABLES */
.table {
  border-collapse: separate;
}
.table-hover > tbody > tr:hover > td,
.table-hover > tbody > tr:hover > th {
  background-color: #eee;
}
.table thead > tr > th {
  border-bottom: 1px solid #C2C2C2;
  padding-bottom: 0;
}
.table tbody > tr > td {
  font-size: 0.875em;
  background: #f5f5f5;
  border-top: 10px solid #fff;
  vertical-align: middle;
  padding: 12px 8px;
}
.table tbody > tr > td:first-child,
.table thead > tr > th:first-child {
  padding-left: 20px;
}
.table thead > tr > th span {
  border-bottom: 2px solid #C2C2C2;
  display: inline-block;
  padding: 0 5px;
  padding-bottom: 5px;
  font-weight: normal;
}
.table thead > tr > th > a span {
  color: #344644;
}
.table thead > tr > th > a span:after {
  content: "\f0dc";
  font-family: FontAwesome;
  font-style: normal;
  font-weight: normal;
  text-decoration: inherit;
  margin-left: 5px;
  font-size: 0.75em;
}
.table thead > tr > th > a.asc span:after {
  content: "\f0dd";
}
.table thead > tr > th > a.desc span:after {
  content: "\f0de";
}
.table thead > tr > th > a:hover span {
  text-decoration: none;
  color: #2bb6a3;
  border-color: #2bb6a3;
}
.table.table-hover tbody > tr > td {
  -webkit-transition: background-color 0.15s ease-in-out 0s;
  transition: background-color 0.15s ease-in-out 0s;
}
.table tbody tr td .call-type {
  display: block;
  font-size: 0.75em;
  text-align: center;
}
.table tbody tr td .first-line {
  line-height: 1.5;
  font-weight: 400;
  font-size: 1.125em;
}
.table tbody tr td .first-line span {
  font-size: 0.875em;
  color: #969696;
  font-weight: 300;
}
.table tbody tr td .second-line {
  font-size: 0.875em;
  line-height: 1.2;
}
.table a.table-link {
  margin: 0 5px;
  font-size: 1.125em;
}
.table a.table-link:hover {
  text-decoration: none;
  color: #2aa493;
}
.table a.table-link.danger {
  color: #fe635f;
}
.table a.table-link.danger:hover {
  color: #dd504c;
}

.table-products tbody > tr > td {
  background: none;
  border: none;
  border-bottom: 1px solid #ebebeb;
  -webkit-transition: background-color 0.15s ease-in-out 0s;
  transition: background-color 0.15s ease-in-out 0s;
  position: relative;
}
.table-products tbody > tr:hover > td {
  text-decoration: none;
  background-color: #f6f6f6;
}
.table-products .name {
  display: block;
  font-weight: 600;
  padding-bottom: 7px;
}
.table-products .price {
  display: block;
  text-decoration: none;
  width: 50%;
  float: left;
  font-size: 0.875em;
}
.table-products .price > i {
  color: #8dc859;
}
.table-products .warranty {
  display: block;
  text-decoration: none;
  width: 50%;
  float: left;
  font-size: 0.875em;
}
.table-products .warranty > i {
  color: #f1c40f;
}
.table tbody > tr.table-line-fb > td {
  background-color: #9daccb;
  color: #262525;
}
.table tbody > tr.table-line-twitter > td {
  background-color: #9fccff;
  color: #262525;
}
.table tbody > tr.table-line-plus > td {
  background-color: #eea59c;
  color: #262525;
}
.table-stats .status-social-icon {
  font-size: 1.9em;
  vertical-align: bottom;
}
.table-stats .table-line-fb .status-social-icon {
  color: #556484;
}
.table-stats .table-line-twitter .status-social-icon {
  color: #5885b8;
}
.table-stats .table-line-plus .status-social-icon {
  color: #a75d54;
}
.sort-filter {
  text-align: left;
}

.ui-button {
  text-align: right;
}


b {
  font-size: 50px;
}

</style>