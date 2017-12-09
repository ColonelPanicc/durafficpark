$(document).ready(function() {
    // Simulation Settings
    var SIM_SETTINGS = {

    };

    // Domain and Socket IO stuff
    var currentDomain = window.location.origin;
    var server = io();

    // Define bounds to lock map into area that dataset covers
    var corner1 = L.latLng(54.7831, -1.6016),
    corner2 = L.latLng(54.7609, -1.5445),
    bounds = L.latLngBounds(corner1, corner2);

    var map = L.map("mapElement", {
        maxBounds: bounds
    }).setView([54.77525, -1.584851], 13);

    L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
        minZoom: 13,
        maxZoom: 19,
        attribution: "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"
    }).addTo(map);


    $('#start-simulation').on('click', function() {
        console.log("Start sim button clicked");
        server.emit('sim-start', JSON.stringify(SIM_SETTINGS));
    });



    server.on('sim-client-update', function(data) {
        // todo display
    });
});
