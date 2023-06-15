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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.time.Instant;

@EnabledIfSystemProperty(named="benchmark", matches="true")
public class BenchmarkTest {
    @Test
    public void testBenchmarkStringTimestamp() {
        long start = System.nanoTime();
        int events = 10_000_000;
        for(int i=0; i<events; i++) {
            SyslogMessage message = new SyslogMessage()
                    .withTimestamp("2023-06-14T16:37:00.000Z")
                    .withAppName("my_app")
                    .withHostname("localhost")
                    .withFacility(Facility.USER)
                    .withSeverity(Severity.INFORMATIONAL)
                    .withMsg("a syslog message");
            String actual = message.toRfc5424SyslogMessage();
        }
        long end = System.nanoTime();
        long elapsed = end - start;
        printData((double) elapsed / 1_000_000_000, events, "String Timestamp");
    }

    @Test
    public void testBenchmarkStringTimestampSkipParse() {
        long start = System.nanoTime();
        int events = 10_000_000;
        for(int i=0; i<events; i++) {
            SyslogMessage message = new SyslogMessage()
                    .withTimestamp("2023-06-14T16:37:00.000Z", true)
                    .withAppName("my_app")
                    .withHostname("localhost")
                    .withFacility(Facility.USER)
                    .withSeverity(Severity.INFORMATIONAL)
                    .withMsg("a syslog message");
            String actual = message.toRfc5424SyslogMessage();
        }
        long end = System.nanoTime();
        long elapsed = end - start;
        printData((double) elapsed / 1_000_000_000, events, "String skipParse Timestamp");
    }

    @Test
    public void testBenchmarkLongTimestamp() {
        long start = System.nanoTime();
        int events = 10_000_000;
        long time = Instant.now().toEpochMilli();
        for(int i=0; i<events; i++) {
            SyslogMessage message = new SyslogMessage()
                    .withTimestamp(time)
                    .withAppName("my_app")
                    .withHostname("localhost")
                    .withFacility(Facility.USER)
                    .withSeverity(Severity.INFORMATIONAL)
                    .withMsg("a syslog message");
            String actual = message.toRfc5424SyslogMessage();
        };
        long end = System.nanoTime();
        long elapsed = end - start;
        printData((double) elapsed / 1_000_000_000, events, "Long Timestamp");
    }

    @Test
    public void testBenchmarkInstantTimestamp() {
        long start = System.nanoTime();
        int events = 10_000_000;
        Instant time = Instant.now();
        for(int i=0; i<events; i++) {
            SyslogMessage message = new SyslogMessage()
                    .withTimestamp(time)
                    .withAppName("my_app")
                    .withHostname("localhost")
                    .withFacility(Facility.USER)
                    .withSeverity(Severity.INFORMATIONAL)
                    .withMsg("a syslog message");
            String actual = message.toRfc5424SyslogMessage();
        };
        long end = System.nanoTime();
        long elapsed = end - start;
        printData((double) elapsed / 1_000_000_000, events, "Instant Timestamp");
    }

    private void printData(double elapsed, int events, String type) {
        System.out.println("[" + type + "] Took " + elapsed + " seconds to process " + events + " (" + (events/elapsed) + " eps)" );
    }
}
