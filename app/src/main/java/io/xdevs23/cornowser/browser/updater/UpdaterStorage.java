package io.xdevs23.cornowser.browser.updater;


public class UpdaterStorage {

    public static final String
            URL_RAW_GITHUB_USERCONTENT  = "https://raw.githubusercontent.com",
            URL_RAW_CB_GITHUB_REPO      = URL_RAW_GITHUB_USERCONTENT + "/xdevs23/Cornowser/master",
            URL_RAW_UBP_GITHUB_REPO     = URL_RAW_CB_GITHUB_REPO,

            URL_VERSION_CODE            = URL_RAW_UBP_GITHUB_REPO + "/update/rel.txt",
            URL_VERSION_NAME            = URL_RAW_UBP_GITHUB_REPO + "/update/version.txt",

            URL_CHANGELOG               = URL_RAW_UBP_GITHUB_REPO + "/update/changelog.txt",

            URL_APK                     = URL_RAW_UBP_GITHUB_REPO + "/update/Cornowser.apk"
            ;

}
