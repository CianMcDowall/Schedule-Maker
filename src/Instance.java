public class Instance {
    private int day;
    private String dayName;
    private int hour;
    private int length;

    public Instance(int day, int hour, int length)
    {
        this.day = day;
        this.hour = hour;
        this.length = length;

        switch (day) {
            case 1:     dayName = "Monday";
                        break;
            case 2:     dayName = "Tuesday";
                        break;
            case 3:     dayName = "Wednesday";
                        break;
            case 4:     dayName = "Thursday";
                        break;
            case 5:     dayName = "Friday";
                        break;
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
}