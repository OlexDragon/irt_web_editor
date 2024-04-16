$('#groupSelect').change(function(){

	$('#filterContent').empty();
	let $name = $('#newFilterNeme');
	let $description = $('#newDescription');

	let val = $(this).val();
	if(val){
		$('#filterSelect').load('/filter_options?ownerId=' + val, function(data){
			$.each($(data), function(){
				if(this.localName != 'option' || parseInt(this.value)<0)
					return;
				let $filterContent = $('#filterContent');
				let $row = $('<div>', {class: 'row'})
					.append($('<div>', {class:'col-auto', text: 'ID: ' + this.value}))
					.append($('<div>', {class:'col', text: this.innerText}))
					.append($('<div>', {class: 'col', text: this.dataset.description ? this.dataset.description : ''}))
					.append($('<div>', {class: 'col-1', text: 'Order: ' + this.dataset.order}))
					.append($('<div>', {class: 'col-1', text: this.dataset.radio=='true' ? 'RADIO' : 'CHECK'}))
					.append($('<div>', {class: 'col-1', text: this.dataset.active ? 'Active' : 'OFF'}));
				$filterContent.append($row);
			});
		});
		$name.prop('disabled', false);
		$description.prop('disabled', false);
	}else{
		$name.prop('disabled', true);
		$description.prop('disabled', true);
	}

	$('#newFilterOrder').prop('disabled', true);

	let label = $('#newFilterActive').prop('checked', true).prop('nextElementSibling');
	$(label).addClass('disabled');
	$('#btnSaveFilter').text('Add Filter');
});
$('#filterSelect').change(function(){
	let selectOption = this.options[this.selectedIndex];
	if(selectOption.value=='-1'){
		$('#newFilterNeme').val('');
		$('#newDescription').val('');
		$('#newFilterOrder').val(0).prop('disabled', true);
		let label = $('#newIsRadio').prop('checked', false).prop('nextElementSibling');
		$(label).addClass('disabled');
		label = $('#newFilterActive').prop('checked', true).prop('nextElementSibling');
		$(label).addClass('disabled');
		$('#btnSaveFilter').text('Add Filter');
	}else{
		$('#newFilterNeme').val(selectOption.innerText);
		$('#newDescription').val(selectOption.dataset.description).prop('disabled', false);
		$('#newFilterOrder').val(selectOption.dataset.order).prop('disabled', false);
		let label = $('#newIsRadio').prop('checked', selectOption.dataset.radio=='true').prop('nextElementSibling');
		$(label).removeClass('disabled');
		label = $('#newFilterActive').prop('checked', selectOption.dataset.active=='true').prop('nextElementSibling');
		$(label).removeClass('disabled');
		$('#btnSaveFilter').text('Save Filter');
	}

	enableSaveButton();
});
$('#newFilterNeme').on('input', function(){
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
$('#newFilterOrder').on('input', function(){
	enableSaveButton();
})
.change(function(e){
	e.stopPropagation();
});
$('#newIsRadio').change(function(){
	enableSaveButton();
});
$('#newFilterActive').change(function(){
	enableSaveButton();
});

function enableSaveButton(){

	let $button = $('#btnSaveFilter');
	let $newFilterNeme = $('#newFilterNeme');
	let newFilterNemeVal = $newFilterNeme.val().trim();

	if(!newFilterNemeVal){
		$button.addClass('disabled');
		return;
	}

	let newDescriptionVal = $('#newDescription').val().trim();
	let newFilterOrder = $('#newFilterOrder').val();
	let newIsRadio = $('#newIsRadio').prop('checked');
	let newFilterActive = $('#newFilterActive').prop('checked');

	let select = $('#filterSelect').get(0);
	let selectOption = select.options[select.selectedIndex];

	if(		selectOption.text == newFilterNemeVal && 
			((newDescriptionVal == '' && typeof(selectOption.dataset.description) == "undefined") || (newDescriptionVal == selectOption.dataset.description)) && 
			newFilterOrder == selectOption.dataset.order && 
			String(newIsRadio) == selectOption.dataset.radio &&
			String(newFilterActive) == selectOption.dataset.active){

		$button.addClass('disabled');
		return;
	}

	$button.removeClass('disabled');
}
$('#btnSaveFilter').click(function(){
	
	let data = {};
	let filterId = $('#filterSelect').val();
	if(filterId>=0)
		data.filterId = filterId;

	data.ownerId	 = parseInt($('#groupSelect').val());
	data.filterpName = $('#newFilterNeme').val();
	data.description = $('#newDescription').val();
	data.order		 = $('#newFilterOrder').val();
	data.radio		 = $('#newIsRadio').prop('checked');
	data.active		 = $('#newFilterActive').prop('checked');

	$.post('/rest/filter', data)
	.done(data=>window.location.reload())
	.fail(function(error) {
		console.error(error);
  	});	
});