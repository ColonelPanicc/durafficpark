$(document).ready(function() {
    var map = L.map("mapElement").setView([54.77525, -1.584851], 13);
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
        maxZoom: 19
        // id: 'mapbox.streets',
        // accessToken: 'your.mapbox.access.token'
    }).addTo(map);
});
