var express = require('express');
var path = require('path');
var app = express();

var currentDirectory = (process.env.PORT) ? process.cwd() : __dirname;

app.set('port', process.env.PORT || 3000);

app.use(express.static(path.join(currentDirectory, "static")));

app.get("*", function(req, res) {
    res.status(404).send("File not found");

});

app.listen(app.get('port'), function() {
    console.log("Server started on port " + app.get('port'));
});
