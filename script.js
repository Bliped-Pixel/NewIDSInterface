

document.getElementById("login").addEventListener("click", submitted);

function submitted()
{
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    sendRequest(username, password);
}

function sendRequest(username, password)
{
    var req = new Request("/NewIDSInterface/login", {
        headers: {
            "sender" : "index",
            "action" : "login",
            "username" : username,
            "password" : password
        }
    })
    fetch(req).then(response => 
        response.json().then(data => {
            if(data.response === "true"){
                window.location.href = 'configure.html'
            }
            else{
                alert("username or password is incorrect")
            } 
        }))    
}

