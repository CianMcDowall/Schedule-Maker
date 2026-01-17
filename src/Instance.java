public class Instance {
    private int day;
    private String dayName;
    private int hour;
    private int length;
    private Class type;

    public Instance(int day, int hour, int length)
    {
        this.day = day;
        this.hour = hour;
        this.length = length;

        dayName = Instance.dayName(day);
    }

    public static String dayName(int day)
    {
        switch (day) {
            case 1:     return "Monday";
            case 2:     return "Tuesday";
            case 3:     return "Wednesday";
            case 4:     return "Thursday";
            case 5:     return "Friday";
            default:    return "Invalid";
        }
    }

    public int getDay()
    {
        return day;
    }

    public int getHour()
    {
        return hour;
    }

    public int getLength()
    {
        return length;
    }
    
    public String getDayName()
    {
        return dayName;
    }

    public String toString()
    {
        return dayName + " " + hour + ":00 for " + length + " hours";
    }

    public static Instance[] createInstances(int[] days, int[] times, int[] lengths)
    {
        if((days.length == times.length) && times.length == lengths.length)
        {
            Instance[] instances = new Instance[days.length];
            for (int i = 0; i < days.length; i++)
            {
                instances[i] = new Instance(days[i], times[i], lengths[i]);
            }

            return instances;
        } else {
            return null;
        }

    }

    public Class getType()
    {
        return type;
    }

    public void setType(Class type)
    {
        this.type = type;
    }
}