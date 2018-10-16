package schedule;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class CalendarEvent implements Serializable {
    private final GregorianCalendar from, to;
    private final String desc;
    private final int id;

    public CalendarEvent(GregorianCalendar from,
                  GregorianCalendar to,
                  String desc,
                  int id) {
        this.from = from;
        this.to = to;
        this.desc = desc;
        this.id = id;
    }

    public GregorianCalendar getFrom() { return from; }
    public GregorianCalendar getTo() { return to; }
    public String getDesc() { return desc; }
    public int getId() { return id; }


    public boolean isConflict(CalendarEvent other) {
        return (from.compareTo(other.from) <= 0 && other.from.compareTo(to) <= 0) ||
                (from.compareTo(other.to) <= 0 && other.to.compareTo(to) <= 0);
    }
}
