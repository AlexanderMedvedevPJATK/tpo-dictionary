package com.s28572.tpo02.profiles;

import com.s28572.tpo02.Entry;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("LowerCase")
public class LowerCaseProfile implements CaseProfile {
    @Override
    public Entry modify(Entry record) {
        record.setEn(record.getEn().toLowerCase());
        record.setDe(record.getDe().toLowerCase());
        record.setPl(record.getPl().toLowerCase());
        return record;
    }
}
