var css = 'body { -webkit-filter: hue-rotate(180deg) invert(1); }';

var head = document.getElementsByTagName('head')[0];

var style = document.createElement('style');

style.type = 'text/css';
if (style.styleSheet) {
    style.styleSheet.cssText = css;
} else {
    style.appendChild(document.createTextNode(css));
}

//injecting the css to the head
head.appendChild(style);

try {
    var oldStyles = document.getElementsByTagName('style');
    var oldStyle = oldStyles[oldStyles.length - 2];
    head.removeChild(oldStyle);
} catch(ex) { /* Do nothing */ }