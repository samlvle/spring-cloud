package com.baozun.unex.hystrics;

import org.springframework.stereotype.Component;

import com.baozun.unex.ServiceFeign.client.SchedualServiceHi;

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
    }
}