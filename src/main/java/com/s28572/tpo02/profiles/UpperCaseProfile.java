package com.s28572.tpo02.profiles;

import com.s28572.tpo02.Entry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("UpperCase")
public class UpperCaseProfile implements CaseProfile {
    @Override
    public Entry modify(Entry record) {
        record.setEn(record.getEn().toUpperCase());
        record.setDe(record.getDe().toUpperCase());
        record.setPl(record.getPl().toUpperCase());
        return record;
    }
}
