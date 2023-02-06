<template>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<div class="container">
    <div class="modal fade" id="EPrescriptionMedicinesModal" tabindex="-1" aria-labelledby="EPrescriptionMedicinesModal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="EPrescriptionMedicinesModal">E-Prescription medicines</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-lg-12">
                        <div class="main-box clearfix">
                            <div class="table-responsive">
                            <table class="table user-list">
                                <thead>
                                <tr>
                                <th><span>Medicine Name</span></th>
                                <th><span>Amount</span></th>
                                <th><span>Therapy duration</span></th>
                                <th>&nbsp;</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="medicine in medicinesInEPrescription" :key="medicine.id">
                                    <td><span class="user-subhead">{{ medicine.name }}</span></td>
                                    <td><span class="user-subhead">{{ medicine.amount }}</span></td>
                                    <td><span class="user-subhead">{{ medicine.therapy }}</span></td>
                                </tr>
                                </tbody>
                            </table>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="d-flex justify-content-between first">
        <div class="sort-filter">
        <h3>Sort by: </h3>
        <select class="search-select" @change=sortEPrescriptions($event)>
            <option value="">Select one</option>
            <option value="dateOfIssue-ascending">Date  &#8593</option>
            <option value="dateOfIssue-descending">Date  &#8595</option>
        </select>
        </div>
    </div>

    <div class="d-flex justify-content-between">
        <div class="sort-filter">
        <h3>Filter by: </h3>
        <select class="search-select" @change=filterEPrescriptions($event)>
            <option value="">Select one</option>
            <option value="NEW">NEW </option>
            <option value="PROCESSED">PROCESSED </option>
            <option value="REJECTED">REJECTED </option>
        </select>
        </div>
    </div><br>

    <div class="row">
        <div class="col-lg-12">
        <div class="main-box clearfix">
            <div class="table-responsive">
            <table class="table user-list">
                <thead>
                <tr>
                <th><span>ID</span></th>
                <th><span>Title</span></th>
                <th><span>Date of issue</span></th>
                <th><span>Status</span></th>
                <th>&nbsp;</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="ePrescription in ePrescriptions" :key="ePrescription.id">
                    <td><span class="user-subhead">{{ ePrescription.id }}</span></td>
                    <td><span class="user-subhead">Title</span></td>
                    <td>{{ new Date(ePrescription.dateOfIssue).toLocaleDateString('en-GB') }}</td>
                    <td><span class="user-subhead">{{ ePrescription.status }}</span></td>
                    <td style="width: 20%;">
                    <button type="button" class="btn btn-secondary" data-bs-toggle="modal" data-bs-target="#EPrescriptionMedicinesModal" @click="findMedicinesForEPrescription(ePrescription.id)">
                        Show Medicines
                    </button>
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
import PatientService from '@/service/PatientService.js';
import EPrescriptionService from '@/service/EPrescriptionService.js';

export default {
    name: "EPrescriptions",
    data() {
        return {
            ePrescriptions: [],
            searchParameters: {
                sortBy: "",
                orderBy: "",
                filterBy: ""
            },
            medicinesInEPrescription: []
        }
    },
    computed: {
        user() {
            return this.$store.getters.getUser;
        }
    },
    mounted() {
        let data = this;
        PatientService.findEPrescriptions(this.user.id).then((response) => { data.ePrescriptions = response.data; });
    },
    methods: {
        searchEPrescriptions() {
            PatientService.searchEPrescriptions(this.user.id, this.searchParameters)
            .then(response => {
                this.ePrescriptions = response.data;
            })
        },
        sortEPrescriptions(event) {
            let sortOrder = event.target.value.split("-");
            this.searchParameters.sortBy = sortOrder[0];
            this.searchParameters.orderBy = sortOrder[1];
            this.searchEPrescriptions();
        },
        filterEPrescriptions(event) {
            this.searchParameters.filterBy = event.target.value;
            this.searchEPrescriptions();
        },
        findMedicinesForEPrescription(id) {
            EPrescriptionService.findMedicinesForEPrescription(id)
            .then(response => { this.medicinesInEPrescription = response.data })
            .catch((error) => { this.toast(error.message); });
        }

    }
}
</script>

<style scoped>

body{margin-top:20px;}

.container {
  margin-top: 1%;
}

.first {
  float: left;
  margin-right: 5%;
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