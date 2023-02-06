<template>
  <popup-modal ref="popup">
    <div class="container">
      <div class="row">
        <h2 style="margin-top: 0" class="col-md-11">Pharmacy data change</h2>
        <button type="button" style="color: white" class="btn btn-close col-md-1" @click="close">&times;</button>
      </div>
      <div class="row">
        <div class="col-md-6">
          <OpenLayersMap v-if="pharmacy" :lon="pharmacy.address.longitude" :lat="pharmacy.address.latitude"
                         editMode="m2" v-on:newAddress="setAddress" ></OpenLayersMap>
        </div>
        <br />
        <div class="col-md-6">
          <div class="form-group">
            <label for="pharmacy-name">Pharmacy name</label>
            <input type="text" class="form-control" id="pharmacy-name" placeholder="Name" v-model="name">
          </div>
          <br>
          <button class="btn btn-default" @click="changeName()">Change name</button>
          <br>
          <div class="my-form">
            <div class="form-group">
              <label for="form-location">Location</label>
              <input type="text" class="form-control" id="form-location" placeholder="Location" v-model="location" readonly>
            </div>
            <div class="form-group">
              <div class="row">
                <div class="col-md-6">
                  <label for="form-longitude">Longitude</label>
                  <input type="text" class="form-control" id="form-longitude" placeholder="Longitude" v-model="addressEdit.longitude" readonly>
                </div>
                <div class="col-md-6">
                  <label for="form-latitude">Longitude</label>
                  <input type="text" class="form-control" id="form-latitude" placeholder="Latitude" v-model="addressEdit.latitude" readonly>
                </div>
              </div>
            </div>
            <br>
            <button class="btn btn-default" @click="changeAddress()">Change address</button>
          </div>
        </div>
      </div>
    </div>
  </popup-modal>
</template>

<script>
import PopupModal from "./PopupModal";
import OpenLayersMap from "../pharmacy/OpenLayersMap";
import PharmacyService from "../../service/PharmacyService";

export default {
  name: "ChangePharmacyData",
  props: ['pharmacy'],
  components: {PopupModal, OpenLayersMap},
  emits: ['notify', 'close'],
  data() {
    return {
      location: this.pharmacy.address.name + " " + this.pharmacy.address.number +
          " , " + this.pharmacy.address.city + " " + this.pharmacy.address.postalCode,
      name: this.pharmacy.name,
      addressEdit: {
        id: -1,
        name: this.pharmacy.address.name,
        number: this.pharmacy.address.number,
        city: this.pharmacy.address.city,
        postalCode: this.pharmacy.address.postalCode,
        longitude: this.pharmacy.address.longitude,
        latitude: this.pharmacy.address.latitude
      }
    }
  },
  mounted() {
    this.show();
  },
  methods: {
    show() {
      this.$refs.popup.open();
    },
    close() {
      this.$emit('close');
      this.$refs.popup.close();
    },
    changeName() {
      PharmacyService.changePharmacyName(this.pharmacy.id, this.name)
      .then((response) => {
        this.$emit('notify');
        this.$toast.show('Pharmacy name \''+ this.pharmacy.name + '\' successfully changed to \''+ this.name + '\'', {
          type: 'info',
          position: 'top',
          duration: 5000
        });
      });
    },
    changeAddress() {
      PharmacyService.changePharmacyAddress(this.pharmacy.id, this.addressEdit)
      .then((response) => {
        this.$emit('notify');
        this.$toast.show('Pharmacy address successfully changed!', {
          type: 'info',
          position: 'top',
          duration: 3000
        });
      });
    },
    setAddress(street, number, city, country, postalCode, longitude, latitude) {
      console.log(street + " " + number + " , " + city + " (" + country + ") " + postalCode);
      this.addressEdit.street = street;
      this.addressEdit.number = number;
      this.addressEdit.city = city;
      this.addressEdit.postalCode = postalCode;
      this.addressEdit.longitude = longitude;
      this.addressEdit.latitude = latitude;
      this.location = this.addressEdit.street + " " + this.addressEdit.number + " , " +
          this.addressEdit.city + " " + this.addressEdit.postalCode;
    }
  }
}
</script>

<style scoped>
.my-form {
  color: #305896;
}
.btn-default, .row .close {
  background-color: #305896;
  color: #fff;
  border-radius: 0;
}
.btn-default:hover, .row .close:hover {
  background-color: #4498C6;
  color: #fff;
}
.my-form .form-control {
  border-radius: 0;
}
</style>