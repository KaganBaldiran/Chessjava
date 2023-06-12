import java.util.Vector;

public class GameEventHandler
{
    public final static int LAN_GAME_EVENT = 3333;
    public final static int OFFLINE_GAME_EVENT = 4444;
    public final static int WAN_GAME_EVENT = 5555;
    private final Vector<GameEvent> Games = new Vector<>();
    MouseInputListener mouseInputListenerReference;
    GameEventHandler(MouseInputListener mouseListener)
    {
         this.mouseInputListenerReference = mouseListener;
    }


    public void AddGameEvent(int GameType , Boolean GameUsage , UI currentUI , String ExternalIP)
    {
        if(!Games.isEmpty())
        {
            Games.clear();
        }
        if(GameType == LAN_GAME_EVENT)
        {
            Games.add(new GameEvent.LANGameEvent(mouseInputListenerReference,GameUsage,currentUI , ExternalIP));
        }

    }

    public void DeleteGameEvents()
    {
        if(!Games.isEmpty())
        {
            Games.clear();
        }
    }



    public<type> type GetGameEvent(int index)
    {
        return (type)Games.get(index);
    }

    public int GetGameEventCount()
    {
        return Games.size();
    }

    public boolean IsThereGame()
    {
        return !Games.isEmpty();
    }
}
