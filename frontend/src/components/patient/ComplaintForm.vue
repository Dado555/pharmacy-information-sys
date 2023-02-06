<template>
<form id="stepper" @submit.prevent="makeComplaint">
    <nav>
        <div class="nav nav-pills nav-fill" id="nav-tab">
            <a class="nav-link active" id="step1-tab" data-toggle="tab">Choose entity</a>
            <a class="nav-link" id="step2-tab" data-toggle="tab">Choose one</a>
            <a class="nav-link" id="step3-tab" data-toggle="tab">Write complaint</a>
        </div>
    </nav><br>

    <div class="tab-content" id="nav-tabContent">

        <div class="tab-pane fade show active" id="step1">
            <div class="table-responsive">
                <table class="table user-list">
                    <thead>
                        <tr>
                        <th></th>
                        <th>Entity</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td>
                            <input v-model="choosedEntity" :value="'pharmaciyId'" class="form-check-input" type="radio" name="pharmacyId" id="pharmacyId" checked>
                            <label class="form-check-label" for="pharmacyId"></label>
                        </td><td>Pharmacies</td></tr>
                        
                        <tr><td>
                            <input v-model="choosedEntity" :value="'pharmacistId'" class="form-check-input" type="radio" name="pharmacistId" id="pharmacistId">
                            <label class="form-check-label" for="pharmacistId"></label>
                        </td><td>Pharmacists</td></tr>
                        <tr><td>
                            <input v-model="choosedEntity" :value="'dermatologistId'" class="form-check-input" type="radio" name="dermatologistId" id="dermatologistId">
                            <label class="form-check-label" for="dermatologistId"></label>
                        </td><td>Dermatologists</td></tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="tab-pane fade" id="step2">
            <div class="table-responsive" id="pharmacy-table" style="display: none;">
                <table class="table user-list">
                    <thead>
                        <tr>
                        <th><span></span></th>
                        <th><span>Name</span></th>
                        <th><span>Address</span></th>
                        <th><span>Rating</span></th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="pharmacy in pharmacies" :key="pharmacy.id">
                        <td>
                        <input v-model="pharmacyId" :value="pharmacy.id" class="form-check-input" type="radio" name="pharmacy">
                        <label class="form-check-label" for="pharmacy">
                        </label></td>
                        <td>{{ pharmacy.name }}</td>
                        <td>{{ pharmacy.address.name }} {{ pharmacy.address.number }}, {{pharmacy.address.city}} {{pharmacy.address.postalCode}}</td>
                        <td>{{ pharmacy.rating }}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="table-responsive" id="pharmacist-table" style="display: none;">
                <table class="table user-list">
                    <thead>
                        <tr>
                            <th></th>
                            <th><span>First Name</span></th>
                            <th><span>Last Name</span></th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="pharmacist in pharmacists" :key="pharmacist.id">
                        <td>
                            <input v-model="pharmacistId" :value="pharmacist.id" class="form-check-input" type="radio" name="pharmacist">
                            <label class="form-check-label" for="pharmacist">
                            </label>
                        </td>
                        <td>{{ pharmacist.firstName }}</td>
                        <td>{{ pharmacist.lastName }}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="table-responsive" id="dermatologist-table" style="display: none;">
                <table class="table user-list">
                    <thead>
                        <tr>
                            <th><span></span></th>
                            <th><span>First Name</span></th>
                            <th><span>Last Name</span></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="dermatologist in dermatologists" :key="dermatologist.id">
                            <td>
                                <input v-model="dermatologistId" :value="dermatologist.id" class="form-check-input" type="radio" name="dermatologist">
                                <label class="form-check-label" for="dermatologist">
                                </label>
                            </td>
                            <td>{{ dermatologist.firstName }}</td>
                            <td>{{ dermatologist.lastName }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

        </div>

        <div class="tab-pane fade show" id="step3">
            <div class="form-group">
                <label class="col-form-label">Complaint Message:</label>
                <input type="text" class="form-control" v-model="complaintMessage">
            </div>
        </div>

    </div>
    <br>
    <div class="modal-footer">
        <button v-if="showPreviousLink()" @click="updateCurrentStep(currentStep - 1)" type="button" class="btn btn-secondary" data-enchanter="previous">Previous</button>
        <button v-if="showNextLink()" @click="updateCurrentStep(currentStep + 1)" type="button" class="btn btn-primary" data-enchanter="next" id="next">Next</button>
        <button v-if="currentStep === totalSteps" type="submit" class="btn btn-primary" data-enchanter="finish">Finish</button>
    </div>
</form>
</template>

<script>
import PatientService from '@/service/PatientService.js';
import PharmacyService from '@/service/PharmacyService.js';
import PharmacistService from '@/service/PharmacistService.js';
import DermatologistService from '@/service/DermatologistService.js';

export default {
    name: "ComplaintForm",
    computed: {
        user() {
            return this.$store.getters.getUser;
        },
        complaintDTO() {
            return {
                complaintMessage: this.complaintMessage,
                patientId: this.user.id,
                entityId: this[this.choosedEntity],
            }
        },
    },
        data() {
            return {
                totalSteps: 3,
                currentStep: 1,

                choosedEntity: "pharmacyId",
                pharmacies: [],
                pharmacists: [],
                dermatologists: [],
                complaintMessage: "",

                pharmacyId: null,
                pharmacistId: null,
                dermatologistId: null
            };
        },
    methods: {  
        makeComplaint() {
            PatientService.addComplaint(this.user.id, this.complaintDTO)
            .then(response => { this.toast("Complaint is created successfully!", "info"); this.$emit('loadComplaints'); })
            
            let tab = document.getElementById('step' + this.currentStep + '-tab');
            tab.classList.remove("active");
            let el = document.getElementById('step' + this.currentStep);
            el.classList.remove("show")
            el.classList.remove('active');

            this.currentStep = 1;
            let newTab = document.getElementById('step' + this.currentStep + '-tab');
            newTab.classList.add("active");
            let newEl = document.getElementById('step' + this.currentStep);
            newEl.classList.add("show");
            newEl.classList.add('active');
        },
        updateCurrentStep(step) {

            if (step === 2) {
                if (this.choosedEntity === "pharmacyId") {
                    this.loadPharmacies();
                    let el =  document.getElementById("pharmacy-table")
                    el.style.display = "block";
                    el =  document.getElementById("pharmacist-table")
                    el.style.display = "none";
                    el =  document.getElementById("dermatologist-table")
                    el.style.display = "none";
                }
                else if (this.choosedEntity === "pharmacistId") {
                    this.loadPharmacists();
                    let el =  document.getElementById("pharmacist-table")
                    el.style.display = "block";
                    el =  document.getElementById("pharmacy-table")
                    el.style.display = "none";
                    el =  document.getElementById("dermatologist-table")
                    el.style.display = "none";
                }
                else if (this.choosedEntity === "dermatologistId") {
                    this.loadDermatologists();
                    let el =  document.getElementById("dermatologist-table")
                    el.style.display = "block";
                    el =  document.getElementById("pharmacist-table")
                    el.style.display = "none";
                    el =  document.getElementById("pharmacy-table")
                    el.style.display = "none";
                }
            }
            let tab = document.getElementById('step' + this.currentStep + '-tab');
            tab.classList.remove("active");
            let el = document.getElementById('step' + this.currentStep);
            el.classList.remove("show")
            el.classList.remove('active');

            this.currentStep = step;
            let newTab = document.getElementById('step' + this.currentStep + '-tab');
            newTab.classList.add("active");
            let newEl = document.getElementById('step' + this.currentStep);
            newEl.classList.add("show");
            newEl.classList.add('active');
        },
        loadPharmacies() {
            PharmacyService.findAll()
            .then((response) => { this.pharmacies = response.data.pharmacies; });
        },
        loadPharmacists() {
            PharmacistService.findAll()
            .then((response) => { this.pharmacists = response.data; })
            .catch((error) => { this.toast(error.response.data.message, 'error'); });
        },
        loadDermatologists() {
            DermatologistService.findAll()
            .then((response) => { this.dermatologists = response.data; })
            .catch((error) => { this.toast(error.response.data.message, 'error'); });
        },
        showPreviousLink() {
            return this.currentStep == 1 ? false : true;
        },
        showNextLink() {
            return this.currentStep == this.totalSteps ? false : true;
        },
        toast(message, type) {
		    this.$toast.show(message, {
                type: type,
                position: 'top',
                duration: 2000
		    });
	    }
    }
};
</script>

<style scoped>

.left {
    display: inline-block;
}

img {
  position: relative;
  max-width: 50px;
  float: left;
}

</style>