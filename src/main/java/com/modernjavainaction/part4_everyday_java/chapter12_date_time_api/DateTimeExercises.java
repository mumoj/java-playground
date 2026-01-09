package com.modernjavainaction.part4_everyday_java.chapter12_date_time_api;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Chapter 12: New Date and Time API
 * 
 * Key concepts:
 * - LocalDate, LocalTime, LocalDateTime
 * - Instant, Duration, Period
 * - Manipulating dates
 * - TemporalAdjusters
 * - Time zones (ZonedDateTime)
 * - Formatting and parsing
 */
public class DateTimeExercises {

    public static void main(String[] args) {
        System.out.println("=== Chapter 12: New Date and Time API ===\n");

        // 1. LocalDate
        System.out.println("1. LocalDate:");
        LocalDate today = LocalDate.now();
        LocalDate specificDate = LocalDate.of(2024, 3, 15);
        LocalDate parsedDate = LocalDate.parse("2024-12-25");

        System.out.println("  Today: " + today);
        System.out.println("  Specific date: " + specificDate);
        System.out.println("  Parsed date: " + parsedDate);
        System.out.println("  Year: " + today.getYear());
        System.out.println("  Month: " + today.getMonth() + " (" + today.getMonthValue() + ")");
        System.out.println("  Day of month: " + today.getDayOfMonth());
        System.out.println("  Day of week: " + today.getDayOfWeek());
        System.out.println("  Day of year: " + today.getDayOfYear());
        System.out.println("  Is leap year: " + today.isLeapYear());

        // 2. LocalTime
        System.out.println("\n2. LocalTime:");
        LocalTime now = LocalTime.now();
        LocalTime specificTime = LocalTime.of(14, 30, 45);
        LocalTime parsedTime = LocalTime.parse("10:15:30");

        System.out.println("  Now: " + now);
        System.out.println("  Specific time: " + specificTime);
        System.out.println("  Hour: " + now.getHour());
        System.out.println("  Minute: " + now.getMinute());
        System.out.println("  Second: " + now.getSecond());

        // 3. LocalDateTime
        System.out.println("\n3. LocalDateTime:");
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MARCH, 15, 14, 30, 0);
        LocalDateTime nowDateTime = LocalDateTime.now();

        System.out.println("  Specific: " + dateTime);
        System.out.println("  Now: " + nowDateTime);
        System.out.println("  Combined from date and time: " +
                LocalDateTime.of(today, specificTime));

        // 4. Instant - machine timestamp
        System.out.println("\n4. Instant (machine time):");
        Instant instant = Instant.now();
        System.out.println("  Now: " + instant);
        System.out.println("  Epoch second: " + instant.getEpochSecond());
        System.out.println("  3 seconds later: " + instant.plusSeconds(3));

        // 5. Duration and Period
        System.out.println("\n5. Duration and Period:");
        Duration duration = Duration.between(specificTime, LocalTime.now());
        System.out.println("  Duration since " + specificTime + ": " + duration);

        Period period = Period.between(specificDate, today);
        System.out.println("  Period since " + specificDate + ": " + period);
        System.out.println("  Years: " + period.getYears() + ", Months: " + period.getMonths() +
                ", Days: " + period.getDays());

        Duration twoHours = Duration.ofHours(2);
        Period tenDays = Period.ofDays(10);
        System.out.println("  Two hours: " + twoHours);
        System.out.println("  Ten days: " + tenDays);

        // 6. Manipulating dates (immutable - returns new instance)
        System.out.println("\n6. Manipulating dates:");
        LocalDate date1 = LocalDate.of(2024, 3, 15);
        LocalDate date2 = date1.plusWeeks(1);
        LocalDate date3 = date1.minusDays(5);
        LocalDate date4 = date1.withYear(2025);
        LocalDate date5 = date1.withMonth(12);

        System.out.println("  Original: " + date1);
        System.out.println("  Plus 1 week: " + date2);
        System.out.println("  Minus 5 days: " + date3);
        System.out.println("  With year 2025: " + date4);
        System.out.println("  With month 12: " + date5);

        // 7. TemporalAdjusters
        System.out.println("\n7. TemporalAdjusters:");
        LocalDate date = LocalDate.of(2024, 3, 15);

        System.out.println("  Date: " + date);
        System.out.println("  Next Monday: " + date.with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
        System.out.println("  First day of month: " + date.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.println("  Last day of month: " + date.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println("  First day of next month: " + date.with(TemporalAdjusters.firstDayOfNextMonth()));
        System.out.println("  First day of next year: " + date.with(TemporalAdjusters.firstDayOfNextYear()));

        // 8. Time zones
        System.out.println("\n8. Time Zones:");
        ZoneId zoneNY = ZoneId.of("America/New_York");
        ZoneId zoneTokyo = ZoneId.of("Asia/Tokyo");
        ZoneId zoneLocal = ZoneId.systemDefault();

        ZonedDateTime zonedNow = ZonedDateTime.now();
        ZonedDateTime zonedNY = ZonedDateTime.now(zoneNY);
        ZonedDateTime zonedTokyo = ZonedDateTime.now(zoneTokyo);

        System.out.println("  Local zone: " + zoneLocal);
        System.out.println("  Local time: " + zonedNow);
        System.out.println("  New York time: " + zonedNY);
        System.out.println("  Tokyo time: " + zonedTokyo);

        // Convert between zones
        ZonedDateTime localToTokyo = zonedNow.withZoneSameInstant(zoneTokyo);
        System.out.println("  Local time in Tokyo: " + localToTokyo);

        // 9. Formatting and parsing
        System.out.println("\n9. Formatting and Parsing:");
        LocalDate dateToFormat = LocalDate.of(2024, 3, 15);

        System.out.println("  BASIC_ISO_DATE: " + dateToFormat.format(DateTimeFormatter.BASIC_ISO_DATE));
        System.out.println("  ISO_LOCAL_DATE: " + dateToFormat.format(DateTimeFormatter.ISO_LOCAL_DATE));

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("  Custom (dd/MM/yyyy): " + dateToFormat.format(customFormatter));

        DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy",
                java.util.Locale.ITALIAN);
        System.out.println("  Italian: " + dateToFormat.format(italianFormatter));

        // Parsing with custom format
        LocalDate parsed = LocalDate.parse("15/03/2024", customFormatter);
        System.out.println("  Parsed from '15/03/2024': " + parsed);

        System.out.println("\n=== EXERCISES ===");
        System.out.println("Exercise 12.1: Calculate your age in days");
        System.out.println("Exercise 12.2: Find the next payday (15th or last day of month)");
        System.out.println("Exercise 12.3: Create a custom TemporalAdjuster for next working day");
        System.out.println("Exercise 12.4: Calculate business days between two dates");
    }

    // TODO: Exercise 12.2 - Next payday calculator
    public static LocalDate nextPayday(LocalDate date) {
        // Implement: return next 15th or last day of month (whichever is sooner)
        return null;
    }

    // TODO: Exercise 12.4 - Business days calculator
    public static long businessDaysBetween(LocalDate start, LocalDate end) {
        // Implement: count only weekdays
        return 0;
    }
}
