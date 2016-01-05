function dc() {
    var webCol;
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
                    webCol = "default";
                }
            }
        }
    }

    document.location.href = "CornHandler://setWebThemeColor:" + webCol;
    return true;
}

dc();