package com.teragrep.rlo_14;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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

public class SDParamTest {
    @Test
    public void testSDParamEscape() {
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
                                "exampleSD@48577",
                                new SDParam("mySD", "value=\"\\[1]\\\"")
                        )
                );
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00Z localhost example - - [exampleSD@48577 mySD=\"value=\\\"\\\\[1\\]\\\\\\\"\"] test message";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testEmptySDParamName() {
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
                                            "exampleSD@48577",
                                            new SDParam("", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testNullSDParamName() {
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
                                            "exampleSD@48577",
                                            new SDParam(null, "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDParamNameTooLong() {
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
                                            "exampleSD@48577",
                                            new SDParam("ThisIsLongerThan32CharactersLongParam", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDSpace() {
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
                                            "exampleSD@48577",
                                            new SDParam("not good", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDEquals() {
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
                                            "exampleSD@48577",
                                            new SDParam("not=good", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDBracketClose() {
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
                                            "exampleSD@48577",
                                            new SDParam("not]good", "1")
                                    )
                            );
                }
        );
    }

    @Test
    public void testInvalidSDQuote() {
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
                                            "exampleSD@48577",
                                            new SDParam("not\"good", "1")
                                    )
                            );
                }
        );
    }
}