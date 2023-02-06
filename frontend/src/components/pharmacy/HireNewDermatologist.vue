<template>
  <div class="row">
    <div class="col-lg-12">
      <div class="main-box clearfix">
        <div class="table-responsive">
          <table class="table user-list">
            <thead>
            <tr>
              <th><span>User</span></th>
              <th><span>Created</span></th>
              <th class="text-center"><span>Status</span></th>
              <th><span>Email</span></th>
              <th>&nbsp;</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="der in dermatologists" :key="der.id">
              <td>
                <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="">
                <a href="#" class="user-link" @click="showProfile(der)">{{der.firstName}} {{der.lastName}}</a>
                <span class="user-subhead">{{der.role}}</span>
              </td>
              <td>
                2013/08/08
              </td>
              <td class="text-center">
                <span class="label label-default">{{ der.active }}</span>
              </td>
              <td>
                <a href="#">{{ der.email }}</a>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <div v-if="showOne && dermatologistShow">
    <transition name="modal" id="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-container">
            <div class="modal-header">
              <span id="title-header"> Input dermatologist work time: </span>
              <button type="button" class="btn btn-close" style="margin-left: 100px" data-bs-dismiss="modal" @click="showOne=false"></button>
            </div>
            <div class="modal-body">
              <DermatologistHiringModal @change="onTimeChange" :dermatologistShow="dermatologistShow" :scheduleList="scheduleList" :pharmacy="pharmacy"></DermatologistHiringModal>
            </div>
            <div class="container">
              <button type="button" class="btn btn-primary mx-2" @click="hireDermatologist()">HIRE</button>
              <button type="button" class="btn btn-outline-primary" @click="showOne=false">Cancel</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import DermatologistHiringModal from "./DermatologistHiringModal";
import DermacyService from "../../service/DermacyService";
import PharmacyService from "../../service/PharmacyService";

export default {
  name: "HireNewDermatologist",
  props: ['pharmacyId'],
  data: function () {
    return {
      dermatologists: [],
      dermatologistId: -1,
      dermatologistShow: null,
      showOne: false,
      scheduleList: [],
      workSchedule: {
        start: null,
        end: null,
        dermatologistId: this.dermatologistId,
        pharmacyId: this.pharmacyId
      },
      pharmacy: null
    }
  },
  components: {
    DermatologistHiringModal,
  },
  mounted() {
    this.loadDermatologists();
  },
  methods: {
    loadDermatologists() {
      DermacyService.findEmployableDermatologists(this.pharmacyId)
      .then((response) => {
        this.dermatologists = response.data;
        PharmacyService.findOne(this.pharmacyId)
        .then((response) => {
          this.pharmacy = response.data;
        })
      });
    },
    showProfile(dermatologist) {
      DermacyService.getDermatologistSchedule(dermatologist.id)
      .then((response) => {
        this.scheduleList = response.data;
        console.log(this.scheduleList);
        this.dermatologistShow = dermatologist;
        this.dermatologistId = dermatologist.id;
        this.showOne = true;
      });
    },
    onTimeChange(start, end) {
      console.log(start);
      console.log(end);
      this.workSchedule.start = start;
      this.workSchedule.end = end;
      this.workSchedule.dermatologistId = this.dermatologistId;
      this.workSchedule.pharmacyId = this.pharmacyId;
    },
    hireDermatologist() {
      if(this.workSchedule.start != null && this.workSchedule.end != null) {
        console.log("MOZE DALJE");
        console.log(this.workSchedule);
        DermacyService.hireDermatologist(this.workSchedule)
          .then((response) => {
            this.showOne = false;
            this.$toast.show('Dermatologist hired successfully!', {
              type: 'info',
              position: 'top',
              duration: 3000
            });
            this.loadDermatologists();
          });
      }
      else{
        this.$toast.show('Can\'t hire dermatologist. Time not set properly!', {
          type: 'info',
          position: 'top',
          duration: 4000
        });
      }
    }
  }
}
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

#title-header {
  margin-left: 40%;
  font-size: large;
  font-weight: bolder;
}

</style>