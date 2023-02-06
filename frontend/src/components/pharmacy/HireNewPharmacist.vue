<template>
  <div class="row">
    <div class="col-lg-12">
      <div class="main-box clearfix">
        <div class="table-responsive">
          <table class="table user-list">
            <thead>
            <tr>
              <th><span>User</span></th>
              <th class="text-center"><span>Status</span></th>
              <th><span>Phone number</span></th>
              <th><span>Email</span></th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="phar in pharmacists" :key="phar.id">
              <td>
                <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="">
                <a href="#" class="user-link" @click="showProfile(phar)">{{phar.firstName}} {{phar.lastName}}</a>
                <span class="user-subhead">{{phar.role}}</span>
              </td>
              <td class="text-center">
                <span class="label label-default">{{ phar.active }}</span>
              </td>
              <td>
                <span class="label label-default">{{ phar.phoneNumber }}</span>
              </td>
              <td>
                <a href="#">{{ phar.email }}</a>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <div v-if="showOne && pharmacistShow">
    <transition name="modal" id="modal">
      <div class="modal-mask">
        <div class="modal-wrapper">
          <div class="modal-container" style="width: 400px">
            <div class="modal-header">
              <button type="button" class="btn btn-close" data-bs-dismiss="modal" @click="showOne=false"></button>
            </div>
            <div class="modal-body">
              <div class="container">
                <dl class="row" style="text-align: justify">
                  <dt class="col-sm-9">Pharmacist:</dt>
                  <dd class="col-sm-9">{{ pharmacistShow.firstName }}  {{ pharmacistShow.lastName }}</dd>

                  <dt class="col-sm-9">Email:</dt>
                  <dd class="col-sm-9">{{ pharmacistShow.email }}</dd>

                  <dt class="col-sm-9">Phone number: </dt>
                  <dd class="col-sm-9">{{ pharmacistShow.phoneNumber }}</dd>

                  <dt class="col-sm-9">Address: </dt>
                  <dd class="col-sm-12">{{ pharmacistShow.address.name }} {{ pharmacistShow.address.number }}, {{ pharmacistShow.address.city }} {{ pharmacistShow.address.postalCode }}</dd>

                  <dt class="col-sm-9">Role: </dt>
                  <dd class="col-sm-9">Pharmacist</dd>

                  <dt class="col-sm-9">Is user active: </dt>
                  <dd class="col-sm-9">{{ pharmacistShow.active }}</dd>

                  <dt class="col-sm-9">Pharmacy work time:</dt>
                  <dd class="col-sm-9">{{ pharmacy.startWorkTime/60 }}:{{ pharmacy.startWorkTime%60 }}h -
                                       {{ pharmacy.endWorkTime/60 }}:{{ pharmacy.endWorkTime%60 }}</dd>

                  <dt class="col-sm-9">Enter pharmacist work time:</dt>
                  <dd class="col-sm-9">
                    <label>Start</label>
                    <flat-pickr v-model="startTime" :config="configPicker"
                                class="form-control col-sm-5" placeholder="Select start time">
                    </flat-pickr>
                    <label>End</label>
                    <flat-pickr v-model="endTime" :config="configPicker"
                                class="form-control col-sm-5" placeholder="Select end time">
                    </flat-pickr>
                  </dd>
                </dl>
              </div>
            </div>
            <div class="container">
              <button type="button" class="btn btn-primary mx-2" @click="hirePharmacist()">HIRE</button>
              <button type="button" class="btn btn-outline-primary" @click="showOne=false">Cancel</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>

</template>

<script>
import flatPickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.css';
import PharmacyService from "../../service/PharmacyService";
import MedicalWorkerService from "../../service/MedicalWorkerService";

export default {
  name: "HireNewPharmacist",
  props: ['pharmacyId'],
  components: {flatPickr},
  data: function() {
    return {
      pharmacists: [],
      pharmacistId: -1,
      pharmacistShow: null,
      showOne: false,
      pharmacy: null,
      startTime: null,
      endTime: null,
      configPicker: {
        enableTime: "true",
        noCalendar: "true",
        dateFormat: "H:i",
        time_24hr: "true"
      }
    }
  },
  mounted() {
    this.loadPharmacists();
  },
  methods: {
    loadPharmacists() {
      PharmacyService.findOne(this.pharmacyId)
        .then((response) => {
          this.pharmacy = response.data;
          MedicalWorkerService.getUnemployedPharmacists()
            .then((response) => {
              this.pharmacists = response.data;
            })
        });
    },
    showProfile(pharmacist) {
      this.pharmacistShow = pharmacist;
      this.showOne = true;
    },
    hirePharmacist() {
      if(this.startTime === null || this.endTime === null) {
        this.$toast.show('Enter start and end work time!',{
          type: 'info',
          position: 'top',
          duration: 3000
        });
      } else {
        let hoursStart = parseInt(this.startTime.split(":")[0]);
        let minutesStart =  parseInt(this.startTime.split(":")[1]);
        let hoursEnd = parseInt(this.endTime.split(":")[0]);
        let minutesEnd =  parseInt(this.endTime.split(":")[1]);
        let startTime = hoursStart*60+minutesStart;
        let endTime = hoursEnd*60+minutesEnd;
        if(startTime < this.pharmacy.startWorkTime || endTime > this.pharmacy.endWorkTime) {
          this.$toast.show('Enter valid start and end work time!', {
            type: 'info',
            position: 'top',
            duration: 3000
          });
        } else {
          MedicalWorkerService.hirePharmacist(this.pharmacistShow.id, this.pharmacyId, startTime, endTime)
            .then((response) => {
              this.showOne = false;
              this.$toast.show('Pharmacist \'' + response.data.firstName + ' ' + response.data.lastName + '\' hired successfully!',{
                type: 'info',
                position: 'top',
                duration: 3000
              });
              this.loadPharmacists();
            });
        }
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

</style>