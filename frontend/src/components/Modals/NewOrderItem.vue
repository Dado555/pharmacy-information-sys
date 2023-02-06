<template>
  <popup-modal ref="popup">
    <div class="container">
      <div class="row">
        <h2 style="margin-top: 0" class="col-md-11">Medicines</h2>
        <button type="button" style="color: white" class="btn btn-close col-md-1" @click="close">&times;</button>
      </div>
      <div class="row" style="overflow-y: scroll; max-height: 700px">
        <div class="col-lg-12">
          <div class="main-box clearfix">
            <div class="table-responsive">
              <table class="table user-list">
                <thead>
                <tr>
                  <th><span>Name/type</span></th>
                  <th><span>Form</span></th>
                  <th class="text-center"><span>Structure</span></th>
                  <th><span>Manufacture</span></th>
                  <th>&nbsp;</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="medicine in medicines" :key="medicine.id">
                  <td>
                    <img src="../../../public/medicine.png" alt="">
                    <a href="#" class="user-link" @click="showProfile(medicine)">{{ medicine.name }}</a>
                    <span class="user-subhead">{{ medicine.type }}</span>
                  </td>
                  <td>
                    {{ medicine.medicineForm }}
                  </td>
                  <td class="text-center">
                    <span class="label label-default">{{ medicine.structure }}</span>
                  </td>
                  <td class="text-center">
                    <span class="label label-default">{{ medicine.manufacture }}</span>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div v-if="showOne && medicineShow">
        <transition name="modal" id="modal">
          <div class="modal-mask">
            <div class="modal-wrapper">
              <div class="modal-container" style="width: 400px">
                <div class="modal-header">
                  <button type="button" class="btn btn-close" data-bs-dismiss="modal" @click="showOne=false"></button>
                </div>
                <div class="modal-body">
                  <div class="container">
                    <dl class="row" style="text-align: justify">
                      <dt class="col-sm-9">Medicine (type):</dt>
                      <dd class="col-sm-9">{{ medicineShow.name }} ( {{ medicineShow.type }} )</dd>

                      <dt class="col-sm-9">Form:</dt>
                      <dd class="col-sm-9">{{ medicineShow.medicineForm }}</dd>

                      <dt class="col-sm-9">Structure:</dt>
                      <dd class="col-sm-9">{{ medicineShow.structure }}</dd>

                      <dt class="col-sm-9">Issuing type: </dt>
                      <dd class="col-sm-9">{{ medicineShow.medicineIssuingType }}</dd>

                      <dt class="col-sm-9">Daily intake: </dt>
                      <dd class="col-sm-12">{{ medicineShow.dailyIntake }}</dd>

                      <dt class="col-sm-9">Contraindications: </dt>
                      <dd class="col-sm-9">{{ medicineShow.contraindications }}</dd>

                      <dt class="col-sm-9">Avarage rating: </dt>
                      <dd class="col-sm-9">{{ medicineShow.averageRating }}</dd>
                    </dl>
                    <span class="col-sm-9">AMOUNT: </span>
                    <input class="col-sm-9" type="number" v-model="itemDTO.amount">
                  </div>
                </div>
                <div class="container">
                  <button type="button" class="btn btn-primary mx-2" @click="addMedicineToOrder()">Add</button>
                  <button type="button" class="btn btn-outline-primary" @click="showOne=false">Cancel</button>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </popup-modal>
</template>

<script>
import PopupModal from "./PopupModal";
import MedicineService from "../../service/MedicineService";

export default {
  name: "NewOrderItem",
  components: {PopupModal},
  props: ['order'],
  emits: ['reloadItems', 'close'],
  data: function () {
    return {
      medicines: [],
      medicineShow: null,
      showOne: false,
      itemDTO: {
        medicineDTO: null,
        amount: -1
      }
    }
  },
  mounted() {
    this.loadMedicines();
    this.$refs.popup.open();
  },
  methods: {
    loadMedicines() {
      MedicineService.findAll()
        .then((response) => {
          this.medicines = response.data.medicines;
        });
    },
    close() {
      this.$emit('close');
      this.$refs.popup.close();
    },
    showProfile(medicine) {
      this.medicineShow = medicine;
      this.showOne = true;
    },
    toast(message) {
      this.$toast.show(message, {
        type: 'info',
        position: 'top',
        duration: 2000
      });
    },
    addMedicineToOrder() {
      this.itemDTO.medicineDTO = this.medicineShow;
      if (this.itemDTO.amount < 1)
        this.toast("Enter valid amount for medicine!");
      else if (this.itemDTO.medicineDTO === null)
        this.toast("Medicine not selected!");
      else {
        this.showOne = false;
        let index = -1;
        for(let i = 0; i < this.order.orderItems.length; i++) {
          if(this.order.orderItems[i].medicineDTO.id === this.itemDTO.medicineDTO.id)
            index = i;
        }
        if (index > -1) {
          this.order.orderItems[index].amount = parseInt(this.order.orderItems[index].amount) + parseInt(this.itemDTO.amount);
        }
        else
          this.order.orderItems.push(this.itemDTO);
        this.toast("Item successfully added!");
        this.close();
      }
    }
  }
}
</script>

<style scoped>

  /* USER LIST TABLE */
.user-list tbody td > img {
  position: relative;
  max-width: 50px;
  float: left;
  margin-right: 15px;
}
.user-list tbody td .user-link {
  display: block;
  font-size: 1.25em;
  padding-top: 3px;
  margin-left: 60px;
}
.user-list tbody td .user-subhead {
  font-size: 0.875em;
  font-style: italic;
}

/* TABLES */
.table {
  border-collapse: separate;
}
.table-hover > tbody > tr:hover > td,
.table-hover > tbody > tr:hover > th {
  background-color: #eee;
}
.table thead > tr > th {
  border-bottom: 1px solid #C2C2C2;
  padding-bottom: 0;
}
.table tbody > tr > td {
  font-size: 0.875em;
  background: #f5f5f5;
  border-top: 10px solid #fff;
  vertical-align: middle;
  padding: 12px 8px;
}
.table tbody > tr > td:first-child,
.table thead > tr > th:first-child {
  padding-left: 20px;
}
.table thead > tr > th span {
  border-bottom: 2px solid #C2C2C2;
  display: inline-block;
  padding: 0 5px;
  padding-bottom: 5px;
  font-weight: normal;
}
.table thead > tr > th > a span {
  color: #344644;
}
.table thead > tr > th > a span:after {
  content: "\f0dc";
  font-family: FontAwesome;
  font-style: normal;
  font-weight: normal;
  text-decoration: inherit;
  margin-left: 5px;
  font-size: 0.75em;
}
.table thead > tr > th > a.asc span:after {
  content: "\f0dd";
}
.table thead > tr > th > a.desc span:after {
  content: "\f0de";
}
.table thead > tr > th > a:hover span {
  text-decoration: none;
  color: #2bb6a3;
  border-color: #2bb6a3;
}
.table.table-hover tbody > tr > td {
  -webkit-transition: background-color 0.15s ease-in-out 0s;
  transition: background-color 0.15s ease-in-out 0s;
}
.table tbody tr td .call-type {
  display: block;
  font-size: 0.75em;
  text-align: center;
}
.table tbody tr td .first-line {
  line-height: 1.5;
  font-weight: 400;
  font-size: 1.125em;
}
.table tbody tr td .first-line span {
  font-size: 0.875em;
  color: #969696;
  font-weight: 300;
}
.table tbody tr td .second-line {
  font-size: 0.875em;
  line-height: 1.2;
}
.table a.table-link {
  margin: 0 5px;
  font-size: 1.125em;
}
.table a.table-link:hover {
  text-decoration: none;
  color: #2aa493;
}
.table a.table-link.danger {
  color: #fe635f;
}
.table a.table-link.danger:hover {
  color: #dd504c;
}

.table-products tbody > tr > td {
  background: none;
  border: none;
  border-bottom: 1px solid #ebebeb;
  -webkit-transition: background-color 0.15s ease-in-out 0s;
  transition: background-color 0.15s ease-in-out 0s;
  position: relative;
}
.table-products tbody > tr:hover > td {
  text-decoration: none;
  background-color: #f6f6f6;
}
.table-products .name {
  display: block;
  font-weight: 600;
  padding-bottom: 7px;
}
.table-products .price {
  display: block;
  text-decoration: none;
  width: 50%;
  float: left;
  font-size: 0.875em;
}
.table-products .price > i {
  color: #8dc859;
}
.table-products .warranty {
  display: block;
  text-decoration: none;
  width: 50%;
  float: left;
  font-size: 0.875em;
}
.table-products .warranty > i {
  color: #f1c40f;
}
.table tbody > tr.table-line-fb > td {
  background-color: #9daccb;
  color: #262525;
}
.table tbody > tr.table-line-twitter > td {
  background-color: #9fccff;
  color: #262525;
}
.table tbody > tr.table-line-plus > td {
  background-color: #eea59c;
  color: #262525;
}
.table-stats .status-social-icon {
  font-size: 1.9em;
  vertical-align: bottom;
}
.table-stats .table-line-fb .status-social-icon {
  color: #556484;
}
.table-stats .table-line-twitter .status-social-icon {
  color: #5885b8;
}
.table-stats .table-line-plus .status-social-icon {
  color: #a75d54;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
  height: 100%;
}

.modal-container {
  width: 70%;
  height: 80%;
  margin: 5% auto;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
  transition: all .3s ease;
  font-family: Helvetica, Arial, sans-serif;
}

.modal-body {
  position: relative;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow-y: auto;
  max-height: calc(100vh - 200px);
}

</style>