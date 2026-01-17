import java.io.*;
import java.util.*;

public class Schedule {
    private Subject[] subjects;

    private int[] variant;
    private double score, normalisedScore;
    private int number;
    private boolean valid = true;
    private int numDays = 0;
    private int totalBreak = 0;
    private int longestBreak = 0;
    private int longestConsecutive = 0;
    private Instance[][] slots;
    private int earliestStart = -1;
    private int latestStart = -1;
    private int latestFinish = -1; 

    public Schedule()
    {
        subjects = new Subject[0];
    }

    public Schedule(Subject[] subjects, int[] variant, int number)
    {
        this.subjects = subjects;
        this.variant = variant;
        this.number = number;
    }

    public void addSubject(Subject subject)
    {
        Subject[] s = new Subject[subjects.length + 1];

        for (int i = 0; i < subjects.length; i++)
        {
            s[i] = subjects[i];
        }

        s[s.length - 1] = subject;

        subjects = s;
    }

    public static Schedule[] sort(Schedule template)
    {
        int options = template.getOptions();
        Schedule[] processed = new Schedule[options];

        int[] currentVariant = new int[template.subjects.length];

        double highestScore = 0;

        for (int i = 0; i < options; i++)
        {
            for (int v = currentVariant.length - 1; v > 0; v--)
            {
                if(currentVariant[v] >= template.subjects[v].getOptions() && v != 0)
                {
                    currentVariant[v] = 0;
                    currentVariant[v - 1]++;
                } else if(v == 0) {
                    break;
                }
            }

            Schedule schedule = Schedule.clone(template, currentVariant, i);
            //processed[i] = schedule;

            schedule.calculateScore();

            if (schedule.getScore() >= highestScore) highestScore = schedule.getScore();

            if (processed[0] != null)
            {
                for (int j = 0; j < processed.length; j++)
                {
                    if (processed[j] == null || schedule.getScore() > processed[j].getScore())
                    {
                        for (int s = processed.length - 2; s >= j; s--)
                        {
                            processed[s + 1] = processed[s];
                        }
                        processed[j] = schedule;
                        break;
                    }
                }
            } else {
                processed[0] = schedule;
            }

            currentVariant[currentVariant.length - 1]++;
        }

        for(int i = 0; i < processed.length; i++)
        {
            processed[i].setNormalisedScore(100 * processed[i].getScore() / highestScore);
        }

        //for(int i = 0; i < 100; i++)
        //{
            //System.out.print(processed[i]);
        //}

        return processed;
    }

    public static Schedule[] sort(Schedule template, int topX)
    {
        Schedule[] toReturn = new Schedule[topX];
        Schedule[] sorted = Schedule.sort(template);

        for(int i = 0; i < topX; i++)
        {
            toReturn[i] = sorted[i];
        }

        return toReturn;
    }

    public static void exportSchedule(String fileName, Schedule[] schedules)
    {
        try {
            File f = new File(fileName + ".txt");
            PrintWriter p = new PrintWriter(f);

            for (Schedule s : schedules)
            {
                p.write(s.toString());
            }

            p.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

    public static void exportSchedule(String fileName, Schedule schedule)
    {
        Schedule[] s = {schedule};
        exportSchedule(fileName, s);
    }

    public static Schedule importOptions(String fileName)
    {
        Schedule toReturn = new Schedule();
        try {
            File f = new File(fileName + ".txt");
            //System.out.println(f.getName() + f.exists());
            Scanner r = new Scanner(f);

            boolean toContinue = true;
            while (toContinue) {
                String data = "";

                while(true) {
                    if(r.hasNextLine())
                    {
                        if(data.length() == 0 || data.charAt(data.length() - 1) != ';')
                        {
                            data += r.nextLine();
                        } else {
                            break;
                        }
                    } else {
                        toContinue = false;
                        break;
                    }
                }

                String name = data.substring(0, data.indexOf('(') - 1);
                String classCode = data.substring(data.indexOf('(') + 1, data.indexOf(')') - 1);

                String classesData = data.substring(data.indexOf('{') + 1, data.indexOf('}'));
                Class[] classes = new Class[0];
                while(true) {
                    String type = classesData.substring(0, classesData.indexOf('-'));
                    String instances = classesData.substring(classesData.indexOf('(') + 1, classesData.indexOf(')'));
                    Instance[] ins = new Instance[0];

                    int nextIndex = 0;

                    while (true)
                    {
                        if(instances.length() > nextIndex && instances.charAt(nextIndex) == '[')
                        {
                            
                            String instance = instances.substring(nextIndex + 1, instances.indexOf(']', nextIndex));
                            nextIndex = instances.indexOf(']', nextIndex) + 2;
                            int curIndex = instance.indexOf(',');
                            int day = Integer.parseInt(instance.substring(0, curIndex));
                            int hour = Integer.parseInt(instance.substring(curIndex + 1, instance.lastIndexOf(',')));
                            instance.indexOf(',', curIndex + 1);
                            int length = Integer.parseInt(instance.substring(instance.lastIndexOf(',') + 1, instance.length()));

                            Instance newInstance = new Instance(day, hour, length);

                            Instance[] temp = new Instance[ins.length + 1];

                            for(int i = 0; i < ins.length; i++)
                            {
                                temp[i] = ins[i];
                            }

                            temp[ins.length] = newInstance;
                            ins = temp;
                        } else {
                            break;
                        }
                    }

                    Class newClass = new Class(ins, type);
                    for (int i = 0; i < ins.length; i++)
                    {
                        ins[i].setType(newClass);
                    }

                    Class[] temp = new Class[classes.length + 1];

                    for(int i = 0; i < classes.length; i++)
                    {
                        temp[i] = classes[i];
                    }

                    temp[temp.length - 1] = newClass;
                    classes = temp;
                    
                    if(classesData.indexOf(')') != classesData.length())
                    {
                        classesData = classesData.substring(classesData.indexOf(')') + 1, classesData.length());
                        if(classesData.equals(""))
                        {
                            break;
                        }
                    } else {
                        break;
                    }
                }

                Subject subject = new Subject(name, classCode, classes);
                Subject[] temp = new Subject[toReturn.getSubjects().length + 1];

                for (int i = 0; i < toReturn.getSubjects().length; i++)
                {
                    temp[i] = toReturn.getSubjects()[i];
                }

                temp[toReturn.getSubjects().length] = subject;
                toReturn.setSubjects(temp);;

                data = "";
            }

            r.close();
        } catch (Exception e)
        {
            e.printStackTrace();   
        }

        return toReturn;
    }

    public String toString()
    {
        String toReturn = "";

        if(valid)
        {
            toReturn += "\n\nSchedule: " + number + "  SCORES - NORMALISED: " + normalisedScore + " RAW: " + score;
            toReturn += "\nnum of days:" + numDays + " total break:" + totalBreak + " longest break:" + longestBreak + " longest consecutive:" + longestConsecutive + " Earliest:" + earliestStart + ":00 Latest Start:" + latestStart + ":00 Latest Finish:" + latestFinish + ":00";
            for(int i = 0; i < slots.length; i++)
            {
                toReturn += "\n" + Instance.dayName(i + 1) + ": ";
                for (int j = 0; j < slots[i].length; j++)
                {
                    if(slots[i][j] != null)
                    {
                        toReturn += " " + j + ":00 " + slots[i][j].getType().getSubject().getName() + " " + slots[i][j].getType().getType() + " - ";
                    } else {
                        toReturn += ".";
                    }

                }
            }
        } else {
            //toReturn += "invalid.";
        }

        return toReturn;
    }

    public int getOptions()
    {
        int options = 1;

        for (Subject s : subjects)
        {
            options = options *  s.getOptions();
            //System.out.println(s.getName() + " " + s.getOptions());
        }

        return options;
    }

    public Schedule clone(int[] variant, int number)
    {
        return new Schedule(subjects, variant, number);
    }

    public static Schedule clone(Schedule template, int[] variant, int number)
    {
        return template.clone(variant, number);
    }

    public void copy(Schedule toCopy)
    {
        this.subjects = toCopy.subjects;
    }

    public void setVariant(int[] variant)
    {
        this.variant = variant;
    }

    public void calculateScore()
    {
        double score = 0;
        
        slots = new Instance[5][24];

        for (int i = 0; i < subjects.length; i++)
        {
            Subject subject = subjects[i];
            Instance[] chosen = subject.getOption(variant[i]);

            for (int j = 0; j < chosen.length; j++)
            {
                int day = chosen[j].getDay() - 1;
                int time = chosen[j].getHour();
                int length = chosen[j].getLength();

                for(int h = 0; h < length; h++)
                {
                    if(slots[day][time + h] != null)
                    {
                        valid = false;
                    } else {
                        slots[day][time + h] = chosen[j];
                    }
                }
            }
        }

        if(!valid) return;

        for(int i = 0; i < slots.length; i++)
        {
            for (int j = 0; j < slots[i].length; j++)
            {
                if (slots[i][j] != null)
                {
                    numDays++;
                    break;
                }
            }
            int breaksConsecutive = -1;
            int highestBreaks = 0;
            int classesConsecutive = -1;
            int highestClasses = 0;
            
            for (int j = 0; j < slots[i].length; j++)
            {
                if (slots[i][j] == null && breaksConsecutive != -1)
                {
                    breaksConsecutive++;
                } else if (slots[i][j] != null)
                {
                    if(latestStart <= j  && breaksConsecutive == -1) latestStart = j;
                    if (breaksConsecutive >= highestBreaks) highestBreaks = breaksConsecutive;
                    if(breaksConsecutive != -1) totalBreak += breaksConsecutive;
                    breaksConsecutive = 0;
                    if(earliestStart >= j || earliestStart == -1) earliestStart = j;
                    if(latestFinish <= j + 1) latestFinish = j + 1;
                }

                if(slots[i][j] != null && classesConsecutive != -1)
                {
                    classesConsecutive++;
                } else if (slots[i][j] == null)
                {
                    if (classesConsecutive >= highestClasses) highestClasses = classesConsecutive;
                    classesConsecutive = 0;
                }
            }
            if (highestBreaks >= longestBreak) longestBreak = highestBreaks;
            if (highestClasses >= longestConsecutive) longestConsecutive = highestClasses;
        }

        //score = (500 *(1/Math.pow(numDays, 3)) * 1 / (longestBreak + 0.5)) - totalBreak - longestConsecutive;
        score = (46 / (numDays - 5.1) + 112) - Math.pow(Math.E, ((2 * longestBreak) - 2)) - Math.pow((totalBreak / 2), 3) - Math.pow((2 * (Math.pow(longestConsecutive, 2) / 10 - 1)), 2) - Math.pow(earliestStart / 1.5 -7, 2) - Math.pow(latestStart / 1.5 -7, 2) - Math.pow(Math.E, latestFinish - 15) / 2;

        if(valid) this.score = score;
    }

    public double getScore()
    {
        return score;
    }

    public void printVariant()
    {
            for (int j = 0; j < variant.length; j++)
            {
                System.out.print(" [" + variant[j] + "]");
            }

            System.out.print("\n");
    }

    public boolean getValid()
    {
        return valid;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNormalisedScore(double normalisedScore)
    {
        this.normalisedScore = normalisedScore;
    }

    public double getNormalisedScore()
    {
        return normalisedScore;
    }

    public Subject[] getSubjects()
    {
        return subjects;
    }

    public void setSubjects(Subject[] subjects)
    {
        this.subjects = subjects;
    }
}