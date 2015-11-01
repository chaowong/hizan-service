$.root_ = $('body');
//document.title = '学院鹿';

loadURL('leftpanel.html',jQuery('.leftpanel'));
loadURL('headerbar.html',jQuery('.headerbar'));

//处理框架样式  全屏or固定宽度-----------------------

if($.root_.hasClass('fixed')) {
	jQuery('#fullscreenview').find('i').removeClass('glyphicon-resize-horizontal').addClass('glyphicon-fullscreen');	
}
else {
	jQuery('#fullscreenview').find('i').removeClass('glyphicon-fullscreen').addClass('glyphicon-resize-horizontal');
}
jQuery('#fullscreenview').click(function(){
	if($.root_.hasClass('fixed')) {
		$.root_.removeClass('fixed');
		$(this).find('i').removeClass('glyphicon-fullscreen').addClass('glyphicon-resize-horizontal');
	}
	else {
		$('body').addClass('fixed');
		$(this).find('i').removeClass('glyphicon-resize-horizontal').addClass('glyphicon-fullscreen');	
	}
});
//-----------------------------------------------
jQuery(window).load(function() {

   "use strict";
   // Page Preloader
   jQuery('#preloader').delay(50).fadeOut(function(){
      jQuery('body').delay(50).css({'overflow':'visible'});
   });
});

jQuery(document).ready(function() {

   "use strict";

   // Toggle Left Menu
   jQuery('.leftpanel .nav-parent > a').live('click', function() {

      var parent = jQuery(this).parent();
      var sub = parent.find('> ul');

      // Dropdown works only when leftpanel is not collapsed
      if(!jQuery('body').hasClass('leftpanel-collapsed')) {
         if(sub.is(':visible')) {
        	 parent.removeClass('nav-active');
             sub.slideUp(200, function(){
               
               jQuery('.mainpanel').css({height: ''});
               adjustmainpanelheight();
            });
         } else {
            closeVisibleSubMenu();
            parent.addClass('nav-active');
            sub.slideDown(200, function(){
               adjustmainpanelheight();
            });
         }
      }
      return false;
   });

   function closeVisibleSubMenu() {
      jQuery('.leftpanel .nav-parent').each(function() {
         var t = jQuery(this);
         if(t.hasClass('nav-active')) {
            t.find('> ul').slideUp(200, function(){
               t.removeClass('nav-active');
            });
         }
      });
   }

   function adjustmainpanelheight() {
      // Adjust mainpanel height
      var docHeight = jQuery(document).height();
      if(docHeight > jQuery('.mainpanel').height())
         jQuery('.mainpanel').height(docHeight);
   }
   adjustmainpanelheight();
   // Tooltip
   jQuery('.tooltips').tooltip({ container: 'body'});

   // Popover
   jQuery('.popovers').popover();

   // Close Button in Panels
   jQuery('.panel .panel-close').click(function(){
      jQuery(this).closest('.panel').fadeOut(200);
      return false;
   });

   // Form Toggles
   jQuery('.toggle').toggles({on: true});

   jQuery('.toggle-chat1').toggles({on: false});

   var scColor1 = '#428BCA';
   if (jQuery.cookie('change-skin') && jQuery.cookie('change-skin') == 'bluenav') {
      scColor1 = '#fff';
   }


   // Sparkline
   jQuery('#sidebar-chart').sparkline([4,3,3,1,4,3,2,2,3,10,9,6], {
	 type: 'bar',
	 height:'30px',
         barColor: scColor1
   });

   jQuery('#sidebar-chart2').sparkline([1,3,4,5,4,10,8,5,7,6,9,3], {
	  type: 'bar',
	  height:'30px',
         barColor: '#D9534F'
   });

   jQuery('#sidebar-chart3').sparkline([5,9,3,8,4,10,8,5,7,6,9,3], {
	  type: 'bar',
	  height:'30px',
         barColor: '#1CAF9A'
   });

   jQuery('#sidebar-chart4').sparkline([4,3,3,1,4,3,2,2,3,10,9,6], {
	  type: 'bar',
	  height:'30px',
         barColor: scColor1
   });

   jQuery('#sidebar-chart5').sparkline([1,3,4,5,4,10,8,5,7,6,9,3], {
	  type: 'bar',
	  height:'30px',
      barColor: '#F0AD4E'
   });


   // Minimize Button in Panels
   jQuery('.minimize').click(function(){
      var t = jQuery(this);
      var p = t.closest('.panel');
      if(!jQuery(this).hasClass('maximize')) {
         p.find('.panel-body, .panel-footer').slideUp(200);
         t.addClass('maximize');
         t.html('&plus;');
      } else {
    	 t.removeClass('maximize');
         p.find('.panel-body, .panel-footer').slideDown(200);
         
         t.html('&minus;');
      }
      return false;
   });


   // Add class everytime a mouse pointer hover over it
   jQuery('.nav-bracket > li').hover(function(){
      jQuery(this).addClass('nav-hover');
   }, function(){
      jQuery(this).removeClass('nav-hover');
   });


   // Menu Toggle
   jQuery('.menutoggle').click(function(){

      var body = jQuery('body');
      var bodypos = body.css('position');

      if(bodypos != 'relative') {

         if(!body.hasClass('leftpanel-collapsed')) {
            body.addClass('leftpanel-collapsed');
            jQuery('.nav-bracket ul').attr('style','');

            jQuery(this).addClass('menu-collapsed');

         } else {
            body.removeClass('leftpanel-collapsed chat-view');
            jQuery('.nav-bracket li.active ul').css({display: 'block'});

            jQuery(this).removeClass('menu-collapsed');

         }
      } else {

         if(body.hasClass('leftpanel-show'))
            body.removeClass('leftpanel-show');
         else
            body.addClass('leftpanel-show');

         adjustmainpanelheight();
      }

   });

   // Chat View
   jQuery('#chatview').click(function(){

      var body = jQuery('body');
      var bodypos = body.css('position');

      if(bodypos != 'relative') {

         if(!body.hasClass('chat-view')) {
            body.addClass('leftpanel-collapsed chat-view');
            jQuery('.nav-bracket ul').attr('style','');

         } else {

            body.removeClass('chat-view');

            if(!jQuery('.menutoggle').hasClass('menu-collapsed')) {
               jQuery('body').removeClass('leftpanel-collapsed');
               jQuery('.nav-bracket li.active ul').css({display: 'block'});
            } else {

            }
         }

      } else {

         if(!body.hasClass('chat-relative-view')) {

            body.addClass('chat-relative-view');
            body.css({left: ''});

         } else {
            body.removeClass('chat-relative-view');
         }
      }

   });

   reposition_topnav();
   reposition_searchform();

   jQuery(window).resize(function(){

      if(jQuery('body').css('position') == 'relative') {

         jQuery('body').removeClass('leftpanel-collapsed chat-view');

      } else {

         jQuery('body').removeClass('chat-relative-view');
         jQuery('body').css({left: '', marginRight: ''});
      }


      reposition_searchform();
      reposition_topnav();

   });



   /* This function will reposition search form to the left panel when viewed
    * in screens smaller than 767px and will return to top when viewed higher
    * than 767px
    */
   function reposition_searchform() {
      if(jQuery('.searchform').css('position') == 'relative') {
         jQuery('.searchform').insertBefore('.leftpanelinner .userlogged');
      } else {
         jQuery('.searchform').insertBefore('.header-right');
      }
   }



   /* This function allows top navigation menu to move to left navigation menu
    * when viewed in screens lower than 1024px and will move it back when viewed
    * higher than 1024px
    */
   function reposition_topnav() {
      if(jQuery('.nav-horizontal').length > 0) {

         // top navigation move to left nav
         // .nav-horizontal will set position to relative when viewed in screen below 1024
         if(jQuery('.nav-horizontal').css('position') == 'relative') {

            if(jQuery('.leftpanel .nav-bracket').length == 2) {
               jQuery('.nav-horizontal').insertAfter('.nav-bracket:eq(1)');
            } else {
               // only add to bottom if .nav-horizontal is not yet in the left panel
               if(jQuery('.leftpanel .nav-horizontal').length == 0)
                  jQuery('.nav-horizontal').appendTo('.leftpanelinner');
            }

            jQuery('.nav-horizontal').css({display: 'block'})
                                  .addClass('nav-pills nav-stacked nav-bracket');

            jQuery('.nav-horizontal .children').removeClass('dropdown-menu');
            jQuery('.nav-horizontal > li').each(function() {

               jQuery(this).removeClass('open');
               jQuery(this).find('a').removeAttr('class');
               jQuery(this).find('a').removeAttr('data-toggle');

            });

            if(jQuery('.nav-horizontal li:last-child').has('form')) {
               jQuery('.nav-horizontal li:last-child form').addClass('searchform').appendTo('.topnav');
               jQuery('.nav-horizontal li:last-child').hide();
            }

         } else {
            // move nav only when .nav-horizontal is currently from leftpanel
            // that is viewed from screen size above 1024
            if(jQuery('.leftpanel .nav-horizontal').length > 0) {

               jQuery('.nav-horizontal').removeClass('nav-pills nav-stacked nav-bracket')
                                        .appendTo('.topnav');
               jQuery('.nav-horizontal .children').addClass('dropdown-menu').removeAttr('style');
               jQuery('.nav-horizontal li:last-child').show();
               jQuery('.searchform').removeClass('searchform').appendTo('.nav-horizontal li:last-child .dropdown-menu');
               jQuery('.nav-horizontal > li > a').each(function() {

                  jQuery(this).parent().removeClass('nav-active');

                  if(jQuery(this).parent().find('.dropdown-menu').length > 0) {
                     jQuery(this).attr('class','dropdown-toggle');
                     jQuery(this).attr('data-toggle','dropdown');
                  }

               });
            }

         }

      }
   }


   // Sticky Header
   if(jQuery.cookie('sticky-header'))
      jQuery('body').addClass('stickyheader');

   // Sticky Left Panel
   if(jQuery.cookie('sticky-leftpanel')) {
      jQuery('body').addClass('stickyheader');
      jQuery('.leftpanel').addClass('sticky-leftpanel');
   }

   // Left Panel Collapsed
   if(jQuery.cookie('leftpanel-collapsed')) {
      jQuery('body').addClass('leftpanel-collapsed');
      jQuery('.menutoggle').addClass('menu-collapsed');
   }

   // Changing Skin
   var c = jQuery.cookie('change-skin');
   var cssSkin = 'css/style.'+c+'.css';
   if (jQuery('body').css('direction') == 'rtl') {
      cssSkin = '../css/style.'+c+'.css';
      jQuery('html').addClass('rtl');
   }
   if(c) {
      jQuery('head').append('<link id="skinswitch" rel="stylesheet" href="'+cssSkin+'" />');
   }

   // Changing Font
   var fnt = jQuery.cookie('change-font');
   if(fnt) {
      jQuery('head').append('<link id="fontswitch" rel="stylesheet" href="css/font.'+fnt+'.css" />');
   }

   // Check if leftpanel is collapsed
   if(jQuery('body').hasClass('leftpanel-collapsed'))
      jQuery('.nav-bracket .children').css({display: ''});


   // Handles form inside of dropdown
   jQuery('.dropdown-menu').find('form').click(function (e) {
      e.stopPropagation();
   });


   // This is not actually changing color of btn-primary
   // This is like you are changing it to use btn-orange instead of btn-primary
   // This is for demo purposes only
   var c = jQuery.cookie('change-skin');
   if (c && c == 'greyjoy') {
      $('.btn-primary').removeClass('btn-primary').addClass('btn-orange');
      $('.rdio-primary').addClass('rdio-default').removeClass('rdio-primary');
      $('.text-primary').removeClass('text-primary').addClass('text-orange');
   }

});

/*
//// Analytics Code
//(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
//(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
//m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
//})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

//ga('create', 'UA-40361841-2', 'auto');
//ga('send', 'pageview');

//fire this on page load if .nav exists
if ($('.nav').length) {
    checkURL();
};

$('.nav a[href!="#"]').click(function (e) {
        e.preventDefault();
        $this = $(this);

        // if parent is not active then get hash, or else page is assumed to be loaded
        if (!$this.parent().hasClass("active") && !$this.attr('target')) {

            // update window with hash

            if ($.root_.hasClass('mobile-view-activated')) {
                $.root_.removeClass('hidden-menu');
                window.setTimeout(function () {
                    window.location.hash = $this.attr('href');
                }, 250);
                // it may not need this delay...
            } else {
                window.location.hash = $this.attr('href');
            }
        }
    });

// fire links with targets on different window
$('.nav a[target="_blank"]').click(function (e) {
        e.preventDefault();
        $this = $(this);
        window.open($this.attr('href'));
    });

// fire links with targets on same window
$('.nav a[target="_top"]').click(function (e) {
        e.preventDefault();
        $this = $(this);
        window.location = ($this.attr('href'));
    });

// all links with hash tags are ignored
$('.nav a[href="#"]').click(function (e) {
        e.preventDefault();
    });

// DO on hash change
$(window).on('hashchange', function () {
        checkURL();
    });

// CHECK TO SEE IF URL EXISTS
function checkURL() {

    //get the url by removing the hash
    url = location.hash.replace(/^#/, '');

    container = $('.contentpanel');
    // Do this if url exists (for page refresh, etc...)
    if (url) {
        // remove all active class
        $('.nav li.active').removeClass("active");
        // match the url and add the active class
        $('.nav li:has(a[href="' + url + '"])').addClass("active");
        title = ($('.nav a[href="' + url + '"]').attr('title'));

        // change page title from global var
        document.title = (title || document.title);
        //console.log("page title: " + document.title);

        // parse url to jquery
        loadURL(url, container);
    } else {

        // grab the first URL from .nav
        $this = $('.nav > ul > li:first-child > a[href!="#"]');

        //update hash
        window.location.hash = $this.attr('href');

    }

}


//==================

 * LOAD SCRIPTS
 * Usage:
 * Define function = myPrettyCode ()...
 * loadScript("js/my_lovely_script.js", myPrettyCode);
 
*/
var jsArray = "";
function loadScript(scriptName, callback) {

    if (jsArray.indexOf("[" + scriptName + "]") == -1) {

        //List of files added in the form "[filename1],[filename2],etc"
        jsArray += "[" + scriptName + "]";

        // adding the script tag to the head as suggested before
        var body = document.getElementsByTagName('body')[0];
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = scriptName;

        // then bind the event to the callback function
        // there are several events for cross browser compatibility
        //script.onreadystatechange = callback;
        script.onload = callback;

        // fire the loading
        body.appendChild(script);

    } else if (callback) { // changed else to else if(callback)
        //console.log("JS file already added!");
        //execute function
        callback();
    }

}



//LOAD AJAX PAGES

function loadURL(url, container) {
    //console.log(container)

    $.ajax({
        type: "GET",
        url: url,
        dataType: 'html',
        cache: true, // (warning: this will cause a timestamp and will call the request twice)
        beforeSend: function () {
            container.html('<h1><i class="fa fa-cog fa-spin"></i> 加载...</h1>');
        },
        success: function (data) {
            container.css({
                opacity: '0.0'
            }).html(data).delay(100).animate({
                    opacity: '1.0'
                }, 100);
            drawBreadCrumb();
        },
        error: function (xhr, ajaxOptions, thrownError) {
            container.html(
                '<h4 style="margin-top:10px; display:block; text-align:left"><i class="fa fa-warning txt-color-orangeDark"></i> Error 404! Page not found.</h4>'
            );
            drawBreadCrumb();
        },
        async: false
    });

    //console.log("ajax request sent");
}

//UPDATE BREADCRUMB
function drawBreadCrumb() {

    $("#ribbon ol.breadcrumb").empty();
    $("#ribbon ol.breadcrumb").append($("<li>Home</li>"));
    $('nav li.active > a').each(function () {
            $("#ribbon ol.breadcrumb").append($("<li></li>").html(
            		$.trim($(this).clone().children(".badge").remove().end().text()))
            		);
        });

    //console.log("breadcrumb created");
}

$('.statistics').click(function(){
	var $this=$(this);
	var page=document.title;
	var tag=$this[0].tagName;
	var title=$this.attr('statTitle');
	var brow=$.browser;
	var bInfo="";
	
    if(brow.msie) {bInfo="Microsoft Internet Explorer "+brow.version;}
	if(brow.mozilla) {bInfo="Mozilla Firefox "+brow.version;}
	if(brow.safari) {bInfo="Apple Safari "+brow.version;}
	if(brow.opera) {bInfo="Opera "+brow.version;}
	var pos="top:"+$this.offset().top+" , left:"+$this.offset().left;
	var screen= window.screen.width+" X "+window.screen.height;
	var url=document.URL;
	$.ajax({
        type: "POST",
        url: "statistics/add.action",
        data:{client:bInfo,url:url,page:page,tag:tag,title:title,screen:screen,position:pos},
        dataType: 'json',
        cache: true, // (warning: this will cause a timestamp and will call the request twice)
        beforeSend: function () {
            //container.html('<h1><i class="fa fa-cog fa-spin"></i> 加载...</h1>');
        	console.log("sending data...");
        },
        success: function (data) {
        	console.log("success...");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            console.log("error...");
        },
        async: false
    });
});
