// Add Modernizr to the core and deny global access to it.
core.libs.add('underscore', _.noConflict());

if (!(core.detect.browser.IE && core.detect.browser.VERSION < 9)) {
  delete window._;
  delete _;
}