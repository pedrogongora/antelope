/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
cargando="<img src='/Juguetes/SDN/Imagenes/procesa.gif' alt='' title='' /> Cargando ...";

function setInContent(idhtml,url){
    if(idhtml == null || url == null){
        alert("No se indicó alguno de los parámetros de entrada (setIntContent)");
        return;
    }
    var node = document.getElementById(idhtml);
    while (node.hasChildNodes()) {
        node.removeChild(node.lastChild);
    }
    $("#"+idhtml).html(cargando);
    $("#"+idhtml).load(url);
}

/* --------------------------------------------------------------------
    Pedro
   -------------------------------------------------------------------- */

// validateDate: valida fechas, devuelve true sólo cuando la fecha tiene
// formato correcto y está dentro de los límites.
function validateDate(dateString, limiteInferior, limiteSuperior) {
    if (dateString && dateString != '') {
        var splits = dateString.split('/');
        if (splits.length != 3) {
            return false;
        } else {
            var d = new Date(splits[2], splits[1]-1, splits[0]);
            return (d >= limiteInferior) && (d <= limiteSuperior);
        }
    } else {
        return false;
    }
}

// Inserta slashes en campos para fechas.
// Hay que vincular con evento 'keypress' de JQuery.
// E.g.:
//      $(document).ready(function() {
//          $('#myDateInput').keypress(easyDate);
//      });
function easyDate(event) {
    var input = $(event.target);
    var value = input.val();
    if (event.which >= 48 && event.which <= 59) { // números
        if (value && value != '') {
            if (value.length == 2 || value.length == 5) {
                value = value + '/';
                input.val(value);
            }
        }
    } else if (event.which == 47) { // slash
        return true; // dejar pasar el evento
    } else if (event.which == 8 || event.which == 0) { // BS y t. de cursor
        return true; // dejar pasar el evento
    } else {
        return false; // no se deja pasar el evento
    }
}

$(document).ready(function() {
    
    // Colores intercalados para renglones de tablas
    $('table.data>thead>tr>th').addClass('ui-widget-header');
    $('table.data').each(function () {
        $(this).find('tr > th:first').addClass('ui-corner-tl');
        $(this).find('tr > th:last').addClass('ui-corner-tr');
    });
    //$('table.data tr>th:first').addClass('ui-corner-tl');
    //$('table.data tr>th:last').addClass('ui-corner-tr');
    $('table.data>tbody>tr:odd').addClass('odd0');
    $('table.data>tbody>tr:even').addClass('odd1');
    
    // dropdown menu
    $('.ddMenuButton > li').click(function() {
        
        var menu = $(this).find('ul');
        if (menu.css('visibility') == 'hidden') {
            menu.css('visibility', 'visible');
        } else {
            menu.css('visibility', 'hidden');
        }
    });
    $('.ddMenuButton > li > ul > li > a').click(function() {
        $('.ddMenuButton > li > ul').css('visibility', 'hidden');
        return false;
    });
    $(document).bind('click', function (e) {
        var $clicked = $(e.target);
        if (!$clicked.parents().hasClass('ddMenuButton')) {
            $('.ddMenuButton > li > ul').css('visibility', 'hidden');
        }
    });
    $('.ddMenuButton > li > button').button({
        icons: {secondary: "ui-icon-triangle-1-s"}
    });
    $('.ddMenuButton > li > ul > li').addClass('ui-state-default');
    $('.ddMenuButton > li > ul > li').mouseover(function() {
        $(this).addClass('ui-state-hover');
    });
    $('.ddMenuButton li ul li').mouseout(function() {
        $(this).removeClass('ui-state-hover').addClass('ui-state-default');
    });
});


/* --------------------------------------------------------------------
    Pedro
   -------------------------------------------------------------------- */

