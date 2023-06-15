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

public class MissingFieldsTest {
    @Test
    public void missingFacilityTest() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    String message = new SyslogMessage().withSeverity(Severity.INFORMATIONAL).toRfc5424SyslogMessage();
                }
        );
    }

    @Test
    public void missingSeverityTest() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    String message = new SyslogMessage().withFacility(Facility.USER).toRfc5424SyslogMessage();
                }
        );
    }

    @Test
    public void missingRestFieldsTest() {
        SyslogMessage message = new SyslogMessage()
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL);
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 - - - - - -";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void missingRestFieldsTestWithMsg() {
        SyslogMessage message = new SyslogMessage()
                .withFacility(Facility.USER)
                .withSeverity(Severity.INFORMATIONAL)
                .withMsg("Test message");
        String actual = message.toRfc5424SyslogMessage();
        String expected = "<14>1 - - - - - - Test message";
        Assertions.assertEquals(expected, actual);
    }
}
