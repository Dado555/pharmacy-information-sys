<template>
  <Navbar></Navbar>

  <article>
    <h1>Welcome to Pharmacy Information System</h1>
    <img alt="Vue logo" src="../assets/pharmacy.png">
  </article>
</template>

<script>
// @ is an alias to /src
import Navbar from '@/components/Navbar.vue'

export default {
  name: 'Home',
  components: {
    Navbar
  }
}
</script>

<style scoped>

article img {
  text-align: center;
}

article h1 {
  margin-top: 5%;
  margin-bottom: 5%;
  text-align: center;
  font-weight: bold;
  font-size: 300%;
}

</style>
