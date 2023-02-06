<template>
  <popup-modal ref="popup">
    <div class="container">
      <div class="row">
        <h3 style="margin-top: 0" class="col-md-11">{{ medicine.medicineDTO.name }} promotion</h3>
        <button type="button" style="color: white; border-style: hidden; height: 99%" class="close col-md-1" @click="close">&times;</button>
      </div>
      <br>
      <div class="row">
        <div class="col-md-12">
          <div class="my-form">
            <div class="form-group">
              <label for="medicine-price">Promotional price</label>
              <input type="number" step="0.01" class="form-control" id="medicine-price" v-model="newPrice">
              <br>
            </div>
            <div class="form-group">
              <label for="until-datetime">Worth until (date-time)</label>
              <input type="datetime-local" class="form-control" id="until-datetime" v-model="until">
              <br><br>
            </div>
          </div>
        </div>
        <div class="col">
          <button class="btn btn-default col-md-4" @click="newPromotion()">OK</button>
          <button class="btn btn-default col-md-3" @click="close">Cancel</button>
          <button class="btn btn-default col-md-4" @click="cancelPromotion">Delete</button>
        </div>
      </div>
    </div>
  </popup-modal>
</template>

<script>
import PopupModal from "./PopupModal";
import MedicineService from "../../service/MedicineService";
export default {
  name: "PromoteMedicine",
  props: ['medicine', 'untildt', 'pharmacyId'],
  emits: ['close'],
  components: {PopupModal},
  data() {
    return {
      medicineEdit: this.medicine,
      newPrice: this.medicine.actionPrice,
      until: this.untildt
    }
  },
  mounted() {
    this.show();
  },
  methods: {
    show() {
      this.$refs.popup.open();
    },
    close() {
      this.$emit('close');
      this.$refs.popup.close();
    },
    newPromotion() {
      console.log(this.until);
      let date = null;
      if(isNaN(this.until))
        date = new Date(this.until);
      else
        date = new Date(parseInt(this.until));
      this.medicineEdit.actionPrice = parseFloat(this.newPrice);
      this.medicineEdit.untilDateTime = date.getTime();
      console.log(this.medicineEdit.untilDateTime);
      let message = 'Promotion for \''+ this.medicineEdit.medicineDTO.name + '\' successfully made!';
      this.makePromotion(message);
      this.$refs.popup.close();
    },
    makePromotion(message) {
      MedicineService.makeMedicinePromotion(this.pharmacyId, this.medicineEdit)
        .then((response) => {
          this.$emit('close');
          this.$toast.show(message, {
            type: 'info',
            position: 'top',
            duration: 5000
          });
        });
    },
    cancelPromotion() {
      this.medicineEdit.actionPrice = null;
      this.medicineEdit.untilDateTime = null;
      let message = 'Promotion for \''+ this.medicineEdit.medicineDTO.name + '\' successfully canceled!';
      this.makePromotion(message);
      this.$refs.popup.close();
    }
  }
}
</script>

<style scoped>
.my-form {
  color: #305896;
}
.btn-default, .row .close {
  background-color: #305896;
  color: #fff;
  border-radius: 0;
}
.btn-default:hover, .row .close:hover {
  background-color: #4498C6;
  color: #fff;
}
.my-form .form-control {
  border-radius: 0;
}
.btn-default {
  margin-left: 10px;
}
</style>