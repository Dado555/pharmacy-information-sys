<template>
  <Navbar></Navbar>

  <div class="container">
    <div class="main-body">
      <div class="row gutters-sm">
        <div class="col-md-4 mb-3">
          <PatientUpdate></PatientUpdate>
          <PatientLinks></PatientLinks>
        </div>
        <div class="col-md-8">
          <PatientInfo v-if="homePage"></PatientInfo>
          <router-view> </router-view>
      </div>
    </div>
  </div>
</div>

</template>

<script>
import Navbar from '@/components/Navbar.vue'
import PatientUpdate from '@/components/patient/PatientUpdate.vue'
import PatientLinks from '@/components/patient/PatientLinks.vue'
import PatientInfo from '@/components/patient/PatientInfo.vue'

export default {
    name: "PatientProfile",
    components: {
        Navbar, PatientUpdate, PatientLinks, PatientInfo
    },
    computed: {
      homePage() {
        return (this.$route.path === "/patient")
      },
      reservedMedicines () {
        return (this.$route.path === "/patient/reserved-medicines")
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