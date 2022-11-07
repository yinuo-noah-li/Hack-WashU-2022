document.getElementById("roundUp").addEventListener("click", roundUp);
document.getElementById("roundUp").addEventListener("click", callBack);
function callBack(){
  var xMLHttp = new XMLHttpRequest();
  xMLHttp.open("GET", "http://localhost:8080/func1?param1=200&param2=USD", true);
  xMLHttp.addEventListener = ("load", ajaxCallBack, false);
  xMLHttp.send(null);
}

function ajaxCallBack(event){
  result = event.target.responseText;
  tree = result.trees
  carbon = result.carbonSequestered
}

function roundUp(){
  price = document.getElementById("price").innerHTML
  impact = document.getElementById("impactCents").innerHTML
  holder = ""
  holderTwo = ""
  for (let i = 1; i<price.length; i++){
    holder = holder + price.charAt(i);
  }
  for (let i = 6; i<impact.length-16; i++){
    holderTwo = holderTwo + impact.charAt(i);
  }
  oldTotal = parseFloat(holder)
  newTotal = Math.floor(parseFloat(holder)) + 1
  difference = Math.round((newTotal - oldTotal)*100) / 100
  oldDifference = parseFloat(holderTwo)
  oldDifference = oldDifference + difference
  oldDifference = Math.round((oldDifference)*100) / 100
  carbon = Math.round((50 * oldDifference)*100) / 100
  tree = Math.round((0.5*oldDifference)*100) / 100
  document.getElementById("price").innerHTML = "$" + newTotal + ".00"
  document.getElementById("impactCents").innerHTML = "Your $" + oldDifference + " Worth of Impact:"
  document.getElementById("carbon").innerHTML = "Carbon sequestered: " + carbon + " g"
  document.getElementById("trees").innerHTML = "Trees saved: " + tree + " trees"
}