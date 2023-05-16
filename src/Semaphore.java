public class Semaphore
{
    private int Flag;
    private final int CountofStates;

    Semaphore(int CountofStates)
    {
        this.CountofStates = CountofStates;
    }

    public int getFlag()
    {
        return Flag;
    }

    public int getCountofStates()
    {
        return CountofStates;
    }

    public void setFlag(int flag)
    {
        if(flag <= (CountofStates - 1))
        {
            Flag = flag;
        }
        else
        {
            throw new RuntimeException("Semaphore out of range :: "+ flag + " | " + CountofStates);
        }
    }

    public void Signal()
    {
        if(Flag <= (CountofStates - 2))
        {
            Flag++;
        }
        else
        {
            throw new RuntimeException("Semaphore out of range :: "+ Flag + " | " + CountofStates);
        }
    }

    public void Wait()
    {
        if(Flag >= 1)
        {
            Flag--;
        }
        else
        {
            throw new RuntimeException("Semaphore out of range :: "+ Flag + " | " + CountofStates);
        }
    }

    public void SetMutexTrue()
    {
        if (CountofStates == 2)
        {
            Flag = 1;
        }
        else
        {
            throw new RuntimeException("State count isn't two");
        }
    }

    public void SetMutexFalse()
    {
        if (CountofStates == 2)
        {
            Flag = 0;
        }
        else
        {
            throw new RuntimeException("State count isn't two");
        }
    }

    public boolean IsMutexTrue()
    {
        if (CountofStates == 2)
        {
            return Flag == 1;
        }
        else
        {
            throw new RuntimeException("State count isn't two");
        }
    }

}
