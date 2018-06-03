if(jQuery) (function($){
	
	$.extend($.fn, {
		fileTree: function(o) {
			// Defaults
			if( !o ) var o = {};
			if( o.script == undefined ) o.script = 'jqueryFileTree-default';
			if( o.folderEvent == undefined ) o.folderEvent = 'click';
			if( o.expandSpeed == undefined ) o.expandSpeed= 500;
			if( o.collapseSpeed == undefined ) o.collapseSpeed= 500;
			if( o.expandEasing == undefined ) o.expandEasing = null;
			if( o.collapseEasing == undefined ) o.collapseEasing = null;
			if( o.multiFolder == undefined ) o.multiFolder = true;
			if( o.loadMessage == undefined ) o.loadMessage = 'Loading...';
			if( o.showOnlyDirs == undefined ) o.showOnlyDirs = false;
			if( o.onClickFolder == undefined ) o.onClickFolder = function(){};
			if( o.onClickFile == undefined ) o.onClickFile = function(){};
			if( o.onClickItem == undefined ) o.onClickItem = function(){};
			
			$(this).each( function() {
				
				function showTree(c, t) {
					$(c).addClass('wait');
					$(".jqueryFileTree.start").remove();
					var typeFiles = "default";
					if(c.context.baseURI.indexOf("history") !== -1) {
						typeFiles="history1";
					}
					$.get("jqueryFileTree-"+typeFiles, { dir: t, displayOnlyDirs : globalVariable.displayOnlyDirs}, function(data) {
						$(c).find('.start').html('');
						$(c).removeClass('wait').append(data);
						if( o.root == t ) $(c).find('UL:hidden').show(); else $(c).find('UL:hidden').slideDown({ duration: o.expandSpeed, easing: o.expandEasing });
						bindTree(c);
					});
				}
				
				function displayFile(rel){
					$.get("get-content", { dir: rel }, function(data) {
						var link = rel.split("WebCMS");
						$("#resourcePath").html("Path: /"+location.pathname.split('/')[1] + link[1]);
						if (data.indexOf("alt='ShowImage'") >= 0 || data.indexOf("type='video") >= 0) {
							$("#content img").remove();
							$("#content video").remove();
							$("#fileContent").hide();
							$("#content").prepend(data);
							$("#saveButton").addClass("hidden")
							$("#cancelButton").addClass("hidden")
						} else {
							$("#fileContent")[0].value = removeLeadingNewLines(data);
							$("#fileContent").show();
							$("#content img").remove();
							$("#content video").remove();
							$("#saveButton").removeClass("hidden")
							$("#cancelButton").removeClass("hidden")
						}
						globalVariable.currentFilePath = rel;
						$("#previewButton").removeClass("hidden")
					});
				}
				
				function highlightCurentSelection(rel){
					if(o.jqueryFileTreeId != undefined){
						var links = $(o.jqueryFileTreeId).find('LI A');
						links.toArray().forEach(function(li){
							if($(li).attr('rel') == rel){
								$(li).addClass('highlighted');						
							}else{
								$(li).removeClass('highlighted');
							}
						});
					}
				}
				
				function bindTree(t) {
					$(t).find('LI A').bind(o.folderEvent, function() {
						if( $(this).parent().hasClass('directory') ) {
							if( $(this).parent().hasClass('collapsed') ) {
								// Expand
								if( !o.multiFolder ) {
									$(this).parent().parent().find('UL').slideUp({ duration: o.collapseSpeed, easing: o.collapseEasing });
									$(this).parent().parent().find('LI.directory').removeClass('expanded').addClass('collapsed');
								}
								$(this).parent().find('UL').remove(); // cleanup
								showTree( $(this).parent(), escape($(this).attr('rel').match( /.*\// )));
								$(this).parent().removeClass('collapsed').addClass('expanded');
							} else {
								// Collapse
								$(this).parent().find('UL').slideUp({ duration: o.collapseSpeed, easing: o.collapseEasing });
								$(this).parent().removeClass('expanded').addClass('collapsed');
							}
							o.onClickFolder($(this));
						} else {
							displayFile($(this).attr('rel'));
							o.onClickFile($(this));
						}
						o.onClickItem($(this));
						highlightCurentSelection($(this).attr('rel'));
						return false;
					});
					// Prevent A from triggering the # on non-click events
					if( o.folderEvent.toLowerCase != 'click' ) $(t).find('LI A').bind('click', function() { return false; });
				}
				// Loading message
				$(this).html('<ul class="jqueryFileTree start"><li class="wait">' + o.loadMessage + '<li></ul>');
				// Get the initial file list
				showTree( $(this), escape(o.root));
			});
		}
	});
	
})(jQuery);