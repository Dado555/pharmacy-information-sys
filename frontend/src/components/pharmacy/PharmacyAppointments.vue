<template>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

<div class="container">

    <div class="d-flex justify-content-between">
        <div class="sort-filter">
            <h3>Sort by: </h3>
            <select class="search-select" v-model="sortBy" @change=sortAppointments>
                <option value="">Select one</option>
                <option value="price-ascending">Price  &#8593</option>
                <option value="price-descending">Price  &#8595</option>
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
              <th><span>Appointment</span></th>
              <th><span>Appointment status</span></th>     
              <th><span>Date</span></th>
              <th><span>From</span></th>
              <th><span>To</span></th>
              <th><span>Dermatologist</span></th>
              <th class="text-center"><span>Price</span></th>
              <th class="text-center"><span>Rating</span></th>
              <th><span></span></th>
            </tr>
            </thead>
            <tbody>
              <tr v-for="appointment in appointments" :key="appointment.id">
                <td>
                  <img src="../../../src/assets/appointment.png" alt="">
                  <a href="#" class="user-link">{{ appointment.title }}</a>
                </td>
                <td><span class="user-subhead">{{ appointment.appointmentStatus }}</span></td>
                <td>{{ new Date(appointment.start).toLocaleString('en-GB').split(',')[0] }}</td>
                <td>{{ new Date(appointment.start).toLocaleString('en-GB').split(',')[1].split(':').slice(0, 2).join(':') }}</td>
                <td>{{ new Date(appointment.end).toLocaleString('en-GB').split(',')[1].split(':').slice(0, 2).join(':') }}</td>
                <td><span class="user-subhead">{{ appointment.medicalWorker }}</span></td>
                <td class="text-center">
                  <span class="label label-default">{{ appointment.price }}</span>
                </td>
                <td class="text-center">
                  <span class="label label-default">{{ appointment.rating }}</span>
                </td>
                <td v-if="user.role === 'PATIENT'"><button @click="reserveAppointment(appointment.id)" type="button" class="btn btn-primary">Reserve appointment</button></td>
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
import AppointmentService from '@/service/AppointmentService.js'
import PharmacyService from "../../service/PharmacyService";

export default {
    name: "PharmacyAppointments",
    data() {
        return {
            appointments: [],
            sortBy: ""
        }
    },
    computed: {
        user() {
            return this.$store.getters.getUser;
        },
    },
    mounted() {
        this.loadAppointments();
    },
    methods: {
        loadAppointments() {
            let data = this;
            PharmacyService.getAppointments(this.$route.query.id)
            .then((response) => {
                data.appointments = response.data;
            });
        },
        reserveAppointment(appointmentId) {
            AppointmentService.addAppointmentExamination(appointmentId)
            .then((response) => { this.appointments = this.loadAppointments(); this.toast("Appointment reserved successfully! Check mail for confirmation."); })
            .catch((error) => { this.toast(error.response.data.message, 'error'); });
        },
        sortAppointments() {
            if (this.sortBy === "") return;
            let sortOrder = this.sortBy.split("-");
            if (sortOrder[0] === "price" && sortOrder[1] === "ascending")
                this.appointments.sort((a1, a2) => a1.price - a2.price);
            else if (sortOrder[0] === "price" && sortOrder[1] === "descending")
                this.appointments.sort((a1, a2) => a2.price - a1.price);
            else if (sortOrder[0] === "rating" && sortOrder[1] === "ascending")
                this.appointments.sort((a1, a2) => a1.rating - a2.rating);
            else
                this.appointments.sort((a1, a2) => a2.rating - a1.rating);
        },
        toast(message) {
            this.$toast.show(message, {
              type: 'info',
              position: 'top',
              duration: 2500
            });
        },
    }
}
</script>

<style scoped>

body{margin-top:20px;}

.container {
  margin-top: 1%;
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
</style>