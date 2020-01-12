class DeliveryPackage {
    constructor(deliveryPackageId, tags, width, length, height, weight, lat, lng, createdTime, lastModifiedTime, version) {
        this.deliveryPackageId = deliveryPackageId;
        this.tags = tags;
        this.width = width;
        this.length = length;
        this.height = height;
        this.weight = weight;
        this.lat = lat;
        this.lng = lng;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.version = version;
    }
}

class CreatePackageRequest {
    constructor(weight, lat, lng) {
        this.weight = weight;
        this.lat = lat;
        this.lng = lng;
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
    constructor(jobId, status, vehicleCapacity, jobResult) {
        this.jobId = jobId;
        this.status = status;
        this.vehicleCapacity = vehicleCapacity;
        this.jobResult = jobResult;
    }
}

class CreateJobRequest {
    constructor(vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }
}