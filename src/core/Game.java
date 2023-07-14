package core;

import org.bitlet.weupnp.GatewayDevice;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static java.lang.System.exit;


public class Game extends JPanel implements Runnable
{

    Canvas current_canvas;
    Board chessBoard;
    InputHandler input_handler;
    BufferedImage bufferedImage;
    boolean isRunning;
    MouseInputListener mouseListener;
    Dimension ScreenSize;
    boolean allow_click[];
    Math.Vec2<Float> FBO_position = new Math.Vec2<>();
    Math.Vec2<Float> Boundry_size = new Math.Vec2<>();
    javax.swing.JFrame frame;
    UI.UIcomponents leftComponent;
    UI ui;


    GameEventHandler Games;
    Color LeftComponentColor = GraphicHandler.HexToRgba("#F5E7E4");
    Math.Pair<GatewayDevice , String> PortMapping = new Math.Pair<>();
    boolean DeleteGameEvent = false;
    Color RightComponentColor = GraphicHandler.HexToRgba("#E4F5EF");

    UI.Text ConnectionState;
    UI.Text PlayerNameOnThisMachine;
    UI.Text PlayerNameOnOpponentMachine;
    int GameCount = 0;


    UI.GameSettingsDialog GameSettingDialog;


    Game() {

        chessBoard = new Board();

        frame = new javax.swing.JFrame("Main.Chess");
        MouseInputListener mouseListener = new MouseInputListener(frame);
        this.mouseListener = mouseListener;

        frame.addMouseListener(mouseListener);

        ScreenSize = Toolkit.getDefaultToolkit().getScreenSize().getSize();

        ScreenSize.setSize(ScreenSize.getHeight() * 0.90f + ScreenSize.getHeight() * 0.20f , ScreenSize.getHeight() * 0.90f);


        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);

        this.input_handler = new InputHandler();
        this.isRunning = true;
        this.allow_click = new boolean[4];


        frame.setFocusable(true);

        this.ui = new UI(frame,mouseListener);

        current_canvas = new Canvas();

        current_canvas.setSize(new Dimension((int)ScreenSize.getWidth(), (int)ScreenSize.getHeight()));
        current_canvas.setFocusable(true);
        current_canvas.requestFocus();


        frame.addKeyListener(input_handler);

        frame.add(current_canvas);

        current_canvas.addMouseListener(mouseListener);

        frame.pack();

        Boundry_size.SetValues((float) (frame.getWidth()- ScreenSize.getWidth()), (float) (frame.getHeight() - ScreenSize.getHeight()));

        current_canvas.setSize(new Dimension((int)frame.getContentPane().getWidth(), (int)frame.getContentPane().getHeight()));

        current_canvas.createBufferStrategy(3);

        bufferedImage = new BufferedImage(this.current_canvas.getWidth(), this.current_canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

        System.out.println("this.current_canvas.getWidth() X :  " + this.current_canvas.getWidth() + " Y: " + this.current_canvas.getHeight());

        this.leftComponent = new UI.UIcomponents((int) (ScreenSize.getWidth() * 0.82f), (int) (ScreenSize.getWidth() * 0.82f),BufferedImage.TYPE_INT_ARGB);

        Games = new GameEventHandler(mouseListener);

        this.ConnectionState = new UI.Text(bufferedImage.getGraphics(),"Arial" ,Font.BOLD, 24 , Color.black);
        this.PlayerNameOnThisMachine = new UI.Text(bufferedImage.getGraphics(),"Arial" ,Font.BOLD, 24 , Color.black);
        this.PlayerNameOnOpponentMachine = new UI.Text(bufferedImage.getGraphics(),"Arial" ,Font.BOLD, 24 , Color.black);

        this.GameSettingDialog = new UI.GameSettingsDialog(frame);
        GameSettingDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if(Games.IsThereGame())
                {
                    ((GameEvent.LANGameEvent)Games.GetGameEvent(Games.GetGameEventCount() - 1)).Client.Disconnect();

                    if(ui.PortMapping.isSelected())
                    {
                        if(((GameEvent.LANGameEvent) Games.GetGameEvent(Games.GetGameEventCount() - 1)).Server != null) {
                            KryonetServer.DeletePortMapping(PortMapping.first, 54555, "TCP");
                        }
                        else {
                            KryonetClient.DeletePortMapping(PortMapping.first, 54555, "TCP");
                        }
                    }
                }

                isRunning = false;

            }
        });

    }


    @Override
    public void run()
    {
        while (isRunning)
        {
            if(frame.isFocused()) {

                if (ui.CreateGameButton.Pressed.IsMutexTrue()) {

                    if(ui.PortMapping.isSelected())
                    {
                        try {
                            PortMapping = KryonetServer.PortMapping(54555 , 54555 , "TCP" , DeleteGameEvent);
                        } catch (IOException | ParserConfigurationException | SAXException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    while(Objects.equals(GameSettingDialog.GetPlayerName(), ""))
                    {
                        this.GameSettingDialog.setVisible(true);
                    }

                    Games.AddGameEvent(GameEventHandler.LAN_GAME_EVENT, true, ui , PortMapping.second);

                    if(DeleteGameEvent)
                    {
                        Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).DeleteGameEvent.SetMutexTrue();
                        DeleteGameEvent = false;
                    }

                    if (Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).DeleteGameEvent.IsMutexTrue()) {
                        Games.DeleteGameEvents();
                    } else {
                        if (Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).Server != null) {

                            if(PortMapping.second == null)
                            {
                                ui.ReadOnlyField.SetText(Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).Server.GameLinkInternal);
                            }
                            else
                            {
                                ui.ReadOnlyField.SetText(Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).Server.GameLinkExternal);
                            }

                            ui.ReadOnlyField.SetEditable(false);
                        }

                        ui.JoinGameButton.setVisible(false);
                        ui.CreateGameButton.setVisible(false);
                        ui.PortMapping.setVisible(false);
                        ui.CreateGameMenu.comboBox.setVisible(false);
                        ui.DisconnectButton.setVisible(true);
                    }

                    ui.CreateGameButton.Pressed.SetMutexFalse();
                }

                if (ui.JoinGameButton.Pressed.IsMutexTrue()) {

                    if(ui.PortMapping.isSelected())
                    {
                        try {
                            PortMapping = KryonetClient.PortMapping(54555 , 54555 , "TCP" , DeleteGameEvent);
                        } catch (IOException | ParserConfigurationException | SAXException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    while(Objects.equals(GameSettingDialog.GetPlayerName(), ""))
                    {
                        this.GameSettingDialog.setVisible(true);
                    }

                    Games.AddGameEvent(GameEventHandler.LAN_GAME_EVENT, false, ui , PortMapping.second);

                    if(DeleteGameEvent)
                    {
                        Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).DeleteGameEvent.SetMutexTrue();
                        DeleteGameEvent = false;
                    }

                    if (Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).DeleteGameEvent.IsMutexTrue()) {
                        Games.DeleteGameEvents();
                    } else {
                        ui.JoinGameButton.setVisible(false);
                        ui.CreateGameButton.setVisible(false);
                        ui.PortMapping.setVisible(false);
                        ui.CreateGameMenu.comboBox.setVisible(false);
                        ui.DisconnectButton.setVisible(true);

                    }

                    ui.JoinGameButton.Pressed.SetMutexFalse();
                }

                if(ui.DisconnectButton.isVisible()) {
                    if (ui.DisconnectButton.Pressed.IsMutexTrue()) {
                        Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).Client.Disconnect();
                        Games.DeleteGameEvents();

                        ui.DisconnectButton.Pressed.SetMutexFalse();
                        ui.JoinGameButton.setVisible(true);
                        ui.CreateGameButton.setVisible(true);
                        ui.CreateGameMenu.comboBox.setVisible(true);
                        ui.DisconnectButton.setVisible(false);
                        ui.ReadOnlyField.setVisible(true);
                    }
                }

                if (Games.IsThereGame()) {
                    Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).GameLoop(GameSettingDialog.GetPlayerName());
                }

                BufferStrategy bufferstrategy = current_canvas.getBufferStrategy();
                Graphics graphics = bufferstrategy.getDrawGraphics();

                Graphics2D bufferedGraphics = bufferedImage.createGraphics();
                bufferedGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                graphics.setColor(Color.WHITE);
                graphics.fillRect(0, 0, current_canvas.getWidth(), current_canvas.getHeight());

                float final_scale_coeffi = GraphicHandler.GetScreenScaleCoefficient(frame, this.current_canvas, this.ScreenSize);

                bufferedGraphics.setColor(RightComponentColor.darker());
                bufferedGraphics.fillRect((int) (ui.UIsliderBar.UnchangingComponentSizes.x), 0, ui.UIsliderBar.UnchangingComponentSizes.y, (int) ScreenSize.getHeight());

                bufferedGraphics.setColor(RightComponentColor);
                bufferedGraphics.fillRoundRect((int) (ui.UIsliderBar.UnchangingComponentSizes.x + ((ui.UIsliderBar.UnchangingComponentSizes.y * 0.10f) / 2)), 10, (int) (ui.UIsliderBar.UnchangingComponentSizes.y * 0.90f), (int) (bufferedImage.getHeight() * 0.980f), 30, 30);



                if (Games.IsThereGame())
                {
                    //System.out.println("IS THERE GAME: " + Games.IsThereGame());

                    if (((GameEvent.LANGameEvent) Games.GetGameEvent(Games.GetGameEventCount() - 1)).Client.ConnectionState.equalsIgnoreCase("CONNECTED")) {
                        Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).DrawGame(leftComponent.bufferedGraphics);
                    } else if (((GameEvent.LANGameEvent) Games.GetGameEvent(Games.GetGameEventCount() - 1)).Client.ConnectionState.equalsIgnoreCase("DISCONNECTED")) {
                        bufferedGraphics.setColor(piece.TRANSPARENT_LIGHT_GRAY);
                        bufferedGraphics.fillRect(0,
                                (int) ((bufferedImage.getHeight() - ui.UIsliderBar.UnchangingComponentSizes.x) / 2),
                                ui.UIsliderBar.UnchangingComponentSizes.x,
                                ui.UIsliderBar.UnchangingComponentSizes.x);
                    }
                }
                else
                {
                    //System.out.println("NO GAME: " + Games.IsThereGame());

                    bufferedGraphics.setColor(piece.TRANSPARENT_LIGHT_GRAY);
                    bufferedGraphics.fillRect(0,
                            (int) ((bufferedImage.getHeight() - ui.UIsliderBar.UnchangingComponentSizes.x) / 2),
                            ui.UIsliderBar.UnchangingComponentSizes.x,
                            ui.UIsliderBar.UnchangingComponentSizes.x);
                }

                float scaledWidth = bufferedImage.getWidth() * final_scale_coeffi;
                float scaledHeight = bufferedImage.getHeight() * final_scale_coeffi;

                leftComponent.drawUIcomponent(bufferedGraphics,
                        0,
                        (int) ((bufferedImage.getHeight() - ui.UIsliderBar.UnchangingComponentSizes.x) / 2),
                        ui.UIsliderBar.UnchangingComponentSizes.x,
                        ui.UIsliderBar.UnchangingComponentSizes.x,
                        current_canvas);


                FBO_position.SetValues((current_canvas.getWidth() / 2) - (scaledWidth / 2), current_canvas.getHeight() / 2 - (float) (ScreenSize.getHeight() * final_scale_coeffi) / 2);

                if (Games.IsThereGame()) {
                    Games.<GameEvent.LANGameEvent>GetGameEvent(Games.GetGameEventCount() - 1).UpdateBoardUtilities(ScreenSize, final_scale_coeffi, FBO_position, bufferedImage, ui);
                } else {
                    Board.UpdateSquareSize(ScreenSize.height * final_scale_coeffi);
                    Board.UpdateSQUARESIZEUI(ui.UIsliderBar.ComponentSizes.z);
                }

                ui.setUIsize(((int) scaledWidth), (int) scaledHeight, ScreenSize.height * final_scale_coeffi, ScreenSize.height * final_scale_coeffi);

                if (Games.IsThereGame()) {
                    ConnectionState.SetPosition((int) (ScreenSize.width * 0.835f), 30);
                    ConnectionState.DrawText(((GameEvent.LANGameEvent) Games.GetGameEvent(Games.GetGameEventCount() - 1)).Client.ConnectionState);

                    PlayerNameOnThisMachine.SetPosition((int) (ScreenSize.width * 0.835f), 940);
                    PlayerNameOnThisMachine.DrawText(GameSettingDialog.GetPlayerName());

                    PlayerNameOnOpponentMachine.SetPosition((int) (ScreenSize.width * 0.835f), 50);
                    PlayerNameOnOpponentMachine.DrawText(((GameEvent.LANGameEvent) Games.GetGameEvent(Games.GetGameEventCount() - 1)).Client.OpponentPlayerName);
                }

                graphics.drawImage(bufferedImage, FBO_position.x.intValue(), FBO_position.y.intValue(), (int) scaledWidth, (int) scaledHeight, current_canvas);

                ui.UpdateBoardAttribs(FBO_position, final_scale_coeffi, ScreenSize);
                ui.Update();

                ui.paintComponent(graphics);

                bufferstrategy.show();
                graphics.dispose();

                bufferedGraphics.setBackground(LeftComponentColor);
                bufferedGraphics.clearRect(0, 0, 3000, 4000);

                frame.revalidate();
                frame.repaint();
            }
        }

        FileHandler.WriteGameDataToJSON("GameData","GameData" , String.valueOf(Integer.parseInt(FileHandler.ReadDataFromJSONFile("GameData","GameData" ,"GameCount")) + 1), "GameCount");
        System.out.println("Main.Game Terminated");
        frame.dispose();
        exit(1);
    }

}
