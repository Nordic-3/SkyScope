let earthContainer;
let earth;
let searchBarContainer;
let originCity = null;
let destinationCity = null;
let labeledCities = [];
let originCityElement;
let destinationCityElement;
let lineDatas = {startLat: null, startLng: null, endLat: null, endLng: null};

function bodyLoaded() {
    earthContainer = document.getElementById("main");
    searchBarContainer = document.getElementById("searchBar");
    earth = new Globe(earthContainer)
        .globeImageUrl("//cdn.jsdelivr.net/npm/three-globe/example/img/earth-day.jpg")
        .backgroundImageUrl("//cdn.jsdelivr.net/npm/three-globe/example/img/night-sky.png")
        .globeOffset([0, getHeightOfElementWithMargin(searchBarContainer) / 2]);

    earth.controls().autoRotate = true;
    earth.controls().autoRotateSpeed = 0.6;

    window.addEventListener("resize", resizeEarth);

    originCityElement = document.getElementById("originCity");
    destinationCityElement = document.getElementById("destinationCity");
}

function resizeEarth() {
    earth.width(window.innerWidth);
    earth.height(window.innerHeight);
}

function getHeightOfElementWithMargin(element) {
    let styles = window.getComputedStyle(element);
    let margin = parseFloat(styles["marginTop"]) + parseFloat(styles["marginBottom"]);
    return Math.ceil(element.offsetHeight + margin);
}

function goToOriginCity() {
    let inputOriginCity = originCityElement.value;
    if (originCity !== null && inputOriginCity === "") {
        removeCityFromEarth(originCity);
        originCity = null;
        setEarthToIdleIfEmptyInput();
        return;
    } else if (originCity !== null) {
        removeCityFromEarth(originCity);
    }
    if (inputOriginCity !== "") {
        fetch("/city?city=" + inputOriginCity)
            .then(response => response.text())
            .then(responseInText => {
                try {
                    let responseJSON = JSON.parse(responseInText);
                    let cityDetails = {lat: responseJSON.lat, lng: responseJSON.lng, text: responseJSON.name};
                    labeledCities.push(cityDetails);
                    labelCityAndNavigate(cityDetails);
                    lineDatas.startLat = cityDetails.lat;
                    lineDatas.startLng = cityDetails.lng;
                    connectCitesIfAllGiven();
                    originCity = cityDetails;
                } catch (exception) {
                    originCityElement.classList.add("border-danger");
                }
            });
    }
    originCityElement.classList.remove("border-danger");
}

function goToDestinationCity() {
    let inputDestinationCity = destinationCityElement.value;
    if (destinationCity !== null && inputDestinationCity === "") {
        removeCityFromEarth(destinationCity);
        destinationCity = null;
        setEarthToIdleIfEmptyInput();
        return;
    } else if (destinationCity !== null) {
        removeCityFromEarth(destinationCity);
    }
    if (inputDestinationCity !== "") {
        fetch("/city?city=" + inputDestinationCity)
            .then(response => response.text())
            .then(responseInText => {
                try {
                    let responseJSON = JSON.parse(responseInText);
                    let cityDetails = {lat: responseJSON.lat, lng: responseJSON.lng, text: responseJSON.name};
                    labeledCities.push(cityDetails);
                    labelCityAndNavigate(cityDetails);
                    lineDatas.endLat = cityDetails.lat;
                    lineDatas.endLng = cityDetails.lng;
                    connectCitesIfAllGiven();
                    destinationCity = cityDetails;
                } catch (exception) {
                    destinationCityElement.classList.add("border-danger");
                }
            });
    }
    destinationCityElement.classList.remove("border-danger");
}

function labelCityAndNavigate(cityDetails) {
    earth.controls().autoRotate = false;
    earth.labelsData(labeledCities)
        .labelLat(data => data.lat)
        .labelLng(data => data.lng)
        .labelText(data => data.text)
        .labelSize(1)
        .labelDotRadius(1)
        .labelColor(() => "black");
    earth.pointOfView({lat: cityDetails.lat, lng: cityDetails.lng, altitude: 1}, 3000);
}

function removeCityFromEarth(city) {
    labeledCities = labeledCities.filter(label => label.text !== city.text);
    if (lineDatas.startLat === city.lat && lineDatas.startLng === city.lng) {
        lineDatas.startLat = null;
        lineDatas.startLng = null;
    } else if (lineDatas.endLat === city.lat && lineDatas.endLng === city.lng) {
        lineDatas.endLat = null;
        lineDatas.endLng = null;
    }
    earth.labelsData(labeledCities)
        .arcsData([]);

}

function setEarthToIdleIfEmptyInput() {
    if (originCity === null && destinationCity === null) {
        earth.pointOfView({lat: 0, lng: 0, altitude: 2.5}, 2000);
        earth.controls().autoRotate = true;
    }
}

function connectCitesIfAllGiven() {
    if (!Object.values(lineDatas).includes(null)) {
        earth.arcsData([lineDatas])
            .arcColor(() => "#ff0000")
            .arcsTransitionDuration(4000)
            .arcDashAnimateTime(3000);
    }
}