/**
 * Copyright (C) 2011 Red Hat, Inc.
 * Copyright (C) 2016 CloudBees, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"log"
	"reflect"
	"strings"
	"time"

	"github.com/cloudbees/lxd-client/generator/pkg/schemagen"
	api "github.com/lxc/lxd/shared"
)

type Schema struct {
    CertInfo                      api.CertInfo
	IdmapEntry                    api.IdmapEntry
    Device                        api.Device
    Devices                       api.Devices
	ContainerState                api.ContainerState
	ContainerStateDisk            api.ContainerStateDisk
	ContainerStateCPU             api.ContainerStateCPU
	ContainerStateMemory          api.ContainerStateMemory
	ContainerStateNetwork         api.ContainerStateNetwork
	ContainerStateNetworkAddress  api.ContainerStateNetworkAddress
	ContainerStateNetworkCounters api.ContainerStateNetworkCounters
	ContainerExecControl          api.ContainerExecControl
	SnapshotInfo                  api.SnapshotInfo
	ContainerInfo                 api.ContainerInfo
    BriefContainerInfo            api.BriefContainerInfo
    ProfileConfig                 api.ProfileConfig
    NetworkConfig                 api.NetworkConfig
    ImageAliasesEntry             api.ImageAliasesEntry
    ImageSource                   api.ImageSource
    ImageInfo                     api.ImageInfo
	BriefImageInfo                api.BriefImageInfo
	ServerStateEnvironment        api.ServerStateEnvironment
	ServerState                   api.ServerState
	BriefServerState              api.BriefServerState
    SimpleStreamsManifest         api.SimpleStreamsManifest
    SimpleStreamsManifestProduct  api.SimpleStreamsManifestProduct
    SimpleStreamsManifestProductVersion api.SimpleStreamsManifestProductVersion
    SimpleStreamsManifestProductVersionItem api.SimpleStreamsManifestProductVersionItem
    SimpleStreamsIndex            api.SimpleStreamsIndex
    SimpleStreamsIndexStream      api.SimpleStreamsIndexStream
}

func main() {
	packages := []schemagen.PackageDescriptor{
		{"github.com/cloudbees/lxd-client/generator/vendor/github.com/lxc/lxd/shared", "com.cloudbees.lxd.client.api", "lxd_"},
	}

	typeMap := map[reflect.Type]reflect.Type{
		reflect.TypeOf(time.Time{}): reflect.TypeOf(""),
		reflect.TypeOf(struct{}{}):  reflect.TypeOf(""),
	}
	schema, err := schemagen.GenerateSchema(reflect.TypeOf(Schema{}), packages, typeMap)
	if err != nil {
		fmt.Errorf("An error occurred: %v", err)
		return
	}

	b, err := json.Marshal(&schema)
	if err != nil {
		log.Fatal(err)
	}
	result := string(b)
	result = strings.Replace(result, "\"additionalProperty\":", "\"additionalProperties\":", -1)
	var out bytes.Buffer
	err = json.Indent(&out, []byte(result), "", "  ")
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println(out.String())
}
