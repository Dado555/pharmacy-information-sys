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
                <th><span>Order id</span></th>
                <th><span>Deadline</span></th>
                <th><span>Created date</span></th>
                <th><span>Pharmacy</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="order in orders" :key="order.id">
                <td><span>{{ order.id }}</span></td>
                <td>{{ new Date(order.deadline).toLocaleDateString('en-GB') }}</td>
                <td>{{ new Date(order.createdDate).toLocaleDateString('en-GB') }}</td>
                <td><span>{{ order.pharmacyId }}</span></td>
                <td><span>{{  }}</span></td>
                <td>
                  <button @click="showOrder=order; showItems=true;" type="button" class="btn btn-primary">Show items</button>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="modal-footer">
    <button class="btn btn-outline-primary" data-bs-target="#make-modal" data-bs-toggle="modal">Make an offer</button>
  </div>

  <OrderItems v-if="showItems" :orderId="showOrder.id" v-on:close="showItems=false"></OrderItems>

  <div class="modal" id="make-modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Make an offer</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="cancel" id="close"></button>
        </div>
        <div class="modal-body text-start">
          <form>
            <div class="form-group">
              <label class="col-form-label">Order id:</label>
              <input type="text" class="form-control" v-model="orderOffer.order.id" v-bind:class="{'is-invalid': invalidOrderId}">
              <div class="invalid-feedback">
                Invalid order id.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Total price:</label>
              <input type="text" class="form-control" v-model="orderOffer.totalPrice" v-bind:class="{'is-invalid': invalidTotalPrice}">
              <div class="invalid-feedback">
                Invalid total price.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Delivery deadline:</label>
              <input type="date" class="form-control" v-model="orderOffer.deliveryDeadline" v-bind:class="{'is-invalid': invalidDeliveryDeadline}">
              <div class="invalid-feedback">
                Invalid delivery deadline.
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" v-on:click="makeOffer">Confirm</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" v-on:click="cancel">Cancel</button>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import Navbar from '@/components/Navbar.vue'
import OrderItems from "@/components/Modals/OrderItems";
import SupplierService from "@/service/SupplierService";

export default {
  name: "Orders",
  components: { Navbar, OrderItems },
  data() {
    return {
      orders: [],
      orderOffer: {
        order: {},
        totalPrice: "",
        deliveryDeadline: ""
      },
      invalidOrderId: false,
      invalidTotalPrice: false,
      invalidDeliveryDeadline: false,
      showItems: false,
      showOrder: null
    }
  },
  computed: {
    user() {
      return this.$store.getters.getUser;
    }
  },
  mounted() {
    this.loadOrders();
  },
  methods: {
    loadOrders() {
      let data = this;
      SupplierService.orders()
          .then((response) => {
            data.orders = response.data;
          });

    },
    makeOffer(event) {
      if (this.validate()) {
        this.orderOffer.deliveryDeadline = Date.parse(this.orderOffer.deliveryDeadline);
        SupplierService.makeOrderOffer(this.user.id, this.orderOffer)
            .then(response => {
              this.$toast.show('Order offer successfully made!', {
                type: 'info',
                position: 'top',
                duration: 2000
              });
            }).catch(response => {
          this.$toast.show('Invalid order id!', {
            type: 'error',
            position: 'top',
            duration: 2000
          });
        });
        document.getElementById('close').click();
      }
    },
    cancel() {
      this.invalidOrderId = false;
      this.invalidTotalPrice = false;
      this.invalidDeliveryDeadline = false;
    },
    validate() {
      this.invalidOrderId = false;
      this.invalidTotalPrice = false;
      this.invalidDeliveryDeadline = false;

      if (this.orderOffer.order.id === undefined || !this.orderOffer.order.id.match('^[0-9]*$')) {
        this.invalidOrderId = true;
      }
      if (this.orderOffer.totalPrice === "" || !this.orderOffer.totalPrice.match('^[0-9]*$')) {
        this.invalidTotalPrice = true;
      }
      if (this.orderOffer.deliveryDeadline === "") {
        this.invalidDeliveryDeadline = true;
      }

      return !this.invalidOrderId && !this.invalidTotalPrice && !this.invalidDeliveryDeadline;

    }
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

.btn {
  margin: auto;
  display: block;
}

a {
  color: blue;
  text-decoration: underline;
}

</style>