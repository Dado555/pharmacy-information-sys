<template>
  <Navbar></Navbar>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="header">
        <h1 class="display-5">Add new dermatologist</h1>
      </div>
      <div class="modal-body text-start">
        <form>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.email" v-bind:class="{'is-invalid': invalidEmail}"
                   placeholder="Email">
            <div class="invalid-feedback">
              Invalid email.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.firstName"
                   v-bind:class="{'is-invalid': invalidFirstName}" placeholder="First name">
            <div class="invalid-feedback">
              Invalid first name (first letter uppercase).
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.lastName"
                   v-bind:class="{'is-invalid': invalidLastName}" placeholder="Last name">
            <div class="invalid-feedback">
              Invalid last name (first letter uppercase).
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="input.phoneNumber"
                   v-bind:class="{'is-invalid': invalidPhoneNumber}" placeholder="Phone number">
            <div class="invalid-feedback">
              Invalid phone number (10 digits).
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
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" v-on:click="addDermatologist">ADD</button>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import DermatologistService from "@/service/DermatologistService";
export default {
  name: "AddDermatologist",
  components: {Navbar},
  methods: {
    addDermatologist(event) {
      if (this.validate()) {
        DermatologistService.addDermatologist(this.input)
          .then(response => {
            this.$toast.show('Dermatologist successfully added!', {
              type: 'info',
              position: 'top',
              duration: 2000
            });
          }).catch(response => {
            this.$toast.show('Email already taken!', {
              type: 'error',
              position: 'top',
              duration: 2000
            });
          });
      }
    },
    validate() {
      this.invalidEmail = false;
      this.invalidFirstName = false;
      this.invalidLastName = false;
      this.invalidPhoneNumber = false;
      this.invalidAddressCity = false;
      this.invalidAddressName = false;
      this.invalidAddressNumber = false;
      this.invalidAddressPostalCode = false;

      if (!this.input.email.match('^(.+)@(.+)$')) {
        this.invalidEmail = true;
      }
      if (this.input.firstName === "" || !this.input.firstName.match('^[A-Z]{1}[a-z]+$')) {
        this.invalidFirstName = true;
      }
      if (this.input.lastName === "" || !this.input.lastName.match('^[A-Z]{1}[a-z]+$')) {
        this.invalidLastName = true;
      }
      if (this.input.phoneNumber === "" || !this.input.phoneNumber.match('^[0-9]{10}$')) {
        this.invalidPhoneNumber = true;
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

      return !this.invalidEmail && !this.invalidFirstName && !this.invalidLastName &&
          !this.invalidPhoneNumber && !this.invalidAddressCity && !this.invalidAddressName && !this.invalidAddressNumber
          && !this.invalidAddressPostalCode;
    }
  },
  data() {
    return {
      input: {
        email: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        address: {}
      },
      invalidEmail: false,
      invalidFirstName: false,
      invalidLastName: false,
      invalidPhoneNumber: false,
      invalidAddressCity: false,
      invalidAddressName: false,
      invalidAddressNumber: false,
      invalidAddressPostalCode: false
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