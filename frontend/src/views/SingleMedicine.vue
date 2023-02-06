<template>
<Navbar />
<div v-if="medicine != null" class="card mb-3 center" style="max-width: 60rem;">
    <div class="row g-0">
        <div class="col-md-4">
          <img src="../../public/medicine.png"/>
        </div>

        <div class="col-md-8">
            <div class="card-body">
                <h2><b>{{ medicine.name }}</b></h2>
                <p class="text-secondary mb-1"><b>Type: </b>: {{ medicine.type }}</p>
                <p class="text-secondary mb-1"><b>Form: </b>{{ medicine.medicineForm }}</p>
                <p class="text-secondary mb-1"><b>Structure: </b>{{ medicine.structure }}</p>
                <p class="text-secondary mb-1"><b>Manufacture: </b>{{ medicine.manufacture }}</p>
                <p class="text-secondary mb-1"><b>Medicine Issuing Type: </b>{{ medicine.medicineIssuingType }}</p>
                <p class="text-secondary mb-1"><b>Daily Intake: </b>{{ medicine.dailyIntake }}</p>
                <p class="text-secondary mb-1"><b>Contraindications: </b>{{ medicine.contraindications }}</p>
                <p class="text-secondary mb-1"><b>Average Rating: </b>{{ medicine.rating }}</p>
                <hr>
                <div class="left input-group-append">
                    <button class="btn btn-secondary" data-bs-target="#availability-modal" data-bs-toggle="modal" v-on:click="loadPharmacyMedicines(medicine.id)">Availability</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal" id="availability-modal" v-if="medicine != null">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"> {{ medicine.name }} in pharmacies</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" id="close"></button>
                </div>
                <div class="table-responsive">
                    <table class="table user-list">
                        <thead>
                            <tr>
                                <th><span>Pharmacy</span></th>
                                <th><span>Available amount</span></th>
                                <th><span>Price</span></th>
                                <th><span>Amount</span></th>
                                <th><span></span></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="pharmacyMedicine in pharmacyMedicines" :key="pharmacyMedicine.id">
                                <td><span>{{ pharmacyMedicine.pharmacyDTO.name }}</span></td>
                                <td><span>{{ pharmacyMedicine.availableAmount }}</span></td>
                                <td><span>{{ pharmacyMedicine.price }}</span></td>
                                <td v-if="role==='PATIENT'"><span><input :id="pharmacyMedicine.id" type="number" min="1" :max="pharmacyMedicine.availableAmount" placeholder="1" value="1"></span></td>
                                <td v-if="role==='PATIENT'"><span><button class="btn btn-secondary" @click="addToCart(pharmacyMedicine)">Add to Cart</button></span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import {mapState} from "vuex";

import PharmacyMedicineService from '@/service/PharmacyMedicineService'
import Navbar from "@/components/Navbar";
import MedicineService from "@/service/MedicineService";

export default {
    name: "SingleMedicine",
    components: { Navbar },
    data() {
        return {
            medicine: null,
            pharmacyMedicines: []
        }
    },
    computed: {
        ...mapState({
            role: state => state.user.role,
        })
    },
    mounted() {
        let data = this;
        MedicineService.findOne(this.$route.query.id)
            .then(response => { data.medicine = response.data; });
    },
    methods: {
        loadPharmacyMedicines(medicineId) {
        let data = this;
        PharmacyMedicineService.findPharmaciesForMedicine(medicineId)
            .then(response => { data.pharmacyMedicines = response.data; });
        },
        addToCart(pharmacyMedicineDTO) {
            let amount = document.getElementById(pharmacyMedicineDTO.id).value;
            this.$store.dispatch('addItemToCart', { pharmacyMedicineDTO, amount });
            this.toast('Medicine added to cart');
        },
        toast(message) {
            this.$toast.show(message, {
                type: 'info',
                position: 'top',
                duration: 2000
            });
        }
    }
}


</script>

<style scoped>
.center {
    margin: auto;
    margin-top: 5rem;
}
img {
    width: 18rem;
    margin-top: 20px;
}
.card-body {
    text-align: left;
    margin-bottom: 1rem;
}
.modal-dialog {
    max-width: 40rem;
}

</style>