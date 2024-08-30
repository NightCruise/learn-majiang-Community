function post(){
    let questionId = $("#question_id").val()
    let content = $("#comment_content").val()
    if (!content){
        alert("不能回复空内容~~~")
        return;
    }
    $.ajax({
        url: "/comment",
        data: JSON.stringify({
                "parentId": questionId,
                "content": content,
                "type": 1
            })
        ,
        type: "POST",
        dataType : "json",
        contentType: "application/json",
    })
        .done(function( response ) {
            if (response.code === 200){
                window.location.reload();
            }else {
                if (response.code === 2003){
                    let isAccepted = confirm(response.message)
                    if (isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=Ov23liijchVoPwBKVJUY&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable", true)
                    }
                }else {
                    alert(response.message)
                }
            }
        })
        .fail(function( xhr, status, errorThrown ) {
            alert( "Sorry, there was a problem!" );
            console.log( "Error: " + errorThrown );
            console.log( "Status: " + status );
            console.dir( xhr );
        })
}