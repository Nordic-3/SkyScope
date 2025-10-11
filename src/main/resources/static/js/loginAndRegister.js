function contentLoaded() {
    if(window.location.href.includes("error")) {
        document.getElementsByName("error").forEach(function(message) {
            message.removeAttribute("hidden");
        });
    }
    if (window.location.href.includes("invalidPassword")) {
        document.getElementById("reg").click();
    }
    if (window.location.href.includes("success")) {
        window.location.href = window.localStorage.getItem("selectedOffer");
    }
}