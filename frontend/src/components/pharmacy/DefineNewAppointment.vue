<template>
  <div class="container">
    <dl class="row" style="text-align: justify">
      <div class="col-sm-4 align-self-center">
        <dt class="col-sm-9">{{ role }}:</dt>
        <dd class="col-sm-9">{{ worker.firstName }}  {{ worker.lastName }}</dd>
        <dt class="col-sm-9">Email:</dt>
        <dd class="col-sm-9">{{ worker.email }}</dd>

        <dt class="col-sm-9">Phone number: </dt>
        <dd class="col-sm-9">{{ worker.phoneNumber }}</dd>

        <dt class="col-sm-9">Address: </dt>
        <dd class="col-sm-12">{{ worker.address.name }} {{ worker.address.number }}, {{ worker.address.city }} {{ worker.address.postalCode }}</dd>

        <dt class="col-sm-9">Work time: </dt>
        <dd class="col-sm-9"> {{ startWork/60 }}:{{ startWork%60 }}h - {{ endWork/60 }}:{{ endWork%60 }}h </dd>
        <br>
        <div v-if="userRole === 'PHARMACY_ADMIN'">
          <dt class="col-sm-9">START TIME: </dt>
          <input class="col-sm-9" v-model="startTime" readonly="readonly" style="border: hidden">

          <dt class="col-sm-9">END TIME: </dt>
          <input class="col-sm-9" v-model="endTime" readonly="readonly" style="border: hidden">

          <dt class="col-sm-9">PRICE: </dt>
          <input type="number" step="0.01" v-model="newAppointment.price" style="border: hidden" placeholder="Price: 1000 RSD" />
          <br><br>
          <button type="button" class="btn btn-outline-primary col-sm-9" @click="restartTime()">Restart start and end time</button>
          <br><br>
          <button type="button" class="btn btn-primary col-sm-9" @click="createNewAppointment()">Confirm</button>
        </div>
      </div>
      <div class="col-sm-8">
        <dt class="col-sm-12">
          <div><FullCalendar ref="calendar" :events="events" :options="options" /></div>
        </dt>
      </div>
    </dl>
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
      </div>
    </div>
  </div>

  <confirmation-dialogue ref="confirmDialogue"></confirmation-dialogue>
</template>

<script>
import FullCalendar from 'primevue/fullcalendar'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import PopupModal from "../Modals/PopupModal";
import moment from "moment";
import ConfirmationDialogue from "../Modals/ConfirmationDialogue";
import PatientService from "../../service/PatientService";
import MedicalWorkerService from "../../service/MedicalWorkerService";

export default {
  name: "DefineNewAppointment",
  props: ['worker', 'pharmacyId', 'startWork', 'endWork', 'role', 'userRole', 'userId'],
  data: function (){
    return {
      options: {
        plugins:[timeGridPlugin, interactionPlugin],
        initialView: 'timeGridWeek',
        dateClick: this.handleDateClick,
        eventClick: this.handleEventClick,
        headerToolbar: {
          left: 'prev,next',
          center: 'title',
          right: 'timeGridWeek,timeGridDay'
        },
        editable: true,
        selectable: true
      },
      events: [
      ],
      selectedTime: {
        start: null,
        end: null
      },
      startTime: "Not defined",
      endTime: "Not defined",
      calendarApi: null,
      newAppointment: {
        title: 0, // EXAMINATION(1), COUNSELING(2);
        appointmentStatus: 0, // FREE(1)
        start: -1, // long date
        end: -1,
        price: 0.0,
        pharmacyId: parseInt(this.pharmacyId),
        workerId: this.worker.id
      },
      counter: 99999,
      selectedEvent: {
        extendedProps: {
          appointmentStatus: ""
        }
      },
    }
  },
  components: {
    ConfirmationDialogue,
    PopupModal,
    FullCalendar
  },
  mounted() {
    this.initialize();
  },
  methods: {
    initialize() {
      MedicalWorkerService.getAppointments(this.role, this.worker.id)
      .then((response) => {
        let data = [];
        for(let i = 0; i < response.data.length; i++) {
          if(response.data[i].appointmentStatus === "FREE"){
            let event = response.data[i];
            event.title = event.title + " -- FREE -- Price: " + event.price;
            event.backgroundColor = "red";
            data.push(event);
          }else
            data.push(response.data[i]);
        }
        this.events = data;
        console.log(this.events);
      });
    },
    restartTime() {
      if(this.calendarApi === null) {
        this.$toast.show('No time to remove!', {
          type: 'info',
          position: 'top',
          duration: 5000
        });
      } else {
        this.startTime = "Not defined";
        this.endTime = "Not defined";
        this.selectedTime.start = null;
        this.selectedTime.end = null;
        if(this.calendarApi.getEventById((this.counter-1).toString()))
          this.calendarApi.getEventById((this.counter-1).toString()).remove();
        this.calendarApi = null;
      }
    },
    selectedTimeVer(time, date) {
      let current = new Date();
      if(date < current){
        this.$toast.show('Can\'t set appointment in the past!', {
          type: 'info',
          position: 'top',
          duration: 4000
        });
        return false;
      }
      if(time < this.startWork || time > this.endWork) {
        let hoursStart = this.startWork / 60;
        let minutesStart = this.startWork % 60;
        let hoursEnd = this.endWork / 60;
        let minutesEnd = this.endWork % 60;
        if(minutesStart === 0)
          minutesStart = "00";
        if(minutesEnd === 0)
          minutesEnd = "00";
        this.$toast.show('Worker work time is ' + hoursStart + ":" + minutesStart + "h - " + hoursEnd + ":" + minutesEnd + "h", {
          type: 'info',
          position: 'top',
          duration: 4000
        });
        return false;
      }
      if(this.selectedTime.start == null || this.selectedTime.end == null)
        return true;
    },
    handleDateClick(arg) {
      if(this.userRole === "PHARMACY_ADMIN" ) {
        // definisi novi termin
        let date = new Date(arg.dateStr);
        let hours = date.getHours();
        let minutes = date.getMinutes();
        let time = hours * 60 + minutes;
        if (this.selectedTimeVer(time, date)) {
          if (this.selectedTime.start == null) {
            let closestBef = this.closestBefore(date.getTime());
            let closestAft = this.closestAfter(date.getTime());
            if (closestBef[1] >= 0 && closestAft[1] >= 0) {
              this.selectedTime.start = [time, date];
              this.newAppointment.start = date.getTime();
              if (minutes === 0)
                minutes = "00";
              this.$toast.show('Start time is set to ' + hours + ":" + minutes + "h", {
                type: 'info',
                position: 'top',
                duration: 4000
              });
              this.startTime = hours + ":" + minutes + "h";
            } else {
              this.$toast.show('Can\'t have more than one appointment at a time!', {
                type: 'info',
                position: 'top',
                duration: 5000
              });
            }
          } else {
            this.endSelected(time, date, arg);
          }
        }
      } else {
        this.$toast.show('date click! ' + arg.dateStr, {
          type: 'info',
          position: 'top',
          duration: 2000
        });
      }
    },
    endSelected(time, date, arg) {
      let closestAftStart = this.closestAfter(this.selectedTime.start[1].getTime())[0];
      if (closestAftStart == null || (date.getTime() <= closestAftStart.start &&
          date.getTime() >= this.selectedTime.start[1].getTime() + 1800000)) {
        let hours = date.getHours();
        let minutes = date.getMinutes();
        this.selectedTime.end = [time, date];
        this.newAppointment.end = date.getTime();
        if (minutes === 0)
          minutes = "00";
        this.$toast.show('End time is set to ' + hours + ":" + minutes + "h", {
          type: 'info',
          position: 'top',
          duration: 4000
        });
        this.endTime = hours + ":" + minutes + "h";

        let calendarApi = arg.view.calendar
        calendarApi.addEvent({
          id: this.counter.toString(),
          title: "Termin you set",
          start: this.selectedTime.start[1].getTime(),
          end: this.selectedTime.end[1].getTime()
        });
        this.counter++;
        this.calendarApi = calendarApi;
      } else {
        this.$toast.show('Can\'t set this end time!', {
          type: 'info',
          position: 'top',
          duration: 3000
        });
      }
    },
    createNewAppointment() {
      if(this.newAppointment.price < 10.00 || this.newAppointment.start === -1 || this.newAppointment.end === -1) {
        this.$toast.show('Define start-end time and valid price!', {
          type: 'info',
          position: 'top',
          duration: 3000
        });
      }else {
        this.newAppointment.price = parseFloat(this.newAppointment.price);
        this.calendarApi.getEventById((this.counter-1).toString()).remove();
        MedicalWorkerService.setAppointment(this.newAppointment, this.role)
        .then((response) => {
          if (response) {
            this.$toast.show('Appointment successfully added!', {
              type: 'info',
              position: 'top',
              duration: 2000
            });
            this.restartTime();
            this.initialize();
          } else {
            this.$toast.show('Could not add appointment!', {
              type: 'info',
              position: 'top',
              duration: 2000
            });
          }
        });

      }
    },
    handleEventClick(info) {
      if (this.userRole === 'PHARMACY_ADMIN'){
        if(this.selectedTime.start != null) {
          let date = new Date(info.event.start.getTime());
          let hours = date.getHours();
          let minutes = date.getMinutes();
          let time = hours * 60 + minutes;
          this.endSelected(time, date, info);
        } else {
          this.selectedEvent = info.event;
          document.getElementById('call-modal').click();
        }
      } else if (this.userRole === 'PATIENT' && info.event.extendedProps.appointmentStatus === "FREE") {
        let appointmentDTO = Object.assign({id:info.event._def.publicId,
          start: info.event.start.getTime(), end: info.event.end.getTime()}, info.event.extendedProps);
        appointmentDTO.patientId = this.userId;
        this.areYouSure(appointmentDTO, this.moment(info.event.start).format("DD.MM.YYYY. hh:mm"),
            this.moment(info.event.end).format("DD.MM.YYYY. hh:mm"));
      }
    },
    areYouSure(appointmentDTO, start, end) {
      this.$refs.confirmDialogue.show({
        title: "Set up appointment",
        message: "Pharmacy: " + appointmentDTO.pharmacy + ", Start: " + start + ", End: " + end,
        okButton: 'Set'
      }).then((result)=>{
        if(result)
          this.setAppointment(appointmentDTO);
      });
    },
    setAppointment(appointmentDTO) {
      PatientService.setAppointment(appointmentDTO)
        .then((response) => {
          if(response) {
            this.$toast.show('Appointment successfully set!', {
              type: 'info',
              position: 'top',
              duration: 4000
            });
            this.initialize();
          } else {
            this.$toast.show('Someone already took this appointment!', {
              type: 'info',
              position: 'top',
              duration: 4000
            });
            this.initialize();
          }
        });
    },
    closestBefore(time) {
      let min = 9999999999999;
      let minSch = null;
      for(let i = 0; i < this.events.length; i++) {
        let event = this.events[i];
        if(event.end <= time && time - event.end < min) {
          min = time-event.end;
          minSch = event;
        }
      }
      return [minSch, min]
    },
    closestAfter(time) {
      let min = 9999999999999;
      let minSch = null;
      for(let i = 0; i < this.events.length; i++) {
        let event = this.events[i];
        if(event.start >= time && event.start - time < min) {
          min = event.start - time;
          minSch = event;
        }
      }
      return [minSch, min]
    },
    moment
  }
}
</script>

<style scoped>

</style>