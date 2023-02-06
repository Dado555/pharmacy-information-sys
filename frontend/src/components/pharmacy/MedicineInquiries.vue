<template>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

  <div class="container">
    <br><br><br>

    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Medicine</span></th>
                <th><span>Date created</span></th>
                <th><span>Resolved</span></th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="inq in inquiries" :key="inq.id">
                <td> {{ inq.medicineId }} </td>
                <td>{{ new Date(inq.dateCreated).toLocaleString() }}</td>
                <td>{{ inq.resolved }}</td>
                <td>
                  <button v-if="inq.resolved !== true" class="btn btn-primary" type="button" @click="resolveInq(inq.id)">
                    Resolve
                  </button>
                </td>
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
import PharmacyService from "../../service/PharmacyService";

export default {
  name: "MedicineInquiries",
  data() {
    return {
      inquiries: []
    }
  },
  mounted() {
    this.loadInquiries();
  },
  methods: {
    loadInquiries() {
      PharmacyService.getMedicineInquiries(this.$route.query.id)
      .then((response) => {
        this.inquiries = response.data;
      });
    },
    toast(message, len) {
      this.$toast.show(message, {
        type: 'info',
        position: 'top',
        duration: len
      });
    },
    resolveInq(inqId) {
      PharmacyService.resolveMedicineInquiry(inqId)
      .then((response) => {
        this.loadInquiries();
        this.toast("Medicine inquiry resolved!", 2000);
      });
    }
  }
}
</script>

<style scoped>

</style>