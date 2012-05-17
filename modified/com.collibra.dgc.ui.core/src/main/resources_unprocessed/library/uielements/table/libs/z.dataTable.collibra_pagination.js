/**
 * Adding new pagination. (based on dataTable's "full_numbers" pagination).
 *
 * Copyright Collibra NV/SA
 * @author Grzegorz Pawlowski <grzegorz@collibra.com>
 */
$.fn.dataTableExt.oPagination.collibra_pagination = {
      /*
       * Function: oPagination.collibra_pagination.fnInit
       * Purpose:  Initialise dom elements required for pagination with a list of the pages
       * Returns:  -
       * Inputs:   object:oSettings - dataTables settings object
       *           node:nPaging - the DIV which contains this pagination control
       *           function:fnCallbackDraw - draw function which must be called on update
       */
      "fnInit": function ( oSettings, nPaging, fnCallbackDraw )
      {
        var oLang = oSettings.oLanguage.oPaginate,
            oClasses = oSettings.oClasses,
            fnClickHandler = function ( e ) {
              if (oSettings.oApi._fnPageChange( oSettings, e.data.action )) {
                fnCallbackDraw( oSettings );
              }
            };
  
        $(nPaging).append(
          '<a  tabindex="'+oSettings.iTabIndex+'" class="'+oClasses.sPageButton+" "+oClasses.sPageFirst+'">'+oLang.sFirst+'</a>'+
          '<a  tabindex="'+oSettings.iTabIndex+'" class="'+oClasses.sPageButton+" "+oClasses.sPagePrevious+'">'+oLang.sPrevious+'</a>'+
          '<span></span>'+
          '<a tabindex="'+oSettings.iTabIndex+'" class="'+oClasses.sPageButton+" "+oClasses.sPageNext+'">'+oLang.sNext+'</a>'+
          '<a tabindex="'+oSettings.iTabIndex+'" class="'+oClasses.sPageButton+" "+oClasses.sPageLast+'">'+oLang.sLast+'</a>'
        );
        var els = $('a', nPaging),
            nFirst = els[0],
            nPrev = els[1],
            nNext = els[2],
            nLast = els[3];
        
        oSettings.oApi._fnBindAction( nFirst, {action: "first"},    fnClickHandler );
        oSettings.oApi._fnBindAction( nPrev,  {action: "previous"}, fnClickHandler );
        oSettings.oApi._fnBindAction( nNext,  {action: "next"},     fnClickHandler );
        oSettings.oApi._fnBindAction( nLast,  {action: "last"},     fnClickHandler );
        
        /* ID the first elements only */
        if ( !oSettings.aanFeatures.p ) {
          nPaging.id = oSettings.sTableId+'_paginate';
          nFirst.id =oSettings.sTableId+'_first';
          nPrev.id =oSettings.sTableId+'_previous';
          nNext.id =oSettings.sTableId+'_next';
          nLast.id =oSettings.sTableId+'_last';
        }
      },
      
      /*
       * Function: oPagination.full_numbers.fnUpdate
       * Purpose:  Update the list of page buttons shows
       * Returns:  -
       * Inputs:   object:oSettings - dataTables settings object
       *           function:fnCallbackDraw - draw function to call on page change
       */
      "fnUpdate": function ( oSettings, fnCallbackDraw )
      {
        if (!oSettings.aanFeatures.p) {
          return;
        }
        
        var iPageCount = jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages,
            iPageCountHalf = Math.floor(iPageCount / 2),
            iPages = Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength),
            iCurrentPage = Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1,
            sList = "",
            iStartButton, iEndButton, i, iLen,
            oClasses = oSettings.oClasses,
            anButtons, anStatic, nPaginateList,
            an = oSettings.aanFeatures.p,
            fnBind = function (j) {
              oSettings.oApi._fnBindAction( this, {"page": j+iStartButton-1}, function(e) {
              /* Use the information in the element to jump to the required page */
              oSettings.oApi._fnPageChange( oSettings, e.data.page );
              fnCallbackDraw( oSettings );
              e.preventDefault();
              } );
            };
        
        /* Pages calculation */
        if (oSettings._iDisplayLength === -1) {
          iStartButton = 1;
          iEndButton = 1;
          iCurrentPage = 1;
        } else if (iPages < iPageCount) {
          //All page numbers are displayed.
          iStartButton = 1;
          iEndButton = iPages;
        } else if (iCurrentPage <= iPageCountHalf) {
          iStartButton = 1;
          iEndButton = iPageCount;
        } else if (iCurrentPage >= (iPages - iPageCountHalf)) {
          iStartButton = iPages - iPageCount + 1;
          iEndButton = iPages;
        } else {
          iStartButton = iCurrentPage - Math.ceil(iPageCount / 2) + 1;
          iEndButton = iStartButton + iPageCount - 1;
        }
  
        
        /* Build the dynamic list */
        for ( i=iStartButton ; i<=iEndButton ; i++ ) {
          sList += (iCurrentPage !== i) ?
            '<a tabindex="'+oSettings.iTabIndex+'" class="'+oClasses.sPageButton+'">'+oSettings.fnFormatNumber(i)+'</a>' :
            '<a tabindex="'+oSettings.iTabIndex+'" class="'+oClasses.sPageButtonActive+'">'+oSettings.fnFormatNumber(i)+'</a>';
        }
        
        /* Loop over each instance of the pager */
        for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
          if ( an[i].childNodes.length === 0 ) {
            continue;
          }
          
          /* Build up the dynamic list forst - html and listeners */
          $('span:eq(0)', an[i])
            .html( sList )
            .children('a').each( fnBind );
          
          /* Update the premanent botton's classes */
          anButtons = an[i].getElementsByTagName('a');
          anStatic = [
            anButtons[0], anButtons[1], 
            anButtons[anButtons.length-2], anButtons[anButtons.length-1]
          ];

          $(anStatic).removeClass( oClasses.sPageButton+" "+oClasses.sPageButtonActive+" "+oClasses.sPageButtonStaticDisabled );
          
          $([anStatic[0], anStatic[1]]).addClass( 
            (iCurrentPage==1) ?
              oClasses.sPageButtonStaticDisabled :
              oClasses.sPageButton
          );
          $([anStatic[2], anStatic[3]]).addClass(
            (iPages===0 || iCurrentPage===iPages || oSettings._iDisplayLength===-1) ?
              oClasses.sPageButtonStaticDisabled :
              oClasses.sPageButton
          );

          //Hide pagination if there is only one page
          if (iPages === 1 || oSettings._iDisplayLength === -1) {
            $(an[i]).hide();
          }
        }
      }
    };