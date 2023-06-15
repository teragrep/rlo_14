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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class FormatTest {
    @Test
    public void testMessageWithLongDate() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        SyslogMessage message = new SyslogMessage()
            .withTimestamp(time.toEpochMilli())
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMessageWithLongDateFrac() {
        Instant time = Instant.parse("2023-06-14T16:37:00.123Z");
        SyslogMessage message = new SyslogMessage()
            .withTimestamp(time.toEpochMilli())
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00.123Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMessageWithStringDate() {
        SyslogMessage message = new SyslogMessage()
            .withTimestamp("2023-06-14T16:37:00.000Z")
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMessageWithStringDateFrac() {
        SyslogMessage message = new SyslogMessage()
            .withTimestamp("2023-06-14T16:37:00.123Z")
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00.123Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMessageWithStringDateSkipFormat() {
        SyslogMessage message = new SyslogMessage()
            .withTimestamp("2023-06-14T16:37:00.000Z", true)
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00.000Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMessageWithInstantDate() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        SyslogMessage message = new SyslogMessage()
            .withTimestamp(time)
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testMessageWithInstantDateFrac() {
        Instant time = Instant.parse("2023-06-14T16:37:00.123Z");
        SyslogMessage message = new SyslogMessage()
            .withTimestamp(time)
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00.123Z localhost example - - - test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testStructuredData() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        SyslogMessage message = new SyslogMessage()
            .withTimestamp(time.toEpochMilli())
            .withAppName("example")
            .withHostname("localhost")
            .withFacility(Facility.USER)
            .withSeverity(Severity.INFORMATIONAL)
            .withMsg("test message")
            .withSDElement(
                new SDElement(
                    "event_version@48577",
                    new SDParam("major", "1"),
                    new SDParam("minor", "0"),
                    new SDParam("version_source", "source")
                )
            );
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00Z localhost example - - [event_version@48577 major=\"1\" minor=\"0\" version_source=\"source\"] test message";
        Assertions.assertEquals(expected, actual);
        message.withSDElement(
            new SDElement(
                "session@48577",
                new SDParam("state", "new"),
                new SDParam("counter", "1")
            )
        );
        actual = message.toRfc5424SyslogMessage();
        expected = "<14>1 2023-06-14T16:37:00Z localhost example - - [event_version@48577 major=\"1\" minor=\"0\" version_source=\"source\"][session@48577 state=\"new\" counter=\"1\"] test message";
        Assertions.assertEquals(expected, actual);
    }
}
