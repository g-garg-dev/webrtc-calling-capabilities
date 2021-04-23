var streamGobal;

var remoteInboundStream;

function startVideoChat(){
  to= document.getElementById("friendName").value;
  getStreams(handleStream);
}

function getStreams(handleStream){
    navigator.mediaDevices.getUserMedia({audio:true,video:true}).then(function (stream) {
      handleStream(stream);
    },
      function (err) { // errorCallback
        console.log('getUserMedia for screen failed!: thawing generic error' + err);
      }
    );
}
function handleStream(stream){
    streamGobal=stream;
    var video = document.getElementById('selfVideo');
    video.srcObject = stream;
    createRtpConnection();
    sendCreateRTPConnectionInitiateMessage();
    createSDPOffer();
    sendAudioVideoTrack();
}


