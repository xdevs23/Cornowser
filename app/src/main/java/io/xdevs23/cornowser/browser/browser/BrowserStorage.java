package io.xdevs23.cornowser.browser.browser;

public class BrowserStorage {

    public BrowserStorage() {

    }

    private String
            userHomePage = BrowserDefaults.HOME_URL
            ;

    public void setUserHomePage(String url) {
        this.userHomePage = url;
    }

    public String getUserHomePage() {
        return userHomePage;
    }

}
