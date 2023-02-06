<template>
  <popup-modal ref="popup">
    <h2 style="margin-top: 0">{{ title }}</h2>
    <div class="buttons-change-chart">
      <button class="btn btn-secondary" @click="monthlyView">Monthly</button>
      <span class="btn btn-secondary" @click="yearlyView">Yearly</span>
    </div>
    <div class="container">
      <vue3-chart-js
          :id="chart.id"
          ref="chartRef"
          :type="chart.type"
          :data="chart.data"
          :key="componentKey"
      ></vue3-chart-js>
    </div>
    <div class="buttons-ok-cancel">
      <button class="btn btn-primary" @click="close">OK</button>
      <span class="btn btn-secondary" @click="close">Cancel</span>
    </div>
  </popup-modal>
</template>
<script>
import Vue3ChartJs from '@j-t-mcc/vue3-chartjs'
import PopupModal from "./PopupModal";
import MedicalWorkerService from "../../service/MedicalWorkerService";
import MedicineService from "../../service/MedicineService";
import PharmacyService from "../../service/PharmacyService";
export default {
  props: ['type', 'dermatologist', 'pharmacist', 'medicine', 'pharmacy'],
  name: "StatsChart",
  components: {PopupModal, Vue3ChartJs},
  data() {
    return {
      componentKey: 0,
      title: "",
      chartData: [],
      monthly_yearly: "m",
      colours: [
        '#b80606',
        '#fa6262',
        '#ffcecb',
        '#73fafa',
        '#06b8b8'
      ],
      labels: [
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec",
        "end"
      ],
      chart: {
        id: 'barChart',
        type: 'line', // line, bar, radar, doughnut, pie
        data: {
          labels: [],
          datasets: []
        }
      },
    }
  },
  created() {
    this.set("Monthly");
  },
  mounted() {
    this.show();
    this.updateChart();
  },
  methods: {
    set(type) {
      if (this.type === "dermatologist") {
        this.title = type + " appointments count for " + this.dermatologist.firstName;
        if(this.monthly_yearly === "m")
          this.getDermatologistAppointmentsMonthly(this.dermatologist.id);
        else
          this.getDermatologistAppointmentsYearly(this.dermatologist.id);
      } else if(this.type === "pharmacist"){
        this.title = type + " appointments count for " + this.pharmacist.firstName;
        if(this.monthly_yearly === "m")
          this.getPharmacistAppointmentsMonthly(this.pharmacist.id);
        else
          this.getPharmacistAppointmentsYearly(this.pharmacist.id);
      } else if(this.type === "medicine") {
        this.title = type + " sold count for " + this.medicine.medicineDTO.name;
        if(this.monthly_yearly === "m")
          this.getMedicineSoldMonthly(this.medicine.id);
        else
          this.getMedicineSoldYearly(this.medicine.id);
      } else if(this.type === "earnings") {
        this.title = type + " earnings for " + this.pharmacy.name;
        if(this.monthly_yearly === "m")
          this.getPharmacyEarningsMonthly(this.pharmacy.id);
        else
          this.getPharmacyEarningsYearly(this.pharmacy.id);
      }
    },
    forceRerender() {
      this.componentKey += 1;
    },
    show() {
      this.$refs.popup.open();
      this.showChart = true;
    },
    close() {
      this.$emit('close');
      this.$refs.popup.close();
    },
    monthlyView() {
      this.monthly_yearly = "m";
      this.set("Monthly");
    },
    yearlyView() {
      this.monthly_yearly = "y";
      this.set("Yearly");
    },
    updateChart() {
      let colours = [];
      for(let i = 0; i < this.chartData.length; i++){
        if(this.chartData[i] <= 5)
          colours.push(this.colours[Math.round(this.chartData[i])-1]);
        else
          colours.push(this.colours[4]);
      }
      console.log("BOJE: " + colours);
      this.chart.data.datasets = [
        {
          data: this.chartData,
          backgroundColor: colours
        }
      ]
      let d = new Date();
      if(this.monthly_yearly === "m") {
        let month = d.getMonth();
        let arr = [];
        if (month === 11) {
          arr = this.labels.slice(0, 12);
        } else {
          arr = this.labels.slice(month + 1, 12);
          arr = arr.concat(this.labels.slice(0, month + 1));
        }
        console.log("Za mjesece: " + arr);
        this.chart.data.labels = arr;
      } else {
        let year = d.getFullYear();
        let arr = [];
        for(let i = year - 9; i <= year; i++) {
          arr.push(i.toString());
        }
        console.log("Za godine: " + arr);
        this.chart.data.labels = arr;
      }
    },
    getDermatologistAppointmentsMonthly(dermatologistId) {
      MedicalWorkerService.workerAppointmentsStats('dermatologist', dermatologistId, 'monthly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getDermatologistAppointmentsYearly(dermatologistId) {
      MedicalWorkerService.workerAppointmentsStats('dermatologist', dermatologistId, 'yearly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacistAppointmentsMonthly(pharmacistId) {
      MedicalWorkerService.workerAppointmentsStats('pharmacist', pharmacistId, 'monthly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacistAppointmentsYearly(pharmacistId) {
      MedicalWorkerService.workerAppointmentsStats('pharmacist', pharmacistId, 'yearly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getMedicineSoldMonthly(medId) {
      MedicineService.medicineSoldStats(medId, 'monthly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getMedicineSoldYearly(medId) {
      MedicineService.medicineSoldStats(medId, 'yearly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacyEarningsMonthly(pharmacyId) {
      PharmacyService.getEarningsStats(pharmacyId, 'monthly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacyEarningsYearly(pharmacyId) {
      PharmacyService.getEarningsStats(pharmacyId, 'yearly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    }
  }
}
</script>
<style scoped>
.buttons-change-chart, .buttons-ok-cancel {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
</style>