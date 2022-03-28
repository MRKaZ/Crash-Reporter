<!-- PROJECT LOGO -->
<div align="center">
	<img src="https://github.com/MRKaZ/Crash-Reporter/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" width="115">
</div>

<!-- TITLE --> 
<p align="center">
  <h1 align="center">Crash reporter</h1>
</p>

<!-- INTRODUCTION --> 
<p align="center">
 Crash reporter library. Simply catch fatal exceptions and throwables in your application. Kotlin language.
</p>

## Prerequisites

#### Add this in your root `settings.gradle` file.

```gradle
dependencyResolutionManagement {
    ...
    repositories {
	...
        maven { url "https://jitpack.io" }
    }
}

```

## Dependency

#### Add this to your module's `build.gradle` file:

```gradle
dependencies {
	...
	implementation 'com.github.MRKaZ:Crash-Reporter:v1.0'
}
```
<!-- IMPLEMENTING -->
## Implementing
#### Initiate the library using an ``Application`` class. Example class [here](https://github.com/MRKaZ/Crash-Reporter/blob/master/app/src/main/java/com/mrkazofficial/crashreporter/App.kt).
```
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initiate crash reporter
        CrashReporter.initiate(this)
        // TODO : Configuration
        // If you want to save crash log as a .txt file true/false
        // Need WRITE_EXTERNAL_STORAGE && READ_EXTERNAL_STORAGE permissions
        CrashReporter.saveCrashLog(true)
        // You can set a custom directory to save crash log .txt file, Default: App package files directory
        CrashReporter.setCrashReporterSavePath(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            ).toString()
        )
    }
}
```
<!-- USAGE -->
## Usage
#### Catch crashes or throwables following like this.
```
try {
  ...
  // Do your job here.
} catch (e: Exception) {
  //Caught exception
  CrashReporter.exception(e)
}
```
### **Receive data.**
- Also you can receive the crash reports using a ``BroadcastReceiver`` [Example](https://github.com/MRKaZ/Crash-Reporter/blob/30889eb8646eb930ea773cae5ecaa388c817a9da/app/src/main/java/com/mrkazofficial/crashreporter/MainActivity.kt#L113).
- First of all you have to register to a **LocalBroadcastManager** using local event key.
- Keys for receive data
  - Register with ``CRASH_REPORTER_LOCAL_EVENT``
  - ``CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY``
  - ``CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY``

#### Examples 
1. Do the register stuff with using this local event key ``CRASH_REPORTER_LOCAL_EVENT`` also dont forget to unregister on destroy and stop.
Do not register on resume.
```
// Register localBroadcastManager
private fun registerReceivers() {
    LocalBroadcastManager.getInstance(this).registerReceiver(
        crashReporterReceiver, IntentFilter(CRASH_REPORTER_LOCAL_EVENT)
    )
}

// Unregister localBroadcastManager
private fun unRegisterReceivers() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(crashReporterReceiver)
}
```
2. Receive report data ``CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY``.
```
val crashReports = intent.getStringExtra(CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY)
```
3. If you want to show a dialog when app get crash? ``CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY``.
```
val isShowDialog = intent.getBooleanExtra(CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY, false)
```
4. BroadcastReceiver.
```
private var crashReporterReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // For view reports.
            val crashReports = intent.getStringExtra(CRASH_REPORTER_LOCAL_EXTRA_CRASH_LOG_KEY)
            // print("Crash reports $crashReports")
            // If you want to show a dialog you can use like this.
            val isShowDialog = intent.getBooleanExtra(CRASH_REPORTER_LOCAL_EXTRA_SHOW_DIALOG_KEY, false)
            if (isShowDialog) {
               ...
               // Show your own dialog or I already provided a custom dialog with a expandable layout to show crash report.
            }
        }
    }
```
5. If you already setup ``CrashReporter.saveCrashLog(true)`` to save crash reports as a .txt. So you can retrieve the reports saved path like this.
```
// Crash reports saved path.
val path = CrashReporter.getCrashReportPath()
```

### **Adapter**
1. If you want to setup a crash reports adapter [CrashLogActivity](https://github.com/MRKaZ/Crash-Reporter/blob/30889eb8646eb930ea773cae5ecaa388c817a9da/app/src/main/java/com/mrkazofficial/crashreporter/CrashLogActivity.kt) & [CrashLogsAdapter](https://github.com/MRKaZ/Crash-Reporter/blob/30889eb8646eb930ea773cae5ecaa388c817a9da/app/src/main/java/com/mrkazofficial/crashreporter/adapters/CrashLogsAdapter.kt).
   - For retrieve the all crashes as a list ``CrashReporterUtils.getAllCrashes(File(CrashReporter.getCrashReportPath()))``

#### Adapter examples
```
// Crash reports saved path.
val path = CrashReporter.getCrashReportPath()
// Filr list of crashes.
val fileList: ArrayList<File> = ArrayList(CrashReporterUtils.getAllCrashes(File(path)))
```

<!-- PREVIEW -->
<!-- https://github.com/MRKaZ/Crash-Reporter/blob/master/art/27032022_221055.mp4 -->
## Preview
<img src="https://i2.paste.pics/a50f6bbcc7d747ee14074efc7a7f57ee.png" width="250"> <img src="https://i2.paste.pics/97687c17c215de5ba1cf16944863c02c.png" width="250"> <img src="https://i2.paste.pics/e789b2073ff5d76881e9cb18e932848d.png" width="250"> 
<img src="https://i2.paste.pics/6219668e234a0c828a96fa141f4cf680.png" width="250"> <img src="https://i2.paste.pics/94a3e73ebfa1f53dc5fbe5342615a051.png" width="250"> <img src="https://i2.paste.pics/3e33be5e4ecdcf78e368d5e49f4187b7.png" width="250">

### [Video Preview](https://github.com/MRKaZ/Crash-Reporter/blob/30889eb8646eb930ea773cae5ecaa388c817a9da/art/27032022_221055.mp4?raw=true)

<!-- CREDITS -->
## Credits
* [AAkira - ExpandableLayout](https://github.com/AAkira/ExpandableLayout)

<!-- LICENSE -->
## License
<div align="left">
	<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/ASF_Logo.svg/1200px-ASF_Logo.svg.png" width="220">
</div>

<!-- WHITESPACE -->
<br />

Distributed under the **APACHE 2.0 LICENSE**. See [`LICENSE`](https://github.com/MRKaZ/Crash-Reporter/blob/master/LICENSE) for more information.
