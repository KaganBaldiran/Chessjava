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

    public void AddGameEvent(int GameType)
    {
       if(!Games.isEmpty())
       {
           Games.clear();
       }
       if(GameType == LAN_GAME_EVENT)
       {
           Games.add(new GameEvent.LANGameEvent(mouseInputListenerReference));
       }

    }

    public GameEvent GetGameEvent(int index)
    {
        return Games.get(index);
    }
}
