var http = require('http');
var https = require('https');
var fs = require('fs');
var decompress = require('decompress');
var decompressTarbz = require('decompress-tarbz2');

function getFile(url, path, cb) {
    var http_or_https = http;
    if (/^https:\/\//.test(url)) {
        http_or_https = https;
    }
    http_or_https.get(url, function(response) {
        var headers = JSON.stringify(response.headers);
        switch(response.statusCode) {
            case 200:
                var file = fs.createWriteStream(path);
                response.on('data', function(chunk){
                    file.write(chunk);
                }).on('end', function(){
                    file.end();
                    cb(null);
                });
                break;
            case 301:
            case 302:
            case 303:
            case 307:
                getFile('https://media.twiliocdn.com' + response.headers.location, path, cb);
                break;
            default:
                cb(new Error('Server responded with status code ' + response.statusCode));
        }

    })
    .on('error', function(err) {
        cb(err);
    });
}

getFile('https://media.twiliocdn.com/sdk/ios/video/releases/1.3.9/twilio-video-ios-1.3.9.tar.bz2', 'twilio-video-ios.tar.bz2', function(err) {
  if (err === null) {
    decompress('twilio-video-ios.tar.bz2', 'src/ios/frameworks', {
        plugins: [
            decompressTarbz()
        ]
    }).then(() => {
        fs.unlink('twilio-video-ios.tar.bz2');
    });
  }
});
