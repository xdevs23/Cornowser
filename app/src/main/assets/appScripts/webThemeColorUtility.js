var webCol = "default";
try {
    webCol = document.querySelector("meta[name='theme-color']").getAttribute("content").toString();
} catch(a) {
    try {
        webCol = document.querySelector("meta[name='msapplication-TileColor']").getAttribute("content").toString();
    } catch(b) {
        try {
            webCol = document.querySelector("meta[name='web-color']").getAttribute("content").toString();
        } catch(c) {
            try {
                webCol = document.querySelector("meta[name='apple-mobile-web-app-status-bar-style']").getAttribute("content").toString();
            } catch(d) {
                try {
                    webCol = document.querySelector("meta[name='msapplication-navbutton-color']").getAttribute("content").toString();
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

        if     (cu.indexOf("facebook")          !== -1) webCol = "#3b5998";
        else if(cu.indexOf("google")            !== -1) webCol = "#484848";
        else if(cu.indexOf("twitter")           !== -1) webCol = "#55acee";
        else if(cu.indexOf("youtube")           !== -1) webCol = "#dd0804";
        else if(cu.indexOf("xda-developers")    !== -1) webCol = "#de7300";

        else webCol = "default";

    }
} catch(ex) {
    webCol = "default";
}

if(webCol === "") webCol = "default";


console.log("CornHandler://setWebThemeColor:" + webCol);

