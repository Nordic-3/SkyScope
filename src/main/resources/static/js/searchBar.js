let inGoingInput;
let oneWay = false;

function searchBarLoaded() {
    inGoingInput = document.getElementById("inGoingDate");
}

function setInGoingMin() {
    if (!oneWay) {
        inGoingInput.min = document.getElementById("outGoingDate").value;
    }
}

function setOutGoingMin() {
    let inGoingDate = document.getElementById("inGoingDate").value;
    document.getElementById("outGoingDate").max = inGoingDate;
}

function isOneWay() {
    oneWay = document.getElementById("onlyOrigin").checked;
    inGoingInput.disabled = oneWay;
}