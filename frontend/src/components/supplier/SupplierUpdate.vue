<template>
  <div class="card">
    <div class="card-body">
      <div class="d-flex flex-column align-items-center text-center">
        <div class="profile-img">
          <img src="../../assets/medical-worker-profile-pic.png" class="rounded-circle" width="170"/>
          <div class="file btn btn-lg btn-primary">
            Change Photo
            <input type="file" name="file"/>
          </div>
        </div>
        <div class="mt-3">
          <h4>{{ $store.getters.getUser.firstName }} {{ $store.getters.getUser.lastName }}</h4>
          <p class="text-secondary mb-1">{{ $store.getters.getUser.role }}</p>
          <p class="text-muted font-size-sm">{{ $store.getters.getUser.email }}</p>
          <button class="btn btn-outline-primary mx-2" data-bs-target="#edit-modal-supplier" data-bs-toggle="modal" v-on:click="edit">Edit</button>
          <button class="btn btn-outline-primary" data-bs-target="#change-password-modal-supplier" data-bs-toggle="modal">Change password</button>
        </div>
      </div>
    </div>
  </div>

  <div class="modal" id="edit-modal-supplier">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Update your data</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" v-on:click="cancel" id="close-modal-supplier"></button>
        </div>
        <div class="modal-body text-start">
          <form>
            <div class="form-group">
              <label class="col-form-label">First Name:</label>
              <input type="text" class="form-control" v-model="updated.firstName" v-bind:class="{'is-invalid': invalidFirstName}">
              <div class="invalid-feedback">
                Invalid first name.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Last Name:</label>
              <input type="text" class="form-control" v-model="updated.lastName" v-bind:class="{'is-invalid': invalidLastName}">
              <div class="invalid-feedback">
                Invalid last name.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Phone Number:</label>
              <input type="text" class="form-control" v-model="updated.phoneNumber" v-bind:class="{'is-invalid': invalidPhoneNumber}">
              <div class="invalid-feedback">
                Invalid phone number.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">City:</label>
              <input type="text" class="form-control" v-model="updated.address.city" v-bind:class="{'is-invalid': invalidAddressCity}">
              <div class="invalid-feedback">
                Invalid city.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Street:</label>
              <input type="text" class="form-control" v-model="updated.address.name" v-bind:class="{'is-invalid': invalidAddressStreet}">
              <div class="invalid-feedback">
                Invalid street.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Number:</label>
              <input type="text" class="form-control" v-model="updated.address.number" v-bind:class="{'is-invalid': invalidAddressNumber}">
              <div class="invalid-feedback">
                Invalid number.
              </div>
            </div>
            <div class="form-group">
              <label class="col-form-label">Postal code:</label>
              <input type="text" class="form-control" v-model="updated.address.postalCode" v-bind:class="{'is-invalid': invalidAddressPostalCode}">
              <div class="invalid-feedback">
                Invalid postal code.
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

  <div class="modal" id="change-password-modal-supplier">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Change password</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body text-start">
          <form>
            <div class="form-group">
              <label class="col-form-label">Old password:</label>
              <input type="password" class="form-control" placeholder="Old password">
            </div>
            <div class="form-group">
              <label class="col-form-label">New password:</label>
              <input type="password" class="form-control" placeholder="New password">
            </div>
            <div class="form-group">
              <label class="col-form-label">Confirm new password:</label>
              <input type="password" class="form-control" placeholder="Confirm new password">
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary">Save changes</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SupplierService from "@/service/SupplierService";

export default {
  name: "SupplierUpdate",
  methods: {
    saveChanges(event) {
      if (this.validate()) {
        SupplierService.updateSupplier(this.updated.id, this.updated)
        .then((response) => {
          response.data.role = this.updated.role;
          this.$store.commit('updateUser', response.data);
        });
        document.getElementById('close-modal-supplier').click();
        this.$toast.show('Data successfully updated!', {
          type: 'info',
          position: 'top',
          duration: 2000
        });
      }
    },
    cancel() {
      this.updated = JSON.parse(JSON.stringify(this.$store.getters.getUser));
      this.invalidFirstName = false;
      this.invalidLastName = false;
      this.invalidPhoneNumber = false;
      this.invalidAddressCity = false;
      this.invalidAddressName = false;
      this.invalidAddressNumber = false;
      this.invalidAddressPostalCode = false;
    },
    validate() {
      let current = this.$store.getters.getUser;
      if (this.updated.firstName === current.firstName && this.updated.lastName === current.lastName &&
          this.updated.phoneNumber === current.phoneNumber && this.updated.address.city === current.address.city &&
          this.updated.address.name === current.address.name && this.updated.address.number === current.address.number
          && this.updated.address.postalCode === current.address.postalCode) {
        document.getElementById('close-modal-supplier').click();
        this.$toast.show('No changes were made.', {
          position: 'top',
          duration: 2000
        });
        return false;
      }
      this.invalidFirstName = false;
      this.invalidLastName = false;
      this.invalidPhoneNumber = false;
      this.invalidAddressCity = false;
      this.invalidAddressName = false;
      this.invalidAddressNumber = false;
      this.invalidAddressPostalCode = false;

      if (this.updated.firstName === "" || !this.updated.firstName.match('^[A-Z]{1}[a-z]+$')) {
        this.invalidFirstName = true;
      }
      if (this.updated.lastName === "" || !this.updated.lastName.match('^[A-Z]{1}[a-z]+$')) {
        this.invalidLastName = true;
      }
      if (this.updated.phoneNumber === "" || !this.updated.phoneNumber.match('^[0-9]{10}$')) {
        this.invalidPhoneNumber = true;
      }
      if (this.updated.address.city === undefined || this.updated.address.city === "") {
        this.invalidAddressCity = true;
      }
      if (this.updated.address.name === undefined || this.updated.address.name === "") {
        this.invalidAddressName = true;
      }
      if (this.updated.address.number === undefined || this.updated.address.number === ""||
          !this.updated.address.number.match('^[0-9]*$')) {
        this.invalidAddressNumber = true;
      }
      if (this.updated.address.postalCode === undefined || this.updated.address.postalCode === ""
          || !this.updated.address.postalCode.match('^[0-9]*$')) {
        this.invalidAddressPostalCode = true;
      }

      return !this.invalidFirstName && !this.invalidLastName && !this.invalidPhoneNumber && !this.invalidAddressCity
          && !this.invalidAddressName && !this.invalidAddressNumber && !this.invalidAddressPostalCode;

    },
    edit() {
      this.updated = JSON.parse(JSON.stringify(this.$store.getters.getUser));
    }
  },
  data() {
    return {
      updated: {
        id: 0,
        firstName: "",
        lastName: "",
        phoneNumber: "",
        email: "",
        address: {}
      },
      invalidFirstName: false,
      invalidLastName: false,
      invalidPhoneNumber: false,
      invalidAddressCity: false,
      invalidAddressName: false,
      invalidAddressNumber: false,
      invalidAddressPostalCode: false
    }
  }
}
</script>

<style scoped>

.profile-img .file {
  position: relative;
  overflow: hidden;
  margin-top: -20%;
  width: 50%;
  border: none;
  border-radius: 0;
  font-size: 15px;
  background: #212529a1;
}
.profile-img .file input {
  position: absolute;
  opacity: 0;
  right: 0;
  top: 0;
}

</style>