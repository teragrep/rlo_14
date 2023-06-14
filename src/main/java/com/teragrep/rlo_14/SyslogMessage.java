/*
 * Copyright 2010-2014, CloudBees Inc.
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
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Syslog message as defined in <a href="https://tools.ietf.org/html/rfc5424">RFC 5424 - The Syslog Protocol</a>.
 *
 * Also compatible with <a href="http://tools.ietf.org/html/rfc3164">RFC-3164: The BSD syslog Protocol</a>,
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class SyslogMessage {
    public final static String SP = " ";
    public final static String NILVALUE = "-";

    private Facility facility;
    private Severity severity;
    private String timestamp;
    private String hostname;
    private String appName = NILVALUE;
    private String procId = NILVALUE;
    private String msgId = NILVALUE;
    private Set<SDElement> sdElements;
    private static final SimpleDateFormat rfc3339Formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * Use a {@link java.io.CharArrayWriter} instead of a {@link String}  or a {@code char[]} because middlewares like
     * Apache Tomcat use {@code CharArrayWriter} and it's convenient for pooling objects.
     */
    private CharArrayWriter msg;

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public SyslogMessage withFacility(Facility facility) {
        this.facility = facility;
        return this;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public SyslogMessage withSeverity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public Date getTimestamp() {
        return timestamp == null ? null : new Date(timestamp);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = rfc3339Formatter.format(timestamp == null ? new Date() : new Date(timestamp.getTime()));
    }

    public SyslogMessage withTimestamp(long timestamp) {
        this.timestamp = rfc3339Formatter.format(new Date(timestamp));
        return this;
    }

    public SyslogMessage withTimestamp(Date timestamp) {
        this.timestamp = rfc3339Formatter.format(timestamp == null ? new Date() : new Date(timestamp.getTime()));
        return this;
    }

    public SyslogMessage withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public SyslogMessage withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public SyslogMessage withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public SyslogMessage withProcId(String procId) {
        this.procId = procId;
        return this;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public SyslogMessage withMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public CharArrayWriter getMsg() {
        return msg;
    }

    public void setMsg(CharArrayWriter msg) {
        this.msg = msg;
    }

    public SyslogMessage withMsg(CharArrayWriter msg) {
        this.msg = msg;
        return this;
    }

    public SyslogMessage withMsg(final String msg) {
        return withMsg(new CharArrayWriter() {
            {
                append(msg);
            }
        });
    }
    
    public Set<SDElement> getSDElements() {
        Set<SDElement> ssde = sdElements;
        if (ssde == null) {
            ssde = new HashSet<>(0);
        }
        return ssde;
    }
    
    public void setSDElements(Set<SDElement> ssde) {
        this.sdElements = ssde;
    }
    
    public SyslogMessage withSDElement(SDElement sde) {
        if (sdElements == null) {
            sdElements = new HashSet<>();
        }
        sdElements.add(sde);
        return this;
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc5424">RFC-5424</a> message.
     */
    public String toRfc5424SyslogMessage() {

        StringWriter sw = new StringWriter(msg == null ? 32 : msg.size() + 32);
        try {
            toRfc5424SyslogMessage(sw);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return sw.toString();
    }

    /**
     * Generates an <a href="http://tools.ietf.org/html/rfc5424">RFC-5424</a> message.
     *
     * The priority is calculated by facility * 8 + severity, see
     * <a href="https://tools.ietf.org/html/rfc5424#section-6.2.1">RFC-5424, Section 6.2.1</a>
     */
    public void toRfc5424SyslogMessage(Writer out) throws IOException {

        int pri = facility.numericalCode() * 8 + severity.numericalCode();

        out.write('<');
        out.write(String.valueOf(pri));
        out.write('>');
        out.write('1'); // version
        out.write(SP);
        out.write(timestamp); // message time
        out.write(SP);
        out.write(hostname); // emitting server hostname
        out.write(SP);
        out.write(appName);
        out.write(SP);
        out.write(procId);
        out.write(SP);
        out.write(msgId);
        out.write(SP);
        writeStructuredDataOrNillableValue(sdElements, out);
        if (msg != null) {
            out.write(SP);
            msg.writeTo(out);
        }
    }
    
    protected void writeStructuredDataOrNillableValue(Set<SDElement> ssde, Writer out) throws IOException {
        if (ssde == null || ssde.isEmpty()) {
            out.write(NILVALUE);
        } else {
            for (SDElement sde : ssde) {
                writeSDElement(sde, out);
            }
        }
    }
    
    protected void writeSDElement(SDElement sde, Writer out) throws IOException {
        out.write("[");
        out.write(sde.getSdID());
        for (SDParam sdp : sde.getSdParams()) {
            writeSDParam(sdp, out);
        }
        out.write("]");
    }
    
    protected void writeSDParam(SDParam sdp, Writer out) throws IOException {
        out.write(SP);
        out.write(sdp.getParamName());
        out.write('=');
        out.write('"');
        out.write(getEscapedParamValue(sdp.getParamValue()));
        out.write('"');
    }
    
    protected String getEscapedParamValue(String paramValue) {
        StringBuilder sb = new StringBuilder(paramValue.length());
        
        for (int i = 0; i < paramValue.length(); i++) {
            char c = paramValue.charAt(i);
            switch (c) {
                // Falls through
                case '"':
                case '\\':
                case ']':
                    sb.append('\\');
                    break;
                default:
                    break;
            }
            sb.append(c);
        }
        
        return sb.toString();
    }
}
