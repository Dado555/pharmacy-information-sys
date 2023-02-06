<template>
  <div class="card mb-3">
    <div class="card-body">
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">First Name</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.firstName }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Last Name</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.lastName }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Phone Number</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.phoneNumber }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Email</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.email }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Address</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.address.name }}, {{ $store.getters.getUser.address.number }}, {{ $store.getters.getUser.address.city }}
        </div>
      </div>
    </div>
  </div>

  <button id="btn-trigger" class="btn btn-outline-primary" type="button" data-bs-target="#time-off-request" data-bs-toggle="modal" hidden></button>
  <!-- Modal -->
  <div class="modal fade" id="time-off-request" tabindex="-1" aria-labelledby="time-off-request" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Create time off request</h5>
          <button type="button" id="time-off-request-close" v-on:click="close" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="container col-md-10 d-grid gap-2">
            <div class="form-floating">
              <input v-model="startDate" class="form-control" type="text" onfocus="(this.type='date')" placeholder="Start date:" aria-label="default input example">
              <label for="floatingInput">Start date:</label>
            </div>
            <div class="form-floating">
              <input v-model="endDate" class="form-control" type="text" onfocus="(this.type='date')" placeholder="End date:" aria-label="default input example">
              <label for="floatingInput">End date:</label>
            </div>
          </div>
          <textarea style="font-size: 17px" v-model="content" class="form-control my-3" rows="5" placeholder="Content"></textarea>
          <hr>
          <div class="container">
            <button type="button" class="btn btn-primary" v-on:click="send" v-bind:disabled="disabled"> Send </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MedicalWorkerService from '@/service/MedicalWorkerService.js'
export default {
  name: "TimeOffRequest",
  methods:
      {
        getConvertedDate(date)
        {
          return new Date(date).getTime();
        },
        close()
        {
          this.$router.replace('/medical-worker');
        },
        send()
        {
          if (this.startDate === '' || this.endDate === '' || this.getConvertedDate(this.startDate) < Date.now() + 86400000 || this.getConvertedDate(this.startDate) >= this.getConvertedDate(this.endDate)) {
            this.$toast.show('Invalid date and time.',
                {
                  type: 'error',
                  duration: 2000
                });
            return;
          }
          this.disabled = true;
          MedicalWorkerService.sendTimeOffRequest(this.getConvertedDate(this.startDate), this.getConvertedDate(this.endDate), this.content, this.$store.getters.getUser.id, this.$store.getters.getUser.role)
              .then((response) =>
              {
                if (response.data) {
                  this.$toast.show('Time off request sent!',
                      {
                        type: 'info',
                        duration: 2000
                      });
                  this.content = "";
                  this.startDate = "";
                  this.endDate = "";
                  document.getElementById('time-off-request-close').click();
                  this.$router.replace('/medical-worker');
                }
              });
        }
      },
  data() {
    return {
      content: "",
      startDate: "",
      endDate: "",
      disabled: false
    };
  },
  mounted() {
    document.getElementById('btn-trigger').click();
  }
}
</script>