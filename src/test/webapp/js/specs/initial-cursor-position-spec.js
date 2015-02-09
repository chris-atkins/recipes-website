describe("The initial-cursor-position.js... ", function() {
  
	describe("when there is one element on the screen with the 'start-with-focus' class...", function() {

		beforeEach(function() {
			affix('input[value="there"]+input.start-with-focus[value="hi"]');
			initialCursorPositionNS.init();
		});
		
		it("that element has focus when the page is loaded.", function() {
			expect($('.start-with-focus')).toBeFocused();
		});
	});
	
	describe("when there are multiple elements on the screen with the 'start-with-focus' class...", function() {
		
		beforeEach(function() {
			affix('input.start-with-focus#id1[value="hi"]+input.start-with-focus#id2[value="there"]');
			initialCursorPositionNS.init();
		});
		
		it("the first element has focus when the page is loaded.", function() {
			expect($('#id1')).toBeFocused();
		});
	});
});