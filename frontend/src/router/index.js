  
import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import store from '@/store/store.js'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/pharmacies',
    name: 'Pharmacies',
    component: () => import(/* webpackChunkName: "about" */ '../views/Pharmacies.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role !== 'PHARMACIST' && role !== "DERMATOLOGIST") {
          next();
        }
        else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/medicines',
    name: 'Medicines',
    component: () => import(/* webpackChunkName: "about" */ '../views/Medicines.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role !== 'PHARMACIST' && role !== "DERMATOLOGIST") {
          next();
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import(/* webpackChunkName: "about" */ '../views/Orders.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SUPPLIER') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/dermatologists',
    name: 'Dermatologists',
    component: () => import(/* webpackChunkName: "about" */ '../views/Dermatologists.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT' || role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/pharmacists',
    name: 'Pharmacists',
    component: () => import(/* webpackChunkName: "about" */ '../views/Pharmacists.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/pharmacyAdmins',
    name: 'PharmacyAdmins',
    component: () => import(/* webpackChunkName: "about" */ '../views/PharmacyAdmins.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/systemAdmins',
    name: 'SystemAdmins',
    component: () => import(/* webpackChunkName: "about" */ '../views/SystemAdmins.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/suppliers',
    name: 'Suppliers',
    component: () => import(/* webpackChunkName: "about" */ '../views/Suppliers.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/qrcode',
    name: 'QRCode',
    component: () => import(/* webpackChunkName: "about" */ '../views/QRCode.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  },
  {
    path: '/addMedicineForm',
    name: 'AddMedicineForm',
    component: () => import(/* webpackChunkName: "about" */ '../views/AddMedicineForm.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/medical-worker',
    name: 'MedicalWorker',
    component: () => import(/* webpackChunkName: "about" */ '../views/MedicalWorker.vue'),
    children: [
        {
          path: 'patients',
          component: () => import(/* webpackChunkName: "about" */ '../components/medical_worker/PatientSearch.vue')
        },
      {
        path: 'work-calendar',
        component: () => import(/* webpackChunkName: "about" */ '../components/medical_worker/WorkCalendar.vue')
      },
      {
        path: 'appointment-history',
        component: () => import(/* webpackChunkName: "about" */ '../components/medical_worker/AppointmentHistory.vue')
      },
      {
        path: 'request-time-off',
        component: () => import(/* webpackChunkName: "about" */ '../components/medical_worker/TimeOffRequest')
      },
      {
        path: 'medicine-issuing',
        component: () => import(/* webpackChunkName: "about" */ '../components/medical_worker/MedicineIssuing'),
        async beforeEnter(to, from, next) {
          try {
            let role = await store.dispatch('getUserRole');
            if (role === 'PHARMACIST') {
              next()
            } else {
              next({
                name: 'Home' // back to safety route //
              })
            }
          } catch (e) {
            next({
              name: 'Home' // back to safety route //
            })
          }
        }
      }
    ],
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'DERMATOLOGIST' || role === 'PHARMACIST') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/patient',
    name: 'Patient',
    component: () => import(/* webpackChunkName: "about" */ '../views/Patient.vue'),
    children: [
      {
        path: 'info',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/PatientInfo.vue')
      },
      {
        path: 'reserved-medicines',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/ReservedMedicinesInfo.vue')
      },
      {
        path: 'allergic-medicines',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/AllergicMedicines.vue')
      },
      {
        path: 'scheduled-appointments',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/ScheduledAppointments.vue')
      },
      {
        path: 'appointment-history',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/AppointmentHistory.vue')
      },
      {
        path: 'e-prescriptions',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/EPrescriptions.vue')
      },
      {
        path: 'penalties',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/Penalties.vue')
      },
      {
        path: 'subscriptions',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/Subscriptions.vue')
      },
      {
        path: 'complaints',
        component: () => import(/* webpackChunkName: "about" */ '../components/patient/Complaints.vue')
      }
  ],
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/supplier',
    name: 'Supplier',
    component: () => import(/* webpackChunkName: "about" */ '../views/Supplier.vue'),
    children: [
      {
        path: 'info',
        component: () => import(/* webpackChunkName: "about" */ '../components/supplier/SupplierInfo.vue')
      },
      {
        path: 'order-offers',
        component: () => import(/* webpackChunkName: "about" */ '../components/supplier/SupplierOrderOffers.vue')
      }
    ],
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SUPPLIER') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/pharmacy-admin',
    name: 'PharmacyAdmin',
    component: () => import(/* webpackChunkName: "about" */ '../views/PharmacyAdmin.vue'),
    children: [
      {
        path: 'info',
        component: () => import(/* webpackChunkName: "about" */ '../components/pharmacy_admin/PharmacyAdminInfo.vue')
      }
    ],
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PHARMACY_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/pharmacy',
    name: 'Pharmacy',
    component: () => import('../views/Pharmacy.vue'),
    children: [
      {
        path: 'dermatologists',
        name: 'PharmacyDermatologists',
        component: () => import('@/components/pharmacy/PharmacyDermatologists.vue')
      },
      {
        path: 'pharmacists',
        name: 'PharmacyPharmacists',
        component: () => import('@/components/pharmacy/PharmacyPharmacists.vue')
      },
      {
        path: 'availableMedicine',
        name: 'PharmaciesAvailableMedicine',
        component: () => import('@/components/pharmacy/PharmaciesAvailableMedicine.vue')
      }, 
      {
        path: 'appointments',
        name: 'PharmacyAppointments',
        component: () => import('@/components/pharmacy/PharmacyAppointments.vue')
      },
      {
        path: 'orders',
        name: 'PharmacyOrders',
        component: () => import('@/components/pharmacy/PharmacyOrders.vue')
      },
      {
        path: 'time-off',
        name: 'TimeOffRequests',
        component: () => import('@/components/pharmacy/TimeOffRequests.vue')
      },
      {
        path: 'medicine-inquiry',
        name: 'MedicineInquiries',
        component: () => import('@/components/pharmacy/MedicineInquiries.vue')
      }
    ],
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT' || role === "PHARMACY_ADMIN" || role === "SUPPLIER") {
          next()
        } else {
          next({
            name: 'Login' // back to safety route //
          })
        }
        next()
      } catch (e) {
        next({
          name: 'Login' // back to safety route //
        })
      }
    }
  },
  {
    path: '/medicine',
    name: 'SingleMedicine',
    component: () => import('../views/SingleMedicine.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT' || role === "PHARMACY_ADMIN") {
          next()
        } else {
          next({
            name: 'Login' // back to safety route //
          })
        }
        next()
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/pharmacy-medicine',
    name: 'Medicine',
    component: () => import('../views/Medicine.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
        next()
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/register',
    name: 'UserRegistration',
    component: () => import('../views/UserRegistration.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/addPharmacy',
    name: 'AddPharmacy',
    component: () => import('../views/AddPharmacy.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/addPharmacyAdmin',
    name: 'AddPharmacyAdmin',
    component: () => import('../views/AddPharmacyAdmin.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/addDermatologist',
    name: 'AddDermatologist',
    component: () => import('../views/AddDermatologist.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/addSupplier',
    name: 'AddSupplier',
    component: () => import('../views/AddSupplier.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/addSystemAdmin',
    name: 'AddSystemAdmin',
    component: () => import('../views/AddSystemAdmin.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/complaints',
    name: 'Complaints',
    component: () => import('../views/Complaints'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/complaintsNotAnswered',
    name: 'AnswerComplaints',
    component: () => import('../views/AnswerComplaints'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'SYSTEM_ADMIN') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  },
  {
    path: '/cart',
    name: "Cart",
    component: () => import('../components/Cart.vue'),
    async beforeEnter(to, from, next) {
      try {
        let role = await store.dispatch('getUserRole');
        if (role === 'PATIENT') {
          next()
        } else {
          next({
            name: 'Home' // back to safety route //
          })
        }
      } catch (e) {
        next({
          name: 'Home' // back to safety route //
        })
      }
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router