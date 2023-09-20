Stream = require('node-rtsp-stream')

var rtspList = [
        {"url":'rtsp://root:kiot!@34@14.45.111.107:10554/axis-media/media.amp', "port":9999, "stream":null},
	{"url":'rtsp://root:kiot!@34@211.229.79.224:10554/axis-media/media.amp',"port":9998, "stream":null},
	{"url":'rtsp://root:kiot!@34@14.45.111.107:10554/axis-media/media.amp',"port":9997, "stream":null},
	{"url":'rtsp://root:kiot!@34@14.45.111.107:10554/axis-media/media.amp', "port":9996, "stream":null},
//	{"url":'rtsp://root:kiot!@34@14.45.111.107:10554/axis-media/media.amp', "port":9995, "stream":null},
//	{"url":'rtsp://root:kiot!@34@14.45.111.107:10554/axis-media/media.amp', "port":9994, "stream":null}
];

var rtspListLength = rtspList.length;
for(var i=0; i<rtspListLength; i++){
        openStream(rtspList[i]);

}

function openStream(obj){
        var stream = new Stream({
                name: 'name',
                streamUrl : obj.url,
                wsPort: obj.port,
                ffmpegOptions: { // options ffmpeg flags
                        '-stats': '', // an option with no neccessary value uses a blank string
                        '-r': 30, // options with required values specify the value after the key
                }
        });
        stream.mpeg1Muxer.on('exitWithError',()=>{
                stream.stop();
                openStream(obj);
        });

        stream.mpeg1Muxer.on('ffmpegStderr', (data)=>{
                data = data.toString();
                if(data.includes('muxing overhead')){
                        stream.stop();
                        openStream(obj);
                }
        });
}
