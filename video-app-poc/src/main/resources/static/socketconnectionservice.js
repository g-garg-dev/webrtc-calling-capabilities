var webSocket;
var conf={};
var from;
var to;


var url = window.location.href;
var arr = url.split("/");
conf.websocketUrl="wss://"+arr[2]+"/connect";
function registerSelf(){

  var userName= document.getElementById("selfName").value;

  webSocket = new WebSocket(conf.websocketUrl+"?userId="+userName);
  webSocket.onopen = function (event) {
      console.log("ws established");
  };

  webSocket.onmessage = function (event) {
      console.log(event.data);
      var receivedMessage = JSON.parse(event.data);
      var msgType = receivedMessage.msgType;
      var message = receivedMessage.message;

      if(msgType=="AVAILABILITY_RESPONSE" && message == "true"){
        document.getElementById("startChatting").disabled=false;
      }else if(msgType=="CREATE_RTP_CONNECTION"){
        handleRTPConnectionInitiateMessage(receivedMessage.from,message);
      }else if(msgType=="SDP_OFFER"){
        handleSDPOfferMessage(message);
      }else if(msgType=="SDP_ANSWER"){
        handleSDPAnswerMessage(message);
      }else if(msgType=="ICE_CANDIDATE"){
        handleIceCandidateMessage(message);
      }

  }

  webSocket.onclose = function(event) {
  console.log("WebSocket is closed now.");
  };

}

function checkAvailable(){
  var selfName= document.getElementById("selfName").value;
  var friendName= document.getElementById("friendName").value;
  var msg = createMessage(selfName,"server","CHECK_AVAILABILITY",friendName);
  webSocket.send(msg);
}

function sendCreateRTPConnectionInitiateMessage(){
  var selfName= document.getElementById("selfName").value;
  var msg = createMessage(selfName,to,"CREATE_RTP_CONNECTION","");
  webSocket.send(msg);
}

function handleRTPConnectionInitiateMessage(from,message){
    to = from;
    createRtpConnection();
}

function sendSDPOffer(sdpOffer){
  var selfName= document.getElementById("selfName").value;
  var msg = JSON.stringify({"sdpOffer":sdpOffer,"from":selfName});
  var sdpOfferMessage = createMessage(selfName,to,"SDP_OFFER",msg);
  webSocket.send(sdpOfferMessage);
}


function handleSDPOfferMessage(message){
    var msg= JSON.parse(message);
    var to=msg.from;
    handleRemoteSdpOffer(to,msg.sdpOffer);
}

function sendSDAnswer(to,sdpAnswer){
  var selfName= document.getElementById("selfName").value;
  var msg = JSON.stringify({"sdpAnswer":sdpAnswer,"from":selfName});
  var msg = createMessage(selfName,to,"SDP_ANSWER",msg);
  webSocket.send(msg);
}

function handleSDPAnswerMessage(message){
    var msg= JSON.parse(message);
    var to=msg.from;
    handleRemoteSdpAnswer(msg.sdpAnswer);
}


function sendIceCandidate(iceCandidate){
  var selfName= document.getElementById("selfName").value;
  var msg = createMessage(selfName,to,"ICE_CANDIDATE",iceCandidate);
  webSocket.send(msg);
}

function handleIceCandidateMessage(message){
    handleRemoteIceCandidate(message);
}

function createMessage( from ,  to,  msgType, message){
    var obj={};
    obj.from=from;
    obj.to=to;
    obj.msgType=msgType;
    obj.message=message;
    return JSON.stringify(obj);
}
