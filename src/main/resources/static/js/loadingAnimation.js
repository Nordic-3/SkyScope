function checkResults(searchId) {
    fetch('/results/' + searchId)
        .then(response => {
            if (response.status === 200) {
                window.location.href = '/resultsPage/' + searchId;
            } else {
                setTimeout(() => checkResults(searchId), 2000);
            }
        })
        .catch(err => console.error(err));
}

function contentLoaded() {
    let searchId = document.getElementById("searchId").value;
    earthContainer = document.getElementById("main");
    checkResults(searchId);
    initEarth();
    earth.globeOffset([0, window.height / 2]);
    labelCitiesAndConnect();
    earth.controls().autoRotate = true;
    earth.controls().autoRotateSpeed = 5;
}

function labelCitiesAndConnect() {
    labelCityOnLoading(JSON.parse(window.localStorage.getItem('originCity')));
    labelCityOnLoading(JSON.parse(window.localStorage.getItem('destinationCity')));
    lineDatas.startLat = labeledCities.at(0).lat;
    lineDatas.startLng = labeledCities.at(0).lng;
    lineDatas.endLat = labeledCities.at(1).lat;
    lineDatas.endLng = labeledCities.at(1).lng;
    connectCitesIfAllGiven();
}

function labelCityOnLoading(cityDetails) {
    labeledCities.push(cityDetails);
    labelCity();
}