// Copyright 2015-2016 Stanford University
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package glade.util;

import picocli.CommandLine;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    public enum Level {
        OFF(null), ERROR("red"), INFO("white"), DEBUG("white"), ALL(null);

        final String color;

        Level(String color) {
            this.color = color;
        }
    }

    private static OutputStream outputStream = System.out;
	private static Level loggingLevel = Level.INFO;
	private static CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.AUTO;

	public static void setOutputStream(OutputStream outputStream) {
        if (outputStream == System.out) {
            ansi = CommandLine.Help.Ansi.AUTO;
        } else {
            ansi = CommandLine.Help.Ansi.OFF;
        }
        Log.outputStream = outputStream;
    }

	public static void setLoggingLevel(Level loggingLevel) {
		Log.loggingLevel = loggingLevel;
	}

	public static void error(String message) {
	    writeLog(Level.ERROR, message);
	}

    public static void info(String message) {
	    writeLog(Level.INFO, message);
    }

    public static void debug(String message) {
	    writeLog(Level.DEBUG, message);
    }

    private static void writeLog(Level level, String message) {
        if (loggingLevel.ordinal() >= level.ordinal()) {
            try {
                outputStream.write(ansi.string(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS").format(LocalDateTime.now())
                        + " - @|" + level.color + " " + level + "|@ - " + message + "\n")
                    .getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
