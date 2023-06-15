/*
   Java RFC5424 Syslog Formatter RLO_14
   Copyright (C) 2023  Suomen Kanuuna Oy

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.teragrep.rlo_14;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Warmup;

import java.time.Instant;

public class BenchmarkTest {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    @Fork(value=1, warmups=1)
    @Warmup(iterations=1, time=5)
    @Measurement(iterations=5, time=5)
    public void testBenchmarkStringTimestamp() {
        String message = new SyslogMessage()
            .withTimestamp("2023-06-14T16:37:00.000Z")
            .withAppName("my_app")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("a syslog message")
            .toRfc5424SyslogMessage();
    }

    @Benchmark
    @Fork(value=1, warmups=1)
    @Warmup(iterations=1, time=5)
    @Measurement(iterations=5, time=5)
    public void testBenchmarkStringTimestampSkipParse() {
        String message = new SyslogMessage()
            .withTimestamp("2023-06-14T16:37:00.000Z", true)
            .withAppName("my_app")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("a syslog message")
            .toRfc5424SyslogMessage();
    }

    @Benchmark
    @Fork(value=1, warmups=1)
    @Warmup(iterations=1, time=5)
    @Measurement(iterations=5, time=5)
    public void testBenchmarkLongTimestamp() {
        long time = Instant.now().toEpochMilli();
        String message = new SyslogMessage()
            .withTimestamp(time)
            .withAppName("my_app")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("a syslog message")
            .toRfc5424SyslogMessage();
    }

    @Benchmark
    @Fork(value=1, warmups=1)
    @Warmup(iterations=1, time=5)
    @Measurement(iterations=5, time=5)
    public void testBenchmarkInstantTimestamp() {
        Instant time = Instant.now();
        String message = new SyslogMessage()
            .withTimestamp(time)
            .withAppName("my_app")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("a syslog message")
            .toRfc5424SyslogMessage();
    }
}
