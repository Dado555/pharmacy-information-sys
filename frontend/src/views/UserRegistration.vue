<template>
  <Navbar></Navbar>
  <div class="modal-dialog">
    <div class="modal-content">
      <a class="navbar-brand" href="/">
        <img alt="Logo" src="../assets/pharmacy.png">
      </a>
      <div class="modal-body text-start">
        <form>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.email" v-bind:class="{'is-invalid': invalidEmail}"
                   placeholder="Email">
            <div class="invalid-feedback">
              Invalid email.
            </div>
          </div>
          <div class="form-group">
            <input type="password" class="form-control" v-model="user.password"
                   v-bind:class="{'is-invalid': invalidPassword}" placeholder="Password">
            <div class="invalid-feedback">
              Invalid password.
            </div>
          </div>
          <div class="form-group">
            <input type="password" class="form-control" v-model="confirmPassword"
                   v-bind:class="{'is-invalid': invalidConfirmPassword}" placeholder="Confirm password">
            <div class="invalid-feedback">
              Passwords do not match.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.firstName"
                   v-bind:class="{'is-invalid': invalidFirstName}" placeholder="First name">
            <div class="invalid-feedback">
              Invalid first name.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.lastName"
                   v-bind:class="{'is-invalid': invalidLastName}" placeholder="Last name">
            <div class="invalid-feedback">
              Invalid last name.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.phoneNumber"
                   v-bind:class="{'is-invalid': invalidPhoneNumber}" placeholder="Phone number">
            <div class="invalid-feedback">
              Invalid phone number.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.address.city"
                   v-bind:class="{'is-invalid': invalidAddressCity}" placeholder="City">
            <div class="invalid-feedback">
              Invalid city.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.address.name"
                   v-bind:class="{'is-invalid': invalidAddressName}" placeholder="Street">
            <div class="invalid-feedback">
              Invalid street.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.address.number"
                   v-bind:class="{'is-invalid': invalidAddressNumber}" placeholder="Number">
            <div class="invalid-feedback">
              Invalid number.
            </div>
          </div>
          <div class="form-group">
            <input type="text" class="form-control" v-model="user.address.postalCode"
                   v-bind:class="{'is-invalid': invalidAddressPostalCode}" placeholder="Postal code">
            <div class="invalid-feedback">
              Invalid postal code.
            </div>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" v-on:click="signUp">Sign up</button>
      </div>
    </div>
  </div>
  <div class="modal-dialog">
    <div class="modal-content">
      <label class="col-form-label">Have an account? <a href="http://localhost:8080/login">Log in</a> </label>
    </div>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import PatientService from "@/service/PatientService";

export default {
  name: "UserRegistration",
  components: {Navbar},
  methods: {
    signUp(event) {
      if (this.validate()) {
        PatientService.registerUser(this.user)
            .then(response => {
              if (response.data == null) {
                this.$toast.show('There is already user with same email!', {
                  type: 'error',
                  position: 'top',
                  duration: 2000
                });
              } else {
                this.$toast.show('Please check your email and complete registration!', {
                  type: 'info',
                  position: 'top',
                  duration: 2000
                });
              }
        })
      }
    },
    validate() {
      this.invalidEmail = false;
      this.invalidPassword = false;
      this.invalidFirstName = false;
      this.invalidLastName = false;
      this.invalidPhoneNumber = false;
      this.invalidAddressCity = false;
      this.invalidAddressName = false;
      this.invalidAddressNumber = false;
      this.invalidAddressPostalCode = false;
      this.invalidConfirmPassword = false;

      if (!this.user.email.match('^(.+)@(.+)$')) {
        this.invalidEmail = true;
      }
      if (this.user.password === "" || !this.user.password.match('^(?=.*[0-9]).{8,}$')) {
        this.invalidPassword = true;
      }
      if (this.user.firstName === "" || !this.user.firstName.match('^[A-Z]{1}[a-z]+$')) {
        this.invalidFirstName = true;
      }
      if (this.user.lastName === "" || !this.user.lastName.match('^[A-Z]{1}[a-z]+$')) {
        this.invalidLastName = true;
      }
      if (this.user.phoneNumber === "" || !this.user.phoneNumber.match('^[0-9]{10}$')) {
        this.invalidPhoneNumber = true;
      }
      if (this.user.address.city === undefined || this.user.address.city === "") {
        this.invalidAddressCity = true;
      }
      if (this.user.address.name === undefined || this.user.address.name === "") {
        this.invalidAddressName = true;
      }
      if (this.user.address.number === undefined || this.user.address.number === ""||
          !this.user.address.number.match('^[0-9]*$')) {
        this.invalidAddressNumber = true;
      }
      if (this.user.address.postalCode === undefined || this.user.address.postalCode === ""
          || !this.user.address.postalCode.match('^[0-9]*$')) {
        this.invalidAddressPostalCode = true;
      }
      if (this.confirmPassword === "" || this.user.password !== this.confirmPassword) {
        this.invalidConfirmPassword = true;
      }

      return !this.invalidEmail && !this.invalidPassword && !this.invalidFirstName && !this.invalidLastName &&
          !this.invalidPhoneNumber && !this.invalidAddressCity && !this.invalidAddressName && !this.invalidAddressNumber
          && !this.invalidAddressPostalCode && !this.invalidConfirmPassword;

    }
  },
  data() {
    return {
      user: {
        email: "",
        password: "",
        firstName: "",
        lastName: "",
        phoneNumber: "",
        address: {}
      },
      invalidEmail: false,
      invalidPassword: false,
      invalidFirstName: false,
      invalidLastName: false,
      invalidPhoneNumber: false,
      invalidAddressCity: false,
      invalidAddressName: false,
      invalidAddressNumber: false,
      invalidAddressPostalCode: false,
      invalidConfirmPassword: false,

      confirmPassword: ""
    }
  }
}
</script>

<style scoped>

.navbar-brand img {
  height: 60px;
  float: inside;
  margin-right: 30px;
  margin-left: 30px;
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