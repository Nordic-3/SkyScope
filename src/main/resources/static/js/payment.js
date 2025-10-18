function contentLoaded() {
    if (window.location.href.includes("success"))
        new bootstrap.Modal(document.getElementById("success")).show();
}