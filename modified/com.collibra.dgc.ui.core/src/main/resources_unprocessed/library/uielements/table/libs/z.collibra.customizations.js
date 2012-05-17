/**
 * Add FixedHeader to jQuery (for uielements).
 *
 * Copyright Collibra NV/SA
 * @author Grzegorz Pawlowski <grzegorz@collibra.com>
 */
(function() {

  //set FixedHeader
  $.fn.tableFixedHeader = function(config) {
    var header = new FixedHeader(this, config);

    this.data('fixedHeader', header);

    return header;
  };

  //set displayed pagination pages to 10
  jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10
  
  //add sorting.
  jQuery.fn.dataTableExt.oSort['natural-asc'] = function(x, y) {
    return core.sort.comparators.natural(x, y);
  }
   
  jQuery.fn.dataTableExt.oSort['natural-desc'] = function(x, y) {
    return core.sort.comparators.natural(x, y, false, true);
  }
})();