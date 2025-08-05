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
}