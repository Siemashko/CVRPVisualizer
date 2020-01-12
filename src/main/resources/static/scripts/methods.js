async function initializeApplication() {
    if (!mymap) {
        mymap = L.map('mapid').setView([52.23, 21], 12);
        markerLayerGroup = L.layerGroup().addTo(mymap);
        polygonLayerGroup = L.layerGroup().addTo(mymap);

        L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 18,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
                '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox.streets'
        }).addTo(mymap);
        mymap.on('click', onMapClick);
    }
    markerLayerGroup.clearLayers();
    polygonLayerGroup.clearLayers();
    document.getElementById("package-list").innerHTML = "";
    document.getElementById("job-list").innerHTML = "";
    // var listOfDeliveryPackages = await findAll();
    // listOfDeliveryPackages.forEach(package => addPackageToList(package));
    Object.keys(visibleDeliveryPackages).forEach(packageIdentifier => addPackageToList(visibleDeliveryPackages[packageIdentifier]));
    var jobList = await findAllJobs();
    visibleJobs = jobList.reduce(function(map, obj) {
        map[obj.id] = obj;
        return map;
    }, {});
    Object.keys(visibleJobs).forEach(jobId => addJobToList(visibleJobs[jobId]));
    if (activeJob && activeJob.jobStatus === "DONE") {
        drawPathsBasedOnVehicleRoutes(mapCurrentVehicleRoutesToPoints(JSON.parse(activeJob.jobResult)[currentFrame].currentVehicleRoutes));
    }
    var greenIcon = new L.Icon({
        iconUrl: 'https://cdn.rawgit.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
        shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
        iconSize: [50 * 0.707, 82 * 0.707],
        iconAnchor: [25 * 0.707, 82 * 0.707],
        popupAnchor: [1, -34],
        shadowSize: [82 * 0.707, 82 * 0.707]
    });
    L.marker([52.2, 21], { icon: greenIcon }).addTo(markerLayerGroup);

}


function addJobToList(job) {
    var jobList = document.getElementById("job-list");
    var listElement = document.createElement("li");
    listElement.id = "job-" + job.id;
    listElement.innerHTML = "Vehicle job: " + job.id + "<br/>Status: " + job.jobStatus;
    listElement.addEventListener("click", setActiveJob);
    jobList.append(listElement);
    // var marker = L.marker([package.lat, package.lng]).addTo(markerLayerGroup);
    // marker.id = "marker-" + package.deliveryPackageId;
    // marker.on('click', showModalFromMarker);
}

function addPackageToList(package) {
    var packageList = document.getElementById("package-list");
    var listElement = document.createElement("li");
    listElement.id = "package-" + package.uniqueIdentifier;
    listElement.innerHTML = "lat: " + package.latitude + " lng: " + package.longitude + " weight: " + package.weight;
    listElement.addEventListener("click", showModalFromPackage);
    packageList.append(listElement);
    var marker = L.marker([package.latitude, package.longitude]).addTo(markerLayerGroup);
    marker.id = "marker-" + package.uniqueIdentifier;
    marker.on('click', showModalFromMarker);
}

function setActiveJob(e) {
    currentFrame = 0;
    activeJob = visibleJobs[e.target.id.replace("job-", "")];
    visibleDeliveryPackages = activeJob.points.reduce(function(map, obj) {
        map[obj.uniqueIdentifier] = obj;
        return map;
    }, {});
    initializeApplication();
}

function nextFrame() {
    currentFrame++;
    initializeApplication();
}

function previousFrame() {
    currentFrame--;
    initializeApplication();
}

function showModal() {
    activeModaleDeliveryPackageId = null;

    document.getElementById("weight").value = "";
    document.getElementById("lat").value = "";
    document.getElementById("lng").value = "";

    document.getElementById("modal-update-button").classList.add("hidden");
    document.getElementById("modal-delete-button").classList.add("hidden");
    document.getElementById("modal-submit-button").classList.remove("hidden");
    $("#myModal").modal("show");
}

function Visualize() {

    drawPath(52.2861, 21.0476, 52.1848, 20.9546);
    drawPath(52.2587, 20.6984, 52.2612, 21);
    drawPath(52.2587, 20.6984, 52.1848, 20.9546);


}

function drawPath(lat1, lng1, lat2, lng2, color) {

    var routingControl = L.Routing.control({
        waypoints: [
            L.latLng(lat1, lng1),
            L.latLng(lat2, lng2)
        ],
        router: L.Routing.graphHopper('cb77ede8-3517-4c8c-a7a7-c40124f4009b'),
        createMarker: function(i, waypoint, n) {
            return null;
        },
        geocoder: L.Control.Geocoder.nominatim(),
        routeWhileDragging: false,
        fitSelectedRoutes: false,
        draggableWaypoints: false,
	    addWaypoints: false,
        lineOptions: {
            styles: [{
                color: color,
                opacity: 1,
                weight: 5
            }]
        }
    }).addTo(mymap);
    routingControl.hide();
}

function ValidateCars() {
    var cars = document.getElementsByName("cars")[0].value;
    if (/^\d+$/.test(cars)) {
        return true;
    } else {
        alert("Number of cars has to be positive integer");
        return false;
    }
}

function showVehicleModal() {
    document.getElementById("capacity").value = "";
    $("#myModal2").modal("show");
}

function showModalFromPackage(e) {
    packageId = Number(e.target.id.replace("package-", ""));
    activeModaleDeliveryPackageId = packageId;
    var deliveryPackage = visibleDeliveryPackages[packageId];

    document.getElementById("weight").value = deliveryPackage.weight;
    document.getElementById("lat").value = deliveryPackage.latitude;
    document.getElementById("lng").value = deliveryPackage.longitude;

    document.getElementById("modal-update-button").classList.remove("hidden");
    document.getElementById("modal-delete-button").classList.remove("hidden");
    document.getElementById("modal-submit-button").classList.add("hidden");

    $("#myModal").modal("show");

}

function showModalFromMarker(e) {
    console.log(e.target.id);
    packageId = Number(e.target.id.replace("marker-", ""));
    activeModaleDeliveryPackageId = packageId;
    var deliveryPackage = visibleDeliveryPackages[packageId];

    document.getElementById("weight").value = deliveryPackage.weight;
    document.getElementById("lat").value = deliveryPackage.latitude;
    document.getElementById("lng").value = deliveryPackage.longitude;

    document.getElementById("modal-update-button").classList.remove("hidden");
    document.getElementById("modal-delete-button").classList.remove("hidden");
    document.getElementById("modal-submit-button").classList.add("hidden");

    $("#myModal").modal("show");

}

async function sendCreateJobRequest(e) {
    if (!ValidateCars()) {
        return 0
    }
    var vehicleCapacities = Array.from({ length: Number(document.getElementById("cars").value) }, (v, k) => 10);
    var points = Object.values(visibleDeliveryPackages);
    var depot = new DeliveryPackage(0, 52.2, 21, "depot");
    var algorithm = document.getElementsByName("algorithm")[0].value;
    var createJobRequest = new CreateJobRequest(points, algorithm, depot, vehicleCapacities);
    response = await createJob(createJobRequest);
    console.log(response);
    initializeApplication();
}

function sendCreateDeliveryPackageRequest(e) {
    var weight = Number(document.getElementById("weight").value);
    var lat = Number(document.getElementById("lat").value);
    var lng = Number(document.getElementById("lng").value);
    var uniqueIdentifier = Math.random().toString(36).slice(2);

    visibleDeliveryPackages[uniqueIdentifier] = new DeliveryPackage(weight, lat, lng, uniqueIdentifier);
    initializeApplication();
}

function sendUpdateDeliveryPackageRequest(e) {
    var weight = Number(document.getElementById("weight").value);
    var lat = Number(document.getElementById("lat").value);
    var lng = Number(document.getElementById("lng").value);

    var deliveryPackage = visibleDeliveryPackages[activeModaleDeliveryPackageId];
    deliveryPackage.weight = weight;
    deliveryPackage.latitude = lat;
    deliveryPackage.longitude = lng;
    visibleDeliveryPackages[activeModaleDeliveryPackageId] = deliveryPackage;

    initializeApplication();
}

function sendDeleteDeliveryPackageRequest(e) {
    delete visibleDeliveryPackages[activeModaleDeliveryPackageId];

    initializeApplication();
}

function onMapClick(e) {
    showModal();
    console.log(e.latlng);
    document.getElementById("lat").value = e.latlng.lat.toFixed(4);
    document.getElementById("lng").value = e.latlng.lng.toFixed(4);
}
document.addEventListener("DOMContentLoaded", initializeApplication)

function sendFile() {
    var path = document.getElementById("file").value
    var script = document.createElement("script");
    script.src = path;
    $("head").append(script);
    // var data = $.getJSON(path, function(obj) {});
    console.log(newValues);
}

function mapCurrentVehicleRoutesToPoints(currentVehicleRoutes) {
    return currentVehicleRoutes.map(currentVehicleRoute => currentVehicleRoute.map(ui => {
        if (ui === "depot") {
            return new DeliveryPackage(0, 52.2, 21, "depot");
        } else {
            return visibleDeliveryPackages[ui];
        }
    }));
}

function drawPathsBasedOnVehicleRoutes(vehicleRoutes) {
    for (var k = 0; k < vehicleRoutes.length; k++) {
        var vehicleRoute = vehicleRoutes[k];
        var color = colormap[k % 11];
        for (var i = 0; i + 1 < vehicleRoute.length; i++) {
            drawPath(vehicleRoute[i].latitude, vehicleRoute[i].longitude, vehicleRoute[i + 1].latitude, vehicleRoute[i + 1].longitude, color);
        }
    }
}
