/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docx4j.template.jsp.engine.runtime.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 
 * TODO
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class JspOutputStreamPrinterEx extends JspWriter {
    private final OutputStream os;
    private final Charset charset;

    public JspOutputStreamPrinterEx(OutputStream os, Charset charset) {
        this.os = os;
        this.charset = charset;
    }

    @Override
    public Object getOriginStream() {
        return os;
    }

    @Override
    public boolean isStreaming() {
        return true;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public boolean isSkipErrors() {
        return false;
    }

    @Override
    public void print(int x) throws IOException {
        os.write(x);
    }

    @Override
    public void print(byte x[]) throws IOException {
        if (x != null) {
            os.write(x);
        }
    }

    @Override
    public void print(byte x[], int offset, int length) throws IOException {
        if (x != null) {
            os.write(x, offset, length);
        }
    }

    @Override
    public void print(char x[]) throws IOException {
        if (x != null) {
            os.write(new String(x).getBytes(charset));
        }
    }

    @Override
    public void print(char x[], int offset, int length) throws IOException {
        if (x != null) {
            os.write(new String(x, offset, length).getBytes(charset));
        }
    }

    @Override
    public void print(String x) throws IOException {
        if (x != null) {
            os.write(x.getBytes(charset));
        }
    }

    @Override
    public void flush() throws IOException {
        os.flush();
    }

    @Override
    public void close() throws IOException {
        os.close();
    }
}
