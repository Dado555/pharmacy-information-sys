<template>
  <div class="card mt-3">
    <ul class="list-group list-group-flush">
      <router-link :to="{ path: '/pharmacy/dermatologists', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap" ref="listaDermatologa">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar2-check-fill" viewBox="0 0 16 16">
            <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zm9.954 3H2.545c-.3 0-.545.224-.545.5v1c0 .276.244.5.545.5h10.91c.3 0 .545-.224.545-.5v-1c0-.276-.244-.5-.546-.5zm-2.6 5.854a.5.5 0 0 0-.708-.708L7.5 10.793 6.354 9.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z"/>
          </svg> Dermatologists </h6>
      </router-link>
      <router-link :to="{ path: '/pharmacy/pharmacists', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-calendar2-check-fill" viewBox="0 0 16 16">
            <path d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zm9.954 3H2.545c-.3 0-.545.224-.545.5v1c0 .276.244.5.545.5h10.91c.3 0 .545-.224.545-.5v-1c0-.276-.244-.5-.546-.5zm-2.6 5.854a.5.5 0 0 0-.708-.708L7.5 10.793 6.354 9.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0l3-3z"/>
          </svg> Pharmacists </h6>
      </router-link>
      <router-link :to="{ path: '/pharmacy/availableMedicine', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill" viewBox="0 0 16 16">
            <path d="M12.643 15C13.979 15 15 13.845 15 12.5V5H1v7.5C1 13.845 2.021 15 3.357 15h9.286zM5.5 7h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1 0-1zM.8 1a.8.8 0 0 0-.8.8V3a.8.8 0 0 0 .8.8h14.4A.8.8 0 0 0 16 3V1.8a.8.8 0 0 0-.8-.8H.8z"/>
          </svg> Medicines </h6>
      </router-link>
      <router-link :to="{ path: '/pharmacy/appointments', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill" viewBox="0 0 16 16">
            <path d="M12.643 15C13.979 15 15 13.845 15 12.5V5H1v7.5C1 13.845 2.021 15 3.357 15h9.286zM5.5 7h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1 0-1zM.8 1a.8.8 0 0 0-.8.8V3a.8.8 0 0 0 .8.8h14.4A.8.8 0 0 0 16 3V1.8a.8.8 0 0 0-.8-.8H.8z"/>
          </svg> Appointments </h6>
      </router-link>
      <router-link v-if="user.role === 'PHARMACY_ADMIN'" :to="{ path: '/pharmacy/orders', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill" viewBox="0 0 16 16">
            <path d="M12.643 15C13.979 15 15 13.845 15 12.5V5H1v7.5C1 13.845 2.021 15 3.357 15h9.286zM5.5 7h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1 0-1zM.8 1a.8.8 0 0 0-.8.8V3a.8.8 0 0 0 .8.8h14.4A.8.8 0 0 0 16 3V1.8a.8.8 0 0 0-.8-.8H.8z"/>
          </svg> Orders </h6>
      </router-link>
      <router-link v-if="user.role === 'PHARMACY_ADMIN'" :to="{ path: '/pharmacy/time-off', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill" viewBox="0 0 16 16">
            <path d="M12.643 15C13.979 15 15 13.845 15 12.5V5H1v7.5C1 13.845 2.021 15 3.357 15h9.286zM5.5 7h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1 0-1zM.8 1a.8.8 0 0 0-.8.8V3a.8.8 0 0 0 .8.8h14.4A.8.8 0 0 0 16 3V1.8a.8.8 0 0 0-.8-.8H.8z"/>
          </svg> Time off </h6>
      </router-link>
      <router-link v-if="user.role === 'PHARMACY_ADMIN'" :to="{ path: '/pharmacy/medicine-inquiry', query: {id : pharmacy_id}}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center flex-wrap">
        <h6 class="mb-0">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill" viewBox="0 0 16 16">
            <path d="M12.643 15C13.979 15 15 13.845 15 12.5V5H1v7.5C1 13.845 2.021 15 3.357 15h9.286zM5.5 7h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1 0-1zM.8 1a.8.8 0 0 0-.8.8V3a.8.8 0 0 0 .8.8h14.4A.8.8 0 0 0 16 3V1.8a.8.8 0 0 0-.8-.8H.8z"/>
          </svg> Inquiries </h6>
      </router-link>
    </ul>
  </div>
</template>

<script>
export default {
  name: "PharmacyLinks",
  props: ['id'],
  data: function() {
    return {
      pharmacy_id: 0
    }
  },
  created() {
    this.pharmacy_id = this.id
  },
  computed: {
    user() {
      return this.$store.getters.getUser;
    },
  }
}
</script>

<style scoped>

</style>