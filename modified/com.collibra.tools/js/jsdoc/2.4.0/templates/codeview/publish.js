/** Called automatically by JsDoc Toolkit. */
function publish(symbolSet) {
	publish.conf = {  // trailing slash expected for dirs
		ext:         ".html",
		outDir:      JSDOC.opt.d || SYS.pwd+"../out/jsdoc/",
		templatesDir: JSDOC.opt.t || SYS.pwd+"../templates/jsdoc/",
		staticDir:   "static/",
		symbolsDir:  "symbols/",
		srcDir:      "symbols/src/",
		cssDir:      "css/",
		fontsDir:    "css/fonts/",
		imagesDir:      "images/",
		jsDir:       "javascript/",
		templateName: "Collibra",
		templateVersion: "1.0",
		templateLink: "#"
	};
	
	// is source output is suppressed, just display the links to the source file
	if (JSDOC.opt.s && defined(Link) && Link.prototype._makeSrcLink) {
		Link.prototype._makeSrcLink = function(srcFilePath) {
			return "&lt;"+srcFilePath+"&gt;";
		}
	}
	
	IO.include("frame/Dumper.js");
	//IO.include("../templates/codeview/javascript/jquery.js");
	
	// create the folders and subfolders to hold the output
	IO.mkPath((publish.conf.outDir+publish.conf.cssDir));
	IO.mkPath((publish.conf.outDir+publish.conf.fontsDir));
	IO.mkPath((publish.conf.outDir+publish.conf.imagesDir));
	IO.mkPath((publish.conf.outDir+publish.conf.jsDir));
	IO.mkPath((publish.conf.outDir+"symbols/src").split("/"));
	
	// used to allow Link to check the details of things being linked to
	Link.symbolSet = symbolSet;

	// create the required templates
	try {
		var classTemplate = new JSDOC.JsPlate(publish.conf.templatesDir+"class.tmpl"),
		    moduleTemplate = new JSDOC.JsPlate(publish.conf.templatesDir+"module.tmpl"),
		    controlTemplate = new JSDOC.JsPlate(publish.conf.templatesDir+"control.tmpl"),
		    coreTemplate = new JSDOC.JsPlate(publish.conf.templatesDir+"core.tmpl");
	}
	catch(e) {
		print("Couldn't create the required templates: "+e);
		quit();
	}
	
	// some utility filters
	function hasNoParent($) {return ($.memberOf == "")}
	function isaFile($) {return ($.is("FILE"))}
	function isaClass($) {return (($.is("CONSTRUCTOR") || $.isNamespace) && ($.alias != "_global_" || !JSDOC.opt.D.noGlobal))}
	
	// get an array version of the symbolset, useful for filtering
	var symbols = symbolSet.toArray();
	
	// create the hilited source code files
	var files = JSDOC.opt.srcFiles;
 	for (var i = 0, l = files.length; i < l; i++) {
 		var file = files[i];
 		var srcDir = publish.conf.outDir + publish.conf.srcDir;
		makeSrcFile(file, srcDir);
 	}

 	// get a list of all the classes in the symbolset
 	publish.classes = symbols.filter(isaClass).sort(makeSortby("alias"));

 	function ise(obj) {
 	  for(var prop in obj) {
 	    if (Object.prototype.hasOwnProperty.call(obj, prop)) {
 	      return false;
 	    }
 	  }
 	  return true;
 	}
 	
 	publish.draw = function(children, recursive) {
    var level = (recursive) ? 'subLevel' : 'mainLevel' ,
        out = '<ul class="' + level + '">',
        keys = [];
    
    for (var o in children) {
      if (children.hasOwnProperty(o)) {
          keys.push(o);
      }
    }
    keys.sort(function(a, b) {
      a = a.toLowerCase();
      b = b.toLowerCase();
      //print(a + " < " + b + " = " a<b);
      if (a < b  || a === "core") return -1;
      if (a > b) return 1;
      return 0;
    });
    
    for (var i = 0; i < keys.length; i++) {
      var name = (keys[i] !== "core")? wordwrapNamespace(new Link().toClass(keys[i]) + "") : wordwrapNamespace(keys[i]) ;
      if (level === 'mainLevel') {
        out += '<li class="' + level + '"><span class="menuHeader">' + name + '</span>';
      } else {
        out += '<li class="' + level + '">' + name;
      }
        if (!ise(children[keys[i]]))
          out += publish.draw(children[keys[i]], true);
      out += '</li>';
    }
    return out += '</ul>';
  }
 	
 	
 	publish.sortByKey = function(obj) {
 	  var keys = [],
 	      sobj = {};
 	  for (var o in obj) {
 	    if (obj.hasOwnProperty(o)) {
 	        obj[o] =  publish.sortByKey(obj[o]);
 	        keys.push(o);
 	    }
 	  }
 	  keys.sort(function(a, b) {
 	    a = a.toLowerCase();
 	    b = b.toLowerCase();
 	    if (a < b) return -1;
 	    if (a > b) return 1;
 	    return 0;
 	  });
 	  for (var i = 0; i < keys.length; i++) {
 	    sobj[keys[i]] = obj[keys[i]];
 	  }
 	  return sobj;
 	}
 	
  // get an array of groups
  publish.groups = {};
  
  var perc = 0;
 	for (p in publish.classes) {
 	 for (t in publish.classes[p].comment.tags) {
    var tag = publish.classes[p].comment.tags[t];
    var alias = publish.classes[p].alias;
    if (tag.title === "namespace") {
      tag = tag.toString() + "/" + alias;
      var tokens = tag.toString().split("/");
      var descent = publish.groups ;
        for (k=0;k<tokens.length;k++) {
          if (descent.hasOwnProperty(tokens[k])) {
            descent = descent[tokens[k]];
          } else {
            descent[tokens[k]] = {};
            k--;
        }
      } 
    }
 	 }
 	}
 	

  
  
 	//print(publish.draw(publish.groups));
 	
	// create a filemap in which outfiles must be to be named uniquely, ignoring case
	if (JSDOC.opt.u) {
		var filemapCounts = {};
		Link.filemap = {};
		for (var i = 0, l = publish.classes.length; i < l; i++) {
			var lcAlias = publish.classes[i].alias.toLowerCase();
			
			if (!filemapCounts[lcAlias]) filemapCounts[lcAlias] = 1;
			else filemapCounts[lcAlias]++;
			
			Link.filemap[publish.classes[i].alias] = 
				(filemapCounts[lcAlias] > 1)?
				lcAlias+"_"+filemapCounts[lcAlias] : lcAlias;
		}
	}
	
	print("Done.");
	print("Creating documents..");
	// create each of the class pages
	for (var i = 0, l = publish.classes.length; i < l; i++) {
		var symbol = publish.classes[i];
		
		symbol.events = symbol.getEvents();   // 1 order matters
		symbol.methods = symbol.getMethods(); // 2
		
		//if (symbol.name === "dom") {
		  
		  //print(symbol.serialize());
		//}
		
		//classTemplate
		var fname = (symbol.comment.getTag("namespace") + "/" + symbol.name).replace(/controls\/|modules\//,"").replace(/\//g,".");
		var type = symbol.comment.getTag("namespace").toString().split("/")[0];
		symbol.type = type;
		symbol.fname = fname;
		if ((/^true$/i).test(JSDOC.opt.D.development)) {
		  print("Namespace: " + type.toString().toUpperCase() + " - " + symbol.name);
		}
		
		var output = "";
		switch (type) {
		  case "core": 
		    output = coreTemplate.process(symbol); 
		    break;
		  case "controls": 
		    output = controlTemplate.process(symbol); 
		    break;
		  case "modules": 
		    var fname_tokens = fname.toString().split(".");
        fname = fname_tokens[fname_tokens.length - 1];
        symbol.fname = fname;
        output = moduleTemplate.process(symbol); 
		    break;
		}
		//print("sda"+fname);
		IO.saveFile(publish.conf.outDir+publish.conf.symbolsDir, ((JSDOC.opt.u)? Link.filemap[symbol.alias] : symbol.alias) + publish.conf.ext, output);
	}
	
	print("Complete.");
	
	// create the class index page
	try {
		var classesindexTemplate = new JSDOC.JsPlate(publish.conf.templatesDir+"index.tmpl");
	}
	catch(e) { print(e.message); quit(); }
	
	var classesIndex = classesindexTemplate.process(publish.classes);
	IO.saveFile(publish.conf.outDir, (JSDOC.opt.D.index=="files"?"allclasses":"index")+publish.conf.ext, classesIndex);
	classesindexTemplate = classesIndex = classes = null;
	
	// create the file index page
//	try {
//		var fileindexTemplate = new JSDOC.JsPlate(publish.conf.templatesDir+"allfiles.tmpl");
//	}
//	catch(e) { print(e.message); quit(); }
	
	var documentedFiles = symbols.filter(isaFile); // files that have file-level docs
	var allFiles = []; // not all files have file-level docs, but we need to list every one
	
	for (var i = 0; i < files.length; i++) {
		allFiles.push(new JSDOC.Symbol(files[i], [], "FILE", new JSDOC.DocComment("/** */")));
	}
	
	for (var i = 0; i < documentedFiles.length; i++) {
		var offset = files.indexOf(documentedFiles[i].alias);
		allFiles[offset] = documentedFiles[i];
	}
		
	allFiles = allFiles.sort(makeSortby("name"));

	// output the file index page
//	var filesIndex = fileindexTemplate.process(allFiles);
//	IO.saveFile(publish.conf.outDir, (JSDOC.opt.D.index=="files"?"index":"files")+publish.conf.ext, filesIndex);
//	fileindexTemplate = filesIndex = files = null;
	
	// copy static files
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.cssDir+"screen.css", publish.conf.outDir+"/"+publish.conf.cssDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.cssDir+"shCore.css", publish.conf.outDir+"/"+publish.conf.cssDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.cssDir+"shThemeDefault.css", publish.conf.outDir+"/"+publish.conf.cssDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"gillsanslight-webfont.ttf", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"monaco-webfont.ttf", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"gillsanslight-webfont.eot", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"monaco-webfont.eot", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"gillsanslight-webfont.svg", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"monaco-webfont.svg", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"gillsanslight-webfont.woff", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.fontsDir+"monaco-webfont.woff", publish.conf.outDir+"/"+publish.conf.fontsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.imagesDir+"logo.png", publish.conf.outDir+"/"+publish.conf.imagesDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.imagesDir+"favicon.ico", publish.conf.outDir+"/"+publish.conf.imagesDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.imagesDir+"controls.png", publish.conf.outDir+"/"+publish.conf.imagesDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.imagesDir+"icons.gif", publish.conf.outDir+"/"+publish.conf.imagesDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.jsDir+"jquery.beautyOfCode.js", publish.conf.outDir+"/"+publish.conf.jsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.jsDir+"shCore.js", publish.conf.outDir+"/"+publish.conf.jsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.jsDir+"shBrushJScript.js", publish.conf.outDir+"/"+publish.conf.jsDir);
  IO.copyFile(publish.conf.templatesDir+"/"+publish.conf.jsDir+"jquery.js", publish.conf.outDir+"/"+publish.conf.jsDir);
}

/** Include a sub-template in the current template, specifying a data object */
function subtemplate(template, data) {
	try {
		return new JSDOC.JsPlate(publish.conf.templatesDir+template).process(data);
	}
	catch(e) { print(e.message); quit(); }
}

/** Just the first sentence (up to a full stop). Should not break on dotted variable names. */
function summarize(desc) {
	if (typeof desc != "undefined")
		return desc.match(/([\w\W]+?\.)[^a-z0-9_$]/i)? RegExp.$1 : desc;
}

/** Make a symbol sorter by some attribute. */
function makeSortby(attribute) {
	return function(a, b) {
		if (a[attribute] != undefined && b[attribute] != undefined) {
			a = a[attribute].toLowerCase();
			b = b[attribute].toLowerCase();
			if (a < b) return -1;
			if (a > b) return 1;
			return 0;
		}
	}
}

function wordwrapNamespace(classLink) {
	var classText = classLink.match(/[^<>]+(?=[<])/) + "";
	var classTextNew = classText.replace(/\./g,  "<span class='break'> </span>.<span class='break'> </span>") + "";
	classLink = classLink.replace(/[^<>]+(?=[<])/,  classTextNew);
	return classLink;
}

/** Pull in the contents of an external file at the given path. */
function include(path) {
	var path = publish.conf.templatesDir+path;
	return IO.readFile(path);
}

/** Turn a raw source file into a code-hilited page in the docs. */
function makeSrcFile(path, srcDir, name) {
	if (JSDOC.opt.s) return;
	
	if (!name) {
		name = path.replace(/\.\.?[\\\/]/g, "").replace(/[\\\/]/g, "_");
		name = name.replace(/\:/g, "_");
	}
	
	var src = {path: path, name:name, charset: IO.encoding, hilited: ""};
	
	if (defined(JSDOC.PluginManager)) {
		JSDOC.PluginManager.run("onPublishSrc", src);
	}

	if (src.hilited) {
		IO.saveFile(srcDir, name+publish.conf.ext, src.hilited);
	}
}

/** Build output for displaying function parameters. */
function makeSignature(params) {
	if (!params) return "()";
	var signature = "("
	+
	params.filter(
		function($) {
			return $.name.indexOf(".") == -1; // don't show config params in signature
		}
	).map(
		function($) {
			return $.name;
		}
	).join(", ")
	+
	")";
	return signature;
}

/** Find symbol {@link ...} strings in text and turn into html links */
function resolveLinks(str, from) {
   str = str.replace(/\{@link ([^}]+)\}/gi,
     function(match, symbolName) {
	symbolName = symbolName.trim();
	var index = symbolName.indexOf(' ');
	if (index > 0) {
	   var label = symbolName.substring(index + 1);
	   symbolName = symbolName.substring(0, index);
	   return new Link().toSymbol(symbolName).withText(label);
	} else {
	   return new Link().toSymbol(symbolName);
	}
     }
   );
   return str;
}


var MAX_DUMP_DEPTH = 10;

function dump(obj, name, indent, depth) {
       if (depth > MAX_DUMP_DEPTH) {
              return indent + name + ": <Maximum Depth Reached>\n";
       }
       if (typeof obj == "object") {
              var child = null;
              var output = indent + name + "\n";
              indent += "\t";
              for (var item in obj)
              {
                    try {
                           child = obj[item];
                    } catch (e) {
                           child = "<Unable to Evaluate>";
                    }
                    if (typeof child == "object") {
                           output += dump(child, item, indent, depth + 1);
                    } else {
                           output += indent + item + ": " + child + "\n";
                    }
              }
              return output;
       } else {
              return obj;
       }
}


















