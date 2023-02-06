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
                <th><span>Answer</span></th>
                <th><span>Patient</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="complaint in complaints" :key="complaint.id">
                <td><span>{{ complaint.id }}</span></td>
                <td><span>{{ complaint.complaintMessage }}</span></td>
                <td><span>{{ complaint.complaintResponse }}</span></td>
                <td><span>{{ complaint.patientEmail}}</span></td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from '@/components/Navbar.vue'
import SystemAdminService from "@/service/SystemAdminService";

export default {
  name: "Complaints",
  components: { Navbar },
  data() {
    return {
      complaints: []
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
      SystemAdminService.getComplaints(this.user.id)
          .then((response) => {
            data.complaints = response.data;
          });

    }
  }
}
</script>

<style scoped>

</style>