def call(project, flows, args = []) {
    withEnv([
            "DOCKER_TLS_VERIFY=1",
            "DOCKER_HOST=tcp://192.168.99.101:3376",
            "DOCKER_CERT_PATH=/Users/vfarcic/.docker/machine/machines/swarm-master",
            "FLOW_PROXY_HOST=192.168.99.101",
            "FLOW_CONSUL_ADDRESS=http://192.168.99.100:8500",
            "FLOW_PROXY_DOCKER_HOST=tcp://192.168.99.101:2376",
            "FLOW_PROXY_DOCKER_HOST=tcp://192.168.99.101:2376",
            "FLOW_PROXY_DOCKER_CERT_PATH=/Users/vfarcic/.docker/machine/machines/swarm-master",
    ]) {
        def dfArgs = "-p " + project + " --flow=" + flows.join(" --flow=") + " " + args.join(" ")
        sh "docker-flow ${dfArgs}"
        sh "docker ps -a"
    }
}
