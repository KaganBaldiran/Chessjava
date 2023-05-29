import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class UI extends JPanel
{

    Button CreateGameButton = new Button("CREATE GAME");
    Button JoinGameButton = new Button("JOIN GAME");
    Button DisconnectButton = new Button("DISCONNECT");

    Button CopyButton;
    JFrame frame_reference;
    Math.Vec2<Float> BoardSize = new Math.Vec2<>(0.0f,0.0f);
    Math.Vec2<Float> BoardLocation = new Math.Vec2<>(0.0f,0.0f);

    Math.Vec2<Float> UIsize = new Math.Vec2<>(0.0f,0.0f);

    public static class Button extends JButton
    {
        Semaphore Pressed = new Semaphore(2);

        Button(String Name)
        {
            super(Name);
            Pressed.SetMutexFalse();
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Pressed.SetMutexTrue();
                }
            });
        }
        Button(ImageIcon image)
        {
            super(image);
            Pressed.SetMutexFalse();
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    Pressed.SetMutexTrue();
                }
            });
        }

    }
    public static class Text
    {
        Graphics graphics;
        Font font;
        Color color;
        Math.Vec2<Integer> position = new Math.Vec2<>(0,0);

        Text(Graphics graphics , String FontName , int Style , int fontSize , Color Color)
        {
            this.graphics = graphics;
            this.color = Color;

            font = new Font("Arial", Font.BOLD, 24);

            String text = "Hello, Java!";

        }
        public void SetPosition(int x , int y)
        {
            position.SetValues(x,y);
        }
        public void DrawText(String text)
        {
            graphics.setFont(this.font);
            graphics.setColor(this.color);
            graphics.drawString(text, position.x, position.y);
        }
    }


    public static class UIcomponents
    {
        public BufferedImage componentImage;
        public Graphics2D bufferedGraphics;
        UIcomponents(int width , int height , int imagetype)
        {
            this.componentImage = new BufferedImage(width,height,imagetype);
            bufferedGraphics = componentImage.createGraphics();
        }
        public void drawUIcomponent(Graphics g , int x , int y , int width , int height , ImageObserver observer)
        {
            bufferedGraphics = componentImage.createGraphics();
            g.drawImage(this.componentImage,x,y,width,height,observer);

        }
    }

    public static class TextField extends JPanel {
        private final JTextField textField;
        private final JPopupMenu popupMenu;


        public TextField(String text , JFrame currentFrame , boolean editable) {
            textField = new JTextField(text);
            textField.setEditable(editable);
            textField.setLocation(300,300);
            textField.setSize(100,30);

            popupMenu = new JPopupMenu();
            JMenuItem cutItem = new JMenuItem("Cut");
            JMenuItem copyItem = new JMenuItem("Copy");
            JMenuItem pasteItem = new JMenuItem("Paste");

            cutItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textField.cut();
                }
            });

            copyItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textField.copy();
                }
            });

            pasteItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textField.paste();
                }
            });

            popupMenu.add(cutItem);
            popupMenu.add(copyItem);
            popupMenu.add(pasteItem);

            textField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(textField, e.getX(), e.getY());
                    }
                }
            });




            currentFrame.add(textField);

        }
        public void copyToClipboard() {
            StringSelection selection = new StringSelection(textField.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
        }

        public void Copy()
        {
            textField.copy();
        }
        public void SetText(String text)
        {
            textField.setText(text);
        }
        public void SetSize(int width , int height)
        {
            textField.setSize(width, height);
        }
        public void SetPosition(int x , int y)
        {
            textField.setLocation(x, y);
        }
        public String GetText()
        {
            return textField.getText();
        }
        public void SetEditable(boolean flag)
        {
            textField.setEditable(flag);
        }
    }

    public static class SliderBar extends JPanel
    {
        public CollisionBox collisionBox;
        Math.Vec4<Integer> Drawingattrib = new Math.Vec4<>();
        Color color;
        public int Shape;
        public final static int ROUND = 10000;
        int roundness = 0;
        public final static int SHARP = 20000;
        MouseInputListener mouselistenerReference;

        Math.Vec2<Double> ClickkedInitialPosition = new Math.Vec2<>();

        boolean initialized = false;

        Math.Vec2<Integer> initialXvalues = new Math.Vec2<>(0,0);

        Math.Vec4<Integer> ComponentSizes = new Math.Vec4<>(0,0,0,0);

        SliderBar(int Shape , MouseInputListener Mouselistener)
        {
            collisionBox = new CollisionBox();
            this.Shape = Shape;
            this.color = new Color(0x000000);
            this.mouselistenerReference = Mouselistener;
        }
        SliderBar(int Shape , int roundness , MouseInputListener Mouselistener)
        {
            collisionBox = new CollisionBox();
            this.Shape = Shape;
            this.roundness = roundness;
            this.color = new Color(0x000000);
            this.mouselistenerReference = Mouselistener;
        }

        int slided_x_position = 0;
        int permanent_slide_position = 0;
        public void SetCollisionAttribs(int x , int y , int width , int height )
        {
            this.collisionBox.SetValues(x, y, width, height);
            initialXvalues.x = x;
        }
        public void SetDrawingAttribs(int x , int y , int width , int height , Color color )
        {
            this.Drawingattrib.SetValues(x ,y,width,height);
            initialXvalues.y = x;
            this.color = color;
        }

        Math.Vec2<Integer> UnchangingComponentSizes = new Math.Vec2<>(0,0);

        int TotalSlideX = 0;
        int prevslidedposition = 0;

        boolean resetprevslidedposition = false;

        public void Update(float scalecoeff , int BoarddSizeX , Dimension screenSize, int BoardLocationX)
        {
            prevslidedposition = slided_x_position;
            Double ScaleAmount = SlideAmount(scalecoeff);

            if (ScaleAmount != 0.0)
            {
                slided_x_position = ScaleAmount.intValue();
            }
            else
            {
                slided_x_position = 0;
            }

            TotalSlideX += ((slided_x_position - prevslidedposition));
            this.Drawingattrib.x = (int) (Drawingattrib.x + (TotalSlideX * scalecoeff));
        }

        float ScaleCoefBoardSizeLeftCom = 0.0f;

        float UnchangingScaleSlideBoardRatio = 0.0f;

        public void CalculateComponentSizes(int BoardSize_x , int BoardLocationX , Dimension screenSize , float scalecoeff)
        {
            ComponentSizes.x = BoardSize_x - Drawingattrib.x;
            ComponentSizes.z = (Drawingattrib.x-BoardLocationX);

            ScaleCoefBoardSizeLeftCom = ComponentSizes.z.floatValue() / BoardSize_x;

            UnchangingComponentSizes.x = (int) (TotalSlideX + (screenSize.width * 0.82f)) ;
            UnchangingComponentSizes.y = (int) (screenSize.width - (TotalSlideX + (screenSize.width * 0.82f)));

        }

        public void ReverseUpdate()
        {
            collisionBox.x = collisionBox.x - slided_x_position;
            this.Drawingattrib.x = this.Drawingattrib.x - slided_x_position;
        }

        public boolean IsClicked()
        {
            return this.collisionBox.CheckCollisionBoxMouse(this.mouselistenerReference.GetMousePos());
        }

        boolean pressed = false;
        boolean released = false;

        double last_size = 0.0;


        public Double SlideAmount(float scalecoeff)
        {
            if(this.mouselistenerReference.isReleased(MouseEvent.BUTTON1))
            {
                if(pressed)
                {
                    prevslidedposition = 0;
                }
                released = true;
                pressed = false;
            }

            if(IsClicked() && this.mouselistenerReference.isClicked(MouseEvent.BUTTON1) && released)
            {
                resetprevslidedposition = true;
                ClickkedInitialPosition.SetValues(this.mouselistenerReference.GetMousePos());
                pressed = true;
                released = false;
            }

            if (ClickkedInitialPosition.x != null && pressed && !released)
            {
                if (this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x != 0.0)
                {
                    last_size = this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x;
                    return this.mouselistenerReference.GetMousePos().x - ClickkedInitialPosition.x;
                }
            }

            return 0.0;

        }
        @Override
        protected void paintComponent(Graphics g) {

            if(!IsClicked())
            {
                g.setColor(color);
            }
            else
            {
                g.setColor(color.brighter());
            }

            if(Shape == ROUND)
            {
                if(roundness <= 0)
                {
                    throw new RuntimeException("Roundness value is too small :: SliderBar");
                }

                g.fillRoundRect(Drawingattrib.x ,Drawingattrib.y , Drawingattrib.z, Drawingattrib.w, roundness , roundness);
            }
            else if(Shape == SHARP)
            {
                g.fillRect(Drawingattrib.x ,Drawingattrib.y , Drawingattrib.z, Drawingattrib.w);

            }
        }
    }

    public static class MainMenu
    {
        public JMenuBar menuBar;
        public JMenu filemenu;
        public JMenuItem filemenuitem;

        MainMenu(JFrame frame)
        {
            menuBar = new JMenuBar();
            filemenu = new JMenu("File");
            filemenuitem = new JMenuItem("Open");
            filemenu.add(filemenuitem);
            menuBar.add(filemenu);
            frame.setJMenuBar(menuBar);
        }

    }

    public static class DropDownMenu
    {
        String[] options;
        JComboBox<String> comboBox;
        DropDownMenu(JFrame frame)
        {
            options = new String[]{"LAN Online", "WAN Online", "Offline"};
            comboBox = new JComboBox<String>(options);
            comboBox.setBounds(50, 50, 200, 30);
            comboBox.setSelectedIndex(0);
            frame.add(comboBox);
        }
        public int GetSelectedItemIndex()
        {
            return comboBox.getSelectedIndex();
        }
        public String GetSelectedItem()
        {
            return options[comboBox.getSelectedIndex()];
        }
        public void SetPosition(int x , int y)
        {
            comboBox.setLocation(x , y);
        }
        public void SetSize(int width , int height)
        {
            comboBox.setSize(width, height);
        }
    }

    SliderBar UIsliderBar;
    float scale_coeffic = 0;
    boolean button1Pressed = false;
    MainMenu gamemenu;
    DropDownMenu CreateGameMenu;
    TextField ReadOnlyField;


    UI(JFrame frame ,MouseInputListener Mouselistener) {

        CreateGameButton.setSize(100, 50);
        CreateGameButton.setFont(new Font("Arial", Font.PLAIN, 9));
        CreateGameButton.setLocation(0, 0);
        CreateGameButton.setBackground(GraphicHandler.HexToRgba("#9CF3F5"));

        CreateGameButton.addMouseListener(Mouselistener);

        JoinGameButton.setSize(100, 50);
        JoinGameButton.setFont(new Font("Arial", Font.PLAIN, 9));
        JoinGameButton.setLocation(0, 0);
        JoinGameButton.setBackground(GraphicHandler.HexToRgba("#9CF3F5"));

        JoinGameButton.addMouseListener(Mouselistener);

        ImageIcon copyAllPng = new ImageIcon("resources/copy.png");
        CopyButton = new Button(copyAllPng);
        CopyButton.setSize(50, 50);
        CopyButton.setFont(new Font("Arial", Font.PLAIN, 9));
        CopyButton.setLocation(0, 0);
        CopyButton.setBackground(GraphicHandler.HexToRgba("#9CF3F5"));

        CopyButton.addMouseListener(Mouselistener);

        frame_reference = frame;
        frame_reference.add(CreateGameButton);
        frame_reference.add(JoinGameButton);
        frame_reference.add(CopyButton);

        UIsliderBar = new SliderBar(SliderBar.ROUND , 7,Mouselistener);
        ReadOnlyField = new TextField("" , frame_reference ,true);
        gamemenu = new MainMenu(frame_reference);
        CreateGameMenu = new DropDownMenu(frame_reference);
    }

    Dimension ScreenSize = new Dimension(0,0);

    public void UpdateBoardAttribs(Math.Vec2<Float> Location , float scale_coeffi , Dimension screensize)
    {
        this.BoardSize.SetValues((float) (Board.SQUARE_SIZE_non_GUI * 8), (float) (Board.SQUARE_SIZE_non_GUI * 8));
        this.BoardLocation = Location;
        this.scale_coeffic = scale_coeffi;
        ScreenSize.setSize(screensize);
    }

    public void setUIsize(int FrameWidth , int FrameHeight , float BoardWidth , float BoardHeight) {
        this.UIsize.SetValues(FrameWidth - BoardWidth, BoardHeight);
    }

    public void CopyToClipBoard(String StringToCopy)
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(StringToCopy);
        clipboard.setContents(stringSelection, null);
        System.out.println("String copied to clipboard! :: " + StringToCopy);
    }

    public void Update()
    {
            UIsliderBar.SetDrawingAttribs((int) (BoardLocation.x.intValue() + BoardSize.x ) ,
                    BoardLocation.y.intValue(),(int)(UIsize.x * 0.10f),
                    UIsize.y.intValue(),
                    GraphicHandler.HexToRgba("#4A4D46"));

            UIsliderBar.SetCollisionAttribs((int) ((BoardLocation.x.intValue() + BoardSize.x ) + ((UIsize.x * 0.10f)/2) + (UIsliderBar.TotalSlideX * scale_coeffic) ),
                    (int) ((BoardLocation.y.intValue() + BoardSize.y ) + ((UIsize.y * 0.10f)/2)),
                    (int)(UIsize.x * 0.10f),
                    2* UIsize.y.intValue());


        CreateGameButton.setLocation((int) (BoardLocation.x.intValue() + BoardSize.x ), (int) (BoardLocation.y + BoardSize.y / 2));

        CreateGameButton.setPreferredSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));
        CreateGameButton.setSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));

        CopyButton.setLocation((int) ((UIsliderBar.ComponentSizes.z + BoardLocation.x + (UIsliderBar.ComponentSizes.x * 0.10)) + (105 * scale_coeffic)), (int) (BoardLocation.y + (BoardSize.y * 0.39f)));

        CopyButton.setPreferredSize(new Dimension((int) (50 * scale_coeffic),  (int) (50 * scale_coeffic)));
        CopyButton.setSize(new Dimension((int) (50 * scale_coeffic),  (int) (50 * scale_coeffic)));


        JoinGameButton.setLocation((int) (BoardLocation.x.intValue() + BoardSize.x ), (int) (BoardLocation.y + BoardSize.y / 1.5));

        JoinGameButton.setPreferredSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));
        JoinGameButton.setSize(new Dimension((int) (100 * scale_coeffic),  (int) (50 * scale_coeffic)));

        CreateGameMenu.SetPosition((int) (UIsliderBar.ComponentSizes.z + BoardLocation.x + (UIsliderBar.ComponentSizes.x * 0.10)), (int) (BoardLocation.y + (BoardSize.y * 0.20f)));
        CreateGameMenu.SetSize((int) (200 * scale_coeffic), (int) (30 * scale_coeffic));

        ReadOnlyField.SetPosition((int) (UIsliderBar.ComponentSizes.z + BoardLocation.x + (UIsliderBar.ComponentSizes.x * 0.10)), (int) (BoardLocation.y + (BoardSize.y * 0.40f)));
        ReadOnlyField.SetSize((int) (100 * scale_coeffic), (int) (30 * scale_coeffic));

        UIsliderBar.Update(scale_coeffic , BoardSize.x.intValue() + (int)((BoardSize.x/9)*2), ScreenSize , BoardLocation.x.intValue());

        UIsliderBar.CalculateComponentSizes(BoardSize.x.intValue() + (int)((BoardSize.x/9)*2) + BoardLocation.x.intValue() , BoardLocation.x.intValue() ,ScreenSize,scale_coeffic);

        if (CopyButton.Pressed.IsMutexTrue())
        {
            ReadOnlyField.copyToClipboard();
            CopyButton.Pressed.SetMutexFalse();
        }

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIsliderBar.paintComponent(g);
    }
}
