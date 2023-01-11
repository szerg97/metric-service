package com.szalai.metricservice.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Metrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DataController {

    private final Stack<String> stack = new Stack<>();
    private final Counter requestCount = Metrics.counter("my_query_counter");


    public DataController() {
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

    private void modifyStack(){
        if(new Random().nextInt(2) == 1){
            stack.pop();
        }
        else{
            stack.push("[{\"key\":\"identifier\", \"value\":"+ UUID.randomUUID() +"}]");
        }
    }
}
