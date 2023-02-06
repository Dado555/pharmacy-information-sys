<template>
  <div class="row gutters-sm">
    <div class="col-sm-12 mb-3">
      <div class="card h-100">
        <div class="card-body">
          <FullCalendar :events="events" :options="options" />
        </div>
      </div>
    </div>
  </div>

  <button id="call-modal" data-bs-target="#modal" data-bs-toggle="modal" hidden></button>
  <!-- Modal -->
  <div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button id="close-no-show" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="container">
            <img src="../../assets/appointment.png" width="100" class="mb-3"/>
            <dl class="row" style="text-align: justify">
              <dt class="col-sm-4" v-bind:hidden="selectedEvent.extendedProps.appointmentStatus === 'FREE'">Patient:</dt>
              <dd class="col-sm-9" v-bind:hidden="selectedEvent.extendedProps.appointmentStatus === 'FREE'">{{ selectedEvent.extendedProps.patient }}</dd>

              <dt class="col-sm-4">Status:</dt>
              <dd class="col-sm-9">{{ selectedEvent.extendedProps.appointmentStatus }}</dd>

              <dt class="col-sm-4">Start: </dt>
              <dd class="col-sm-9">{{ moment(selectedEvent.start).format("DD.MM.YYYY. hh:mm") }}</dd>

              <dt class="col-sm-4 text-truncate">End: </dt>
              <dd class="col-sm-9">{{ moment(selectedEvent.end).format("DD.MM.YYYY. hh:mm") }}</dd>

              <dt class="col-sm-4 text-truncate">Duration: </dt>
              <dd class="col-sm-9">{{ moment(selectedEvent.end).diff(moment(selectedEvent.start), 'minutes') }} minutes</dd>

              <dt class="col-sm-4 text-truncate">Price: </dt>
              <dd class="col-sm-9">{{ selectedEvent.extendedProps.price }} $</dd>

              <dt class="col-sm-4 text-truncate">Pharmacy: </dt>
              <dd class="col-sm-9">{{ selectedEvent.extendedProps.pharmacy }}</dd>
            </dl>
          </div>
        </div>
        <div class="container">
          <button type="button" class="btn btn-primary mx-2" v-bind:disabled="selectedEvent.extendedProps.appointmentStatus != 'RESERVED' || selectedEvent.start < Date.now() || selectedEvent.start > Date.now() + 86400000" v-on:click="begin">Begin</button>
          <button type="button" class="btn btn-danger" v-bind:disabled="selectedEvent.extendedProps.appointmentStatus != 'RESERVED' || selectedEvent.start < Date.now() || selectedEvent.start > Date.now() + 86400000" data-bs-target="#cancel-modal-wcl" data-bs-toggle="modal">No-show</button>
        </div>
        <div class="modal-footer">
        </div>
      </div>
    </div>
  </div>

  <!-- Modal -->
  <div class="modal fade" id="cancel-modal-wcl" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document" style="width: 400px">
      <div class="modal-content" style="border:1px solid cadetblue;background-color: beige;">
        <div class="modal-header">
          <h5 class="modal-title">No-show?</h5>
          <button id="close-no-show-wcl" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
import moment from 'moment'
import FullCalendar from 'primevue/fullcalendar'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import emitter from "./EventBus"
export default {
  name: "WorkCalendar",
  components: {
    FullCalendar, AppointmentReport, Medicines
  },
  data() {
    return {
      showReport: false,
      showMedicines: false,
      options: {
        plugins:[dayGridPlugin, timeGridPlugin, interactionPlugin],
        dateClick: this.handleDateClick,
        eventClick: this.handleEventClick,
        headerToolbar: {
          left: 'prev,next',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        editable: true,
        selectable: true
      },
      events: [
      ],
      selectedEvent: {
        extendedProps: {
          appointmentStatus: ""
        }
      }
    };
  },
  methods: {
    handleDateClick: function(arg) {
      this.$toast.show('date click! ' + arg.dateStr, {
        type: 'info',
        position: 'top',
        duration: 2000
      });
    },
    handleEventClick(info) {
      this.selectedEvent = info.event;
      document.getElementById('call-modal').click();
    },
    cancel() {
      document.getElementById('close-no-show-wcl').click();
      this.selectedEvent.setExtendedProp('appointmentStatus', 'CANCELED');
          MedicalWorkerService.cancelAppointment(this.selectedEvent.id, this.selectedEvent.extendedProps.patientId).then((response) =>
          {
            this.$toast.show('Appointment canceled.',
                {
                  duration: 2000
                });
          });
    },
    begin() {
      let appointment = {};
      appointment['id'] = this.selectedEvent.id;
      appointment['status'] = this.selectedEvent.extendedProps.appointmentStatus;
      appointment['patient'] = this.selectedEvent.extendedProps.patient;
      appointment['patientId'] = this.selectedEvent.extendedProps.patientId;
      appointment['pharmacyId'] = this.selectedEvent.extendedProps.pharmacyId;
      appointment['start'] = this.selectedEvent.start;
      appointment['end'] = this.selectedEvent.end;
      MedicalWorkerService.setBusy(this.$store.getters.getUser.id).then((response) => {
      this.$store.commit('updateAppointmentReport', appointment);
      this.$toast.show('Appointment started.',
          {
            type: 'info',
            duration: 2000
          });
      document.getElementById('close-no-show').click();
      this.$store.commit('updateShowReport', true);
      }).catch((err) => {
        this.$toast.show(err.response.data.message,
            {
              type: 'error',
              duration: 2000
            });
      });
    },
    moment
  },
  mounted() {
    MedicalWorkerService.getMedicalWorkerAppointments(this.$store.getters.getUser.role, this.$store.getters.getUser.id).then((response) => {
      this.events = response.data;
    });

    emitter.on('reloadEvents', () => {
      MedicalWorkerService.getMedicalWorkerAppointments(this.$store.getters.getUser.role, this.$store.getters.getUser.id).then((response) => {
        this.events = response.data;
      });
    });
  }
}
</script>