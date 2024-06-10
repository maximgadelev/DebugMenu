package ru.kpfu.itis.crashreporter.crashreporter.src.main.java.com.gadelev.crashreporter.utils;

public class CrashReporterExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler exceptionHandler;

    public CrashReporterExceptionHandler() {
        this.exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        CrashUtil.saveCrashReport(throwable);

        exceptionHandler.uncaughtException(thread, throwable);
    }
}
