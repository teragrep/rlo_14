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
public class SDElementTest {
    @Test
    public void testReservedSDElement() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "notReserved",
                                            new SDParam("mySD", "value=\"1\"")
                                    )
                            );
                }
        );
    }

    @Test
    public void testEmptySDElement() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "",
                                            new SDParam("mySD", "value=\"1\"")
                                    )
                            );
                }
        );
    }

    @Test
    public void testNullSDElement() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            null,
                                            new SDParam("mySD", "value=\"1\"")
                                    )
                            );
                }
        );
    }

    @Test
    public void testEmptySDElementName() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "",
                                            new SDParam("test", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDElementNameTooLong() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "ThisIsLongerThan32CharactersLongSD",
                                            new SDParam("test", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDElementSpace() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "This is invalid",
                                            new SDParam("test", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDElementEquals() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "this=invalid",
                                            new SDParam("test", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDElementBracketClose() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "this]notgood",
                                            new SDParam("test", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDElementQuote() {
        Instant time = Instant.parse("2023-06-14T16:37:00.000Z");
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    SyslogMessage message = new SyslogMessage()
                            .withTimestamp(time.toEpochMilli())
                            .withAppName("example")
                            .withHostname("localhost")
                            .withFacility(Facility.USER)
                            .withSeverity(Severity.INFORMATIONAL)
                            .withMsg("test message")
                            .withSDElement(
                                    new SDElement(
                                            "this\"notgood",
                                            new SDParam("test", "1")
                                    )
                            );
                }
        );
    }

}