public class Subject {
    private Class[] classes;
    private String name;
    private String classCode;

    public Subject(String name, String classCode, Class[] classes)
    {
        this.name = name;
        this.classCode = classCode;
        this.classes = classes;

        for (Class c : classes)
        {
            c.setSubject(this);
        }
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        String toReturn = "";

        for (int i = 0; i < classes.length; i++)
        {
            toReturn += "\n" + classes[i];
        }

        return name + " (" + classCode + ") {" + toReturn + "}\n\n";
    }

    public boolean isChosen()
    {
        boolean chosen = true;

        for (Class c : classes)
        {
            if(c.getChosen() == null)
            {
                chosen = false;
            }
        }

        return chosen;
    }
    
    public int getOptions()
    {
        int toReturn = 1;

        for (Class c : classes)
        {
            toReturn = toReturn * c.getOptions().length;
        }
        return toReturn;
    }

    public Instance[] getOption(int option)
    {
        //System.out.print("\n" + name + " - " + option + " - ");
        Instance[] toReturn = new Instance[classes.length];

        if (classes.length == 1)
        {
            toReturn[0] = classes[0].getInstance(option);
            //System.out.print("Class " + option + " - " + toReturn[0] + ", ");
        } else {
            int[] returnIndices = new int[classes.length];
            int index = 0;

            while (index < option)
            {
                for (int i = returnIndices.length - 1; i > 0; i--)
                {
                    returnIndices[returnIndices.length - 1]++;
                    index++;
                    if (returnIndices[i] >= classes[i].getOptions().length && i != 0)
                    {
                        returnIndices[i] = 0;
                        returnIndices[i - 1]++;
                    } else if(i == 0)
                    {
                        break;
                    }
                }
            }
            for (int i = 0; i < toReturn.length; i++)
            {
                toReturn[i] = classes[i].getOptions()[returnIndices[i]];
            }

            //for(int i = 0; i < toReturn.length; i++)
            //{
            //    System.out.print("Class " + returnIndices[i] + " - " + toReturn[i] + ", ");
            //}
        }

        return toReturn;
    }

    public Class[] getClasses()
    {
        return classes;
    }
}