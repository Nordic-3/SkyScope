function contentLoaded() {
    bootstrap.Toast.getOrCreateInstance(document.getElementById("liveToast")).show();
    document.getElementById("next").addEventListener("click", function() {
        document.getElementById("loadingIndicator").removeAttribute("hidden");
    });
    if (document.getElementsByClassName("text-danger").length > 0) {
        document.getElementById("loadingIndicator").setAttribute("hidden", "");
    }
}