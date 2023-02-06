<template>
  <popup-modal ref="popup">
  <div class="card mb-3">
    <div class="card-body">

      <div class="row">
        <div class="col-lg-3"></div>
        <div class="col-lg-6">
          <div class="input-group mb-4">
            <input v-model="searchInput"  type="text" class="form-control" placeholder="Medicine name..." aria-label="Recipient's username" aria-describedby="basic-addon2">
          </div><!-- /input-group -->
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-3"></div>
      </div><!-- /.row -->
      <div style="font-size: 45px; color: #DFDFDF" v-show="medicines.length == 0" class="mt-3"> No results.</div>
      <div style="border: 1px solid #ddd" v-show="medicines.length != 0">
        <table class="table table-hover table-borderless">

          <thead style="border-bottom: 3px solid #DFDFDF; font-weight: 600">
          <tr style="font-size: 17px">
            <td style="width: 5%" > </td>
            <td style="width: 15%"> <a href="#" v-on:click="sort('name')" onclick="return false;"> Name </a> </td>
            <td style="width: 10%"> <a href="#" v-on:click="sort('type')" onclick="return false;"> Type </a> </td>
            <td style="width: 10%"> <a href="#" v-on:click="sort('medicineForm')" onclick="return false;"> Form </a> </td>
            <td style="width: 10%"> <a href="#" v-on:click="sort('manufacture')" onclick="return false;"> Manufacture </a> </td>
            <td style="width: 5%"> <a href="#" v-on:click="sort('amount')" onclick="return false;"> Amount </a> </td>
            <td style="width: 25%" > </td>
          </tr>
          </thead>

          <tbody style="font-size: 15px">
          <tr v-for="medicine in sortedMedicines" style="border-bottom: 1px solid #ddd; word-break: break-all">
            <td v-on:click="details(medicine)">  <img style="border: 1px solid #ddd; width: 64px" src="../../../public/medicine.png"/></td>
            <td>{{ medicine.name }}</td>
            <td>{{ medicine.type }}</td>
            <td>{{ medicine.medicineForm }}</td>
            <td>{{ medicine.manufacture }}</td>
            <td>{{ medicine.amount }}</td>

            <th>
              <button class="btn btn-outline-primary mx-2" type="button" data-bs-target="#modal-medicine-details" data-bs-toggle="modal" v-on:click="selectedMedicine = medicine">Details</button>
              <button class="btn btn-primary" type="button" data-bs-target="#modal-amount" data-bs-toggle="modal" v-on:click="selectedMedicine = medicine">Prescribe</button>
            </th>

          </tr>
          </tbody>

        </table>
      </div>
      <div class="row">
        <button class="btn btn-success col-md-1 mt-2 mx-3" type="button" v-on:click="updateComponents(true, false)">Back</button>
      </div>
    </div>
  </div>
  </popup-modal>

  <button id="details-medicines" class="btn btn-outline-primary mx-2" type="button" data-bs-target="#modal-medicine-details" data-bs-toggle="modal" hidden>Details</button>
  <!-- Modal -->
  <div class="modal fade" id="modal-medicine-details" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button id="close-details" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="container">
            <img src="../../../public/medicine.png" width="100" class="mb-3"/>
            <dl class="row" style="text-align: justify">
              <dt class="col-sm-4">Medicine:</dt>
              <dd class="col-sm-9">{{ selectedMedicine.name }}</dd>

              <dt class="col-sm-4">Type:</dt>
              <dd class="col-sm-9">{{ selectedMedicine.type }}</dd>

              <dt class="col-sm-4">Form: </dt>
              <dd class="col-sm-9">{{ selectedMedicine.medicineForm }}</dd>

              <dt class="col-sm-4 text-truncate">Structure: </dt>
              <dd class="col-sm-9">{{ selectedMedicine.structure }}</dd>

              <dt class="col-sm-4 text-truncate">Manufacture: </dt>
              <dd class="col-sm-9">{{ selectedMedicine.manufacture }}</dd>

              <dt class="col-sm-4 text-truncate">Contraindications: </dt>
              <dd class="col-sm-9">{{ selectedMedicine.contraindications }}</dd>

              <dt class="col-sm-4 text-truncate">Daily intake: </dt>
              <dd class="col-sm-9">{{ selectedMedicine.dailyIntake }}</dd>
            </dl>
          </div>
        </div>
        <div class="container">
          <button type="button" class="btn btn-primary mx-2" data-bs-target="#modal-amount" data-bs-toggle="modal" v-on:click="closeDetails">Prescribe</button>
        </div>
        <div class="modal-footer">
        </div>
      </div>
    </div>
  </div>

  <div class="modal fade" id="modal-amount" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
           <h5 class="modal-title">Amount and therapy definition</h5>
           <button id="close-amount" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="container">
            <img src="../../../public/medicine.png" width="100" class="mb-3"/>
            <dl class="row" style="text-align: justify">
              <dt class="col-sm-9">Amount:</dt>
              <dd class="col-sm-4">
                <select class="form-select" v-model="amount">
                  <option v-for="n in 100" :value="n">{{ n }}</option>
                </select>
              </dd>

              <dt class="col-sm-9">Therapy (days):</dt>
              <dd class="col-sm-4">
                <select class="form-select" v-model="therapy">
                  <option v-for="n in 100" :value="n">{{ n }}</option>
                </select>
              </dd>

            </dl>
          </div>
        </div>
        <div class="container">
          <button type="button" class="btn btn-primary mx-2" v-on:click="prescribe" >Prescribe</button>
        </div>
        <div class="modal-footer">
        </div>
      </div>
    </div>
  </div>

  <!-- Button trigger modal -->
  <button id="replacement-btn" type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#replacement-medicines" hidden>
    Replacement medicines
  </button>

  <!-- Modal -->
  <div class="modal fade" id="replacement-medicines" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Replacement medicines</h5>
          <button id="replacement-close" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          {{ message }}
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-primary" data-bs-dismiss="modal">No</button>
          <button type="button" class="btn btn-primary" v-on:click="replacementMedicines">Yes</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import MedicalWorkerService from '@/service/MedicalWorkerService.js'
import PopupModal from "./PopupModal";
export default {
  name: "Medicines",
  components: {PopupModal},
  data() {
    return {
      searchInput: '',
      medicines: [],
      selectedMedicine: {},
      currentSort: 'name',
      currentSortDir: 'asc',
      amount: 1,
      therapy: 1,
      message: ''
    }
  },
  methods: {
    updateComponents(report, medicines) {
      this.$store.commit('updateShowReport', report);
      this.$store.commit('updateShowMedicines', medicines);
    },
    sort:function(s) {
      if(s === this.currentSort) {
        this.currentSortDir = this.currentSortDir==='asc'?'desc':'asc';
      }
      this.currentSort = s;
    },
    details(medicine) {
      this.selectedMedicine = medicine;
      document.getElementById('details-medicines').click();
    },
    prescribe() {
      let medicine = this.selectedMedicine;
      let patientId = this.$store.getters.getAppointmentReport.appointment.patientId;
      MedicalWorkerService.prescribeMedicine(patientId, this.amount, medicine.pharmacyMedicineId).then((response) => {

        document.getElementById('close-amount').click();

        if (response.data === -2) {
          this.$toast.show('Patient is allergic to selected medicine!',
              {
                type: 'error',
                duration: 2000
              });
          this.message = 'Patient is allergic to selected medicine. Do you want to check replacement medicines?'
          document.getElementById('replacement-btn').click();
          return;
        }

        if (response.data === -1) {
          this.$toast.show('Selected amount is not available.',
              {
                type: 'error',
                duration: 2000
              });
          this.message = 'Selected amount is not available. Do you want to check replacement medicines?'
          document.getElementById('replacement-btn').click();
          return;
        }

        medicine.amount = medicine.amount + this.amount;
        this.$store.commit('prescribeMedicine', {id:medicine.id, name: medicine.name, medicineForm: medicine.medicineForm, amount: this.amount, therapy: this.therapy, pharmacyMedicineId: medicine.pharmacyMedicineId});
        this.$toast.show('Medicine prescribed.',
            {
              type: 'info',
              duration: 2000
            });

      });
    },
    closeDetails() {
      document.getElementById('close-details').click();
    },
    replacementMedicines() {
      document.getElementById('replacement-close').click();
      MedicalWorkerService.getReplacementMedicines(this.selectedMedicine.pharmacyMedicineId).then((response) => {
        this.medicines = [];
        let medicines = this.medicines;
        response.data.forEach(function (item) {
          item.medicineDTO['amount'] = 0;
          item.medicineDTO['pharmacyMedicineId'] = item.id;
          medicines.push(item.medicineDTO);
        });
      });
    }
  },
  computed: {
    sortedMedicines:function() {
      return this.medicines.sort((a,b) => {
        let modifier = 1;
        if(this.currentSortDir === 'desc') modifier = -1;

        if(a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
        if(a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
        return 0;
      }).filter(medicine => {
        return medicine.name.toLowerCase().includes(this.searchInput.toLowerCase())
      });
    }

  },
  mounted() {
    MedicalWorkerService.getPharmacyMedicines(this.$store.getters.getAppointmentReport.appointment.pharmacyId).then((response) => {
        let medicines = this.medicines;
        response.data.forEach(function (item) {
          item.medicineDTO['amount'] = 0;
          item.medicineDTO['pharmacyMedicineId'] = item.id;
          medicines.push(item.medicineDTO);
        });
      this.$refs.popup.open();
    });
  }
}
</script>

<style scoped>
.form-control:focus {
  outline: none;
  box-shadow: none;
}
.btn:focus, select:focus {
  outline: none;
  box-shadow: none;
}

table {
  display:block;
  height:500px;
  overflow-y:scroll;
  text-align: left;
  vertical-align: middle;
}

th, td {
  width:250px;
}

::-webkit-scrollbar{
  width: 10px;
}
::-webkit-scrollbar-track{
  background: linear-gradient(transparent, #2596be);
  border-radius: 5px;
}
::-webkit-scrollbar-thumb{
  background: linear-gradient(transparent, #7db47f);
  border-radius: 5px;
}
::-webkit-scrollbar-thumb:hover{
  background: linear-gradient(transparent, #52b366);
}
</style>