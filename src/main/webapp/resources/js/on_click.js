var ctx = $("#graf")[0].getContext("2d");

resizeCtxCanvas(ctx);

function onlyOne(checkbox) {
    document.getElementsByName('r').forEach((item) => { if (item !== checkbox) item.checked = false; });
    $('input[type="checkbox"]').is(":checked")
        ? $('.button-label').removeClass('invalid').removeClass('glowing_bottons')
        : $('.button-label').addClass('invalid');
}

function validate() {
    return $("input[type='radio']:checked").length === 1 && document.getElementById("y-field").validity.valid && $('input[type="checkbox"]').is(":checked");
}

function getRow(obj) {
    return '<tr class="removable">'
        + '<td>' + obj.x + '</td>'
        + '<td>' + obj.y + '</td>'
        + '<td>' + obj.r + '</td>'
        + '<td>' + obj.hit + '</td>'
        + '<td>' + obj.curtime + '</td>'
        + '<td>' + obj.exectime + '</td>';
}

function resizeCtxCanvas(ctx) {
    const { width, height } = ctx.canvas.getBoundingClientRect();
    ctx.canvas.width = width;
    ctx.canvas.height = height;
}

function redrawDots(ctx) {
    for (let dot of Object.values(localStorage)) {
        dot = JSON.parse(dot);
        drawDot(ctx, dot.x * (ctx.canvas.width / dot.canvas_size), dot.y * (ctx.canvas.width / dot.canvas_size));
    }
}

function drawDot(ctx, x, y) {
    let circle = new Path2D();
    circle.arc(x, y, 8, 0, 2 * Math.PI);
    ctx.fillStyle = "#f5f5f5";
    ctx.fill(circle);
}

function removeDots(ctx) {
    ctx.save();

    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

    ctx.restore();
}

function transformCoords(x, y, half_canvas_size) {
    console.log(Number($('#j_idt10\\:r').val()));
    r = Number($('#j_idt10\\:r').val());
    x = ((x - half_canvas_size) * ((r + (r * 0.705)) / half_canvas_size)).toFixed(5);
    y = (((-1 * (y - half_canvas_size))) * ((r + (r * 0.705)) / half_canvas_size)).toFixed(5);
    return { x: x, y: y, r: r };

}

/* 
function sendData(data) {
    $.ajax({
        url: 'http://127.0.0.1:3107/web-lab2/controller_servlet',
        dataType: "json",
        data: data,
        beforeSend: () => $('.button').attr('disabled', 'disabled'),
        success: function (data) {
            $('.button').attr('disabled', false);
            $('#result-table').append(getRow(data));
        },
        statusCode: {
            400: (x) => alert(x)
        },
        error: () => alert("Something went wrong!")
    });
}
*/

/* 
$('form').on('submit', function (event) {
    event.preventDefault();
    if (!validate()) return;
    sendData($('form').serialize());
});
*/


$(document).ready(function () {
    redrawDots(ctx);
});

$('#j_idt10\\:reset-button').click(function () {
    console.log(ctx.canvas.width);
    console.log(ctx.canvas.height);
    localStorage.clear();
    removeDots(ctx);
});


window.addEventListener('resize', () => {
    resizeCtxCanvas(ctx);
    redrawDots(ctx);
});


$("#graf").click((e) => {
    var x = e.offsetX
        y = e.offsetY;

    localStorage.setItem(localStorage.length,
        JSON.stringify({ x: x, y: y, canvas_size: ctx.canvas.width }));

    drawDot(ctx, x, y);

    const { x: x0, y: y0, r} = transformCoords(x, y, ctx.canvas.width / 2);

    document.getElementById("canvas_form:canvas_x").value = x0;
    document.getElementById("canvas_form:canvas_y").value = y0;
    document.getElementById("canvas_form:canvas_r").value = r;
    canvas_submit();

    //$('.button-label').addClass('glowing_bottons');
 
});