function contentLoaded() {
    if(window.location.href.includes("error")) {
        document.getElementsByName("error").forEach(function(message) {
            message.removeAttribute("hidden");
        });
    }
}