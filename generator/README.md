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


## Updating

LXD sources are vendored, meaning that updating the schema requires running `go vendor fetch` again.
