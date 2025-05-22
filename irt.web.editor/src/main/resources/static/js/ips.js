
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
			window.console.error(error);
  		});
});
$('.form-select').change(e=>{

	if(confirm('Are you sure you want to change the status of this address? - ' + e.currentTarget.value))
		$.post('/rest/ips/status', {ip: $(e.currentTarget.parentElement.parentElement).find('.ip').text(), status: e.currentTarget.value})
		.done(message=>{

			if(message){
				alert(message);
				return;
			}
		})
		.fail(function(error) {
			window.console.error(error);
  		});
});
const $modal = $('.modal');
const $modalBody = $('.modal-body');
$('div[data-id]').click(e=>{

if(e.target.type != 'select-one')
	$.post('rest/ips/connections', {ipId: e.currentTarget.dataset.id})
	.done(data=>{

		if(data){
			$modalBody.empty();
			let container = $('<table>', {clas: 'table'}).appendTo($modalBody);
			data.forEach(connection=>{
				container
				.append(
					$('<tr>')
					.append($('<td>', {text: connection.id}))
					.append($('<td>', {text: connection.connectTo}))
					.append($('<td>', {text: connection.date}))
				);
			});
			$modal.modal('show');
		}else
			alert("No Connection Entity");
	})
	.fail(function(error) {
		window.console.error(error);
	});
})