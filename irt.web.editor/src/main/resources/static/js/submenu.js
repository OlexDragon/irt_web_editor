$('#nb_submenu').addClass('navbar-brand');

$('#menuSelect').change(function(){

	let $checkBox = $('#collapseFilters input').prop('checked', false).prop('disabled', false);
	$('#collapseFilters label').removeClass('disabled');

	let $menuContent = $('#menuContent').empty();
	let ownerId = this.value;
	let selectOption = this.options[this.selectedIndex];
	let filterIds = JSON.parse(selectOption.dataset.filters).sort();
	filterIds.forEach((id)=>{
		let label = $('#id' + id).prop('checked', true).prop('disabled', true).prop('nextElementSibling');
		$(label).addClass('disabled');
	});

	$('#submenuSelect').load('/submenu_options?ownerId=' + ownerId, function(){
		if(ownerId>=0){
			$('#newMenuNeme').prop('disabled', false);
			$.each($(this).find('option'), (i,o) => {

				if(o.value=='-1') return;

				$menuContent
				.append(
					$('<div>', {class: 'row'})
					.append($('<div>', {class: 'col-auto', text: 'ID: ' + o.value}))
					.append($('<div>', {class: 'col', text: o.innerText}))
					.append($('<div>', {class: 'col', text: 'link: ' + o.dataset.link}))
					.append($('<div>', {class: 'col-auto', text: 'order: ' + o.dataset.order}))
					.append($('<div>', {class: 'col-auto', text: 'active: ' + o.dataset.active})));
			});
		}else{
			$('#newMenuNeme').prop('disabled', true);
			$menuContent.empty();
		}
	})
});

$('#newMenuNeme').on('input', function(){
	enableMenuButton();
})
.change(function(e){
	e.stopPropagation();
});

$('#newLink').on('input', function(){
	enableMenuButton();
})
.change(function(e){
	e.stopPropagation();
});

$('#newMenuOrder').on('input', function(){
	enableMenuButton();
})
.change(function(e){
	e.stopPropagation();
});
$('#newMenuActive').change(function(){
	enableMenuButton();
});

function enableMenuButton(){

	let $button = $('#btnSaveMenu');
	let $newMenu = $('#newMenuNeme');
	let newMenuVal = $newMenu.val().trim();

	if(!newMenuVal){
		$button.addClass('disabled');
		return;
	}

	let select = $('#submenuSelect').get(0);
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
$('#btnSaveMenu').click(function(){
	
	let data = {};
	let $menuSelect = $('#submenuSelect');
	let menuId = parseInt($menuSelect.val());
	if(menuId>=0)
		data.menuId = menuId;

	data.ownerId = $('#menuSelect').val();
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
$('#submenuSelect').change(function(){
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

	enableMenuButton();
});
$('#collapseFilters input').change(function(e){

	let $menuSelect = $('#submenuSelect');
	let menuId = $menuSelect.val();

	if(menuId=='-1'){
		e.stopPropagation();
		$(this).prop('checked', false);
		alert('First select the menu to edit.');
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

	let menuId = $('#submenuSelect').val();
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
