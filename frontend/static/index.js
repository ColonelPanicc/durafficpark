$(document).ready(function() {
    // Simulation Settings
    var SIM_SETTINGS = {

    };

    // Domain and Socket IO stuff
    var currentDomain = window.location.origin;
    var server = io(currentDomain);

    var map = L.map("mapElement").setView([54.77525, -1.584851], 13);
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
