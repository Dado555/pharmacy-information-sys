<template>
<div class="card mb-3">
    <div class="card-body">
        <div class="row">
            <div class="col-sm-3">
                <h6 class="mb-0">First Name</h6>
            </div>
            <div class="col-sm-9 text-secondary">
                {{ $store.getters.getUser.firstName }}
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-sm-3">
                <h6 class="mb-0">Last Name</h6>
            </div>
            <div class="col-sm-9 text-secondary">
                {{ $store.getters.getUser.lastName }}
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-sm-3">
                <h6 class="mb-0">Phone Number</h6>
            </div>
            <div class="col-sm-9 text-secondary">
                {{ $store.getters.getUser.phoneNumber }}
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-sm-3">
                <h6 class="mb-0">Email</h6>
            </div>
            <div class="col-sm-9 text-secondary">
                {{ $store.getters.getUser.email }}
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-sm-3">
                <h6 class="mb-0">Address</h6>
            </div>
            <div class="col-sm-9 text-secondary">
                {{ $store.getters.getUser.address.name }}, {{ $store.getters.getUser.address.number }}, {{ $store.getters.getUser.address.city }}
            </div>
        </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Category</h6>
        </div>
        <div v-if="patient.category?.name === 'SILVER'" class="col-sm-9 text-secondary">
          {{ patient.category?.name }}<br>
          <img class="category" src="../../../src/assets/second.png" alt="">
        </div>
        <div v-if="patient.category?.name === 'GOLD'" class="col-sm-9 text-secondary">
          {{ patient.category?.name }}<br>
          <img class="category" src="../../../src/assets/first.png" alt="">
        </div>
        <div v-if="patient.category?.name !== 'GOLD' && patient.category?.name !== 'SILVER'" class="col-sm-9 text-secondary">
          {{ patient.category?.name }}<br>
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Discount points</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ patient.points }}
        </div>
      </div>
    </div>
</div>
</template>

<script>
import PatientService from "@/service/PatientService";

export default {
  name: "PatientInfo",
  data() {
    return {
      patient: {}
    }
  },
  mounted() {
    this.loadPatient();
  },
  methods: {
    loadPatient() {
      let data = this;
      PatientService.findOne(this.$store.getters.getUser.id)
      .then((response) => {
        data.patient = response.data;
      });

    }
  }
}
</script>

<style scoped>
.category {
  text-align: right;
  width: 70px;
  height: auto;
}

</style>