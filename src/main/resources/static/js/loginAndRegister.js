function contentLoaded() {
    if(window.location.href.includes("error")) {
       document.getElementById("error").removeAttribute("hidden");
    }
    if (window.location.href.includes("invalidPassword")) {
        document.getElementById("reg").click();
    }
}