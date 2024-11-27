
$('input').on('input', contentInput);

function contentInput(e){
	const $input = $(e.currentTarget);

	if($input.hasClass('array-content')){
		const $parent = $input.parent().parent();
		const $empty = $parent.children().filter((i,el)=>!el.children[0].value);
		if(!$empty.length)
			$parent.append($('<div>', {class: 'col'})
			.append($('<input>', {type:'text', class: 'form-control array-content'}).on('input', contentInput)));
		else if($empty.length>1){
			for(let i=1; i<$empty.length; i++)
				$empty[i].remove();
		}
	}

	const $row = $input.parents('.array-row');
	const $inputs = $row.find('input');
	const $filled = $inputs.filter((i,el)=>el.value);
	const $button = $row.find('button');

	if($filled.length>3 && ($filled.length == $inputs.length || $filled.length == ($inputs.length-1)))
		$button.prop('disabled', false);
	else
		$button.prop('disabled', true);

	if(identical){
		$inputs.filter((i,el)=>el.value===identical && el.classList.contains('array-content')).removeClass('border border-danger');
		$input.removeClass('border border-danger');
		identical = undefined;
	}

	const $changed = $filled.filter((i,el)=> el.dataset.value!=el.value.trim());
	const $arraySize = $input.parents('.array-size');
	const arraySize = $arraySize.data('arraySize');
	const length = $arraySize.find('input').filter((i,el)=>el.value).length;

	if($changed.length || (length && arraySize!=length)){
		$button.text('Save').removeClass('btn-outline-dark').addClass('btn-outline-primary');
	}else
		$button.text('Delete').addClass('btn-outline-dark').removeClass('btn-outline-primary');
}

let identical;

$('.array-btn').click(e=>{

	if(!confirm("Are you sure you want to perform this action?"))
		return;

	e.currentTarget.disabled = true;
	if(e.currentTarget.innerText==='Delete')
		deleteArray(e);

	else
		saveArray(e);
});
function deleteArray(e){
	const $inputs = $(e.currentTarget).parents('.array-row').find('input').filter((i,el)=>el.value);
	const arrayName = $inputs.filter((i,el)=> el.classList.contains('array-name')).val().trim();
	const arrayType = $inputs.filter((i,el)=> el.classList.contains('array-type')).val().trim();
	const arraySubtype = $inputs.filter((i,el)=> el.classList.contains('array-subtype')).val().trim();

	$.post('/arrays/delete', {name: arrayName, type: arrayType, subtype: arraySubtype})
	.done(d=>alert(d));
}
function saveArray(e){

		const $inputs = $(e.currentTarget).parents('.array-row').find('input').filter((i,el)=>el.value);
		const content = $inputs.filter((i,el)=>el.classList.contains('array-content')).map((i,el)=>el.value.trim()).get();
		const hasIdentical = !content.every((val,i,arr)=>{
								const result = arr.filter(v=>v===val).length===1;
									if(!result)
										identical = val;
								return result;
							});
		if(hasIdentical){
			alert('Two identical values ​​were found.');
			$inputs.filter((i,el)=>el.value===identical && el.classList.contains('array-content')).addClass('border border-danger');
			return;
		}

		const postData = {};
		let $input = $inputs.filter((i,el)=> el.classList.contains('array-name'));
		let value = $input.val().trim();
		let data = $input.data('value');
		if(data && data!==value){
			postData.name = data;
			postData.newName = value
		}else
			postData.name = value;

		$input = $inputs.filter((i,el)=> el.classList.contains('array-type'));
		value = $input.val().trim();
		data = $input.data('value');
		if(data && data!==value){
			postData.type = data;
			postData.newType = value
		}else
			postData.type = value;

		$input = $inputs.filter((i,el)=> el.classList.contains('array-subtype'));
		value = $input.val().trim();
		data = $input.data('value');
		if(data && data!==value){
			postData.subtype = data;
			postData.newSubtype = value
		}else
			postData.subtype = value;

		postData.content = content;

		$.ajax({
		    url: '/arrays/save'
		,   type: 'POST'
		,   contentType: 'application/json'
		,   data: JSON.stringify(postData)
		,   success: d=>{alert(d);location.reload();}
		});
}