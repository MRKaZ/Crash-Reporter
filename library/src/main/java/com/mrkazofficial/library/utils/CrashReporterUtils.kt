package com.mrkazofficial.library.utils

import java.io.File
import java.util.*

/**
 * @Project Crash Reporter
 * @Class CrashReporterUtils
 * @Author MRKaZ
 * @Since 11:35 AM, 3/10/2022
 * @Origin Taprobana (LK)
 * @Copyright (c) 2022 MRKaZ. All rights reserved.
 */

class CrashReporterUtils {

    companion object{

         fun getAllCrashes(folder: File): ArrayList<File> {
            val listOfFiles = ArrayList<File>()
            if (folder.listFiles() != null) {
                for (files in folder.listFiles()) {
                    if (files.isDirectory) {
                        getAllCrashes(files)
                    } else {
                        if (files.absolutePath.endsWith(Constants.LOG_FILE_EXTENSION)) {
                            listOfFiles.add(files)
                        }
                    }
                }
            }
            listOfFiles.sortWith(Collections.reverseOrder())
            return listOfFiles
        }
    }
}