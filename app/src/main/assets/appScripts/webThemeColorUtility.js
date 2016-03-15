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
        else if(cu.indexOf("plus.google")       !== -1) webCol = "#d32f2f";
        else if(cu.indexOf("play.google")       !== -1) webCol = "#60c416";
        else if(cu.indexOf("google")            !== -1) webCol = "#484848";
        else if(cu.indexOf("twitter")           !== -1) webCol = "#55acee";
        else if(cu.indexOf("youtube")           !== -1) webCol = "#dd0804";
        else if(cu.indexOf("xda-developers")    !== -1) webCol = "#de7300";
        else if(cu.indexOf("yahoo")             !== -1) webCol = "#400090";
        else if(cu.indexOf("twitch")            !== -1) webCol = "#6441a5";
        else if(cu.indexOf("stackoverflow")     !== -1) webCol = "#23334e";
        else if(cu.indexOf("androidfilehost")   !== -1) webCol = "#222222";
        else if(cu.indexOf("codepen")           !== -1) webCol = "#1d1f20";
        else if(cu.indexOf("jsfiddle")          !== -1) webCol = "#323232";
        else if(cu.indexOf("agar")              !== -1) webCol = "#428bca";

        else webCol = "default";

    }
} catch(ex) {
    webCol = "default";
}

if(webCol === "") webCol = "default";


console.log("CornHandler://setWebThemeColor:" + webCol);

