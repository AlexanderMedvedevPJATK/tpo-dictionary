package com.s28572.tpo02.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("LowerCase")
public class LowerCaseProfile implements CaseProfile {
    @Override
    public String modify(Object record) {
        return record.toString().toLowerCase();
    }
}
