$('#nb_filter_group').addClass('navbar-brand');

$('#newGroupNeme').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});

$('#newDescription').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});

$('#newGroupOrder').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});
$('#newIsRadio').change(function(){
	enableSaveButton();
});
$('#newGroupActive').change(function(){
	enableSaveButton();
});

function enableSaveButton(){

	let $button = $('#saveGroup');
	let $newGroup = $('#newGroupNeme');
	let newGroupVal = $newGroup.val().trim();

	if(!newGroupVal){
		$button.addClass('disabled');
		return;
	}

	let select = $('#groupSelect').get(0);
	let selectOption = select.options[select.selectedIndex];

	let newDescription = $('#newDescription').val();
	let newGroupOrder = $('#newGroupOrder').val();
	let newIsRadio = $('#newIsRadio').prop('checked');
	let newGroupActive = $('#newGroupActive').prop('checked');

	if(		selectOption.text == newGroupVal && 
			newDescription == selectOption.dataset.description && 
			newGroupOrder == selectOption.dataset.order && 
			String(newIsRadio) == selectOption.dataset.radio && 
			String(newGroupActive) == selectOption.dataset.active){

		$button.addClass('disabled');
		return;
	}

	$button.removeClass('disabled');
}
$('#saveGroup').click(function(){
	
	let data = {};
	let $groupSelect = $('#groupSelect');
	let filterId = parseInt($groupSelect.val());
	if(filterId>=0)
		data.filterId = filterId;

	data.filterpName = $('#newGroupNeme').val().trim();
	data.description = $('#newDescription').val().trim();
	data.order = $('#newGroupOrder').val();
	data.radio = $('#newIsRadio').prop('checked');
	data.active = $('#newGroupActive').prop('checked');

	$.post('/rest/filter', data)
	.done(data=>window.location.reload())
	.fail(function(error) {
		console.error(error);
  	});	
});
$('#groupSelect').change(function(){
	let selectOption = this.options[this.selectedIndex];
	if(selectOption.value=='-1'){
		$('#newGroupNeme').val('');
		$('#newDescription').val('');
		$('#newGroupOrder').val(0).prop('disabled', true);
		let label = $('#newIsRadio').prop('checked', false).prop('nextElementSibling');
		$(label).addClass('disabled');
		label = $('#newGroupActive').prop('checked', true).prop('nextElementSibling');
		$(label).addClass('disabled');
		$('#saveGroup').text('Add Group');
	}else{
		$('#newGroupNeme').val(selectOption.innerText);
		$('#newDescription').val(selectOption.dataset.description);
		$('#newGroupOrder').val(selectOption.dataset.order).prop('disabled', false);
		let label = $('#newIsRadio').prop('checked', selectOption.dataset.radio=='true').prop('nextElementSibling');
		$(label).removeClass('disabled');
		label = $('#newGroupActive').prop('checked', selectOption.dataset.active=='true').prop('nextElementSibling');
		$(label).removeClass('disabled');
		$('#saveGroup').text('Save Group');
	}

	enableSaveButton();
});