<template>
  <div class="card mb-3">
    <div class="card-body">
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">First Name</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.firstName }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Last Name</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.lastName }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Phone Number</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.phoneNumber }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Email</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.email }}
        </div>
      </div>
      <hr>
      <div class="row">
        <div class="col-sm-3">
          <h6 class="mb-0">Address</h6>
        </div>
        <div class="col-sm-9 text-secondary">
          {{ $store.getters.getUser.address.name }}, {{ $store.getters.getUser.address.number }}, {{ $store.getters.getUser.address.city }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PharmacyAdminInfo"
}
</script>

<style scoped>

</style>