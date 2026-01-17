Schedule Maker

============

This is a schedule maker for my university classes. I decided to automate the process instead of doing it manually.

To list all do not include a top X when calling Schedule.sort().

Lists all possible valid variants of a university timetable, then ranks them by a score out of 100 utilising the following algorithm:

(46 / (numDays - 5.1) + 112) - (e^(2 * longestBreak) - 2) - (totalBreak / 2)^3 - (2 * (longestConsecutive^2 / 10 - 1))^2 - (earliestStart / 1.5 -7)^2 - (latestStart / 1.5 -7)^2 - e^((latestFinish - 15) / 2)

Subjects are inputted in the format of:
Programming Environment (CSE1PE) {
Lecture-([2,9,2])
Lab-([2,12,2],[2,14,2],[3,9,2],[3,11,2],[4,9,2],[4,11,2])};

Code is not commented.

Enjoy :)

"# Schedule-Maker" 
