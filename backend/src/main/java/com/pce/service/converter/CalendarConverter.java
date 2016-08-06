package com.pce.service.converter;

import org.modelmapper.AbstractConverter;
import org.modelmapper.internal.cglib.core.Converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 6/08/2016.
 */
public class CalendarConverter  extends AbstractConverter<Calendar, String> {

  private final String dateTimeFormatPattern = "yyyy/MM/dd HH:mm:ss z";
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatPattern);
  @Override
  protected String convert(Calendar source) {
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(source.getTime().toInstant(),
            ZoneId.systemDefault());
    return source != null ? formatter.format(zonedDateTime) : null;
  }
}
