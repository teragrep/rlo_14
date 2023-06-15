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

import java.util.Calendar;
import java.util.TimeZone;

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
    public void testBenchmarkDateTimestamp() {
        long start = System.nanoTime();
        int events = 10_000_000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(2013, Calendar.DECEMBER, 5, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);
        for(int i=0; i<events; i++) {
            SyslogMessage message = new SyslogMessage()
                    .withTimestamp(cal.getTime())
                    .withAppName("my_app")
                    .withHostname("localhost")
                    .withFacility(Facility.USER)
                    .withSeverity(Severity.INFORMATIONAL)
                    .withMsg("a syslog message");

        };
        long end = System.nanoTime();
        long elapsed = end - start;
        printData((double) elapsed / 1_000_000_000, events, "Date Timestamp");
    }

    @Test
    public void testBenchmarkLongTimestamp() {
        long start = System.nanoTime();
        int events = 10_000_000;
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(2013, Calendar.DECEMBER, 5, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);
        for(int i=0; i<events; i++) {
            SyslogMessage message = new SyslogMessage()
                    .withTimestamp(cal.getTimeInMillis())
                    .withAppName("my_app")
                    .withHostname("localhost")
                    .withFacility(Facility.USER)
                    .withSeverity(Severity.INFORMATIONAL)
                    .withMsg("a syslog message");

        };
        long end = System.nanoTime();
        long elapsed = end - start;
        printData((double) elapsed / 1_000_000_000, events, "Long Timestamp");
    }

    private void printData(double elapsed, int events, String type) {
        System.out.println("[" + type + "] Took " + elapsed + " seconds to process " + events + " (" + (events/elapsed) + " eps)" );
    }
}
