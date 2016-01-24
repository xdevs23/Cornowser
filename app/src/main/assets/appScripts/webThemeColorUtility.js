var webCol = "default";
try {
    webCol = document.querySelector("meta[name='theme-color']").getAttribute("content").toString();
} catch(a) {
    try {
        webCol = document.querySelector("meta[name='apple-mobile-web-app-status-bar-style']").getAttribute("content").toString();
    } catch(b) {
        try {
            webCol = document.querySelector("meta[name='msapplication-navbutton-color']").getAttribute("content").toString();
        } catch(c) {
            try {
                webCol = document.querySelector("meta[name='web-color']").getAttribute("content").toString();
            } catch(d) {
                try {
                    webCol = document.querySelector("meta[name='msapplication-TileColor']").getAttribute("content").toString();
                } catch(e) {
                    webCol = "default";
                }
            }
        }
    }
}

try {
    if(webCol === "default") {
        var cu = document.location.host;

        if     (cu.indexOf("facebook")  !== -1) webCol = "#3b5998";
        else if(cu.indexOf("google")    !== -1) webCol = "#484848";
        else if(cu.indexOf("twitter")   !== -1) webCol = "#55acee";
        else if(cu.indexOf("youtube")   !== -1) webCol = "#dd0804";

        else webCol = "default";

    }
} catch(ex) {}

function tryElem(elem, attr) {
    if(webCol !== "default") return webCol;
    try {
        var dega = document.getElementById(elem).getAttribute(attr);
        if(dega !== "") return dega;
    } catch(ex) {

    }
    try {
        return document.getElementById(elem).style.backgroundColor;
    } catch(ex) {
        return "default";
    }
}

try {
    if(webCol === "default") {
        webCol = tryElem("navbar", "color");
        webCol = tryElem("titlebar", "color");
        webCol = tryElem("title", "color");
        webCol = tryElem("navigation", "color");
    }
    if(webCol === "") webCol = "default";
    if(webCol.toUpperCase() === "#FFFFFF") webCol = "default";
} catch(ex) {
    webCol = "default";
}


console.log("CornHandler://setWebThemeColor:" + webCol);
