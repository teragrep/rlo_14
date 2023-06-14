/*
 * Copyright 2010-2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teragrep.rlo_14;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Calendar;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogMessageTest {
    @Test
    public void testMessageWithLongDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.DECEMBER, 5, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);
        SyslogMessage message = new SyslogMessage()
                .withTimestamp(cal.getTimeInMillis())
                .withAppName("my_app")
                .withHostname("localhost")
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withMsg("a syslog message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2013-12-05T10:30:05.000Z localhost my_app - - - a syslog message";
        Assertions.assertEquals(actual, expected);

    }

    @Test
    public void testMessageWithStringDate() {
        SyslogMessage message = new SyslogMessage()
                .withTimestamp("2023-06-14T16:37:00.000Z")
                .withAppName("my_app")
                .withHostname("localhost")
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withMsg("a syslog message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00.000Z localhost my_app - - - a syslog message";
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testMessageWithDateDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2023, Calendar.JUNE, 14, 16, 37, 00);
        cal.set(Calendar.MILLISECOND, 123);
        SyslogMessage message = new SyslogMessage()
                .withTimestamp(cal.getTime())
                .withAppName("my_app")
                .withHostname("localhost")
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withMsg("a syslog message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2023-06-14T16:37:00.123Z localhost my_app - - - a syslog message";
        Assertions.assertEquals(actual, expected);
    }

    @Test
    public void testStructuredData() {
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.DECEMBER, 5, 10, 30, 5);
        cal.set(Calendar.MILLISECOND, 0);
        SyslogMessage message = new SyslogMessage()
                .withTimestamp(cal.getTimeInMillis())
                .withAppName("my_app")
                .withHostname("localhost")
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withMsg("a syslog message")
                .withSDElement(new SDElement("exampleSDID@32473", new SDParam("iut", "3"), new SDParam("eventSource", "Application"), new SDParam("eventID", "1011")));
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 2013-12-05T10:30:05.000Z localhost my_app - - [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"] a syslog message";
        Assertions.assertEquals(actual, expected);

        message.withSDElement(new SDElement("examplePriority@32473", new SDParam("class", "high")));
        actual = message.toRfc5424SyslogMessage();
        expected = "<14>1 2013-12-05T10:30:05.000Z localhost my_app - - [exampleSDID@32473 iut=\"3\" eventSource=\"Application\" eventID=\"1011\"][examplePriority@32473 class=\"high\"] a syslog message";
        Assertions.assertEquals(actual, expected);
    }
}
