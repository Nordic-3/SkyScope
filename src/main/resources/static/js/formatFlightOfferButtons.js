function formatSortButton() {
    document.getElementById(getUrlParameter()).classList.add("active");

}

function getUrlParameter() {
    return new URLSearchParams(window.location.search).get("by");
}