package jodathroughjacksonwtf;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.MoreObjects;

/**
 * Configure Jackson to do ISO-8601 conversions on DateTimes.
 */
public class JodaThroughJacksonTest {
  static class Beanie {
    public final DateTime jodaDateTime;
    public final Date javaUtilDate;

    @JsonCreator
    public Beanie(@JsonProperty("jodaDateTime") DateTime jodaDateTime,
        @JsonProperty("javaUtilDate") Date javaUtilDate) {
      this.jodaDateTime = jodaDateTime;
      this.javaUtilDate = javaUtilDate;
    }

    @Override
    public String toString() {
      String toString =
          MoreObjects.toStringHelper(this.getClass()).add("jodaDateTime", jodaDateTime)
              .add("javaUtilDate", javaUtilDate).toString();
      return toString;
    }
  }

  @Test
  public void iso8601ThroughJoda() throws ParseException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new JodaModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
    Date targetDate = df.parse("2000-01-01 00:00:00 UTC");
    Beanie expectedBean = new Beanie(new DateTime(targetDate.getTime()), targetDate);

    String expectedJSON =
        "{\"jodaDateTime\":\"2000-01-01T00:00:00.000Z\","
            + "\"javaUtilDate\":\"2000-01-01T00:00:00.000+0000\"}";
    String actualJSON = mapper.writeValueAsString(expectedBean);
    assertEquals(actualJSON, expectedJSON);

    Beanie actualBean = mapper.readValue(actualJSON, Beanie.class);
    assertEquals(actualBean.javaUtilDate, expectedBean.javaUtilDate);
    assertEquals(actualBean.jodaDateTime.getMillis(), expectedBean.jodaDateTime.getMillis());

    /*
     * Why doesn't the JodaModule deserialize a DateTime that is equal to the one it serialized in
     * the first place? I expected these two to be equal but they aren't.
     */
    assertEquals(actualBean.jodaDateTime, expectedBean.jodaDateTime);
  }

}
