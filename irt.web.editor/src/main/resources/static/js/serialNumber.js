const $newSn = $('#newSn').on('input', disableBtnCreate);
const $selectedPn = $('#selectedPn');
const $btnCreate = $('#btnCreate').click(create);
const $newdPn = $('#newdPn').on('input', onNewPnInput);
const $newDescription = $('#newdDescription').on('input', onNewPnInput);
const $content = $('#content');
const $filterSn = $('#filterSn');
const $filterPn = $('#filterPn');
const $filterDescr = $('#filterDescr');
const $modal = $('.modal');
const $hiddenPnId = $('#hiddenPnId');
const $selectPn = $('#selectPn').change(()=>{
	$newdPn.val('');
	$newDescription.val('');
	const $option = $selectPn.find(":selected")
	$hiddenPnId.val($option.val());
	$selectedPn.val($option.text());
	disableBtnCreate();
});
function disableBtnCreate(){
	const disable = !($newSn.val() && ($hiddenPnId.val() || ($newdPn.val() && $newDescription.val())));
	$btnCreate.prop('disabled', disable);
}
$('.filter').change(()=>{
	const toSend = {};

	const sn = $filterSn.val();
	if(sn)
		toSend.sn = sn;

	const pn = $filterPn.val();
	if(pn)
		toSend.pn = pn;

	const d = $filterDescr.val();
	if(d)
		toSend.d = d;

	if(!Object.keys(toSend).length)
		return;

	$content.load('/sn/content', toSend);
});
$('#btnNew').click(()=>{

	$modal.modal('show');

	$selectPn.empty();
	$.get('rest/sn/pn')
	.done(fillPartNumbers);
});
let timeout
$('#modalFilter').on('input', e=>{
	clearTimeout(timeout);
	timeout = setTimeout(()=>{
		$selectPn.empty();
		$.get('/rest/sn/pn', {search: e.currentTarget.value})
		.done(fillPartNumbers);
	}, 3000);
});
function fillPartNumbers(data){

		if(!data || !data.length)
		return;

	data.forEach(d=>{
		$selectPn.append($('<option>', {value: d.id, text: d.partNumber + ' - ' + d.description}))
	});
}
function onNewPnInput(){
	$hiddenPnId.val('');
	$selectedPn.val($newdPn.val() + ' - ' + $newDescription.val());
	disableBtnCreate();
}
function create(){
	const toSend = {};
	toSend.sn = $newSn.val();
	toSend.pnId = $hiddenPnId.val();
	toSend.newPn = $newdPn.val();
	toSend.description = $newDescription.val();
	$.post('/rest/sn/create', toSend)
	.done(data=>{
		if(!data)
			retrun;

		alert(data.message);
	});
}