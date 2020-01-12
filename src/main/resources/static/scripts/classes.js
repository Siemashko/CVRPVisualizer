class DeliveryPackage {
    constructor(weight, lat, lng) {
        this.weight = weight;
        this.latitude = lat;
        this.longitude = lng;
    }
}

class CreateJobRequest {
    constructor(algorithm, points, depot, vehicleCapacities) {
        this.points = points;
        this.algorithm = algorithm;
        this.depot = depot;
        this.vehicleCapacities = vehicleCapacities;
    }
}

class UpdatePackageRequest {
    constructor(deliveryPackageId, weight, lat, lng, version) {
        this.deliveryPackageId = deliveryPackageId;
        this.weight = weight;
        this.lat = lat;
        this.lng = lng;
        this.version = version;
    }
}

class Job {
    constructor(id, algorithm, jobStatus, vehicleCapacities, points, depot, jobResult) {
        this.id = id
        this.algorithm = algorithm;
        this.jobStatus = jobStatus;
        this.vehicleCapacities = vehicleCapacities;
        this.points = points;
        this.depot = depot;
        this.jobResult = jobResult;
    }
}