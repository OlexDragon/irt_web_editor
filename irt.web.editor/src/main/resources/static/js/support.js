const guiText = $('#guiText');
$('#btnGuiText').click(()=>{

	if(!confirm("Are you sure you want to save your data?"))
		return;

	$.post('/rest/suport/gui_text', {text: guiText.val()})
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

const $faqID = $('#faqID');
const $question = $('#question');
const $answer = $('#answer');
const $btnsFAQ = $('button.faq');
const $btnAddFAQ = $('#btnAddFAQ');
const $btnSaveFAQ = $('#btnSaveFAQ');

$('input.faq, textarea.faq').on('input', ()=>{
	if($question.val().trim().length === 0 || $answer.val().trim().length === 0){
		disableButtons($btnsFAQ);
		return;
	}

	let saveAnable = $faqID.val().length > 0

	if(saveAnable)
		enableButtons($btnSaveFAQ, 'btn-outline-primary');
		
	else
		enableButtons($btnAddFAQ, 'btn-outline-success');
	
});
$('#btnClearFAQ').click(()=>{
	$question.val('');
	$faqID.val('');
	$answer.val('')
	disableButtons($btnsFAQ);
});
$btnsFAQ.click(()=>{

	if(!confirm("Are you sure you want to save your data?"))
		return;

	let question = $question.val();
	if(question.length>255){
		alert('The question is too long.\nMaximum length is 255 characters.');
		return;
	}
	let answer = $answer.val();
	if(answer.length>1000){
		alert('The answer is too long.\nMaximum length 1000 characters.');
		return;
	}
	let data = {question: question, answer: answer};
	let id = $faqID.val();

	if(id)
		data.faqID = id;
	
	$.post('/rest/suport/save-faq', data)
	.done(message=>{
		alert(message);
		window.location.reload(true);
	})
	.fail(function(error) {
		window.console.error(error);
  	});	

});
$('p.faq').click(e=>{
	disableButtons($btnsFAQ);
	$faqID.val(e.currentTarget.dataset.id);
	$question.val(e.currentTarget.textContent);
	$.post('/rest/suport/answer', {faqID: e.currentTarget.dataset.id})
	.done(answer=>$answer.val(answer))
	.fail(function(error) {
		window.console.error(error);
  	});
})
function disableButtons($buttons){
		$buttons.prop('disabled', true);
		$buttons.removeClass('btn-outline-success btn-outline-primary').addClass('btn-secondary');
}
function enableButtons($buttons, classToAdd){
		$buttons.prop('disabled', false);
		$buttons.removeClass('btn-secondary').addClass(classToAdd);
}