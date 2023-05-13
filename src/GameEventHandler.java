import java.util.Vector;

public class GameEventHandler
{
    private final Vector<GameEvent> Games = new Vector<>();
    MouseInputListener mouseInputListenerReference;
    GameEventHandler(MouseInputListener mouseListener)
    {
         this.mouseInputListenerReference = mouseListener;
    }

    public void AddGameEvent()
    {
       if(!Games.isEmpty())
       {
           Games.clear();
       }
       Games.add(new GameEvent(mouseInputListenerReference));
    }

    public GameEvent GetGameEvent(int index)
    {
        return Games.get(index);
    }
}
