$('#nb_products').addClass('navbar-brand');

let $inputProductName 	= $('#inputProductName');
let $inputPartNumber 	= $('#inputPartNumber');
let $cbActive 			= $('#cbActive');
let $lblAddImage 		= $('label[for=attachImages]');
let $pdfPath			= $('#pdfPath');

let $btnSavePDF			= $('#btnSavePDF')
				.click(e=>{
				});

let selectedProduct = null;

let $productSelect 	= $('#productSelect')

				.change(function(){

					$button.addClass('disabled');

					let productId = this.value;

					if(productId=='-1'){
						$button.text('Add Product');
						selectedProduct = null;
						let label = $cbActive.prop('checked', true).prop('nextElementSibling');
						$(label).addClass('disabled');
						$inputProductName.val('');
						$inputPartNumber.val('');
						$lblAddImage.addClass('disabled');
						$pdfPath.prop('readonly', true);
						$btnSavePDF.addClass('disabled');
						return;
					}

					$button.text('Save Product');
					selectedProduct = products.find(o=>o.id == productId);

					if(!selectedProduct)
						return;

					$lblAddImage.removeClass('disabled');
					$inputProductName.val(selectedProduct.name);
					$inputPartNumber.val(selectedProduct.partNumber);
					let label = $cbActive.prop('checked', selectedProduct.active).prop('nextElementSibling');
					$(label).removeClass('disabled');
					$pdfPath.prop('readonly', false);
					$btnSavePDF.removeClass('disabled');

					$('#collapseFilters input[type=checkbox]').prop('checked', false);
					selectedProduct.filterIds.forEach(id=>{
						$(`#collapseFilters input[value=${id}]`).prop('checked', true);
					})
    				$('#imageContent').load(`/products/images?productId=${productId}`);
				});

let $button = $('#btnSaveProduct')

				.click(function(e){
					e.preventDefault();

					let data = {};
					let productId = $productSelect.val();

					if(productId!='-1')
						data.productId = productId;

					data.name = $inputProductName.val().trim();
					data.partNumber = $inputPartNumber.val().trim();
					data.active = $cbActive.prop('checked');

					$.post('/rest/product', data)
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


$('#productEditor').on('input', 'input', function(){
	enableSaveButton();
})
.on('change', 'input', function(e){
	e.stopPropagation();
});

function enableSaveButton(){


	let inputProductName 	= $inputProductName.val().trim();
	let inputPartNumber 	= $inputPartNumber.val().trim();

	if(	(!selectedProduct && inputProductName && inputPartNumber) || 
		(selectedProduct && (selectedProduct.name != inputProductName || selectedProduct.partNumber != inputPartNumber) )){
		$button.removeClass('disabled');
		return;
	}

	$button.addClass('disabled');
}
$('#collapseFilters input').change(function(e){

	if(selectedProduct==null){
		e.stopPropagation();
		$(this).prop('checked', false);
		alert('Select the menu first.');
		return;
	}

	let checked = $.map($('#collapseFilters input').filter((i,o)=>o.checked), (o, i)=>parseInt(o.value)).sort();

	if(checked.length==selectedProduct.filterIds.length && JSON.stringify(checked) === JSON.stringify(selectedProduct.filterIds)){
		$('#btnSaveFilters').addClass('disabled');
	}else{
		$('#btnSaveFilters').removeClass('disabled');
	}
});
$('#btnSaveFilters').click(function(){

	if(!selectedProduct)
		return;

	let checked = $.map($('#collapseFilters input').filter((i,o)=>o.checked), (o, i)=>parseInt(o.value));

	$.post('/rest/product/filter', {productId: selectedProduct.id, checked: checked})
	.done(data=>window.location.reload())
	.fail(function(error) {
		console.error(error);
  	});	
});

// Images
$('#attachImages').change(e=>{

	let productId = $productSelect.val();
	let files = e.currentTarget.files;
	if(!(productId != '-1' && confirm(`Are you sure you want to save ${files.length} image${files.length>1 ? "s" : ""}?`)))
		return;

	let fd = new FormData();
	fd.append("productId", productId);
	$.each(files, (i,f)=>{
		fd.append("fileToAttach", f)
	});

	$.ajax({
    	url: '/images/product/add',
    	data: fd,
    	cache: false,
    	contentType: false,
    	processData: false,
    	method: 'POST',
    	type: 'POST', // For jQuery < 1.9
    	success: function(data){
    		$('#imageContent').load(`/products/images?productId=${productId}`);
    	},
        error: function(error) {
			if(error.statusText!='abort')
				alert(error.responseText);
        }
	});
});