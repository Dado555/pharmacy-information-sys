<template>
<div id="rate-stars">
    <hr>
    <h5 class="mt-2 el1">Rate: </h5>
    <fieldset class="rating el2"> 
        <input @click="rate" type="radio" :id="'star5' + id" name="rating" value="5" />
        <label class="full" :for="'star5' + id" title="Awesome - 5 stars"></label> 
        <input @click="rate" type="radio" :id="'star4' + id" name="rating" value="4" />
        <label class="full" :for="'star4' + id" title="Pretty good - 4 stars"></label> 
        <input @click="rate" type="radio" :id="'star3' + id" name="rating" value="3" />
        <label class="full" :for="'star3' + id" title="Meh - 3 stars"></label> 
        <input @click="rate" type="radio" :id="'star2' + id" name="rating" value="2" />
        <label class="full" :for="'star2' + id" title="Kinda bad - 2 stars"></label> 
        <input @click="rate" type="radio" :id="'star1' + id" name="rating" value="1" />
        <label class="full" :for="'star1' + id" title="Sucks big time - 1 star"></label> 
        <input @click="rate" type="radio" class="reset-option" name="rating" value="reset" /> 
    </fieldset>
    <p :id="'ajdi' + id">{{ userRating }}</p>
</div>
</template>

<script>
import RatingService from '@/service/RatingService.js';

export default {
    name: 'RatingStars',
    props: [ 'id', 'rating', 'entity', 'patientRating'],
    data() {
        return {
            userRating: ""
        }
    },
    mounted() {
        let el = document.getElementById('ajdi' + this.id);
        el.style.fontSize = '25px';
        el.style.color = 'green';
        el.style.float = 'left';
        el.style.marginRight = '10px';

        if (this.patientRating)
            this.changeRating(this.patientRating);
    },
    methods: {
        rate() {
            var sim = document.querySelector("input[type='radio']:checked").value;
            if (this.entity === 'pharmacy') {
                RatingService.addPharmacyRating(this.id, sim)
                .then(response => { this.$emit("reload"); this.changeRating(sim); })
                .catch(error => { this.toast(error.response.data.message); });
            }
            if (this.entity === 'medicine') {
                RatingService.addMedicineRating(this.id, sim)
                .then(response => { this.$emit("reload"); this.changeRating(sim); })
                .catch(error => { this.toast(error.response.data.message); });
            }
            if (this.entity === 'medical-worker') {
                RatingService.addMedicalWorkerRating(this.id, sim)
                .then(response => { this.$emit("reload"); this.changeRating(sim); })
                .catch(error => { this.toast(error.response.data.message); });
            }
        },
        changeRating(sim) {
            let el = document.getElementById('ajdi' + this.id);
            this.userRating = sim;
            if (sim < 3)
                el.style.color = 'red'; 
            else
                el.style.color = 'green'; 
        },
        toast(message) {
            this.$toast.show(message, {
                type: 'error',
                position: 'top',
                duration: 2000
            })
      }
    }
}
</script>

<style scoped>
@import url(https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css);
@import url('https://fonts.googleapis.com/css?family=Source+Sans+Pro');
.rate-stars body {
    background-color: #D32F2F;
    font-family: 'Source Sans Pro', sans-serif !important
}
.el1 {
    float: left;
    margin-right: 15px;
    margin-top: 10px;
}
.el2 {
    float: left;
    margin-top: 5px;
}
fieldset,
.rate-stars label {
    margin: 0;
    padding: 0
}

.rate-stars body {
    margin: 20px
}
.rating {
    border: none;
    margin-right: 49px
}
.rating>[id^="star"] {
    display: none
}
.rating>label:before {
    margin: 5px;
    font-size: 1em;
    font-family: FontAwesome;
    display: inline-block;
    content: "\f005"
}
.rating>.half:before {
    content: "\f089";
    position: absolute
}
.rating>label {
    color: #ddd;
    float: right
}
.rating>[id^="star"]:checked~label,
.rating:not(:checked)>label:hover,
.rating:not(:checked)>label:hover~label {
    color: #FFD700
}
.rating>[id^="star"]:checked+label:hover,
.rating>[id^="star"]:checked~label:hover,
.rating>label:hover~[id^="star"]:checked~label,
.rating>[id^="star"]:checked~label:hover~label {
    color: #FFED85
}
.reset-option {
    display: none
}
.reset-button {
    margin: 6px 12px;
    background-color: rgb(255, 255, 255);
    text-transform: uppercase
}
.rate-stars p {
    font-size: 14px
}
.rate-stars h4 {
    margin-top: 18px
}
</style>