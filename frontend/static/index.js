$(document).ready(function() {
    // Simulation Settings
    var SIM_SETTINGS = {

    };

    // Domain and Socket IO stuff
    var currentDomain = window.location.origin;
    var server = io();

    // Define bounds to lock map into area that dataset covers
    var corner1 = L.latLng(54.7831, -1.6016);
    var corner2 = L.latLng(54.7609, -1.5445);
    var bounds = L.latLngBounds(corner1, corner2);
    var center = L.latLng((corner1.lat + corner2.lat) / 2, (corner1.lng + corner2.lng)/2);

    // Initialise map element with location bounds
    var map = L.map("mapElement", {
        maxBounds: bounds
    }).setView(center, 15);

    // Initialise actual map view layer with zoom bounds
    var tileLayer = L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
        minZoom: 15,
        maxZoom: 19,
        attribution: "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"
    }).addTo(map);

    // Plot dataset enabled area (in palatine purple)
    var boundingRectangle = L.rectangle([
        corner1, corner2
    ], {color: "#7E317B", fill:false, weight: 4}).addTo(map);

    $('#start-simulation').on('click', function() {
        console.log("Start sim button clicked");
        server.emit('sim-start', JSON.stringify(SIM_SETTINGS));
    });

    server.on('sim-client-update', function(data) {
        // todo display
    });
});
