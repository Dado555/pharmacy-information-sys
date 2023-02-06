<template>
  <Navbar></Navbar>
  <div class="container">
    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Complaint id</span></th>
                <th><span>Complaint</span></th>
                <th><span>Patient</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="complaint in complaints" :key="complaint.id">
                <td><span>{{ complaint.id }}</span></td>
                <td><span>{{ complaint.complaintMessage }}</span></td>
                <td><span>{{ complaint.patientEmail}}</span></td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="modal-footer">
    <button class="btn btn-outline-primary" data-bs-target="#answer-modal" data-bs-toggle="modal">Make a response</button>
  </div>

  <div class="modal" id="answer-modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Answer</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="cancel" id="close"></button>
        </div>
        <div class="modal-body text-start">
          <form>
            <div class="form-group">
              <label class="col-form-label">Complaint id:</label>
              <input type="text" class="form-control" v-model="complaint.id" v-bind:class="{'is-invalid': invalidComplaintId}">
              <div class="invalid-feedback">
                Invalid complaint id.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Response:</label>
              <input type="text" class="form-control" v-model="complaint.complaintResponse" v-bind:class="{'is-invalid': invalidComplaintResponse}">
              <div class="invalid-feedback">
                Invalid response.
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" v-on:click="respond">Confirm</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" v-on:click="cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import Navbar from '@/components/Navbar.vue'
import SystemAdminService from "@/service/SystemAdminService";

export default {
  name: "AnswerComplaints",
  components: { Navbar },
  data() {
    return {
      complaints: [],
      complaint: {
        id: "",
        complaintResponse: ""
      },

      invalidComplaintId: false,
      invalidComplaintResponse: false
    }
  },
  computed: {
    user() {
      return this.$store.getters.getUser;
    }
  },
  mounted() {
    this.loadComplaints();
  },
  methods: {
    loadComplaints() {
      let data = this;
      SystemAdminService.getAllComplaints()
          .then((response) => {
            data.complaints = response.data;
          });

    },
    respond() {
      if (this.validate()) {
        SystemAdminService.updateComplaint(this.user.id, this.complaint)
            .then((response) => {
              this.$toast.show('Response successfully made!', {
                type: 'info',
                position: 'top',
                duration: 2000
              });
              this.loadComplaints();
            }).catch(response => {
          this.$toast.show("Invalid complaint id!",
              {
                type: 'error',
                duration: 2000,
                position: 'top'
              });
        });
        document.getElementById('close').click();
      }
    },
    cancel() {
      this.invalidComplaintId = false;
      this.invalidComplaintResponse = false;
    },
    validate() {
      this.invalidComplaintId = false;
      this.invalidComplaintResponse = false;

      if (this.complaint.id === "" || !this.complaint.id.match('^[0-9]*$')) {
        this.invalidComplaintId = true;
      }
      if (this.complaint.complaintResponse === "") {
        this.invalidComplaintResponse = true;
      }

      return !this.invalidOrderId && !this.invalidTotalPrice && !this.invalidDeliveryDeadline;
    }
  }
}
</script>

<style scoped>

.btn {
  margin: auto;
  display: block;
}

a {
  color: blue;
  text-decoration: underline;
}

</style>