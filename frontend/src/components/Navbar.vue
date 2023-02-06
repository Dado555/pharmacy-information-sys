<template>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">
            <img alt="Logo" src="../assets/pharmacy.png">
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
                <router-link to="/" class="nav-link">Home</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/pharmacy" class="nav-link" v-if="$store.getters.getUser.role === 'PHARMACY_ADMIN'">Pharmacy</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/pharmacy-admin" class="nav-link" v-if="$store.getters.getUser.role === 'PHARMACY_ADMIN'">Profile</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/pharmacies" class="nav-link" v-if="$store.getters.getUser.role !== 'PHARMACIST' && $store.getters.getUser.role !== 'DERMATOLOGIST' && $store.getters.getUser.role !== 'SUPPLIER'">Pharmacies</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/medicines" class="nav-link" v-if="$store.getters.getUser.role !== 'PHARMACIST' && $store.getters.getUser.role !== 'DERMATOLOGIST' && $store.getters.getUser.role !== 'SUPPLIER'">Medicines</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/orders" class="nav-link" v-if="$store.getters.getUser.role === 'SUPPLIER'">Orders</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/dermatologists" class="nav-link" v-if="$store.getters.getUser.role === 'PATIENT' || $store.getters.getUser.role === 'SYSTEM_ADMIN'">Dermatologists</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/pharmacists" class="nav-link" v-if="$store.getters.getUser.role === 'PATIENT'">Pharmacists</router-link>
            </li>
              <li class="nav-item">
                <router-link to="/pharmacyAdmins" class="nav-link" v-if="$store.getters.getUser.role === 'SYSTEM_ADMIN'">Pharmacy admins</router-link>
              </li>
              <li class="nav-item">
                <router-link to="/systemAdmins" class="nav-link" v-if="$store.getters.getUser.role === 'SYSTEM_ADMIN'">System admins</router-link>
              </li>
              <li class="nav-item">
                <router-link to="/suppliers" class="nav-link" v-if="$store.getters.getUser.role === 'SYSTEM_ADMIN'">Suppliers</router-link>
              </li>
              <li class="nav-item">
                <router-link to="/qrcode" class="nav-link" v-if="$store.getters.getUser.role === 'PATIENT'">Upload QR code</router-link>
              </li>
            <li class="nav-item">
                <router-link to="/medical-worker" class="nav-link" v-if="$store.getters.getUser.role === 'DERMATOLOGIST' || $store.getters.getUser.role === 'PHARMACIST'">Medical Worker</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/supplier" class="nav-link" v-if="$store.getters.getUser.role === 'SUPPLIER'">Profile</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/complaints" class="nav-link" v-if="$store.getters.getUser.role === 'SYSTEM_ADMIN'">Answered complaints</router-link>
            </li>
            <li class="nav-item">
                <router-link to="/complaintsNotAnswered" class="nav-link" v-if="$store.getters.getUser.role === 'SYSTEM_ADMIN'">Complaints</router-link>
            </li>
            <li class="nav-item about">
                <router-link to="/about" class="nav-link">About</router-link>
            </li>
            </ul>
            <span class="nav-item profile">
                <router-link to="/patient" class="nav-link profile" v-if="$store.getters.getUser.role === 'PATIENT'">Profile</router-link>
            </span>
            <span class="nav-item cart">
                <router-link class="nav-link" to="/cart" v-if="$store.getters.getUser.role === 'PATIENT'">
                    <a class="cart-img" href="/">
                        <img src="../assets/cart.png">
                    </a>
                </router-link>
            </span>   
            <form class="d-flex">
                <router-link to="/login" class="nav-link" v-if="$store.getters.getUser.role == null">Login</router-link>
                <router-link to="/register" class="btn btn-outline-primary" v-if="$store.getters.getUser.role == null">Register</router-link>
                <button to="/logout" @click="logout" class="btn btn-outline-primary" v-if="$store.getters.getUser.role != null">Logout</button>
            </form>
        </div>
    </div>
</nav>
</template>

<script>
export default {
    name: 'Navbar',
    methods: {
        logout() {
            localStorage.removeItem('token');
            this.$router.go();
        }
    }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
nav {
    z-index: 1;
    padding: 10px;
    background-color: white;
}

.profile {
    color: rgba(0,0,0,.55);
}

nav a {
    font-size: 120%;
}

.nav-item {
    font-weight: bold;
}

.navbar-brand img {
    height: 60px;
    float: left;
    margin-right: 30px;
    margin-left: 30px;
}

.cart-img img{
    width: 30px;
    height: 30px;
}

.profile {
    padding: 0px;
}

.d-flex {
    margin-left: 0.5rem;
}



</style>
