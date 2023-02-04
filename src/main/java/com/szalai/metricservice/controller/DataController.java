package com.szalai.metricservice.controller;

import com.szalai.metricservice.service.DataService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class DataController {

    private final Stack<String> stack = new Stack<>();
    private final Counter requestCount = Metrics.counter("my_query_counter");
    private final DataService dataService;


    public DataController(@Autowired DataService dataService) {
        this.dataService = dataService;
        stack.addAll(List.of(
                "[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]",
                "[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]",
                "[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]",
                "[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]",
                "[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]"
        ));
        Gauge.builder("size_of_stack", stack, Stack::size)
                .register(Metrics.globalRegistry);
    }

    @GetMapping("/data")
    public Stack<String> getData(){
        modifyStack();
        requestCount.increment();
        return stack;
    }

    @GetMapping("/timer")
    public Stack<String> getTimer(){
        return dataService.getData();
    }

    @GetMapping("/timer/annotated")
    public Stack<String> getTimerAnnotated(){
        return dataService.getDataAnnotated();
    }

    @GetMapping("/timer/histogram")
    public Stack<String> getTimerHistogram(){
        return dataService.getDataHistogram();
    }

    private void modifyStack(){
        if(new Random().nextInt(2) == 1){
            stack.pop();
        }
        else{
            stack.push("[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]");
        }
    }
}
