var express = require("express");
var path = require("path");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io")(server);
var simulationServer = require('socket.io-client')('http://localhost:4000');
var currentDirectory = process.env.PORT ? process.cwd() : __dirname;

app.set("port", process.env.PORT || 3000);

app.use("/jquery", express.static(path.join(currentDirectory, "node_modules/jquery/dist")));
app.use("/bootstrap", express.static(path.join(currentDirectory, "node_modules/bootstrap/dist")));
app.use("/socketio", express.static(path.join(currentDirectory, "node_modules/socket.io-client/dist/socket.io.js")));
app.use(express.static(path.join(currentDirectory, "static")));

app.get("*", function(req, res) {
    // console.log("404 request", req)
    res.status(404).send("File not found");
});

app.listen(app.get("port"), function() {
    console.log("Server started on port " + app.get("port"));
});

// --------- Socket IO Stuff ------------

io.on("connection", function(client) {
    console.log("Client Connected");
});
