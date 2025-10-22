function contentLoaded() {
    document.getElementById("payBtn").addEventListener("click", function() {
        document.getElementById("loadingIndicator").removeAttribute("hidden");
    });
}