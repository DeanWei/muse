/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年2月1日 下午1:27:54
 */

package com.mogujie.jarvis.core.expression.cron;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import com.google.common.collect.Range;

public class MonthAbbreviationParser extends AbstractParser {

    private int index = -1;
    private static final String[] MONTH_ABBREVIATIONS = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

    public MonthAbbreviationParser(Range<Integer> range, DurationField type) {
        super(range, type);
    }

    @Override
    public boolean matches(String cronFieldExp) throws ParseException {
        int i = 0;
        String cronFieldExpUpperCase = cronFieldExp.toUpperCase();
        for (String abbr : MONTH_ABBREVIATIONS) {
            i++;
            if (abbr.equals(cronFieldExpUpperCase)) {
                index = i;
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Integer> parse(DateTime dateTime) {
        Set<Integer> result = new HashSet<Integer>();
        if (index != -1) {
            result.add(index);
        }

        return result;
    }

}
