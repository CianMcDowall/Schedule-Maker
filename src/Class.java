public class Class {
    private Instance[] options;
    private Instance chosen;
    private String type;
    private Subject subject;

    public Class(Instance[] options, Subject subject, String type)
    {
        this.subject = subject;
        this.type = type;
        this.options = options;

        if(options.length == 1)
        {
            chosen = options[0];
        }
    }

    public Class(Instance[] options, String type)
    {
        this.type = type;
        this.options = options;

        if(options.length == 1)
        {
            chosen = options[0];
        }
    }

    public Instance[] getOptions()
    {
        return options;
    }

    public Instance getChosen()
    {
        return chosen;
    }

    public void setChosen(Instance chosen)
    {
        this.chosen = chosen;
    }

    public String getType()
    {
        return type;
    }

    public Subject getSubject()
    {
        return subject;
    }

    public void setSubject(Subject subject)
    {
        this.subject = subject;
    }

    public String toString()
    {
        if (chosen != null)
        {
            return type + " >>> " + chosen;
        } else {
            return type + " time not chosen";
        }
    }
}