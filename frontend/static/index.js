$(document).ready(function() {
    // Simulation Settings
    var SIM_SETTINGS = {

    };

    // Domain and Socket IO stuff
    var currentDomain = window.location.origin;
    var server = io(currentDomain);

    var corner1 = L.latLng(54.7831, -1.6016),
    corner2 = L.latLng(54.7609, -1.5445),
    bounds = L.latLngBounds(corner1, corner2);

    var map = L.map("mapElement", {
        maxBounds: bounds
    }).setView([54.77525, -1.584851], 13);

    L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"
    }).addTo(map);


    $('#start-simulation').on('click', function() {
        server.emit('sim-start', SIM_SETTINGS);
    });



    server.on('sim-client-update', function(data) {
        // todo display
    });
});
