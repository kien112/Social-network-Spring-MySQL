function formatDatePost (){
    console.log("aa");
    let createAtSpans = document.getElementsByClassName("createAtSpan");
    Array.from(createAtSpans).forEach(function (span) {
        let createAtValue = span.getAttribute("data-createAt");
        let createAt = new Date(createAtValue);
        span.textContent = formatTime(createAt);
        console.log('ss:', span);
    });
}

formatDatePost();
