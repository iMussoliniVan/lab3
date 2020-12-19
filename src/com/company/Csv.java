package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.Closeable;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class Csv {
    public static class Writer {
        private Appendable appendable;

        private char delimiter = ';';

        private boolean first = true;

        public Writer(String fileName) {
            this(new File(fileName));
        }

        public Writer(File file) {
            try {
                appendable = new FileWriter(file);
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
        }

        public Writer(Appendable appendable) {
            this.appendable = appendable;
        }

        public Writer value(String value) {
            if (!first) string("" + delimiter);
            string(escape(value));
            first = false;
            return this;
        }

        public Writer newLine() {
            first = true;
            return string("\n");
        }

        public Writer comment(String comment) {
            if (!first) throw new FormatException("invalid csv: misplaced comment");
            return string("#").string(comment).newLine();
        }

        public Writer flush() {
            try {
                if (appendable instanceof Flushable) {
                    Flushable flushable = (Flushable) appendable;
                    flushable.flush();
                }
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
            return this;
        }

        public void close() {
            try {
                if (appendable instanceof Closeable) {
                    Closeable closeable = (Closeable) appendable;
                    closeable.close();
                }
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
        }

        private Writer string(String s) {
            try {
                appendable.append(s);
            } catch (java.io.IOException e) {
                throw new IOException(e);
            }
            return this;
        }

        private String escape(String value) {
            if (value == null) return "";
            if (value.length() == 0) return "\"\"";

            boolean needQuoting = value.startsWith(" ") || value.endsWith(" ") || (value.startsWith("#") && first);
            if (!needQuoting) {
                for (char ch : new char[]{'\"', '\\', '\r', '\n', '\t', delimiter}) {
                    if (value.indexOf(ch) != -1) {
                        needQuoting = true;
                        break;
                    }
                }
            }

            String result = value.replace("\"", "\"\"");
            if (needQuoting) result = "\"" + result + "\"";
            return result;
        }

        public Writer delimiter(char delimiter) {
            this.delimiter = delimiter;
            return this;
        }
    }

    public static class Exception extends RuntimeException {
        public Exception() {
        }

        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }

        public Exception(Throwable cause) {
            super(cause);
        }
    }

    public static class IOException extends Exception {
        public IOException() {
        }

        public IOException(String message) {
            super(message);
        }

        public IOException(String message, Throwable cause) {
            super(message, cause);
        }

        public IOException(Throwable cause) {
            super(cause);
        }
    }

    public static class FormatException extends Exception {
        public FormatException() {
        }

        public FormatException(String message) {
            super(message);
        }

        public FormatException(String message, Throwable cause) {
            super(message, cause);
        }

        public FormatException(Throwable cause) {
            super(cause);
        }
    }
}