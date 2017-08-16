/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年1月13日 下午1:57:53
 */
package com.mogujie.jarvis.core.expression.cron;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.google.common.collect.Range;

/**
 * @author wuya
 *
 */
public class LastDayOfMonthParser extends AbstractParser {

    private Set<Integer> set = new HashSet<>();
    private Set<Integer> result = new HashSet<>();
    private Range<Integer> range;
    private DurationField type;
    private static final Pattern LAST_DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d+)?L");

    public LastDayOfMonthParser(Range<Integer> range, DurationField type) {
        super(range, type);
        this.range = range;
        this.type = type;
    }

    @Override
    public boolean matches(String cronFieldExp) throws ParseException {
        Matcher m = LAST_DAY_OF_MONTH_PATTERN.matcher(cronFieldExp);
        if (m.matches()) {
            if ("L".equals(cronFieldExp)) {
                if (type.equals(DurationField.DAY_OF_MONTH)) {
                    set.add(1);
                    return true;
                }
            } else {
                int value = Integer.parseInt(m.group(1));
                if (range.contains(value)) {
                    set.add(value);
                    return true;
                } else {
                    throw new ParseException(
                            String.format("Invalid value of %s: %s, out of range %s", type.name, cronFieldExp, range.toString().replace("‥", ", ")),
                            -1);
                }
            }
        }

        return false;
    }

    @Override
    public Set<Integer> parse(DateTime dateTime) {
        if (set != null) {
            result.clear();

            MutableDateTime mdt = dateTime.dayOfMonth().withMaximumValue().toMutableDateTime();
            int maxDayOfMonth = mdt.getDayOfMonth();
            if (type == DurationField.DAY_OF_MONTH) {
                for (Integer value : set) {
                    result.add(maxDayOfMonth - value + 1);
                }
            }

            if (type == DurationField.DAY_OF_WEEK) {
                for (int i = 0; i < 7; i++) {
                    mdt.setDayOfMonth(maxDayOfMonth - i);
                    if (set.contains(mdt.getDayOfWeek())) {
                        result.add(mdt.getDayOfMonth());
                    }
                }
            }

            return result;
        }

        return null;
    }

}
