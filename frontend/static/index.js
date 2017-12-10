$(document).ready(function() {
    // Simulation Settings
    var SIM_SETTINGS = {
        "dt": "1",
        "runtime": "600",
        "density": "0.27",
        "saveGap": "20",
        "a": "0.1",
        "b": "1",
        "c": "3"
    };

    // Domain and Socket IO stuff
    var currentDomain = window.location.origin;
    var server = io();

    // Define bounds to lock map into area that dataset covers
    var corner1 = L.latLng(54.7831, -1.6016);
    var corner2 = L.latLng(54.7609, -1.5445);
    var bounds = L.latLngBounds(corner1, corner2);
    var center = L.latLng((corner1.lat + corner2.lat) / 2, (corner1.lng + corner2.lng) / 2);

    // Store polylines to be able to remove them later
    var polylines = [];

    // Store a history of timeframes and display info
    var timeframes = [];
    var timeframeIndex = -1;
    var paused = true;
    var intervalID = -1;

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
    ], {
        color: "#7E317B",
        fill: false,
        weight: 4
    }).addTo(map);

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
        for (var i = 0; i < timeframe.values.length; i++) {
            var nodeA = [timeframe.values[i][0], timeframe.values[i][1]];
            var nodeB = [timeframe.values[i][2], timeframe.values[i][3]];
            var heat = timeframe.values[i][4];
            polylines.push(drawHeat(nodeA, nodeB, heat));
        }

        // Actually display these polylines
        for (var i = 0; i < polylines.length; i++) {
            map.addLayer(polylines[i]);
        }
    }

    function updateProgressBar() {
        var percent = 100 * (timeframeIndex + 1) / timeframes.length;
        $("#prgTimeframes").text("" + Math.round(percent) + "%");
        $("#prgTimeframes").css("width", "" + percent + "%");
    }

    function drawCurrentTimeframe() {
        drawTimeframe(timeframes[timeframeIndex]);
        updateProgressBar();
    }

    $("#start-simulation").on("click", function() {
        server.emit("sim-start", JSON.stringify(SIM_SETTINGS));
        $('#loading-spinner').removeClass("hide");
    });

    server.on("sim-client-update", function(data) {
        var obj = JSON.parse(data);
        var newTimeframes = [];
        for (var i = 0; i < obj.length; i++) {
            newTimeframes.push(JSON.parse(obj[i]));
        }
        if (newTimeframes.length > 0) {
            timeframes = newTimeframes;
            timeframeIndex = 0;
            drawCurrentTimeframe();

            // enable timeline controls now we have frames for the timeline
            $('#loading-spinner').addClass("hide");
            $("#btnSkipToStart").removeClass("disabled");
            $("#btnBackwardOne").removeClass("disabled");
            $("#btnPlayPause").removeClass("disabled");
            $("#btnForwardOne").removeClass("disabled");
            $("#btnSkipToEnd").removeClass("disabled");
        }
    });

    $("#btnForwardOne").on("click", function() {
        if (!$(this).hasClass("disabled")) {
            timeframeIndex = (timeframeIndex + 1) % timeframes.length;
            drawCurrentTimeframe();
        }
    });

    $("#btnBackwardOne").on("click", function() {
        if (!$(this).hasClass("disabled")) {
            timeframeIndex -= 1;
            if (timeframeIndex < 0) {
                timeframeIndex += timeframes.length;
            }
            drawCurrentTimeframe();
        }
    });

    $("#btnSkipToStart").on("click", function() {
        if (!$(this).hasClass("disabled")) {
            timeframeIndex = 0;
            drawCurrentTimeframe();
        }
    });

    $("#btnSkipToEnd").on("click", function() {
        if (!$(this).hasClass("disabled")) {
            timeframeIndex = timeframes.length - 1;
            drawCurrentTimeframe();
        }
    });

    function autoUpdate() {
        timeframeIndex = (timeframeIndex + 1) % timeframes.length;
        drawCurrentTimeframe();
        if (!paused) {
            setTimeout(function() {
                window.requestAnimationFrame(autoUpdate)
            }, 1200);
        }
    }

    $("#btnPlayPause").on("click", function() {
        if (!$(this).hasClass("disabled")) {
            if (paused) {
                // intervalID = window.setInterval(autoUpdate, 1200);
                window.requestAnimationFrame(autoUpdate)
                $("#spanPlayPause").removeClass("glyphicon-play");
                $("#spanPlayPause").addClass("glyphicon-pause");
                $("#prgTimeframes").addClass("progress-bar-striped active");
            } else {
                // window.clearInterval(intervalID);
                $("#spanPlayPause").removeClass("glyphicon-pause");
                $("#spanPlayPause").addClass("glyphicon-play");
                $("#prgTimeframes").removeClass("progress-bar-striped active");
            }
            paused = !paused;
        }
    });

    $("#aInput").bind('keyup mouseup', function () {
    SIM_SETTINGS['a'] =  $(this).val();
    });
    $("#bInput").bind('keyup mouseup', function () {
    SIM_SETTINGS['b'] =  $(this).val();
    });
    $("#cInput").bind('keyup mouseup', function () {
    SIM_SETTINGS['c'] = $(this).val();
    });
    $("#dt").bind('keyup mouseup', function () {
    SIM_SETTINGS['dt'] =  $(this).val();
    });
    $("#density").bind('keyup mouseup', function () {
    SIM_SETTINGS['density'] =  $(this).val();
    });
    $("#runtime").bind('keyup mouseup', function () {
    SIM_SETTINGS['runtime'] =  $(this).val();
    });
    $("#savegap").bind('keyup mouseup', function () {
    SIM_SETTINGS['saveGap'] =  $(this).val();
    });


    $('#aInput').val(SIM_SETTINGS['a']);
    $('#bInput').val(SIM_SETTINGS['b']);
    $('#cInput').val(SIM_SETTINGS['c']);
    $('#dt').val(SIM_SETTINGS['dt']);
    $('#density').val(SIM_SETTINGS['density']);
    $('#runtime').val(SIM_SETTINGS['runtime']);
    $('#savegap').val(SIM_SETTINGS['saveGap']);
});
