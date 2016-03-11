/*
 * Cornowser
 * Copyright (C) 2015-2016 Simao Gomes Viana
 *
 * Thanks a lot to my helpful team mate @Thunderbottom
 * Thanks a lot also to all translators and other contributors, as well as
 * the supporters and users of Cornowser.
 * Thanks a lot to the authors and developers of the dependencies needed by
 * this application.
 * Thanks a lot to everyone who help us!
 * And thanks for using this application.
 *
 * PLEASE DON'T CREATE BACKSPACEWARE OF THIS APPLICATION!
 * Feel free to contribute and share but don't steal the application and
 * say it's fully yours. Just say that it is based on Cornowser which
 * is made by us. And don't forget to say that you like Cornowser ;D
 * Thank you.
 *
 * ---
 *
 * This project/application is licensed under the MIT license.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.xdevs23.cornowser.browser;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.xdevs23.config.ConfigUtils;
import org.xdevs23.debugutils.Logging;

import io.xdevs23.cornowser.browser.browser.BrowserStorage;

/**
 * This is the core of the application
 */
public class Core extends Application {

    protected static Context coreContext;

    protected static Core applicationCore;

    @Override
    public void onCreate() {
        Logging.logd("PRE INIT START");

        applicationCore = this;

        coreContext = this.getApplicationContext();

        SharedPreferences sp = coreContext.getSharedPreferences("userprefs", 0);
        ConfigUtils.forceDebug = sp.getBoolean(BrowserStorage.BPrefKeys.debugModePref, false);

        if(ConfigUtils.forceDebug) Logging.logd("DEBUG MODE IS FORCE-ENABLED.\nPre init finished!");

        super.onCreate();
    }

}
