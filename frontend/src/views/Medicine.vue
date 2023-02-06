<template>
<Navbar />
<div v-if="pharmacyMedicine != null" class="card mb-3 center" style="max-width: 60rem;">
    <div class="row g-0">
        <div class="col-md-4">
            <img src="../../public/medicine.png"  alt="...">
        </div>
        <div class="col-md-8">
            <div class="card-body">
                <h2><b>{{ pharmacyMedicine.medicineDTO.name }}</b></h2>
                <p class="text-secondary mb-1"><b>Type: </b>: {{ pharmacyMedicine.medicineDTO.type }}</p>
                <p class="text-secondary mb-1"><b>Form: </b>{{ pharmacyMedicine.medicineDTO.medicineForm }}</p>
                <p class="text-secondary mb-1"><b>Structure: </b>{{ pharmacyMedicine.medicineDTO.structure }}</p>        
                <p class="text-secondary mb-1"><b>Manufacture: </b>{{ pharmacyMedicine.medicineDTO.manufacture }}</p>        
                <p class="text-secondary mb-1"><b>Medicine Issuing Type: </b>{{ pharmacyMedicine.medicineDTO.medicineIssuingType }}</p>        
                <p class="text-secondary mb-1"><b>Daily Intake: </b>{{ pharmacyMedicine.medicineDTO.dailyIntake }}</p>        
                <p class="text-secondary mb-1"><b>Contraindications: </b>{{ pharmacyMedicine.medicineDTO.contraindications }}</p>    
                <p class="text-secondary mb-1"><b>Average Rating: </b>{{ pharmacyMedicine.medicineDTO.averageRating }}</p>    
                <p class="text-secondary mb-1"><b>Available amount: </b>{{ pharmacyMedicine.availableAmount }}</p>    
                <hr>
                <h5 class="text-secondary mb-1"><b>Price: </b>{{ pharmacyMedicine.price }}$</h5>    
                <div class="left">
                <input class="form-control" :id="pharmacyMedicine.id" type="number" min="1" :max="pharmacyMedicine.availableAmount" placeholder="1" value="1">
                </div>
                <div class="left input-group-append">
                    <button class="btn btn-secondary" @click="addToCart(pharmacyMedicine)">Add to Cart</button>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import PharmacyMedicineService from '@/service/PharmacyMedicineService'
import Navbar from "@/components/Navbar";

export default {
    name: "Medicine",
    components: { Navbar },
    data() {
        return {
            pharmacyMedicine: null
        }
    },
    mounted() {
        let data = this;
        PharmacyMedicineService.findMedicinesFromPharmacy(this.$route.query.id)
        .then(response => { data.pharmacyMedicine = response.data; });
    },
    methods: {
        addToCart(pharmacyMedicineDTO) {
            let amount = document.getElementById(pharmacyMedicineDTO.id).value;
            this.$store.dispatch('addItemToCart', { pharmacyMedicineDTO, amount });
            this.toast('Item added!');
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
    margin-bottom: 3rem;
}
.left {
    float: left;
    margin-right: 1rem;
}
</style>