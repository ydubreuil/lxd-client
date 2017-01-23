# LXD client for Java

This is a REST API client for [LXD REST API](https://linuxcontainers.org/lxd/rest-api/) written in Java using [OKHttp](http://square.github.io/okhttp/), [Jackson](https://github.com/FasterXML/jackson) and [RxJava 2](https://github.com/ReactiveX/RxJava/tree/2.x).

## Using the API

Start by creating an LxdClient instance

    try (LxdClient client = new LxdClient(Config.localAccessConfig())) {
       // code using the client
    }

This will default to use LXD available locally. The client creates its own OkHttpClient which is closed by `LxdClient.close()`.

Then, you can make requests to LXD. All requests are asynchronous and can be combined per Rx usage:

    LxdClient.ContainerClient containerClient = client.container("demo-container");

    containerClient.init("ubuntu", "16.04", null, null, null, true)
        .doOnComplete(() -> System.out.println("Container created"))
        .andThen(container.start().doOnComplete(() -> System.out.println("Container started")))
        .blockingAwait();

You can find usage sample in tests.

## Status

The API is in early beta, so breakage will happen for time to time. What's implemented by now:

* connecting through HTTPS and Unix Socket
* some operations to manage images
* some operations to manage containers

## Developing

Client code is based on LXD documentation, see:

* https://github.com/lxc/lxd/blob/master/doc/rest-api.md
* https://www.stgraber.org/2016/04/18/lxd-api-direct-interaction/

Starting to code with Rx can be difficult, so here is some good starting points:

* https://yarikx.github.io/NotRxJava/

### Vagrant box with LXD

The root of this repos contains a Vagrantfile to start a VM with LXD. To launch it, you need Vagrant set up.

Then, just run:

    vagrant init

This will launch a Ubuntu VM with LXD on it.

Connecting to LXD deamon running in the Vagrant box can be done either by

* activating and forwarding HTTPS on LXD
* forwarding the LXD unix socket

#### Using HTTPS

HTTPS is forwarded to port 8443 on the host in Vagrant configuation (see Vagrantfile). LXD uses mutual certificate
authentication to authenticate clients on HTTPS. Certificates are copied to `vagrant` folder upon VM provisioning
(see `Vagrantfile` for details).

Integration tests are configured to pick up those certificate automatically.

#### Forwarding Unix Socket

To integrate the VM with your local development environment, you can run:

    vagrant ssh -- -L${PWD}/lxd.socket:/var/lib/lxd/unix.socket -D 1080

to forward the LXD socket to a local file and publish an SOCK5 proxy with will let you jump into the box easily

Keep command running to access the LXD control socket from your computer. If it fails, remove the stale socket file:

    rm lxd.socket

## Credits

I wouldn't have been able to write this client without borrowing ideas and code from

* https://github.com/fabric8io/docker-client
* https://github.com/shekhargulati/rx-docker-client

Thanks to them!

## Debugging tips

### Accessing a remote LXD thought the Unix socket

Since OpenSSH 6.7, it's possible to forward [Unix sockets over SSH](https://lwn.net/Articles/609321/), so it's quite easy
to access LXD without having to bother with SSL certificates, networks and the likes...

    ssh  -L$HOME/tmp/lxd.socket:/var/lib/lxd/unix.socket ubuntu@server

### Using cURL with Unix socket

see https://www.stgraber.org/2016/04/18/lxd-api-direct-interaction/

    curl -s --unix-socket /var/lib/lxd/unix.socket a/1.0

### Capturing from the LXD Unix socket

There's no direct way to capture from a unix socket, like you could do on a network device. If you want to capture the
exchanges from `lxc` to `lxd` server, here's a trick: create a socat sandwitch! With `socat`, you can forward the
unix socket to a TCP port and then forward the TCP port to the original LXD socket. That way, you can snoop on localhost
network interface to capture the TCP traffic.

    # fake TCP server connects to real Unix socket
    socat TCP-LISTEN:6000,reuseaddr,fork,bind=localhost UNIX-CONNECT:/var/lib/lxd/unix.socket.orig
    # clients connect to this Unix socket
    sudo socat UNIX-LISTEN:/var/lib/lxd/unix.socket,mode=777,reuseaddr,fork TCP-CONNECT:127.0.0.1:6000

http://unix.stackexchange.com/questions/219853/how-to-passively-capture-from-unix-domain-sockets-af-unix-socket-monitoring

You can then capture the TCP stream and write them to a pcap file

    # start packet capture on said port
    tcpdump -i lo -w capture.pcap  port 6000 and host localhost

### Connect to a container using SSH

To connect to a container inside the Vagrant box from the host, you can leverage the SOCKS5 proxy to jump into the box, like this:

    ssh -o ProxyCommand='nc -x localhost:1080 %h %p' ubuntu@<IP_ADDRESS_OF_CONTAINER>
