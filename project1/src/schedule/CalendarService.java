package schedule;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public interface CalendarService extends Remote {

    /*
     * @param from
     * @param to
     * @param desc  description
     * @return      the id of newly-added schedule if succeed, -1 otherwise
     */
    int addSchedule(GregorianCalendar from, GregorianCalendar to, String desc) throws RemoteException;

    /*
     * @param id    the id of schedule
     */
    void deleteSchedule(int id) throws RemoteException;

    /*
     * @param from
     * @param to
     * @return      a vector of all schedules between 'from' and 'to'
     */
    ArrayList<CalendarEvent> retrieveSchedule(GregorianCalendar from, GregorianCalendar to) throws RemoteException;

}

