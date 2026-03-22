docker pull aerokube/selenoid:latest-release
docker pull selenoid/video-recorder:latest-release
docker pull selenoid/vnc:chrome_120.0
cat > browsers.json << 'JSON'
{
  "chrome": {
    "default": "120.0",
    "versions": {
      "120.0": {
        "image": "selenoid/vnc:chrome_120.0",
        "port": "4444",
        "path": "/",
        "tmpfs": {"/tmp":"size=128m"}
      }
    }
  }
}
JSON
docker run -d --name selenoid -p 4444:4444 \
  -e DOCKER_API_VERSION=1.44 \
  -v //var/run/docker.sock:/var/run/docker.sock \
  -v //$PWD/browsers.json:/etc/selenoid/browsers.json \
  -v //$PWD/video:/opt/selenoid/video \
  -e OVERRIDE_VIDEO_OUTPUT_DIR=//$PWD/video \
  aerokube/selenoid:latest-release -limit 4 -timeout 3m -capture-driver-logs