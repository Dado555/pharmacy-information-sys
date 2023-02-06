<template>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

  <div class="container">
    <form @submit.prevent=searchMedicines>
      <div class="search">
        <div class="input-group">
          <input v-model="searchParameters.name" type="text" class="form-control" placeholder="Name: ">
          <input v-model="searchParameters.type" type="text" class="form-control" placeholder="Type: ">
          <input v-model="searchParameters.ratingFrom" type="number" class="form-control" placeholder="Rating From: ">
          <input v-model="searchParameters.ratingTo" type="number" class="form-control" placeholder="Rating To: ">

          <div class="input-group-append">
            <button class="btn btn-primary">Search</button>
          </div>
        </div>
      </div>
    </form><br><br>

    <div class="d-flex justify-content-between">
      <div class="sort-filter">
        <h3>Sort by: </h3>
        <select class="search-select" @change=sortMedicines($event)>
          <option value="">Select one</option>
          <option value="medicine.name-ascending">Name  &#8593</option>
          <option value="medicine.name-descending">Name  &#8595</option>
          <option value="price-ascending">Price  &#8593</option>
          <option value="price-descending">Price  &#8595</option>
        </select>
      </div>

      <div v-if="role==='PHARMACY_ADMIN'" class="ui-button">
        <button class="btn btn-primary" @click="showModal = true">Add new</button>
      </div>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Name / Type</span></th>
                <th><span>Price</span></th>
                <th class="text-center"><span>Available</span></th>
                <th><span>Rating</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="medicine in medicines" :key="medicine.id">
                <td>
                  <img src="../../../public/medicine.png" alt="">
                  <a class="user-link"><router-link :to="{ path: '/pharmacy-medicine', query: { id : medicine.id}}">{{ medicine.medicineDTO.name }}</router-link></a>
                  <span class="user-subhead">{{ medicine.medicineDTO.type }}</span>
                </td>
                <td v-if="medicine.actionPrice">
                  <del>{{ medicine.price }}</del> <b>{{ medicine.actionPrice }}</b>
                </td>
                <td v-else>
                  {{ medicine.price }}
                </td>
                <td class="text-center">
                  <span class="label label-default">{{ medicine.availableAmount }}</span>
                </td>
                <td>
                  <a href="#">{{ medicine.medicineDTO.averageRating }}</a>
                </td>
                <td v-if="role==='PHARMACY_ADMIN'" style="width: 20%;">
                  <a href="#" class="table-link" @click="promoteMedicine(medicine)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-bell fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                  <a href="#" class="table-link" @click="orderStats=true; currentMedicine=medicine">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-bar-chart fa-stack-1x fa-inverse" aria-hidden="true"></i>
                    </span>
                  </a>
                  <a href="#" class="table-link" data-bs-target="#edit-modal-pharmacyMedicine" data-bs-toggle="modal" v-on:click="edit(medicine)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-pencil fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                  <a href="#" class="table-link danger" @click="areYouSure(medicine, searchParameters.pharmacyId)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                </td>
                <td v-if="role==='PATIENT'"><input :id="medicine.id" type="number" min="1" :max="medicine.availableAmount" placeholder="1" value="1"></td>
                <td v-if="role==='PATIENT'"><button class="btn btn-secondary" @click="addToCart(medicine)">Add to Cart</button></td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="pagination-wrapper">
            <nav aria-label="Page navigation example" class="nav-pagination">
              <ul class="pagination">
                <li class="page-item"><button v-if="showPreviousLink()" @click="updatePage(currentPage - 1)" class="page-link">Previous</button></li>
                <li v-for="index in totalPages" :key="index" class="page-item" :id="index"><button class="page-link" @click="updatePage(index-1)"> {{ index }} </button></li>
                <li class="page-item"><button v-if="showNextLink()" @click="updatePage(currentPage + 1)" class="page-link">Next</button></li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>

    <div class="modal" id="edit-modal-pharmacyMedicine">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Update medicine data</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="cancel" id="close-modal-update"></button>
          </div>
          <div class="modal-body text-start">
            <form>
              <div class="form-group">
                <label class="col-form-label">Name:</label>
                <input type="text" class="form-control" readonly="readonly" v-model="updated.name" v-bind:class="{'is-invalid': invalidName}">
                <div class="invalid-feedback">
                  Invalid medicine name.
                </div>
              </div>
              <div class="form-group">
                <label class="col-form-label">Type:</label>
                <input type="text" class="form-control" readonly="readonly" v-model="updated.type" v-bind:class="{'is-invalid': invalidType}">
                <div class="invalid-feedback">
                  Invalid medicine type.
                </div>
              </div>
              <div class="form-group">
                <label class="col-form-label">Price:</label>
                <input type="text" class="form-control" v-model="updated.price" v-bind:class="{'is-invalid': invalidPrice}">
                <div class="invalid-feedback">
                  Invalid price.
                </div>
              </div>
              <div class="form-group">
                <label class="col-form-label">Available amount:</label>
                <input type="text" class="form-control" v-model="updated.available" v-bind:class="{'is-invalid': invalidAmount}">
                <div class="invalid-feedback">
                  Invalid price.
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="saveChanges">Save changes</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" v-on:click="cancel">Cancel</button>
          </div>
        </div>
      </div>
    </div>

  </div>

  <div v-if="showModal">
    <transition name="modal" id="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-container">
            <div class="modal-header">
              <button type="button" class="btn btn-close" data-bs-dismiss="modal" @click="showModal=false"></button>
            </div>
            <div class="modal-body">
              <AddNewMedicine :pharmacyId="searchParameters.pharmacyId"></AddNewMedicine>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>

  <confirmation-dialogue ref="confirmDialogue"></confirmation-dialogue>

  <PromoteMedicine v-if="promotion" @close="promotion=false" :medicine="currentMedicine" :untildt="until" :pharmacyId="searchParameters.pharmacyId"></PromoteMedicine>

  <StatsChart v-if="orderStats" type="medicine" :medicine="currentMedicine" @close="orderStats=false"></StatsChart>
</template>

<script>
import {mapState} from "vuex";
import AddNewMedicine from "./AddNewMedicine";
import ConfirmationDialogue from "../Modals/ConfirmationDialogue";
import StatsChart from "../Modals/StatsChart";
import PromoteMedicine from "../Modals/PromoteMedicine";
import PharmacyService from "../../service/PharmacyService";
import MedicineService from "../../service/MedicineService";

export default {
  name: "PharmaciesAvailableMedicine",
  components: {PromoteMedicine, AddNewMedicine, ConfirmationDialogue, StatsChart},
  data() {
    return {
      medicines: [],
      searchParameters: {
        name: "",
        type: "",
        ratingFrom: "",
        ratingTo: "",
        sortBy: "",
        orderBy: "",
        pharmacyId : this.$route.query.id
      },
      updated: {
        id: 0,
        name: "",
        type: "",
        price: 0,
        available: 0
      },
      invalidName: false,
      invalidType: false,
      invalidPrice: false,
      invalidAmount: false,
      currentPage: 0,
      pageSize: 8,
      totalPages: 1,
      showModal: false,
      promotion: false,
      currentMedicine: null,
      until: null,
      orderStats: false
    }
  },
  computed: {
    ...mapState({
      role: state => state.user.role,
      adminId: state => state.user.id
    })
  },
  mounted() {
    this.loadAvailableMedicine();
  },
  watch: {
    showModal: function() {
      if(this.showModal === false)
        this.loadAvailableMedicine();
    },
    promotion: function () {
      if(this.promotion === false)
        this.loadAvailableMedicine();
    }
  },
  methods: {
    loadAvailableMedicine() {
      let data = this;
      PharmacyService.getPharmacyMedicines(this.$route.query.id)
        .then((response) => {
          data.medicines = response.data;
        });
    },
    promoteMedicine(medicine) {
      this.currentMedicine = medicine;
      console.log(medicine);
      this.until = this.setDate(medicine.untilDateTime);
      this.promotion = true;
    },
    setDate(until) {
      let string = null;
      console.log(until);
      if(until) {
        let date = new Date(until)
        string = date.getFullYear() + "-";
        if(date.getMonth()+1 < 10)
          string += "0" + (date.getMonth()+1);
        else
          string += date.getMonth()+1;
        string += "-";
        if(date.getDate() < 10)
          string += "0" + date.getDate();
        else
          string += date.getDate();
        string += "T";
        if(date.getHours()<10)
          string += "0" + date.getHours();
        else
          string += date.getHours();
        string += ":";
        if(date.getMinutes() < 10)
          string += "0" + date.getMinutes();
        else
          string += date.getMinutes();
        console.log(string);
      }
      return string;
    },
    searchMedicines() {
      if (this.searchParameters.ratingFrom === "")
        this.searchParameters.ratingFrom = 0;
      else
        this.searchParameters.ratingFrom = Math.round(this.searchParameters.ratingFrom)
      if (this.searchParameters.ratingTo === "")
        this.searchParameters.ratingTo = 5;
      else
        this.searchParameters.ratingTo = Math.round(this.searchParameters.ratingTo)
      this.searchParameters.pharmacyId = parseInt(this.searchParameters.pharmacyId)
      console.log(this.searchParameters);
      this.doSearch();
    },
    sortMedicines(event) {
      let sortOrder = event.target.value.split("-");
      this.searchParameters.sortBy = sortOrder[0];
      this.searchParameters.orderBy = sortOrder[1];
      this.searchMedicines();
    },
    doSearch() {
      MedicineService.searchPharmacyMedicines(this.searchParameters)
      .then((response) => {
        this.medicines = response.data.medicines;
        this.totalPages = response.data.totalPages;
        this.currentPage = 0;
        try {
          document.querySelector(".active").classList.remove("active");
          document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
        } catch (err) {}
      });
    },
    updatePage(pageNumber) {
      this.currentPage = pageNumber;
      document.querySelector(".active").classList.remove("active");
      document.getElementById(pageNumber+1).classList.toggle("active");
      MedicineService.updatePagePharmacyMedicines(this.searchParameters, pageNumber)
      .then((response) => {
        this.medicines = response.data.medicines;
        this.totalPages = response.data.totalPages;
        if (this.medicines.length === 0 && this.currentPage > 0)
          this.updatePage(this.currentPage - 1);
      });
    },
    showPreviousLink() {
      return this.currentPage !== 0;
    },
    showNextLink() {
      return this.currentPage !== this.totalPages - 1;
    },
    areYouSure(medicine, pharmacyId) {
      this.$refs.confirmDialogue.show({
        title: "Delete medicine "+ medicine.medicineDTO.name + " (" + medicine.medicineDTO.name + ")",
        message: "Are you sure you want to delete this product?",
        okButton: 'Delete'
      }).then((result)=>{
        if(result)
          this.deleteMedicine(medicine.id, pharmacyId);
      });
    },
    deleteMedicine(medicineId, pharmacyId) {
      MedicineService.deletePharmacyMedicine(medicineId, pharmacyId)
      .then((response) => {
        this.loadAvailableMedicine();
      });
    },
    saveChanges(pharmacyId) {
      MedicineService.updatePharmacyMedicine(this.updated)
      .then((response) => {
        this.loadAvailableMedicine();
        document.getElementById('close-modal-update').click();
        this.$toast.show('Data successfully updated!', {
          type: 'info',
          position: 'top',
          duration: 2000
        });
      });
    },
    cancel() {
      this.invalidPrice = false;
      this.invalidAmount = false;
    },
    edit(medicine) {
      this.updated = {id: medicine.id, name: medicine.medicineDTO.name, type: medicine.medicineDTO.type, price: medicine.price, available: medicine.availableAmount};
    },
    validate() {
      // get current medicine info and compare with edited, if they are the same print no changes were made
      // validate name, type, price and amount with regex
    },
    addToCart(pharmacyMedicineDTO) {
      let amount = document.getElementById(pharmacyMedicineDTO.id).value;
      this.$store.dispatch('addItemToCart', { pharmacyMedicineDTO, amount });
      this.$toast.show('Item added!', {
        type: 'info',
        position: 'top',
        duration: 2000
      });
    }
  }
}
</script>

<style scoped>

body{margin-top:20px;}

.container {
  margin-top: 5%;
}

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

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
  height: 100%;
}

.modal-container {
  width: 70%;
  height: 80%;
  margin: 5% auto;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
  transition: all .3s ease;
  font-family: Helvetica, Arial, sans-serif;
}

.modal-body {
  position: relative;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow-y: auto;
  max-height: calc(100vh - 200px);
}

</style>