## Distributed Computing - Assignment 1

### Enviornment
Ubuntu 18.04 LTS 64bit
JDK-11 JRE-11

### How to compile

```
$ ./build.sh
$ ./run_rmiregistry.sh
```

After waiting a few seconds, start new session.
```
$ ./run_server.sh
```

In another session,
```
$ ./run_client.sh
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
1
Enter start time (year month day hour minute) :
2018 10 01 13 00
Enter end time (year month day hour minute) :
2018 10 01 14 00
Title: Algorithm
Sucessfully added. ID: 0
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
1
Enter start time (year month day hour minute) :
2018 10 01 11 30
Enter end time (year month day hour minute) :
2018 10 01 12 30
Title: Lunch
Sucessfully added. ID: 1
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
1
Enter start time (year month day hour minute) :
2018 10 01 10 00
Enter end time (year month day hour minute) :
2018 10 01 14 00
Title: Date
Overlapping event(s) exists.
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
3
Enter start time (year month day hour minute) :
1 1 1 1 1
Enter end time (year month day hour minute) :
2018 10 01 12 00
0 event(s) found.
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
3
Enter start time (year month day hour minute) :
2018 10 01 10 00
Enter end time (year month day hour minute) :
2018 10 01 13 00
1 event(s) found.
Event Lunch from 2018/10/1 11:30 to 2018/10/1 12:30
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
2
ID: 1
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
3
Enter start time (year month day hour minute) :
2018 10 01 00 00
Enter end time (year month day hour minute) :
2018 10 01 15 00
1 event(s) found.
Event Algorithm from 2018/10/1 13:00 to 2018/10/1 14:00
===========================
1. Add Schedule
2. Delete Schedule
3. Retrieve Schedule
4. Exit
===========================
4
```
