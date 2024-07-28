document.getElementById("on").addEventListener("click", turnon);
document.getElementById("off").addEventListener("click", turnoff);


function turnon()
{
  var req = new Request("/NewIDSInterface/change", {
    headers: {
        "sender" : "naveen",
        "action" : "on"
    }
  })
  fetch(req).then(response => 
    response.json().then(data => {
        console.log(data);
        if(data.response === "on"){
            console.log(data.response)
        }
        else{
            alert("username or password is incorrect in on")
        } 
    }))   ;
}


function turnoff()
{
  var req = new Request("/NewIDSInterface/change", {
    headers: {
        "sender" : "index",
        "action" : "off"
    }
  })
  fetch(req).then(response => 
    response.json().then(data => {
        console.log(data);
        if(data.response === "off"){
            console.log(data.response)
        }
        else{
            alert("username or password is incorrect in off")
        } 
    }));
}
