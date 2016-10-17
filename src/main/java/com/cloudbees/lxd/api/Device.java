package com.cloudbees.lxd.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device extends HashMap<String, String> {
}
