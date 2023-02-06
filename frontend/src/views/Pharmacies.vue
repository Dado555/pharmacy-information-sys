  
<template>
    <Navbar></Navbar>

    <div class="container">

        <div class="modal fade" id="AddPharmacyModal" tabindex="-1" aria-labelledby="AddPharmacyModal" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="AddPharmacyModal">Add new pharmacy</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <!-- Modal body -->
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
                                <div class="form-group">
                                  <input type="text" class="form-control" v-model="input.counselingPrice"
                                         v-bind:class="{'is-invalid': invalidCounselingPrice}" placeholder="Counseling price">
                                  <div class="invalid-feedback">
                                    Invalid counseling price.
                                  </div>
                                </div>
                                <div class="form-group">
                                  <br>
                                  <p style="color: gray">Start work time:</p>
                                  <input type="time" class="form-control" v-model="input.startWorkTime"
                                         v-bind:class="{'is-invalid': invalidStartWorkTime}" placeholder="Start work time">
                                  <div class="invalid-feedback">
                                    Invalid start work time.
                                  </div>
                                </div>
                                <div class="form-group">
                                  <br>
                                  <p style="color: gray">End work time:</p>
                                  <input type="time" class="form-control" v-model="input.endWorkTime"
                                         v-bind:class="{'is-invalid': invalidEndWorkTime}" placeholder="End work time">
                                  <div class="invalid-feedback">
                                    Invalid end work time.
                                  </div>
                                </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" v-on:click="addPharmacy">ADD</button>
                            </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="ui-button add" v-if="user.role == 'SYSTEM_ADMIN'">
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#AddPharmacyModal">Add pharmacy</button>
        </div>

        <form @submit.prevent=searchByCriteria>
            <div class="search">
                <div class="input-group">
                    <input v-model="searchParameters.name" type="text" class="form-control" placeholder="Name: ">
                    <input v-model="searchParameters.city" type="text" class="form-control" placeholder="City: ">
                    <div class="input-group-append">
                        <button class="btn btn-primary">Search</button>
                    </div>
                </div>
            </div>
        </form><br><br>

        <div class="sort-filter">
            <h3>Sort by: </h3>
            <select class="search-select" @change=sortPharmacies($event)>
                <option disabled value="">Select one</option>
                <option value="name-ascending">Name  &#8593</option>
                <option value="name-descending">Name  &#8595</option>
                <option value="city-ascending">City  &#8593</option>
                <option value="city-descending">City  &#8595</option>
                <option value="rating-ascending">Rating  &#8593</option>
                <option value="rating-descending">Rating  &#8595</option>
            </select>
        </div>

        <div class="row card-group" v-for="n in Math.ceil(pharmacies.length/4)" :key="n.id">
            <div class="col-3" v-for="pharmacy in pharmacies.slice((n-1)*4, n*4)" :key="pharmacy.id">
                <div class="card">
                    <router-link class="routerLink" :to="{ path: 'pharmacy', query: { id : pharmacy.id}}">
                    <img class="card-img-top" src="../../public/pharmacy.png" alt="Card image cap">
                    <div class="card-header"><b>{{ pharmacy.name }}</b></div>
                    </router-link>
                    <div class="card-body">
                        <p>ID: {{ pharmacy.id }}</p>
                        <p>Address: {{ pharmacy.address.name }} {{ pharmacy.address.number }}, {{pharmacy.address.city}} {{pharmacy.address.postalCode}} </p>
                        <p>Rating: {{ pharmacy.rating }} </p>
                        <RatingStars  v-if="pharmacy.rateable" :id="pharmacy.id" :rating="pharmacy.rating" :entity="'pharmacy'" :patientRating="pharmacy.patientRating" @reload="getAverageRating(pharmacy)"/>
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
import PharmacyService from '@/service/PharmacyService.js';
import RatingService from '@/service/RatingService.js';

import Navbar from '@/components/Navbar.vue';
import RatingStars from "@/components/RatingStars.vue";
import axios from 'axios';

export default {
    name: "Pharmacies",
    components: { Navbar, RatingStars },
    data() {
        return {
            pharmacies: [],
            searchParameters: { 
                name: "", 
                city: "", 
                sortBy: "", 
                orderBy: ""
            },
            currentPage: 0,
            pageSize: 8,
            totalPages: 1,

            input: {
                name: "",
                address: {},
                point: {},
                counselingPrice: "",
                startWorkTime: "",
                endWorkTime: ""
            },
            invalidName: false,
            invalidAddressCity: false,
            invalidAddressName: false,
            invalidAddressNumber: false,
            invalidAddressPostalCode: false,
            invalidPharmacistCounseling: false,
            invalidDermatologistExamination: false,
            invalidCounselingPrice: false,
            invalidStartWorkTime: false,
            invalidEndWorkTime: false
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
        loadPharmacies() {
            let data = this;
            data.currentPage = 0;
            PharmacyService.findAll()
            .then(response => {
                data.pharmacies = response.data.pharmacies;
                data.totalPages = response.data.totalPages == 0 ? 1 : response.data.totalPages;
                document.querySelector(".active")?.classList?.remove("active");
                document.getElementById((data.currentPage+1).toString()).classList.toggle("active");
            })
        },
        searchByCriteria() {
            PharmacyService.searchPharmacies(this.searchParameters)
            .then((response) => {
                this.pharmacies = response.data.pharmacies;
                this.totalPages = response.data.totalPages == 0 ? 1 : response.data.totalPages;
                this.currentPage = 0;
                document.querySelector(".active").classList.remove("active");
                document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
            });
        },
        sortPharmacies(event) {
            let sortOrder = event.target.value.split("-");
            this.searchParameters.sortBy = sortOrder[0];
            this.searchParameters.orderBy = sortOrder[1];
            this.searchByCriteria();
        },
        updatePage(pageNumber) {
            this.currentPage = pageNumber;
            document.querySelector(".active").classList.remove("active");
            document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
            PharmacyService.updatePage(this.currentPage, this.searchParameters)
            .then((response) => {
                this.pharmacies = response.data.pharmacies;
                this.totalPages = response.data.totalPages == 0 ? 1 : response.data.totalPages;
                if (this.pharmacies.length == 0 && this.currentPage > 0)
                    this.updatePage(this.currentPage - 1);
            });
        },
        getAverageRating(pharmacy) {
            RatingService.getAverageRatingForPharmacy(pharmacy.id)
            .then(response => { pharmacy.rating = response.data; })
        },
        showPreviousLink() {
            return this.currentPage == 0 ? false : true;
        },
        showNextLink() {
            return this.currentPage == this.totalPages-1 ? false : true;
        },
        addPharmacy(event) {
            if(this.validate()) {
              let time = this.input.startWorkTime.split(":");
              let time2 = this.input.endWorkTime.split(":");
              this.input.startWorkTime = time[0] * 60 + time[1];
              this.input.endWorkTime = time2[0] * 60 + time2[1];
              PharmacyService.addPharmacy(this.input);
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
            this.invalidCounselingPrice = false;
            this.invalidStartWorkTime = false;
            this.invalidEndWorkTime = false;

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

            if (this.input.counselingPrice === undefined || this.input.counselingPrice === ""
                || !this.input.counselingPrice.match('^[0-9]*$')) {
              this.invalidCounselingPrice = true;
            }

            if (this.input.startWorkTime === undefined || this.input.startWorkTime === "") {
              this.invalidStartWorkTime = true;
            }

          if (this.input.endWorkTime === undefined || this.input.endWorkTime === "") {
            this.invalidEndWorkTime = true;
          }

            return !this.invalidName && !this.invalidAddressCity && !this.invalidAddressName && !this.invalidAddressNumber
                && !this.invalidAddressPostalCode && !this.invalidDermatologistExamination &&
                !this.invalidPharmacistCounseling && !this.invalidCounselingPrice && !this.invalidStartWorkTime
                && !this.invalidEndWorkTime;
        }
    }
}
</script>

<style scoped>
.add {
    text-align: right;
    margin-bottom: 2%;
}

.routerLink {
    text-decoration: none;
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
/* Styles for wrapping the search box */
.main {
    width: 50%;
    margin: 50px auto;
}
.sort-filter {
    text-align: left;
}
.el1 {
    margin-left: 10px;
}

</style>