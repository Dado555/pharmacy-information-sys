<template>
  <popup-modal ref="popup">
  <div class="card mb-3">
    <div class="card-body">
      <div class="container">
        <img style="width: 13%" src="../../assets/appointment.png"/>
        <div class="mt-2 display-6" style="font-size: 35px"> {{ $store.getters.getAppointmentReport.appointment.patient }}</div>
        <h4> {{ moment($store.getters.getAppointmentReport.appointment.start).format("DD.MM.YYYY. hh:mm") }}h </h4>
      </div>
      <div v-show="$store.getters.getAppointmentReport.appointment.id != -1">
      <div class="form-group">
        <textarea style="font-size: 17px" v-model="$store.getters.getAppointmentReport.appointmentInfo" class="form-control my-3" rows="5" placeholder="Diagnosis, observations, advices..."></textarea>
      </div>
      <div class="container">
          <button class="btn btn-primary" type="button" v-on:click="updateComponents(false, true)">Prescribe medicines </button>
      </div>
      <div class="mt-3" style="border: 1px solid #ddd" v-show="$store.getters.getAppointmentReport.medicines.length != 0">
      <table class="table table-hover table-borderless" style="text-align: center">
        <thead style="border-bottom: 1px solid #DFDFDF;font-weight: bold">
        <tr style="font-size: 15px">
          <td style="width: 5%" > </td>
          <td style="width: 20%">  Medicine name  </td>
          <td style="width: 20%">  Medicine form  </td>
          <td style="width: 15%">  Amount  </td>
          <td style="width: 20%">  Therapy (days)  </td>
          <td style="width: 20%"> </td>
        </tr>
        </thead>

        <tbody style="font-size: 15px">
        <tr v-for="medicine in $store.getters.getAppointmentReport.medicines" style="border-bottom: 1px solid #ddd; word-break: break-all;">
          <td>  <img style="border: 1px solid #ddd; width: 64px" src="../../../public/medicine.png"/></td>
          <td>{{ medicine.name }}</td>
          <td>{{ medicine.medicineForm }}</td>
          <td>{{ medicine.amount }}</td>
          <td>{{ medicine.therapy }}</td>

          <th>
            <button class="btn btn-danger mx-2" type="button" v-on:click="remove(medicine)">Remove</button>
          </th>

        </tr>
        </tbody>
      </table>
      </div>
      <div class="mt-5" style="text-align: right">
        <button class="btn btn-danger mx-2" type="button" v-on:click="close">Close</button>
        <button class="btn btn-primary" type="button" v-on:click="save">Save</button>
      </div>
      </div>
    </div>
  </div>
  </popup-modal>

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


  <button id="schedule-appointment-btn" type="button" class="btn btn-outline-primary mx-2" data-bs-toggle="modal" data-bs-target="#schedule-appointment" hidden> </button>
  <!-- Modal -->
  <div class="modal fade" id="schedule-appointment" tabindex="-1" aria-labelledby="schedule-appointment" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Schedule next appointment</h5>
          <button type="button" id="schedule-appointment-close" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="updateComponents(false, false)"></button>
        </div>
        <div class="modal-body">
          <div class="container col-md-10 d-grid gap-2">
            <div class="form-floating">
              <input v-model="date" class="form-control" type="text" onfocus="(this.type='date')" placeholder="Date:" aria-label="default input example">
              <label for="floatingInput">Date:</label>
            </div>
            <div class="form-floating">
              <input v-model="time" class="form-control" type="text" onfocus="(this.type='time')" placeholder="Time:" aria-label="default input example">
              <label for="floatingInput">Time:</label>
            </div>
            <div class="form-floating">
              <input v-model="duration" class="form-control" type="number" placeholder="Duration in minutes:" aria-label="default input example">
              <label for="floatingInput">Duration in minutes:</label>
            </div>
          </div>
          <hr>
          <div class="container">
            <button id="free-appointments-btn" type="button" class="btn btn-outline-primary mx-2" v-show="$store.getters.getUser.role === 'DERMATOLOGIST'" v-on:click="loadFreeAppointments" data-bs-toggle="modal" data-bs-target="#free-appointments" hidden>Choose existing FREE appointment </button>
            <button type="button" class="btn btn-primary" v-on:click="schedule"> Schedule </button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="free-appointments" tabindex="-1" role="dialog" aria-labelledby="free-appointments" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Free appointments</h5>
          <button id="close-free-appointments" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="updateComponents(false, false)"></button>
        </div>
        <div class="modal-body">
          <div style="font-size: 25px; color: slategrey" v-show="freeAppointments.length === 0" class="mt-3"> There are no any available appointments for current pharmacy.</div>
          <div v-show="freeAppointments.length > 0" class="card mb-2" v-for="appointment in freeAppointments">
            <div class="card-body row">
              <img src="../../assets/appointment.png" class="col-md-2"/>
              <div class="col-md-4">  <b> Start: </b> {{ moment(appointment.startDateAndTime).format("DD.MM.YYYY. hh:mm") }} </div>
              <div class="col-md-4">  <b> Duration: </b> {{ moment(appointment.endDateAndTime).diff(moment(appointment.startDateAndTime), 'minutes') }} </div>
              <input class="col-md-2 mt-2" type="radio" name="flexRadioDefault" v-on:click="selectedAppointment=appointment">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" v-on:click="confirm" v-bind:disabled="freeAppointments.length === 0">Confirm</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import MedicalWorkerService from '@/service/MedicalWorkerService.js'
import moment from 'moment'
import PopupModal from "./PopupModal"
import emitter from "./EventBus"
export default {
  name: "AppointmentReport",
  components: {PopupModal},
  methods: {
    moment,
    save() {
      MedicalWorkerService.saveAppointmentReport(this.$store.getters.getAppointmentReport)
          .then((response) => {
            emitter.emit('reloadEvents');
            this.$toast.show('Appointment report saved.',
            {
              type: 'info',
              duration: 2000
            });
            if (this.$store.getters.getUser.role === 'PHARMACIST') {
              document.getElementById('schedule-appointment-btn').click();
            } else {
              document.getElementById('free-appointments-btn').click();
            }
      });

      MedicalWorkerService.setFree(this.$store.getters.getUser.id).then((response) => {
      }).catch((err) => {
        this.$toast.show(err.response.data.message,
            {
              type: 'error',
              duration: 2000
            });
      });
    },
    remove(medicine) {
      MedicalWorkerService.removeMedicine(medicine.amount, medicine.pharmacyMedicineId).then((response) => {
      this.$toast.show('Medicine removed.',
          {
            duration: 2000
          });
        this.$store.commit('removeMedicine', medicine);
      });
    },
    loadFreeAppointments() {
      let appointmentReport = this.$store.getters.getAppointmentReport;
      MedicalWorkerService.loadFreeAppointments(this.$store.getters.getUser.id, appointmentReport.appointment.pharmacyId)
          .then((response) => {
            this.freeAppointments = response.data;
          });
    },
    getConvertedDateTime() {
      let date = new Date(this.date).getTime();
      let arr = this.time.split(':');
      let time = (arr[0] * 3600 * 1000) + arr[1] * 60 * 1000 - 2 * 3600 * 1000;
      return date + time;
    },
    confirm() {
      let appointmentReport = this.$store.getters.getAppointmentReport;
      MedicalWorkerService.freeToReservedAppointment(this.selectedAppointment.id, appointmentReport.appointment.patientId).then((response) => {
        if (response.data) {
          this.$toast.show('FREE appointment set to RESERVED!',
              {
                type: 'info',
                position: 'top',
                duration: 2000
              });
          emitter.emit('reloadEvents');
          this.updateComponents(false, false);
          document.getElementById('close-free-appointments').click();
        }
      }).catch((err) => {
        this.$toast.show(err.response.data.message,
            {
              type: 'error',
              duration: 2000
            });
      });

    },
    schedule() {
      let appointmentReport = this.$store.getters.getAppointmentReport;
      if (this.date === '' || this.time === '' || this.getConvertedDateTime() < Date.now() + 86400000) {
        this.$toast.show('Invalid date and time.',
            {
              type: 'error',
              duration: 2000
            });
        return;
      }
      MedicalWorkerService.scheduleNewAppointment(this.getConvertedDateTime(), this.getConvertedDateTime() + this.duration * 60 * 1000, appointmentReport.appointment.patientId, this.$store.getters.getUser.id, appointmentReport.appointment.pharmacyId).then((response) => {
        if (response.data) {
          this.$toast.show('Appointment scheduled!',
              {
                type: 'info',
                position: 'top',
                duration: 2000
              });
          emitter.emit('reloadEvents');
          this.updateComponents(false, false);
          document.getElementById('schedule-appointment-close').click();
        }
      }).catch((err) => {
        this.$toast.show(err.response.data.message,
            {
              type: 'error',
              duration: 2000
            });
      });
    },
    updateComponents(report, medicines) {
      this.$store.commit('updateShowReport', report);
      this.$store.commit('updateShowMedicines', medicines);
    },
    close() {
      MedicalWorkerService.setFree(this.$store.getters.getUser.id).then((response) => {
        this.updateComponents(false, false);
      }).catch((err) => {
        this.$toast.show(err.response.data.message,
            {
              type: 'error',
              duration: 2000
            });
      });
      }

  },
  data() {
    return {
      freeAppointments: [{startDateAndTime: 1621934704000, endDateAndTime: 1621938304000}],
      selectedAppointment: {},
      date: null,
      time: null,
      duration: null,

      pharmacyId: null,
      medicalWorkerId: null
    };
  },
  mounted() {
    this.$refs.popup.open();
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
  overflow-y:scroll;
  text-align: left;
  vertical-align: middle;
}

textarea:focus {
  border-color: lightseagreen;
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