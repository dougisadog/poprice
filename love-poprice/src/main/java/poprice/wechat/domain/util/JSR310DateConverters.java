package poprice.wechat.domain.util;

import com.google.common.base.Strings;

import poprice.wechat.Constants;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class JSR310DateConverters {

    private JSR310DateConverters() {}

    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        public static final LocalDateToDateConverter INSTANCE = new LocalDateToDateConverter();

        private LocalDateToDateConverter() {}

        @Override
        public Date convert(LocalDate source) {
            return source == null ? null : Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {
        public static final DateToLocalDateConverter INSTANCE = new DateToLocalDateConverter();
        private DateToLocalDateConverter() {}

        @Override
        public LocalDate convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()).toLocalDate();
        }
    }

    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
        public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();
        private ZonedDateTimeToDateConverter() {}

        @Override
        public Date convert(ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
        public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();
        private DateToZonedDateTimeConverter() {}

        @Override
        public ZonedDateTime convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        public static final StringToLocalDateTimeConverter INSTANCE = new StringToLocalDateTimeConverter();
        private StringToLocalDateTimeConverter() {}

        @Override
        public LocalDateTime convert(String source) {
            if (Strings.isNullOrEmpty(source)) {
                return null;
            }

            DateTimeFormatter fmt = null;
            LocalDateTime _value = null;
            final boolean isDateOnly = source.length() <= 10;
            if (isDateOnly) {
                fmt = Constants.FORMAT_DATE;
                _value = LocalDateTime.from(LocalDate.parse(source, fmt).atStartOfDay());
            } else {
                fmt = Constants.FORMAT_DATETIME;
                _value = LocalDateTime.parse(source, fmt);
            }

            return _value;

        }
    }

    public static class StringToZonedDateTimeConverter implements Converter<String, ZonedDateTime> {
        public static final StringToZonedDateTimeConverter INSTANCE = new StringToZonedDateTimeConverter();
        private StringToZonedDateTimeConverter() {}

        @Override
        public ZonedDateTime convert(String source) {
            return source == null ? null : ZonedDateTime.parse(source);
        }
    }

    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        public static final LocalDateTimeToDateConverter INSTANCE = new LocalDateTimeToDateConverter();
        private LocalDateTimeToDateConverter() {}

        @Override
        public Date convert(LocalDateTime source) {
            return source == null ? null : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        public static final DateToLocalDateTimeConverter INSTANCE = new DateToLocalDateTimeConverter();
        private DateToLocalDateTimeConverter() {}

        @Override
        public LocalDateTime convert(Date source) {
            return source == null ? null : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }
}
