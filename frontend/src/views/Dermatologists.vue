<template>
  <Navbar></Navbar>

  <div class="container">

    <div class="ui-button add" v-if="user.role == 'SYSTEM_ADMIN'">
      <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#AddDermatologistModal">Add dermatologist</button>
    </div>

    <div class="modal fade" id="AddDermatologistModal" tabindex="-1" aria-labelledby="AddDermatologistModal" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="AddDermatologistModal">Add new dermatologist</h5>
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
              <button type="button" class="btn btn-primary" v-on:click="addDermatologist">ADD</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <form @submit.prevent=searchDermatologists()>
      <div class="search">
        <div class="input-group">
          <input v-model="searchParameters.firstName" type="text" class="form-control" placeholder="First name: ">
          <input v-model="searchParameters.lastName" type="text" class="form-control" placeholder="Last name: ">
          <input v-model="searchParameters.ratingFrom" type="number" class="form-control" placeholder="Rating From: ">
          <input v-model="searchParameters.ratingTo" type="number" class="form-control" placeholder="Rating To: ">

          <div class="input-group-append">
            <button class="btn btn-primary">Search</button>
          </div>
        </div>
        <Multiselect style="background: white" v-model="value" mode="tags"
                     placeholder="Add pharmacy" :searchable="true"
                     :createTag="true" :options="options"></Multiselect>
      </div>
    </form><br><br>

    <div class="sort-filter">
      <h3>Sort by: </h3>
      <select class="search-select" @change=sortDermatologists($event)>
        <option disabled value="">Select one</option>
        <option value="firstName-ascending">First name  &#8593</option>
        <option value="firstName-descending">First name  &#8595</option>
        <option value="lastName-ascending">Last name  &#8593</option>
        <option value="lastName-descending">Last name  &#8595</option>
        <option value="rating-ascending">Rating  &#8593</option>
        <option value="rating-descending">Rating  &#8595</option>
      </select>
    </div>

    <div class="row card-group" v-for="n in Math.ceil(dermatologists.length/4)" :key="n.id">
      <div class="col-3" v-for="dermatologist in dermatologists.slice((n-1)*4, n*4)" :key="dermatologist.id">
        <div class="card">
          <img class="card-img-top" src="../../public/medical-worker-profile-pic.png" alt="Card image cap">
          <div class="card-header"><b>{{ dermatologist.firstName }} {{ dermatologist.lastName }}</b></div>
          <div class="card-body">
            <p>Email: {{ dermatologist.email }}</p>
            <p>Phone number: {{ dermatologist.phoneNumber }}</p>
            <p>Rating: {{ dermatologist.rating }}</p>
            <p v-if="dermatologist.pharmacies">
              Works in:
              <router-link v-for="pharmacy in dermatologist.pharmacies"
                           :to="{ path: '/pharmacy/dermatologists' , query: { id : pharmacy.id}}">{{ pharmacy.name }}--
              </router-link>
            </p>
            <RatingStars v-if="dermatologist.rateable" :id="dermatologist.id" :rating="dermatologist.rating" :entity="'medical-worker'" :patientRating="dermatologist.patientRating" @reload="getAverageRating(dermatologist)"/>
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
import DermatologistService from "@/service/DermatologistService";
import PharmacyService from "@/service/PharmacyService";

export default {
  name: "Dermatologists",
  components: {Navbar, Multiselect, RatingStars},
  data () {
    return {
      value: [],
      options: [],
      dermatologists: [],
      searchParameters: {
        firstName: "",
        lastName: "",
        ratingFrom: "",
        ratingTo: "",
        sortBy: "",
        orderBy: "",
        pharmacyIds: [],
        phoneNumber: "",
        address: ""
      },
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
    this.loadPharmacies();
  },
  computed: {
    user() {
      return this.$store.getters.getUser;
    }
  },
  methods: {
    loadDermatologists() {
      DermatologistService.dermatologistsPage()
          .then((response) => {
            this.dermatologists = response.data.dermatologists;
            for(let j = 0; j < this.dermatologists.length; j++) {
              if(this.dermatologists[j].pharmacyIds.length === 0) {
                this.dermatologists[j].pharmacies = null;
                continue;
              }
              this.dermatologists[j].pharmacies = [];
              for(let i = 0; i < this.pharmacies.length; i++) {
                if(this.dermatologists[j].pharmacyIds.includes(this.pharmacies[i].id)){
                  this.dermatologists[j].pharmacies.push(this.pharmacies[i]);
                }
              }
            }

            this.totalPages = response.data.totalPages;
            document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
          });
    },
    loadPharmacies() {
      PharmacyService.getAllPharmacies()
          .then((response) => {
            this.pharmacies = response.data;
            console.log(this.pharmacies);
            this.options = [];
            for(let i = 0; i < this.pharmacies.length; i++) {
              this.options.push({value: this.pharmacies[i].id, label:this.pharmacies[i].name});
            }
            this.loadDermatologists();
          });
    },
    sortDermatologists(event) {
      let sortOrder = event.target.value.split("-");
      this.searchParameters.sortBy = sortOrder[0];
      this.searchParameters.orderBy = sortOrder[1];
      this.searchDermatologists();
    },
    searchDermatologists() {
      console.log(this.value);
      this.searchParameters.pharmacyIds = this.value;
      if (this.searchParameters.ratingFrom === "")
        this.searchParameters.ratingFrom = 0;
      if (this.searchParameters.ratingTo === "")
        this.searchParameters.ratingTo = 5;
      DermatologistService.search(this.searchParameters)
          .then((response) => {
            this.dermatologists = response.data.dermatologists;
            this.totalPages = response.data.totalPages === 0 ? 1 : response.data.totalPages;
            this.currentPage = 0;
            document.querySelector(".active").classList.remove("active");
            document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
          });
    },
    updatePage(pageNumber) {
      if (this.searchParameters.ratingFrom === "")
        this.searchParameters.ratingFrom = 0;
      if (this.searchParameters.ratingTo === "")
        this.searchParameters.ratingTo = 5;
      if (this.searchParameters.sortBy) {
        let sortOrder = this.searchParameters.sortBy.split("-");
        this.searchParameters.sortBy = sortOrder[0];
        this.searchParameters.orderBy = sortOrder[1];
      }

      this.currentPage = pageNumber;
      document.querySelector(".active")?.classList?.remove("active");
      document.getElementById(pageNumber+1).classList.toggle("active");
      DermatologistService.searchPage(pageNumber, this.searchParameters)
          .then((response) => {
            this.dermatologists = response.data.dermatologists;
            this.totalPages = response.data.totalPages === 0 ? 1 : response.data.totalPages;
            if (this.dermatologists.length === 0 && this.currentPage > 0)
              this.updatePage(this.currentPage - 1);
          });
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