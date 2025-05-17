let flightNumberInput;
let flightNumber;
let map;
let planeIcon;
let marker = null;
let plane;
let intervalID = null;
let lastApiCallTime;
let lastPlanePositionUpdateTime;

function documentLoaded() {
    map = L.map("map").setView([0, 0], 2);
    L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; <a href='http://www.openstreetmap.org/copyright'>OpenStreetMap</a>"
    }).addTo(map);
    flightNumberInput = document.getElementById("flightNumber");
}

function searchFlightNumber() {
    if (intervalID != null) {
        clearInterval(intervalID);
        animationRunningCount = 0;
    }
    fetch("/planePosition?callsign=" + flightNumber)
        .then(response => response.text())
        .then(responseInText => {
            try {
                let responseJSON = JSON.parse(responseInText);
                plane = {
                    callsign: responseJSON.callsign,
                    lat: responseJSON.latitude,
                    lng: responseJSON.longitude,
                    heading: responseJSON.heading,
                    groundSpeedInMS: responseJSON.groundSpeedInMS
                };
                planeIcon = L.icon({
                    iconUrl: "/../photos/planeIconOnMap.png",
                    iconSize: [38, 38],
                    iconAnchor: [19, 19]
                });
                if (marker != null) {
                    map.removeLayer(marker);
                    marker = null;
                }
                marker = L.marker([plane.lat, plane.lng],
                    {icon: planeIcon, rotationAngle: plane.heading})
                    .addTo(map);
                map.setView([plane.lat, plane.lng], 10);
                intervalID = setInterval(animate, 20);
                lastPlanePositionUpdateTime = Date.now();
                lastApiCallTime = Date.now();
            } catch (exception) {
                flightNumberInput.classList.add("border-danger");
            }
        });
}

function validate() {
    flightNumber = flightNumberInput.value;
    if (flightNumber === "") {
        flightNumberInput.classList.add("border-danger");
    } else {
        flightNumberInput.classList.remove("border-danger");
    }
}

function calculateAndUpdatePlanPosition() {
    let now = Date.now();
    if ((now - lastApiCallTime) / 1000 > 90) {
        flightNumber = plane.callsign;
        searchFlightNumber();
        return;
    }
    let elapsedTimeSinceLastUpdate = (now - lastPlanePositionUpdateTime) / 1000;
    lastPlanePositionUpdateTime = now;
    let averageRadiusOfEarth = 6371000;
    let distance = elapsedTimeSinceLastUpdate * plane.groundSpeedInMS;
    let planeInRadian = {
        lng: convertDegreeToRadian(plane.lng),
        lat: convertDegreeToRadian(plane.lat),
        heading: convertDegreeToRadian(plane.heading)
    }
    let squareDistance = distance / averageRadiusOfEarth;

    let newLat = Math.asin(
        Math.sin(planeInRadian.lat) * Math.cos(squareDistance) + Math.cos(planeInRadian.lat) * Math.sin(squareDistance) * Math.cos(planeInRadian.heading));
    let newLng = planeInRadian.lng + Math.atan2(
        Math.sin(planeInRadian.heading) * Math.sin(squareDistance) * Math.cos(planeInRadian.lat),
        Math.cos(squareDistance) - Math.sin(planeInRadian.lat) * Math.sin(newLat));
    plane.lat = convertRadianToDegree(newLat);
    plane.lng = convertRadianToDegree(newLng);
    marker.setLatLng([plane.lat, plane.lng]);
}

function animate() {
    window.requestAnimationFrame(calculateAndUpdatePlanPosition);
}

function convertDegreeToRadian(degree) {
    return degree * (Math.PI / 180);
}

function convertRadianToDegree(radian) {
    return radian * (180 / Math.PI);
}
