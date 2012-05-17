// Add the icontains expression to search for strings but case-insensitive.
jQuery.expr[':'].icontains = function(a, i, m) {
  return core.libs.get('jquery')(a).text().toUpperCase()
      .indexOf(m[3].toUpperCase()) >= 0;
};

// Add jQuery to the core and deny global access to it.
core.libs.add('jquery', jQuery.noConflict(), true);

if (!(jQuery.browser.msie && jQuery.browser.version < 9)) {
  delete jQuery;
  delete window.$;
  delete window.jQuery;
}