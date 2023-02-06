<template>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

  <div class="container">
    <form @submit.prevent=searchDermatologists>
      <div class="search">
        <div class="input-group">
          <input v-model="searchParameters.firstName" type="text" class="form-control" placeholder="First name: ">
          <input v-model="searchParameters.lastName" type="text" class="form-control" placeholder="Last name: ">
          <input v-model="searchParameters.phoneNumber" type="text" class="form-control" placeholder="Phone number: ">
          <input v-model="searchParameters.address" type="text" class="form-control" placeholder="Address: ">
          <div class="input-group-append">
            <button class="btn btn-primary">Search</button>
          </div>
        </div>
      </div>
    </form><br><br>

    <div class="d-flex justify-content-between">
      <div class="sort-filter">
        <h3>Sort by: </h3>
        <select class="search-select" @change=sortDermatologists($event)>
          <option disabled value="">Select one</option>
          <option value="firstName-ascending">First name  &#8593</option>
          <option value="firstName-descending">First name  &#8595</option>
          <option value="lastName-ascending">Last name  &#8593</option>
          <option value="lastName-descending">Last name  &#8595</option>
          <option value="phoneNumber-ascending">Phone number  &#8593</option>
          <option value="phoneNumber-descending">Phone number  &#8595</option>
          <option value="address-ascending">Address  &#8593</option>
          <option value="address-descending">Address  &#8595</option>
        </select>
      </div>

      <div v-if="role==='PHARMACY_ADMIN'" class="ui-button">
        <button class="btn btn-primary" @click="showHire=true">Hire new dermatologist</button>
      </div>
    </div>

    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>User</span></th>
                <th class="text-center"><span>Status</span></th>
                <th><span>Email</span></th>
                <th><span>Phone number</span></th>
                <th><span>Address</span></th>
                <th>&nbsp;</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="der in dermatologists" :key="der.id">
                <td>
                  <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="">
                  <h6>{{der.firstName}} {{der.lastName}}</h6>
                </td>
                <td class="text-center">
                  <span class="label label-default">{{der.active}}</span>
                </td>
                <td>
                  <a href="#">{{der.email}}</a>
                </td>
                <td>
                  {{ der.phoneNumber }}
                </td>
                <td>
                  {{ der.address.name }} {{ der.address.number }}, {{ der.address.city }} {{ der.address.postalCode }}
                </td>
                <td v-if="role==='PHARMACY_ADMIN'"  style="width: 20%;">
                  <a href="#" class="table-link" @click="isChart=true; currentDermatologist = der">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-star fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                  <a href="#" class="table-link" @click="appointmentsStats=true; currentDermatologist=der">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-calendar fa-stack-1x fa-inverse" aria-hidden="true"></i>
                    </span>
                  </a>
                  <a href="#" class="table-link" @click="showAppointments(der)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-search-plus fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                  <a href="#" class="table-link danger" @click="areYouSure(der, pharmacyId)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                </td>
                <td v-else>
                  <button class="btn btn-secondary" @click="showAppointments(der)">Set appointment</button>
                </td>
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
  </div>

  <confirmation-dialogue ref="confirmDialogue"></confirmation-dialogue>

  <div v-if="showCalendar || showHire">
    <transition name="modal">
      <div class="modal-mask" @click="$emit('close')">
        <div class="modal-wrapper">
          <div class="modal-container">
            <div class="modal-header">
              <button type="button" class="btn btn-close" data-bs-dismiss="modal" @click="showCalendar=false; showHire=false"></button>
            </div>
            <div class="modal-body" @click.stop>
              <DefineNewAppointment v-if="showCalendar"  :worker="currentDermatologist" :pharmacy-id="pharmacyId"
                                    :start-work="schedule.start" :end-work="schedule.end"
                                    role="dermatologist" :user-role="currentRole" :user-id="currentUserId"></DefineNewAppointment>
              <HireNewDermatologist v-if="showHire" :pharmacy-id="pharmacyId"></HireNewDermatologist>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>

  <confirmation-dialogue ref="confirmDialogue"></confirmation-dialogue>

  <Chart v-if="isChart" v-on:close="isChart=false" ref="chart" type="dermatologist" :dermatologist="currentDermatologist"></Chart>

  <StatsChart v-if="appointmentsStats" type="dermatologist" ref="appointmentsStats"
                         @close="appointmentsStats=false" :dermatologist="currentDermatologist"></StatsChart>
</template>

<script>
import {mapState} from "vuex";
import HireNewDermatologist from "./HireNewDermatologist";
import ConfirmationDialogue from "../Modals/ConfirmationDialogue";
import Chart from '../Modals/Chart.vue';
import DefineNewAppointment from "./DefineNewAppointment";
import StatsChart from "../Modals/StatsChart";
import PharmacyService from "../../service/PharmacyService";
import MedicalWorkerService from "../../service/MedicalWorkerService";
import DermacyService from "../../service/DermacyService";

export default {
  name: "PharmacyDermatologists",
  components: {DefineNewAppointment, HireNewDermatologist, ConfirmationDialogue, Chart, StatsChart},
  data: function() {
    return {
      dermatologists: [],
      isDeleted : null,
      title: "",
      isChart: false,
      ratingsList: [],
      pharmacyId: this.$route.query.id,
      searchParameters: {
        firstName: "",
        lastName: "",
        phoneNumber: "",
        address: "",
        sortBy: "",
        orderBy: "",
        pharmacyIds: [this.$route.query.id],
        ratingFrom: "",
        ratingTo: ""
      },
      currentPage: 0,
      pageSize: 7,
      totalPages: 1,
      showCalendar: false,
      showHire: false,
      dermatologistId: -1,
      currentDermatologist: null,
      schedule: null,
      currentRole: null,
      currentUserId: null,
      appointmentsStats: false
    }
  },
  computed: {
    ...mapState({
      role: state => state.user.role,
      userId: state => state.user.id
    })
  },
  mounted() {
    this.loadDermatologists();
  },
  watch: {
    showHire: function () {
      if(this.showHire === false)
        this.loadDermatologists();
    }
  },
  methods: {
    loadDermatologists() {
      PharmacyService.getEmployedWorkers(this.$route.query.id, 'dermatologist')
        .then((response) => {
          this.dermatologists = response.data;
        });
    },
    sortDermatologists(event) {
      let sortOrder = event.target.value.split("-");
      this.searchParameters.sortBy = sortOrder[0];
      this.searchParameters.orderBy = sortOrder[1];
      this.searchDermatologists();
    },
    searchDermatologists() {
      if (this.searchParameters.ratingFrom === "")
        this.searchParameters.ratingFrom = 0;
      if (this.searchParameters.ratingTo === "")
        this.searchParameters.ratingTo = 5;
      MedicalWorkerService.searchWorker('dermatologist', this.searchParameters)
      .then((response) => {
        this.dermatologists = response.data.dermatologists;
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
      MedicalWorkerService.updatePageSearchWorker('dermatologist', pageNumber, this.searchParameters)
      .then((response) => {
        this.dermatologists = response.data.workers;
        this.totalPages = response.data.totalPages;
        if (this.dermatologists.length === 0 && this.currentPage > 0)
          this.updatePage(this.currentPage - 1);
      });
    },
    loadSchedule(dermatologistId) {
      MedicalWorkerService.getWorkerSchedule('dermatologist', dermatologistId, this.$route.query.id)
        .then((response) => {
          this.schedule = response.data;
          this.showCalendar = true;
        });
    },
    showPreviousLink() {
      return this.currentPage !== 0;
    },
    showNextLink() {
      return this.currentPage !== this.totalPages - 1;
    },
    showAppointments(dermatologist) {
      this.currentRole = this.role;
      this.currentUserId = this.userId;
      this.currentDermatologist = dermatologist;
      this.loadSchedule(this.currentDermatologist.id);
      console.log(dermatologist.id);
    },
    areYouSure(dermatologist, pharmacyId) {
      this.$refs.confirmDialogue.show({
        title: "Fire dermatologist "+ dermatologist.firstName + " " + dermatologist.lastName,
        message: "Are you sure you want to fire this dermatologist?",
        okButton: 'Fire'
      }).then((result)=>{
        if(result)
          this.dermatologistDismissal(dermatologist, pharmacyId);
      });
    },
    dermatologistDismissal(dermatologist, pharmacyId) {
      MedicalWorkerService.getFutureAppointments('dermatologist', Number(dermatologist.id), Number(pharmacyId))
      .then((response) => {
        if (response.data.length === 0) {
          DermacyService.fireDermatologist(dermatologist.id, pharmacyId)
          .then((response) => {
            this.loadDermatologists();
            this.$toast.show('Dermatologist \'' + dermatologist.firstName + ' ' + dermatologist.lastName + ' \' fired successfully!', {
              type: 'info',
              position: 'top',
              duration: 6000
            });
          });
        } else {
          this.$toast.show('Dermatologist can\'t be fired because of future reserved appointments!', {
            type: 'info',
            position: 'top',
            duration: 2000
          });
        }
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