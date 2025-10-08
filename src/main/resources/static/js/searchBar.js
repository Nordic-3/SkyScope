let inGoingInput;
let oneWay = false;

function searchBarLoaded() {
    inGoingInput = document.getElementById("inGoingDate");
    if (document.getElementsByClassName("text-danger") !== null) {
        document.getElementById("loadingIndicator").setAttribute("hidden", "");
    }
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
    oneWay = document.getElementById("onlyOneWay").checked;
    inGoingInput.disabled = oneWay;
}

function showLoadingIndicator() {
    document.getElementById("loadingIndicator").removeAttribute("hidden");
}