
$('.btn-delete').click(e=>{

	if(confirm('Are you sure you want to delete this address? - ' + e.currentTarget.value))
		e.currentTarget.setAttribute("disabled", "disabled");
		$.post('/rest/ips/delete', {ip: e.currentTarget.value})
		.done(message=>{

			if(message){
				alert(message);
				return;
			}

			window.location.reload();
		})
		.fail(function(error) {
			window.console.errorr(error);
  		});
});
$('.form-select').change(e=>{

	if(confirm('Are you sure you want to change the status of this address? - ' + e.currentTarget.value))
		$.post('/rest/ips/status', {ip: e.currentTarget.parentElement.parentElement.firstElementChild.innerText, status: e.currentTarget.value})
		.done(message=>{

			if(message){
				alert(message);
				return;
			}
		})
		.fail(function(error) {
			window.console.errorr(error);
  		});
});