var initialCursorPositionNS = {};
(function($, ns) {

	ns.init = function() {
		var wantingFocus = $('.start-with-focus');
		if (wantingFocus) {
			$(wantingFocus[0]).focus();
		}
	};

	ns.init();

})(jQuery, initialCursorPositionNS);