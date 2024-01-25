$('#nb_menu').addClass('navbar-brand');

$('#newMenuNeme').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});

$('#newLink').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});

$('#newMenuOrder').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});
$('#newMenuActive').change(function(){
	enableSaveButton();
});

function enableSaveButton(){

	let $button = $('#saveMenu');
	let $newMenu = $('#newMenuNeme');
	let newMenuVal = $newMenu.val().trim();

	if(!newMenuVal){
		$button.addClass('disabled');
		return;
	}

	let select = $('#menuSelect').get(0);
	let selectOption = select.options[select.selectedIndex];

	let newLink = $('#newLink').val();
	let newMenuOrder = $('#newMenuOrder').val();
	let newMenuActive = $('#newMenuActive').prop('checked');

	if(selectOption.text == newMenuVal && newLink == selectOption.dataset.link && newMenuOrder == selectOption.dataset.order && String(newMenuActive) == selectOption.dataset.active){
		$button.addClass('disabled');
		return;
	}

	$button.removeClass('disabled');
}
$('#saveMenu').click(function(){
	
	let data = {};
	let $menuSelect = $('#menuSelect');
	let menuId = parseInt($menuSelect.val());
	if(menuId>=0)
		data.menuId = menuId;

	data.menuNeme = $('#newMenuNeme').val().trim();
	data.link = $('#newLink').val().trim();
	data.order = $('#newMenuOrder').val();
	data.active = $('#newMenuActive').prop('checked');

	$.post('/rest/menu_name', data)
	.done(data=>window.location.reload())
	.fail(function(error) {
		console.error(error);
  	});	
});
$('#menuSelect').change(function(){
	let selectOption = this.options[this.selectedIndex];
	if(selectOption.value>=0){
		$('#newMenuNeme').val(selectOption.innerText);
		$('#newLink').val(selectOption.dataset.link).prop('disabled', false);
		$('#newMenuOrder').val(selectOption.dataset.order).prop('disabled', false);
		let label = $('#newMenuActive').prop('checked', selectOption.dataset.active=='true').prop('nextElementSibling');
		$(label).removeClass('disabled');
		$('#saveMenu').text('Save Menu');
		selectFilters(JSON.parse(selectOption.dataset.filters));
	}else{
		$('#newMenuNeme').val('');
		$('#newLink').val('/products').prop('disabled', true);
		$('#newMenuOrder').val(0).prop('disabled', true);
		let label = $('#newMenuActive').prop('checked', true).prop('nextElementSibling');
		$(label).addClass('disabled');
		$('#saveMenu').text('Add Menu');
	}

	enableSaveButton();
});
$('#collapseFilters input').change(function(e){

	let $menuSelect = $('#menuSelect');
	let menuId = $menuSelect.val();

	if(menuId=='-1'){
		e.stopPropagation();
		$(this).prop('checked', false);
		alert('Select the menu first.');
		return;
	}

	let checked = $.map($('#collapseFilters input').filter((i,o)=>o.checked), (o, i)=>parseInt(o.value)).sort();
	let select = $menuSelect.get(0);
	let selectOption = select.options[select.selectedIndex];
	let filters = JSON.parse(selectOption.dataset.filters).sort();

	if(checked.length==filters.length && JSON.stringify(checked) === JSON.stringify(filters)){
		$('#saveMenuFilters').addClass('disabled');
	}else{
		$('#saveMenuFilters').removeClass('disabled');
	}
});
$('#saveMenuFilters').click(function(){

	let menuId = $('#menuSelect').val();
	let checked = $.map($('#collapseFilters input').filter((i,o)=>o.checked), (o, i)=>parseInt(o.value));

	$.post('/rest/menu/filter', {menuId: menuId, checked: checked})
	.done(data=>window.location.reload())
	.fail(function(error) {
		console.error(error);
  	});	
});
function selectFilters(ids){
	let $checkBox = $('#collapseFilters input').prop('checked', false);
	ids.forEach((id)=>$('#id' + id).prop('checked', true));
}
