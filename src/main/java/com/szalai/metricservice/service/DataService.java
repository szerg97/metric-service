package com.szalai.metricservice.service;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DataService {

    private final Stack<String> stack = new Stack<>();
    private final Timer timer = Metrics.timer("get_data_timer", List.of());
    private final Timer timerAnnotated = Metrics.timer("timer_annotated");

    private void loadData(){
        if (!stack.isEmpty()){
            stack.clear();
        }
        stack.addAll(List.of(
                "[{\"key\":" + UUID.randomUUID() + ", \"value\":"+ LocalDateTime.of(2023,2,4,1,0) +"}]",
                "[{\"key\":" + UUID.randomUUID() + ", \"value\":"+ LocalDateTime.of(2023,2,4,1,1) +"}]",
                "[{\"key\":" + UUID.randomUUID() + ", \"value\":"+ LocalDateTime.of(2023,2,4,1,2) +"}]",
                "[{\"key\":" + UUID.randomUUID() + ", \"value\":"+ LocalDateTime.of(2023,2,4,1,3) +"}]",
                "[{\"key\":" + UUID.randomUUID() + ", \"value\":"+ LocalDateTime.of(2023,2,4,1,4) +"}]"
        ));
    }

    public Stack<String> getData(){
        timer.record(() -> {
           try{
               TimeUnit.MILLISECONDS.sleep(15);
           } catch(InterruptedException ignored){

           }
        });
        timer.record(30, TimeUnit.MILLISECONDS);
        try{
            timer.recordCallable(this::myCallable);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return this.stack;
    }

    private Integer myCallable(){
        try{
            TimeUnit.MILLISECONDS.sleep(3000);
        } catch(InterruptedException ignored){

        }
        return 1;
    }

    public Stack<String> getDataAnnotated(){
        timerAnnotated.record(()-> {
            try{
                loadData();
                long time = new Random().nextLong(1, 10000);
                TimeUnit.MILLISECONDS.sleep(time);
            } catch(InterruptedException ignored){

            }
        });
        return this.stack;
    }
}
