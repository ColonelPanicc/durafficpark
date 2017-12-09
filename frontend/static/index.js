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

    // Store polylines to be able to remove them later
    var polylines = [];

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

    // On-road 'heatmap' drawing
    function drawHeat(nodeA, nodeB, heat) {
        // Assuming heat > 0.9, make red
        var hue = 0;
        if (heat <= 0.4) {
            // If heat < 0.4, make green
            hue = 100;
        } else if (heat < 0.9) {
            // If 0.4 < heat < 0.9, linearly interpolate hue
            hue = Math.round(180 - 200 * heat)
        }
        // Draw road with relevant colour
        return L.polyline([
            nodeA, nodeB
        ], {
            color: "hsl(" + hue + ",100%,50%)"
        });
    }

    // Draw each road within a timeframe
    function drawTimeframe(timeframe) {
        // Remove existing polylines from map
        for (var i = 0; i < polylines.length; i++) {
            map.removeLayer(polylines[i]);
        }

        // Forget the removed polylines
        polylines = [];

        // Define polylines for this timeframe
        for (var i = 0; i < timeframe.heats.length; i++) {
            var nodeA = timeframe.heats[i].nodeA;
            var nodeB = timeframe.heats[i].nodeB;
            var heat = timeframe.heats[i].heat;
            polylines.push(drawHeat(nodeA, nodeB, heat));
        }

        // Actually display these polylines
        for (var i = 0; i < polylines.length; i++) {
            map.addLayer(polylines[i]);
        }
    }

    // Example timeframe drawing
    var timeframe = {
        heats: [
            {
                nodeA: corner1,
                nodeB: center,
                heat: 0.4
            },
            {
                nodeA: corner2,
                nodeB: center,
                heat: 0.7
            }
        ]
    }
    drawTimeframe(timeframe);

    $('#start-simulation').on('click', function() {
        console.log("Start sim button clicked");
        server.emit('sim-start', JSON.stringify(SIM_SETTINGS));
    });

    server.on('sim-client-update', function(data) {
        // todo display
    });
});
