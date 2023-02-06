<template>
  <Navbar></Navbar>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="header">
        <h1 class="display-5">Add new pharmacy</h1>
      </div>
      <div class="modal-body text-start">
        <form>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.name" v-bind:class="{'is-invalid': invalidName}"
                   placeholder="Name">
            <div class="invalid-feedback">
              Invalid name.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.address.city"
                   v-bind:class="{'is-invalid': invalidAddressCity}" placeholder="City">
            <div class="invalid-feedback">
              Invalid city.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.address.name"
                   v-bind:class="{'is-invalid': invalidAddressName}" placeholder="Street">
            <div class="invalid-feedback">
              Invalid street.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.address.number"
                   v-bind:class="{'is-invalid': invalidAddressNumber}" placeholder="Number">
            <div class="invalid-feedback">
              Invalid number.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.address.postalCode"
                   v-bind:class="{'is-invalid': invalidAddressPostalCode}" placeholder="Postal code">
            <div class="invalid-feedback">
              Invalid postal code.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.point.pharmacistCounseling"
                   v-bind:class="{'is-invalid': invalidPharmacistCounseling}" placeholder="Discount points for pharmacist counseling">
            <div class="invalid-feedback">
              Invalid counseling discount points.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.point.dermatologistExamination"
                   v-bind:class="{'is-invalid': invalidDermatologistExamination}" placeholder="Discount points for dermatologist examination">
            <div class="invalid-feedback">
              Invalid examination discount points.
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" v-on:click="addPharmacy">ADD</button>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import axios from "axios";
export default {
  name: "AddPharmacy",
  components: {Navbar},
  methods: {
    addPharmacy(event) {
      if(this.validate()) {
        axios({
          method: 'POST',
          data: this.input,
          url: 'https://pis-back.herokuapp.com/api/pharmacies',
        })
        this.$toast.show('Pharmacy successfully added!', {
          type: 'info',
          position: 'top',
          duration: 2000
        });
      }
    },
    validate() {
      this.invalidName = false;
      this.invalidAddressCity = false;
      this.invalidAddressName = false;
      this.invalidAddressNumber = false;
      this.invalidAddressPostalCode = false;
      this.invalidPharmacistCounseling = false;
      this.invalidDermatologistExamination = false;

      if (this.input.name === "") {
        this.invalidName = true;
      }
      if (this.input.address.city === undefined || this.input.address.city === "") {
        this.invalidAddressCity = true;
      }
      if (this.input.address.name === undefined || this.input.address.name === "") {
        this.invalidAddressName = true;
      }
      if (this.input.address.number === undefined || this.input.address.number === ""||
          !this.input.address.number.match('^[0-9]*$')) {
        this.invalidAddressNumber = true;
      }
      if (this.input.address.postalCode === undefined || this.input.address.postalCode === ""
          || !this.input.address.postalCode.match('^[0-9]*$')) {
        this.invalidAddressPostalCode = true;
      }
      if (this.input.point.dermatologistExamination === undefined || this.input.point.dermatologistExamination === ""
          || !this.input.point.dermatologistExamination.match('^[0-9]*$')) {
        this.invalidDermatologistExamination = true;
      }
      if (this.input.point.pharmacistCounseling === undefined || this.input.point.pharmacistCounseling === ""
          || !this.input.point.pharmacistCounseling.match('^[0-9]*$')) {
        this.invalidPharmacistCounseling = true;
      }

      return !this.invalidName && !this.invalidAddressCity && !this.invalidAddressName && !this.invalidAddressNumber
          && !this.invalidAddressPostalCode && !this.invalidDermatologistExamination && !this.invalidPharmacistCounseling;
    }
  },
  data() {
    return {
      input: {
        name: "",
        address: {},
        point: {}
      },
      invalidName: false,
      invalidAddressCity: false,
      invalidAddressName: false,
      invalidAddressNumber: false,
      invalidAddressPostalCode: false,
      invalidPharmacistCounseling: false,
      invalidDermatologistExamination: false
    }
  }
}
</script>

<style scoped>

.header {
  background: #0d6efd;
  color: white;
  font-size: 30px;
}

input {
  outline: none;
  border-radius: 37px;
  border: none;
  background: #d3d3d3;
  font-family: Tahoma;
  margin-bottom: 15px;
}

label {
  font-family: Tahoma;
}

.btn {
  margin: auto;
  display: block;
}

a {
  color: blue;
  text-decoration: underline;
}

</style>