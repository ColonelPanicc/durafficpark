$(document).ready(function() {
    var map = L.map("mapElement").setView([54.77525, -1.584851], 13);
    L.tileLayer("http://{s}.tile.osm.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; <a href=\"http://osm.org/copyright\">OpenStreetMap</a> contributors"
    }).addTo(map);
});
