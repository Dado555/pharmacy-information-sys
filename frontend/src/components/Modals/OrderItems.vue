<template>
  <popup-modal ref="popup">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

    <div v-if="order" class="container">
      <div class="row">
        <h2 style="margin-top: 0" class="col-md-11">Order items list</h2>
        <button type="button" style="color: white" class="btn btn-close col-md-1" @click="close">&times;</button>
      </div>
      <div v-if="adminAndOrderStatus(order)" class="ui-button">
        <button class="btn btn-primary" @click="addItem=true" style="margin-left: 10px">Add new item</button>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <div class="main-box clearfix">
            <div class="table-responsive">
              <table class="table user-list">
                <thead>
                <tr>
                  <th><span>Name</span></th>
                  <th><span>Type</span></th>
                  <th><span>Form</span></th>
                  <th><span>Manufacturer</span></th>
                  <th><span>Amount</span></th>
                  <th v-if="adminAndOrderStatus(order)"><span>NEW AMOUNT</span></th>
                  <th v-if="adminAndOrderStatus(order)"></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in order.orderItems">
                  <td>{{ item.medicineDTO.name }}</td>
                  <td>{{ item.medicineDTO.type }}</td>
                  <td>{{ item.medicineDTO.medicineForm }}</td>
                  <td>{{ item.medicineDTO.manufacture }}</td>
                  <td>{{ item.amount }}</td>
                  <td v-if="adminAndOrderStatus(order)"><input type="number" min="1" v-model="newAmount" :readonly="canEdit(order.pharmacyAdminId)"></td>
                  <td v-if="adminAndOrderStatus(order)">
                    <button class="btn btn-primary" @click="saveChanges(item)">Save</button>
                    <button class="btn btn-primary" @click="removeItem(item)"
                            style="margin-left: 10px; background-color: red; border-color: red">Remove</button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </popup-modal>
  <AddOrderItem v-if="addItem" v-on:close="addItem=false" v-on:reloadItems="loadOrder()" :order="order"></AddOrderItem>
</template>

<script>
import PopupModal from "./PopupModal";
import {mapState} from "vuex";
import AddOrderItem from "./AddOrderItem";
import PharmacyService from "../../service/PharmacyService";
export default {
  name: "OrderItems",
  props: ['orderId'],
  emits: ['close'],
  components: {AddOrderItem, PopupModal},
  data() {
    return {
      addItem: false,
      order: null,
      newAmount: 0
    }
  },
  mounted() {
    this.loadOrder();
    this.$refs.popup.open();
  },
  methods: {
    loadOrder() {
      if (this.$route.query.id === undefined) {
        this.$route.query.id = '0';
      }
      PharmacyService.getOneOrder(this.$route.query.id, this.orderId)
        .then((response) => {
          this.order = response.data;
        });
    },
    close() {
      this.$emit('close');
      this.$refs.popup.close();
    },
    canEdit(adminId) {
      return adminId !== this.userId;
    },
    adminAndOrderStatus(order) {
      return this.userId === order.pharmacyAdminId && order.orderStatus === 'IN_PROGRESS';
    },
    toast(message) {
      this.$toast.show(message, {
        type: 'info',
        position: 'top',
        duration: 4000
      });
    },
    saveChanges(item) {
      console.log(item.amount);
      console.log(this.newAmount);
      if(parseInt(item.amount) === parseInt(this.newAmount))
        this.toast("No changes made!");
      else if (this.newAmount < 1)
        this.toast("Enter valid amount!")
      else
        {
          item.amount = parseInt(this.newAmount);
          this.updateItem(item);
        }
    },
    updateItem(item) {
      PharmacyService.updateOrderItem(this.order.id, item.id, item)
        .then((response) => {
          this.loadOrder();
          if(response.data) {
            this.toast("Item updated successfully!");
          } else {
            this.toast("Can't update items, because this order have offers!");
          }
        });
    },
    removeItem(item) {
      PharmacyService.deleteOrderItem(this.order.id, item.id)
        .then((response) => {
          if(response.data) {
            this.loadOrder();
            this.toast("Item removed successfully!");
          } else {
            this.toast("Can't remove items, because this order have offers!");
          }
        });
    }
  },
  computed: {
    ...mapState({
      userId: state => state.user.id
    })
  }
}
</script>

<style scoped>
body{margin-top:20px;}

.container {
  margin-top: 1%;
}


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
.sort-filter {
  text-align: left;
}

.ui-button {
  text-align: right;
}
</style>