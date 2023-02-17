var ctx = $("#graf")[0].getContext("2d");

resizeCtxCanvas(ctx);

function resizeCtxCanvas(ctx) {
    const { width, height } = ctx.canvas.getBoundingClientRect();
    ctx.canvas.width = width;
    ctx.canvas.height = height;
}

function redrawDots(ctx) {
    r = Number(getById('r').val());
    for (let dot of Object.values(localStorage)) {
        dot = JSON.parse(dot);
        if (dot.r == r)
            drawDot(ctx, dot.x * (ctx.canvas.width / dot.canvas_size), dot.y * (ctx.canvas.width / dot.canvas_size), dot.hit == "Hit");
    }
}

function drawDot(ctx, x, y, isHit) {
    let circle = new Path2D();
    circle.arc(x, y, 8, 0, 2 * Math.PI);
    ctx.fillStyle = isHit ? "#f5f5f5" : "#fa2c2c";
    ctx.fill(circle);
}

function removeDots(ctx) {
    ctx.save();

    ctx.setTransform(1, 0, 0, 1, 0, 0);
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

    ctx.restore();
}

function transformCoords(x, y, half_canvas_size) {
    r = Number(getById('r').val());
    x = ((x - half_canvas_size) * ((r + (r * 0.705)) / half_canvas_size)).toFixed(5);
    y = (((-1 * (y - half_canvas_size))) * ((r + (r * 0.705)) / half_canvas_size)).toFixed(5);
    return { x: x, y: y, r: r };
}

function inverseTransformCoords(x, y, half_canvas_size, r) {
    var new_x = Math.round((x / ((r + (r * 0.705)) / half_canvas_size)) + half_canvas_size);
    var new_y = Math.round(((-1 * y) / ((r + (r * 0.705)) / half_canvas_size)) + half_canvas_size);
    return { x: new_x, y: new_y };
}


function validateX(x) {
    return !(x == undefined || isNaN(x) || x == "" || x > 5 || x < -5);
}

function validateY(y) {
    return !(y == undefined || isNaN(y) || y == "" || y > 5 || y < -3);
}

function storeValues(x, y, hit, r) {
    localStorage.setItem(localStorage.length, JSON.stringify({ x: x, y: y, canvas_size: ctx.canvas.width, hit: hit, r: r }));
}

$(document).ready(function () {
    redrawDots(ctx);
});

function getById(id) {
    return $(`#main-form\\:${id}`);
}

getById('reset-button').click(function () {
    getById('spinner_input').removeClass('glowing_bottons') 
    getById('y').removeClass('glowing_bottons')
    getById('spinner_input').val('');
    getById('y').val('');
    getById('r').val('1');
    localStorage.clear();
    removeDots(ctx);
});


getById('submit-button').click(function () {
    let x = getById('spinner_input').val()
        y = getById('y').val()
        r = getById('r').val()
    if (!validateX(x)) getById('spinner_input').addClass('glowing_bottons');
    if (!validateY(y)) getById('y').addClass('glowing_bottons');
});

getById('spinner_input').click(function () {
    getById('spinner_input').removeClass('glowing_bottons') 
});

getById('y').click(function () {
    getById('y').removeClass('glowing_bottons')
});

getById('r').on('change', function() {
    removeDots(ctx);
    redrawDots(ctx);
});

window.addEventListener('resize', () => {
    resizeCtxCanvas(ctx);
    redrawDots(ctx);
});


$("#graf").click((e) => {
    getById('spinner_input').removeClass('glowing_bottons') 
    getById('y').removeClass('glowing_bottons')

    var x = e.offsetX
        y = e.offsetY;

    //storeValues(x, y)

    //drawDot(ctx, x, y);

    const { x: x0, y: y0, r} = transformCoords(x, y, ctx.canvas.width / 2);

    document.getElementById("canvas_form:canvas_x").value = x0;
    document.getElementById("canvas_form:canvas_y").value = y0;
    document.getElementById("canvas_form:canvas_r").value = r;
    canvas_submit();
});


function eho(x, y, r, hit) {
    const { x: x0, y: y0 } = inverseTransformCoords(Number(x), Number(y), ctx.canvas.width / 2, Number(r));
    storeValues(x0, y0, hit, Number(r));
    drawDot(ctx, x0, y0, hit == "Hit");
}