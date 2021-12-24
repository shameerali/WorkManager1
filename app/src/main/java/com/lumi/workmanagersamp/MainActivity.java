package com.lumi.workmanagersamp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button onetimeWork, periodicWork, chainableWork, parallelWork, cancelPeriodicWork,
    workWithConstraints, workWithData;

    private  PeriodicWorkRequest periodicWorkRequest;

    private UUID getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        onetimeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                        .build();

                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }
        });

        periodicWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWork.class, 10 , TimeUnit.MINUTES)
                        .addTag("periodicWorkRequest")
                        .build();

                WorkManager.getInstance().enqueue(periodicWorkRequest);
            }
        });

        chainableWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OneTimeWorkRequest MyWorkA  = new OneTimeWorkRequest.Builder(MyWorkA.class)
                        .build();

                OneTimeWorkRequest MyWorkB = new OneTimeWorkRequest.Builder(MyWorkB.class)
                        .build();

                OneTimeWorkRequest MyWorkC  = new OneTimeWorkRequest.Builder(MyWorkC.class)
                        .build();

                WorkManager.getInstance()
                        .beginWith(MyWorkA)
                        .then(MyWorkB)
                        .then(MyWorkC)
                        .enqueue();

            }
        });

        parallelWork. setOnClickListener(v->{
            OneTimeWorkRequest MyWorkA  = new OneTimeWorkRequest.Builder(MyWorkA.class)
                    .build();

            OneTimeWorkRequest MyWorkB = new OneTimeWorkRequest.Builder(MyWorkB.class)
                    .build();

            OneTimeWorkRequest MyWorkC  = new OneTimeWorkRequest.Builder(MyWorkC.class)
                    .build();

            List<OneTimeWorkRequest> beginWith_A_and_B = new ArrayList<>();
            beginWith_A_and_B.add(MyWorkA);
            beginWith_A_and_B.add(MyWorkB);

            WorkManager.getInstance()
                    .beginWith(beginWith_A_and_B)
                    .then(MyWorkC)
                    .enqueue();
        });

        cancelPeriodicWork.setOnClickListener(v ->{

            getId = periodicWorkRequest.getId();
            WorkManager.getInstance().cancelWorkById(getId);
            Toast.makeText(this, "PeriodicWork Cancel By Id", Toast.LENGTH_LONG).show();

        });

        workWithConstraints.setOnClickListener(v->{
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();


            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWork.class)
                    .setConstraints(constraints)

                    .build();

            WorkManager.getInstance().enqueue(oneTimeWorkRequest);

            WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null){
                                Toast.makeText(getApplicationContext(), "on time request "+
                                        String.valueOf(workInfo.getState().name()),Toast.LENGTH_LONG).show();
                            }

                            if (workInfo != null && workInfo.getState().isFinished()) {
                                // ... do something with the result ...
                                Toast.makeText(MainActivity.this, "Work Finished",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        });

        workWithData.setOnClickListener(v->{
            Data data = new Data.Builder()
                    .putString(MyWorkWithData.EXTRA_TITLE,"I came activity")
                    .putString(MyWorkWithData.EXTRA_TEXT,"This is messaasdsfsf")
                    .build();

            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorkWithData.class)
                    .setInputData(data)
                    .build();

            WorkManager.getInstance().enqueue(oneTimeWorkRequest);

            WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                String message = workInfo.getOutputData().getString(MyWorkWithData.EXTRA_OUTPUT_MESSAGE);

                                Log.d("TAG==>", "onChanged: "+message);
                            }
                        }
                    });
        });


    }

    private void initView(){
        onetimeWork = findViewById(R.id.OneTimeWork);
        periodicWork = findViewById(R.id.PeriodicWork);
        chainableWork = findViewById(R.id.ChainableWork);
        parallelWork = findViewById(R.id.parallelwork);
        cancelPeriodicWork = findViewById(R.id.CancelPeriodicWork);
        workWithConstraints = findViewById(R.id.workwithconstraints);
        workWithData = findViewById(R.id.WorkWithData);
    }
}