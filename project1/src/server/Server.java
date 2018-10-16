package server;

import schedule.CalendarEvent;
import schedule.CalendarService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public class Server implements CalendarService {

    private static ArrayList<CalendarEvent> calendarEventVector;
    private static int nextEventId = 0;


    public static void main(String[] args) {
        calendarEventVector = new ArrayList<CalendarEvent>();
        try {
            Server obj = new Server();
            CalendarService stub = (CalendarService) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("CalendarService", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server Exception: " + e.toString());
            e.printStackTrace();
        }

    }


    @Override
    public int addSchedule(GregorianCalendar from, GregorianCalendar to, String desc) {
        CalendarEvent candidate = new CalendarEvent(from, to, desc, nextEventId);
        for (CalendarEvent event : calendarEventVector) {
            if (candidate.isConflict(event)) {
                return -1;
            }
        }

        calendarEventVector.add(candidate);
        return nextEventId++;
    }

    @Override
    public void deleteSchedule(int id)  {
        for (CalendarEvent event : calendarEventVector) {
            if (event.getId() == id) {
                calendarEventVector.remove(event);
                break;
            }
        }
    }

    @Override
    public ArrayList<CalendarEvent> retrieveSchedule(GregorianCalendar from, GregorianCalendar to) {
        ArrayList<CalendarEvent> res = new ArrayList<CalendarEvent>();
        for (CalendarEvent event : calendarEventVector) {
            if (from.compareTo(event.getFrom()) <= 0 &&
                    event.getTo().compareTo(to) <= 0) {
                res.add(event);
            }
        }

        return res;
    }
}

