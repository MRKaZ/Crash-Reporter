package com.mrkazofficial.library.utils

import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

/**
 * @Project Crash Reporter
 * @Class ParseStackTrace
 * @Author MRKaZ
 * @Since 10:32 PM, 3/8/2022
 * @Origin Taprobana (LK)
 * @Copyright (c) 2022 MRKaZ. All rights reserved.
 */

/** Parse the fatal exceptions and throwable */
fun ParseStackTrace(e: Throwable): String {
    val result: Writer = StringWriter()
    val printWriter = PrintWriter(result)
    e.printStackTrace(printWriter)
    val crashLog = result.toString()
    printWriter.close()
    return crashLog
}