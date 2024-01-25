
  $(".m_products a").click(function(e){
    //alert('Hello')
    e.preventDefault();
    let environment = $(this).data('environment'),
        family= $(this).data('family'),
        type = $(this).data('type'),
        power = $(this).data('power'),
        voltage=$(this).data('voltage'),
        device = $(this).data('device'),
        queryString ="";
        console.log(family);
    if(environment){
        queryString ='?environment='+ encodeURIComponent(environment)
    }
    if(family){
        queryString ='?family='+ encodeURIComponent(family)
    }
    if(type){
        queryString ='?type='+ encodeURIComponent(type)
    }
    if(power){
        queryString ='?power='+ encodeURIComponent(power)
    }
    if(voltage){
        queryString ='?voltage='+ encodeURIComponent(voltage)
    }
    if(device){
        queryString ='?device='+ encodeURIComponent(device)
    }
    
    
    window.location.href = 'products.html' + queryString;
     
    })


//=========================

let timer = null;
let filterValue = {};
 filterValue.filter = [];
 console.log(filterValue);
let selectedFilters = [];
let $matte = $('.matte');
let $matteLoad = $('.matte-1');
let templeteProduct = document.querySelector('#templete-product');
let ourProduct = $('.our-product');
var perPage = 1;
let url = window.location.pathname.split('?')[0];
let f = '?';
let isFirstLoad = true;
$('.filter input')
.on('input', function() {
    filter();
});

function filter() {
    let newFilterValue = {};
    newFilterValue.filter = [];
    $matte.removeClass('active');
    $matteLoad.removeClass('load');
    ourProduct.removeClass('dnone') ;
    //ourProduct.removeClass('productnone') ;
    templeteProduct.classList.add('d-none');
    let visibleTemplete =false;
    //filterValue.filter = [];
    
    
    if (timer !== null) {
        clearTimeout(timer);
    }
    
    //getFiltersFromUrl();
    let search = $('#filter-name').val();
    if (search) {
        newFilterValue.search = search;
    }
    
    let selectedCheckboxes = $('.filter input:checked');
    if (selectedCheckboxes.length > 0) {
        selectedCheckboxes.each(function() {
            let idAndText = {};
            idAndText.checkboxtext = this.parentElement.innerText;
            idAndText.checkboxId = this.value;
            
            let fam = this.closest('.filter-family');
            idAndText.family = fam.firstElementChild.innerText;

            
            newFilterValue.filter.push(idAndText);

        });
    }

    UpdateCurrentFilters(newFilterValue.filter);
   

    
    if (isFilterValueEqual(newFilterValue)) {
        return;
    }

   $matteLoad.addClass('load');
   

  //  if (isFirstLoad) {
     
  //  }   
    timer = setTimeout(function() {
      $matte.addClass('active').show();
      setTimeout(firstLoad(), 2000); 

        
        f='?';    
             
        if (newFilterValue.search) {
            f += 'search=' + encodeURIComponent(newFilterValue.search) + '&';
        }
        if (newFilterValue.filter.length > 0) {
            newFilterValue.filter.forEach(function(idAndText) {
                f += 'filter=' + encodeURIComponent(idAndText.checkboxId) + '&';
            });
        }
        if (f != "?") {
                
          f = f.substring(0, f.length - 1);
      }
        

        filterValue = newFilterValue;
   // обновляет строку запроса с помощью метода "pushState()" объекта "history"
   const newUrl = url + f;
history.pushState(null, '', newUrl);
        //history.pushState(null, '', url + f);
        $matteLoad.removeClass('load'); 
        
       
    }, 3000);
    
}

function isFilterValueEqual(b) {

  if(b.search != filterValue.search)
    return false;
 
 let valueNewFilter1= $.map(b.filter,function(a,i){
    return a.checkboxId;
  }).sort();
  let valueNewFilter2= $.map(filterValue.filter,function(a,i){
    return a.checkboxId;
  }).sort();

    return JSON.stringify(valueNewFilter1) === JSON.stringify(valueNewFilter2);

} 

function getFiltersFromUrl(){
  
  var params = new URLSearchParams(window.location.search);
  params.forEach(function(value, key) { 
    let $filterName = $('#filter-name').val('');
    if (key == 'search') {
      $filterName.val(value);
      filterValue.search = value;
    }else{
      let $filterInputs = $(`.filter input[value=${value}]`);
      $filterInputs.prop('checked', false);
       let r = $filterInputs.prop('checked', true);
      filterValue.filter.push({'checkboxId' : value}); 
     
    }
  });
}




function firstLoad() {
  ourProduct.addClass('dnone');
  let length = $('#productList').children().length;

  //находим элемент и даем ему новые id
  let templeteVisible =$('#templete-product').clone().prop('id', 'tttt' + length);
  $('#productList').append(templeteVisible.removeClass('d-none'));
  $matte.fadeOut();
  visibleTemplete=true
  if (visibleTemplete=true) {
      setTimeout(secondLoad(), 1000);
         
  }
  
}

function secondLoad() {
  var isLoading = false; // переменная для проверки, загружаются ли уже данные
  // обработчик события прокрутки страницы
   $(window).on('resize scroll', function() { 
    
    if ($(document).scrollTop() > 100) {
      $(".filter").addClass("test");
    } else {
      $(".filter").removeClass("test");
    }
     loadMore(); // если пользователь достиг конца списка, загружаем следующую порцию данных
  });
  
  // вызов функции загрузки при первоначальной загрузке страницы
  loadMore();

}



function loadMore() {
   
    let  bb= $('#checkLoad').offset().top
    let aa = $(window).scrollTop()
    let cc=$(window).height()
    let visible=aa >= bb - cc
    if (visible) {
      for (var i = 0; i < perPage; i++) {
        let length = $('#productList').children().length;
        var newProduct = $('#templete-product').clone().prop('id', 'tttt' + length);
          newProduct.removeClass('d-none');
          $('#productList').append(newProduct);
         
        }
      
    } else {
      return;
    }
    let allNewProd = $('.newprod')
    //console.log(allNewProd);
   
    if(allNewProd.length>200){
        console.log(">20");
       //window.removeEventListener('scroll', loadMore)
       
       $(window).off('scroll');
       
    }
    // сбрасываем флаг загрузки
    //isLoading = false;

    loadMore();
  }
 
  
    
  //   getFiltersFromUrl();
  
    
  window.addEventListener('popstate', function(event) {
     
    //getProducts();
    getFiltersFromUrl();
    getProducts();
    });
  



//gestionnaire du clic sur la croix du current filtres

  $("#filters-selected").on("click", ".filter-cross", function () { 
     clickFilterCross(this);
  });

  function clickFilterCross(filtersSelected){
    // data-value of X
    let currenVal = filtersSelected.dataset.value;
      
    // Supprimer la sélection de la case correspondante
    let inputlVal = $(`.filter input[value=${currenVal}]`);
    inputlVal.prop("checked", false);
    filter();
  
    filtersSelected.parentElement.remove();
    
    const tooltip = bootstrap.Tooltip.getInstance(filtersSelected.parentElement);
    if (tooltip) {
      tooltip.dispose();
    }
}


// fonction pour mettre à jour les current filtres  dans le balisage
function UpdateCurrentFilters(filter) {
  //nettoyer les filtres actuels dans le balisage
  $(".filters-list").empty();

    //=========================
    filter.forEach((idAndText) => {
      
        let $filterItem = $("<li>", {'class':"filter-item ", 
        'title':idAndText.family, 'data-toggle':"tooltip",
        'data-placement':"top", 
        'tabindex': "0"}); 
        const $filterValue = $("<span>", { 
              'class': "filter-value"  
        }).text(idAndText.checkboxtext);
        const $filterCross = $("<span>", { 
              class: "filter-cross", 
              'data-value':idAndText.checkboxId 
        }).text(" \u2715")
          .hover(function() {
            $(this).toggleClass('active')
              var $filterValue = $(this).parent(".filter-item").find(".filter-value");
              $filterValue.css('color', 'red');
              }, function() {
                    var $filterValue = $(this).parent(".filter-item").find(".filter-value");
                    $filterValue.css('color', '#0f4178');
              });
              $filterItem.append(  $filterValue, $filterCross); 
              $(".filters-list").append($filterItem); 
              
              const tooltip = new bootstrap.Tooltip($filterItem) 
                // $filterItem.tooltip();
             
        
    });
}

$('.filter-cross').hover(function() {
    var $filterValue = $(this).next();
    $filterValue.css('font-weight', 'bold');
  }, function() {
    var $filterValue = $(this).next();
    $filterValue.css('font-weight', '');
  });

  getFiltersFromUrl();



console.log(selectedFilters);
  

$(".selected-filter-wrapper").hover(function() {
    let del = $(this).find(".delete");
    if($(".filter-item").length > 0 || del.is(":visible")){
          del.toggle();
      }
});
    
$(".delete-all-button").click(function() {

 //     let filterCurrentItem= $('.filter-item');
    let filterCrossElements = $(".filter-cross");
    //let filterCrossElements= document.querySelectorAll()

    //==============js foreach======
    //  filterCrossElements.forEach(function(filterCrossElement){
    //   clickFilterCross(filterCrossElement)
    // })

    //====jQuery each========
    filterCrossElements.each(function(i){
      clickFilterCross(filterCrossElements[i])
    })



    //=============js for=======  
    // for (let i = 0; i < filterCrossElements.length; i++) {
    //   clickFilterCross(filterCrossElements[i])
      
    // }

   //   filterCurrentItem.remove();
      $(".filter input").prop("checked", false);
});






 