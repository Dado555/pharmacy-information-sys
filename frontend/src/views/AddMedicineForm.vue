<template>
  <Navbar></Navbar>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="header">
        <h1 class="display-5">Add new medicine</h1>
      </div>
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
</template>

<script>
import Navbar from "@/components/Navbar";
import axios from "axios";
export default {
  name: "AddMedicineForm",
  components: {Navbar},
  methods: {
    addMedicine(event) {
      if (this.validate()) {
        axios({
          method: 'POST',
          data: this.input,
          url: 'https://pis-back.herokuapp.com/api/medicines',
        })
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
  },
  data() {
    return {
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
  }
}
</script>

<style scoped>

.header {
  background: #0d6efd;
  color: white;
  font-size: 30px;
}

input {
  outline: none;
  border-radius: 37px;
  border: none;
  background: #d3d3d3;
  font-family: Tahoma;
  margin-bottom: 15px;
}

label {
  font-family: Tahoma;
}

.btn {
  margin: auto;
  display: block;
}

a {
  color: blue;
  text-decoration: underline;
}

</style>