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
            <input type="text" class="form-control" v-model="user.email"
                   v-bind:class="{'is-invalid': invalidEmail}" placeholder="Email">
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
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" @click="login">Log in</button>
      </div>
    </div>
  </div>

  <div class="modal" id="password-modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Change password</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="cancel" id="close"></button>
        </div>
        <div class="modal-body text-start">
          <form>
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
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" v-on:click="changePassword">Confirm</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" v-on:click="cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import LoginService from '@/service/LoginService.js'
import Navbar from "../components/Navbar"
import { Modal } from 'bootstrap';

export default {
  name: "Login",
  components: {Navbar},
  methods: {
    login(event) {
      if (this.validate()) {
        LoginService.login(this.user).then(response => {
          if (response.data.accessToken.toString() === "not_active") {
            this.$toast.show('User not active!', {
              type: 'info',
              position: 'top',
              duration: 2000
            });
          }
          else if (response.data.accessToken.toString() === "first_login") {
            this.$toast.show('First login! Change password.', {
              type: 'info',
              position: 'top',
              duration: 3000
            });

            let myModal = new Modal(document.getElementById('password-modal'));
            myModal.show();

          }
          else {
            localStorage.setItem('token', JSON.stringify(response.data.accessToken));
            axios.defaults.headers.common['Authorization'] = "Bearer " + JSON.stringify(response.data.accessToken);
            this.$store.dispatch('getUser');
            this.$toast.show('Welcome! ', {
              type: 'info',
              position: 'top',
              duration: 2000
            });
            console.log(this.$store.getters.getUser.role);
            window.location.href = "/";
          }
        }).catch(response => {
          this.$toast.show("Invalid email or password!",
              {
                type: 'error',
                duration: 2000,
                position: 'top'
              });
        });
      }
    },
    validate() {
      this.invalidEmail = false;
      this.invalidPassword = false;

      if (!this.user.email.match('^(.+)@(.+)$')) {
        this.invalidEmail = true;
      }
      if (this.user.password === "") {
        this.invalidPassword = true;
      }

      return !this.invalidEmail && !this.invalidPassword;
    },
    validateChangePassword() {
      this.invalidPassword = false;
      this.invalidConfirmPassword = false;

      if (this.user.password === "" || !this.user.password.match('^(?=.*[0-9]).{8,}$')) {
        this.invalidPassword = true;
      }
      if (this.confirmPassword === "" || this.user.password !== this.confirmPassword) {
        this.invalidConfirmPassword = true;
      }

      return !this.invalidPassword && !this.invalidConfirmPassword;
    },
    cancel() {
      this.invalidPassword = false;
      this.invalidConfirmPassword = false;
    },
    changePassword() {
      if (this.validateChangePassword()) {
        localStorage.removeItem('token');
        this.$router.go();
        LoginService.changePassword(this.user);
      }
    }
  },
  data() {
    return {
      user: {
        email: "",
        password: ""
      },
      invalidEmail: false,
      invalidPassword: false,
      invalidConfirmPassword: false,

      confirmPassword: ""
    }
  }
}
</script>

<style scoped>

body{margin-top:20px;}

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