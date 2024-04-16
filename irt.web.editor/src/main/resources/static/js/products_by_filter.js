const $chechBoxes = $('#productsContent input[type=checkbox]')
const $filterSelect = $('#filterSelect').change(function(){

	$chechBoxes.prop('checked', false);
	let filterId = this.value;
	let withFilter = products.filter(p=>{
											let filters = p.filterIds.filter(f=>{
												return f == parseInt(filterId);
											});
									return filters.length;
								});
	withFilter.forEach(p=>{
		$chechBoxes.filter((i, cb)=>{
			return parseInt(cb.value)==p.id;
		}).prop('checked', true);
	});
});

$chechBoxes.change(function(){

	if(!confirm('Are you sure you want to save your changes?')){
		this.checked = !this.checked;
		return
	}

	let selectedProduct = products.find(o=>o.id == this.value);
	if(!selectedProduct){
		this.checked = false;
		return;
	}

	if(this.checked){
		selectedProduct.filterIds.push($filterSelect.val());
	}else{
		const indexOf = selectedProduct.filterIds.indexOf(parseInt($filterSelect.val()));
		if(indexOf<0)
			return;
		selectedProduct.filterIds.splice(indexOf, 1);
	}
	$.post('/rest/product/filter', {productId: this.value, checked: selectedProduct.filterIds})
//		.done(data=>window.location.reload())
	.fail(function(error) {
		console.error(error);
  	});	
});