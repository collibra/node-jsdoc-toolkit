// We have to set the global vars undefined, because they cannot be
// removed with the delete operator. This can only be done on objects and their
// properties.
Modernizr = undefined;
window.Modernizr = undefined;