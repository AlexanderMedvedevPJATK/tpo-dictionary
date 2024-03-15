package com.s28572.tpo02.profiles;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("Default")
public class DefaultCaseProfile implements CaseProfile {

    @Override
    public String modify(Object record) {
        return record.toString();
    }
}
