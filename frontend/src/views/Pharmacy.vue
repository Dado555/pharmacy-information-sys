<template>
  <Navbar></Navbar>
  <div class="container-fluid" :key="componentKey">
    <div class="main-body">
      <div class="row gutters-sm">
        <div class="col-md-3 mb-3">
          <PharmacyInfo v-if="pharmacy" :pha="pharmacy" v-on:notify="reload" :earnings="earnings"></PharmacyInfo>
          <PharmacyLinks v-if="pharmacy" :id="pharmacy.id" ref="linkovi"></PharmacyLinks><br>
          <OpenLayersMap v-if="pharmacy" :lon="pharmacy.address.longitude" :lat="pharmacy.address.latitude" editMode="m1"></OpenLayersMap>
        </div>
        <div class="col-md-9">
          <PharmacyDermatologists v-if="dermatologistsList"></PharmacyDermatologists>
          <PharmacyPharmacists v-if="pharmacistsList"></PharmacyPharmacists>
          <PharmaciesAvailableMedicine v-if="availableMedicineList"></PharmaciesAvailableMedicine>
          <PharmacyAppointments v-if="pharmacyAppointmentsList"></PharmacyAppointments>
          <PharmacyOrders v-if="pharmacyOrders"></PharmacyOrders>
          <TimeOffRequests v-if="timeOffRequests"></TimeOffRequests>
          <MedicineInquiries v-if="medicineInquiries"></MedicineInquiries>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import OpenLayersMap from "@/components/pharmacy/OpenLayersMap";
import PharmacyInfo from "@/components/pharmacy/PharmacyInfo";
import PharmacyLinks from "@/components/pharmacy/PharmacyLinks";
import PharmacyDermatologists from "@/components/pharmacy/PharmacyDermatologists";
import PharmacyPharmacists from "@/components/pharmacy/PharmacyPharmacists";
import PharmaciesAvailableMedicine from "@/components/pharmacy/PharmaciesAvailableMedicine";
import PharmacyAppointments from '@/components/pharmacy/PharmacyAppointments';
import {mapState} from "vuex";
import PharmacyOrders from "../components/pharmacy/PharmacyOrders";
import TimeOffRequests from "../components/pharmacy/TimeOffRequests";
import MedicineInquiries from "../components/pharmacy/MedicineInquiries";
import PharmacyService from "../service/PharmacyService";
import PharmacyAdminService from "../service/PharmacyAdminService";

export default {
  name: "Pharmacy",
  components: {
    MedicineInquiries,
    TimeOffRequests,
    PharmacyOrders,
    PharmaciesAvailableMedicine,
    PharmacyPharmacists,
    Navbar, PharmacyDermatologists, PharmacyLinks, PharmacyInfo, OpenLayersMap, PharmacyAppointments },
  data() {
    return {
      pharmacy: null,
      pharmacyAdmin: null,
      componentKey: 0,
      earnings: null
    }
  },
  mounted() {
    if(this.role === 'PHARMACY_ADMIN') {
      console.log("Role : ", this.role);
      console.log("Admin id: ", this.adminId);
      this.getPharmacyId(this.adminId);
    }
    else
      this.loadPharmacyData(this.$route.query.id);
  },
  methods: {
    loadPharmacyData(pharmacyId) {
      PharmacyService.findOne(pharmacyId)
          .then((response) => {
            this.pharmacy = response.data;
            console.log(this.pharmacy.address.longitude, this.pharmacy.address.latitude);
            this.componentKey += 1;
          });
    },
    getPharmacyId(adminId) {
      PharmacyAdminService.findOne(adminId)
          .then(response => {
            this.pharmacyAdmin = response.data;
            this.loadPharmacyData(this.pharmacyAdmin.pharmacyId);
            this.loadEarnings(this.pharmacyAdmin.pharmacyId);
          });
    },
    loadEarnings(pharmacyId) {
      PharmacyService.getEarningsAmount(pharmacyId)
          .then((response) => {
            this.earnings = response.data;
          });
    },
    reload() {
      this.loadPharmacyData(this.pharmacy.id);
    }
  },
  computed: {
    dermatologistsList() {
      return this.$route.path === "/pharmacy/dermatologists";
    },
    pharmacistsList() {
      return this.$route.path === "/pharmacy/pharmacists";
    },
    availableMedicineList() {
      return this.$route.path === "/pharmacy/availableMedicine";
    },
    pharmacyAppointmentsList() {
      return this.$route.path === "/pharmacy/appointments";
    },
    pharmacyOrders() {
      return this.$route.path === "/pharmacy/orders";
    },
    timeOffRequests() {
      return this.$route.path === "/pharmacy/time-off";
    },
    medicineInquiries() {
      return this.$route.path === "/pharmacy/medicine-inquiry";
    },
    ...mapState({
      role: state => state.user.role,
      adminId: state => state.user.id
    })
  }
}
</script>

<style scoped>

body{
  margin-top:20px;
  color: #1a202c;
  text-align: left;
  background-color: #e2e8f0;
}
.main-body {
  padding: 15px;
}
.card {
  box-shadow: 0 1px 3px 0 rgba(0,0,0,.1), 0 1px 2px 0 rgba(0,0,0,.06);
}

.card {
  position: relative;
  display: flex;
  flex-direction: column;
  min-width: 0;
  word-wrap: break-word;
  background-color: #fff;
  background-clip: border-box;
  border: 0 solid rgba(0,0,0,.125);
  border-radius: .25rem;
}

.card-body {
  flex: 1 1 auto;
  min-height: 1px;
  padding: 1rem;
}

.gutters-sm {
  margin-right: -8px;
  margin-left: -8px;
}

.gutters-sm>.col, .gutters-sm>[class*=col-] {
  padding-right: 8px;
  padding-left: 8px;
}
.mb-3, .my-3 {
  margin-bottom: 1rem!important;
}

.bg-gray-300 {
  background-color: #e2e8f0;
}
.h-100 {
  height: 100%!important;
}
.shadow-none {
  box-shadow: none!important;
}

</style>