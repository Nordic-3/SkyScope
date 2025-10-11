let isOnceShowedCheaper = false;
let cheaperOfferToast;

function offersLoaded() {
    document.getElementById(getUrlParameter()).classList.add("active");
    let searchId = window.location.pathname.split("/")[2];
    cheaperOfferToast = document.getElementById("cheaperOfferToast");
    cheaperOfferToast.addEventListener("hide.bs.toast", dontShowAgain);
    let cheaperOfferBtn = document.getElementById("cheaperOfferBtn");
    cheaperOfferBtn.addEventListener("click", dontShowAgain);
    if (!isOnceShowedCheaper) {
        checkResults(searchId);
    }
    window.localStorage.setItem("selectedOffer", document.getElementById("book").attributes.getNamedItem("href").value);
}

function getUrlParameter() {
    return new URLSearchParams(window.location.search).get("by");
}

function checkResults(searchId) {
    fetch("/cheaperOffer/" + searchId)
        .then(response => {
            if (response.status === 200) {
                isOnceShowedCheaper = true;
                if (window.localStorage.getItem("dontShowAgain") !== "true") {
                    bootstrap.Toast.getOrCreateInstance(cheaperOfferToast).show();
                }
            } else {
                setTimeout(() => checkResults(searchId), 2000);
            }
        })
        .catch(err => console.error(err));
}

function dontShowAgain() {
    window.localStorage.setItem("dontShowAgain", "true");
}
