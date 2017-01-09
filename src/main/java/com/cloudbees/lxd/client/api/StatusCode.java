/*
 * The MIT License
 *
 * Copyright (c) 2017 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.cloudbees.lxd.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum StatusCode {
    OperationCreated(100),
    Started(101),
    Stopped(102),
    Running(103),
    Cancelling(104),
    Pending(105),
    Starting(106),
    Stopping(107),
    Aborting(108),
    Freezing(109),
    Frozen(110),
    Thawed(111),
    Error(112),
    Success(200),
    Failure(400),
    Cancelled(401);

    private final int value;
    StatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static Map<Integer, StatusCode> FORMAT_MAP = Stream.of(StatusCode.values()).collect(
        Collectors.toMap(s -> s.value, Function.identity()));

    @JsonCreator
    public static StatusCode fromCode(int code) {
        StatusCode status = FORMAT_MAP.get(code);
        if (status == null) {
            throw new IllegalArgumentException(code + " has no corresponding value");
        }
        return status;
    }
}
