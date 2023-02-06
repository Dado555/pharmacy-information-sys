<template>
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">

  <div class="container">

    <div class="d-flex justify-content-between">
      <div class="sort-filter">
        <h3>Filter by: </h3>
        <select class="search-select" @change=filterOrderOffers($event)>
          <option value="">Select one</option>
          <option value="APPROVED">APPROVED </option>
          <option value="UNKNOWN">UNKNOWN </option>
          <option value="REJECTED">REJECTED </option>
        </select>
      </div>
    </div><br>

    <div class="row">
      <div class="col-lg-12">
        <div class="main-box clearfix">
          <div class="table-responsive">
            <table class="table user-list">
              <thead>
              <tr>
                <th><span>Order offer id</span></th>
                <th><span>Status</span></th>
                <th><span>Delivery deadline</span></th>
                <th class="text-center"><span>Total price</span></th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="orderOffer in orderOffers" :key="orderOffer.id">
                <td><span>{{ orderOffer.id }}</span></td>
                <td><span>{{ orderOffer.orderOfferStatus }}</span></td>
                <td>{{ new Date(orderOffer.deliveryDeadline).toLocaleDateString('en-GB') }}</td>
                <td class="text-center">
                  <span class="label label-default">{{ orderOffer.totalPrice }}</span>
                </td>
                <td style="width: 20%;">
                  <a href="#" class="table-link" data-bs-target="#edit-modal-orderOffer" data-bs-toggle="modal" v-on:click="edit(orderOffer)">
                    <span class="fa-stack">
                      <i class="fa fa-square fa-stack-2x"></i>
                      <i class="fa fa-pencil fa-stack-1x fa-inverse"></i>
                    </span>
                  </a>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div class="modal" id="edit-modal-orderOffer">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Update order offer data</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="cancel" id="close-modal-update"></button>
          </div>
          <div class="modal-body text-start">
            <form>
              <div class="form-group">
                <label class="col-form-label">Order offer id:</label>
                <input type="text" class="form-control" readonly="readonly" v-model="updated.id">
              </div>
              <div class="form-group">
                <label class="col-form-label">Status:</label>
                <input type="text" class="form-control" readonly="readonly" v-model="updated.status">
              </div>
              <div class="form-group">
                <label class="col-form-label">Delivery deadline:</label>
                <input type="date" class="form-control" v-model="updated.deliveryDeadline" v-bind:class="{'is-invalid': invalidDeliveryDeadline}">
                <div class="invalid-feedback">
                  Invalid delivery deadline.
                </div>
              </div>
              <div class="form-group">
                <label class="col-form-label">Total price:</label>
                <input type="text" class="form-control" v-model="updated.totalPrice" v-bind:class="{'is-invalid': invalidTotalPrice}">
                <div class="invalid-feedback">
                  Invalid total price.
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" v-on:click="saveChanges">Save changes</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" v-on:click="cancel">Cancel</button>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import SupplierService from "@/service/SupplierService";

export default {
  name: "SupplierOrderOffers",
  data() {
    return {
      orderOffers: [],
      searchParameters: {
        filterBy: ""
      },
      updated: {
        id: "",
        status: "",
        totalPrice: "",
        deliveryDeadline: ""
      },
      invalidDeliveryDeadline: false,
      invalidTotalPrice: false
    }
  },
  computed: {
    user() {
      return this.$store.getters.getUser;
    }
  },
  mounted() {
    this.loadOrderOffers();
  },
  methods: {
    loadOrderOffers() {
      let data = this;
      SupplierService.orderOffers(this.user.id)
      .then((response) => {
        data.orderOffers = response.data;
      });
    },
    searchByCriteria() {
      let data = this;
      SupplierService.searchByCriteria(this.searchParameters)
          .then((response) => {
            data.orderOffers = response.data;
          });
    },
    filterOrderOffers(event) {
      this.searchParameters.filterBy = event.target.value;
      this.searchByCriteria();
    },
    saveChanges() {
      if (this.validate()) {
        this.updated.deliveryDeadline = Date.parse(this.updated.deliveryDeadline);
        SupplierService.updateOrderOffer(this.updated)
        .then((response) => {
          if (response.data === true) {
            this.loadOrderOffers();
            document.getElementById('close-modal-update').click();
            this.$toast.show('Data successfully updated!', {
              type: 'info',
              position: 'top',
              duration: 2000
            });
          } else {
            this.$toast.show('This order offer cannot be edited due to deadline!', {
              type: 'error',
              position: 'top',
              duration: 2000
            });
          }
        });
      }
    },
    cancel() {
      this.invalidDeliveryDeadline = false;
      this.invalidTotalPrice = false;
    },
    edit(orderOffer) {
      this.updated = {id: orderOffer.id, status: orderOffer.orderOfferStatus, deliveryDeadline: orderOffer.deliveryDeadline, totalPrice: orderOffer.totalPrice};
    },
    validate() {
      this.invalidDeliveryDeadline = false;
      this.invalidTotalPrice = false;

      if (this.updated.totalPrice === undefined) {
        this.invalidTotalPrice = true;
      }
      if (this.updated.deliveryDeadline === undefined) {
        this.invalidDeliveryDeadline = true;
      }

      return !this.invalidTotalPrice && !this.invalidDeliveryDeadline;
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

</style>