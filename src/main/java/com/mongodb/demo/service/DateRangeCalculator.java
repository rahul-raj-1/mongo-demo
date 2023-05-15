package com.mongodb.demo.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

public class DateRangeCalculator {
    public static Set<DateRange> calculateDateRanges(LocalDate minDate, LocalDate maxDate) {
        Set<DateRange> dateRanges = new TreeSet<>((dr1, dr2) -> dr1.getStartDate().compareTo(dr2.getStartDate()));
        LocalDate startDate = minDate;
        LocalDate endDate;

        while (startDate.isBefore(maxDate) || startDate.isEqual(maxDate)) {
            endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            if (endDate.isAfter(maxDate)) {
                endDate = maxDate;
            }

            dateRanges.add(new DateRange(startDate, endDate));

            startDate = endDate.plusDays(1);
        }

        return dateRanges;
    }

    public static void main(String[] args) {
        LocalDate minDate = LocalDate.of(2023, 1, 10);
        LocalDate maxDate = LocalDate.of(2023, 4, 20);

        Set<DateRange> dateRanges = calculateDateRanges(minDate, maxDate);

        for (DateRange range : dateRanges) {
            System.out.println("Start date: " + range.getStartDate() + ", End date: " + range.getEndDate());
        }
    }

    static class DateRange {
        private final LocalDate startDate;
        private final LocalDate endDate;

        public DateRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }
}

