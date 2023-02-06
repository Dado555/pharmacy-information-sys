<template>
    <div class="container">
      <dl class="row" style="text-align: justify">
        <div class="col-sm-4 align-self-center">
          <dt class="col-sm-9">Dermatologist:</dt>
          <dd class="col-sm-9">{{ dermatologistShow.firstName }}  {{ dermatologistShow.lastName }}</dd>

          <dt class="col-sm-9">Email:</dt>
          <dd class="col-sm-9">{{ dermatologistShow.email }}</dd>

          <dt class="col-sm-9">Phone number: </dt>
          <dd class="col-sm-9">{{ dermatologistShow.phoneNumber }}</dd>

          <dt class="col-sm-9">Address: </dt>
          <dd class="col-sm-12">{{ dermatologistShow.address.name }} {{ dermatologistShow.address.number }}, {{ dermatologistShow.address.city }} {{ dermatologistShow.address.postalCode }}</dd>

          <dt class="col-sm-9">Role: </dt>
          <dd class="col-sm-9">Pharmacist</dd>

          <dt class="col-sm-9">Is user active: </dt>
          <dd class="col-sm-9">{{ dermatologistShow.active }}</dd>

          <dt class="col-sm-9">START TIME: </dt>
          <input class="col-sm-9" v-model="startTime" readonly="readonly" style="border: hidden">

          <dt class="col-sm-9">END TIME: </dt>
          <input class="col-sm-9" v-model="endTime" readonly="readonly" style="border: hidden">

          <button type="button" class="btn btn-outline-primary" @click="restartTime()">Restart start and end time</button>
        </div>
        <div class="col-sm-8">
          <dt class="col-sm-12">
            <div><FullCalendar ref="calendar" :events="events" :options="options" /></div>
          </dt>
        </div>
      </dl>
    </div>
</template>

<script>
import FullCalendar from 'primevue/fullcalendar'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'

export default {
  name: "DermatologistHiringModal",
  props: ['dermatologistShow', 'scheduleList', 'pharmacy'],
  data: function (){
    return {
      options: {
        plugins:[timeGridPlugin, interactionPlugin],
        initialView: 'timeGridDay',
        dateClick: this.handleDateClick,
        eventClick: this.handleEventClick,
        headerToolbar: {
          right: '',
          center: '',
          left: ''
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
      calendarApi: null
    }
  },
  components: {
    FullCalendar
  },
  mounted() {
    let currentDateTime = new Date();
    console.log("SCHEDULE LIST: ", this.scheduleList);
    console.log(this.pharmacy);
    let arr = [];
    for(let i = 0; i < this.scheduleList.length; i++) {
      let schedule = this.scheduleList[i];
      let hours = Math.floor(schedule[0]/60);
      let minutes = schedule[0] % 60;
      let start = currentDateTime.setHours(hours, minutes, 0);
      hours = Math.floor(schedule[1]/60);
      minutes = schedule[1] % 60;
      let end = currentDateTime.setHours(hours, minutes, 0);
      arr.push({title: "TAKEN", start: start, end: end});
    }
    this.events = arr;
  },
  methods: {
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
        this.calendarApi.getEventById('99999').remove();
        this.calendarApi = null;
        this.$emit('change', null, null);
      }
    },
    selectedTimeVer(time) {
      if(time < this.pharmacy.startWorkTime || time > this.pharmacy.endWorkTime) {
        let hoursStart = this.pharmacy.startWorkTime / 60;
        let minutesStart = this.pharmacy.startWorkTime % 60;
        let hoursEnd = this.pharmacy.endWorkTime / 60;
        let minutesEnd = this.pharmacy.endWorkTime % 60;
        if(minutesStart === 0)
          minutesStart = "00";
        if(minutesEnd === 0)
          minutesEnd = "00";
        this.$toast.show('Pharmacy work time is ' + hoursStart + ":" + minutesStart + "h - " + hoursEnd + ":" + minutesEnd + "h", {
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
      // definisi novi termin
      let date = new Date(arg.dateStr);
      let hours = date.getHours();
      let minutes = date.getMinutes();
      let time = hours * 60 + minutes;
      if(this.selectedTimeVer(time)) {
        if (this.selectedTime.start == null) {
          let closestBef = this.closestBefore(time);
          let closestAft = this.closestAfter(time);
          if (closestBef[1] >= 30 && closestAft[1] >= 60) {
            this.selectedTime.start = [time, date];
            if (minutes === 0)
              minutes = "00";
            this.$toast.show('Start time is set to ' + hours + ":" + minutes + "h", {
              type: 'info',
              position: 'top',
              duration: 4000
            });
            this.startTime = hours + ":" + minutes + "h";
          } else {
            this.$toast.show('Can\'t set this start time. Start time must be at least 30min after last appointment and at least 1h before next appointment!', {
              type: 'info',
              position: 'top',
              duration: 6000
            });
          }
        } else {
          console.log(this.selectedTime.start);
          let closestAftStart = this.closestAfter(this.selectedTime.start[0])[0];
          if (closestAftStart == null || (time <= closestAftStart[0] - 30)) {
            this.selectedTime.end = [time, date];
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
              id: '99999',
              title: "Termin you set",
              start: this.selectedTime.start[1].getTime(),
              end: this.selectedTime.end[1].getTime()
            });
            this.calendarApi = calendarApi;
            this.$emit('change', this.selectedTime.start[0], this.selectedTime.end[0]);
          } else {
            this.$toast.show('Can\'t set this end time. End time must be at least 30min before next appointment!', {
              type: 'info',
              position: 'top',
              duration: 6000
            });
          }
        }
      }
    },
    closestBefore(time) {
      let min = 999999;
      let minSch = null;
      for(let i = 0; i < this.scheduleList.length; i++) {
        let schedule = this.scheduleList[i];
        if(schedule[1] <= time && time - schedule[1] < min) {
          min = time-schedule[1];
          minSch = schedule;
        }
      }
      return [minSch, min]
    },
    closestAfter(time) {
      let min = 999999;
      let minSch = null;
      for(let i = 0; i < this.scheduleList.length; i++) {
        let schedule = this.scheduleList[i];
        if(schedule[0] >= time && schedule[0] - time < min) {
          min = schedule[0] - time;
          minSch = schedule;
        }
      }
      return [minSch, min]
    }
  }
}
</script>

<style scoped>

</style>