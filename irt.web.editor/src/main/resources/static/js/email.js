
let $emailData = $('.email-data');
let $hpToast = $('#hpToast');

$('#saveEmailData').click(e=>{
	if(!confirm("Do you want to save EMail Data?"))
		return;

	let toSend = {}
	toSend.pageName = 'email';
	toSend.values = [];

	$emailData.each((i,e)=>{

		let value = e.value.trim();
		if(value)
			toSend.values.push({nodeId: e.id, value: value, valueType: 'TEXT'})
	});

	if(!toSend.values.length){
		alert('No Data to save.')
		return;
	}

	$.ajax(
		'/rest/email/values/save',
		{
			data: JSON.stringify(toSend),
			contentType : 'application/json',
    		type : 'POST'
    	})
    .done(data=>{

		let $toast = $('<div>', {class: 'toast', role: 'alert', 'aria-live': 'assertive', 'aria-atomic': 'true'})
    					.append($('<div>', {class: 'toast-header text-bg-primary'})
    								.append($('<strong>', {class: 'me-auto', text: "Save e-mail data."}))
    								.append($('<button>', {type: 'button', class: 'btn-close', 'data-bs-dismiss': 'toast', 'aria-label': 'Close'})))
    					.append($('<div>', {class: 'toast-body', text: data}));

	  	$hpToast.append($toast);

		bootstrap.Toast.getOrCreateInstance($toast, {delay: 30000}).show();
		$toast.on('hidden.bs.toast', e=>$(e.target).remove());
    })
    .fail(err=>console.error(err));
});