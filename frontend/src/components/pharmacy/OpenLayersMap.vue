<template>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/openlayers/openlayers.github.io@master/en/v6.5.0/css/ol.css" type="text/css">
  <div :id="editMode" class="omap"></div>
</template>

<script>
import VectorSource from 'ol/source/Vector'
import VectorLayer from 'ol/layer/Vector'
import Style from 'ol/style/Style'
import Icon from 'ol/style/Icon'
import Point from 'ol/geom/Point'
import {fromLonLat} from 'ol/proj'
import {toLonLat} from 'ol/proj'
import Tile from 'ol/layer/Tile'
import OSM from 'ol/source/OSM'
import Map from 'ol/Map'
import View from 'ol/View'
import Feature from 'ol/Feature'
import axios from "axios";
import imagePath from '../../assets/blue.png'

export default {
  name: "OpenLayersMap",
  props: ['lon', 'lat', 'editMode'],
  emits: ['newAddress'],
  data: function() {
    return {
      longitude: 0.0,
      latitude: 0.0,
      city: "",
      country: "",
      postcode: "",
      street: "",
      number: "",
      map: null
    }
  },
  created() {
    this.longitude = this.lon;
    this.latitude = this.lat;
  },
  mounted() {
    this.$nextTick(function () {
      this.mapInit();
    });
  },
  methods: {
    mapInit() {
      let vectorSource = new VectorSource({});
      let vectorLayer = new VectorLayer({
        source: vectorSource
      });

      // create custom marker image with custom text in bottom
      let iconStyle = new Style({
        image: new Icon({
          anchor: [12, 37],
          anchorXUnits: 'pixels', //'fraction'
          anchorYUnits: 'pixels',
          opacity: 0.8,
          src: imagePath
        })
      });

      let marker;

      function setMarker(coordinate) {
        marker = new Feature(
            new Point(fromLonLat(coordinate))
        );
        marker.setStyle(iconStyle);
        vectorSource.addFeature(marker);
      }

      this.map = new Map({
        target: document.getElementById(this.editMode),
        layers: [
          // adding a background tiled layer
          new Tile({
            source: new OSM() // tiles are served by OpenStreetMap
          }), vectorLayer
        ],

        view: new View({
          zoom: 13,
          center: fromLonLat([this.latitude, this.longitude]),
          constrainResolution: true
        }),
      });
      console.log(this.longitude, this.latitude);
      setMarker([this.latitude, this.longitude]);

      if(this.editMode === 'm2') {
        this.map.on('click', (event) => {
          let coordinates = toLonLat(event.coordinate);
          console.log(coordinates);
          vectorSource.clear();
          setMarker(coordinates);
          this.longitude = coordinates[0];
          this.latitude = coordinates[1];
          this.getAddress(coordinates[0], coordinates[1]);
        });
      }
    },
    getAddress: function(lon, lat) {
      axios({
        method: 'get',
        url: 'https://nominatim.openstreetmap.org/reverse?lat='+ lat +'&lon='+lon + '&format=json'
      }).then((response) => {
        if(response.data != null) {
          console.log(response.data);
          this.city = response.data.address.city;
          this.postcode = response.data.address.postcode;
          this.country = response.data.address.country;
          this.street = response.data.address.road;
          this.number = response.data.address.house_number;
          console.log(this.street + " " + this.number + " , " + this.city + " (" + this.country + ") " + this.postcode);
          this.$emit('newAddress', this.street, this.number, this.city, this.country, this.postcode, this.longitude, this.latitude);
        }
      });
    }
  }
}
</script>

<style scoped>
.omap {
  height: 400px;
  width: 100%;
}
</style>