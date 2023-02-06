<template>
  <Navbar></Navbar>

  <div class="container">
    <div class="main-body">
      <div class="row gutters-sm">
        <div class="col-md-4 mb-3">
          <MedicalWorkerUpdate></MedicalWorkerUpdate>
          <MedicalWorkerLinks></MedicalWorkerLinks>
        </div>
        <div class="col-md-8">
          <MedicalWorkerInfo v-if="homePage"></MedicalWorkerInfo>
          <router-view> </router-view>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import Navbar from '@/components/Navbar.vue'
import MedicalWorkerUpdate from '@/components/medical_worker/MedicalWorkerUpdate.vue'
import MedicalWorkerLinks from '@/components/medical_worker/MedicalWorkerLinks.vue'
import MedicalWorkerInfo from '@/components/medical_worker/MedicalWorkerInfo.vue'
import WorkCalendar from '@/components/medical_worker/WorkCalendar.vue'
export default {
  name: "MedicalWorkerProfile",
  components: {
    Navbar, MedicalWorkerUpdate, MedicalWorkerLinks, MedicalWorkerInfo, WorkCalendar
  },
  computed: {
    homePage() {
      if(this.$route.path == "/medical-worker") {
        return true
      } else {
        return false
      }
    },
    patientSearch() {
      if(this.$route.path == "/medical-worker/patients") {
        return true
      } else {
        return false
      }
    }
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