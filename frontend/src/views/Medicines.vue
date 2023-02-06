<template>
    <Navbar></Navbar>

    <div class="container">

        <div class="ui-button add" v-if="user.role == 'SYSTEM_ADMIN'">
          <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#AddMedicineModal">Add medicine</button>
        </div>

      <div class="modal fade" id="AddMedicineModal" tabindex="-1" aria-labelledby="AddMedicineModal" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="AddMedicineModal">Add new medicine</h5>
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
                    <input type="text" class="form-control" v-model="input.type"
                           v-bind:class="{'is-invalid': invalidType}" placeholder="Type">
                    <div class="invalid-feedback">
                      Invalid type.
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.medicineForm"
                           v-bind:class="{'is-invalid': invalidMedicineForm}" placeholder="Medicine form">
                    <div class="invalid-feedback">
                      Invalid medicine form [POWDER, CAPSULE, PILL, UNGUENT, GEL, SYRUP].
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.structure"
                           v-bind:class="{'is-invalid': invalidStructure}" placeholder="Structure">
                    <div class="invalid-feedback">
                      Invalid structure.
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.manufacture"
                           v-bind:class="{'is-invalid': invalidManufacture}" placeholder="Manufacture">
                    <div class="invalid-feedback">
                      Invalid manufacture.
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.medicineIssuingType"
                           v-bind:class="{'is-invalid': invalidMedicineIssuingType}" placeholder="Medicine issuing type" />
                    <div class="invalid-feedback">
                      Invalid medicine issuing type [WITH_PRESCRIPTION, WITHOUT_PRESCRIPTION].
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.dailyIntake"
                           v-bind:class="{'is-invalid': invalidDailyIntake}" placeholder="Daily intake" />
                    <div class="invalid-feedback">
                      Invalid daily intake.
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.contraindications"
                           v-bind:class="{'is-invalid': invalidContraindications}" placeholder="Contraindications" />
                    <div class="invalid-feedback">
                      Invalid contraindications.
                    </div>
                  </div>
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="input.points"
                           v-bind:class="{'is-invalid': invalidPoints}" placeholder="Points" />
                    <div class="invalid-feedback">
                      Invalid points.
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-primary" v-on:click="addMedicine">ADD</button>
              </div>
            </div>
          </div>
        </div>
      </div>

        <form @submit.prevent=searchByCriteria>
            <div class="search">
                <div class="input-group">
                    <input v-model="searchParameters.name" type="text" class="form-control" placeholder="Name: ">
                    <input v-model="searchParameters.type" type="text" class="form-control" placeholder="Type: ">
                    <input v-model="searchParameters.ratingFrom" type="number" class="form-control" placeholder="Rating From: ">
                    <input v-model="searchParameters.ratingTo" type="number" class="form-control" placeholder="Rating To: ">

                    <div class="input-group-append">
                        <button class="btn btn-primary">Search</button>
                    </div>
                </div>
            </div>
        </form><br><br>

        <div class="sort-filter">
            <h3>Sort by: </h3>
            <select class="search-select" @change=sortMedicines($event)>
                <option disabled value="">Select one</option>
                <option value="name-ascending">Name  &#8593</option>
                <option value="name-descending">Name  &#8595</option>
                <option value="rating-ascending">Rating  &#8593</option>
                <option value="rating-descending">Rating  &#8595</option>
            </select>
        </div>

        <!-- <div class="sort-filter">
          <h3>Filter by: </h3>
          <select class="search-select" @change=filterMedicines($event)>
            <option value="">Select one</option>
            <option value="sedativ"> sedativ </option>
            <option value="analgetik"> analgetik </option>

          </select>
        </div><br> -->

        <div class="row card-group" v-for="n in Math.ceil(medicines.length/4)" :key="n.id">
            <div class="col-md-3 p-3" v-for="medicine in medicines.slice((n-1)*4, n*4)" :key="medicine.id">
                <div class="card">
                    <a><router-link :to="{ path: 'medicine', query: { id : medicine.id}}">
                    <img class="card-img-top" src="../../public/medicine.png" alt="Card image cap">
                    <div class="card-header"><b>{{ medicine.name }}</b></div>
                    </router-link></a>
                    <div class="card-body">
                        <p>Type: {{ medicine.type }}</p>
                        <p>Rating: {{ medicine.rating }}</p>
                        <RatingStars  v-if="medicine.rateable" :id="medicine.id" :rating="medicine.rating" :entity="'medicine'" :patientRating="medicine.patientRating" @reload="getAverageRating(medicine)"/>
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
import MedicineService from '@/service/MedicineService.js';
import RatingService from '@/service/RatingService.js';

import Navbar from '@/components/Navbar.vue';
import RatingStars from '@/components/RatingStars.vue';
import PatientService from "@/service/PatientService";

export default {
    name: "Medicines",
    components: { Navbar, RatingStars },
    data() {
        return {
            medicines: [],
            searchParameters: { 
                name: "", 
                type: "", 
                ratingFrom: "",
                ratingTo: "",
                sortBy: "", 
                orderBy: "",
                filterBy: ""
            },
            currentPage: 0,
            pageSize: 8,
            totalPages: 1,
          input: {
            name: "",
            type: "",
            medicineForm: "",
            structure: "",
            manufacture: "",
            medicineIssuingType: "",
            dailyIntake: "",
            contraindications: "",
            points: ""
          },
          invalidName: false,
          invalidType: false,
          invalidMedicineForm: false,
          invalidStructure: false,
          invalidManufacture: false,
          invalidMedicineIssuingType: false,
          invalidDailyIntake: false,
          invalidContraindications: false,
          invalidPoints: false
        }
    },
    mounted() {
        this.loadMedicines();
    },
    computed: {
    user() {
      return this.$store.getters.getUser;
      }
    },
    methods: {
        loadMedicines() {
            let data = this;
            data.currentPage = 0;
            MedicineService.findAll()
            .then((response) => {
                data.medicines = response.data.medicines;
                data.totalPages = response.data.totalPages == 0 ? 1 : response.data.totalPages;
                document.querySelector(".active")?.classList?.remove("active");
                document.getElementById((data.currentPage+1).toString()).classList.toggle("active");
            });
        },
        searchByCriteria() {
            if (this.searchParameters.ratingFrom === "")
                this.searchParameters.ratingFrom = 0;
            if (this.searchParameters.ratingTo === "")
                this.searchParameters.ratingTo = 5;
            MedicineService.searchMedicines(this.searchParameters)
            .then((response) => {
                this.medicines = response.data.medicines;
                this.totalPages = response.data.totalPages == 0 ? 1 : response.data.totalPages;
                this.currentPage = 0;
                document.querySelector(".active").classList.remove("active");
                document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
            });
        },
        sortMedicines(event) {
            let sortOrder = event.target.value.split("-");
            this.searchParameters.sortBy = sortOrder[0];
            this.searchParameters.orderBy = sortOrder[1];
            this.searchByCriteria();
        },
        filterMedicines(event) {
          if (this.searchParameters.ratingFrom === "")
            this.searchParameters.ratingFrom = 0;
          if (this.searchParameters.ratingTo === "")
            this.searchParameters.ratingTo = 5;

          this.searchParameters.filterBy = event.target.value;
          this.searchByCriteria();
        },
        updatePage(pageNumber) {
            if (this.searchParameters.ratingFrom === "")
                this.searchParameters.ratingFrom = 0;
            if (this.searchParameters.ratingTo === "")
                this.searchParameters.ratingTo = 5;

            this.currentPage = pageNumber;
            document.querySelector(".active").classList.remove("active");
            document.getElementById((this.currentPage+1).toString()).classList.toggle("active");
            MedicineService.updatePage(this.currentPage, this.searchParameters)
            .then((response) => {
                this.medicines = response.data.medicines;
                this.totalPages = response.data.totalPages == 0 ? 1 : response.data.totalPages;
                if (this.medicines.length == 0 && this.currentPage > 0)
                    this.updatePage(this.currentPage - 1);
            });
        },
        getAverageRating(medicine) {
            RatingService.getAverageRatingForMedicine(medicine.id)
            .then(response => { medicine.rating = response.data; })
        },
        showPreviousLink() {
            return this.currentPage == 0 ? false : true;
        },
        showNextLink() {
            return this.currentPage == this.totalPages-1 ? false : true;
        },
      addMedicine(event) {
        if (this.validate()) {
          MedicineService.addMedicine(this.input);
          this.$toast.show('Medicine successfully added!', {
            type: 'info',
            position: 'top',
            duration: 2000
          });
        }
      },
      validate() {
        this.invalidName = false;
        this.invalidType = false;
        this.invalidMedicineForm = false;
        this.invalidStructure = false;
        this.invalidManufacture = false;
        this.invalidMedicineIssuingType = false;
        this.invalidDailyIntake = false;
        this.invalidContraindications = false;
        this.invalidPoints = false;

        if (this.input.name === "") {
          this.invalidName = true;
        }
        if (this.input.type === "") {
          this.invalidType = true;
        }
        if (this.input.medicineForm === "" || this.input.medicineForm !== "POWDER" && this.input.medicineForm !== "PILL"
            && this.input.medicineForm !== "CAPSULE" && this.input.medicineForm !== "UNGUENT" && this.input.medicineForm !== "GEL"
            && this.input.medicineForm !== "SYRUP") {
          this.invalidMedicineForm = true;
        }
        if (this.input.structure === "") {
          this.invalidStructure = true;
        }
        if (this.input.manufacture === "") {
          this.invalidManufacture = true;
        }
        if (this.input.medicineIssuingType === "" || this.input.medicineIssuingType !== "WITH_PRESCRIPTION"
            && this.input.medicineIssuingType !== "WITHOUT_PRESCRIPTION") {
          this.invalidMedicineIssuingType = true;
        }
        if (this.input.dailyIntake === "" || !this.input.dailyIntake.match('^[0-9]*$')) {
          this.invalidDailyIntake = true;
        }
        if (this.input.contraindications === "") {
          this.invalidContraindications = true;
        }
        if (this.input.points === "" || !this.input.points.match('^[0-9]*$')) {
          this.invalidPoints = true;
        }

        return !this.invalidName && !this.invalidType && !this.invalidMedicineForm && !this.invalidStructure
            && !this.invalidManufacture && !this.invalidMedicineIssuingType && !this.invalidDailyIntake
            && !this.invalidContraindications && !this.invalidPoints;
      }
    }
}
</script>

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

img {
    padding: 1rem;
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


</style>