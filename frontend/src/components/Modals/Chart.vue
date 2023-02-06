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
import PharmacyService from "../../service/PharmacyService";
import MedicalWorkerService from "../../service/MedicalWorkerService";

export default {
  props: ['type', 'pharmacy', 'dermatologist', 'pharmacist'],
  name: "Chart",
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
      if(this.type === "pharmacy"){
        this.title = type + " rating report for " + this.pharmacy.name;
        if(this.monthly_yearly === "m")
          this.getPharmacyRatingsMonthly(this.pharmacy.id);
        else
          this.getPharmacyRatingsYearly(this.pharmacy.id);
      } else if (this.type === "dermatologist") {
        this.title = type + " rating report for " + this.dermatologist.firstName;
        if(this.monthly_yearly === "m")
          this.getDermatologistRatingsMonthly(this.dermatologist.id);
        else
          this.getDermatologistRatingsYearly(this.dermatologist.id);
      } else {
        this.title = type + " rating report for " + this.pharmacist.firstName;
        if(this.monthly_yearly === "m")
          this.getPharmacistRatingsMonthly(this.pharmacist.id);
        else
          this.getPharmacistRatingsYearly(this.pharmacist.id);
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
        colours.push(this.colours[Math.round(this.chartData[i])-1]);
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
    getPharmacyRatingsMonthly(pharmacyId) {
      PharmacyService.pharmacyMonthlyRating(pharmacyId)
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacyRatingsYearly(pharmacyId) {
      PharmacyService.pharmacyYearlyRating(pharmacyId)
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getDermatologistRatingsMonthly(dermatologistId) {
      MedicalWorkerService.workerRatings('dermatologist', dermatologistId, 'monthly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getDermatologistRatingsYearly(dermatologistId) {
      MedicalWorkerService.workerRatings('dermatologist', dermatologistId, 'yearly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacistRatingsMonthly(pharmacistId) {
      MedicalWorkerService.workerRatings('pharmacist', pharmacistId, 'monthly')
        .then((response) => {
          this.chartData = response.data;
          console.log(response.data);
          this.updateChart();
          this.forceRerender();
        });
    },
    getPharmacistRatingsYearly(pharmacistId) {
      MedicalWorkerService.workerRatings('pharmacist', pharmacistId, 'yearly')
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