<template>
  <div class="card mb-3">
    <div class="card-body">

      <div class="row">
        <div class="col-lg-3"></div>
        <div class="col-lg-6">
          <div class="input-group mb-4">
            <input v-model="searchInput"  type="text" class="form-control" placeholder="Patients' first and last name" aria-label="Recipient's username" aria-describedby="basic-addon2">
            <div class="input-group-append">
              <button id="search-btn" class="btn btn-primary"  v-on:click="search" v-show="showSearch">Search</button>
            </div>
          </div><!-- /input-group -->
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-3"></div>
      </div><!-- /.row -->
      <div style="font-size: 45px; color: #DFDFDF" v-show="patients.length == 0" class="mt-3"> No results.</div>
      <div style="border: 1px solid #ddd" v-show="patients.length">
      <table class="table table-hover table-borderless">

        <thead style="border-bottom: 3px solid #DFDFDF; font-weight: 600">
        <tr style="font-size: 17px">
          <td style="width: 5%" > </td>
          <td style="width: 20%"> <a href="#" v-on:click="sort('firstName')" onclick="return false;"> First name </a> </td>
          <td style="width: 20%"> <a href="#" v-on:click="sort('lastName')" onclick="return false;"> Last name </a> </td>
          <td style="width: 25%"> <a href="#" v-on:click="sort('email')" onclick="return false;"> Email address </a> </td>
          <td style="width: 30%"> <a href="#" v-on:click="sort('appointmentStatus')" onclick="return false;"> Upcoming appointment </a> </td>
        </tr>
        </thead>

        <tbody style="font-size: 15px">
        <tr v-for="patient in sortedPatients" style="border-bottom: 1px solid #ddd; word-break: break-all">
          <td>  <img style="border: 1px solid #ddd" src="https://img.icons8.com/cute-clipart/64/26e07f/protection-mask--v2.png"/></td>
          <td>{{ patient.firstName }}</td>
          <td>{{ patient.lastName }}</td>
          <td>{{ patient.email }}</td>

          <th v-show="patient.appointment == null"> Nothing scheduled. </th>

          <th v-show="patient.appointment && patient.appointment.appointmentStatus != 'CANCELED'">
            <button class="btn btn-primary" type="button" v-on:click="begin(patient)">Begin</button>
            <button class="btn btn-danger mx-2" type="button" data-bs-target="#cancel-modal" data-bs-toggle="modal" v-on:click="noShowPatient = patient">No-show</button>
          </th>

          <th v-show="patient.appointment && patient.appointment.appointmentStatus == 'CANCELED'"> Canceled. </th>
        </tr>
        </tbody>

      </table>
      </div>
    </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="cancel-modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">No-show?</h5>
          <button id="close-no-show" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          Patient did not appear for the appointment?
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" v-on:click="cancel">Confirm</button>
        </div>
      </div>
    </div>
  </div>


  <AppointmentReport v-if="$store.getters.showReport"></AppointmentReport>
  <Medicines v-if="$store.getters.showMedicines"></Medicines>
</template>

<script>
import MedicalWorkerService from '@/service/MedicalWorkerService.js'
import AppointmentReport from './AppointmentReport'
import Medicines from './Medicines'
export default {
  name: "PatientSearch",
  components: {
    AppointmentReport, Medicines
  },
  data() {
    return {
      searchInput: '',
      patients: [],
      noShowPatient: {},
      currentSort: 'appointmentStatus',
      currentSortDir: 'desc'
    }
  },
  methods: {
    cancel() {
      document.getElementById('close-no-show').click();
      this.noShowPatient.appointment.appointmentStatus = 'CANCELED';
          MedicalWorkerService.cancelAppointment(this.noShowPatient.appointment.id, this.noShowPatient.id).then((response) =>
          {
            this.$toast.show('Appointment canceled.',
                {
                  duration: 2000
                });
          });

    },
    search() {
      this.currentSort = 'appointmentStatus';
      this.currentSortDir = 'desc';
      if (this.searchInput.trim() === '') {
        this.patients = [];
        return;
      }
          MedicalWorkerService.patientSearch(this.searchInput, this.$store.getters.getUser.id).then((response) =>
          {
            this.patients = response.data;
          });
    },
    begin(patient) {
      MedicalWorkerService.setBusy(this.$store.getters.getUser.id).then((response) => {
      this.$store.commit('updateAppointmentReport', patient.appointment);
      this.$toast.show('Appointment started.',
          {
            type: 'info',
            duration: 2000
          });
      this.searchInput = '';
      document.getElementById('search-btn').click();
      this.$store.commit('updateShowReport', true);
      }).catch((err) => {
        this.$toast.show(err.response.data.message,
            {
              type: 'error',
              duration: 2000
            });
      });
    },
    sort:function(s) {
      if(s === this.currentSort) {
        this.currentSortDir = this.currentSortDir==='asc'?'desc':'asc';
      }
      this.currentSort = s;
    }
  },
  computed:{
    sortedPatients:function() {
      return this.patients.sort((a,b) => {
        let modifier = 1;
        if(this.currentSortDir === 'desc') modifier = -1;

        if (this.currentSort === 'appointmentStatus')
        {
          if(a['appointment'] && b['appointment'])
          {
          if(a['appointment'][this.currentSort] < b['appointment'][this.currentSort]) return -1 * modifier;
          if(a['appointment'][this.currentSort] > b['appointment'][this.currentSort]) return 1 * modifier;
          }
          if(a['appointment']) return 1 * modifier;
          if(b['appointment']) return -1 * modifier;
          return 0;
        }

        if(a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
        if(a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
        return 0;
      });
    },
    showSearch:function () {
      return !(this.$store.getters.showReport || this.$store.getters.showMedicines);
    }
  }
}
</script>

<style scoped>
.form-control:focus {
  outline: none;
  box-shadow: none;
}
.btn:focus {
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