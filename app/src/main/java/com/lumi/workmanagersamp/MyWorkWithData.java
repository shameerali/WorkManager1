package com.lumi.workmanagersamp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorkWithData extends Worker {

    private static final String TAB = MyWorkWithData.class.getSimpleName();
    static final String EXTRA_TITLE = "title";
    static final String EXTRA_TEXT = "text";
    static final String EXTRA_OUTPUT_MESSAGE = "output_message";


    public MyWorkWithData(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString(EXTRA_TITLE);
        String text = getInputData().getString(EXTRA_TEXT);

        Log.d(TAB, "MyWorkWithData: "+title+"  "+text);

        Data data = new Data.Builder()
                .putString(EXTRA_OUTPUT_MESSAGE , "I have came MyWorkWithData")
                .build();

        return Result.success(data);
    }
}
