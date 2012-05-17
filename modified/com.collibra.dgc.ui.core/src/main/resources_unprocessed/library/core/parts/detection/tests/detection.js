/*global
TestCase
core
*/
TestCase("CORE :: detection :: detection.js :: detect :: browser", {
  detect: core.detect,
  
  testBrowser: function() {
    var _self = this,
        browser = _self.detect.browser,
        feature = _self.detect.feature,
        $ = core.libs.get("jquery");
    
    
    
    assertEquals("Is ie6", !!$.browser.msie && $.browser.version === "6.0", browser.IE6);
    assertEquals("Is ie7", !!$.browser.msie && $.browser.version === "7.0", browser.IE7);
    assertEquals("Is ie8", !!$.browser.msie && $.browser.version === "8.0", browser.IE8);
    assertEquals("Is ie9", !!$.browser.msie && $.browser.version === "9.0", browser.IE9);
    assertEquals("Is ie10", !!$.browser.msie && $.browser.version === "10.0", browser.IE10);
    
    assertEquals("Is webkit", !!$.browser.webkit, browser.WEBKIT && browser.ENGINE === "webkit");
    assertEquals("Is trident", !!$.browser.msie, browser.TRIDENT && browser.ENGINE === "trident");
    assertEquals("Is gecko", !!$.browser.mozilla, browser.GECKO && browser.ENGINE === "gecko");
    assertEquals("Is presto", !!$.browser.opera, browser.PRESTO && browser.ENGINE === "presto");
    
    assertEquals("Is ie, trident, TRIDENT", !!$.browser.msie, browser.IE && browser.ENGINE === "trident" && browser.TRIDENT);
    assertEquals("Is OPERA,presto, PRESTO", !!$.browser.opera, browser.OPERA && browser.ENGINE === "presto" && browser.PRESTO);
    assertEquals("Is CHROME,webkit,WEBKIT", !!$.browser.chrome, browser.CHROME && browser.ENGINE === "webkit" && browser.WEBKIT);
    
    //This commented assert below is prob platform dependent. $.browser.safari under chrome(windows) returns true, while $.browser.chrome is undefined
    //userAgent regexp need to be more sophisticated. My example ua: ["Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11"]
    //assertEquals("Is SAFARI,webkit,WEBKIT", !!$.browser.safari, browser.SAFARI && browser.ENGINE === "webkit" && browser.WEBKIT);
    
    assertEquals("Is FIREFOX,gecko,GECKO", !!$.browser.firefox, browser.FIREFOX && browser.ENGINE === "gecko" && browser.GECKO);
    
    assertEquals("Is current version", parseInt($.browser.version, 10), parseInt(browser.VERSION, 10));
    
    
  }
});