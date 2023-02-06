  
<template>

  <router-view/>
</template>

<script>
import axios from 'axios'

export default {
  beforeMount() {
    if (!localStorage.getItem('token'))
      return;
      
    let token = localStorage.getItem('token').substring(1, localStorage.getItem('token').length-1);
    axios.defaults.headers.common['Authorization'] = "Bearer " + token; // for all requests
    axios.interceptors.response.use(function (response) {
      return response;
    }, function (error) {
      if (error.response.status === 401) {
        this.$router.push({name: "Login"});
      }
      return Promise.reject(error);
    });
  },
  created() {
    this.$store.dispatch('getUser');
  },
}
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Source+Sans+Pro');
#app {
  font-family: 'Source Sans Pro', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
</style>