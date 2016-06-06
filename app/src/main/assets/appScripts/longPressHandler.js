console.log("Long-press handler initiated!");
if(!window.jQuery) {
    console.log("jQuery not found, binding it!");
    var script = document.createElement("script");
    script.src = "https://code.jquery.com/jquery-2.2.3.min.js";
    script.type = "text/javascript";
    document.getElementsByTagName("head")[0].appendChild(script);
}


console.log("jQuery loaded, starting long-press integration");
var onlongtouchlinks;
var onlongtouchimages;
var timerforlinks;
var touchdurationforlinks = 1000;
var currentLinkTitle  = "";
var currentLinkUrl    = "";
var currentImageTitle = "";

/* Handle long-press on links */

function checkVars() {
    if(typeof currentLinkUrl    === "undefined" || currentLinkUrl    === "") currentLinkUrl    = " ";
    if(typeof currentLinkTitle  === "undefined" || currentLinkTitle  === "") currentLinkTitle  = " ";
    if(typeof currentImageTitle === "undefined" || currentImageTitle === "") currentImageTitle = " ";
}

onlongtouchlinks = function() {
    console.log("CornHandler://handleLongpressLink:"
        + currentLinkTitle.replace(":", "::") + ":" + currentLinkUrl.replace(":", "::"));
};

$("a").on("touchstart", function(event) {
    timerforlinks = setTimeout(onlongtouchlinks, touchdurationforlinks);
    currentLinkTitle  = event.target.innerHTML;
    currentLinkUrl    = event.target.getAttribute("href");
    currentImageTitle = "";
    checkVars();
});

$("a").on("touchend", function() {
    // Prevents short touches from firing the event
    if (timerforlinks)
        clearTimeout(timerforlinks);
});

/* Handle long-press on images */

onlongtouchimages = function() {
    console.log("CornHandler://handleLongpressImage:"
        + currentLinkUrl.replace(":", "::") + ":" + currentImageTitle.replace(":", "::"));
};

$("img").on("touchstart", function(event) {
    timerforlinks = setTimeout(onlongtouchimages, touchdurationforlinks);
    currentLinkTitle  = event.target.getAttribute("alt");
    currentLinkUrl    = event.target.getAttribute("src");
    currentImageTitle = event.target.getAttribute("alt");
    checkVars();
});

$("img").on("touchend", function() {
    // Prevents short touches from firing the event
    if (timerforlinks)
        clearTimeout(timerforlinks);
});