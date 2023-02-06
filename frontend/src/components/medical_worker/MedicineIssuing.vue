<template>
  <div class="card mb-3">
    <div class="card-body">

      <div class="row">
        <div class="col-lg-3"></div>
        <div class="col-lg-6">
          <div class="input-group mb-4">
            <input v-model="searchInput"  type="text" class="form-control" placeholder="Reservation number..." aria-label="Reservation number" aria-describedby="basic-addon2">
            <div class="input-group-append">
              <button class="btn btn-primary" type="button" v-on:click="search">Search</button>
            </div>
          </div><!-- /input-group -->
        </div><!-- /.col-lg-4 -->
        <div class="col-lg-3"></div>
      </div><!-- /.row -->
      <div style="font-size: 45px; color: #DFDFDF" v-show="reservations.length == 0" class="mt-3"> No results.</div>
      <div style="border: 1px solid #ddd" v-show="reservations.length">
        <table class="table table-hover table-borderless">

          <thead style="border-bottom: 3px solid #DFDFDF; font-weight: 600">
          <tr style="font-size: 17px">
            <td style="width: 5%" > </td>
            <td style="width: 10%">Reservation </td>
            <td style="width: 10%">  Patient  </td>
            <td style="width: 20%">  Medicine </td>
            <td style="width: 10%"> Amount </td>
            <td style="width: 10%"> Price </td>
            <td style="width: 20%"> Date </td>
            <td style="width: 15%"> </td>
          </tr>
          </thead>

          <tbody style="font-size: 15px">
          <tr v-for="reservation in reservations" style="border-bottom: 1px solid #ddd; word-break: break-all">
            <td>  <img style="border: 1px solid #ddd;width: 70px" src="../../assets/reservation.png"/></td>
            <td>{{ reservation.reservationId }}</td>
            <td>{{ reservation.patientName }}</td>
            <td>{{ reservation.pharmacyMedicineDTO.medicineDTO.name }}</td>
            <td>{{ reservation.amount }}</td>
            <td>{{ reservation.price }}</td>
            <td>{{ moment(reservation.dateAndTime).format("DD.MM.YYYY.") }}</td>

            <th>
              <button class="btn btn-primary" type="button" v-on:click="confirm(reservation)">Confirm</button>
            </th>

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
  name: "MedicineIssuing",
  data() {
    return {
      searchInput: '',
      reservations: []
    }
  },
  methods: {
    moment,
    search() {

      MedicalWorkerService.getReservation(this.searchInput, this.$store.getters.getUser.id)
          .then((response) =>
          {
            this.reservations = response.data;
            if (response.data && response.data.length > 0) {
            this.reservations = response.data;
            } else {
              this.$toast.show('Invalid reservation number.',
                  {
                    position: 'top',
                    type: 'error',
                    duration: 2000
                  });
            }
          }).catch(err => {
            this.reservations = [];
        this.$toast.show('Invalid reservation number.',
            {
              position: 'top',
              type: 'error',
              duration: 2000
            });
      });
    },
    confirm(reservation) {
      MedicalWorkerService.confirmReservation(reservation)
          .then((response) =>
          {
            if (response.data) {
              this.$toast.show('Medicine issued successfully!',
                  {
                    type: 'info',
                    duration: 2000
                  });
              this.reservations = this.reservations.filter(function (item){
                return item !== reservation;
              });
            }
          }).catch(err => {
        this.reservations = [];
        this.$toast.show(err.response.data.message,
            {
              position: 'top',
              type: 'error',
              duration: 2000
            });
      });
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