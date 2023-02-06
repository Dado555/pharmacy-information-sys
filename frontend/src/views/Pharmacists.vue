<template>
  <Navbar></Navbar>

  <div class="container">

    <form @submit.prevent=searchPharmacists>
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
      <select class="search-select" @change=sortPharmacists($event)>
        <option disabled value="">Select one</option>
        <option value="firstName-ascending">First name  &#8593</option>
        <option value="firstName-descending">First name  &#8595</option>
        <option value="lastName-ascending">Last name  &#8593</option>
        <option value="lastName-descending">Last name  &#8595</option>
        <option value="rating-ascending">Rating  &#8593</option>
        <option value="rating-descending">Rating  &#8595</option>
      </select>
    </div>

    <div class="row card-group" v-for="n in Math.ceil(pharmacists.length/4)" :key="n.id">
      <div class="col-3" v-for="pharmacist in pharmacists.slice((n-1)*4, n*4)" :key="pharmacist.id">
        <div class="card">
          <img class="card-img-top" src="../../public/medical-worker-profile-pic.png" alt="Card image cap">
          <div class="card-header"><b>{{ pharmacist.firstName }} {{ pharmacist.lastName }}</b></div>
          <div class="card-body">
            <p>Email: {{ pharmacist.email }}</p>
            <p>Phone number: {{ pharmacist.phoneNumber }}</p>
            <p>Rating: {{ pharmacist.rating }}</p>
            <p v-if="pharmacist.pharmacy">Works in:
              <router-link :to="{ path: '/pharmacy/pharmacists' , query: { id : pharmacist.pharmacy.id}}">
                {{ pharmacist.pharmacy.name }}
              </router-link>
            </p>
            <RatingStars v-if="pharmacist.rateable" :id="pharmacist.id" :rating="pharmacist.rating" :entity="'medical-worker'" :patientRating="pharmacist.patientRating" @reload="getAverageRating(pharmacist)"/>
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
import axios from "axios";
import RatingService from '@/service/RatingService.js';

import Navbar from "@/components/Navbar";
import RatingStars from "@/components/RatingStars";
import Multiselect from '@vueform/multiselect';
import PharmacistService from "@/service/PharmacistService";
import PharmacyService from "@/service/PharmacyService";

export default {
  name: "Pharmacists",
  components: { Navbar, Multiselect, RatingStars },
  data () {
    return {
      value: null,
      options: [],
      pharmacists: [],
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
      pharmacies: [],
      currentPage: 0,
      pageSize: 18,
      totalPages: 1
    }
  },
  mounted() {
    this.loadPharmacies();
  },
  methods: {
    searchPharmacists() {
      this.searchParameters.pharmacyIds = this.value;
      if (this.searchParameters.ratingFrom === "")
        this.searchParameters.ratingFrom = 0;
      if (this.searchParameters.ratingTo === "")
        this.searchParameters.ratingTo = 5;
      PharmacistService.searchPharmacists(this.searchParameters)
        .then((response) => {
        this.pharmacists = response.data.pharmacists;
        this.totalPages = response.data.totalPages === 0 ? 1 : response.data.totalPages;
        this.currentPage = 0;
        document.querySelector(".active").classList.remove("active");
        document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
      });
    },
    sortPharmacists(event) {
      let sortOrder = event.target.value.split("-");
      this.searchParameters.sortBy = sortOrder[0];
      this.searchParameters.orderBy = sortOrder[1];
      this.searchPharmacists();
    },
    loadPharmacies() {
      PharmacyService.getAllPharmacies()
      .then((response) => {
        this.pharmacies = response.data;
        this.options = [];
        for(let i = 0; i < this.pharmacies.length; i++) {
          this.options.push({value: this.pharmacies[i].id, label:this.pharmacies[i].name});
        }
        this.loadPharmacists();
      });
    },
    loadPharmacists() {
      PharmacistService.pharmacistsPage()
      .then((response) => {
        this.pharmacists = response.data.pharmacists;
        for(let j = 0; j < this.pharmacists.length; j++) {
          if(this.pharmacists[j].pharmacyId === null) {
            this.pharmacists[j].pharmacy = null;
            continue;
          }
          for(let i = 0; i < this.pharmacies.length; i++) {
            if(this.pharmacies[i].id === this.pharmacists[j].pharmacyId){
              this.pharmacists[j].pharmacy = this.pharmacies[i];
              break;
            }
          }
        }
        this.totalPages = response.data.totalPages;
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
      document.getElementById(pageNumber+1)?.classList?.toggle("active");
      PharmacistService.updatePage(pageNumber, this.searchParameters)
      .then((response) => {
        this.pharmacists = response.data.pharmacists;
        this.totalPages = response.data.totalPages === 0 ? 1 : response.data.totalPages;
        if (this.pharmacists.length === 0 && this.currentPage > 0)
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
    }
  }
}
</script>
<style src="@vueform/multiselect/themes/default.css"></style>

<style scoped>
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