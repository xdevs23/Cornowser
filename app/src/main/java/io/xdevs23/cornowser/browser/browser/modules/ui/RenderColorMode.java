package io.xdevs23.cornowser.browser.browser.modules.ui;

public class RenderColorMode {

    public static class ColorMode {

        public static final int
                NORMAL      = 0,
                DARK        = 1,
                NEGATIVE    = 2,
                INVERT      = 3,
                GREYSCALE   = 4
                        ;

        public int mode = NORMAL;

        public ColorMode setColorMode(int colorMode) {
            mode = colorMode;
            return this;
        }

    }

    public static ColorMode toColorMode(int colorMode) {
        return (new ColorMode()).setColorMode(colorMode);
    }

}
