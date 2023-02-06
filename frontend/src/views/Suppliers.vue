<template>
  <Navbar></Navbar>

  <div class="container">

    <div class="ui-button add" v-if="user.role == 'SYSTEM_ADMIN'">
      <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#AddSupplierModal">Add supplier</button>
    </div>

    <div class="modal fade" id="AddSupplierModal" tabindex="-1" aria-labelledby="AddSupplierModal" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="AddSupplierModal">Add new supplier</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <!-- Modal body -->
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
              <button type="button" class="btn btn-primary" v-on:click="addSupplier">ADD</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="row card-group" v-for="n in Math.ceil(suppliers.length/4)" :key="n.id">
      <div class="col-3" v-for="supplier in suppliers.slice((n-1)*4, n*4)" :key="supplier.id">
        <div class="card">
          <img class="card-img-top" src="../../public/medical-worker-profile-pic.png" alt="Card image cap">
          <div class="card-header"><b>{{ supplier.firstName }} {{ supplier.lastName }}</b></div>
          <div class="card-body">
            <p>Email: {{ supplier.email }}</p>
            <p>Phone number: {{ supplier.phoneNumber }}</p>
            <p>Rating: {{ supplier.rating }}</p>
            <RatingStars v-if="supplier.rateable" :id="supplier.id" :rating="supplier.rating" :entity="'medical-worker'" :patientRating="supplier.patientRating" @reload="getAverageRating(supplier)"/>
          </div>
        </div>
      </div>
    </div>

    <div class="pagination-wrapper">
      <nav aria-label="Page navigation example" class="nav-pagination">
        <ul class="pagination">
          <li class="page-item"><button v-if="showPreviousLink()" @click="updatePage(currentPage - 1)" class="page-link">Previous</button></li>
          <li v-for="index in totalPages" :key="index" class="page-item" :id="index"><button class="page-link" @click="updatePage(index-1)"> {{ index }} </button></li>
          <li class="page-item"><button v-if="showNextLink()" @click="updatePage(currentPage + 1)" class="page-link">Next</button></li>
        </ul>
      </nav>
    </div>
  </div>
</template>

<script>
import RatingService from "@/service/RatingService.js";

import Navbar from "@/components/Navbar";
import RatingStars from "@/components/RatingStars"
import Multiselect from '@vueform/multiselect'
import SupplierService from "@/service/SupplierService";

export default {
  name: "Suppliers",
  components: {Navbar, Multiselect, RatingStars},
  data () {
    return {
      value: [],
      options: [],
      suppliers: [],
      currentPage: 0,
      pageSize: 18,
      totalPages: 1,
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
  },
  mounted() {
    this.loadSuppliers();
  },
  computed: {
    user() {
      return this.$store.getters.getUser;
    }
  },
  methods: {
    loadSuppliers() {
      SupplierService.suppliersPage()
      .then((response) => {
        this.suppliers = response.data.suppliers;
        this.totalPages = response.data.totalPages;
        document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
      });
    },
    updatePage(pageNumber) {
      this.currentPage = pageNumber;
      document.querySelector(".active")?.classList?.remove("active");
      document.getElementById(pageNumber+1).classList.toggle("active");
    },
    getAverageRating(medicalWorker) {
      RatingService.getAverageRatingForMedicalWorker(medicalWorker.id)
          .then(response => { medicalWorker.rating = response.data; })
    },
    showPreviousLink() {
      return this.currentPage !== 0;
    },
    showNextLink() {
      return this.currentPage !== this.totalPages - 1;
    },
    addSupplier(event) {
      if (this.validate()) {
        SupplierService.addSupplier(this.input)
            .then(response => {
              this.$toast.show('Supplier successfully added!', {
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
  }
}
</script>
<style src="@vueform/multiselect/themes/default.css"></style>

<style scoped>

.add {
  text-align: right;
  margin-bottom: 2%;
}

img {
  height: 10%
}

.container {
  margin-top: 5%;
}

.row {
  margin-top: 2%;
  margin-bottom: 5%;
}

.card {
  text-align: left;
}

p {
  margin: 0
}

.sort-filter {
  text-align: left;
}
</style>