<template>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

  <div class="card">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
        <div class="profile-img">
          <img src="../../assets/pharmacy.png" class="rounded-circle" width="170" alt="slika apoteke"/>
          <div class="file btn btn-lg btn-primary">
            Change Photo
            <input type="file" name="file"/>
          </div>
        </div>
        <div class="mt-3">
          <div class="container">
            <p style="font-size: x-large">
            {{ pharmacy.name }}
            <a v-if="role==='PHARMACY_ADMIN'" href="#" @click="changeData=true">
              <span class="fa-stack">
                <i class="fa fa-square fa-stack-2x"></i>
                <i class="fa fa-pencil fa-stack-1x fa-inverse"></i>
              </span>
            </a>
            </p>
            <p class="text-secondary mb-1">
              ID: {{ pharmacy.id }} ,
              <a v-if="role==='PHARMACY_ADMIN'" href="#" class="user-link" @click="isChart=true"> Rating </a> <span v-else>Rating</span>: {{ pharmacy.rating }},
              <span v-if="role==='PHARMACY_ADMIN'"><a href="#" class="user-link" @click="earningStats=true">Earnings:</a> {{ earnings }} RSD </span>
            </p>
          </div>
          <p class="text-muted font-size-sm">Address: {{ pharmacy.address.name }} {{ pharmacy.address.number }}, {{pharmacy.address.city}} {{pharmacy.address.postalCode}}</p>

          <button v-if="role === 'PATIENT' && !isSubscriber" @click="sub(pharmacy.id)"
                  type="button" class="btn btn-primary">Subscribe
          </button>
          <button v-if="role === 'PATIENT' && isSubscriber" @click="unsub(pharmacy.id)"
                  type="button" class="btn btn-primary"
                  style="background-color: red; border-color: red">Unsubscribe
          </button>
        </div>

      </div>
    </div>
  </div>

  <Chart v-if="isChart" v-on:close="isChart=false" ref="chart" type="pharmacy" :pharmacy="pharmacy"></Chart>
  <ChangePharmacyData v-if="changeData" v-on:close="changeData=false" v-on:notify="notify" :pharmacy="pharmacy"></ChangePharmacyData>
  <StatsChart v-if="earningStats" type="earnings" :pharmacy="pharmacy" @close="earningStats=false"></StatsChart>
</template>

<script>

import Chart from '../Modals/Chart.vue'
import {mapState} from "vuex"
import ChangePharmacyData from "../Modals/ChangePharmacyData";
import StatsChart from "../Modals/StatsChart";
import PatientService from "../../service/PatientService";

export default {
  name: "PharmacyInfo",
  props: ['pha', 'earnings'],
  emits: ['notify'],
  components: {ChangePharmacyData, Chart, StatsChart},
  data: function() {
    return {
      pharmacy: null,
      title: "",
      isChart: false,
      ratingsList: [],
      changeData: false,
      earningStats: false,
      isSubscriber: false
    }
  },
  methods: {
    notify() {
      this.$emit('notify');
    },
    isSub(pharId) {
      PatientService.isSubForPharmacy(this.userId, pharId)
      .then((response) => {
        this.isSubscriber = response.data;
      });
    },
    sub(pharId) {
      PatientService.subInPharmacy(this.userId, pharId)
      .then((response) => {
        if(response.data === true) {
          this.toast("Successfully subscribed to this pharmacy!", 3000);
          this.isSubscriber = true;
        }
      });
    },
    unsub(pharId) {
      PatientService.unsubInPharmacy(this.userId, pharId)
      .then((response) => {
        if(response.data === true) {
          this.toast("Successfully unsubscribed!", 3000);
          this.isSubscriber = false;
        }
      });
    },
    toast(message, len) {
      this.$toast.show(message, {
        type: 'info',
        position: 'top',
        duration: len
      });
    }
  },
  created() {
    this.pharmacy = this.pha;
  },
  mounted() {
    if(this.role === 'PATIENT')
      this.isSub(this.pha.id);
  },
  computed: {
    ...mapState({
      role: state => state.user.role,
      userId: state => state.user.id
    })
  }
}
</script>

<style scoped>
.profile-img .file {
  position: relative;
  overflow: hidden;
  margin-top: -20%;
  width: 50%;
  border: none;
  border-radius: 0;
  font-size: 15px;
  background: #212529a1;
}
.profile-img .file input {
  position: absolute;
  opacity: 0;
  right: 0;
  top: 0;
}
.fa.fa-square.fa-stack-2x {
  background: white;
  color: white;
}
.fa.fa-pencil.fa-stack-1x.fa-inverse {
  color: black;
}

.btn2 {
  background-color: #cc0000;
  color: white;
  border: 2px solid #cc0000;
}

</style>