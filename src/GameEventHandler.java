import java.io.IOException;
import java.util.Vector;

public class GameEventHandler
{
    private Vector<GameEvent> Games = new Vector<>();

    MouseInputListener mouseInputListenerReference;

    GameEventHandler(MouseInputListener mouseListener)
    {
         this.mouseInputListenerReference = mouseListener;
    }

    public void AddGameEvent() throws IOException
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
