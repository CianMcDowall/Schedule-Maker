import java.io.*;
import java.util.*;

public class Schedule {
    private Subject[] subjects;

    public Schedule()
    {
        subjects = new Subject[0];
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

    public void sort()
    {

    }

    public void exportSchedule(String fileName)
    {
        try {
            File f = new File(fileName + ".txt");
            PrintWriter p = new PrintWriter(f);
            String toExport = "";

            for (Subject s : subjects) 
            {
                toExport += s;
            }

            p.write(toExport);

            p.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

    public void importOptions(String fileName)
    {
        try {
            File f = new File(fileName + ".txt");
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
                Subject[] temp = new Subject[subjects.length + 1];

                for (int i = 0; i < subjects.length; i++)
                {
                    temp[i] = subjects[i];
                }

                temp[subjects.length] = subject;
                subjects = temp;

                data = "";
            }

            r.close();
        } catch (Exception e)
        {
            e.printStackTrace();   
        }
    }
}