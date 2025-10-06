let earthContainer;
let earth;
let searchBarContainer;
let originCity = null;
let destinationCity = null;
let labeledCities = [];
let originCityElement;
let destinationCityElement;
let lineDatas = {startLat: null, startLng: null, endLat: null, endLng: null};
let timesNewRomanCharacterset;

function bodyLoaded() {
    earthContainer = document.getElementById("main");
    searchBarContainer = document.getElementById("searchBar");
    initEarth();
    earth.globeOffset([0, getHeightOfElementWithMargin(searchBarContainer) / 2]);

    originCityElement = document.getElementById("originCity");
    destinationCityElement = document.getElementById("destinationCity");
    resetInputStatusAndEarthAfterError();
}

function initEarth() {
    earth = new Globe(earthContainer)
        .globeImageUrl("//cdn.jsdelivr.net/npm/three-globe/example/img/earth-day.jpg")
        .backgroundImageUrl("//cdn.jsdelivr.net/npm/three-globe/example/img/night-sky.png");

    earth.controls().autoRotate = true;
    earth.controls().autoRotateSpeed = 0.6;
    window.addEventListener("resize", resizeEarth);
    readCharacterset();
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
    originCityElement = document.getElementById("originCity");
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
                    if (earth !== undefined) {
                        labeledCities.push(cityDetails);
                        labelCityAndNavigate(cityDetails);
                        lineDatas.startLat = cityDetails.lat;
                        lineDatas.startLng = cityDetails.lng;
                        connectCitesIfAllGiven();
                    }
                    originCity = cityDetails;
                    window.localStorage.setItem("originCity", JSON.stringify(cityDetails));
                } catch (exception) {
                    originCityElement.classList.add("border-danger");
                }
            });
    }
    originCityElement.classList.remove("border-danger");
}

function goToDestinationCity() {
    destinationCityElement = document.getElementById("destinationCity");
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
                    if (earth !== undefined) {
                        labeledCities.push(cityDetails);
                        labelCityAndNavigate(cityDetails);
                        lineDatas.endLat = cityDetails.lat;
                        lineDatas.endLng = cityDetails.lng;
                        connectCitesIfAllGiven();
                    }
                    destinationCity = cityDetails;
                    window.localStorage.setItem("destinationCity", JSON.stringify(cityDetails));
                } catch (exception) {
                    destinationCityElement.classList.add("border-danger");
                }
            });
    }
    destinationCityElement.classList.remove("border-danger");
}

function labelCityAndNavigate(cityDetails) {
    earth.controls().autoRotate = false;
    labelCity(cityDetails);
    earth.pointOfView({lat: cityDetails.lat, lng: cityDetails.lng, altitude: 1}, 3000);
}

function labelCity() {
    earth.labelsData(labeledCities)
        .labelTypeFace(timesNewRomanCharacterset)
        .labelLat(data => data.lat)
        .labelLng(data => data.lng)
        .labelText(data => data.text)
        .labelSize(1)
        .labelDotRadius(1)
        .labelColor(() => "black");
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

function resetInputStatusAndEarthAfterError() {
    goToOriginCity();
    goToDestinationCity();
    isOneWay();
}

function readCharacterset() {
    fetch("https://raw.githubusercontent.com/mrdoob/three.js/dev/examples/fonts/gentilis_bold.typeface.json")
        .then(response => response.json())
        .then(data => timesNewRomanCharacterset = data);
}