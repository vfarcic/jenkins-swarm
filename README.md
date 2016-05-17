Setup Jenkins
-------------

```bash
export HOST_IP=192.168.1.37

docker-compose up -d jenkins consul-server

# Get password from the logs
docker-compose logs jenkins

open http://localhost:8080

mkdir -p /tmp/jenkins-slave

chmod 777 /tmp/jenkins-slave

# Connect the slave from Jenkins UI; use cd as label

mkdir -p data/jenkins/workflow-libs/vars

# Change IPs in scripts/dockerFlow.groovy; TODO: Automate

cp scripts/dockerFlow.groovy data/jenkins/workflow-libs/vars/dockerFlow.groovy

docker-compose restart jenkins
```

Setup Docker Flow
-----------------

```bash
sudo wget https://github.com/vfarcic/docker-flow/releases/download/v1.0.1-beta/docker-flow_darwin_amd64 \
    -O /usr/local/bin/docker-flow

sudo chmod +x /usr/local/bin/docker-flow
```

Setup Swarm
-----------

```bash
scripts/setup-swarm.sh

eval "$(docker-machine env --swarm swarm-master)"

docker info
```

Running Pipeline Job
--------------------

```bash
# Create a new Pipeline job
```

```groovy
def serviceName = "go-demo"

node("cd") {
    stage "Checkout"
    git "https://github.com/vfarcic/go-demo.git"

    stage "Pre-Deployment Tests"
    sh "docker-compose -f docker-compose-test.yml run --rm unit"

    stage "Build"
    sh "docker build -t vfarcic/${serviceName} ."
    // sh "docker push vfarcic/${serviceName}"

    stage "Deploy"
    dockerFlow(serviceName, ["deploy", "proxy", "stop-old"])
}
```

```bash
IP=$(docker-machine ip swarm-master)

curl $IP/demo/hello
```
