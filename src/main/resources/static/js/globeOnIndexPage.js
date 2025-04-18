let earthContainer;
let earth;
let searchBarContainer;
let originCity;
let destinationCity;
let labeledCities = [];
let dummyData = [{
    lat: 47.4979,
    lng: 19.0402,
    text: "Budapest"
},
    {
        lat: 41.38879,
        lng: 2.15899,
        text: "Barcelona"
    },
{
    lat: 51.509865,
    lng: -0.118092,
    text: "London"
}];

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
    let inputOriginCity = document.getElementById("originCity").value;
    if (originCity !== null && inputOriginCity === "") {
        removeCityFromLabeledCities(originCity);
        originCity = null;
        setEarthToIdleIfEmptyInput();
        return;
    } else if (originCity !== null) {
        removeCityFromLabeledCities(originCity);
    }
    let cityDetails = getCityFromDataSet(inputOriginCity);
    if (cityDetails !== null) {
        labeledCities.push(cityDetails);
        labelCityAndNavigate(cityDetails);
        originCity = inputOriginCity;
    }
}

function goToDestinationCity() {
    let inputDestinationCity = document.getElementById("destinationCity").value;
    if (destinationCity !== null && inputDestinationCity === "") {
        removeCityFromLabeledCities(destinationCity);
        destinationCity = null;
        setEarthToIdleIfEmptyInput();
        return;
    } else if (destinationCity !== null) {
        removeCityFromLabeledCities(destinationCity);
    }
    let cityDetails = getCityFromDataSet(inputDestinationCity);
    if (cityDetails !== null) {
        labeledCities.push(cityDetails);
        labelCityAndNavigate(cityDetails);
        destinationCity = inputDestinationCity;
    }
}

function getCityFromDataSet(city) {
    for (let i = 0; i < dummyData.length; i++) {
        if (dummyData[i].text === city) {
            return dummyData[i];
        }
    }
    return null;
}

function labelCityAndNavigate(cityDetails) {
    earth.controls().autoRotate = false;
    earth.labelsData(labeledCities)
        .labelLat(data => data.lat)
        .labelLng(data => data.lng)
        .labelText(data => data.text)
        .labelSize(1)
        .labelDotRadius(1);
        earth.pointOfView({lat: cityDetails.lat, lng: cityDetails.lng, altitude: 1}, 3000);
}

function removeCityFromLabeledCities(city) {
    labeledCities = labeledCities.filter(label => label.text !== city);
    earth.labelsData(labeledCities);
}

function setEarthToIdleIfEmptyInput() {
    if (originCity === null && destinationCity === null) {
        earth.pointOfView({lat: 0, lng: 0, altitude: 2.5}, 2000);
        earth.controls().autoRotate = true;
    }
}