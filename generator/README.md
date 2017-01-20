# LXD schema generator

## Developing

Managing GOPATH is not really simple and easy (at least that's my opinion). You always need to refer to a global
workspace and this requires some setup which is not great for one time contribution.

I tried to avoid that burden here, so to build and develop


### In IDEA

Golang Plugin provides good integration, compilation and debugging are easy with it.

Start by creating a GOPATH local to the project:

    make gopath

Within project settings, add a module for path `generator/gopath/src/github.com/cloudbees/lxd-client/generator` (the
project MUST be inside GOPATH, otherwise compilation fails)

## Credits

* Build template comes from https://github.com/thockin/go-build-template
* Schema generator code borrowed and adapted from https://github.com/fabric8io/kubernetes-model


## Building

Run `make` in this folder to compile the generator.  This will use a Docker image
to build, with the current directory volume-mounted into place.

Run `make clean` to clean up.


## Shell configuration

In order to use standard Go tooling from the command line, you need to create a GOPATH with

    make gopath

and then set `GOPATH` variable with

    export GOPATH="$(pwd)/gopath"
    export PATH="$PATH:$(pwd)/gopath/bin"

You can then go inside the `GOPATH` to work with the sources.

### Updating the LXD shared sources

govendor tool is used to manage vendored dependencies. It is not available by default in the Go toolbox. To install it, run:

    go get -u github.com/kardianos/govendor

Then, to update the LXD dependencies, under `gopath/src/github.com/cloudbees/lxd-client/generator`, run

    govendor fetch github.com/lxc/lxd/shared
    govendor fetch github.com/lxc/lxd/shared/api
