package com.lumi.workmanagersamp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorkB extends Worker {

    private static final String TAB = MyWorkB.class.getSimpleName();

    public MyWorkB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAB, "My Work B ");
        return Result.success();
    }
}
