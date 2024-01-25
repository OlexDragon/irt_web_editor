$('#nb_home_page').addClass('navbar-brand');

getDataFromDB();

function getDataFromDB(){

	$.post('/rest/page_valiables', {pageName: 'home_page'}, data=>{

		if(!data)
			return;

		data.forEach(n=>{

			$(`[data-subordinate-id=${n.nodeId}]`).filter((i,el)=>el.dataset.valueType == n.valueType).val(n.value);

			switch(n.valueType){
			case 'TEXT':
				$('#subordinate-' + n.nodeId).text(n.value);
				break;

			case "CLASS":
				$('#subordinate-' + n.nodeId).addClass(n.value);
				break;

			default:
				$('#subordinate-' + n.nodeId).attr(n.valueType, n.value);
				
			}
		});
	})
	.fail(err=>console.error(err));

// Carousel
	$.post('/rest/page_valiables', {pageName: 'home_slider'}, data=>{

		if(!data)
			return;

		let o = {}
		data.forEach(n=>{

			let index = n.nodeId.replace(/\D/g,'');
			let values = o[index];

			if(!values)
				o[index] = [];

			o[index].push({id: n.nodeId, value: n.value}); 
		});

		for(let key in o){

			let titleId, titleValue, descriptionId, descriptionValue, linkId, href;		

			o[key].forEach(
				d=>{

					if(d.id.startsWith('sliderTitle')){

						titleId = d.id;
						titleValue = d.value;

					} else if(d.id.startsWith('sliderDescription')){

						descriptionId = d.id;
						descriptionValue = d.value;

					} else if(d.id.startsWith('sliderLink')){

						linkId = d.id;
						href = d.value;
					}
				})

			addCard(titleId, titleValue, descriptionId, descriptionValue, linkId, href)
		}
	})
	.fail(err=>console.error(err));
}

let $hpToast = $('#hpToast');
$('button[type=submit]').click(function(){

	let $values = $(this).parents('.editor-card').find('.hpValue');
	let toSend = {}
	toSend.pageName = 'home_page';
	toSend.values = [];

	$values.each((i,v)=>toSend.values.push({nodeId: v.dataset.subordinateId, value: v.value.trim(), valueType: v.dataset.valueType}));

	postValues(toSend);
});

let $textBanner = $('.text-banner');
$('#bannerAlignment').change(
	function(){

		enableButton(this);

		$textBanner.removeClass('text-banner-right text-banner-left');
		if(this.value)
			$textBanner.addClass(this.value);
	});


$('.editor_segment input').add('.editor_segment textarea').on('input', function(){

	enableButton(this);

	let $destination = $(`#subordinate-${this.dataset.subordinateId}`);
	switch(this.dataset.valueType){

	case 'TEXT':
		$destination.text(this.value);
		break;

	case 'class':
		$destination.addClass(this.value);
		break;

	default:
		if(this.dataset.valueType)
			$destination.attr(this.dataset.valueType, this.value);
	}
});

let $slider = $('.slider');
let $sliderTitle = $('#siderCardTitle');
let $sliderDescription = $('#sliderCardDescription');
let $sliderLink = $('#sliderCardLink');
let $sliderCount = $('#sliderCardCount');
let $selectedCard;

let $btnAddCard = $('#addCard').click(
	e=>{

		let ids = [0];
		$slider.find('h5').each((i,el)=>ids.push(parseInt(el.id.replace(/\D/g,''))));

		let index = Math.max.apply(Math, ids) + 1;

		addCard(
			'sliderTitle' + index, 
			$sliderTitle.val(), 
			'sliderDescription' + index, 
			$sliderDescription.val(), 
			'sliderLink' + index, 
			$sliderLink.val());

		let count = $slider.children().length;
		$sliderCount.text(`You have ${count} card` + (count==0 || count>1 ? 's' : ''));

		e.currentTarget.disabled = true;
		$btnSaveCard.prop('disabled', false);
//		$btnDeleteCard.prop('disabled', false);
	});

let $btnSaveCard = $('#saveCard').click(

	()=>{

		if(!confirm('Save changes?'))
			return;

		if(!$selectedCard){
			showToast('Not Selected', 'To select a card, click on it.');
			return;
		}

		let toSend = sliderToSend($selectedCard);
		postValues(toSend);

		$selectedCard = null;
		$btnSaveCard.prop('disabled', true);
		$btnDeleteCard.prop('disabled', true);
	});

let $btnDeleteCard = $('#deleteCard').click(

	()=>{

		if(!confirm('Save changes?'))
			return;

		if(!$selectedCard){
			showToast('Not Selected', 'To select a card, click on it.');
			return;
		}

		showToast('Not Implement', 'The delete button does not work yet.');

		$btnSaveCard.prop('disabled', true);
		$btnDeleteCard.prop('disabled', true);
	});

let mousePosition = -1;
let $sliderParent = $slider.parent();
$sliderParent.mouseout(()=> mousePosition = -1)
$sliderParent.mousemove(

	e=>{

		if(mousePosition<0 || e.originalEvent.buttons>0){
			mousePosition = e.originalEvent.clientX;
			return;
		}

		let change = mousePosition - e.originalEvent.clientX;
		if(!change)
			return;

		let marginLeft = parseInt($slider.css('margin-left'));
		let moveTo = marginLeft - change;
		let sum = moveTo;
		$slider.children().map(

			(i,el)=>{

				let s = el.currentStyle || window.getComputedStyle(el);
				return el.offsetWidth + parseInt(s.marginLeft) + parseInt(s.marginRight);
			})

		.each((i, num) => sum += num);

		if($sliderParent.width() > sum)
			return;

		mousePosition = e.originalEvent.clientX;

		if(moveTo>0)
			return;

		$slider.css('margin-left', moveTo);
	});

function enableButton(element){

	let $segment = $(element).parents('.editor_segment');
	let $inputs = $segment.find('input, textarea');
	let $btnSubmit = $segment.find('button').filter((i,b)=>{
		return b.type == 'submit' || b.id == 'addCard';
	});
	let $haveValue = $inputs.filter((i,el)=>el.value);

	if($haveValue.length == $inputs.length)
		$btnSubmit.prop('disabled', false);

	else
		$btnSubmit.prop('disabled', true);
}
function postValues(toSend){

	$.ajax( '/rest/values/save',
		{
			data: JSON.stringify(toSend),
			contentType : 'application/json',
    		type : 'POST'
    	})
    .done(
		data=>{
    		data.forEach(d=>{

 			  	let keys = Object.keys(d);
 			  	if(!keys.length)
 			  		return;

				let $toast = $('<div>', {class: 'toast', role: 'alert', 'aria-live': 'assertive', 'aria-atomic': 'true'})
    							.append($('<div>', {class: 'toast-header text-bg-primary'})
    										.append($('<strong>', {class: 'me-auto', text: keys[0]}))
    										.append($('<button>', {type: 'button', class: 'btn-close', 'data-bs-dismiss': 'toast', 'aria-label': 'Close'})))
    							.append($('<div>', {class: 'toast-body', text: d[keys[0]]}));

			   	$hpToast.append($toast);

				bootstrap.Toast.getOrCreateInstance($toast, {delay: 30000}).show();
				$toast.on('hidden.bs.toast', e=>$(e.target).remove());
    		});
		})
    .fail(err=>console.error(err));
}

function addCard(titleId, titleValue, descriptionId, descriptionValue, linkId, href){

	let $h5 = $('<h5>', {id: titleId, class: 'card-title fw-bold text-sm', text: titleValue});
	let $p = $('<p>', {id: descriptionId, class: 'card-text text-sm', text: descriptionValue});
	let $a = $('<a>', {id: linkId,  href: href}).prop('hidden', true);

	$selectedCard = $('<div>', {class: 'card home-cardcol-md-3  my-2 slide'})

					.append(
						$('<img>', {class: 'card-img-top my-2', src: '/images/jpeg/dont_photo.jpg', alt: 'Card image cap'}))
					.append(
						$('<div>', {class: 'card-body card-product'})
						.append($h5)
						.append($p)
						.append($a)
					)
					.appendTo($slider)
					.click(
						e=>{
							$selectedCard = $(e.currentTarget);
							$selectedCard.find('h5, p, a')
							.each(
								(i,el)=>{

									if(el.id.startsWith('sliderTitle'))
										$sliderTitle.val(el.innerText);

									else if(el.id.startsWith('sliderDescription'))
										$sliderDescription.val(el.innerText);

									else if(el.id.startsWith('sliderLink'))
										$sliderLink.val(el.href);
								});
							$btnSaveCard.prop('disabled', false);
						});
}

function sliderToSend($el){

	let toSend = {};
	toSend.pageName = 'home_slider';
	toSend.values = [];
	$el.find('h5, p').each((i,el)=>toSend.values.push({nodeId: el.id, value: el.innerText, valueType: 'TEXT'}));
	$el.find('a').each((i,el)=>toSend.values.push({nodeId: el.id, value: el.href, valueType: 'HREF'}));

	return toSend;
}

function showToast(title, body){

		let $toast = $('<div>', {class: 'toast', role: 'alert', 'aria-live': 'assertive', 'aria-atomic': 'true'})
    					.append($('<div>', {class: 'toast-header text-bg-primary'})
    								.append($('<strong>', {class: 'me-auto', text: title}))
    								.append($('<button>', {type: 'button', class: 'btn-close', 'data-bs-dismiss': 'toast', 'aria-label': 'Close'})))
    					.append($('<div>', {class: 'toast-body', text: body}));

	   	$hpToast.append($toast);

		bootstrap.Toast.getOrCreateInstance($toast, {delay: 30000}).show();
		$toast.on('hidden.bs.toast', e=>$(e.target).remove());
}