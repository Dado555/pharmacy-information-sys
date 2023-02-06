<template>
<form id="stepper" @submit.prevent="makeAppointmentCounseling">
    <nav>
        <div class="nav nav-pills nav-fill" id="nav-tab">
            <a class="nav-link active" id="step1-tab" data-toggle="tab">Date & Time</a>
            <a class="nav-link" id="step2-tab" data-toggle="tab">Choose Phamracy</a>
            <a class="nav-link" id="step3-tab" data-toggle="tab">Choose Pharmacist</a>
        </div>
    </nav><br>

    <div class="tab-content" id="nav-tabContent">

        <div class="tab-pane fade show active" id="step1">
            <div class="container col-md-10 d-grid gap-2">
                <div class="form-floating">
                    <input v-model="date" class="form-control" type="text" onfocus="(this.type='date')" placeholder="Date:" aria-label="default input example">
                    <label for="floatingInput">Date:</label>
                </div>
                <div class="form-floating">
                    <input v-model="time" class="form-control" type="text" onfocus="(this.type='time')" placeholder="Time:" aria-label="default input example">
                    <label for="floatingInput">Time:</label>
                </div><br>
                <div class="form-floating">
                    <input v-model="duration" class="form-control" type="number" placeholder="Duration in minutes:" aria-label="default input example">
                    <label for="floatingInput">Duration in minutes:</label>
                </div><br>
            </div>
        </div>

        <div class="tab-pane fade" id="step2">
            <div class="d-flex justify-content-between first">
                <div class="sort">
                    <h3>Sort by: </h3>
                    <select class="search-select" @change=sortPharmacies($event)>
                    <option value="">Select one</option>
                    <option value="rating-ascending">Rating  &#8593</option>
                    <option value="rating-descending">Rating  &#8595</option>
                    <option value="price-ascending">Appointment Price  &#8593</option>
                    <option value="price-descending">Appointment Price  &#8595</option>
                    </select>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table user-list">
                    <thead>
                        <tr>
                        <th><span></span></th>
                        <th><span>Name</span></th>
                        <th><span>Location</span></th>
                        <th><span>Price</span></th>
                        <th><span>Rating</span></th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="pharmacy in pharmacies" :key="pharmacy.id">
                        <td>
                            <input v-model="pharmacyId" :value="pharmacy.id" class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" checked>
                            <label class="form-check-label" for="flexRadioDefault2">
                            </label>
                        </td>
                        <td>{{ pharmacy.name }}</td>
                        <td>{{ pharmacy.address.city }}</td>
                        <td>{{ pharmacy.counselingPrice }}</td>
                        <td>{{ pharmacy.rating }}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="tab-pane fade" id="step3">
            <div class="d-flex justify-content-between first">
                <div class="sort">
                    <h3>Sort by: </h3>
                    <select class="search-select" @change=sortPharmacists($event)>
                    <option value="">Select one</option>
                    <option value="rating-ascending">Rating  &#8593</option>
                    <option value="rating-descending">Rating  &#8595</option>
                    </select>
                </div>
            </div>
            <div class="table-responsive">
                <table class="table user-list">
                    <thead>
                        <tr>
                        <th><span></span></th>
                        <th><span>First Name</span></th>
                        <th><span>Last Name</span></th>
                        <th><span>Rating</span></th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr v-for="pharmacist in pharmacists" :key="pharmacist.id">
                        <td>
                            <input v-model="pharmacistId" :value="pharmacist.id" class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" checked>
                            <label class="form-check-label" for="flexRadioDefault2">
                            </label>
                        </td>
                        <td>{{ pharmacist.firstName }}</td>
                        <td>{{ pharmacist.lastName }}</td>
                        <td>{{ pharmacist.rating }}</td>
                    </tr>
                    </tbody>
                </table>
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
import AppointmentService from '@/service/AppointmentService.js'
import PharmacyService from '@/service/PharmacyService.js'

import Datepicker from 'vue3-datepicker';

export default {
    name: "Stepper",
    components: { Datepicker },
    computed: {
        user() {
            return this.$store.getters.getUser;
        },
        appontmentDTO() {
            return {
                start: this.getConvertedDateTime(),
                end: this.getConvertedDateTime() + this.duration * 60 * 1000,
                price: 420,
                patientId: this.user.id,
                workerId: this.pharmacistId,
                pharmacyId: this.pharmacyId,
            }

        }

    },
    data() {
        return {
            totalSteps: 3,
            currentStep: 1,

            date: null,
            time: null,
            duration: null,
            pharmacies: [],
            pharmacists: [],

            pharmacyId: null,
            pharmacistId: null
        };
    },
    methods: {
        makeAppointmentCounseling() {
            AppointmentService.addAppointmentCounseling(this.appontmentDTO)
            .then(response => { this.toast('Appointment scheduled successfully! Check mail for confirmation.', 'info'); this.$emit('loadAppointments'); })
            .catch((error) => { this.toast(error.response.data.message, 'error'); });
            
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
            if (step === 2) 
                this.loadPharmacies();
            if (step === 3)
                this.loadPharmacists();

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
            PharmacyService.findAllByDateAndTimeCriteria(this.getConvertedDate(), this.getConvertedTime(), this.getConvertedTime() + this.duration * 60 * 1000)
            .then((response) => { 
                this.pharmacies = response.data;
                if (this.pharmacies.length === 0)
                    this.toast("No available pharmacies for selected date and time", 'error');
            })
            .catch((error) => { this.toast(error.response.data.message, 'error'); });
        },
        loadPharmacists() {
            PharmacyService.findPharmacistsByDateAndTimeCriteria(this.pharmacyId, this.getConvertedDateTime(), this.getConvertedDateTime() + this.duration * 60 * 1000)
            .then((response) => { this.pharmacists = response.data; })
            .catch((error) => { this.toast(error.response.data.message, 'error'); });
        },
        sortPharmacies(event) {
            let sort = event.target.value;
            if (sort === 'rating-ascending')
                this.pharmacies.sort((a, b) => a.rating - b.rating)
            else if (sort === 'rating-descending')
                this.pharmacies.sort((a, b) => b.rating - a.rating)
            else if (sort === 'price-ascending')
                this.pharmacies.sort((a, b) => a.counselingPrice - b.counselingPrice)
            else
                this.pharmacies.sort((a, b) => b.counselingPrice - a.counselingPrice)
        },
        sortPharmacists(event) {
            let sort = event.target.value;
            if (sort === 'rating-ascending')
                this.pharmacists.sort((a, b) => a.rating - b.rating)
            else
                this.pharmacists.sort((a, b) => b.rating - a.rating)
        },
        showPreviousLink() {
            return this.currentStep == 1 ? false : true;
        },
        showNextLink() {
            return this.currentStep == this.totalSteps ? false : true;
        },
        getConvertedDateTime() {
            let date = new Date(this.date).getTime();
            let arr = this.time.split(':');
            let time = (arr[0] * 3600 * 1000) + arr[1] * 60 * 1000 - 2 * 3600 * 1000;
            return date + time;
        },
        getConvertedDate() {
            let date = new Date(this.date).getTime();
            return date;
        },
        getConvertedTime() {
            let arr = this.time.split(':');
            let time = (arr[0] * 3600 * 1000) + arr[1] * 60 * 1000 - 2 * 3600 * 1000;
            return time;
        },
        toast(message, type) {
		    this.$toast.show(message, {
                type: type,
                position: 'top',
                duration: 2500
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