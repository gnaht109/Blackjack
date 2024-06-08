import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

import java.util.Random;

public class Blackjack {
    Font font = new Font("Comic Sans MS", Font.PLAIN, 15);
    Font font1 = new Font("Comic Sans MS", Font.PLAIN, 30);
    final int frameW = 600;
    final int frameH = 600;
    final int cardW = 100;
    final int cardH = 140;
    
    Random random = new Random();

    ArrayList<Card> deck;

    public boolean isMenu = true;

    //dealer
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;
    Card hiddenCard;

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    public void drawCard(){
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce()?1:0;

        Card card = deck.remove(deck.size()-1);
        dealerSum +=card.getValue();
        dealerAceCount += card.isAce()?1:0;
        dealerHand.add(card);

        //System.out.println("Dealer hands");
        //System.out.println(hiddenCard);
        //System.out.println(dealerHand);
        //System.out.println(dealerSum);
        //System.out.println(dealerAceCount);

        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for(int i = 0;i<2;i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceCount += card.isAce()?1:0;
            playerHand.add(card);
        }

        //System.out.println("PLAYER HAND");
        //System.out.println(playerHand);
        //System.out.println(playerSum);
        //System.out.println(playerAceCount);
    }

    public void buildDeck(){
        deck = new ArrayList<Card>();
        String[] values = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types = {"S","C","D","H"};
        for(int i = 0;i < types.length;i++){
            for(int j = 0;j<values.length;j++){
                Card card = new Card(values[j],types[i]);
                deck.add(card);
            }
        }
        //("BUILD DECK");
        //System.out.println(deck);
    }

    public void shuffle(){
        for(int i=0;i<deck.size();i++){
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i,randomCard);
            deck.set(j,currCard);
        }
        //System.out.println("SHUFFLE THE DECK");
        //System.out.println(deck);
    }

    JPanel gamePanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try{
                //drawHiddenCard
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if(!stayButton.isEnabled() && !hitButton.isEnabled()){
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardW,cardH,null);
                //drawDealerHand
                for(int i = 0;i<dealerHand.size();i++){
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg,20 + cardW + 5 + (cardW + 5)*i,20,cardW,cardH,null);
                }
                //drawPlayerHand
                for(int i = 0;i<playerHand.size();i++){
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg,20 + (cardW + 5)*i,350,cardW,cardH,null);
                }
                if(!hitButton.isEnabled() && reduceDealerAce()>16 ){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    String message = "";
                    //System.out.println("STAY: ");
                    //System.out.println(dealerSum);
                    //System.out.println(playerSum);
                    if(playerSum > 21 && dealerSum > 21){
                        message = "No winner";
                    }
                    else if (playerSum > 21) {
                        message = "You Lose!";
                    }
                    else if (dealerSum > 21) {
                        message = "You Win!";
                    }
                    //both you and dealer <= 21
                    else if (playerSum == dealerSum) {
                        message = "No winner";
                    }
                    else if (playerSum > dealerSum) {
                        message = "You Win!";
                    }
                    else if (playerSum < dealerSum) {
                        message = "You Lose!";
                    }

                    g.setFont(font1);
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton hitButton = new JButton("HIT");
    JButton stayButton = new JButton("STAY");
    JButton resetButton = new JButton("Play Again");
    JFrame frame = new JFrame("Black Jack");
    JButton exitButton = new JButton("EXIT");

    public void setFrame(){
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameW,frameH);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        hitButton.setFocusable(false);
        stayButton.setFocusable(false);
        resetButton.setFocusable(false);
        exitButton.setFocusable(false);
        hitButton.setFont(font);
        stayButton.setFont(font);
        exitButton.setFont(font);
        resetButton.setFont(font);

        hitButton.setBackground(Color.WHITE);
        stayButton.setBackground(Color.WHITE);
        exitButton.setBackground(Color.WHITE);
        resetButton.setBackground(Color.WHITE);

        hitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                hitButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                hitButton.setBackground(Color.WHITE);
            }
        });
        stayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                stayButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                stayButton.setBackground(Color.WHITE);
            }
        });
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                resetButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                resetButton.setBackground(Color.WHITE);
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                exitButton.setBackground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e){
                exitButton.setBackground(Color.WHITE);
            }
        });

        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);

        gamePanel.setBackground(new Color(53,101,77));
        buttonPanel.setBackground(new Color(53,101,77));

        frame.add(buttonPanel,BorderLayout.SOUTH);
        frame.add(gamePanel);

        resetButton.setBounds((frameW/2)+ 110,(frameH/2)-95,150,30);
        exitButton.setBounds((frameW/2)+ 110,(frameH/2)-95 + 35 ,150,30);

        gamePanel.setLayout(null);
        gamePanel.add(resetButton);
        gamePanel.add(exitButton);
        resetButton.setEnabled(false);


        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.dispose();
                setmenu();
            }
        });

    }
    
    public void resetMet(){
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                resetButton.setEnabled(false);
                frame.dispose();
                runAgain();
            }
        });
    }

    public void hitMet(){
        hitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce()?1:0;
                playerHand.add(card);
                gamePanel.repaint();
                //System.out.println(playerSum);
                if(reducePlayerAce()>15){
                    stayButton.setEnabled(true);
                }
                if(reducePlayerAce()>21){
                    hitButton.setEnabled(false);
                    stayButton.doClick();
                    //System.out.println("Stop");
                }
            }
        });
        gamePanel.repaint();
    }

    public void stayMet(){
        stayButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                while(reduceDealerAce() < 17){
                    Card card = deck.remove(deck.size()-1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce()?1:0;
                    dealerHand.add(card);
                    //System.out.println("dealerSum:"+dealerSum);
                    //System.out.println("reduceDealerSum:"+reduceDealerAce());
                }
                resetButton.setEnabled(true);
                gamePanel.repaint();
                System.out.println("Game over");
            }
        });
        gamePanel.repaint();
    }

    public int reducePlayerAce(){
        while(playerSum > 21 && playerAceCount > 0){
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceCount > 0){
            dealerSum -=10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    public void start(){
        buildDeck();
        shuffle();
        drawCard();
    }

    JFrame frame1 = new JFrame("MENU");
    JButton playButton = new JButton("Play");
    JButton helpButton = new JButton("Help");
    JButton quitButton = new JButton("Quit");
    JPanel menuPanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        g.drawRect((frameW/2)-55,(frameH/2)-85,100,50);
        g.drawRect((frameW/2)-55,(frameH/2)-85+85,100,50);
        g.drawRect((frameW/2)-55,(frameH/2)-85+170,100,50);

        Font tilteFont = new Font("Comic Sans MS", Font.PLAIN, 70);
        setFont(tilteFont);
        g.setColor(Color.WHITE);
        g.drawString("BLACK JACK", (frameW/2) - 220, (frameH/2)-150);
    }
};

    public void setmenu(){
        frame1.setBackground(new Color(53,101,77));
        frame1.setSize(frameW,frameH);
        frame1.setVisible(true);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setResizable(false);
        menuPanel.setBackground(new Color(53,101,77));
        //g.drawRect((frameW/2)-50,(frameH/2)-85,100,50);
        //g.drawRect((frameW/2)-50,(frameH/2)-85+85,100,50);
        //g.drawRect((frameW/2)-50,(frameH/2)-85+170,100,50);
        menuPanel.setLayout(null);
        quitButton.setVisible(true);
        //menuPanel.add(playButton);
        menuPanel.add(helpButton);
        menuPanel.add(quitButton);
        playButton.setBounds((frameW/2)-55,(frameH/2)-85,100,50);
        helpButton.setBounds((frameW/2)-55,(frameH/2)-85+85,100,50);
        quitButton.setBounds((frameW/2)-55,(frameH/2)-85+170,100,50);
        playButton.setBackground(Color.WHITE);
        helpButton.setBackground(Color.WHITE);
        quitButton.setBackground(Color.WHITE);

        playButton.setFocusable(false);
        helpButton.setFocusable(false);
        quitButton.setFocusable(false);
        
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                playButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                playButton.setBackground(Color.WHITE);
            }
        });
        helpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                helpButton.setBackground(new Color(74,140,107));
            }
            @Override
            public void mouseExited(MouseEvent e){
                helpButton.setBackground(Color.WHITE);
            }
        });
        quitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                quitButton.setBackground(Color.RED);
            }
            @Override
            public void mouseExited(MouseEvent e){
                quitButton.setBackground(Color.WHITE);
            }
        });

        Font buttonFont = new Font("Comic Sans MS", Font.PLAIN, 24);
        playButton.setFont(buttonFont);
        playButton.setBorder(BorderFactory.createEtchedBorder(1));
        helpButton.setFont(buttonFont);
        helpButton.setBorder(BorderFactory.createEtchedBorder(1));
        quitButton.setFont(buttonFont);
        quitButton.setBorder(BorderFactory.createEtchedBorder(1));
        menuPanel.add(playButton);
        frame1.add(menuPanel);
    }

    public void quitMet(){
        quitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame1.dispose();
                System.out.println("QUIT THE GAME");
            }
        });
    }

    public void playMet(){
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
               running();
               frame1.dispose();
               System.out.println("PlAY THE GAME");
            }
        });
    }

    public void helpMet(){
        helpButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                Help helpmenu = new Help();
                frame1.dispose();
                System.out.println("HELP MENU");
            }
        });
    }

    public void running(){
        start();
        setFrame();
        hitButton.setEnabled(true);
        if(reducePlayerAce()< 16){
            stayButton.setEnabled(false);
        }
        hitMet();
        stayMet();
        resetMet();
        System.out.println("RUN THE GAME");
    }

    public void runAgain(){
        start();
        setFrame();
        hitButton.setEnabled(true);
        if(reducePlayerAce()< 16){
            stayButton.setEnabled(false);
        }
        else{
            stayButton.setEnabled(true);
        }
        System.out.println("PLAY AGAIN");
    }


    Blackjack(){
            setmenu();
            playMet();
            quitMet();
            helpMet();
    }
}


