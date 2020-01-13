const restServiceUrl = 'http://localhost:8080';
var activeModaleDeliveryPackageId;
var mymap;
var activeJob;
var visibleDeliveryPackages = {};
var visibleJobs = {};
var markerLayerGroup;
var polygonLayerGroup;
var pathLayerGroup;
var animationInterval;
var currentFrame = 0;
var routingControls = [];
var colormap = {
    0: "#C20088",
    1: "#426600",
    2: "#FFA8BB",
    3: "#5EF1F2",
    4: "#FFA405",
    5: "#00998F",
    6: "#E0FF66",
    7: "#808080",
    8: "#FFCC99",
    9: "#993F00",
    10: "#FFFFFF"
}