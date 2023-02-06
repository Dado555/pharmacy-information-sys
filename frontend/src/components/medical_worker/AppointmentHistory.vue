<template>
  <div class="card mb-3">
    <div class="card-body">

      <div class="row">
        <div class="col-lg-3"></div>
        <div class="col-lg-6">
          <div class="input-group mb-4">
            <input v-model="searchInput"  type="text" class="form-control" placeholder="Patient's name and lastname..."  aria-describedby="basic-addon2">
          </div><!-- /input-group -->
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-3"></div>
      </div><!-- /.row -->
      <div style="font-size: 45px; color: #DFDFDF" v-show="sortedAppointments.length == 0" class="mt-3"> No results.</div>
      <div style="border: 1px solid #ddd" v-show="sortedAppointments.length != 0">
        <table class="table table-hover table-borderless">

          <thead style="border-bottom: 3px solid #DFDFDF; font-weight: 600">
          <tr style="font-size: 17px">
            <td style="width: 10%" > </td>
            <td style="width: 20%"> <a href="#" v-on:click="sort('patient')" onclick="return false;"> Patient </a> </td>
            <td style="width: 15%"> <a href="#" v-on:click="sort('start')" onclick="return false;"> Date </a> </td>
            <td style="width: 15%"> <a href="#" v-on:click="sort('start')" onclick="return false;"> Time </a> </td>
            <td style="width: 10%"> <a href="#" v-on:click="sort('duration')" onclick="return false;"> Duration </a> </td>
            <td style="width: 10%"> <a href="#" v-on:click="sort('price')" onclick="return false;"> Price </a> </td>
            <td style="width: 15%" > <a href="#" v-on:click="sort('pharmacy')" onclick="return false;"> Pharmacy </a> </td>
          </tr>
          </thead>

          <tbody style="font-size: 15px">
          <tr v-for="appointment in sortedAppointments" style="border-bottom: 1px solid #ddd; word-break: break-all">
            <td v-on:click="details(appointment)">  <img style="border: 1px solid #ddd; width: 64px" src="../../assets/appointment.png"/></td>
            <td>{{ appointment.patient }}</td>
            <td>{{ moment(appointment.start).format("DD.MM.YYYY.") }}</td>
            <td>{{ moment(appointment.start).format("hh:mm") }}</td>
            <td>{{ moment(appointment.end).diff(moment(appointment.start), 'minutes') }}</td>
            <td>{{ appointment.price }}</td>
            <td>{{ appointment.pharmacy }}</td>
          </tr>
          </tbody>

        </table>
      </div>
    </div>
  </div>

</template>

<script>
import MedicalWorkerService from '@/service/MedicalWorkerService.js'
import moment from 'moment'
export default {
  name: "AppointmentHistory",
  data() {
    return {
      searchInput: '',
      appointments: [],
      selectedAppointment: {},
      currentSort: 'name',
      currentSortDir: 'asc'
    }
  },
  methods: {
    moment,
    search() {

    },
    sort:function(s) {
      if(s === this.currentSort) {
        this.currentSortDir = this.currentSortDir==='asc'?'desc':'asc';
      }
      this.currentSort = s;
    },
    details(appointment) {
      this.selectedAppointment = appointment;
      document.getElementById('details').click();
    }
  },
  computed: {
    sortedAppointments:function() {
      return this.appointments.sort((a,b) => {
        let modifier = 1;
        if(this.currentSortDir === 'desc') modifier = -1;

        if(a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
        if(a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
        return 0;
      }).filter(appointment => {
        return appointment.patient.toLowerCase().includes(this.searchInput.toLowerCase())
      });
    }
  },
  mounted() {
    MedicalWorkerService.getDoneAppointments(this.$store.getters.getUser.id)
        .then((response) => {
          this.appointments = response.data;
        });
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