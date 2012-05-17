/**
 * This is the JavaScript module for the main
 * menu. Normally you can find this menu at the top
 * of the page with the large icons.
 *
 * Copyright Collibra NV/SA
 * @author <a href="mailto:clovis@collibra.com">Clovis Six</a>
 * @module modules/menus/main
 * @name MainMenu
 * @namespace modules/menus
 */
/*global define,require,core */
define('modules/menus/main', ['core', 'modules/text'], function(require, exports, module) {
  "use strict";

  /*************************************
   *           Requirements            *
   *************************************/

  /** @private */
  var core = require('core'),
      $j = core.libs.get('jquery');

  /*************************************
   *              Private              *
   *************************************/

  // !TODO: Arrange JS and adapt to our conventions.
  $j(document).ready(function() {
    var mainMenuPointer = $j('#mainmenu').find('.contextarrow'),
        selectedMenuItem = "mainmenuglossary",
        menuItemLabels = $j('.menuitem .label');

    require.async('modules/text', function(text) {
      text.autoSize(menuItemLabels, 9, 12, false);
      var interval = text.maxSizes(menuItemLabels);
      menuItemLabels.css('font-size', interval.min);
    });

    /**
     * Calculate the left position for a specific main menu item.
     *
     * @param menuitemid The id used to identify a menu item by.
     */
    function calculateArrowPosition(menuitemid) {
      if (menuitemid === '') {
        return;
      }

      var menuElement = $j("#" + menuitemid);

      if (menuElement.length > 0) {
        return {
          'left': menuElement.position().left + (menuElement.outerWidth() / 2 - mainMenuPointer.outerWidth() / 2)
        };
      } else {
        return {};
      }
    }

    // Enable animation while hovering over a main menu item.
    $j('#mainmenu').find('.menuitem').hover(function(ev) {
      mainMenuPointer.stop().animate(calculateArrowPosition($j(ev.target).attr('id')), 500);
    }, function(ev) {
      mainMenuPointer.stop().animate(calculateArrowPosition(selectedMenuItem), 500);
    }).click(function(ev) {
      selectedMenuItem = $j(ev.target).attr('id');
    });

    mainMenuPointer.css(calculateArrowPosition(selectedMenuItem));
  });
});

core.module.alias.add('MainMenu', 'modules/menus/main');
core.module.use('MainMenu');