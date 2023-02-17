$(document).ready(function () {
    function updateClock() {
        var now = new Date();
        document.getElementById('time').innerHTML = [now.getHours(), now.getMinutes(), now.getSeconds()].map(x => prependZero(x)).join(':')
        setTimeout(updateClock, 1000);
    }
    function prependZero(time) { return time < 10 ? "0" + time : time; }
    updateClock();
});