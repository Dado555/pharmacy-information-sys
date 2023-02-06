<template>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

<div class="container">
  <div class="d-flex justify-content-between">

    <div class="ui-button">
      <button @click="loadAllMedicines" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">Add new</button>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Mark medicine as allergic</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <!-- Modal body -->
            <div class="row">
              <div class="col-lg-12">
                <div class="main-box clearfix">
                  <div class="table-responsive">
                    <table class="table user-list">
                      <thead>
                      <tr>
                        <th><span></span></th>
                        <th><span>Medicine</span></th>
                        <th><span>Form</span></th>
                        <th class="text-center"><span>Structure</span></th>
                      </tr>
                      </thead>
                      <tbody>
                        <tr v-for="medicine in allMedicines" :key="medicine.id">
                          <td><input class="form-check-input" type="checkbox" :value="medicine.id" v-model="checkedMedicineIds"></td>
                          <td>
                            <img src="../../../public/medicine.png" alt="">
                            <a href="#" class="user-link">{{ medicine.name }}</a>
                            <span class="user-subhead">{{ medicine.type }}</span>
                          </td>
                          <td>{{ medicine.medicineForm }}</td>
                          <td class="text-center">
                            <span class="label label-default">{{ medicine.structure }}</span>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" data-bs-dismiss="modal" @click="addAllergicMedicines">Add selected medicines</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-lg-12">
      <div class="main-box clearfix">
        <div class="table-responsive">
          <table ref="table" class="table user-list">
            <thead>
            <tr>
              <th><span>Medicine</span></th>
              <th><span>Form</span></th>
              <th class="text-center"><span>Structure</span></th>
              <th>&nbsp;</th>
        	</tr>
            </thead>
            <tbody>
              <tr v-for="medicine in allergicMedicines" :key="medicine.id">
                <td>
                  <img src="../../../public/medicine.png" alt="">
                  <a href="#" class="user-link">{{ medicine.name }}</a>
                  <span class="user-subhead">{{ medicine.type }}</span>
                </td>
                <td>{{ medicine.medicineForm }}</td>
                <td class="text-center">
                  <span class="label label-default">{{ medicine.structure }}</span>
                </td>
                <td style="width: 20%;">
                  <a href="#" class="table-link danger" @click="deleteFromAlergicMedicines(medicine.id)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-trash-o fa-stack-1x fa-inverse"></i>
                    </span>
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
import PatientService from '@/service/PatientService.js'
import MedicineService from '@/service/MedicineService.js'

export default {
  name: "ReservecMedicinesInfo",
  data() {
    return {
      allergicMedicines: [],
      allMedicines: [],
      checkedMedicineIds: []
    }
  },
  computed: {
    user() {
        return this.$store.getters.getUser;
    }
  },
  mounted() {
    this.loadAllergicMedicines();
  },

  methods: {

    loadAllergicMedicines() {
        let data = this;
		    PatientService.findAllergicMedicines(this.user.id).then(response => { data.allergicMedicines = response.data; })
    },
    
    loadAllMedicines() {
        let data = this;
        MedicineService.findAll().then((response) => { data.allMedicines = response.data.medicines; });
	},
	
    addAllergicMedicines() {
		PatientService.addAllergicMedicines(this.user.id, this.checkedMedicineIds)
        .then((response) => {
            this.allergicMedicines = response.data;
            this.checkedMedicineIds = [];
            this.toast('Data successfully updated!');
        }); 
	},

    deleteFromAlergicMedicines(id) {
		PatientService.deleteAllergicMedicine(this.user.id, id)
        .then((response) => {
            this.allergicMedicines = response.data;
            this.toast('Data successfully removed!');    
        });    
	},
	
	toast(message) {
		this.$toast.show(message, {
			type: 'info',
			position: 'top',
			duration: 2000
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