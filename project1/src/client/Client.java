package client;

import schedule.CalendarEvent;
import schedule.CalendarService;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Client {

    private static Scanner scanner;
    private static GregorianCalendar getDate(String name) {
        System.out.println("Enter " + name + " time (year month day hour minute) :");
        int year = scanner.nextInt();

        int month = scanner.nextInt();
        int day = scanner.nextInt();
        int hour = scanner.nextInt();
        int minute = scanner.nextInt();
        try {
            return new GregorianCalendar(year, month-1, day, hour, minute);
        } catch (Exception e) {
            System.err.println("Invalid date and time");
            e.printStackTrace();
        }
        return null;
    }

    private static int printMenu() {
        System.out.println("===========================");
        System.out.println("1. Add Schedule");
        System.out.println("2. Delete Schedule");
        System.out.println("3. Retrieve Schedule");
        System.out.println("4. Exit");
        System.out.println("===========================");

        try {
            int code = scanner.nextInt();
            if (code <= 0 || code >= 5 ) {
                return 0;
            } else {
                return code;
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
        return -1;
    }

    private static final String getDateString(GregorianCalendar gc) {

        return String.format("%d/%d/%d %02d:%02d",
                gc.get(Calendar.YEAR),
                gc.get(Calendar.MONTH) +1,
                gc.get(Calendar.DATE),
                gc.get(Calendar.HOUR),
                gc.get(Calendar.MINUTE));

    }

    public static void main(String[] args) {
        CalendarService stub;
        try {
            scanner = new Scanner(System.in);
            Registry registry = LocateRegistry.getRegistry();
            stub = (CalendarService)registry.lookup("CalendarService");
            while(true) {
                final int code = printMenu();

                if (code ==0) {
                    continue;
                }  else if (code ==1) {
                    GregorianCalendar from = getDate("start");
                    GregorianCalendar to = getDate("end");
                    String desc;

                    desc = scanner.next();

                    int id = stub.addSchedule(from, to, desc);
                    if (id == -1) {
                        System.out.println("Overlapping event(s) exists.");
                    } else {
                        System.out.println("Sucessfully added. ID: " + Integer.toString(id));
                    }

                } else if (code == 2) {
                    int id = scanner.nextInt();
                    stub.deleteSchedule(id);
                } else if (code == 3) {
                    GregorianCalendar from = getDate("start");
                    GregorianCalendar to = getDate("end");

                    ArrayList<CalendarEvent> eventList = stub.retrieveSchedule(from, to);
                    System.out.println(Integer.toString(eventList.size()) + " event(s) found.");
                    for (CalendarEvent event : eventList) {
                        System.out.println("Event " + event.getDesc() +
                                " from " + getDateString(event.getFrom()) +
                                " to " + getDateString(event.getTo()));
                    }
                } else if (code == 4) {
                    break;
                }
            }
        } catch(Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


    }
}
