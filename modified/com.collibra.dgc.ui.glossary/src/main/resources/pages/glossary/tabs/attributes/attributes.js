//Initialize widgetboard on term page - attributes tab.
core.module.use("modules/layout/widgetboard", function(WidgetBoard) {
  core.mediator.subscribe('tabbar::tab::attributes::loaded', function() {
    WidgetBoard.create("#widgetboard");
  });
  
  core.mediator.subscribe('tabbar::tab::attributes::settings::entered', function() {
    WidgetBoard.setWidgetsMode("#widgetboard", 2);
  });
  
  core.mediator.subscribe('tabbar::tab::attributes::settings::exited', function() {
    WidgetBoard.setWidgetsMode("#widgetboard", 0);
  });
});