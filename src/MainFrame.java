
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;






/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kgmontecinos92
 */
public class MainFrame extends javax.swing.JFrame {
    private static int moviesPanelCounter = 0;//used to keep track of moviesDisplayPanel cards and disable/enable up/down scroll buttons
    private String movieSelected;//defined after selecting movie and clicking 'next step' button
    //defined after selecting movie experience, showing, number of tickets and clicking the 'next step' button
    private String movieExperienceSeleceted,showingSelected;
    private int numOfAdultTickets, numOfSeniorTickets, numOfChildTickets;
    //defined after selecting seats and clicking 'complete purchase' button
    private ArrayList <String> seatsSelectedArrayList = new ArrayList();
    private int numOfSeatsSelected;
    //movie showings, 3 of 15 have more than 1 movie experience
    private String [] movie1TShowings = {"1:10pm", "4:00pm","6:40pm","9:20pm"};
    private String [] movie2TShowings = {"12:30pm","1:30pm","2:45pm","3:45pm","4:55pm","6:00pm","7:40pm","9:45pm"};
    private String [] movie3TShowings = {"1:45pm","4:15pm","6:45pm","9:15pm"};
    private String [] movie4TShowings = {"1:05pm","3:20pm","10:50pm"};
    private String [] movie4TShowingsIMAX = {"12:00pm","2:20pm","4:45pm","7:20pm","9:40pm"};
    private String [] movie5TShowings = {"1:00pm","2:00pm","4:10pm","5:10pm","7:30pm","8:30pm","10:45"};
    private String [] movie6TShowings = {"12:10pm","3:30pm","4:30pm","8:00pm","10:10pm"};
    private String [] movie7TShowings = {"8:15pm","11:00pm"};
    private String [] movie8TShowings = {"8:10pm","10:50pm"};
    private String [] movie9TShowings = {"4:50pm","7:40pm","9:50pm"};
    private String [] movie10TShowings = {"1:20pm","4:05pm","7:10pm","10:00pm"};
    private String [] movie11TShowings = {"2:25pm","7:45pm"};
    private String [] movie11TShowings3D = {"12:05pm","5:20pm","10:15pm"};
    private String [] movie12TShowings = {"3:15pm"};
    private String [] movie12TShowings3D = {"12:50pm","5:40pm"};
    private String [] movie13TShowings = {"12:20pm","3:40pm","9:50pm"};
    private String [] movie14TShowings = {"1:25pm","4:40pm","7:50pm","10:55pm"};
    private String [] movie15TShowings = {"1:40pm","4:50pm","10:20pm"};
    //movie prices by movie experience and time of day, array in {adult price, senior price, child price} order
    private String [] twoDMoviePricesBefore4pm = {"11.29","11.29","10.29"};
    private String [] twoDMoviePricesAfter4pm = {"12.29","11.29","10.29"};
    private String [] threeDMoviePricesBefore4pm = {"15.29","15.29","14.29"};
    private String [] threeDMoviePricesAfter4pm = {"16.99","15.99","14.29"};
    private String [] imaxMoviePricesBefore4pm = {"16.29","16.29","15.29"};
    private String [] imaxMoviePricesAfter4pm = {"17.99","16.99","15.29"};
    //same spinner models for spinners but individual action
    private SpinnerNumberModel numOfTicketsModel1 = new SpinnerNumberModel(0,0,9,1);
    private SpinnerNumberModel numOfTicketsModel2 = new SpinnerNumberModel(0,0,9,1);
    private SpinnerNumberModel numOfTicketsModel3 = new SpinnerNumberModel(0,0,9,1);

    private JToggleButton [] arrayOf8JToggleButtonsForTimeShowings = {new JToggleButton(),new JToggleButton(),new JToggleButton(),
        new JToggleButton(),new JToggleButton(),new JToggleButton(),new JToggleButton(),new JToggleButton()};
    private javax.swing.ButtonGroup timeSelectedButtonGroup = new javax.swing.ButtonGroup();

    //need 2 dimensional array to keep track of which seats are taken and which are available, (should be read from database at startup)
    //using default values of 0 for 'available' and 1 for 'taken'
    private int [][] arrayOfSeats ;
    private int[][] arrayOfSelectedSeatsDuringSession = new int[12][24];
    private JToggleButton [][] arrayOfSeatsJToggleButtons = new JToggleButton[12][24];
    //(num of tickets x price) for each type
    double adultCalc,seniorCalc,childCalc;
    
    //array of movie complete movie titles
    private String [] arrayOfFullMovieTitles = {"Addicted","Alexander and the Terrible, Horrible, No Good, Very Bad Day","Annabelle",
        "Dracula Untold","Fury","Gone Girl","Guardians of The Galaxy","Kill the Messenger","Teenage Mutant Ninja Turtles","The Best Of Me",
        "The Book of Life","The BoxTrolls","The Equalizer","The Judge","The Maze Runner"};
    
    //array of days of the week
    private String [] arrayOfDaysOfTheWeek = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    //array of months of the year
    private String [] arrayOfMonthsOfTheYear = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    
    //small movie icons for transaction summary
    private ImageIcon movie1IconResizedTS,movie2IconResizedTS,movie3IconResizedTS,movie4IconResizedTS,movie5IconResizedTS,movie6IconResizedTS,movie7IconResizedTS 
        ,movie8IconResizedTS,movie9IconResizedTS,movie10IconResizedTS,movie11IconResizedTS,movie12IconResizedTS,movie13IconResizedTS,movie14IconResizedTS
        ,movie15IconResizedTS;
    private ImageIcon purchaseConfimationIcon;
    
    
    /**
     * Creates new form LogInScreen
     */
    public MainFrame() {
        initComponents();
        readSeatsFromDB();
        //initialize 15 imageIcons for 15 movieToggleButtons
        Image movie1Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/addicted.jpg")).getImage();
        Image movie2Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/alexanderDisneyMovie.jpg")).getImage();
        Image movie3Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/annabelle.jpg")).getImage();
        Image movie4Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/draculaUntold.jpg")).getImage();
        Image movie5Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/fury.jpg")).getImage();
        Image movie6Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/goneGirl.jpg")).getImage();
        Image movie7Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/guardiansOfTheGalaxy.jpg")).getImage();
        Image movie8Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/killTheMessenger.jpg")).getImage();
        Image movie9Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/teenageMutantNinjaTurtles.jpg")).getImage();
        Image movie10Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/theBestOfMe.jpg")).getImage();
        Image movie11Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/theBookOfLife.jpg")).getImage();
        Image movie12Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/theBoxTrolls.jpg")).getImage();
        Image movie13Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/theEqualizer.jpg")).getImage();
        Image movie14Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/theJudge.jpg")).getImage();
        Image movie15Icon = new ImageIcon(getClass().getResource("/MTMImagesFolder/theMazeRunner.jpeg")).getImage();
        //Resize 15 image icons from above to fit in movieToggleButtons
        ImageIcon movie1IconResized = new ImageIcon(movie1Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie2IconResized = new ImageIcon(movie2Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie3IconResized = new ImageIcon(movie3Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie4IconResized = new ImageIcon(movie4Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie5IconResized = new ImageIcon(movie5Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie6IconResized = new ImageIcon(movie6Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie7IconResized = new ImageIcon(movie7Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie8IconResized = new ImageIcon(movie8Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie9IconResized = new ImageIcon(movie9Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie10IconResized = new ImageIcon(movie10Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie11IconResized = new ImageIcon(movie11Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie12IconResized = new ImageIcon(movie12Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie13IconResized = new ImageIcon(movie13Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie14IconResized = new ImageIcon(movie14Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        ImageIcon movie15IconResized = new ImageIcon(movie15Icon.getScaledInstance((row1Movies.getWidth()/3)-14,movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14,  java.awt.Image.SCALE_SMOOTH ));
        
        purchaseConfimationIcon = new ImageIcon(getClass().getResource("/MTMImagesFolder/checkMarkIcon.png"));
        //Assign resized movie imageIcons to movieToggleButtons
        movie1JToggleButton.setIcon(movie1IconResized);
        movie2JToggleButton.setIcon(movie2IconResized);
        movie3JToggleButton.setIcon(movie3IconResized);
        movie4JToggleButton.setIcon(movie4IconResized);
        movie5JToggleButton.setIcon(movie5IconResized);
        movie6JToggleButton.setIcon(movie6IconResized);
        movie7JToggleButton.setIcon(movie7IconResized);
        movie8JToggleButton.setIcon(movie8IconResized);
        movie9JToggleButton.setIcon(movie9IconResized);
        movie10JToggleButton.setIcon(movie10IconResized);
        movie11JToggleButton.setIcon(movie11IconResized);
        movie12JToggleButton.setIcon(movie12IconResized);
        movie13JToggleButton.setIcon(movie13IconResized);
        movie14JToggleButton.setIcon(movie14IconResized);
        movie15JToggleButton.setIcon(movie15IconResized);
        
        //Assign resized movie imageIcons to disabled scmovieToggleButtons
        scmovie1JToggleButton.setDisabledIcon(movie1IconResized);
        scmovie2JToggleButton.setDisabledIcon(movie2IconResized);
        scmovie3JToggleButton.setDisabledIcon(movie3IconResized);
        scmovie4JToggleButton.setDisabledIcon(movie4IconResized);
        scmovie5JToggleButton.setDisabledIcon(movie5IconResized);
        scmovie6JToggleButton.setDisabledIcon(movie6IconResized);
        scmovie7JToggleButton.setDisabledIcon(movie7IconResized);
        scmovie8JToggleButton.setDisabledIcon(movie8IconResized);
        scmovie9JToggleButton.setDisabledIcon(movie9IconResized);
        scmovie10JToggleButton.setDisabledIcon(movie10IconResized);
        scmovie11JToggleButton.setDisabledIcon(movie11IconResized);
        scmovie12JToggleButton.setDisabledIcon(movie12IconResized);
        scmovie13JToggleButton.setDisabledIcon(movie13IconResized);
        scmovie14JToggleButton.setDisabledIcon(movie14IconResized);
        scmovie15JToggleButton.setDisabledIcon(movie15IconResized);
        
        //Resize movie images again for smaller transaction summary panel
        movie1IconResizedTS = new ImageIcon(movie1Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie2IconResizedTS = new ImageIcon(movie2Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie3IconResizedTS = new ImageIcon(movie3Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie4IconResizedTS = new ImageIcon(movie4Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie5IconResizedTS = new ImageIcon(movie5Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie6IconResizedTS = new ImageIcon(movie6Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie7IconResizedTS = new ImageIcon(movie7Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie8IconResizedTS = new ImageIcon(movie8Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie9IconResizedTS = new ImageIcon(movie9Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie10IconResizedTS = new ImageIcon(movie10Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie11IconResizedTS = new ImageIcon(movie11Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie12IconResizedTS = new ImageIcon(movie12Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie13IconResizedTS= new ImageIcon(movie13Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie14IconResizedTS = new ImageIcon(movie14Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        movie15IconResizedTS = new ImageIcon(movie15Icon.getScaledInstance(220,(movie1JToggleButton.getHeight()-chooseAMovieTitlePanel.getHeight()-14)/3,  java.awt.Image.SCALE_SMOOTH ));
        //DEFAULT ICON FOR movieImageTS Label
        movieImageTS.setIcon(movie1IconResizedTS);
        
        //set specific components visibility to false
        incorrectEmailOrPasswordLabel.setVisible(false);
        invalidLogInEmailLabel.setVisible(false);
        invalidRegisterEmailLabel.setVisible(false);
        invalidNameLabel.setVisible(false);
        invalidLNameLabel.setVisible(false);
        invalidLogInEmailLabel.setVisible(false);
        emailsNoMatchLabel.setVisible(false);
        passwordsNoMatchLabel.setVisible(false);
        invalidCityLabel.setVisible(false);
        invalidZipCodeLabel.setVisible(false);
        invalidSCCLabel.setVisible(false);
        invalidCCLabel.setVisible(false);
        invalidPasswordTextPane.setVisible(false);
        spacerTB1.setVisible(false);
        spacerTB2.setVisible(false);
        spacerTB3.setVisible(false);
        spacerTB4.setVisible(false);
        spacerTB5.setVisible(false);
        spacerTB6.setVisible(false);
        spacerTB7.setVisible(false);
        spacerTB8.setVisible(false);
        spacerTB9.setVisible(false);
        spacerTB10.setVisible(false);
        spacerTB11.setVisible(false);
        spacerTB12.setVisible(false);
        spacerTB13.setVisible(false);
        spacerTB14.setVisible(false);
        spacerTB15.setVisible(false);
        spacerTB16.setVisible(false);
        spacerTB17.setVisible(false);
        spacerTB18.setVisible(false);
        spacerTB19.setVisible(false);
        spacerTB20.setVisible(false);
        spacerTB21.setVisible(false);
        spacerTB22.setVisible(false);
        spacerTB23.setVisible(false);
        spacerTB24.setVisible(false);
        spacerTB25.setVisible(false);
        spacerTB26.setVisible(false);
        spacerTB27.setVisible(false);
        spacerTB28.setVisible(false);
        spacerTB29.setVisible(false);
        spacerTB30.setVisible(false);
        spacerTB31.setVisible(false);
        spacerTB32.setVisible(false);
        spacerTB33.setVisible(false);
        spacerTB34.setVisible(false);
        spacerTB35.setVisible(false);
        spacerTB36.setVisible(false);
        spacerTB37.setVisible(false);
        spacerTB38.setVisible(false);
        spacerTB39.setVisible(false);
        spacerTB40.setVisible(false);
        spacerTB41.setVisible(false);
        spacerTB42.setVisible(false);
        spacerTB43.setVisible(false);
        spacerTB44.setVisible(false);
        spacerTB45.setVisible(false);
        spacerTB46.setVisible(false);
        spacerTB47.setVisible(false);
        spacerTB48.setVisible(false);
        spacerTB49.setVisible(false);
        spacerButtonTS1.setVisible(false);
        spacerButtonTS2.setVisible(false);
        spacerToggleButtonTS1.setVisible(false);
        spacerToggleButtonTS2.setVisible(false);
        
        
        //set specific components editability to false
        adultTotalTextField.setEditable(false);
        seniorTotalTextField.setEditable(false);
        childTotalTextField.setEditable(false);
       
        //set size of selectSeatsPanel
        transactionSummaryPanel.setPreferredSize(new Dimension((csacpDisplayPanel.getWidth()/12)*5,csacpDisplayPanel.getHeight()));
        seatLegendPanel.setPreferredSize(new Dimension((csacpDisplayPanel.getWidth()/12)*7,csacpDisplayPanel.getHeight()/7));
        
        //set number model for spinners
        adultTicketsSpinner.setModel(numOfTicketsModel1);
        seniorTicketsSpinner.setModel(numOfTicketsModel2);
        childTicketsSpinner.setModel(numOfTicketsModel3);
        
        //adds 8 anonymous jToggleButtons to timeSelectedButtonGroup and add listeners
        for(int i = 0; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
            timeSelectedButtonGroup.add(arrayOf8JToggleButtonsForTimeShowings[i]);
            arrayOf8JToggleButtonsForTimeShowings[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    showingsSelectionActionPerformed(evt);
                }
            });
        }
        
        
       
     
        this.arrayOfSeatsJToggleButtons[0][0]=seatA1;
        this.arrayOfSeatsJToggleButtons[0][1]=seatA2;
        this.arrayOfSeatsJToggleButtons[0][2]=seatA3;
        this.arrayOfSeatsJToggleButtons[0][3]=seatA4;
        this.arrayOfSeatsJToggleButtons[0][4]=seatA5;
        this.arrayOfSeatsJToggleButtons[0][5]=seatA6;
        this.arrayOfSeatsJToggleButtons[0][6]=seatA7;
        this.arrayOfSeatsJToggleButtons[0][7]=seatA8;
        this.arrayOfSeatsJToggleButtons[0][8]=seatA9;
        this.arrayOfSeatsJToggleButtons[0][9]=seatA10;
        this.arrayOfSeatsJToggleButtons[0][10]=seatA11;
        this.arrayOfSeatsJToggleButtons[0][11]=seatA12;
        this.arrayOfSeatsJToggleButtons[0][12]=seatA13;
        this.arrayOfSeatsJToggleButtons[0][13]=seatA14;
        this.arrayOfSeatsJToggleButtons[0][14]=seatA15;
        this.arrayOfSeatsJToggleButtons[0][15]=seatA16;
        this.arrayOfSeatsJToggleButtons[0][16]=seatA17;
        this.arrayOfSeatsJToggleButtons[0][17]=seatA18;
        this.arrayOfSeatsJToggleButtons[0][18]=seatA19;
        this.arrayOfSeatsJToggleButtons[0][19]=seatA20;
        this.arrayOfSeatsJToggleButtons[0][20]=seatA21;
        this.arrayOfSeatsJToggleButtons[0][21]=seatA22;
        this.arrayOfSeatsJToggleButtons[0][22]=seatA22;
        this.arrayOfSeatsJToggleButtons[0][23]=seatA22;
        
        this.arrayOfSeatsJToggleButtons[1][0]=seatB1;
        this.arrayOfSeatsJToggleButtons[1][1]=seatB2;
        this.arrayOfSeatsJToggleButtons[1][2]=seatB3;
        this.arrayOfSeatsJToggleButtons[1][3]=seatB4;
        this.arrayOfSeatsJToggleButtons[1][4]=seatB5;
        this.arrayOfSeatsJToggleButtons[1][5]=seatB6;
        this.arrayOfSeatsJToggleButtons[1][6]=seatB7;
        this.arrayOfSeatsJToggleButtons[1][7]=seatB8;
        this.arrayOfSeatsJToggleButtons[1][8]=seatB9;
        this.arrayOfSeatsJToggleButtons[1][9]=seatB10;
        this.arrayOfSeatsJToggleButtons[1][10]=seatB11;
        this.arrayOfSeatsJToggleButtons[1][11]=seatB12;
        this.arrayOfSeatsJToggleButtons[1][12]=seatB13;
        this.arrayOfSeatsJToggleButtons[1][13]=seatB14;
        this.arrayOfSeatsJToggleButtons[1][14]=seatB15;
        this.arrayOfSeatsJToggleButtons[1][15]=seatB16;
        this.arrayOfSeatsJToggleButtons[1][16]=seatB17;
        this.arrayOfSeatsJToggleButtons[1][17]=seatB18;
        this.arrayOfSeatsJToggleButtons[1][18]=seatB19;
        this.arrayOfSeatsJToggleButtons[1][19]=seatB20;
        this.arrayOfSeatsJToggleButtons[1][20]=seatB21;
        this.arrayOfSeatsJToggleButtons[1][21]=seatB22;
        this.arrayOfSeatsJToggleButtons[1][22]=seatB22;
        this.arrayOfSeatsJToggleButtons[1][23]=seatB22;
        
        this.arrayOfSeatsJToggleButtons[2][0]=seatC1;
        this.arrayOfSeatsJToggleButtons[2][1]=seatC2;
        this.arrayOfSeatsJToggleButtons[2][2]=seatC3;
        this.arrayOfSeatsJToggleButtons[2][3]=seatC4;
        this.arrayOfSeatsJToggleButtons[2][4]=seatC5;
        this.arrayOfSeatsJToggleButtons[2][5]=seatC6;
        this.arrayOfSeatsJToggleButtons[2][6]=seatC7;
        this.arrayOfSeatsJToggleButtons[2][7]=seatC8;
        this.arrayOfSeatsJToggleButtons[2][8]=seatC9;
        this.arrayOfSeatsJToggleButtons[2][9]=seatC10;
        this.arrayOfSeatsJToggleButtons[2][10]=seatC11;
        this.arrayOfSeatsJToggleButtons[2][11]=seatC12;
        this.arrayOfSeatsJToggleButtons[2][12]=seatC13;
        this.arrayOfSeatsJToggleButtons[2][13]=seatC14;
        this.arrayOfSeatsJToggleButtons[2][14]=seatC15;
        this.arrayOfSeatsJToggleButtons[2][15]=seatC16;
        this.arrayOfSeatsJToggleButtons[2][16]=seatC17;
        this.arrayOfSeatsJToggleButtons[2][17]=seatC18;
        this.arrayOfSeatsJToggleButtons[2][18]=seatC19;
        this.arrayOfSeatsJToggleButtons[2][19]=seatC20;
        this.arrayOfSeatsJToggleButtons[2][20]=seatC21;
        this.arrayOfSeatsJToggleButtons[2][21]=seatC22;
        this.arrayOfSeatsJToggleButtons[2][22]=seatC22;
        this.arrayOfSeatsJToggleButtons[2][23]=seatC22;
        
        this.arrayOfSeatsJToggleButtons[3][0]=seatD1;
        this.arrayOfSeatsJToggleButtons[3][1]=seatD2;
        this.arrayOfSeatsJToggleButtons[3][2]=seatD3;
        this.arrayOfSeatsJToggleButtons[3][3]=seatD4;
        this.arrayOfSeatsJToggleButtons[3][4]=seatD5;
        this.arrayOfSeatsJToggleButtons[3][5]=seatD6;
        this.arrayOfSeatsJToggleButtons[3][6]=seatD7;
        this.arrayOfSeatsJToggleButtons[3][7]=seatD8;
        this.arrayOfSeatsJToggleButtons[3][8]=seatD9;
        this.arrayOfSeatsJToggleButtons[3][9]=seatD10;
        this.arrayOfSeatsJToggleButtons[3][10]=seatD11;
        this.arrayOfSeatsJToggleButtons[3][11]=seatD12;
        this.arrayOfSeatsJToggleButtons[3][12]=seatD13;
        this.arrayOfSeatsJToggleButtons[3][13]=seatD14;
        this.arrayOfSeatsJToggleButtons[3][14]=seatD15;
        this.arrayOfSeatsJToggleButtons[3][15]=seatD16;
        this.arrayOfSeatsJToggleButtons[3][16]=seatD17;
        this.arrayOfSeatsJToggleButtons[3][17]=seatD18;
        this.arrayOfSeatsJToggleButtons[3][18]=seatD19;
        this.arrayOfSeatsJToggleButtons[3][19]=seatD20;
        this.arrayOfSeatsJToggleButtons[3][20]=seatD21;
        this.arrayOfSeatsJToggleButtons[3][21]=seatD22;
        this.arrayOfSeatsJToggleButtons[3][22]=seatD22;
        this.arrayOfSeatsJToggleButtons[3][23]=seatD22;
       
        this.arrayOfSeatsJToggleButtons[4][0]=seatE1;
        this.arrayOfSeatsJToggleButtons[4][1]=seatE2;
        this.arrayOfSeatsJToggleButtons[4][2]=seatE3;
        this.arrayOfSeatsJToggleButtons[4][3]=seatE4;
        this.arrayOfSeatsJToggleButtons[4][4]=seatE5;
        this.arrayOfSeatsJToggleButtons[4][5]=seatE6;
        this.arrayOfSeatsJToggleButtons[4][6]=seatE7;
        this.arrayOfSeatsJToggleButtons[4][7]=seatE8;
        this.arrayOfSeatsJToggleButtons[4][8]=seatE9;
        this.arrayOfSeatsJToggleButtons[4][9]=seatE10;
        this.arrayOfSeatsJToggleButtons[4][10]=seatE11;
        this.arrayOfSeatsJToggleButtons[4][11]=seatE12;
        this.arrayOfSeatsJToggleButtons[4][12]=seatE13;
        this.arrayOfSeatsJToggleButtons[4][13]=seatE14;
        this.arrayOfSeatsJToggleButtons[4][14]=seatE15;
        this.arrayOfSeatsJToggleButtons[4][15]=seatE16;
        this.arrayOfSeatsJToggleButtons[4][16]=seatE17;
        this.arrayOfSeatsJToggleButtons[4][17]=seatE18;
        this.arrayOfSeatsJToggleButtons[4][18]=seatE19;
        this.arrayOfSeatsJToggleButtons[4][19]=seatE19;
        this.arrayOfSeatsJToggleButtons[4][20]=seatE19;
        this.arrayOfSeatsJToggleButtons[4][21]=seatE19;
        this.arrayOfSeatsJToggleButtons[4][22]=seatE19;
        this.arrayOfSeatsJToggleButtons[4][23]=seatE19;
        
        this.arrayOfSeatsJToggleButtons[5][0]=seatF1;
        this.arrayOfSeatsJToggleButtons[5][1]=seatF2;
        this.arrayOfSeatsJToggleButtons[5][2]=seatF3;
        this.arrayOfSeatsJToggleButtons[5][3]=seatF4;
        this.arrayOfSeatsJToggleButtons[5][4]=seatF5;
        this.arrayOfSeatsJToggleButtons[5][5]=seatF6;
        this.arrayOfSeatsJToggleButtons[5][6]=seatF7;
        this.arrayOfSeatsJToggleButtons[5][7]=seatF8;
        this.arrayOfSeatsJToggleButtons[5][8]=seatF9;
        this.arrayOfSeatsJToggleButtons[5][9]=seatF10;
        this.arrayOfSeatsJToggleButtons[5][10]=seatF11;
        this.arrayOfSeatsJToggleButtons[5][11]=seatF12;
        this.arrayOfSeatsJToggleButtons[5][12]=seatF13;
        this.arrayOfSeatsJToggleButtons[5][13]=seatF14;
        this.arrayOfSeatsJToggleButtons[5][14]=seatF15;
        this.arrayOfSeatsJToggleButtons[5][15]=seatF16;
        this.arrayOfSeatsJToggleButtons[5][16]=seatF17;
        this.arrayOfSeatsJToggleButtons[5][17]=seatF18;
        this.arrayOfSeatsJToggleButtons[5][18]=seatF19;
        this.arrayOfSeatsJToggleButtons[5][19]=seatF20;
        this.arrayOfSeatsJToggleButtons[5][20]=seatF21;
        this.arrayOfSeatsJToggleButtons[5][21]=seatF22;
        this.arrayOfSeatsJToggleButtons[5][22]=seatF22;
        this.arrayOfSeatsJToggleButtons[5][23]=seatF22;
       
        this.arrayOfSeatsJToggleButtons[6][0]=seatG1;
        this.arrayOfSeatsJToggleButtons[6][1]=seatG2;
        this.arrayOfSeatsJToggleButtons[6][2]=seatG3;
        this.arrayOfSeatsJToggleButtons[6][3]=seatG4;
        this.arrayOfSeatsJToggleButtons[6][4]=seatG5;
        this.arrayOfSeatsJToggleButtons[6][5]=seatG6;
        this.arrayOfSeatsJToggleButtons[6][6]=seatG7;
        this.arrayOfSeatsJToggleButtons[6][7]=seatG8;
        this.arrayOfSeatsJToggleButtons[6][8]=seatG9;
        this.arrayOfSeatsJToggleButtons[6][9]=seatG10;
        this.arrayOfSeatsJToggleButtons[6][10]=seatG11;
        this.arrayOfSeatsJToggleButtons[6][11]=seatG12;
        this.arrayOfSeatsJToggleButtons[6][12]=seatG13;
        this.arrayOfSeatsJToggleButtons[6][13]=seatG14;
        this.arrayOfSeatsJToggleButtons[6][14]=seatG15;
        this.arrayOfSeatsJToggleButtons[6][15]=seatG16;
        this.arrayOfSeatsJToggleButtons[6][16]=seatG17;
        this.arrayOfSeatsJToggleButtons[6][17]=seatG18;
        this.arrayOfSeatsJToggleButtons[6][18]=seatG19;
        this.arrayOfSeatsJToggleButtons[6][19]=seatG20;
        this.arrayOfSeatsJToggleButtons[6][20]=seatG21;
        this.arrayOfSeatsJToggleButtons[6][21]=seatG22;
        this.arrayOfSeatsJToggleButtons[6][22]=seatG22;
        this.arrayOfSeatsJToggleButtons[6][23]=seatG22;
       
        this.arrayOfSeatsJToggleButtons[7][0]=seatH1;
        this.arrayOfSeatsJToggleButtons[7][1]=seatH2;
        this.arrayOfSeatsJToggleButtons[7][2]=seatH3;
        this.arrayOfSeatsJToggleButtons[7][3]=seatH4;
        this.arrayOfSeatsJToggleButtons[7][4]=seatH5;
        this.arrayOfSeatsJToggleButtons[7][5]=seatH6;
        this.arrayOfSeatsJToggleButtons[7][6]=seatH7;
        this.arrayOfSeatsJToggleButtons[7][7]=seatH8;
        this.arrayOfSeatsJToggleButtons[7][8]=seatH9;
        this.arrayOfSeatsJToggleButtons[7][9]=seatH10;
        this.arrayOfSeatsJToggleButtons[7][10]=seatH11;
        this.arrayOfSeatsJToggleButtons[7][11]=seatH12;
        this.arrayOfSeatsJToggleButtons[7][12]=seatH13;
        this.arrayOfSeatsJToggleButtons[7][13]=seatH14;
        this.arrayOfSeatsJToggleButtons[7][14]=seatH15;
        this.arrayOfSeatsJToggleButtons[7][15]=seatH16;
        this.arrayOfSeatsJToggleButtons[7][16]=seatH17;
        this.arrayOfSeatsJToggleButtons[7][17]=seatH18;
        this.arrayOfSeatsJToggleButtons[7][18]=seatH19;
        this.arrayOfSeatsJToggleButtons[7][19]=seatH20;
        this.arrayOfSeatsJToggleButtons[7][20]=seatH21;
        this.arrayOfSeatsJToggleButtons[7][21]=seatH22;
        this.arrayOfSeatsJToggleButtons[7][22]=seatH22;
        this.arrayOfSeatsJToggleButtons[7][23]=seatH22;
        
        this.arrayOfSeatsJToggleButtons[8][0]=seatI1;
        this.arrayOfSeatsJToggleButtons[8][1]=seatI2;
        this.arrayOfSeatsJToggleButtons[8][2]=seatI3;
        this.arrayOfSeatsJToggleButtons[8][3]=seatI4;
        this.arrayOfSeatsJToggleButtons[8][4]=seatI5;
        this.arrayOfSeatsJToggleButtons[8][5]=seatI6;
        this.arrayOfSeatsJToggleButtons[8][6]=seatI7;
        this.arrayOfSeatsJToggleButtons[8][7]=seatI8;
        this.arrayOfSeatsJToggleButtons[8][8]=seatI9;
        this.arrayOfSeatsJToggleButtons[8][9]=seatI10;
        this.arrayOfSeatsJToggleButtons[8][10]=seatI11;
        this.arrayOfSeatsJToggleButtons[8][11]=seatI12;
        this.arrayOfSeatsJToggleButtons[8][12]=seatI13;
        this.arrayOfSeatsJToggleButtons[8][13]=seatI14;
        this.arrayOfSeatsJToggleButtons[8][14]=seatI15;
        this.arrayOfSeatsJToggleButtons[8][15]=seatI16;
        this.arrayOfSeatsJToggleButtons[8][16]=seatI17;
        this.arrayOfSeatsJToggleButtons[8][17]=seatI18;
        this.arrayOfSeatsJToggleButtons[8][18]=seatI19;
        this.arrayOfSeatsJToggleButtons[8][19]=seatI20;
        this.arrayOfSeatsJToggleButtons[8][20]=seatI21;
        this.arrayOfSeatsJToggleButtons[8][21]=seatI22;
        this.arrayOfSeatsJToggleButtons[8][22]=seatI22;
        this.arrayOfSeatsJToggleButtons[8][23]=seatI22;
        
        this.arrayOfSeatsJToggleButtons[9][0]=seatJ1;
        this.arrayOfSeatsJToggleButtons[9][1]=seatJ2;
        this.arrayOfSeatsJToggleButtons[9][2]=seatJ3;
        this.arrayOfSeatsJToggleButtons[9][3]=seatJ4;
        this.arrayOfSeatsJToggleButtons[9][4]=seatJ5;
        this.arrayOfSeatsJToggleButtons[9][5]=seatJ6;
        this.arrayOfSeatsJToggleButtons[9][6]=seatJ7;
        this.arrayOfSeatsJToggleButtons[9][7]=seatJ8;
        this.arrayOfSeatsJToggleButtons[9][8]=seatJ9;
        this.arrayOfSeatsJToggleButtons[9][9]=seatJ10;
        this.arrayOfSeatsJToggleButtons[9][10]=seatJ11;
        this.arrayOfSeatsJToggleButtons[9][11]=seatJ12;
        this.arrayOfSeatsJToggleButtons[9][12]=seatJ13;
        this.arrayOfSeatsJToggleButtons[9][13]=seatJ14;
        this.arrayOfSeatsJToggleButtons[9][14]=seatJ15;
        this.arrayOfSeatsJToggleButtons[9][15]=seatJ16;
        this.arrayOfSeatsJToggleButtons[9][16]=seatJ17;
        this.arrayOfSeatsJToggleButtons[9][17]=seatJ18;
        this.arrayOfSeatsJToggleButtons[9][18]=seatJ19;
        this.arrayOfSeatsJToggleButtons[9][19]=seatJ20;
        this.arrayOfSeatsJToggleButtons[9][20]=seatJ21;
        this.arrayOfSeatsJToggleButtons[9][21]=seatJ22;
        this.arrayOfSeatsJToggleButtons[9][22]=seatJ22;
        this.arrayOfSeatsJToggleButtons[9][23]=seatJ22;
       
        this.arrayOfSeatsJToggleButtons[10][0]=seatK1;
        this.arrayOfSeatsJToggleButtons[10][1]=seatK2;
        this.arrayOfSeatsJToggleButtons[10][2]=seatK3;
        this.arrayOfSeatsJToggleButtons[10][3]=seatK4;
        this.arrayOfSeatsJToggleButtons[10][4]=seatK5;
        this.arrayOfSeatsJToggleButtons[10][5]=seatK6;
        this.arrayOfSeatsJToggleButtons[10][6]=seatK7;
        this.arrayOfSeatsJToggleButtons[10][7]=seatK8;
        this.arrayOfSeatsJToggleButtons[10][8]=seatK9;
        this.arrayOfSeatsJToggleButtons[10][9]=seatK10;
        this.arrayOfSeatsJToggleButtons[10][10]=seatK11;
        this.arrayOfSeatsJToggleButtons[10][11]=seatK12;
        this.arrayOfSeatsJToggleButtons[10][12]=seatK13;
        this.arrayOfSeatsJToggleButtons[10][13]=seatK14;
        this.arrayOfSeatsJToggleButtons[10][14]=seatK15;
        this.arrayOfSeatsJToggleButtons[10][15]=seatK16;
        this.arrayOfSeatsJToggleButtons[10][16]=seatK17;
        this.arrayOfSeatsJToggleButtons[10][17]=seatK18;
        this.arrayOfSeatsJToggleButtons[10][18]=seatK19;
        this.arrayOfSeatsJToggleButtons[10][19]=seatK20;
        this.arrayOfSeatsJToggleButtons[10][20]=seatK21;
        this.arrayOfSeatsJToggleButtons[10][21]=seatK22;
        this.arrayOfSeatsJToggleButtons[10][22]=seatK22;
        this.arrayOfSeatsJToggleButtons[10][23]=seatK22;
        
        this.arrayOfSeatsJToggleButtons[11][0]=seatL1;
        this.arrayOfSeatsJToggleButtons[11][1]=seatL2;
        this.arrayOfSeatsJToggleButtons[11][2]=seatL3;
        this.arrayOfSeatsJToggleButtons[11][3]=seatL4;
        this.arrayOfSeatsJToggleButtons[11][4]=seatL5;
        this.arrayOfSeatsJToggleButtons[11][5]=seatL6;
        this.arrayOfSeatsJToggleButtons[11][6]=seatL7;
        this.arrayOfSeatsJToggleButtons[11][7]=seatL8;
        this.arrayOfSeatsJToggleButtons[11][8]=seatL9;
        this.arrayOfSeatsJToggleButtons[11][9]=seatL10;
        this.arrayOfSeatsJToggleButtons[11][10]=seatL11;
        this.arrayOfSeatsJToggleButtons[11][11]=seatL12;
        this.arrayOfSeatsJToggleButtons[11][12]=seatL13;
        this.arrayOfSeatsJToggleButtons[11][13]=seatL14;
        this.arrayOfSeatsJToggleButtons[11][14]=seatL15;
        this.arrayOfSeatsJToggleButtons[11][15]=seatL16;
        this.arrayOfSeatsJToggleButtons[11][16]=seatL17;
        this.arrayOfSeatsJToggleButtons[11][17]=seatL18;
        this.arrayOfSeatsJToggleButtons[11][18]=seatL19;
        this.arrayOfSeatsJToggleButtons[11][19]=seatL20;
        this.arrayOfSeatsJToggleButtons[11][20]=seatL21;
        this.arrayOfSeatsJToggleButtons[11][21]=seatL22;
        this.arrayOfSeatsJToggleButtons[11][22]=seatL23;
        this.arrayOfSeatsJToggleButtons[11][23]=seatL24;
        
        
        
        
        
        
        //JToggleButton [][] arrayOfSeatsJToggleButtons;
        //set toggle buttons for seatSelection as selected or not selected at starut up, reading from database
        
        for(int r =0; r<12;r++){//traverses rows
            for(int c=0; c<24; c++){//traverses Columns
               if (arrayOfSeats[r][c] == 1){//if seat is 'taken'
                 arrayOfSeatsJToggleButtons[r][c].setEnabled(false);
               }
            }
        }
        
        
        //requests focus on signin button at logInScreen
        signInButton.requestFocusInWindow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        movieSelectedButtonGroup = new javax.swing.ButtonGroup();
        movieExperienceButtonGroup = new javax.swing.ButtonGroup();
        displayPanel = new javax.swing.JPanel();
        logInScreen = new javax.swing.JPanel();
        leftHalf = new javax.swing.JPanel();
        logInInfoPanel = new javax.swing.JPanel();
        logInEmailTextField = new javax.swing.JTextField();
        logInPasswordField = new javax.swing.JPasswordField();
        signInButton = new javax.swing.JButton();
        incorrectEmailOrPasswordLabel = new javax.swing.JLabel();
        invalidLogInEmailLabel = new javax.swing.JLabel();
        mtmLogo1 = new javax.swing.JLabel();
        mtmLogo2 = new javax.swing.JLabel();
        rightHalf = new javax.swing.JPanel();
        joinMessagePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        joinPanelLabel1 = new javax.swing.JLabel();
        joinButton = new javax.swing.JButton();
        registerScreen = new javax.swing.JPanel();
        registerTitlePanel = new javax.swing.JPanel();
        csacpTitleLabel2 = new javax.swing.JLabel();
        mtmLogo4 = new javax.swing.JLabel();
        leftAndRightHalvesPanel = new javax.swing.JPanel();
        userInfoPanel = new javax.swing.JPanel();
        confirmPasswordField = new javax.swing.JPasswordField();
        firstNameTextField = new javax.swing.JTextField();
        confirmPasswordLabel = new javax.swing.JLabel();
        emailAddressLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        confirmEmailTextField = new javax.swing.JTextField();
        createPasswordField = new javax.swing.JPasswordField();
        confirmEmailAddressLabel = new javax.swing.JLabel();
        lastNameTextField = new javax.swing.JTextField();
        createPasswordLabel = new javax.swing.JLabel();
        registerEmailTextField = new javax.swing.JTextField();
        invalidNameLabel = new javax.swing.JLabel();
        invalidRegisterEmailLabel = new javax.swing.JLabel();
        emailsNoMatchLabel = new javax.swing.JLabel();
        passwordsNoMatchLabel = new javax.swing.JLabel();
        invalidLNameLabel = new javax.swing.JLabel();
        invalidPasswordTextPane = new javax.swing.JTextPane();
        paymentInfoPanel = new javax.swing.JPanel();
        cityTextField = new javax.swing.JTextField();
        streetAddressTextField = new javax.swing.JTextField();
        zipCodeTextField = new javax.swing.JTextField();
        statesComboBox = new javax.swing.JComboBox();
        creditCardNumberTextField = new javax.swing.JTextField();
        ccExpMonthLabel = new javax.swing.JLabel();
        ccExpMonthComboBox = new javax.swing.JComboBox();
        ccExpYearComboBox = new javax.swing.JComboBox();
        billingAddressLabel1 = new javax.swing.JLabel();
        securityCodeTextField = new javax.swing.JTextField();
        billingAddressLabel2 = new javax.swing.JLabel();
        ccExpYearLabel = new javax.swing.JLabel();
        invalidCityLabel = new javax.swing.JLabel();
        invalidZipCodeLabel = new javax.swing.JLabel();
        invalidSCCLabel = new javax.swing.JLabel();
        invalidCCLabel = new javax.swing.JLabel();
        registerScreenButtonPanel = new javax.swing.JPanel();
        cancelRegistrationButton = new javax.swing.JButton();
        submitRegistrationButton = new javax.swing.JButton();
        chooseAMovieScreen = new javax.swing.JPanel();
        chooseAMovieTitlePanel = new javax.swing.JPanel();
        camTitleLabel = new javax.swing.JLabel();
        mtmLogo3 = new javax.swing.JLabel();
        moviesDisplayPanel = new javax.swing.JPanel();
        row1Movies = new javax.swing.JPanel();
        movie1JToggleButton = new javax.swing.JToggleButton();
        movie2JToggleButton = new javax.swing.JToggleButton();
        movie3JToggleButton = new javax.swing.JToggleButton();
        row2Movies = new javax.swing.JPanel();
        movie4JToggleButton = new javax.swing.JToggleButton();
        movie5JToggleButton = new javax.swing.JToggleButton();
        movie6JToggleButton = new javax.swing.JToggleButton();
        row3Movies = new javax.swing.JPanel();
        movie7JToggleButton = new javax.swing.JToggleButton();
        movie8JToggleButton = new javax.swing.JToggleButton();
        movie9JToggleButton = new javax.swing.JToggleButton();
        row4Movies = new javax.swing.JPanel();
        movie10JToggleButton = new javax.swing.JToggleButton();
        movie11JToggleButton = new javax.swing.JToggleButton();
        movie12JToggleButton = new javax.swing.JToggleButton();
        row5Movies = new javax.swing.JPanel();
        movie13JToggleButton = new javax.swing.JToggleButton();
        movie14JToggleButton = new javax.swing.JToggleButton();
        movie15JToggleButton = new javax.swing.JToggleButton();
        camNavButtonPanel = new javax.swing.JPanel();
        camCancelPurchaseButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        camNextButton = new javax.swing.JButton();
        chooseTimeAndTicketsScreen = new javax.swing.JPanel();
        chooseTimeAndTicketsTitlePanel = new javax.swing.JPanel();
        chooseTimeAndTicketsLabel = new javax.swing.JLabel();
        mtmLogo5 = new javax.swing.JLabel();
        chooseTTdisplayPanel = new javax.swing.JPanel();
        selectedMoviePanel = new javax.swing.JPanel();
        scmovie1JToggleButton = new javax.swing.JToggleButton();
        scmovie2JToggleButton = new javax.swing.JToggleButton();
        scmovie3JToggleButton = new javax.swing.JToggleButton();
        scmovie4JToggleButton = new javax.swing.JToggleButton();
        scmovie5JToggleButton = new javax.swing.JToggleButton();
        scmovie6JToggleButton = new javax.swing.JToggleButton();
        scmovie7JToggleButton = new javax.swing.JToggleButton();
        scmovie8JToggleButton = new javax.swing.JToggleButton();
        scmovie9JToggleButton = new javax.swing.JToggleButton();
        scmovie10JToggleButton = new javax.swing.JToggleButton();
        scmovie11JToggleButton = new javax.swing.JToggleButton();
        scmovie12JToggleButton = new javax.swing.JToggleButton();
        scmovie13JToggleButton = new javax.swing.JToggleButton();
        scmovie14JToggleButton = new javax.swing.JToggleButton();
        scmovie15JToggleButton = new javax.swing.JToggleButton();
        chooseTimeAnd3DPanel = new javax.swing.JPanel();
        twoDor3DPanel = new javax.swing.JPanel();
        movieExperienceTitle = new javax.swing.JPanel();
        movieExerienceLabel = new javax.swing.JLabel();
        twoD3DandTitlesPanel = new javax.swing.JPanel();
        twoDJToggleButton = new javax.swing.JToggleButton();
        threeDJToggleButton = new javax.swing.JToggleButton();
        imaxJToggleButton = new javax.swing.JToggleButton();
        movieTimesTitle = new javax.swing.JPanel();
        movieTimesLabel = new javax.swing.JLabel();
        movieTimesPanel = new javax.swing.JPanel();
        chooseTicketQty = new javax.swing.JPanel();
        movieTicketsQtyTitlePanel = new javax.swing.JPanel();
        mtqtLabel = new javax.swing.JLabel();
        threeTicketOptionsPanel = new javax.swing.JPanel();
        adultTicketsTitlePanel = new javax.swing.JPanel();
        adultTicketLabel = new javax.swing.JLabel();
        adultTicketsCalculationPanel = new javax.swing.JPanel();
        adultSpinnerPanel = new javax.swing.JPanel();
        adultTicketsSpinner = new javax.swing.JSpinner();
        adultPriceLabel = new javax.swing.JLabel();
        adultTotalTextField = new javax.swing.JTextField();
        seniorTicketsTitlePanel = new javax.swing.JPanel();
        seniorTicketLabel = new javax.swing.JLabel();
        seniorTicketsCalculationPanel = new javax.swing.JPanel();
        seniorSpinnerPanel = new javax.swing.JPanel();
        seniorTicketsSpinner = new javax.swing.JSpinner();
        seniorPriceLabel = new javax.swing.JLabel();
        seniorTotalTextField = new javax.swing.JTextField();
        childTicketsTitlePanel = new javax.swing.JPanel();
        childTicketLabel = new javax.swing.JLabel();
        childTicketsCalculationPanel = new javax.swing.JPanel();
        childSpinnerPanel = new javax.swing.JPanel();
        childTicketsSpinner = new javax.swing.JSpinner();
        childPriceLabel = new javax.swing.JLabel();
        childTotalTextField = new javax.swing.JTextField();
        spaceHTitlePanel = new javax.swing.JPanel();
        spaceHLabel = new javax.swing.JLabel();
        ctatNavButtonPanel = new javax.swing.JPanel();
        ctatCancelPurchaseButton = new javax.swing.JButton();
        ctatBackButton = new javax.swing.JButton();
        ctatNextButton = new javax.swing.JButton();
        chooseSeatsAndCompletePurchaseScreen = new javax.swing.JPanel();
        csacpTitlePanel = new javax.swing.JPanel();
        csacpTitleLabel = new javax.swing.JLabel();
        mtmLogo = new javax.swing.JLabel();
        csacpDisplayPanel = new javax.swing.JPanel();
        selectSeatsPanel = new javax.swing.JPanel();
        seatLegendPanel = new javax.swing.JPanel();
        availablePanel = new javax.swing.JPanel();
        availableLeftPanel = new javax.swing.JPanel();
        spacerToggleButtonTS1 = new javax.swing.JToggleButton();
        resizeTBPanel = new javax.swing.JPanel();
        availableToggleButtonTS = new javax.swing.JToggleButton();
        availableButtonLabel = new javax.swing.JLabel();
        unavailablePanel = new javax.swing.JPanel();
        unavailableRightPanel = new javax.swing.JPanel();
        spacerToggleButtonTS2 = new javax.swing.JToggleButton();
        resizeTBPanel2 = new javax.swing.JPanel();
        unavailableToggleButtonTS = new javax.swing.JToggleButton();
        unavailableButtonLabel = new javax.swing.JLabel();
        seatsDiagramPanel = new javax.swing.JPanel();
        screenLabelPanel = new javax.swing.JPanel();
        screenLabel = new javax.swing.JLabel();
        seatToggleButtonsPanel = new javax.swing.JPanel();
        spacerTB1 = new javax.swing.JToggleButton();
        seatA1 = new javax.swing.JToggleButton();
        seatA2 = new javax.swing.JToggleButton();
        seatA3 = new javax.swing.JToggleButton();
        seatA4 = new javax.swing.JToggleButton();
        seatA5 = new javax.swing.JToggleButton();
        seatA6 = new javax.swing.JToggleButton();
        seatA7 = new javax.swing.JToggleButton();
        seatA8 = new javax.swing.JToggleButton();
        seatA9 = new javax.swing.JToggleButton();
        seatA10 = new javax.swing.JToggleButton();
        seatA11 = new javax.swing.JToggleButton();
        seatA12 = new javax.swing.JToggleButton();
        seatA13 = new javax.swing.JToggleButton();
        seatA14 = new javax.swing.JToggleButton();
        seatA15 = new javax.swing.JToggleButton();
        seatA16 = new javax.swing.JToggleButton();
        seatA17 = new javax.swing.JToggleButton();
        seatA18 = new javax.swing.JToggleButton();
        seatA19 = new javax.swing.JToggleButton();
        seatA20 = new javax.swing.JToggleButton();
        seatA21 = new javax.swing.JToggleButton();
        seatA22 = new javax.swing.JToggleButton();
        spacerTB35 = new javax.swing.JToggleButton();
        spacerTB2 = new javax.swing.JToggleButton();
        seatB1 = new javax.swing.JToggleButton();
        seatB2 = new javax.swing.JToggleButton();
        seatB3 = new javax.swing.JToggleButton();
        seatB4 = new javax.swing.JToggleButton();
        seatB5 = new javax.swing.JToggleButton();
        seatB6 = new javax.swing.JToggleButton();
        seatB7 = new javax.swing.JToggleButton();
        seatB8 = new javax.swing.JToggleButton();
        seatB9 = new javax.swing.JToggleButton();
        seatB10 = new javax.swing.JToggleButton();
        seatB11 = new javax.swing.JToggleButton();
        seatB12 = new javax.swing.JToggleButton();
        seatB13 = new javax.swing.JToggleButton();
        seatB14 = new javax.swing.JToggleButton();
        seatB15 = new javax.swing.JToggleButton();
        seatB16 = new javax.swing.JToggleButton();
        seatB17 = new javax.swing.JToggleButton();
        seatB18 = new javax.swing.JToggleButton();
        seatB19 = new javax.swing.JToggleButton();
        seatB20 = new javax.swing.JToggleButton();
        seatB21 = new javax.swing.JToggleButton();
        seatB22 = new javax.swing.JToggleButton();
        spacerTB36 = new javax.swing.JToggleButton();
        spacerTB3 = new javax.swing.JToggleButton();
        seatC1 = new javax.swing.JToggleButton();
        seatC2 = new javax.swing.JToggleButton();
        seatC3 = new javax.swing.JToggleButton();
        seatC4 = new javax.swing.JToggleButton();
        seatC5 = new javax.swing.JToggleButton();
        seatC6 = new javax.swing.JToggleButton();
        seatC7 = new javax.swing.JToggleButton();
        seatC8 = new javax.swing.JToggleButton();
        seatC9 = new javax.swing.JToggleButton();
        seatC10 = new javax.swing.JToggleButton();
        seatC11 = new javax.swing.JToggleButton();
        seatC12 = new javax.swing.JToggleButton();
        seatC13 = new javax.swing.JToggleButton();
        seatC14 = new javax.swing.JToggleButton();
        seatC15 = new javax.swing.JToggleButton();
        seatC16 = new javax.swing.JToggleButton();
        seatC17 = new javax.swing.JToggleButton();
        seatC18 = new javax.swing.JToggleButton();
        seatC19 = new javax.swing.JToggleButton();
        seatC20 = new javax.swing.JToggleButton();
        seatC21 = new javax.swing.JToggleButton();
        seatC22 = new javax.swing.JToggleButton();
        spacerTB37 = new javax.swing.JToggleButton();
        spacerTB4 = new javax.swing.JToggleButton();
        seatD1 = new javax.swing.JToggleButton();
        seatD2 = new javax.swing.JToggleButton();
        seatD3 = new javax.swing.JToggleButton();
        seatD4 = new javax.swing.JToggleButton();
        seatD5 = new javax.swing.JToggleButton();
        seatD6 = new javax.swing.JToggleButton();
        seatD7 = new javax.swing.JToggleButton();
        seatD8 = new javax.swing.JToggleButton();
        seatD9 = new javax.swing.JToggleButton();
        seatD10 = new javax.swing.JToggleButton();
        seatD11 = new javax.swing.JToggleButton();
        seatD12 = new javax.swing.JToggleButton();
        seatD13 = new javax.swing.JToggleButton();
        seatD14 = new javax.swing.JToggleButton();
        seatD15 = new javax.swing.JToggleButton();
        seatD16 = new javax.swing.JToggleButton();
        seatD17 = new javax.swing.JToggleButton();
        seatD18 = new javax.swing.JToggleButton();
        seatD19 = new javax.swing.JToggleButton();
        seatD20 = new javax.swing.JToggleButton();
        seatD21 = new javax.swing.JToggleButton();
        seatD22 = new javax.swing.JToggleButton();
        spacerTB38 = new javax.swing.JToggleButton();
        spacerTB5 = new javax.swing.JToggleButton();
        spacerTB13 = new javax.swing.JToggleButton();
        spacerTB14 = new javax.swing.JToggleButton();
        spacerTB15 = new javax.swing.JToggleButton();
        spacerTB16 = new javax.swing.JToggleButton();
        spacerTB17 = new javax.swing.JToggleButton();
        spacerTB18 = new javax.swing.JToggleButton();
        spacerTB19 = new javax.swing.JToggleButton();
        spacerTB20 = new javax.swing.JToggleButton();
        spacerTB21 = new javax.swing.JToggleButton();
        spacerTB22 = new javax.swing.JToggleButton();
        spacerTB23 = new javax.swing.JToggleButton();
        spacerTB24 = new javax.swing.JToggleButton();
        spacerTB25 = new javax.swing.JToggleButton();
        spacerTB26 = new javax.swing.JToggleButton();
        spacerTB27 = new javax.swing.JToggleButton();
        spacerTB28 = new javax.swing.JToggleButton();
        spacerTB29 = new javax.swing.JToggleButton();
        spacerTB30 = new javax.swing.JToggleButton();
        spacerTB31 = new javax.swing.JToggleButton();
        spacerTB32 = new javax.swing.JToggleButton();
        spacerTB33 = new javax.swing.JToggleButton();
        spacerTB34 = new javax.swing.JToggleButton();
        spacerTB39 = new javax.swing.JToggleButton();
        spacerTB6 = new javax.swing.JToggleButton();
        seatE1 = new javax.swing.JToggleButton();
        seatE2 = new javax.swing.JToggleButton();
        seatE3 = new javax.swing.JToggleButton();
        seatE4 = new javax.swing.JToggleButton();
        seatE5 = new javax.swing.JToggleButton();
        seatE6 = new javax.swing.JToggleButton();
        seatE7 = new javax.swing.JToggleButton();
        spacerTB47 = new javax.swing.JToggleButton();
        seatE8 = new javax.swing.JToggleButton();
        seatE9 = new javax.swing.JToggleButton();
        seatE10 = new javax.swing.JToggleButton();
        spacerTB48 = new javax.swing.JToggleButton();
        seatE11 = new javax.swing.JToggleButton();
        seatE12 = new javax.swing.JToggleButton();
        seatE13 = new javax.swing.JToggleButton();
        seatE14 = new javax.swing.JToggleButton();
        spacerTB49 = new javax.swing.JToggleButton();
        seatE15 = new javax.swing.JToggleButton();
        seatE16 = new javax.swing.JToggleButton();
        seatE17 = new javax.swing.JToggleButton();
        seatE18 = new javax.swing.JToggleButton();
        seatE19 = new javax.swing.JToggleButton();
        spacerTB40 = new javax.swing.JToggleButton();
        spacerTB7 = new javax.swing.JToggleButton();
        seatF1 = new javax.swing.JToggleButton();
        seatF2 = new javax.swing.JToggleButton();
        seatF3 = new javax.swing.JToggleButton();
        seatF4 = new javax.swing.JToggleButton();
        seatF5 = new javax.swing.JToggleButton();
        seatF6 = new javax.swing.JToggleButton();
        seatF7 = new javax.swing.JToggleButton();
        seatF8 = new javax.swing.JToggleButton();
        seatF9 = new javax.swing.JToggleButton();
        seatF10 = new javax.swing.JToggleButton();
        seatF11 = new javax.swing.JToggleButton();
        seatF12 = new javax.swing.JToggleButton();
        seatF13 = new javax.swing.JToggleButton();
        seatF14 = new javax.swing.JToggleButton();
        seatF15 = new javax.swing.JToggleButton();
        seatF16 = new javax.swing.JToggleButton();
        seatF17 = new javax.swing.JToggleButton();
        seatF18 = new javax.swing.JToggleButton();
        seatF19 = new javax.swing.JToggleButton();
        seatF20 = new javax.swing.JToggleButton();
        seatF21 = new javax.swing.JToggleButton();
        seatF22 = new javax.swing.JToggleButton();
        spacerTB41 = new javax.swing.JToggleButton();
        spacerTB8 = new javax.swing.JToggleButton();
        seatG1 = new javax.swing.JToggleButton();
        seatG2 = new javax.swing.JToggleButton();
        seatG3 = new javax.swing.JToggleButton();
        seatG4 = new javax.swing.JToggleButton();
        seatG5 = new javax.swing.JToggleButton();
        seatG6 = new javax.swing.JToggleButton();
        seatG7 = new javax.swing.JToggleButton();
        seatG8 = new javax.swing.JToggleButton();
        seatG9 = new javax.swing.JToggleButton();
        seatG10 = new javax.swing.JToggleButton();
        seatG11 = new javax.swing.JToggleButton();
        seatG12 = new javax.swing.JToggleButton();
        seatG13 = new javax.swing.JToggleButton();
        seatG14 = new javax.swing.JToggleButton();
        seatG15 = new javax.swing.JToggleButton();
        seatG16 = new javax.swing.JToggleButton();
        seatG17 = new javax.swing.JToggleButton();
        seatG18 = new javax.swing.JToggleButton();
        seatG19 = new javax.swing.JToggleButton();
        seatG20 = new javax.swing.JToggleButton();
        seatG21 = new javax.swing.JToggleButton();
        seatG22 = new javax.swing.JToggleButton();
        spacerTB42 = new javax.swing.JToggleButton();
        spacerTB9 = new javax.swing.JToggleButton();
        seatH1 = new javax.swing.JToggleButton();
        seatH2 = new javax.swing.JToggleButton();
        seatH3 = new javax.swing.JToggleButton();
        seatH4 = new javax.swing.JToggleButton();
        seatH5 = new javax.swing.JToggleButton();
        seatH6 = new javax.swing.JToggleButton();
        seatH7 = new javax.swing.JToggleButton();
        seatH8 = new javax.swing.JToggleButton();
        seatH9 = new javax.swing.JToggleButton();
        seatH10 = new javax.swing.JToggleButton();
        seatH11 = new javax.swing.JToggleButton();
        seatH12 = new javax.swing.JToggleButton();
        seatH13 = new javax.swing.JToggleButton();
        seatH14 = new javax.swing.JToggleButton();
        seatH15 = new javax.swing.JToggleButton();
        seatH16 = new javax.swing.JToggleButton();
        seatH17 = new javax.swing.JToggleButton();
        seatH18 = new javax.swing.JToggleButton();
        seatH19 = new javax.swing.JToggleButton();
        seatH20 = new javax.swing.JToggleButton();
        seatH21 = new javax.swing.JToggleButton();
        seatH22 = new javax.swing.JToggleButton();
        spacerTB43 = new javax.swing.JToggleButton();
        spacerTB10 = new javax.swing.JToggleButton();
        seatI1 = new javax.swing.JToggleButton();
        seatI2 = new javax.swing.JToggleButton();
        seatI3 = new javax.swing.JToggleButton();
        seatI4 = new javax.swing.JToggleButton();
        seatI5 = new javax.swing.JToggleButton();
        seatI6 = new javax.swing.JToggleButton();
        seatI7 = new javax.swing.JToggleButton();
        seatI8 = new javax.swing.JToggleButton();
        seatI9 = new javax.swing.JToggleButton();
        seatI10 = new javax.swing.JToggleButton();
        seatI11 = new javax.swing.JToggleButton();
        seatI12 = new javax.swing.JToggleButton();
        seatI13 = new javax.swing.JToggleButton();
        seatI14 = new javax.swing.JToggleButton();
        seatI15 = new javax.swing.JToggleButton();
        seatI16 = new javax.swing.JToggleButton();
        seatI17 = new javax.swing.JToggleButton();
        seatI18 = new javax.swing.JToggleButton();
        seatI19 = new javax.swing.JToggleButton();
        seatI20 = new javax.swing.JToggleButton();
        seatI21 = new javax.swing.JToggleButton();
        seatI22 = new javax.swing.JToggleButton();
        spacerTB44 = new javax.swing.JToggleButton();
        spacerTB11 = new javax.swing.JToggleButton();
        seatJ1 = new javax.swing.JToggleButton();
        seatJ2 = new javax.swing.JToggleButton();
        seatJ3 = new javax.swing.JToggleButton();
        seatJ4 = new javax.swing.JToggleButton();
        seatJ5 = new javax.swing.JToggleButton();
        seatJ6 = new javax.swing.JToggleButton();
        seatJ7 = new javax.swing.JToggleButton();
        seatJ8 = new javax.swing.JToggleButton();
        seatJ9 = new javax.swing.JToggleButton();
        seatJ10 = new javax.swing.JToggleButton();
        seatJ11 = new javax.swing.JToggleButton();
        seatJ12 = new javax.swing.JToggleButton();
        seatJ13 = new javax.swing.JToggleButton();
        seatJ14 = new javax.swing.JToggleButton();
        seatJ15 = new javax.swing.JToggleButton();
        seatJ16 = new javax.swing.JToggleButton();
        seatJ17 = new javax.swing.JToggleButton();
        seatJ18 = new javax.swing.JToggleButton();
        seatJ19 = new javax.swing.JToggleButton();
        seatJ20 = new javax.swing.JToggleButton();
        seatJ21 = new javax.swing.JToggleButton();
        seatJ22 = new javax.swing.JToggleButton();
        spacerTB45 = new javax.swing.JToggleButton();
        spacerTB12 = new javax.swing.JToggleButton();
        seatK1 = new javax.swing.JToggleButton();
        seatK2 = new javax.swing.JToggleButton();
        seatK3 = new javax.swing.JToggleButton();
        seatK4 = new javax.swing.JToggleButton();
        seatK5 = new javax.swing.JToggleButton();
        seatK6 = new javax.swing.JToggleButton();
        seatK7 = new javax.swing.JToggleButton();
        seatK8 = new javax.swing.JToggleButton();
        seatK9 = new javax.swing.JToggleButton();
        seatK10 = new javax.swing.JToggleButton();
        seatK11 = new javax.swing.JToggleButton();
        seatK12 = new javax.swing.JToggleButton();
        seatK13 = new javax.swing.JToggleButton();
        seatK14 = new javax.swing.JToggleButton();
        seatK15 = new javax.swing.JToggleButton();
        seatK16 = new javax.swing.JToggleButton();
        seatK17 = new javax.swing.JToggleButton();
        seatK18 = new javax.swing.JToggleButton();
        seatK19 = new javax.swing.JToggleButton();
        seatK20 = new javax.swing.JToggleButton();
        seatK21 = new javax.swing.JToggleButton();
        seatK22 = new javax.swing.JToggleButton();
        spacerTB46 = new javax.swing.JToggleButton();
        seatL1 = new javax.swing.JToggleButton();
        seatL2 = new javax.swing.JToggleButton();
        seatL3 = new javax.swing.JToggleButton();
        seatL4 = new javax.swing.JToggleButton();
        seatL5 = new javax.swing.JToggleButton();
        seatL6 = new javax.swing.JToggleButton();
        seatL7 = new javax.swing.JToggleButton();
        seatL8 = new javax.swing.JToggleButton();
        seatL9 = new javax.swing.JToggleButton();
        seatL10 = new javax.swing.JToggleButton();
        seatL11 = new javax.swing.JToggleButton();
        seatL12 = new javax.swing.JToggleButton();
        seatL13 = new javax.swing.JToggleButton();
        seatL14 = new javax.swing.JToggleButton();
        seatL15 = new javax.swing.JToggleButton();
        seatL16 = new javax.swing.JToggleButton();
        seatL17 = new javax.swing.JToggleButton();
        seatL18 = new javax.swing.JToggleButton();
        seatL19 = new javax.swing.JToggleButton();
        seatL20 = new javax.swing.JToggleButton();
        seatL21 = new javax.swing.JToggleButton();
        seatL22 = new javax.swing.JToggleButton();
        seatL23 = new javax.swing.JToggleButton();
        seatL24 = new javax.swing.JToggleButton();
        spacerSouthPanel = new javax.swing.JPanel();
        spacerWestPanel = new javax.swing.JPanel();
        spacerEastPanel = new javax.swing.JPanel();
        transactionSummaryPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        spacerButtonTS1 = new javax.swing.JButton();
        movieImageTS = new javax.swing.JLabel();
        spacerButtonTS2 = new javax.swing.JButton();
        midPanel = new javax.swing.JPanel();
        movieTitleLabelTS = new javax.swing.JLabel();
        dateOfPurchaseLabelTS = new javax.swing.JLabel();
        auditoriumLabelTS = new javax.swing.JLabel();
        seatsSelectedLabelTS = new javax.swing.JLabel();
        totalCostPanel = new javax.swing.JPanel();
        seniorLabelTS = new javax.swing.JLabel();
        absoluteTotalTS = new javax.swing.JLabel();
        seniorCalculationLabel = new javax.swing.JLabel();
        convenienceFeeCalculationLabel = new javax.swing.JLabel();
        adultTotalTS = new javax.swing.JLabel();
        childCalculationLabel = new javax.swing.JLabel();
        adultCalculationLabel = new javax.swing.JLabel();
        convenienceFeeLabelTS = new javax.swing.JLabel();
        absoluteTotalLabelTS = new javax.swing.JLabel();
        childTotalTS = new javax.swing.JLabel();
        convenienceFeeTotalTS = new javax.swing.JLabel();
        adultLabelTS = new javax.swing.JLabel();
        childLabelTS = new javax.swing.JLabel();
        seniorTotalTS = new javax.swing.JLabel();
        csacpNavButtonPanel = new javax.swing.JPanel();
        cpagbButtonsPanel = new javax.swing.JPanel();
        csacpCancelPurchaseButton = new javax.swing.JButton();
        csacpBackButton = new javax.swing.JButton();
        csacpCompletePurchaseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new SetToFullScreenCode().getFullScreen());

        displayPanel.setLayout(new java.awt.CardLayout());

        logInScreen.setBackground(new java.awt.Color(0, 255, 51));
        logInScreen.setLayout(new java.awt.GridLayout(1, 2));

        leftHalf.setLayout(new java.awt.BorderLayout());

        logInInfoPanel.setBackground(new java.awt.Color(9, 64, 113));
        logInInfoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logInEmailTextField.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        logInEmailTextField.setForeground(new java.awt.Color(130, 123, 123));
        logInEmailTextField.setText("E-mail Address");
        logInEmailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                logInEmailTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                logInEmailTextFieldFocusLost(evt);
            }
        });
        logInInfoPanel.add(logInEmailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, 420, 70));

        logInPasswordField.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        logInPasswordField.setForeground(new java.awt.Color(130, 123, 123));
        logInPasswordField.setText("Password");
        logInPasswordField.setDoubleBuffered(true);
        logInPasswordField.setEchoChar((char)0);
        logInPasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                logInPasswordFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                logInPasswordFieldFocusLost(evt);
            }
        });
        logInPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInPasswordFieldActionPerformed(evt);
            }
        });
        logInInfoPanel.add(logInPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 500, 420, 70));

        signInButton.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        signInButton.setText("SIGN IN");
        signInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signInButtonActionPerformed(evt);
            }
        });
        logInInfoPanel.add(signInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 590, 420, 75));

        incorrectEmailOrPasswordLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        incorrectEmailOrPasswordLabel.setForeground(new java.awt.Color(255, 255, 255));
        incorrectEmailOrPasswordLabel.setText(" The email address or password is incorrect. Please try again.");
        logInInfoPanel.add(incorrectEmailOrPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, -1, -1));

        invalidLogInEmailLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        invalidLogInEmailLabel.setForeground(new java.awt.Color(255, 255, 255));
        invalidLogInEmailLabel.setText("Invalid Email");
        logInInfoPanel.add(invalidLogInEmailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, -1, -1));

        mtmLogo1.setFont(new java.awt.Font("Informal011 BT", 1, 48)); // NOI18N
        mtmLogo1.setForeground(new java.awt.Color(255, 255, 255));
        mtmLogo1.setText("MOvie Ticket Master");
        logInInfoPanel.add(mtmLogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, -1, -1));

        mtmLogo2.setFont(new java.awt.Font("Informal011 BT", 1, 150)); // NOI18N
        mtmLogo2.setForeground(new java.awt.Color(255, 255, 255));
        mtmLogo2.setText("MTM");
        logInInfoPanel.add(mtmLogo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, -1, -1));

        leftHalf.add(logInInfoPanel, java.awt.BorderLayout.CENTER);

        logInScreen.add(leftHalf);

        rightHalf.setLayout(new java.awt.BorderLayout());

        joinMessagePanel.setBackground(new java.awt.Color(102, 102, 102));
        joinMessagePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        jLabel2.setText("Join today!");
        joinMessagePanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 270, 150));

        joinPanelLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        joinPanelLabel1.setText("Not a member yet?");
        joinMessagePanel.add(joinPanelLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 520, 160));

        joinButton.setFont(new java.awt.Font("Lucida Grande", 1, 30)); // NOI18N
        joinButton.setText("JOIN");
        joinButton.setPreferredSize(new java.awt.Dimension(100, 75));
        joinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinButtonActionPerformed(evt);
            }
        });
        joinMessagePanel.add(joinButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 590, 420, 75));

        rightHalf.add(joinMessagePanel, java.awt.BorderLayout.CENTER);

        logInScreen.add(rightHalf);

        displayPanel.add(logInScreen, "card1");

        registerScreen.setLayout(new java.awt.BorderLayout());

        registerTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        registerTitlePanel.setPreferredSize(new java.awt.Dimension(399, 75));
        registerTitlePanel.setRequestFocusEnabled(false);
        registerTitlePanel.setLayout(new java.awt.GridBagLayout());

        csacpTitleLabel2.setFont(new java.awt.Font("Lucida Grande", 3, 48)); // NOI18N
        csacpTitleLabel2.setForeground(new java.awt.Color(255, 255, 255));
        csacpTitleLabel2.setText("Create Your Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        registerTitlePanel.add(csacpTitleLabel2, gridBagConstraints);

        mtmLogo4.setFont(new java.awt.Font("Informal011 BT", 1, 75)); // NOI18N
        mtmLogo4.setForeground(new java.awt.Color(255, 255, 255));
        mtmLogo4.setText("MTM");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 43);
        registerTitlePanel.add(mtmLogo4, gridBagConstraints);

        registerScreen.add(registerTitlePanel, java.awt.BorderLayout.NORTH);

        leftAndRightHalvesPanel.setLayout(new java.awt.GridLayout(1, 2));

        userInfoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        confirmPasswordField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        confirmPasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                confirmPasswordFieldFocusLost(evt);
            }
        });
        userInfoPanel.add(confirmPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 500, 450, 70));

        firstNameTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        firstNameTextField.setForeground(new java.awt.Color(130, 123, 123));
        firstNameTextField.setText("First");
        firstNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                firstNameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                firstNameTextFieldFocusLost(evt);
            }
        });
        userInfoPanel.add(firstNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 220, 70));

        confirmPasswordLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        confirmPasswordLabel.setText("Confirm your password");
        userInfoPanel.add(confirmPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 470, -1, -1));

        emailAddressLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        emailAddressLabel.setText("Enter Your E-mail Address");
        userInfoPanel.add(emailAddressLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, -1, -1));

        nameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        nameLabel.setText("Name");
        userInfoPanel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 110, 24));

        confirmEmailTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        confirmEmailTextField.setForeground(new java.awt.Color(130, 123, 123));
        confirmEmailTextField.setText("example@email.com");
        confirmEmailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                confirmEmailTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                confirmEmailTextFieldFocusLost(evt);
            }
        });
        userInfoPanel.add(confirmEmailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 450, 70));

        createPasswordField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        createPasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                createPasswordFieldFocusLost(evt);
            }
        });
        userInfoPanel.add(createPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, 450, 70));

        confirmEmailAddressLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        confirmEmailAddressLabel.setText("Confirm Your E-mail Address");
        userInfoPanel.add(confirmEmailAddressLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, -1, -1));

        lastNameTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        lastNameTextField.setForeground(new java.awt.Color(130, 123, 123));
        lastNameTextField.setText("Last");
        lastNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lastNameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                lastNameTextFieldFocusLost(evt);
            }
        });
        userInfoPanel.add(lastNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 60, 220, 70));

        createPasswordLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        createPasswordLabel.setText("Create a password");
        userInfoPanel.add(createPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, -1, -1));

        registerEmailTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        registerEmailTextField.setForeground(new java.awt.Color(130, 123, 123));
        registerEmailTextField.setText("example@email.com");
        registerEmailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                registerEmailTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                registerEmailTextFieldFocusLost(evt);
            }
        });
        userInfoPanel.add(registerEmailTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 170, 450, 70));

        invalidNameLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidNameLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidNameLabel.setText("Invalid Name");
        userInfoPanel.add(invalidNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, -1, -1));

        invalidRegisterEmailLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidRegisterEmailLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidRegisterEmailLabel.setText("Invalid Email");
        userInfoPanel.add(invalidRegisterEmailLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, -1, -1));

        emailsNoMatchLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        emailsNoMatchLabel.setForeground(new java.awt.Color(255, 0, 51));
        emailsNoMatchLabel.setText("Emails do not match");
        userInfoPanel.add(emailsNoMatchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, -1, -1));

        passwordsNoMatchLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        passwordsNoMatchLabel.setForeground(new java.awt.Color(255, 0, 51));
        passwordsNoMatchLabel.setText("Passwords do not match");
        userInfoPanel.add(passwordsNoMatchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, -1, -1));

        invalidLNameLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidLNameLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidLNameLabel.setText("Invalid Name");
        userInfoPanel.add(invalidLNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, -1, -1));

        invalidPasswordTextPane.setBackground(new java.awt.Color(238, 238, 238));
        invalidPasswordTextPane.setBorder(null);
        invalidPasswordTextPane.setFont(new java.awt.Font("Lucida Grande", 0, 19)); // NOI18N
        invalidPasswordTextPane.setForeground(new java.awt.Color(255, 0, 51));
        invalidPasswordTextPane.setText("Invalid Password.\nMake sure your password meets these conditions:\n-at least 8 characters\n-includes at least one digit\n-includes at least one lower case letter\n-includes at least one upper case letter\n-includes at least one special character (ex. @#$%^&+)\n-no whitespace allowed");
        invalidPasswordTextPane.setOpaque(false);
        userInfoPanel.add(invalidPasswordTextPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 580, 540, 190));

        leftAndRightHalvesPanel.add(userInfoPanel);

        paymentInfoPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cityTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        cityTextField.setForeground(new java.awt.Color(130, 123, 123));
        cityTextField.setText("City");
        cityTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cityTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cityTextFieldFocusLost(evt);
            }
        });
        paymentInfoPanel.add(cityTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 450, 70));

        streetAddressTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        streetAddressTextField.setForeground(new java.awt.Color(130, 123, 123));
        streetAddressTextField.setText("Street Address");
        streetAddressTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                streetAddressTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                streetAddressTextFieldFocusLost(evt);
            }
        });
        paymentInfoPanel.add(streetAddressTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 450, 70));

        zipCodeTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        zipCodeTextField.setForeground(new java.awt.Color(130, 123, 123));
        zipCodeTextField.setText("Zip Code");
        zipCodeTextField.setToolTipText("");
        zipCodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                zipCodeTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                zipCodeTextFieldFocusLost(evt);
            }
        });
        paymentInfoPanel.add(zipCodeTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 260, 150, 70));

        statesComboBox.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        statesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AL-ALABAMA", "AK-ALASKA", "AZ-ARIZONA", "AR-ARKANSAS", "CA-CALIFORNIA", "CO-COLORADO", "CT-CONNECTICUT", "DE-DELAWARE", "FL-FLORIDA", "GA-GEORGIA", "HI-HAWAII", "ID-IDAHO", "IL-ILLINOIS", "IN-INDIANA", "IA-IOWA", "KS-KANSAS", "KY-KENTUCKY", "LA-LOUISIANA", "ME-MAINE", "MD-MARYLAND", "MA-MASSACHUSSETTS", "MI-MICHIGAN", "MN-MINNESOTA", "MS-MISSISSIPPI", "MO-MISSOURI", "MT-MONTANA", "NE-NEBRASKA", "NV-NEVADA", "NH-NEW HAMPSHIRE", "NJ-NEW JERSEY", "NM-NEW MEXICO", "NY-NEW YORK", "NC-NORTH CAROLINA", "ND-NORTH DAKOTA", "OH-OHIO", "OK-OKLAHOMA", "OR-OREGON", "PA-PENNSYLVANIA", "RI-RHODE ISLAND", "SC-SOUTH CAROLINA", "SD-SOUTH DAKOTA", "TN-TENNESSEE", "TX-TEXAS", "UT-UTAH", "VT-VERMONT", "VA-VIRGINIA", "WA-WASHINGTON", "WV-WEST VIRGINIA", "WI-WISCONSIN", "WY-WYOMING" }));
        paymentInfoPanel.add(statesComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 260, 300, 70));

        creditCardNumberTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        creditCardNumberTextField.setForeground(new java.awt.Color(130, 123, 123));
        creditCardNumberTextField.setText("Credit Card Number");
        creditCardNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                creditCardNumberTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                creditCardNumberTextFieldFocusLost(evt);
            }
        });
        paymentInfoPanel.add(creditCardNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 390, 450, 70));

        ccExpMonthLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        ccExpMonthLabel.setText("Month");
        paymentInfoPanel.add(ccExpMonthLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 470, -1, -1));

        ccExpMonthComboBox.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        ccExpMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        paymentInfoPanel.add(ccExpMonthComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 500, 90, 70));

        ccExpYearComboBox.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        ccExpYearComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2014", "2015", "2016", "2017", "2018", "2019", "2020" }));
        paymentInfoPanel.add(ccExpYearComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 500, 130, 70));

        billingAddressLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        billingAddressLabel1.setText("Billing Address");
        paymentInfoPanel.add(billingAddressLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 210, 30));

        securityCodeTextField.setFont(new java.awt.Font("Lucida Grande", 0, 28)); // NOI18N
        securityCodeTextField.setForeground(new java.awt.Color(130, 123, 123));
        securityCodeTextField.setText("Security Code");
        securityCodeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                securityCodeTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                securityCodeTextFieldFocusLost(evt);
            }
        });
        paymentInfoPanel.add(securityCodeTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 500, 210, 70));

        billingAddressLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        billingAddressLabel2.setText("Credit Card Info");
        paymentInfoPanel.add(billingAddressLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 360, 220, 24));

        ccExpYearLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
        ccExpYearLabel.setText("Year");
        paymentInfoPanel.add(ccExpYearLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 470, -1, -1));

        invalidCityLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidCityLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidCityLabel.setText("Invalid City");
        paymentInfoPanel.add(invalidCityLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        invalidZipCodeLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidZipCodeLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidZipCodeLabel.setText("Invalid ZipCode");
        paymentInfoPanel.add(invalidZipCodeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 330, -1, -1));

        invalidSCCLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidSCCLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidSCCLabel.setText("Invalid SCC#");
        paymentInfoPanel.add(invalidSCCLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 580, -1, -1));

        invalidCCLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        invalidCCLabel.setForeground(new java.awt.Color(255, 0, 51));
        invalidCCLabel.setText("Invalid CC#");
        paymentInfoPanel.add(invalidCCLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, -1, -1));

        leftAndRightHalvesPanel.add(paymentInfoPanel);

        registerScreen.add(leftAndRightHalvesPanel, java.awt.BorderLayout.CENTER);

        registerScreenButtonPanel.setBackground(new java.awt.Color(9, 64, 113));
        registerScreenButtonPanel.setLayout(new java.awt.GridLayout(1, 0));

        cancelRegistrationButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        cancelRegistrationButton.setText("Cancel Registration");
        cancelRegistrationButton.setPreferredSize(new java.awt.Dimension(211, 100));
        cancelRegistrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelRegistrationButtonActionPerformed(evt);
            }
        });
        registerScreenButtonPanel.add(cancelRegistrationButton);

        submitRegistrationButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        submitRegistrationButton.setText("Submit Registration");
        submitRegistrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitRegistrationButtonActionPerformed(evt);
            }
        });
        registerScreenButtonPanel.add(submitRegistrationButton);

        registerScreen.add(registerScreenButtonPanel, java.awt.BorderLayout.SOUTH);

        displayPanel.add(registerScreen, "card2");

        chooseAMovieScreen.setLayout(new java.awt.BorderLayout());

        chooseAMovieTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        chooseAMovieTitlePanel.setPreferredSize(new java.awt.Dimension(399, 75));
        chooseAMovieTitlePanel.setRequestFocusEnabled(false);
        chooseAMovieTitlePanel.setLayout(new java.awt.GridBagLayout());

        camTitleLabel.setFont(new java.awt.Font("Lucida Grande", 3, 48)); // NOI18N
        camTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        camTitleLabel.setText("Choose a movie and proceed to next step");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        chooseAMovieTitlePanel.add(camTitleLabel, gridBagConstraints);

        mtmLogo3.setFont(new java.awt.Font("Informal011 BT", 1, 75)); // NOI18N
        mtmLogo3.setForeground(new java.awt.Color(255, 255, 255));
        mtmLogo3.setText("MTM");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 43);
        chooseAMovieTitlePanel.add(mtmLogo3, gridBagConstraints);

        chooseAMovieScreen.add(chooseAMovieTitlePanel, java.awt.BorderLayout.NORTH);

        moviesDisplayPanel.setLayout(new java.awt.CardLayout());

        row1Movies.setBackground(new java.awt.Color(9, 64, 113));
        row1Movies.setLayout(new java.awt.GridLayout(1, 3));

        movieSelectedButtonGroup.add(movie1JToggleButton);
        movie1JToggleButton.setActionCommand("movie1");
        movie1JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row1Movies.add(movie1JToggleButton);

        movieSelectedButtonGroup.add(movie2JToggleButton);
        movie2JToggleButton.setActionCommand("movie2");
        movie2JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row1Movies.add(movie2JToggleButton);

        movieSelectedButtonGroup.add(movie3JToggleButton);
        movie3JToggleButton.setActionCommand("movie3");
        movie3JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row1Movies.add(movie3JToggleButton);

        moviesDisplayPanel.add(row1Movies, "card1");

        row2Movies.setBackground(new java.awt.Color(9, 64, 113));
        row2Movies.setLayout(new java.awt.GridLayout(1, 3));

        movieSelectedButtonGroup.add(movie4JToggleButton);
        movie4JToggleButton.setActionCommand("movie4");
        movie4JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row2Movies.add(movie4JToggleButton);

        movieSelectedButtonGroup.add(movie5JToggleButton);
        movie5JToggleButton.setActionCommand("movie5");
        movie5JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row2Movies.add(movie5JToggleButton);

        movieSelectedButtonGroup.add(movie6JToggleButton);
        movie6JToggleButton.setActionCommand("movie6");
        movie6JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row2Movies.add(movie6JToggleButton);

        moviesDisplayPanel.add(row2Movies, "card2");

        row3Movies.setBackground(new java.awt.Color(9, 64, 113));
        row3Movies.setLayout(new java.awt.GridLayout(1, 3));

        movieSelectedButtonGroup.add(movie7JToggleButton);
        movie7JToggleButton.setActionCommand("movie7");
        movie7JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row3Movies.add(movie7JToggleButton);

        movieSelectedButtonGroup.add(movie8JToggleButton);
        movie8JToggleButton.setActionCommand("movie8");
        movie8JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row3Movies.add(movie8JToggleButton);

        movieSelectedButtonGroup.add(movie9JToggleButton);
        movie9JToggleButton.setActionCommand("movie9");
        movie9JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row3Movies.add(movie9JToggleButton);

        moviesDisplayPanel.add(row3Movies, "card3");

        row4Movies.setBackground(new java.awt.Color(9, 64, 113));
        row4Movies.setLayout(new java.awt.GridLayout(1, 3));

        movieSelectedButtonGroup.add(movie10JToggleButton);
        movie10JToggleButton.setActionCommand("movie10");
        movie10JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row4Movies.add(movie10JToggleButton);

        movieSelectedButtonGroup.add(movie11JToggleButton);
        movie11JToggleButton.setActionCommand("movie11");
        movie11JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row4Movies.add(movie11JToggleButton);

        movieSelectedButtonGroup.add(movie12JToggleButton);
        movie12JToggleButton.setActionCommand("movie12");
        movie12JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row4Movies.add(movie12JToggleButton);

        moviesDisplayPanel.add(row4Movies, "card4");

        row5Movies.setBackground(new java.awt.Color(9, 64, 113));
        row5Movies.setLayout(new java.awt.GridLayout(1, 3));

        movieSelectedButtonGroup.add(movie13JToggleButton);
        movie13JToggleButton.setActionCommand("movie13");
        movie13JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row5Movies.add(movie13JToggleButton);

        movieSelectedButtonGroup.add(movie14JToggleButton);
        movie14JToggleButton.setActionCommand("movie14");
        movie14JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row5Movies.add(movie14JToggleButton);

        movieSelectedButtonGroup.add(movie15JToggleButton);
        movie15JToggleButton.setActionCommand("movie15");
        movie15JToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonsListener(evt);
            }
        });
        row5Movies.add(movie15JToggleButton);

        moviesDisplayPanel.add(row5Movies, "card5");

        chooseAMovieScreen.add(moviesDisplayPanel, java.awt.BorderLayout.CENTER);

        camNavButtonPanel.setBackground(new java.awt.Color(9, 64, 113));
        camNavButtonPanel.setPreferredSize(new java.awt.Dimension(399, 100));
        camNavButtonPanel.setRequestFocusEnabled(false);
        camNavButtonPanel.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        camCancelPurchaseButton.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        camCancelPurchaseButton.setText("CANCEL PURCHASE");
        camCancelPurchaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                camCancelPurchaseButtonActionPerformed(evt);
            }
        });
        camNavButtonPanel.add(camCancelPurchaseButton);

        upButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        upButton.setText("UP");
        upButton.setEnabled(false);
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        camNavButtonPanel.add(upButton);

        downButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        downButton.setText("DOWN");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        camNavButtonPanel.add(downButton);

        camNextButton.setFont(new java.awt.Font("Lucida Grande", 1, 60)); // NOI18N
        camNextButton.setText("NEXT»--->");
        camNextButton.setEnabled(false);
        camNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                camNextButtonActionPerformed(evt);
            }
        });
        camNavButtonPanel.add(camNextButton);

        chooseAMovieScreen.add(camNavButtonPanel, java.awt.BorderLayout.SOUTH);

        displayPanel.add(chooseAMovieScreen, "card3");

        chooseTimeAndTicketsScreen.setLayout(new java.awt.BorderLayout());

        chooseTimeAndTicketsTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        chooseTimeAndTicketsTitlePanel.setPreferredSize(new java.awt.Dimension(399, 75));
        chooseTimeAndTicketsTitlePanel.setRequestFocusEnabled(false);
        chooseTimeAndTicketsTitlePanel.setLayout(new java.awt.GridBagLayout());

        chooseTimeAndTicketsLabel.setFont(new java.awt.Font("Lucida Grande", 3, 40)); // NOI18N
        chooseTimeAndTicketsLabel.setForeground(new java.awt.Color(255, 255, 255));
        chooseTimeAndTicketsLabel.setText("Choose experience, showing, tickets and proceed to next step");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        chooseTimeAndTicketsTitlePanel.add(chooseTimeAndTicketsLabel, gridBagConstraints);

        mtmLogo5.setFont(new java.awt.Font("Informal011 BT", 1, 75)); // NOI18N
        mtmLogo5.setForeground(new java.awt.Color(255, 255, 255));
        mtmLogo5.setText("MTM");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 35);
        chooseTimeAndTicketsTitlePanel.add(mtmLogo5, gridBagConstraints);

        chooseTimeAndTicketsScreen.add(chooseTimeAndTicketsTitlePanel, java.awt.BorderLayout.NORTH);

        chooseTTdisplayPanel.setLayout(new java.awt.GridLayout(1, 3));

        selectedMoviePanel.setBackground(new java.awt.Color(9, 64, 113));
        selectedMoviePanel.setLayout(new java.awt.CardLayout());

        scmovie1JToggleButton.setActionCommand("movie1");
        scmovie1JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie1JToggleButton, "card1");

        scmovie2JToggleButton.setActionCommand("movie2");
        scmovie2JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie2JToggleButton, "card2");

        scmovie3JToggleButton.setActionCommand("movie3");
        scmovie3JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie3JToggleButton, "card3");

        scmovie4JToggleButton.setActionCommand("movie4");
        scmovie4JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie4JToggleButton, "card4");

        scmovie5JToggleButton.setActionCommand("movie5");
        scmovie5JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie5JToggleButton, "card5");

        scmovie6JToggleButton.setActionCommand("movie6");
        scmovie6JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie6JToggleButton, "card6");

        scmovie7JToggleButton.setActionCommand("movie7");
        scmovie7JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie7JToggleButton, "card7");

        scmovie8JToggleButton.setActionCommand("movie8");
        scmovie8JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie8JToggleButton, "card8");

        scmovie9JToggleButton.setActionCommand("movie9");
        scmovie9JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie9JToggleButton, "card9");

        scmovie10JToggleButton.setActionCommand("movie10");
        scmovie10JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie10JToggleButton, "card10");

        scmovie11JToggleButton.setActionCommand("movie11");
        scmovie11JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie11JToggleButton, "card11");

        scmovie12JToggleButton.setActionCommand("movie12");
        scmovie12JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie12JToggleButton, "card12");

        scmovie13JToggleButton.setActionCommand("movie13");
        scmovie13JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie13JToggleButton, "card13");

        scmovie14JToggleButton.setActionCommand("movie14");
        scmovie14JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie14JToggleButton, "card14");

        scmovie15JToggleButton.setActionCommand("movie15");
        scmovie15JToggleButton.setEnabled(false);
        selectedMoviePanel.add(scmovie15JToggleButton, "card15");

        chooseTTdisplayPanel.add(selectedMoviePanel);

        chooseTimeAnd3DPanel.setBackground(new java.awt.Color(9, 64, 113));
        chooseTimeAnd3DPanel.setLayout(new java.awt.GridLayout(2, 1));

        twoDor3DPanel.setLayout(new java.awt.BorderLayout());

        movieExperienceTitle.setBackground(new java.awt.Color(9, 64, 113));
        movieExperienceTitle.setPreferredSize(new java.awt.Dimension(399, 75));
        movieExperienceTitle.setRequestFocusEnabled(false);
        movieExperienceTitle.setLayout(new java.awt.GridBagLayout());

        movieExerienceLabel.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        movieExerienceLabel.setForeground(new java.awt.Color(255, 255, 255));
        movieExerienceLabel.setText("       Movie Experience        ");
        movieExerienceLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 6, true));
        movieExperienceTitle.add(movieExerienceLabel, new java.awt.GridBagConstraints());

        twoDor3DPanel.add(movieExperienceTitle, java.awt.BorderLayout.NORTH);

        twoD3DandTitlesPanel.setBackground(new java.awt.Color(9, 64, 113));
        twoD3DandTitlesPanel.setLayout(new java.awt.GridLayout(1, 3));

        movieExperienceButtonGroup.add(twoDJToggleButton);
        twoDJToggleButton.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        twoDJToggleButton.setText("2D");
        twoDJToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                movieExperienceButtonsActionPerformed(evt);
            }
        });
        twoD3DandTitlesPanel.add(twoDJToggleButton);

        movieExperienceButtonGroup.add(threeDJToggleButton);
        threeDJToggleButton.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        threeDJToggleButton.setText("3D");
        threeDJToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                movieExperienceButtonsActionPerformed(evt);
            }
        });
        twoD3DandTitlesPanel.add(threeDJToggleButton);

        movieExperienceButtonGroup.add(imaxJToggleButton);
        imaxJToggleButton.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        imaxJToggleButton.setText("IMAX");
        imaxJToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                movieExperienceButtonsActionPerformed(evt);
            }
        });
        twoD3DandTitlesPanel.add(imaxJToggleButton);

        twoDor3DPanel.add(twoD3DandTitlesPanel, java.awt.BorderLayout.CENTER);

        movieTimesTitle.setBackground(new java.awt.Color(9, 64, 113));
        movieTimesTitle.setPreferredSize(new java.awt.Dimension(399, 75));
        movieTimesTitle.setRequestFocusEnabled(false);
        movieTimesTitle.setLayout(new java.awt.GridBagLayout());

        movieTimesLabel.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        movieTimesLabel.setForeground(new java.awt.Color(255, 255, 255));
        movieTimesLabel.setText("      Available Showings      ");
        movieTimesLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 6, true));
        movieTimesTitle.add(movieTimesLabel, new java.awt.GridBagConstraints());

        twoDor3DPanel.add(movieTimesTitle, java.awt.BorderLayout.SOUTH);

        chooseTimeAnd3DPanel.add(twoDor3DPanel);

        movieTimesPanel.setBackground(new java.awt.Color(9, 64, 113));
        movieTimesPanel.setLayout(new java.awt.GridLayout(4, 2));
        chooseTimeAnd3DPanel.add(movieTimesPanel);

        chooseTTdisplayPanel.add(chooseTimeAnd3DPanel);

        chooseTicketQty.setLayout(new java.awt.BorderLayout());

        movieTicketsQtyTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        movieTicketsQtyTitlePanel.setLayout(new java.awt.GridBagLayout());

        mtqtLabel.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        mtqtLabel.setForeground(new java.awt.Color(255, 255, 255));
        mtqtLabel.setText("       How many tickets?       ");
        mtqtLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 6, true));
        mtqtLabel.setInheritsPopupMenu(false);
        movieTicketsQtyTitlePanel.add(mtqtLabel, new java.awt.GridBagConstraints());

        chooseTicketQty.add(movieTicketsQtyTitlePanel, java.awt.BorderLayout.NORTH);

        threeTicketOptionsPanel.setBackground(new java.awt.Color(9, 64, 113));
        threeTicketOptionsPanel.setLayout(new java.awt.GridLayout(7, 1));

        adultTicketsTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        adultTicketsTitlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 50));

        adultTicketLabel.setFont(new java.awt.Font("Lucida Grande", 3, 36)); // NOI18N
        adultTicketLabel.setForeground(new java.awt.Color(255, 255, 255));
        adultTicketLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adultTicketLabel.setText("Adult");
        adultTicketsTitlePanel.add(adultTicketLabel);

        threeTicketOptionsPanel.add(adultTicketsTitlePanel);

        adultTicketsCalculationPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(9, 64, 113), 3, true));
        adultTicketsCalculationPanel.setLayout(new java.awt.GridBagLayout());

        adultSpinnerPanel.setLayout(new java.awt.GridLayout(1, 1));

        adultTicketsSpinner.setFont(new java.awt.Font("Lucida Grande", 0, 48)); // NOI18N
        adultTicketsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ticketSpinnersStateChanged(evt);
            }
        });
        adultSpinnerPanel.add(adultTicketsSpinner);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        adultTicketsCalculationPanel.add(adultSpinnerPanel, gridBagConstraints);

        adultPriceLabel.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        adultPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adultPriceLabel.setText("x$11.50 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        adultTicketsCalculationPanel.add(adultPriceLabel, gridBagConstraints);

        adultTotalTextField.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        adultTotalTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        adultTotalTextField.setText("  $0.0  ");
        adultTotalTextField.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 35);
        adultTicketsCalculationPanel.add(adultTotalTextField, gridBagConstraints);

        threeTicketOptionsPanel.add(adultTicketsCalculationPanel);

        seniorTicketsTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        seniorTicketsTitlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 55));

        seniorTicketLabel.setFont(new java.awt.Font("Lucida Grande", 3, 36)); // NOI18N
        seniorTicketLabel.setForeground(new java.awt.Color(255, 255, 255));
        seniorTicketLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seniorTicketLabel.setText("Senior");
        seniorTicketsTitlePanel.add(seniorTicketLabel);

        threeTicketOptionsPanel.add(seniorTicketsTitlePanel);

        seniorTicketsCalculationPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(9, 64, 113), 3, true));
        seniorTicketsCalculationPanel.setLayout(new java.awt.GridBagLayout());

        seniorSpinnerPanel.setLayout(new java.awt.GridLayout(1, 1));

        seniorTicketsSpinner.setFont(new java.awt.Font("Lucida Grande", 0, 48)); // NOI18N
        seniorTicketsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ticketSpinnersStateChanged(evt);
            }
        });
        seniorSpinnerPanel.add(seniorTicketsSpinner);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        seniorTicketsCalculationPanel.add(seniorSpinnerPanel, gridBagConstraints);

        seniorPriceLabel.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        seniorPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        seniorPriceLabel.setText("x$11.50 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        seniorTicketsCalculationPanel.add(seniorPriceLabel, gridBagConstraints);

        seniorTotalTextField.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        seniorTotalTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        seniorTotalTextField.setText("  $0.0  ");
        seniorTotalTextField.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 35);
        seniorTicketsCalculationPanel.add(seniorTotalTextField, gridBagConstraints);

        threeTicketOptionsPanel.add(seniorTicketsCalculationPanel);

        childTicketsTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        childTicketsTitlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 55));

        childTicketLabel.setFont(new java.awt.Font("Lucida Grande", 3, 36)); // NOI18N
        childTicketLabel.setForeground(new java.awt.Color(255, 255, 255));
        childTicketLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        childTicketLabel.setText("Child");
        childTicketsTitlePanel.add(childTicketLabel);

        threeTicketOptionsPanel.add(childTicketsTitlePanel);

        childTicketsCalculationPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(9, 64, 113), 3, true));
        childTicketsCalculationPanel.setLayout(new java.awt.GridBagLayout());

        childSpinnerPanel.setLayout(new java.awt.GridLayout(1, 1));

        childTicketsSpinner.setFont(new java.awt.Font("Lucida Grande", 0, 48)); // NOI18N
        childTicketsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ticketSpinnersStateChanged(evt);
            }
        });
        childSpinnerPanel.add(childTicketsSpinner);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        childTicketsCalculationPanel.add(childSpinnerPanel, gridBagConstraints);

        childPriceLabel.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        childPriceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        childPriceLabel.setText("x$11.50 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        childTicketsCalculationPanel.add(childPriceLabel, gridBagConstraints);

        childTotalTextField.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        childTotalTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        childTotalTextField.setText("  $0.0  ");
        childTotalTextField.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 35);
        childTicketsCalculationPanel.add(childTotalTextField, gridBagConstraints);

        threeTicketOptionsPanel.add(childTicketsCalculationPanel);

        spaceHTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        spaceHTitlePanel.add(spaceHLabel);

        threeTicketOptionsPanel.add(spaceHTitlePanel);

        chooseTicketQty.add(threeTicketOptionsPanel, java.awt.BorderLayout.CENTER);

        chooseTTdisplayPanel.add(chooseTicketQty);

        chooseTimeAndTicketsScreen.add(chooseTTdisplayPanel, java.awt.BorderLayout.CENTER);

        ctatNavButtonPanel.setBackground(new java.awt.Color(9, 64, 113));
        ctatNavButtonPanel.setPreferredSize(new java.awt.Dimension(399, 100));
        ctatNavButtonPanel.setRequestFocusEnabled(false);
        ctatNavButtonPanel.setLayout(new java.awt.GridLayout(1, 3, 5, 0));

        ctatCancelPurchaseButton.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        ctatCancelPurchaseButton.setText("CANCEL PURCHASE");
        ctatCancelPurchaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctatCancelPurchaseButtonActionPerformed(evt);
            }
        });
        ctatNavButtonPanel.add(ctatCancelPurchaseButton);

        ctatBackButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        ctatBackButton.setText("<--«GO BACK");
        ctatBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctatBackButtonActionPerformed(evt);
            }
        });
        ctatNavButtonPanel.add(ctatBackButton);

        ctatNextButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        ctatNextButton.setText("NEXT»--->");
        ctatNextButton.setEnabled(false);
        ctatNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ctatNextButtonActionPerformed(evt);
            }
        });
        ctatNavButtonPanel.add(ctatNextButton);

        chooseTimeAndTicketsScreen.add(ctatNavButtonPanel, java.awt.BorderLayout.SOUTH);

        displayPanel.add(chooseTimeAndTicketsScreen, "card4");

        chooseSeatsAndCompletePurchaseScreen.setLayout(new java.awt.BorderLayout());

        csacpTitlePanel.setBackground(new java.awt.Color(9, 64, 113));
        csacpTitlePanel.setPreferredSize(new java.awt.Dimension(399, 75));
        csacpTitlePanel.setRequestFocusEnabled(false);
        csacpTitlePanel.setLayout(new java.awt.GridBagLayout());

        csacpTitleLabel.setFont(new java.awt.Font("Lucida Grande", 3, 48)); // NOI18N
        csacpTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        csacpTitleLabel.setText("Choose your seats and Complete purchase");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        csacpTitlePanel.add(csacpTitleLabel, gridBagConstraints);

        mtmLogo.setFont(new java.awt.Font("Informal011 BT", 1, 75)); // NOI18N
        mtmLogo.setForeground(new java.awt.Color(255, 255, 255));
        mtmLogo.setText("MTM");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 43);
        csacpTitlePanel.add(mtmLogo, gridBagConstraints);

        chooseSeatsAndCompletePurchaseScreen.add(csacpTitlePanel, java.awt.BorderLayout.NORTH);

        csacpDisplayPanel.setLayout(new java.awt.BorderLayout());

        selectSeatsPanel.setBackground(new java.awt.Color(255, 153, 153));
        selectSeatsPanel.setLayout(new java.awt.BorderLayout());

        seatLegendPanel.setBackground(new java.awt.Color(204, 204, 204));
        seatLegendPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        seatLegendPanel.setMinimumSize(new java.awt.Dimension(200, 56));
        seatLegendPanel.setPreferredSize(new java.awt.Dimension(399, 75));
        seatLegendPanel.setRequestFocusEnabled(false);
        seatLegendPanel.setLayout(new java.awt.GridLayout(1, 2));

        availablePanel.setBackground(new java.awt.Color(204, 204, 204));
        availablePanel.setLayout(new java.awt.GridLayout(1, 0));

        availableLeftPanel.setBackground(new java.awt.Color(204, 204, 204));
        availableLeftPanel.setLayout(new java.awt.GridLayout(1, 0));

        spacerToggleButtonTS1.setEnabled(false);
        availableLeftPanel.add(spacerToggleButtonTS1);

        resizeTBPanel.setBackground(new java.awt.Color(204, 204, 204));
        resizeTBPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        resizeTBPanel.add(availableToggleButtonTS, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 50, 50));

        availableLeftPanel.add(resizeTBPanel);

        availablePanel.add(availableLeftPanel);

        availableButtonLabel.setFont(new java.awt.Font("Lucida Grande", 1, 20)); // NOI18N
        availableButtonLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        availableButtonLabel.setText("SEAT AVAILABLE");
        availablePanel.add(availableButtonLabel);

        seatLegendPanel.add(availablePanel);

        unavailablePanel.setBackground(new java.awt.Color(204, 204, 204));
        unavailablePanel.setLayout(new java.awt.GridLayout(1, 0));

        unavailableRightPanel.setBackground(new java.awt.Color(204, 204, 204));
        unavailableRightPanel.setLayout(new java.awt.GridLayout(1, 0));

        spacerToggleButtonTS2.setEnabled(false);
        unavailableRightPanel.add(spacerToggleButtonTS2);

        resizeTBPanel2.setBackground(new java.awt.Color(204, 204, 204));
        resizeTBPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        unavailableToggleButtonTS.setEnabled(false);
        resizeTBPanel2.add(unavailableToggleButtonTS, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 50, 50));

        unavailableRightPanel.add(resizeTBPanel2);

        unavailablePanel.add(unavailableRightPanel);

        unavailableButtonLabel.setBackground(new java.awt.Color(204, 204, 204));
        unavailableButtonLabel.setFont(new java.awt.Font("Lucida Grande", 1, 20)); // NOI18N
        unavailableButtonLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        unavailableButtonLabel.setText("SEAT UNAVAILABLE");
        unavailablePanel.add(unavailableButtonLabel);

        seatLegendPanel.add(unavailablePanel);

        selectSeatsPanel.add(seatLegendPanel, java.awt.BorderLayout.NORTH);

        seatsDiagramPanel.setLayout(new java.awt.BorderLayout());

        screenLabelPanel.setBackground(new java.awt.Color(204, 204, 204));
        screenLabelPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        screenLabelPanel.setPreferredSize(new java.awt.Dimension(1045, 50));
        screenLabelPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        screenLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        screenLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        screenLabel.setText("SCREEN");
        screenLabel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));
        screenLabel.setFocusable(false);
        screenLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        screenLabelPanel.add(screenLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 830, 30));

        seatsDiagramPanel.add(screenLabelPanel, java.awt.BorderLayout.NORTH);

        seatToggleButtonsPanel.setBackground(new java.awt.Color(204, 204, 204));
        seatToggleButtonsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        seatToggleButtonsPanel.setLayout(new java.awt.GridLayout(13, 24, 4, 10));

        spacerTB1.setToolTipText("");
        spacerTB1.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB1);

        seatA1.setToolTipText("");
        seatA1.setActionCommand("A1");
        seatA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA1);

        seatA2.setToolTipText("");
        seatA2.setActionCommand("A2");
        seatA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA2);

        seatA3.setToolTipText("");
        seatA3.setActionCommand("A3");
        seatA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA3);

        seatA4.setToolTipText("");
        seatA4.setActionCommand("A4");
        seatA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA4);

        seatA5.setToolTipText("");
        seatA5.setActionCommand("A5");
        seatA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA5);

        seatA6.setToolTipText("");
        seatA6.setActionCommand("A6");
        seatA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA6);

        seatA7.setToolTipText("");
        seatA7.setActionCommand("A7");
        seatA7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA7);

        seatA8.setToolTipText("");
        seatA8.setActionCommand("A8");
        seatA8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA8);

        seatA9.setToolTipText("");
        seatA9.setActionCommand("A9");
        seatA9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA9);

        seatA10.setToolTipText("");
        seatA10.setActionCommand("A10");
        seatA10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA10);

        seatA11.setToolTipText("");
        seatA11.setActionCommand("A11");
        seatA11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA11);

        seatA12.setToolTipText("");
        seatA12.setActionCommand("A12");
        seatA12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA12);

        seatA13.setToolTipText("");
        seatA13.setActionCommand("A13");
        seatA13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA13);

        seatA14.setToolTipText("");
        seatA14.setActionCommand("A14");
        seatA14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA14);

        seatA15.setToolTipText("");
        seatA15.setActionCommand("A15");
        seatA15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA15);

        seatA16.setToolTipText("");
        seatA16.setActionCommand("A16");
        seatA16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA16);

        seatA17.setToolTipText("");
        seatA17.setActionCommand("A17");
        seatA17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA17);

        seatA18.setToolTipText("");
        seatA18.setActionCommand("A18");
        seatA18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA18);

        seatA19.setToolTipText("");
        seatA19.setActionCommand("A19");
        seatA19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA19);

        seatA20.setToolTipText("");
        seatA20.setActionCommand("A20");
        seatA20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA20);

        seatA21.setToolTipText("");
        seatA21.setActionCommand("A21");
        seatA21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA21);

        seatA22.setToolTipText("");
        seatA22.setActionCommand("A22");
        seatA22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatA22);

        spacerTB35.setToolTipText("");
        spacerTB35.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB35);

        spacerTB2.setToolTipText("");
        spacerTB2.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB2);

        seatB1.setToolTipText("");
        seatB1.setActionCommand("B1");
        seatB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB1);

        seatB2.setToolTipText("");
        seatB2.setActionCommand("B2");
        seatB2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB2);

        seatB3.setToolTipText("");
        seatB3.setActionCommand("B3");
        seatB3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB3);

        seatB4.setToolTipText("");
        seatB4.setActionCommand("B4");
        seatB4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB4);

        seatB5.setToolTipText("");
        seatB5.setActionCommand("B5");
        seatB5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB5);

        seatB6.setToolTipText("");
        seatB6.setActionCommand("B6");
        seatB6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB6);

        seatB7.setToolTipText("");
        seatB7.setActionCommand("B7");
        seatB7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB7);

        seatB8.setToolTipText("");
        seatB8.setActionCommand("B8");
        seatB8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB8);

        seatB9.setToolTipText("");
        seatB9.setActionCommand("B9");
        seatB9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB9);

        seatB10.setToolTipText("");
        seatB10.setActionCommand("B10");
        seatB10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB10);

        seatB11.setToolTipText("");
        seatB11.setActionCommand("B11");
        seatB11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB11);

        seatB12.setToolTipText("");
        seatB12.setActionCommand("B12");
        seatB12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB12);

        seatB13.setToolTipText("");
        seatB13.setActionCommand("B13");
        seatB13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB13);

        seatB14.setToolTipText("");
        seatB14.setActionCommand("B14");
        seatB14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB14);

        seatB15.setToolTipText("");
        seatB15.setActionCommand("B15");
        seatB15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB15);

        seatB16.setToolTipText("");
        seatB16.setActionCommand("B16");
        seatB16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB16);

        seatB17.setToolTipText("");
        seatB17.setActionCommand("B17");
        seatB17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB17);

        seatB18.setToolTipText("");
        seatB18.setActionCommand("B18");
        seatB18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB18);

        seatB19.setToolTipText("");
        seatB19.setActionCommand("B19");
        seatB19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB19);

        seatB20.setToolTipText("");
        seatB20.setActionCommand("B20");
        seatB20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB20);

        seatB21.setToolTipText("");
        seatB21.setActionCommand("B21");
        seatB21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB21);

        seatB22.setToolTipText("");
        seatB22.setActionCommand("B22");
        seatB22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatB22);

        spacerTB36.setToolTipText("");
        spacerTB36.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB36);

        spacerTB3.setToolTipText("");
        spacerTB3.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB3);

        seatC1.setToolTipText("");
        seatC1.setActionCommand("C1");
        seatC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC1);

        seatC2.setToolTipText("");
        seatC2.setActionCommand("C2");
        seatC2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC2);

        seatC3.setToolTipText("");
        seatC3.setActionCommand("C3");
        seatC3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC3);

        seatC4.setToolTipText("");
        seatC4.setActionCommand("C4");
        seatC4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC4);

        seatC5.setToolTipText("");
        seatC5.setActionCommand("C5");
        seatC5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC5);

        seatC6.setToolTipText("");
        seatC6.setActionCommand("C6");
        seatC6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC6);

        seatC7.setToolTipText("");
        seatC7.setActionCommand("C7");
        seatC7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC7);

        seatC8.setToolTipText("");
        seatC8.setActionCommand("C8");
        seatC8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC8);

        seatC9.setToolTipText("");
        seatC9.setActionCommand("C9");
        seatC9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC9);

        seatC10.setToolTipText("");
        seatC10.setActionCommand("C10");
        seatC10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC10);

        seatC11.setToolTipText("");
        seatC11.setActionCommand("C11");
        seatC11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC11);

        seatC12.setToolTipText("");
        seatC12.setActionCommand("C12");
        seatC12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC12);

        seatC13.setToolTipText("");
        seatC13.setActionCommand("C13");
        seatC13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC13);

        seatC14.setToolTipText("");
        seatC14.setActionCommand("C14");
        seatC14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC14);

        seatC15.setToolTipText("");
        seatC15.setActionCommand("C15");
        seatC15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC15);

        seatC16.setToolTipText("");
        seatC16.setActionCommand("C16");
        seatC16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC16);

        seatC17.setToolTipText("");
        seatC17.setActionCommand("C17");
        seatC17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC17);

        seatC18.setToolTipText("");
        seatC18.setActionCommand("C18");
        seatC18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC18);

        seatC19.setToolTipText("");
        seatC19.setActionCommand("C19");
        seatC19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC19);

        seatC20.setToolTipText("");
        seatC20.setActionCommand("C20");
        seatC20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC20);

        seatC21.setToolTipText("");
        seatC21.setActionCommand("C21");
        seatC21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC21);

        seatC22.setToolTipText("");
        seatC22.setActionCommand("C22");
        seatC22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatC22);

        spacerTB37.setToolTipText("");
        spacerTB37.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB37);

        spacerTB4.setToolTipText("");
        spacerTB4.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB4);

        seatD1.setToolTipText("");
        seatD1.setActionCommand("D1");
        seatD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD1);

        seatD2.setToolTipText("");
        seatD2.setActionCommand("D2");
        seatD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD2);

        seatD3.setToolTipText("");
        seatD3.setActionCommand("D3");
        seatD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD3);

        seatD4.setToolTipText("");
        seatD4.setActionCommand("D4");
        seatD4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD4);

        seatD5.setToolTipText("");
        seatD5.setActionCommand("D5");
        seatD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD5);

        seatD6.setToolTipText("");
        seatD6.setActionCommand("D6");
        seatD6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD6);

        seatD7.setToolTipText("");
        seatD7.setActionCommand("D7");
        seatD7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD7);

        seatD8.setToolTipText("");
        seatD8.setActionCommand("D8");
        seatD8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD8);

        seatD9.setToolTipText("");
        seatD9.setActionCommand("D9");
        seatD9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD9);

        seatD10.setToolTipText("");
        seatD10.setActionCommand("D10");
        seatD10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD10);

        seatD11.setToolTipText("");
        seatD11.setActionCommand("D11");
        seatD11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD11);

        seatD12.setToolTipText("");
        seatD12.setActionCommand("D12");
        seatD12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD12);

        seatD13.setToolTipText("");
        seatD13.setActionCommand("D13");
        seatD13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD13);

        seatD14.setToolTipText("");
        seatD14.setActionCommand("D14");
        seatD14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD14);

        seatD15.setToolTipText("");
        seatD15.setActionCommand("D15");
        seatD15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD15);

        seatD16.setToolTipText("");
        seatD16.setActionCommand("D16");
        seatD16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD16);

        seatD17.setToolTipText("");
        seatD17.setActionCommand("D17");
        seatD17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD17);

        seatD18.setToolTipText("");
        seatD18.setActionCommand("D18");
        seatD18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD18);

        seatD19.setToolTipText("");
        seatD19.setActionCommand("D19");
        seatD19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD19);

        seatD20.setToolTipText("");
        seatD20.setActionCommand("D20");
        seatD20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD20);

        seatD21.setToolTipText("");
        seatD21.setActionCommand("D21");
        seatD21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD21);

        seatD22.setToolTipText("");
        seatD22.setActionCommand("D22");
        seatD22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatD22);

        spacerTB38.setToolTipText("");
        spacerTB38.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB38);

        spacerTB5.setToolTipText("");
        spacerTB5.setEnabled(false);
        spacerTB5.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB5);

        spacerTB13.setToolTipText("");
        spacerTB13.setEnabled(false);
        spacerTB13.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB13);

        spacerTB14.setToolTipText("");
        spacerTB14.setEnabled(false);
        spacerTB14.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB14);

        spacerTB15.setToolTipText("");
        spacerTB15.setEnabled(false);
        spacerTB15.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB15);

        spacerTB16.setToolTipText("");
        spacerTB16.setEnabled(false);
        spacerTB16.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB16);

        spacerTB17.setToolTipText("");
        spacerTB17.setEnabled(false);
        spacerTB17.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB17);

        spacerTB18.setToolTipText("");
        spacerTB18.setEnabled(false);
        spacerTB18.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB18);

        spacerTB19.setToolTipText("");
        spacerTB19.setEnabled(false);
        spacerTB19.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB19);

        spacerTB20.setToolTipText("");
        spacerTB20.setEnabled(false);
        spacerTB20.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB20);

        spacerTB21.setToolTipText("");
        spacerTB21.setEnabled(false);
        spacerTB21.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB21);

        spacerTB22.setToolTipText("");
        spacerTB22.setEnabled(false);
        spacerTB22.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB22);

        spacerTB23.setToolTipText("");
        spacerTB23.setEnabled(false);
        spacerTB23.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB23);

        spacerTB24.setToolTipText("");
        spacerTB24.setEnabled(false);
        spacerTB24.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB24);

        spacerTB25.setToolTipText("");
        spacerTB25.setEnabled(false);
        spacerTB25.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB25);

        spacerTB26.setToolTipText("");
        spacerTB26.setEnabled(false);
        spacerTB26.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB26);

        spacerTB27.setToolTipText("");
        spacerTB27.setEnabled(false);
        spacerTB27.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB27);

        spacerTB28.setToolTipText("");
        spacerTB28.setEnabled(false);
        spacerTB28.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB28);

        spacerTB29.setToolTipText("");
        spacerTB29.setEnabled(false);
        spacerTB29.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB29);

        spacerTB30.setToolTipText("");
        spacerTB30.setEnabled(false);
        spacerTB30.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB30);

        spacerTB31.setToolTipText("");
        spacerTB31.setEnabled(false);
        spacerTB31.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB31);

        spacerTB32.setToolTipText("");
        spacerTB32.setEnabled(false);
        spacerTB32.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB32);

        spacerTB33.setToolTipText("");
        spacerTB33.setEnabled(false);
        spacerTB33.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB33);

        spacerTB34.setToolTipText("");
        spacerTB34.setEnabled(false);
        spacerTB34.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB34);

        spacerTB39.setToolTipText("");
        spacerTB39.setEnabled(false);
        spacerTB39.setName(""); // NOI18N
        seatToggleButtonsPanel.add(spacerTB39);

        spacerTB6.setToolTipText("");
        spacerTB6.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB6);

        seatE1.setToolTipText("");
        seatE1.setActionCommand("E1");
        seatE1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE1);

        seatE2.setToolTipText("");
        seatE2.setActionCommand("E2");
        seatE2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE2);

        seatE3.setToolTipText("");
        seatE3.setActionCommand("E3");
        seatE3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE3);

        seatE4.setToolTipText("");
        seatE4.setActionCommand("E4");
        seatE4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE4);

        seatE5.setToolTipText("");
        seatE5.setActionCommand("E5");
        seatE5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE5);

        seatE6.setToolTipText("");
        seatE6.setActionCommand("E6");
        seatE6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE6);

        seatE7.setToolTipText("");
        seatE7.setActionCommand("E7");
        seatE7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE7);

        spacerTB47.setToolTipText("");
        spacerTB47.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB47);

        seatE8.setToolTipText("");
        seatE8.setActionCommand("E8");
        seatE8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE8);

        seatE9.setToolTipText("");
        seatE9.setActionCommand("E9");
        seatE9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE9);

        seatE10.setToolTipText("");
        seatE10.setActionCommand("E10");
        seatE10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE10);

        spacerTB48.setToolTipText("");
        spacerTB48.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB48);

        seatE11.setToolTipText("");
        seatE11.setActionCommand("E11");
        seatE11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE11);

        seatE12.setToolTipText("");
        seatE12.setActionCommand("E12");
        seatE12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE12);

        seatE13.setToolTipText("");
        seatE13.setActionCommand("E13");
        seatE13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE13);

        seatE14.setToolTipText("");
        seatE14.setActionCommand("E14");
        seatE14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE14);

        spacerTB49.setToolTipText("");
        spacerTB49.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB49);

        seatE15.setToolTipText("");
        seatE15.setActionCommand("E15");
        seatE15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE15);

        seatE16.setToolTipText("");
        seatE16.setActionCommand("E16");
        seatE16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE16);

        seatE17.setToolTipText("");
        seatE17.setActionCommand("E17");
        seatE17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE17);

        seatE18.setToolTipText("");
        seatE18.setActionCommand("E18");
        seatE18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE18);

        seatE19.setToolTipText("");
        seatE19.setActionCommand("E19");
        seatE19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatE19);

        spacerTB40.setToolTipText("");
        spacerTB40.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB40);

        spacerTB7.setToolTipText("");
        spacerTB7.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB7);

        seatF1.setToolTipText("");
        seatF1.setActionCommand("F1");
        seatF1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF1);

        seatF2.setToolTipText("");
        seatF2.setActionCommand("F2");
        seatF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF2);

        seatF3.setToolTipText("");
        seatF3.setActionCommand("F3");
        seatF3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF3);

        seatF4.setToolTipText("");
        seatF4.setActionCommand("F4");
        seatF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF4);

        seatF5.setToolTipText("");
        seatF5.setActionCommand("F5");
        seatF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF5);

        seatF6.setToolTipText("");
        seatF6.setActionCommand("F6");
        seatF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF6);

        seatF7.setToolTipText("");
        seatF7.setActionCommand("F7");
        seatF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF7);

        seatF8.setToolTipText("");
        seatF8.setActionCommand("F8");
        seatF8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF8);

        seatF9.setToolTipText("");
        seatF9.setActionCommand("F9");
        seatF9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF9);

        seatF10.setToolTipText("");
        seatF10.setActionCommand("F10");
        seatF10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF10);

        seatF11.setToolTipText("");
        seatF11.setActionCommand("F11");
        seatF11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF11);

        seatF12.setToolTipText("");
        seatF12.setActionCommand("F12");
        seatF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF12);

        seatF13.setToolTipText("");
        seatF13.setActionCommand("F13");
        seatF13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF13);

        seatF14.setToolTipText("");
        seatF14.setActionCommand("F14");
        seatF14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF14);

        seatF15.setToolTipText("");
        seatF15.setActionCommand("F15");
        seatF15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF15);

        seatF16.setToolTipText("");
        seatF16.setActionCommand("F16");
        seatF16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF16);

        seatF17.setToolTipText("");
        seatF17.setActionCommand("F17");
        seatF17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF17);

        seatF18.setToolTipText("");
        seatF18.setActionCommand("F18");
        seatF18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF18);

        seatF19.setToolTipText("");
        seatF19.setActionCommand("F19");
        seatF19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF19);

        seatF20.setToolTipText("");
        seatF20.setActionCommand("F20");
        seatF20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF20);

        seatF21.setToolTipText("");
        seatF21.setActionCommand("F21");
        seatF21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF21);

        seatF22.setToolTipText("");
        seatF22.setActionCommand("F22");
        seatF22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatF22);

        spacerTB41.setToolTipText("");
        spacerTB41.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB41);

        spacerTB8.setToolTipText("");
        spacerTB8.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB8);

        seatG1.setToolTipText("");
        seatG1.setActionCommand("G1");
        seatG1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG1);

        seatG2.setToolTipText("");
        seatG2.setActionCommand("G2");
        seatG2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG2);

        seatG3.setToolTipText("");
        seatG3.setActionCommand("G3");
        seatG3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG3);

        seatG4.setToolTipText("");
        seatG4.setActionCommand("G4");
        seatG4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG4);

        seatG5.setToolTipText("");
        seatG5.setActionCommand("G5");
        seatG5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG5);

        seatG6.setToolTipText("");
        seatG6.setActionCommand("G6");
        seatG6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG6);

        seatG7.setToolTipText("");
        seatG7.setActionCommand("G7");
        seatG7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG7);

        seatG8.setToolTipText("");
        seatG8.setActionCommand("G8");
        seatG8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG8);

        seatG9.setToolTipText("");
        seatG9.setActionCommand("G9");
        seatG9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG9);

        seatG10.setToolTipText("");
        seatG10.setActionCommand("G10");
        seatG10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG10);

        seatG11.setToolTipText("");
        seatG11.setActionCommand("G11");
        seatG11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG11);

        seatG12.setToolTipText("");
        seatG12.setActionCommand("G12");
        seatG12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG12);

        seatG13.setToolTipText("");
        seatG13.setActionCommand("G13");
        seatG13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG13);

        seatG14.setToolTipText("");
        seatG14.setActionCommand("G14");
        seatG14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG14);

        seatG15.setToolTipText("");
        seatG15.setActionCommand("G15");
        seatG15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG15);

        seatG16.setToolTipText("");
        seatG16.setActionCommand("G16");
        seatG16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG16);

        seatG17.setToolTipText("");
        seatG17.setActionCommand("G17");
        seatG17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG17);

        seatG18.setToolTipText("");
        seatG18.setActionCommand("G18");
        seatG18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG18);

        seatG19.setToolTipText("");
        seatG19.setActionCommand("G19");
        seatG19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG19);

        seatG20.setToolTipText("");
        seatG20.setActionCommand("G20");
        seatG20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG20);

        seatG21.setToolTipText("");
        seatG21.setActionCommand("G21");
        seatG21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG21);

        seatG22.setToolTipText("");
        seatG22.setActionCommand("G22");
        seatG22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatG22);

        spacerTB42.setToolTipText("");
        spacerTB42.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB42);

        spacerTB9.setToolTipText("");
        spacerTB9.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB9);

        seatH1.setToolTipText("");
        seatH1.setActionCommand("H1");
        seatH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH1);

        seatH2.setToolTipText("");
        seatH2.setActionCommand("H2");
        seatH2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH2);

        seatH3.setToolTipText("");
        seatH3.setActionCommand("H3");
        seatH3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH3);

        seatH4.setToolTipText("");
        seatH4.setActionCommand("H4");
        seatH4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH4);

        seatH5.setToolTipText("");
        seatH5.setActionCommand("H5");
        seatH5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH5);

        seatH6.setToolTipText("");
        seatH6.setActionCommand("H6");
        seatH6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH6);

        seatH7.setToolTipText("");
        seatH7.setActionCommand("H7");
        seatH7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH7);

        seatH8.setToolTipText("");
        seatH8.setActionCommand("H8");
        seatH8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH8);

        seatH9.setToolTipText("");
        seatH9.setActionCommand("H9");
        seatH9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH9);

        seatH10.setToolTipText("");
        seatH10.setActionCommand("H10");
        seatH10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH10);

        seatH11.setToolTipText("");
        seatH11.setActionCommand("H11");
        seatH11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH11);

        seatH12.setToolTipText("");
        seatH12.setActionCommand("H12");
        seatH12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH12);

        seatH13.setToolTipText("");
        seatH13.setActionCommand("H13");
        seatH13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH13);

        seatH14.setToolTipText("");
        seatH14.setActionCommand("H14");
        seatH14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH14);

        seatH15.setToolTipText("");
        seatH15.setActionCommand("H15");
        seatH15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH15);

        seatH16.setToolTipText("");
        seatH16.setActionCommand("H16");
        seatH16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH16);

        seatH17.setToolTipText("");
        seatH17.setActionCommand("H17");
        seatH17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH17);

        seatH18.setToolTipText("");
        seatH18.setActionCommand("H18");
        seatH18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH18);

        seatH19.setToolTipText("");
        seatH19.setActionCommand("H19");
        seatH19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH19);

        seatH20.setToolTipText("");
        seatH20.setActionCommand("H20");
        seatH20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH20);

        seatH21.setToolTipText("");
        seatH21.setActionCommand("H21");
        seatH21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH21);

        seatH22.setToolTipText("");
        seatH22.setActionCommand("H22");
        seatH22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatH22);

        spacerTB43.setToolTipText("");
        spacerTB43.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB43);

        spacerTB10.setToolTipText("");
        spacerTB10.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB10);

        seatI1.setToolTipText("");
        seatI1.setActionCommand("I1");
        seatI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI1);

        seatI2.setToolTipText("");
        seatI2.setActionCommand("I2");
        seatI2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI2);

        seatI3.setToolTipText("");
        seatI3.setActionCommand("I3");
        seatI3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI3);

        seatI4.setToolTipText("");
        seatI4.setActionCommand("I4");
        seatI4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI4);

        seatI5.setToolTipText("");
        seatI5.setActionCommand("I5");
        seatI5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI5);

        seatI6.setToolTipText("");
        seatI6.setActionCommand("I6");
        seatI6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI6);

        seatI7.setToolTipText("");
        seatI7.setActionCommand("I7");
        seatI7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI7);

        seatI8.setToolTipText("");
        seatI8.setActionCommand("I8");
        seatI8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI8);

        seatI9.setToolTipText("");
        seatI9.setActionCommand("I9");
        seatI9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI9);

        seatI10.setToolTipText("");
        seatI10.setActionCommand("I10");
        seatI10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI10);

        seatI11.setToolTipText("");
        seatI11.setActionCommand("I11");
        seatI11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI11);

        seatI12.setToolTipText("");
        seatI12.setActionCommand("I12");
        seatI12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI12);

        seatI13.setToolTipText("");
        seatI13.setActionCommand("I13");
        seatI13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI13);

        seatI14.setToolTipText("");
        seatI14.setActionCommand("I14");
        seatI14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI14);

        seatI15.setToolTipText("");
        seatI15.setActionCommand("I15");
        seatI15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI15);

        seatI16.setToolTipText("");
        seatI16.setActionCommand("I16");
        seatI16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI16);

        seatI17.setToolTipText("");
        seatI17.setActionCommand("I17");
        seatI17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI17);

        seatI18.setToolTipText("");
        seatI18.setActionCommand("I18");
        seatI18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI18);

        seatI19.setToolTipText("");
        seatI19.setActionCommand("I19");
        seatI19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI19);

        seatI20.setToolTipText("");
        seatI20.setActionCommand("I20");
        seatI20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI20);

        seatI21.setToolTipText("");
        seatI21.setActionCommand("I21");
        seatI21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI21);

        seatI22.setToolTipText("");
        seatI22.setActionCommand("I22");
        seatI22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatI22);

        spacerTB44.setToolTipText("");
        spacerTB44.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB44);

        spacerTB11.setToolTipText("");
        spacerTB11.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB11);

        seatJ1.setToolTipText("");
        seatJ1.setActionCommand("J1");
        seatJ1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ1);

        seatJ2.setToolTipText("");
        seatJ2.setActionCommand("J2");
        seatJ2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ2);

        seatJ3.setToolTipText("");
        seatJ3.setActionCommand("J3");
        seatJ3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ3);

        seatJ4.setToolTipText("");
        seatJ4.setActionCommand("J4");
        seatJ4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ4);

        seatJ5.setToolTipText("");
        seatJ5.setActionCommand("J5");
        seatJ5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ5);

        seatJ6.setToolTipText("");
        seatJ6.setActionCommand("J6");
        seatJ6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ6);

        seatJ7.setToolTipText("");
        seatJ7.setActionCommand("J7");
        seatJ7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ7);

        seatJ8.setToolTipText("");
        seatJ8.setActionCommand("8");
        seatJ8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ8);

        seatJ9.setToolTipText("");
        seatJ9.setActionCommand("J9");
        seatJ9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ9);

        seatJ10.setToolTipText("");
        seatJ10.setActionCommand("J10");
        seatJ10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ10);

        seatJ11.setToolTipText("");
        seatJ11.setActionCommand("J11");
        seatJ11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ11);

        seatJ12.setToolTipText("");
        seatJ12.setActionCommand("J12");
        seatJ12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ12);

        seatJ13.setToolTipText("");
        seatJ13.setActionCommand("J13");
        seatJ13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ13);

        seatJ14.setToolTipText("");
        seatJ14.setActionCommand("J14");
        seatJ14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ14);

        seatJ15.setToolTipText("");
        seatJ15.setActionCommand("J15");
        seatJ15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ15);

        seatJ16.setToolTipText("");
        seatJ16.setActionCommand("J16");
        seatJ16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ16);

        seatJ17.setToolTipText("");
        seatJ17.setActionCommand("J17");
        seatJ17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ17);

        seatJ18.setToolTipText("");
        seatJ18.setActionCommand("J18");
        seatJ18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ18);

        seatJ19.setToolTipText("");
        seatJ19.setActionCommand("J19");
        seatJ19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ19);

        seatJ20.setToolTipText("");
        seatJ20.setActionCommand("J20");
        seatJ20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ20);

        seatJ21.setToolTipText("");
        seatJ21.setActionCommand("J21");
        seatJ21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ21);

        seatJ22.setToolTipText("");
        seatJ22.setActionCommand("J22");
        seatJ22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatJ22);

        spacerTB45.setToolTipText("");
        spacerTB45.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB45);

        spacerTB12.setToolTipText("");
        spacerTB12.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB12);

        seatK1.setToolTipText("");
        seatK1.setActionCommand("K1");
        seatK1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK1);

        seatK2.setToolTipText("");
        seatK2.setActionCommand("K2");
        seatK2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK2);

        seatK3.setToolTipText("");
        seatK3.setActionCommand("K3");
        seatK3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK3);

        seatK4.setToolTipText("");
        seatK4.setActionCommand("K4");
        seatK4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK4);

        seatK5.setToolTipText("");
        seatK5.setActionCommand("K5");
        seatK5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK5);

        seatK6.setToolTipText("");
        seatK6.setActionCommand("K6");
        seatK6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK6);

        seatK7.setToolTipText("");
        seatK7.setActionCommand("K7");
        seatK7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK7);

        seatK8.setToolTipText("");
        seatK8.setActionCommand("K8");
        seatK8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK8);

        seatK9.setToolTipText("");
        seatK9.setActionCommand("K9");
        seatK9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK9);

        seatK10.setToolTipText("");
        seatK10.setActionCommand("K10");
        seatK10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK10);

        seatK11.setToolTipText("");
        seatK11.setActionCommand("K11");
        seatK11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK11);

        seatK12.setToolTipText("");
        seatK12.setActionCommand("K12");
        seatK12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK12);

        seatK13.setToolTipText("");
        seatK13.setActionCommand("K13");
        seatK13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK13);

        seatK14.setToolTipText("");
        seatK14.setActionCommand("K14");
        seatK14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK14);

        seatK15.setToolTipText("");
        seatK15.setActionCommand("K15");
        seatK15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK15);

        seatK16.setToolTipText("");
        seatK16.setActionCommand("K16");
        seatK16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK16);

        seatK17.setToolTipText("");
        seatK17.setActionCommand("K17");
        seatK17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK17);

        seatK18.setToolTipText("");
        seatK18.setActionCommand("K18");
        seatK18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK18);

        seatK19.setToolTipText("");
        seatK19.setActionCommand("K19");
        seatK19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK19);

        seatK20.setToolTipText("");
        seatK20.setActionCommand("K20");
        seatK20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK20);

        seatK21.setToolTipText("");
        seatK21.setActionCommand("K21");
        seatK21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK21);

        seatK22.setToolTipText("");
        seatK22.setActionCommand("K22");
        seatK22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatK22);

        spacerTB46.setToolTipText("");
        spacerTB46.setEnabled(false);
        seatToggleButtonsPanel.add(spacerTB46);

        seatL1.setToolTipText("");
        seatL1.setActionCommand("L1");
        seatL1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL1);

        seatL2.setToolTipText("");
        seatL2.setActionCommand("L2");
        seatL2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL2);

        seatL3.setToolTipText("");
        seatL3.setActionCommand("L3");
        seatL3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL3);

        seatL4.setToolTipText("");
        seatL4.setActionCommand("L4");
        seatL4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL4);

        seatL5.setToolTipText("");
        seatL5.setActionCommand("L5");
        seatL5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL5);

        seatL6.setToolTipText("");
        seatL6.setActionCommand("L6");
        seatL6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL6);

        seatL7.setToolTipText("");
        seatL7.setActionCommand("L7");
        seatL7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL7);

        seatL8.setToolTipText("");
        seatL8.setActionCommand("L8");
        seatL8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL8);

        seatL9.setToolTipText("");
        seatL9.setActionCommand("L9");
        seatL9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL9);

        seatL10.setToolTipText("");
        seatL10.setActionCommand("L10");
        seatL10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL10);

        seatL11.setToolTipText("");
        seatL11.setActionCommand("L11");
        seatL11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL11);

        seatL12.setToolTipText("");
        seatL12.setActionCommand("L12");
        seatL12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL12);

        seatL13.setToolTipText("");
        seatL13.setActionCommand("L13");
        seatL13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL13);

        seatL14.setToolTipText("");
        seatL14.setActionCommand("L14");
        seatL14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL14);

        seatL15.setToolTipText("");
        seatL15.setActionCommand("L15");
        seatL15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL15);

        seatL16.setToolTipText("");
        seatL16.setActionCommand("L16");
        seatL16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL16);

        seatL17.setToolTipText("");
        seatL17.setActionCommand("L17");
        seatL17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL17);

        seatL18.setToolTipText("");
        seatL18.setActionCommand("L18");
        seatL18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL18);

        seatL19.setToolTipText("");
        seatL19.setActionCommand("L19");
        seatL19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL19);

        seatL20.setToolTipText("");
        seatL20.setActionCommand("L20");
        seatL20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL20);

        seatL21.setToolTipText("");
        seatL21.setActionCommand("L21");
        seatL21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL21);

        seatL22.setToolTipText("");
        seatL22.setActionCommand("L22");
        seatL22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL22);

        seatL23.setToolTipText("");
        seatL23.setActionCommand("L23");
        seatL23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL23);

        seatL24.setToolTipText("");
        seatL24.setActionCommand("L24");
        seatL24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seatSelectionActionPerformed(evt);
            }
        });
        seatToggleButtonsPanel.add(seatL24);

        seatsDiagramPanel.add(seatToggleButtonsPanel, java.awt.BorderLayout.CENTER);

        spacerSouthPanel.setBackground(new java.awt.Color(9, 64, 113));
        spacerSouthPanel.setPreferredSize(new java.awt.Dimension(10, 25));
        seatsDiagramPanel.add(spacerSouthPanel, java.awt.BorderLayout.SOUTH);

        spacerWestPanel.setBackground(new java.awt.Color(9, 64, 113));
        spacerWestPanel.setPreferredSize(new java.awt.Dimension(100, 10));
        seatsDiagramPanel.add(spacerWestPanel, java.awt.BorderLayout.WEST);

        spacerEastPanel.setBackground(new java.awt.Color(9, 64, 113));
        spacerEastPanel.setPreferredSize(new java.awt.Dimension(100, 10));
        seatsDiagramPanel.add(spacerEastPanel, java.awt.BorderLayout.EAST);

        selectSeatsPanel.add(seatsDiagramPanel, java.awt.BorderLayout.CENTER);

        csacpDisplayPanel.add(selectSeatsPanel, java.awt.BorderLayout.CENTER);

        transactionSummaryPanel.setBackground(new java.awt.Color(204, 204, 204));
        transactionSummaryPanel.setPreferredSize(new java.awt.Dimension(700, 10));
        transactionSummaryPanel.setLayout(new java.awt.GridLayout(3, 1));

        topPanel.setBackground(new java.awt.Color(255, 255, 255));
        topPanel.setLayout(new java.awt.GridLayout(1, 3));

        spacerButtonTS1.setText("jButton1");
        spacerButtonTS1.setEnabled(false);
        topPanel.add(spacerButtonTS1);

        movieImageTS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        topPanel.add(movieImageTS);

        spacerButtonTS2.setText("jButton2");
        spacerButtonTS2.setEnabled(false);
        topPanel.add(spacerButtonTS2);

        transactionSummaryPanel.add(topPanel);

        midPanel.setBackground(new java.awt.Color(255, 255, 255));
        midPanel.setLayout(new java.awt.GridBagLayout());

        movieTitleLabelTS.setFont(new java.awt.Font("Lucida Grande", 2, 20)); // NOI18N
        movieTitleLabelTS.setText("Selected Movie Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 10, 0);
        midPanel.add(movieTitleLabelTS, gridBagConstraints);

        dateOfPurchaseLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        dateOfPurchaseLabelTS.setText("Monday, October, 22");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        midPanel.add(dateOfPurchaseLabelTS, gridBagConstraints);

        auditoriumLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        auditoriumLabelTS.setText("Auditorium: #15");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        midPanel.add(auditoriumLabelTS, gridBagConstraints);

        seatsSelectedLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
        seatsSelectedLabelTS.setText("Seats: Select Your Seat(s)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 0);
        midPanel.add(seatsSelectedLabelTS, gridBagConstraints);

        transactionSummaryPanel.add(midPanel);

        totalCostPanel.setBackground(new java.awt.Color(255, 255, 255));
        totalCostPanel.setLayout(new java.awt.GridBagLayout());

        seniorLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        seniorLabelTS.setText("Senior");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 0);
        totalCostPanel.add(seniorLabelTS, gridBagConstraints);

        absoluteTotalTS.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        absoluteTotalTS.setText("FTotal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 42, 0);
        totalCostPanel.add(absoluteTotalTS, gridBagConstraints);

        seniorCalculationLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        seniorCalculationLabel.setText("1 x$11.29 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 5, 18);
        totalCostPanel.add(seniorCalculationLabel, gridBagConstraints);

        convenienceFeeCalculationLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        convenienceFeeCalculationLabel.setText("3x$1.25 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(9, 18, 0, 18);
        totalCostPanel.add(convenienceFeeCalculationLabel, gridBagConstraints);

        adultTotalTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        adultTotalTS.setText("TotalA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 5, 0);
        totalCostPanel.add(adultTotalTS, gridBagConstraints);

        childCalculationLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        childCalculationLabel.setText("1 x$11.29 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 5, 18);
        totalCostPanel.add(childCalculationLabel, gridBagConstraints);

        adultCalculationLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        adultCalculationLabel.setText("1 x$11.29 =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(17, 18, 5, 18);
        totalCostPanel.add(adultCalculationLabel, gridBagConstraints);

        convenienceFeeLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        convenienceFeeLabelTS.setText("Conv. Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        totalCostPanel.add(convenienceFeeLabelTS, gridBagConstraints);

        absoluteTotalLabelTS.setFont(new java.awt.Font("Lucida Grande", 1, 36)); // NOI18N
        absoluteTotalLabelTS.setText("Total =");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(14, 18, 42, 18);
        totalCostPanel.add(absoluteTotalLabelTS, gridBagConstraints);

        childTotalTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        childTotalTS.setText("TotalC");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 0);
        totalCostPanel.add(childTotalTS, gridBagConstraints);

        convenienceFeeTotalTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        convenienceFeeTotalTS.setText("TotalCon");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        totalCostPanel.add(convenienceFeeTotalTS, gridBagConstraints);

        adultLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        adultLabelTS.setText("Adult");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 5, 0);
        totalCostPanel.add(adultLabelTS, gridBagConstraints);

        childLabelTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        childLabelTS.setText("Child");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 0);
        totalCostPanel.add(childLabelTS, gridBagConstraints);

        seniorTotalTS.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        seniorTotalTS.setText("TotalS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 5, 0);
        totalCostPanel.add(seniorTotalTS, gridBagConstraints);

        transactionSummaryPanel.add(totalCostPanel);

        csacpDisplayPanel.add(transactionSummaryPanel, java.awt.BorderLayout.EAST);

        chooseSeatsAndCompletePurchaseScreen.add(csacpDisplayPanel, java.awt.BorderLayout.CENTER);

        csacpNavButtonPanel.setBackground(new java.awt.Color(9, 64, 113));
        csacpNavButtonPanel.setPreferredSize(new java.awt.Dimension(399, 100));
        csacpNavButtonPanel.setRequestFocusEnabled(false);
        csacpNavButtonPanel.setLayout(new java.awt.GridLayout(1, 2, 5, 0));

        cpagbButtonsPanel.setBackground(new java.awt.Color(9, 64, 113));
        cpagbButtonsPanel.setLayout(new java.awt.GridLayout(1, 2));

        csacpCancelPurchaseButton.setFont(new java.awt.Font("Lucida Grande", 1, 35)); // NOI18N
        csacpCancelPurchaseButton.setText("CANCEL PURCHASE");
        csacpCancelPurchaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csacpCancelPurchaseButtonActionPerformed(evt);
            }
        });
        cpagbButtonsPanel.add(csacpCancelPurchaseButton);

        csacpBackButton.setFont(new java.awt.Font("Lucida Grande", 1, 48)); // NOI18N
        csacpBackButton.setText("<--«GO BACK");
        csacpBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csacpBackButtonActionPerformed(evt);
            }
        });
        cpagbButtonsPanel.add(csacpBackButton);

        csacpNavButtonPanel.add(cpagbButtonsPanel);

        csacpCompletePurchaseButton.setFont(new java.awt.Font("Lucida Grande", 1, 70)); // NOI18N
        csacpCompletePurchaseButton.setText("COMPLETE PURCHASE");
        csacpCompletePurchaseButton.setEnabled(false);
        csacpCompletePurchaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csacpCompletePurchaseButtonActionPerformed(evt);
            }
        });
        csacpNavButtonPanel.add(csacpCompletePurchaseButton);

        chooseSeatsAndCompletePurchaseScreen.add(csacpNavButtonPanel, java.awt.BorderLayout.SOUTH);

        displayPanel.add(chooseSeatsAndCompletePurchaseScreen, "card5");

        getContentPane().add(displayPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void joinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_joinButtonActionPerformed
        // when clicked, switch to card2 = registerScreen
        CardLayout card = (CardLayout) displayPanel.getLayout();
        card.show(displayPanel, "card2");
        
    }//GEN-LAST:event_joinButtonActionPerformed

    private void logInEmailTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_logInEmailTextFieldFocusGained
        // TODO add your handling code here:
        if (logInEmailTextField.getText().equals("E-mail Address")){
            logInEmailTextField.setText("");
            logInEmailTextField.setForeground(Color.BLACK);
        }
        incorrectEmailOrPasswordLabel.setVisible(false);
            
    }//GEN-LAST:event_logInEmailTextFieldFocusGained

    private void logInEmailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_logInEmailTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
       
        if(!(vTool.validateEmailAddress(logInEmailTextField.getText()))){
            logInEmailTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidLogInEmailLabel.setVisible(true);
        }
        else{
            logInEmailTextField.setBorder(null);//throws errors but not significant
            logInEmailTextField.updateUI();//throws errors but not significant
            invalidLogInEmailLabel.setVisible(false);
        }
         if ((logInEmailTextField.getText().equals(""))){
        logInEmailTextField.setForeground(new Color(130,123,123));
        logInEmailTextField.setText("E-mail Address");
        logInEmailTextField.setBorder(null);//throws errors but not significant
        logInEmailTextField.updateUI();//throws errors but not significant
        invalidLogInEmailLabel.setVisible(false);
        }
        
        
    }//GEN-LAST:event_logInEmailTextFieldFocusLost

    private void logInPasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_logInPasswordFieldFocusGained
        // TODO add your handling code here:
        String tempPassword = new String(logInPasswordField.getPassword());
        //System.out.println(tempPassword); //used for testing
        if (tempPassword.equals("Password")){
            logInPasswordField.setForeground(Color.BLACK);
            logInPasswordField.setText("");
            logInPasswordField.setEchoChar('\u25cf');
        }
    }//GEN-LAST:event_logInPasswordFieldFocusGained

    private void logInPasswordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_logInPasswordFieldFocusLost
        // TODO add your handling code here:
        String tempPassword = new String(logInPasswordField.getPassword());
        if (tempPassword.equals("") || tempPassword.equalsIgnoreCase("password")){
            logInPasswordField.setForeground(new Color(130,123,123));
            logInPasswordField.setEchoChar((char)0);
            logInPasswordField.setText("Password");
         }

            
    }//GEN-LAST:event_logInPasswordFieldFocusLost

    private void signInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signInButtonActionPerformed
        //TEMPORARY FOR TESTING
        //CardLayout card = (CardLayout) displayPanel.getLayout();
        //card.show(displayPanel, "card3");


        // check database  if email exists, and if password matches. 
        //If password matches Then:
        //--switch to card3 = Choose a movie screen, 
        //--set navigationButtonPanel visible 'true'
        //ELSE display incorrectEmailOrPasswordLabel
        
        try { 
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database1.sqlite");//connect to database
           
            
            String sql = "SELECT * FROM mtmUSERACCOUNTS WHERE email=? and password=?"; //grab email and password fields
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1,logInEmailTextField.getText());
            pst.setString(2,logInPasswordField.getText());
            
            ResultSet rs = pst.executeQuery();
            
            int result = 0;
            while(rs.next()){
                result+=1;
            }
            if(result == 1){//if log in info correct, proceed to select a movie screen
                CardLayout card = (CardLayout) displayPanel.getLayout();
                card.show(displayPanel, "card3");
                //empty fields in login screen
                logInEmailTextField.setForeground(new Color(130,123,123));
                logInEmailTextField.setText("E-mail Address");
                logInPasswordField.setForeground(new Color(130,123,123));
                logInPasswordField.setText("Password");
            }
            else{
                incorrectEmailOrPasswordLabel.setVisible(true);
            }
            rs.close();
            pst.close();
            
            
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        
    }//GEN-LAST:event_signInButtonActionPerformed

    private void confirmEmailTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_confirmEmailTextFieldFocusGained
        // TODO add your handling code here:
        if (confirmEmailTextField.getText().equals("example@email.com")){
            confirmEmailTextField.setForeground(Color.BLACK);
            confirmEmailTextField.setText("");
        }
    }//GEN-LAST:event_confirmEmailTextFieldFocusGained

    private void confirmEmailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_confirmEmailTextFieldFocusLost
        // TODO add your handling code here:
        /*if ((confirmEmailTextField.getText().equals(""))){
            confirmEmailTextField.setForeground(new Color(130,123,123));    
            confirmEmailTextField.setText("example@email.com");
            confirmEmailTextField.setBorder(null);//throws errors but not significant
            confirmEmailTextField.updateUI();//throws errors but not significant
            emailsNoMatchLabel.setVisible(false);
        }
        */
        //if valid, remove any error labels,reset textfield border
        if(confirmEmailTextField.getText().equals(registerEmailTextField.getText())){
            confirmEmailTextField.setBorder(null);//throws errors but not significant
            confirmEmailTextField.updateUI();//throws errors but not significant
            emailsNoMatchLabel.setVisible(false);
            
        }
        else{//if invalid,
            if ((confirmEmailTextField.getText().equals(""))){
                confirmEmailTextField.setForeground(new Color(130,123,123));    
                confirmEmailTextField.setText("example@email.com");
                confirmEmailTextField.setBorder(null);//throws errors but not significant
                confirmEmailTextField.updateUI();//throws errors but not significant
                emailsNoMatchLabel.setVisible(false);
            }
            else{
                confirmEmailTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
                emailsNoMatchLabel.setVisible(true);
            }
        }
    }//GEN-LAST:event_confirmEmailTextFieldFocusLost

    private void firstNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_firstNameTextFieldFocusGained
        // TODO add your handling code here:
        if (firstNameTextField.getText().equals("First")){
            firstNameTextField.setText("");
            firstNameTextField.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_firstNameTextFieldFocusGained

    private void firstNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_firstNameTextFieldFocusLost
        StringValidationTool vTool = new StringValidationTool();
        // TODO add your handling code here:
        if ((firstNameTextField.getText().equals(""))){
            firstNameTextField.setForeground(new Color(130,123,123));
            firstNameTextField.setText("First");
            firstNameTextField.setBorder(null);//throws errors but not significant
            firstNameTextField.updateUI();
            invalidNameLabel.setVisible(false);
        }
        if (!(vTool.validateLetters(firstNameTextField.getText()))){
            firstNameTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidNameLabel.setVisible(true);
        }
        else{
            firstNameTextField.setBorder(null);//throws errors but not significant
            firstNameTextField.updateUI();//throws errors but not significant
            invalidNameLabel.setVisible(false);
        }
    }//GEN-LAST:event_firstNameTextFieldFocusLost

    private void lastNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lastNameTextFieldFocusGained
        // TODO add your handling code here:
        if (lastNameTextField.getText().equals("Last")){
            lastNameTextField.setForeground(Color.BLACK);
            lastNameTextField.setText("");
        }
    }//GEN-LAST:event_lastNameTextFieldFocusGained

    private void lastNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lastNameTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        if ((lastNameTextField.getText().equals(""))){
            lastNameTextField.setForeground(new Color(130,123,123));
            lastNameTextField.setText("Last");
            lastNameTextField.setBorder(null);//throws errors but not significant
            lastNameTextField.updateUI();//throws errors but not significant
            invalidNameLabel.setVisible(false);
        }
        if (!(vTool.validateLetters(lastNameTextField.getText()))){
            lastNameTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidLNameLabel.setVisible(true);
        }
        else{
            lastNameTextField.setBorder(null);//throws errors but not significant
            lastNameTextField.updateUI();//throws errors but not significant
            invalidLNameLabel.setVisible(false);
        }
    }//GEN-LAST:event_lastNameTextFieldFocusLost

    private void registerEmailTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_registerEmailTextFieldFocusGained
        // TODO add your handling code here:
        if (registerEmailTextField.getText().equals("example@email.com")){
            registerEmailTextField.setForeground(Color.BLACK);
            registerEmailTextField.setText("");
        }
    }//GEN-LAST:event_registerEmailTextFieldFocusGained

    private void registerEmailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_registerEmailTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        if ((registerEmailTextField.getText().equals(""))){
            registerEmailTextField.setForeground(new Color(130,123,123));
            registerEmailTextField.setText("example@email.com");
            registerEmailTextField.setBorder(null);//throws errors but not significant
            registerEmailTextField.updateUI();//throws errors but not significant
            invalidRegisterEmailLabel.setVisible(false);
        }
        if(!(vTool.validateEmailAddress(registerEmailTextField.getText()))){
            registerEmailTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidRegisterEmailLabel.setVisible(true);
        }
        else{
            registerEmailTextField.setBorder(null);//throws errors but not significant
            registerEmailTextField.updateUI();//throws errors but not significant
            invalidRegisterEmailLabel.setVisible(false);
        }
        if(confirmEmailTextField.getText().equals(registerEmailTextField.getText())){//check if moatches with  confirm email
            confirmEmailTextField.setBorder(null);//throws errors but not significant
            confirmEmailTextField.updateUI();//throws errors but not significant
            emailsNoMatchLabel.setVisible(false);
            
        }
    }//GEN-LAST:event_registerEmailTextFieldFocusLost

    private void cityTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cityTextFieldFocusGained
        // TODO add your handling code here:
        if (cityTextField.getText().equals("City")){
            cityTextField.setForeground(Color.BLACK);
            cityTextField.setText("");
        }
    }//GEN-LAST:event_cityTextFieldFocusGained

    private void cityTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cityTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        if ((cityTextField.getText().equals(""))){
            cityTextField.setForeground(new Color(130,123,123));    
            cityTextField.setText("City");
            cityTextField.setBorder(null);//throws errors but not significant
            cityTextField.updateUI();
            invalidCityLabel.setVisible(false);
        }
        if (!(vTool.validateLetters(cityTextField.getText()))){
            cityTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidCityLabel.setVisible(true);
        }
        else{
            cityTextField.setBorder(null);//throws errors but not significant
            cityTextField.updateUI();//throws errors but not significant
            invalidCityLabel.setVisible(false);
        }
    }//GEN-LAST:event_cityTextFieldFocusLost

    private void streetAddressTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_streetAddressTextFieldFocusGained
        // TODO add your handling code here:
        if (streetAddressTextField.getText().equals("Street Address")){
            streetAddressTextField.setForeground(Color.BLACK);
            streetAddressTextField.setText("");
        }
    }//GEN-LAST:event_streetAddressTextFieldFocusGained

    private void streetAddressTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_streetAddressTextFieldFocusLost
        // TODO add your handling code here:
        if (streetAddressTextField.getText().equals("")){  
            streetAddressTextField.setForeground(new Color(130,123,123));
            streetAddressTextField.setText("Street Address");
        }
    }//GEN-LAST:event_streetAddressTextFieldFocusLost

    private void zipCodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_zipCodeTextFieldFocusGained
        // TODO add your handling code here:
        if (zipCodeTextField.getText().equals("Zip Code")){
            zipCodeTextField.setForeground(Color.BLACK);
            zipCodeTextField.setText("");
        }
    }//GEN-LAST:event_zipCodeTextFieldFocusGained

    private void zipCodeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_zipCodeTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        if (zipCodeTextField.getText().equals("")){
            invalidZipCodeLabel.setVisible(false);
            zipCodeTextField.setBorder(null);//throws errors but not significant
            zipCodeTextField.updateUI();
            zipCodeTextField.setForeground(new Color(130,123,123)); 
            zipCodeTextField.setText("Zip Code");
        }
        else if (!(vTool.validateNumbers(zipCodeTextField.getText()))){
            zipCodeTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidZipCodeLabel.setVisible(true);
        }
        else{
            zipCodeTextField.setBorder(null);//throws errors but not significant
            zipCodeTextField.updateUI();//throws errors but not significant
            invalidZipCodeLabel.setVisible(false);
        }
    }//GEN-LAST:event_zipCodeTextFieldFocusLost

    private void confirmPasswordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_confirmPasswordFieldFocusLost
        // TODO add your handling code here:
        String createdPassword = new String(createPasswordField.getPassword());
        String tempConfirmPassword = new String(confirmPasswordField.getPassword());
        if(!(createdPassword.equals(tempConfirmPassword))){
            confirmPasswordField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            passwordsNoMatchLabel.setVisible(true);
        }
        else{
            confirmPasswordField.setBorder(null);
            confirmPasswordField.updateUI();
            passwordsNoMatchLabel.setVisible(false);
        }
    }//GEN-LAST:event_confirmPasswordFieldFocusLost

    private void createPasswordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_createPasswordFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        String tempPassword = new String(createPasswordField.getPassword());
        if (!(vTool.validatePassword(tempPassword))){
            createPasswordField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidPasswordTextPane.setVisible(true);
        }
        else{
            createPasswordField.setBorder(null);
            createPasswordField.updateUI();
            invalidPasswordTextPane.setVisible(false);
        }

    }//GEN-LAST:event_createPasswordFieldFocusLost

    private void cancelRegistrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelRegistrationButtonActionPerformed
        // TODO add your handling code here:
        //javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);
        int answer = javax.swing.JOptionPane.showConfirmDialog(null,"Are you sure you want to cancel registration?",
            "Confirm Cancelation",javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
        if(answer == javax.swing.JOptionPane.YES_OPTION){
            //--->> registration Screen cancel button Commands
            //reset/clear all components in registration form
            firstNameTextField.setForeground(new Color(130,123,123));
            firstNameTextField.setText("First");
            firstNameTextField.setBorder(null);//throws errors but not significant
            firstNameTextField.updateUI();//throws errors but not significant
            invalidNameLabel.setVisible(false);
            
            lastNameTextField.setForeground(new Color(130,123,123));
            lastNameTextField.setText("Last");
            lastNameTextField.setBorder(null);//throws errors but not significant
            lastNameTextField.updateUI();//throws errors but not significant
            invalidLNameLabel.setVisible(false);
            
            registerEmailTextField.setForeground(new Color(130,123,123));
            registerEmailTextField.setText("example@email.com");
            registerEmailTextField.setBorder(null);//throws errors but not significant
            registerEmailTextField.updateUI();//throws errors but not significant
            invalidRegisterEmailLabel.setVisible(false);
            
            confirmEmailTextField.setForeground(new Color(130,123,123));    
            confirmEmailTextField.setText("example@email.com");
            confirmEmailTextField.setBorder(null);//throws errors but not significant
            confirmEmailTextField.updateUI();//throws errors but not significant
            emailsNoMatchLabel.setVisible(false);
            
            createPasswordField.setText("");
            createPasswordField.setBorder(null);
            createPasswordField.updateUI();
            invalidPasswordTextPane.setVisible(false);
            
            confirmPasswordField.setText("");
            confirmPasswordField.setBorder(null);
            confirmPasswordField.updateUI();
            passwordsNoMatchLabel.setVisible(false);
            
            streetAddressTextField.setForeground(new Color(130,123,123));
            streetAddressTextField.setText("Street Address");
            
            cityTextField.setForeground(new Color(130,123,123));    
            cityTextField.setText("City");
            cityTextField.setBorder(null);//throws errors but not significant
            cityTextField.updateUI();
            invalidCityLabel.setVisible(false);
            
            zipCodeTextField.setForeground(new Color(130,123,123)); 
            zipCodeTextField.setText("Zip Code");
            zipCodeTextField.setBorder(null);//throws errors but not significant
            zipCodeTextField.updateUI();
            invalidZipCodeLabel.setVisible(false);
            
            creditCardNumberTextField.setForeground(new Color(130,123,123));
            creditCardNumberTextField.setText("Credit Card Number");
            creditCardNumberTextField.setBorder(null);//throws errors but not significant
            creditCardNumberTextField.updateUI();
            invalidCCLabel.setVisible(false);
            
           
            
            securityCodeTextField.setForeground(new Color(130,123,123));
            securityCodeTextField.setText("Security Code");
            securityCodeTextField.setBorder(null);//throws errors but not significant
            securityCodeTextField.updateUI();
            invalidSCCLabel.setVisible(false);
            
            statesComboBox.setSelectedIndex(0);
            ccExpMonthComboBox.setSelectedIndex(0);
            ccExpYearComboBox.setSelectedIndex(0);
            
            logInEmailTextField.setForeground(new Color(130,123,123));
            logInEmailTextField.setText("E-mail Address");
            logInPasswordField.setForeground(new Color(130,123,123));
            logInPasswordField.setText("Password");
            
            //go to LogIn Screen
            CardLayout card = (CardLayout) displayPanel.getLayout();
            card.show(displayPanel, "card1");
            signInButton.requestFocusInWindow();
            //--->> registration Screen cancel button Commands
        }
    }//GEN-LAST:event_cancelRegistrationButtonActionPerformed

    private void creditCardNumberTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_creditCardNumberTextFieldFocusGained
        // TODO add your handling code here:
        if (creditCardNumberTextField.getText().equals("Credit Card Number")){
            creditCardNumberTextField.setForeground(Color.BLACK);
            creditCardNumberTextField.setText("");
        }
    }//GEN-LAST:event_creditCardNumberTextFieldFocusGained

    private void creditCardNumberTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_creditCardNumberTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        if (creditCardNumberTextField.getText().equals("")){
            invalidCCLabel.setVisible(false);
            creditCardNumberTextField.setBorder(null);//throws errors but not significant
            creditCardNumberTextField.updateUI();
            creditCardNumberTextField.setForeground(new Color(130,123,123));
            creditCardNumberTextField.setText("Credit Card Number");
            //invalidZipCodeLabel.setVisible(false);
        }
        else if (!(vTool.validateNumbers(creditCardNumberTextField.getText()))){
            creditCardNumberTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidCCLabel.setVisible(true);
        }
        else{
            creditCardNumberTextField.setBorder(null);//throws errors but not significant
            creditCardNumberTextField.updateUI();//throws errors but not significant
            invalidCCLabel.setVisible(false);
        }
    }//GEN-LAST:event_creditCardNumberTextFieldFocusLost

    private void securityCodeTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_securityCodeTextFieldFocusGained
        // TODO add your handling code here:
        if (securityCodeTextField.getText().equals("Security Code")){
            securityCodeTextField.setForeground(Color.BLACK);
            securityCodeTextField.setText("");
        }
    }//GEN-LAST:event_securityCodeTextFieldFocusGained

    private void securityCodeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_securityCodeTextFieldFocusLost
        // TODO add your handling code here:
        StringValidationTool vTool = new StringValidationTool();
        if (securityCodeTextField.getText().equals("")){
            invalidSCCLabel.setVisible(false);
            securityCodeTextField.setBorder(null);//throws errors but not significant
            securityCodeTextField.updateUI();
            securityCodeTextField.setForeground(new Color(130,123,123));
            securityCodeTextField.setText("Security Code");
            //invalidZipCodeLabel.setVisible(false);
        }
        else if (!(vTool.validateNumbers(securityCodeTextField.getText()))){
            securityCodeTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2, true));
            invalidSCCLabel.setVisible(true);
        }
        else{
            securityCodeTextField.setBorder(null);//throws errors but not significant
            securityCodeTextField.updateUI();//throws errors but not significant
            invalidSCCLabel.setVisible(false);
        }
    }//GEN-LAST:event_securityCodeTextFieldFocusLost

    private void submitRegistrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitRegistrationButtonActionPerformed
        //DO LAST VALIDATION OF ALL FIELDS BEFORE WRITTING TO DATABASE
        // Check if email address already exists
        //IF email already exists
        //--show 'email address already registered' JOption message dialog 
        //ELSE
        //--Store info on database
        //--Show JOption Pane 'Registration was a Success. Please Use your account info to log in.'
        //----when confirmed: swith to card1=logInScreen
        if(registerEmailTextField.getText().equals("example@email.com") ||  createPasswordField.getText().equals("")
                ||  firstNameTextField.getText().equals("First") ||  lastNameTextField.getText().equals("Last")
                ||  streetAddressTextField.getText().equals("Street Address") ||  cityTextField.getText().equals("City")
                ||  zipCodeTextField.getText().equals("Zip Code") ||  creditCardNumberTextField.getText().equals("Credit Card Number")
                ||  securityCodeTextField.getText().equals("Security Code")){
            JOptionPane.showMessageDialog(null, "You cannot leave any fields unmodified. Try Again.");
        }
        else{
            try { 
                Class.forName("org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection("jdbc:sqlite:database1.sqlite");


                String sql = "SELECT * FROM mtmUSERACCOUNTS WHERE email=?";
                PreparedStatement pst = conn.prepareStatement(sql);

                pst.setString(1,registerEmailTextField.getText());

                ResultSet rs = pst.executeQuery();

                int result = 0;
                while(rs.next()){
                    result+=1;
                }
                rs.close();
                pst.close();

                if(result >= 1){//if email is found,display error  'email already registered'
                    JOptionPane.showMessageDialog(null, "Email address is already registered. Enter a different one, or cancel registration to log in.");
                }
                else{//no matches proceed to insert into database

                    String sql1 = "INSERT INTO mtmUSERACCOUNTS (EMAIL,PASSWORD,FIRSTNAME,LASTNAME,ADDRESS,CITY,STATE,ZIPCODE,CCN,CCNEXPMO,CCNEXPYR,CCNSC) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement pst1 = conn.prepareStatement(sql1);

                    pst1.setString(1,registerEmailTextField.getText());
                    pst1.setString(2,createPasswordField.getText());
                    pst1.setString(3,firstNameTextField.getText());
                    pst1.setString(4,lastNameTextField.getText());
                    pst1.setString(5,streetAddressTextField.getText());
                    pst1.setString(6,cityTextField.getText());
                    pst1.setString(7,statesComboBox.getSelectedItem().toString());
                    pst1.setString(8,zipCodeTextField.getText());
                    pst1.setString(9,creditCardNumberTextField.getText());
                    pst1.setString(10,ccExpMonthComboBox.getSelectedItem().toString());
                    pst1.setString(11,ccExpYearComboBox.getSelectedItem().toString());
                    pst1.setString(12,securityCodeTextField.getText());

                    pst1.execute();

                    javax.swing.JOptionPane.showMessageDialog(null,"Registration was a success. Please log in to purchase your ticket(s).",
                        "Purchase Confirmation",javax.swing.JOptionPane.OK_OPTION,this.purchaseConfimationIcon);

                    pst1.close();

                    //--->> registration Screen cancel button Commands
                    //reset/clear all components in registration form
                    firstNameTextField.setForeground(new Color(130,123,123));
                    firstNameTextField.setText("First");
                    firstNameTextField.setBorder(null);//throws errors but not significant
                    firstNameTextField.updateUI();//throws errors but not significant
                    invalidNameLabel.setVisible(false);

                    lastNameTextField.setForeground(new Color(130,123,123));
                    lastNameTextField.setText("Last");
                    lastNameTextField.setBorder(null);//throws errors but not significant
                    lastNameTextField.updateUI();//throws errors but not significant
                    invalidLNameLabel.setVisible(false);

                    registerEmailTextField.setForeground(new Color(130,123,123));
                    registerEmailTextField.setText("example@email.com");
                    registerEmailTextField.setBorder(null);//throws errors but not significant
                    registerEmailTextField.updateUI();//throws errors but not significant
                    invalidRegisterEmailLabel.setVisible(false);

                    confirmEmailTextField.setForeground(new Color(130,123,123));    
                    confirmEmailTextField.setText("example@email.com");
                    confirmEmailTextField.setBorder(null);//throws errors but not significant
                    confirmEmailTextField.updateUI();//throws errors but not significant
                    emailsNoMatchLabel.setVisible(false);

                    createPasswordField.setText("");
                    createPasswordField.setBorder(null);
                    createPasswordField.updateUI();
                    invalidPasswordTextPane.setVisible(false);

                    confirmPasswordField.setText("");
                    confirmPasswordField.setBorder(null);
                    confirmPasswordField.updateUI();
                    passwordsNoMatchLabel.setVisible(false);

                    streetAddressTextField.setForeground(new Color(130,123,123));
                    streetAddressTextField.setText("Street Address");

                    cityTextField.setForeground(new Color(130,123,123));    
                    cityTextField.setText("City");
                    cityTextField.setBorder(null);//throws errors but not significant
                    cityTextField.updateUI();
                    invalidCityLabel.setVisible(false);

                    zipCodeTextField.setForeground(new Color(130,123,123)); 
                    zipCodeTextField.setText("Zip Code");
                    zipCodeTextField.setBorder(null);//throws errors but not significant
                    zipCodeTextField.updateUI();
                    invalidZipCodeLabel.setVisible(false);

                    creditCardNumberTextField.setForeground(new Color(130,123,123));
                    creditCardNumberTextField.setText("Credit Card Number");
                    creditCardNumberTextField.setBorder(null);//throws errors but not significant
                    creditCardNumberTextField.updateUI();
                    invalidCCLabel.setVisible(false);



                    securityCodeTextField.setForeground(new Color(130,123,123));
                    securityCodeTextField.setText("Security Code");
                    securityCodeTextField.setBorder(null);//throws errors but not significant
                    securityCodeTextField.updateUI();
                    invalidSCCLabel.setVisible(false);

                    statesComboBox.setSelectedIndex(0);
                    ccExpMonthComboBox.setSelectedIndex(0);
                    ccExpYearComboBox.setSelectedIndex(0);


                    //go to LogIn Screen
                    CardLayout card = (CardLayout) displayPanel.getLayout();
                    card.show(displayPanel, "card1");
                    signInButton.requestFocusInWindow();
                    //--->> registration Screen cancel button Commands
                }



            }catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }//GEN-LAST:event_submitRegistrationButtonActionPerformed

    private void camCancelPurchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_camCancelPurchaseButtonActionPerformed
        int answer = javax.swing.JOptionPane.showConfirmDialog(null,"Are you sure you want to cancel this session?",
            "Confirm Cancelation",javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
        if(answer == javax.swing.JOptionPane.YES_OPTION){
            //---->>#1 CANCEL BUTTON COMMANDS
            CardLayout card = (CardLayout) displayPanel.getLayout();
            card.show(displayPanel,"card1");
            //reset movies display panel
            CardLayout card1 = (CardLayout) moviesDisplayPanel.getLayout();
            card1.first(moviesDisplayPanel);
            upButton.setEnabled(false);
            moviesPanelCounter = 0;
            if(!downButton.isEnabled()){
                downButton.setEnabled(true);
            }
            movieSelectedButtonGroup.clearSelection();
            if (movieSelectedButtonGroup.getSelection() == null){
                camNextButton.setEnabled(false);
            }
            //---->>#1 CANCEL BUTTON COMMANDS

            //---->>#3 CANCEL BUTTON COMMANDS
            //unselect any selected seats
            for(int r =0;r<12;r++){//traverses rows
                for(int c =0;c<24;c++){//traverses columns
                    if (arrayOfSelectedSeatsDuringSession[r][c] == 1){//find seats selected and remove them from arrayOfSeats,and seatsSelectedArrayList
                        arrayOfSelectedSeatsDuringSession[r][c] = 0;
                        arrayOfSeats[r][c] = 0;
                        for (int i=0; i<seatsSelectedArrayList.size();i++){
                            if(seatsSelectedArrayList.get(i).equals(arrayOfSeatsJToggleButtons[r][c].getActionCommand())){
                                seatsSelectedArrayList.remove(i);
                            }
                        }       
                    }
                    if(arrayOfSeatsJToggleButtons[r][c].isEnabled()){//if JtoggleButton seat is enabled set to unselected
                        arrayOfSeatsJToggleButtons[r][c].setSelected(false);
                    }

                }
            }
            //reset numOfSeatsSelected
            this.numOfSeatsSelected = 0;
            //reset Seats Selected label
            seatsSelectedLabelTS.setText("Seats: Select Your Seat(s)");
            //---->>#3 CANCEL BUTTON COMMANDS

            //---->>#2 CANCEL BUTTON COMMANDS
            //clear any time selected
            timeSelectedButtonGroup.clearSelection();
            //reset spinners
            adultTicketsSpinner.setValue(0);
            seniorTicketsSpinner.setValue(0);
            childTicketsSpinner.setValue(0);
            //reset adult,senior,child ticket quantities
            this.numOfAdultTickets = 0;
            this.numOfSeniorTickets = 0;
            this.numOfChildTickets = 0;
            //reset all calculations
            this.adultCalc = 0;
            this.seniorCalc = 0;
            this.childCalc = 0;

            movieTimesPanel.removeAll();//remove All movie times toggle buttons from movie times panel
            //set all movie times toggle buttons as enabled
            for(int i =0; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(true);
            }
            //---->>#2 CANCEL BUTTON COMMANDS
        }
    }//GEN-LAST:event_camCancelPurchaseButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        // TODO add your handling code here:
        CardLayout card = (CardLayout) moviesDisplayPanel.getLayout();
        
        //card.show(moviesDisplayPanel, "card2");
        
        card.previous(moviesDisplayPanel);
        moviesPanelCounter--;
        if(moviesPanelCounter == 0){
            upButton.setEnabled(false);
            downButton.setEnabled(true);
        }
        if(moviesPanelCounter == 3){
            downButton.setEnabled(true);
        }
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        
        CardLayout card = (CardLayout) moviesDisplayPanel.getLayout();
        
        //card.show(moviesDisplayPanel, "card2");
        if(moviesPanelCounter == 0){
            upButton.setEnabled(true);
        }
        card.next(moviesDisplayPanel);
        moviesPanelCounter++;
        if(moviesPanelCounter == 4){
            downButton.setEnabled(false);
        }
        
        
        
        
    }//GEN-LAST:event_downButtonActionPerformed

    private void camNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_camNextButtonActionPerformed
      //check which toggle button was selected and store in movieSelected String
        switch(movieSelectedButtonGroup.getSelection().getActionCommand()){
            case "movie1": ///has only 2D movie experience available
                movieSelected = "Addicted"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard1 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard1.show(selectedMoviePanel,"card1");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter1 = 0;
                for(int i = 0; i<movie1TShowings.length; i++){
                    String tempName = movie1TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter1].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter1].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter1].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter1]);
                    jToggleArrayCounter1++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie1TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie1TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie2": ///has only 2D movie experience available
                movieSelected = "AlexanderDisneyMovie"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard2 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard2.show(selectedMoviePanel,"card2");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter2 = 0;
                for(int i = 0; i<movie2TShowings.length; i++){
                    String tempName = movie2TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter2].setFont(new java.awt.Font("Lucida Grande", 1, 40));
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter2].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter2].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter2]);
                    jToggleArrayCounter2++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie2TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie2TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie3": ///has only 2D movie experience available
                movieSelected = "Annabelle"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard3 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard3.show(selectedMoviePanel,"card3");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter3 = 0;
                for(int i = 0; i<movie3TShowings.length; i++){
                    String tempName = movie3TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter3].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter3].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter3].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter3]);
                    jToggleArrayCounter3++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie3TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie3TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie4": ///has 2D and IMAX movie experiences available
                movieSelected = "Dracula Untold"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard4 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard4.show(selectedMoviePanel,"card4");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter4 = 0;
                for(int i = 0; i<movie4TShowings.length; i++){
                    String tempName = movie4TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4]);
                    jToggleArrayCounter4++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie4TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie4TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie5": ///has only 2D movie experience available
                movieSelected = "Fury"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard5 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard5.show(selectedMoviePanel,"card5");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter5 = 0;
                for(int i = 0; i<movie5TShowings.length; i++){
                    String tempName = movie5TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter5].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter5].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter5].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter5]);
                    jToggleArrayCounter5++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie5TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie5TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie6": ///has only 2D movie experience available
                movieSelected = "Gone Girl"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard6 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard6.show(selectedMoviePanel,"card6");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter6 = 0;
                for(int i = 0; i<movie6TShowings.length; i++){
                    String tempName = movie6TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter6].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter6].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter6].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter6]);
                    jToggleArrayCounter6++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie6TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie6TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie7": ///has only 2D movie experience available
                movieSelected = "Guardians of The Galaxy"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard7 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard7.show(selectedMoviePanel,"card7");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter7 = 0;
                for(int i = 0; i<movie7TShowings.length; i++){
                    String tempName = movie7TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter7].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter7].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter7].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter7]);
                    jToggleArrayCounter7++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie7TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie7TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie8": ///has only 2D movie experience available
                movieSelected = "Kill the Messenger"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard8 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard8.show(selectedMoviePanel,"card8");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter8 = 0;
                for(int i = 0; i<movie8TShowings.length; i++){
                    String tempName = movie8TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter8].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter8].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter8].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter8]);
                    jToggleArrayCounter8++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie8TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie8TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie9": ///has only 2D movie experience available
                movieSelected = "Teenage Mutant Ninja Turtles"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard9 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard9.show(selectedMoviePanel,"card9");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter9 = 0;
                for(int i = 0; i<movie9TShowings.length; i++){
                    String tempName = movie9TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter9].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter9].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter9].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter9]);
                    jToggleArrayCounter9++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie9TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie9TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie10": ///has only 2D movie experience available
                movieSelected = "The Best of Me"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard10 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard10.show(selectedMoviePanel,"card10");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter10 = 0;
                for(int i = 0; i<movie10TShowings.length; i++){
                    String tempName = movie10TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter10].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter10].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter10].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter10]);
                    jToggleArrayCounter10++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie10TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie10TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie11": ///has 2D and 3D movie experiences available
                movieSelected = "The Book of Life"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard11 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard11.show(selectedMoviePanel,"card11");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter11 = 0;
                for(int i = 0; i<movie11TShowings.length; i++){
                    String tempName = movie11TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11]);
                    jToggleArrayCounter11++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie11TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie11TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie12": ///has 2D and 3D movie experiences available
                movieSelected = "The BoxTrolls"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard12 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard12.show(selectedMoviePanel,"card12");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter12 = 0;
                for(int i = 0; i<movie12TShowings.length; i++){
                    String tempName = movie12TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12]);
                    jToggleArrayCounter12++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie12TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie12TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie13": ///has only 2D movie experience available
                movieSelected = "The Equalizer"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard13 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard13.show(selectedMoviePanel,"card13");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter13 = 0;
                for(int i = 0; i<movie13TShowings.length; i++){
                    String tempName = movie13TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter13].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter13].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter13].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter13]);
                    jToggleArrayCounter13++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie13TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie13TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie14": ///has only 2D movie experience available
                movieSelected = "The Judge"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard14 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard14.show(selectedMoviePanel,"card14");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter14 = 0;
                for(int i = 0; i<movie14TShowings.length; i++){
                    String tempName = movie14TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter14].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter14].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter14].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter14]);
                    jToggleArrayCounter14++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie14TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie14TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
            case "movie15": ///has only 2D movie experience available
                movieSelected = "The Maze Runner"; System.out.println("You selected"+ movieSelected);
                //disable unavailable movie experiences togglebuttons
                threeDJToggleButton.setEnabled(false);
                imaxJToggleButton.setEnabled(false);
                //always set 2D movieExperience to selected by default
                twoDJToggleButton.setSelected(true);
                CardLayout csacoCard15 = (CardLayout)selectedMoviePanel.getLayout(); csacoCard15.show(selectedMoviePanel,"card15");
                //adds JToggleButtons w/ times of movie1TShowings String array
                int jToggleArrayCounter15 = 0;
                for(int i = 0; i<movie15TShowings.length; i++){
                    String tempName = movie15TShowings[i];
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter15].setFont(new java.awt.Font("Lucida Grande", 1, 40)); 
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter15].setText(tempName);
                    arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter15].setActionCommand(tempName);
                    movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter15]);
                    jToggleArrayCounter15++;
                }
                //sets remainder Jtoggle Buttons as disabled bc they are empty
                if(movie15TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                    for(int i = movie15TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                        if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                        else{
                            arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                            movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                        }
                    }
                }break;
        }
         //(DEFAULT PRICE)keep track of hour of day and display appropriate 2D ticket price
        Calendar calendar = new GregorianCalendar();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(hourOfDay<16){
            adultPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[0]+" =");
            seniorPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[1]+" =");
            childPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[2]+" =");
        }
        else{
            adultPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[0]+" =");
            seniorPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[1]+" =");
            childPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[2]+" =");
        }
        //recalculates price values displayed for adult,senior,child movie ticket quantities
        DecimalFormat decim = new DecimalFormat("#.00");
        if((int)adultTicketsSpinner.getValue() == 0){
            adultTotalTextField.setText("  $0.0  ");
        }
        else{
            int spinnerVal = (int)adultTicketsSpinner.getValue();
            StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
            String priceFromLabel = getPriceFromLabel.substring(2,7);
            double adultPrice = Double.parseDouble(priceFromLabel) ;
            this.adultCalc = spinnerVal * adultPrice;
            double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
            adultTotalTextField.setText("$"+formatedAdultCalc);
        }
        if((int)seniorTicketsSpinner.getValue() == 0){
            seniorTotalTextField.setText("  $0.0  ");
        }
        else{
            int spinnerVal = (int)seniorTicketsSpinner.getValue();
            StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
            String priceFromLabel = getPriceFromLabel.substring(2,7);
            double seniorPrice = Double.parseDouble(priceFromLabel) ;
            this.seniorCalc = spinnerVal * seniorPrice;
            double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
            seniorTotalTextField.setText("$"+formatedSeniorCalc);
        }
        if((int)childTicketsSpinner.getValue() == 0){
            childTotalTextField.setText("  $0.0  ");
        }
        else{
            int spinnerVal = (int)childTicketsSpinner.getValue();
            StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
            String priceFromLabel = getPriceFromLabel.substring(2,7);
            double childPrice = Double.parseDouble(priceFromLabel) ;
            this.childCalc = spinnerVal * childPrice;
            double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
            childTotalTextField.setText("$"+formatedChildCalc);
        }
        CardLayout card = (CardLayout)displayPanel.getLayout();
        card.show(displayPanel,"card4");
        //card.show(moviesPanel, "card2");
    }//GEN-LAST:event_camNextButtonActionPerformed

    private void toggleButtonsListener(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleButtonsListener
        
        //if selection is made, enable next button
        if(movieSelectedButtonGroup.getSelection() != null){
            camNextButton.setEnabled(true);
        }
        //if movie 4 is selected, imax is enabled
        if(movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie4")){
            imaxJToggleButton.setEnabled(true);
        }
        //if movie 11 is selected, 3d is enabled
        if(movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie11")){
            threeDJToggleButton.setEnabled(true);
        }
        //if movie 12 is selected, 3d is enabled
        if(movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie12")){
            threeDJToggleButton.setEnabled(true);
        }
        
    }//GEN-LAST:event_toggleButtonsListener

    private void ctatBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctatBackButtonActionPerformed
        //go to previous screen
        CardLayout card = (CardLayout)displayPanel.getLayout();
        card.show(displayPanel,"card3");
        movieTimesPanel.removeAll();//remove All movie times toggle buttons from movie times panel
        //set all movie times toggle buttons as enabled
        for(int i =0; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
            arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(true);
        }
        timeSelectedButtonGroup.clearSelection();
        twoDJToggleButton.setEnabled(true);
        threeDJToggleButton.setEnabled(true);
        imaxJToggleButton.setEnabled(true);
        movieExperienceButtonGroup.clearSelection();
        ctatNextButton.setEnabled(false);
        
    }//GEN-LAST:event_ctatBackButtonActionPerformed

    private void ctatCancelPurchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctatCancelPurchaseButtonActionPerformed
        // trigger Cancel Purchase Button from selectAMovieScreen
        camCancelPurchaseButton.doClick(); 
    }//GEN-LAST:event_ctatCancelPurchaseButtonActionPerformed

    private void movieExperienceButtonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_movieExperienceButtonsActionPerformed
        //System.out.println("Button state changes");
        //System.out.println(movieSelectedButtonGroup.getSelection().getActionCommand());
        //System.out.println(movieExperienceButtonGroup.getSelection().getActionCommand());
        //System.out.println(evt.getActionCommand());
        DecimalFormat decim = new DecimalFormat("#.00");
        //Redisplay new times in movieTimesPanel if IMAX movie experience is selected for movie4, and appropriate prices based on time of day
        //Also recalculates values for total prices for ticket values in spinners
        if((movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie4")) && (evt.getActionCommand().equals("IMAX"))){
            movieTimesPanel.removeAll();//removes all components from movieTimesPanel
            timeSelectedButtonGroup.clearSelection();
            //adds JToggleButtons w/ times of movie4TShowingsIMAX String array
            int jToggleArrayCounter4 = 0;
            for(int i = 0; i<movie4TShowingsIMAX.length; i++){
                String tempName = movie4TShowingsIMAX[i];
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setEnabled(true);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setText(tempName);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setActionCommand(tempName);
                movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4]);
                jToggleArrayCounter4++;
            }
            //sets remainder Jtoggle Buttons as disabled bc they are empty
            if(movie4TShowingsIMAX.length != arrayOf8JToggleButtonsForTimeShowings.length){
                for(int i = movie4TShowingsIMAX.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                    if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                    else{
                        arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                }
            }
            movieTimesPanel.repaint();
            //set appropriate prices based on time of day
            Calendar calendar = new GregorianCalendar();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay<16){
                adultPriceLabel.setText("x$"+imaxMoviePricesBefore4pm[0]+" =");
                seniorPriceLabel.setText("x$"+imaxMoviePricesBefore4pm[1]+" =");
                childPriceLabel.setText("x$"+imaxMoviePricesBefore4pm[2]+" =");
            }
            else{
                adultPriceLabel.setText("x$"+imaxMoviePricesAfter4pm[0]+" =");
                seniorPriceLabel.setText("x$"+imaxMoviePricesAfter4pm[1]+" =");
                childPriceLabel.setText("x$"+imaxMoviePricesAfter4pm[2]+" =");
            }
            //recalculates price values displayed for adult,senior,child movie ticket quantities
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)adultTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double adultPrice = Double.parseDouble(priceFromLabel) ;
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)seniorTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double seniorPrice = Double.parseDouble(priceFromLabel) ;
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        //Redisplay new times in movieTimesPanel if 2D movie experience is selected for movie4
        if((movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie4")) && (evt.getActionCommand().equals("2D"))){
            movieTimesPanel.removeAll();//removes all components from movieTimesPanel
            timeSelectedButtonGroup.clearSelection();
            //adds JToggleButtons w/ times of movie4TShowings String array
            int jToggleArrayCounter4 = 0;
            for(int i = 0; i<movie4TShowings.length; i++){
                String tempName = movie4TShowings[i];
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setEnabled(true);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setText(tempName);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4].setActionCommand(tempName);
                movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter4]);
                jToggleArrayCounter4++;
            }
            //sets remainder Jtoggle Buttons as disabled bc they are empty
            if(movie4TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                for(int i = movie4TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                    if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                    else{
                        arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                }
            }
            movieTimesPanel.repaint();
            //set appropriate prices based on time of day
            Calendar calendar = new GregorianCalendar();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay<16){
                adultPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[0]+" =");
                seniorPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[1]+" =");
                childPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[2]+" =");
            }
            else{
                adultPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[0]+" =");
                seniorPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[1]+" =");
                childPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[2]+" =");
            }
            //recalculates price values displayed for adult,senior,child movie ticket quantities
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)adultTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double adultPrice = Double.parseDouble(priceFromLabel) ;
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)seniorTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double seniorPrice = Double.parseDouble(priceFromLabel) ;
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        
        
        //Redisplay new times in movieTimesPanel if 3D movie experience is selected for movie11
        if((movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie11")) && (evt.getActionCommand().equals("3D"))){
            movieTimesPanel.removeAll();//removes all components from movieTimesPanel
            timeSelectedButtonGroup.clearSelection();
            //adds JToggleButtons w/ times of movie11TShowings3D String array
            int jToggleArrayCounter11 = 0;
            for(int i = 0; i<movie11TShowings3D.length; i++){
                String tempName = movie11TShowings3D[i];
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setEnabled(true);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setText(tempName);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setActionCommand(tempName);
                movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11]);
                jToggleArrayCounter11++;
            }
            //sets remainder Jtoggle Buttons as disabled bc they are empty
            if(movie11TShowings3D.length != arrayOf8JToggleButtonsForTimeShowings.length){
                for(int i = movie11TShowings3D.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                    if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                    else{
                        arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                }
            }
            movieTimesPanel.repaint();
            //set appropriate prices based on time of day
            Calendar calendar = new GregorianCalendar();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay<16){
                adultPriceLabel.setText("x$"+threeDMoviePricesBefore4pm[0]+" =");
                seniorPriceLabel.setText("x$"+threeDMoviePricesBefore4pm[1]+" =");
                childPriceLabel.setText("x$"+threeDMoviePricesBefore4pm[2]+" =");
            }
            else{
                adultPriceLabel.setText("x$"+threeDMoviePricesAfter4pm[0]+" =");
                seniorPriceLabel.setText("x$"+threeDMoviePricesAfter4pm[1]+" =");
                childPriceLabel.setText("x$"+threeDMoviePricesAfter4pm[2]+" =");
            }
            //recalculates price values displayed for adult,senior,child movie ticket quantities
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)adultTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double adultPrice = Double.parseDouble(priceFromLabel) ;
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)seniorTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double seniorPrice = Double.parseDouble(priceFromLabel) ;
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        
        //Redisplay new times in movieTimesPanel if 2D movie experience is selected for movie11
        if((movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie11")) && (evt.getActionCommand().equals("2D"))){
            movieTimesPanel.removeAll();//removes all components from movieTimesPanel
            timeSelectedButtonGroup.clearSelection();
            //adds JToggleButtons w/ times of movie11TShowings String array
            int jToggleArrayCounter11 = 0;
            for(int i = 0; i<movie11TShowings.length; i++){
                String tempName = movie11TShowings[i];
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setEnabled(true);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setText(tempName);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11].setActionCommand(tempName);
                movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter11]);
                jToggleArrayCounter11++;
            }
            //sets remainder Jtoggle Buttons as disabled bc they are empty
            if(movie11TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                for(int i = movie11TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                    if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                    else{
                        arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                }
            }
            movieTimesPanel.repaint();
            //set appropriate prices based on time of day
            Calendar calendar = new GregorianCalendar();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay<16){
                adultPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[0]+" =");
                seniorPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[1]+" =");
                childPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[2]+" =");
            }
            else{
                adultPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[0]+" =");
                seniorPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[1]+" =");
                childPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[2]+" =");
            }
            //recalculates price values displayed for adult,senior,child movie ticket quantities
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)adultTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double adultPrice = Double.parseDouble(priceFromLabel) ;
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)seniorTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double seniorPrice = Double.parseDouble(priceFromLabel) ;
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        
        
        //Redisplay new times in movieTimesPanel if 3D movie experience is selected for movie12
        if((movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie12")) && (evt.getActionCommand().equals("3D"))){
            movieTimesPanel.removeAll();//removes all components from movieTimesPanel
            timeSelectedButtonGroup.clearSelection();
            //adds JToggleButtons w/ times of movie12TShowings3D String array
            int jToggleArrayCounter12 = 0;
            for(int i = 0; i<movie12TShowings3D.length; i++){
                String tempName = movie12TShowings3D[i];
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setEnabled(true);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setText(tempName);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setActionCommand(tempName);
                movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12]);
                jToggleArrayCounter12++;
            }
            //sets remainder Jtoggle Buttons as disabled bc they are empty
            if(movie12TShowings3D.length != arrayOf8JToggleButtonsForTimeShowings.length){
                for(int i = movie12TShowings3D.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                    if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                    else{
                        arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                }
            }
            movieTimesPanel.repaint();
            //set appropriate prices based on time of day
            Calendar calendar = new GregorianCalendar();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay<16){
                adultPriceLabel.setText("x$"+threeDMoviePricesBefore4pm[0]+" =");
                seniorPriceLabel.setText("x$"+threeDMoviePricesBefore4pm[1]+" =");
                childPriceLabel.setText("x$"+threeDMoviePricesBefore4pm[2]+" =");
            }
            else{
                adultPriceLabel.setText("x$"+threeDMoviePricesAfter4pm[0]+" =");
                seniorPriceLabel.setText("x$"+threeDMoviePricesAfter4pm[1]+" =");
                childPriceLabel.setText("x$"+threeDMoviePricesAfter4pm[2]+" =");
            }
            //recalculates price values displayed for adult,senior,child movie ticket quantities
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)adultTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double adultPrice = Double.parseDouble(priceFromLabel) ;
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)seniorTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double seniorPrice = Double.parseDouble(priceFromLabel) ;
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        
        //Redisplay new times in movieTimesPanel if 2D movie experience is selected for movie12
        if((movieSelectedButtonGroup.getSelection().getActionCommand().equals("movie12")) && (evt.getActionCommand().equals("2D"))){
            movieTimesPanel.removeAll();//removes all components from movieTimesPanel
            timeSelectedButtonGroup.clearSelection();
            //adds JToggleButtons w/ times of movie12TShowings String array
            int jToggleArrayCounter12 = 0;
            for(int i = 0; i<movie12TShowings.length; i++){
                String tempName = movie12TShowings[i];
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setEnabled(true);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setText(tempName);
                arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12].setActionCommand(tempName);
                movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[jToggleArrayCounter12]);
                jToggleArrayCounter12++;
            }
            //sets remainder Jtoggle Buttons as disabled bc they are empty
            if(movie12TShowings.length != arrayOf8JToggleButtonsForTimeShowings.length){
                for(int i = movie12TShowings.length; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                    if(arrayOf8JToggleButtonsForTimeShowings[i].getText().equals("")){
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                    else{
                        arrayOf8JToggleButtonsForTimeShowings[i].setText("");
                        arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(false);
                        movieTimesPanel.add(arrayOf8JToggleButtonsForTimeShowings[i]);
                    }
                }
            }
            movieTimesPanel.repaint();
            //set appropriate prices based on time of day
            Calendar calendar = new GregorianCalendar();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            if(hourOfDay<16){
                adultPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[0]+" =");
                seniorPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[1]+" =");
                childPriceLabel.setText("x$"+twoDMoviePricesBefore4pm[2]+" =");
            }
            else{
                adultPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[0]+" =");
                seniorPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[1]+" =");
                childPriceLabel.setText("x$"+twoDMoviePricesAfter4pm[2]+" =");
            }
            //recalculates price values displayed for adult,senior,child movie ticket quantities
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)adultTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double adultPrice = Double.parseDouble(priceFromLabel) ;
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)seniorTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double seniorPrice = Double.parseDouble(priceFromLabel) ;
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
            }
            else{
                int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        //set next button enable/disable
        if((timeSelectedButtonGroup.getSelection() != null) && (((int)adultTicketsSpinner.getValue()+(int)seniorTicketsSpinner.getValue()+(int)childTicketsSpinner.getValue())!=0)){
            ctatNextButton.setEnabled(true);
        }
        else{
            ctatNextButton.setEnabled(false);
        }
        
               
    }//GEN-LAST:event_movieExperienceButtonsActionPerformed

    private void ctatNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctatNextButtonActionPerformed
        // check if movieExperienceButtonGroup and timeSelectedButtonGroup are not equal to null, and ticketsbought not equal to 0, THEN DO
        if((timeSelectedButtonGroup.getSelection() != null) && (((int)adultTicketsSpinner.getValue()+(int)seniorTicketsSpinner.getValue()+(int)childTicketsSpinner.getValue())!=0)){
            CardLayout Card = (CardLayout)displayPanel.getLayout(); 
            Card.show(displayPanel,"card5");
        }
        //set icon for image in transaction summary and movie title selected
        switch(movieSelectedButtonGroup.getSelection().getActionCommand()){
            case "movie1":
                movieImageTS.setIcon(movie1IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[0]);break;
            case "movie2":
                movieImageTS.setIcon(movie2IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[1]);break;
            case "movie3":
                movieImageTS.setIcon(movie3IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[2]);break;
            case "movie4":
                movieImageTS.setIcon(movie4IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[3]);break;
            case "movie5":
                movieImageTS.setIcon(movie5IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[4]);break;
            case "movie6":
                movieImageTS.setIcon(movie6IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[5]);break;
            case "movie7":
                movieImageTS.setIcon(movie7IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[6]);break;
            case "movie8":
                movieImageTS.setIcon(movie8IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[7]);break;
            case "movie9":
                movieImageTS.setIcon(movie9IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[8]);break;
            case "movie10":
                movieImageTS.setIcon(movie10IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[9]);break;
            case "movie11":
                movieImageTS.setIcon(movie11IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[10]);break;
            case "movie12":
                movieImageTS.setIcon(movie12IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[11]);break;
            case "movie13":
                movieImageTS.setIcon(movie13IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[12]);break;
            case "movie14":
                movieImageTS.setIcon(movie14IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[13]);break;
            case "movie15": 
                movieImageTS.setIcon(movie15IconResizedTS);movieTitleLabelTS.setText(arrayOfFullMovieTitles[14]);break;
        }
        
        //set date
        Calendar calendar = new GregorianCalendar();
        dateOfPurchaseLabelTS.setText(arrayOfDaysOfTheWeek[calendar.get(Calendar.DAY_OF_WEEK)-1]+", "+arrayOfMonthsOfTheYear[calendar.get(Calendar.MONTH)]+", "+calendar.get(Calendar.DAY_OF_MONTH));
        //set time to transaction summary panel
        auditoriumLabelTS.setText("Auditorium: #15 @"+(timeSelectedButtonGroup.getSelection().getActionCommand()));
        
        DecimalFormat decim = new DecimalFormat("#.00");
        //set appropriate text to labels in transactionSummaryPanel
        adultCalculationLabel.setText(adultTicketsSpinner.getValue()+" "+adultPriceLabel.getText());
        adultTotalTS.setText(adultTotalTextField.getText());
        
        seniorCalculationLabel.setText(seniorTicketsSpinner.getValue()+" "+seniorPriceLabel.getText());
        seniorTotalTS.setText(seniorTotalTextField.getText());
        
        childCalculationLabel.setText(childTicketsSpinner.getValue()+" "+childPriceLabel.getText());
        childTotalTS.setText(childTotalTextField.getText());
        
        convenienceFeeCalculationLabel.setText((this.numOfAdultTickets+this.numOfSeniorTickets+this.numOfChildTickets)+"x$1.25 =");
        double formatedconvenienceFeeTotalTS = Double.parseDouble(decim.format((this.numOfAdultTickets+this.numOfSeniorTickets+this.numOfChildTickets)*1.25));
        convenienceFeeTotalTS.setText("$"+formatedconvenienceFeeTotalTS);
        
        //set absolute total cost
        double formatedAbsoluteTotalCalc = Double.parseDouble(decim.format(this.adultCalc+this.seniorCalc+this.childCalc+((this.numOfAdultTickets+this.numOfSeniorTickets+this.numOfChildTickets)*1.25)));
        absoluteTotalTS.setText("$"+formatedAbsoluteTotalCalc);
        //ForTesting
        System.out.println(this.numOfAdultTickets);
        System.out.println(this.adultCalc);
        System.out.println(this.numOfSeniorTickets);
        System.out.println(this.seniorCalc);
        System.out.println(this.numOfChildTickets);
        System.out.println(this.childCalc);
        //System.out.println(((JToggleButton)timeSelectedButtonGroup.getSelection()).getText());
        System.out.println(timeSelectedButtonGroup.getSelection().getActionCommand());
        
        
    }//GEN-LAST:event_ctatNextButtonActionPerformed
    private void showingsSelectionActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if((timeSelectedButtonGroup.getSelection() != null) && (((int)adultTicketsSpinner.getValue()+(int)seniorTicketsSpinner.getValue()+(int)childTicketsSpinner.getValue())!=0)){
            ctatNextButton.setEnabled(true);
        }
        else{
            ctatNextButton.setEnabled(false);
        }
        
    } 
    private void ticketSpinnersStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ticketSpinnersStateChanged
        
        DecimalFormat decim = new DecimalFormat("#.00");
        // calculation for adult tickets
        if(evt.getSource().equals(adultTicketsSpinner)){
            this.numOfAdultTickets = (int)adultTicketsSpinner.getValue();
            int spinnerVal = (int)adultTicketsSpinner.getValue();
            StringBuilder getPriceFromLabel = new StringBuilder(adultPriceLabel.getText());
            String priceFromLabel = getPriceFromLabel.substring(2,7);
            double adultPrice = Double.parseDouble(priceFromLabel);
            if((int)adultTicketsSpinner.getValue() == 0){
                adultTotalTextField.setText("  $0.0  ");
                this.adultCalc = spinnerVal * adultPrice;
            }
            else{
                this.adultCalc = spinnerVal * adultPrice;
                double formatedAdultCalc = Double.parseDouble(decim.format(adultCalc));
                adultTotalTextField.setText("$"+formatedAdultCalc);
            }
        }
        // calculation for senior tickets
        if(evt.getSource().equals(seniorTicketsSpinner)){
            this.numOfSeniorTickets = (int)seniorTicketsSpinner.getValue();
            int spinnerVal = (int)seniorTicketsSpinner.getValue();
            StringBuilder getPriceFromLabel = new StringBuilder(seniorPriceLabel.getText());
            String priceFromLabel = getPriceFromLabel.substring(2,7);
            double seniorPrice = Double.parseDouble(priceFromLabel);
            if((int)seniorTicketsSpinner.getValue() == 0){
                seniorTotalTextField.setText("  $0.0  ");
                this.seniorCalc = spinnerVal * seniorPrice;
            }
            else{
                this.seniorCalc = spinnerVal * seniorPrice;
                double formatedSeniorCalc = Double.parseDouble(decim.format(seniorCalc));
                seniorTotalTextField.setText("$"+formatedSeniorCalc);
            }
        }
        // calculation for child tickets
        if(evt.getSource().equals(childTicketsSpinner)){
            this.numOfChildTickets = (int)childTicketsSpinner.getValue();
            int spinnerVal = (int)childTicketsSpinner.getValue();
                StringBuilder getPriceFromLabel = new StringBuilder(childPriceLabel.getText());
                String priceFromLabel = getPriceFromLabel.substring(2,7);
                double childPrice = Double.parseDouble(priceFromLabel) ;
            if((int)childTicketsSpinner.getValue() == 0){
                childTotalTextField.setText("  $0.0  ");
                this.childCalc = spinnerVal * childPrice;
            }
            else{
                this.childCalc = spinnerVal * childPrice;
                double formatedChildCalc = Double.parseDouble(decim.format(childCalc));
                childTotalTextField.setText("$"+formatedChildCalc);
            }
        }
        if((timeSelectedButtonGroup.getSelection() != null) && (((int)adultTicketsSpinner.getValue()+(int)seniorTicketsSpinner.getValue()+(int)childTicketsSpinner.getValue())!=0)){
            ctatNextButton.setEnabled(true);
        }
        else{
            ctatNextButton.setEnabled(false);
        }
        
        //FOR TESTING
        System.out.println("Adult tickets: "+this.numOfAdultTickets);
        System.out.println("Senior tickets: "+this.numOfSeniorTickets);
        System.out.println("Child tickets: "+this.numOfChildTickets);
        
    }//GEN-LAST:event_ticketSpinnersStateChanged

    private void csacpCancelPurchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csacpCancelPurchaseButtonActionPerformed
        ctatCancelPurchaseButton.doClick();
    }//GEN-LAST:event_csacpCancelPurchaseButtonActionPerformed

    private void csacpBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csacpBackButtonActionPerformed
        // 
        CardLayout card = (CardLayout) displayPanel.getLayout();
        card.show(displayPanel, "card4");
    }//GEN-LAST:event_csacpBackButtonActionPerformed

    
    
    
    private void csacpCompletePurchaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csacpCompletePurchaseButtonActionPerformed
        //--check if amount of seats selected equals the amount of tickets being purchased
        //if everything correct, save new data to database, show temporary thanks for your purchase screen then back to logIn Screen
        if (this.numOfSeatsSelected != (this.numOfAdultTickets+this.numOfChildTickets+this.numOfSeniorTickets)){
            javax.swing.JOptionPane.showMessageDialog(null,"Please select only "+(this.numOfAdultTickets+this.numOfChildTickets+this.numOfSeniorTickets)+" seat(s).",
                "Seat Selection",javax.swing.JOptionPane.OK_OPTION);
        }
        else{//write to database and quit
            
            int [][]arrayOfSeatToWriteToDB = reverseRepositionArray(this.arrayOfSeats);
            
            try { 
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database1.sqlite");
            int A = 0;
            int B = 0;
            int C = 0;
            int D = 0;
            int E = 0;
            int F = 0;
            int G = 0;
            int H = 0;
            int I = 0;
            int J = 0;
            int K = 0;
            int L = 0;
          
            for(int r =0;r<24;r++){
                for(int c=0;c<12;c++){
                    if(c==0){
                        A=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==1){
                        B=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==2){
                        C=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==3){
                        D=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==4){
                        E=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==5){
                        F=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==6){
                        G=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==7){
                        H=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==8){
                        I=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==9){
                        J=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==10){
                        K=arrayOfSeatToWriteToDB[r][c];
                    }
                    else if(c==11){
                        L=arrayOfSeatToWriteToDB[r][c];
                    }
                }
                String sql = "UPDATE auditorium15seats SET A='"+A+"', B='"+B+"', C='"+C+"', D='"+D+"', E='"+E+"', F='"+F+"', G='"+G+"', H='"+H+"', I='"+I+"', J='"+J+"', K='"+K+"', L='"+L+"' WHERE rowID='"+(r+1)+"'";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.execute();
                //pst.close(); 
            }
              
            
            }catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
            
            javax.swing.JOptionPane.showMessageDialog(null,"Purchase was a success. Please Collect Your Ticket(s) and enjoy the Movie.",
                "Purchase Confirmation",javax.swing.JOptionPane.OK_OPTION,this.purchaseConfimationIcon);
            readSeatsFromDB();//read seats chart minus the selected from previous 
            //sets toggle buttons available o not
            for(int r =0; r<12;r++){//traverses rows
                for(int c=0; c<24; c++){//traverses Columns
                   if (arrayOfSeats[r][c] == 1){//if seat is 'taken'
                     arrayOfSeatsJToggleButtons[r][c].setEnabled(false);
                   }
                }
            }
            //---->>#1 CANCEL BUTTON COMMANDS
            CardLayout card = (CardLayout) displayPanel.getLayout();
            card.show(displayPanel,"card1");
            signInButton.requestFocusInWindow();
            //reset movies display panel
            CardLayout card1 = (CardLayout) moviesDisplayPanel.getLayout();
            card1.first(moviesDisplayPanel);
            upButton.setEnabled(false);
            moviesPanelCounter = 0;
            if(!downButton.isEnabled()){
                downButton.setEnabled(true);
            }
            movieSelectedButtonGroup.clearSelection();
            if (movieSelectedButtonGroup.getSelection() == null){
                camNextButton.setEnabled(false);
            }
            //---->>#1 CANCEL BUTTON COMMANDS

            //---->>#3 CANCEL BUTTON COMMANDS
            //unselect any selected seats
            for(int r =0;r<12;r++){//traverses rows
                for(int c =0;c<24;c++){//traverses columns
                    if (arrayOfSelectedSeatsDuringSession[r][c] == 1){//find seats selected and remove them from arrayOfSeats,and seatsSelectedArrayList
                        arrayOfSelectedSeatsDuringSession[r][c] = 0;
                        arrayOfSeats[r][c] = 0;
                        for (int i=0; i<seatsSelectedArrayList.size();i++){
                            if(seatsSelectedArrayList.get(i).equals(arrayOfSeatsJToggleButtons[r][c].getActionCommand())){
                                seatsSelectedArrayList.remove(i);
                            }
                        }       
                    }
                    if(arrayOfSeatsJToggleButtons[r][c].isEnabled()){//if JtoggleButton seat is enabled set to unselected
                        arrayOfSeatsJToggleButtons[r][c].setSelected(false);
                    }

                }
            }
            //reset numOfSeatsSelected
            this.numOfSeatsSelected = 0;
            //reset Seats Selected label
            seatsSelectedLabelTS.setText("Seats: Select Your Seat(s)");
            //---->>#3 CANCEL BUTTON COMMANDS

            //---->>#2 CANCEL BUTTON COMMANDS
            //clear any time selected
            timeSelectedButtonGroup.clearSelection();
            //reset spinners
            adultTicketsSpinner.setValue(0);
            seniorTicketsSpinner.setValue(0);
            childTicketsSpinner.setValue(0);
            //reset adult,senior,child ticket quantities
            this.numOfAdultTickets = 0;
            this.numOfSeniorTickets = 0;
            this.numOfChildTickets = 0;
            //reset all calculations
            this.adultCalc = 0;
            this.seniorCalc = 0;
            this.childCalc = 0;

            movieTimesPanel.removeAll();//remove All movie times toggle buttons from movie times panel
            //set all movie times toggle buttons as enabled
            for(int i =0; i<arrayOf8JToggleButtonsForTimeShowings.length; i++){
                arrayOf8JToggleButtonsForTimeShowings[i].setEnabled(true);
            }
            //---->>#2 CANCEL BUTTON COMMANDS
        }  
    }//GEN-LAST:event_csacpCompletePurchaseButtonActionPerformed

    private void seatSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seatSelectionActionPerformed
        // getActionCommand-> seat 'A1..etc'
        ///changes values of arrayOfSeats and prepares it for writting on database
        
        
        for(int r =0;r<12;r++){//traverses rows of arrayOfSeatsJToggleButtons
            for(int c=0; c<24;c++){//traverses columns of arrayOfSeatsJToggleButtons[r][c]
                int sentinel1=0;
                if(arrayOfSeatsJToggleButtons[r][c].getActionCommand().equals(evt.getActionCommand())){//if action command matches element in array
                    if(arrayOfSeats[r][c]==0){//if seat is available
                        arrayOfSeats[r][c]=1;//seat is now taken
                        this.numOfSeatsSelected++;//increase counter for seats selected
                        //add value to arrayList of seats
                        this.seatsSelectedArrayList.add(evt.getActionCommand());
                        //add value to arrayOfSelectedSeatsDuringSession which is a temporary array holder of values
                        this.arrayOfSelectedSeatsDuringSession[r][c] = 1;
                    }
                    else if(arrayOfSeats[r][c]==1){//if seat is unavailable
                        arrayOfSeats[r][c] = 0;//seat is now available
                        this.arrayOfSelectedSeatsDuringSession[r][c] = 0;//seat now available
                        this.numOfSeatsSelected--;//decrease counter for seats selected
                        //find value in arrayList of seats and remove it
                        for (int i=0; i<seatsSelectedArrayList.size();i++){
                            if(seatsSelectedArrayList.get(i).equals(evt.getActionCommand())){
                                seatsSelectedArrayList.remove(i);
                            }
                        }
                    }
                    sentinel1++;
                    break;
                }
                if(sentinel1!=0){
                    break;
                }
            }
        }
        //set seatsSelectedLabelTS label equal to seats selected
        String sumOfSeats="";
        for(String seat: seatsSelectedArrayList){
            sumOfSeats+=seat+",";
        }
        seatsSelectedLabelTS.setText("Seats: "+sumOfSeats);
        
        //set Complete purchase enable/disable
        if(this.numOfSeatsSelected == 0){
            csacpCompletePurchaseButton.setEnabled(false);
            seatsSelectedLabelTS.setText("Seats: Select Your Seat(s)");//if num of seats selected is 0 set default label
        }
        else{
            csacpCompletePurchaseButton.setEnabled(true);
        }
        
   
        /*
        //FOR TESTING
        
        for(int r =0;r<12;r++){//traverses rows
            for(int c = 0; c<24; c++){//traverses columns
                System.out.print(arrayOfSeats[r][c]+",");
            }
            System.out.print("\n");
        }
        System.out.println(numOfSeatsSelected);
           
        for (String seat : seatsSelectedArrayList){
            System.out.print(seat+",");
        }
        */
        
    }//GEN-LAST:event_seatSelectionActionPerformed

    private void logInPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInPasswordFieldActionPerformed
        // TODO add your handling code here:
        signInButton.doClick();
    }//GEN-LAST:event_logInPasswordFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel absoluteTotalLabelTS;
    private javax.swing.JLabel absoluteTotalTS;
    private javax.swing.JLabel adultCalculationLabel;
    private javax.swing.JLabel adultLabelTS;
    private javax.swing.JLabel adultPriceLabel;
    private javax.swing.JPanel adultSpinnerPanel;
    private javax.swing.JLabel adultTicketLabel;
    private javax.swing.JPanel adultTicketsCalculationPanel;
    private javax.swing.JSpinner adultTicketsSpinner;
    private javax.swing.JPanel adultTicketsTitlePanel;
    private javax.swing.JLabel adultTotalTS;
    private javax.swing.JTextField adultTotalTextField;
    private javax.swing.JLabel auditoriumLabelTS;
    private javax.swing.JLabel availableButtonLabel;
    private javax.swing.JPanel availableLeftPanel;
    private javax.swing.JPanel availablePanel;
    private javax.swing.JToggleButton availableToggleButtonTS;
    private javax.swing.JLabel billingAddressLabel1;
    private javax.swing.JLabel billingAddressLabel2;
    private javax.swing.JButton camCancelPurchaseButton;
    private javax.swing.JPanel camNavButtonPanel;
    private javax.swing.JButton camNextButton;
    private javax.swing.JLabel camTitleLabel;
    private javax.swing.JButton cancelRegistrationButton;
    private javax.swing.JComboBox ccExpMonthComboBox;
    private javax.swing.JLabel ccExpMonthLabel;
    private javax.swing.JComboBox ccExpYearComboBox;
    private javax.swing.JLabel ccExpYearLabel;
    private javax.swing.JLabel childCalculationLabel;
    private javax.swing.JLabel childLabelTS;
    private javax.swing.JLabel childPriceLabel;
    private javax.swing.JPanel childSpinnerPanel;
    private javax.swing.JLabel childTicketLabel;
    private javax.swing.JPanel childTicketsCalculationPanel;
    private javax.swing.JSpinner childTicketsSpinner;
    private javax.swing.JPanel childTicketsTitlePanel;
    private javax.swing.JLabel childTotalTS;
    private javax.swing.JTextField childTotalTextField;
    private javax.swing.JPanel chooseAMovieScreen;
    private javax.swing.JPanel chooseAMovieTitlePanel;
    private javax.swing.JPanel chooseSeatsAndCompletePurchaseScreen;
    private javax.swing.JPanel chooseTTdisplayPanel;
    private javax.swing.JPanel chooseTicketQty;
    private javax.swing.JPanel chooseTimeAnd3DPanel;
    private javax.swing.JLabel chooseTimeAndTicketsLabel;
    private javax.swing.JPanel chooseTimeAndTicketsScreen;
    private javax.swing.JPanel chooseTimeAndTicketsTitlePanel;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JLabel confirmEmailAddressLabel;
    private javax.swing.JTextField confirmEmailTextField;
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JLabel convenienceFeeCalculationLabel;
    private javax.swing.JLabel convenienceFeeLabelTS;
    private javax.swing.JLabel convenienceFeeTotalTS;
    private javax.swing.JPanel cpagbButtonsPanel;
    private javax.swing.JPasswordField createPasswordField;
    private javax.swing.JLabel createPasswordLabel;
    private javax.swing.JTextField creditCardNumberTextField;
    private javax.swing.JButton csacpBackButton;
    private javax.swing.JButton csacpCancelPurchaseButton;
    private javax.swing.JButton csacpCompletePurchaseButton;
    private javax.swing.JPanel csacpDisplayPanel;
    private javax.swing.JPanel csacpNavButtonPanel;
    private javax.swing.JLabel csacpTitleLabel;
    private javax.swing.JLabel csacpTitleLabel2;
    private javax.swing.JPanel csacpTitlePanel;
    private javax.swing.JButton ctatBackButton;
    private javax.swing.JButton ctatCancelPurchaseButton;
    private javax.swing.JPanel ctatNavButtonPanel;
    private javax.swing.JButton ctatNextButton;
    private javax.swing.JLabel dateOfPurchaseLabelTS;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JButton downButton;
    private javax.swing.JLabel emailAddressLabel;
    private javax.swing.JLabel emailsNoMatchLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JToggleButton imaxJToggleButton;
    private javax.swing.JLabel incorrectEmailOrPasswordLabel;
    private javax.swing.JLabel invalidCCLabel;
    private javax.swing.JLabel invalidCityLabel;
    private javax.swing.JLabel invalidLNameLabel;
    private javax.swing.JLabel invalidLogInEmailLabel;
    private javax.swing.JLabel invalidNameLabel;
    private javax.swing.JTextPane invalidPasswordTextPane;
    private javax.swing.JLabel invalidRegisterEmailLabel;
    private javax.swing.JLabel invalidSCCLabel;
    private javax.swing.JLabel invalidZipCodeLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton joinButton;
    private javax.swing.JPanel joinMessagePanel;
    private javax.swing.JLabel joinPanelLabel1;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JPanel leftAndRightHalvesPanel;
    private javax.swing.JPanel leftHalf;
    private javax.swing.JTextField logInEmailTextField;
    private javax.swing.JPanel logInInfoPanel;
    private javax.swing.JPasswordField logInPasswordField;
    private javax.swing.JPanel logInScreen;
    private javax.swing.JPanel midPanel;
    private javax.swing.JToggleButton movie10JToggleButton;
    private javax.swing.JToggleButton movie11JToggleButton;
    private javax.swing.JToggleButton movie12JToggleButton;
    private javax.swing.JToggleButton movie13JToggleButton;
    private javax.swing.JToggleButton movie14JToggleButton;
    private javax.swing.JToggleButton movie15JToggleButton;
    private javax.swing.JToggleButton movie1JToggleButton;
    private javax.swing.JToggleButton movie2JToggleButton;
    private javax.swing.JToggleButton movie3JToggleButton;
    private javax.swing.JToggleButton movie4JToggleButton;
    private javax.swing.JToggleButton movie5JToggleButton;
    private javax.swing.JToggleButton movie6JToggleButton;
    private javax.swing.JToggleButton movie7JToggleButton;
    private javax.swing.JToggleButton movie8JToggleButton;
    private javax.swing.JToggleButton movie9JToggleButton;
    private javax.swing.JLabel movieExerienceLabel;
    private javax.swing.ButtonGroup movieExperienceButtonGroup;
    private javax.swing.JPanel movieExperienceTitle;
    private javax.swing.JLabel movieImageTS;
    private javax.swing.ButtonGroup movieSelectedButtonGroup;
    private javax.swing.JPanel movieTicketsQtyTitlePanel;
    private javax.swing.JLabel movieTimesLabel;
    private javax.swing.JPanel movieTimesPanel;
    private javax.swing.JPanel movieTimesTitle;
    private javax.swing.JLabel movieTitleLabelTS;
    private javax.swing.JPanel moviesDisplayPanel;
    private javax.swing.JLabel mtmLogo;
    private javax.swing.JLabel mtmLogo1;
    private javax.swing.JLabel mtmLogo2;
    private javax.swing.JLabel mtmLogo3;
    private javax.swing.JLabel mtmLogo4;
    private javax.swing.JLabel mtmLogo5;
    private javax.swing.JLabel mtqtLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel passwordsNoMatchLabel;
    private javax.swing.JPanel paymentInfoPanel;
    private javax.swing.JTextField registerEmailTextField;
    private javax.swing.JPanel registerScreen;
    private javax.swing.JPanel registerScreenButtonPanel;
    private javax.swing.JPanel registerTitlePanel;
    private javax.swing.JPanel resizeTBPanel;
    private javax.swing.JPanel resizeTBPanel2;
    private javax.swing.JPanel rightHalf;
    private javax.swing.JPanel row1Movies;
    private javax.swing.JPanel row2Movies;
    private javax.swing.JPanel row3Movies;
    private javax.swing.JPanel row4Movies;
    private javax.swing.JPanel row5Movies;
    private javax.swing.JToggleButton scmovie10JToggleButton;
    private javax.swing.JToggleButton scmovie11JToggleButton;
    private javax.swing.JToggleButton scmovie12JToggleButton;
    private javax.swing.JToggleButton scmovie13JToggleButton;
    private javax.swing.JToggleButton scmovie14JToggleButton;
    private javax.swing.JToggleButton scmovie15JToggleButton;
    private javax.swing.JToggleButton scmovie1JToggleButton;
    private javax.swing.JToggleButton scmovie2JToggleButton;
    private javax.swing.JToggleButton scmovie3JToggleButton;
    private javax.swing.JToggleButton scmovie4JToggleButton;
    private javax.swing.JToggleButton scmovie5JToggleButton;
    private javax.swing.JToggleButton scmovie6JToggleButton;
    private javax.swing.JToggleButton scmovie7JToggleButton;
    private javax.swing.JToggleButton scmovie8JToggleButton;
    private javax.swing.JToggleButton scmovie9JToggleButton;
    private javax.swing.JLabel screenLabel;
    private javax.swing.JPanel screenLabelPanel;
    private javax.swing.JToggleButton seatA1;
    private javax.swing.JToggleButton seatA10;
    private javax.swing.JToggleButton seatA11;
    private javax.swing.JToggleButton seatA12;
    private javax.swing.JToggleButton seatA13;
    private javax.swing.JToggleButton seatA14;
    private javax.swing.JToggleButton seatA15;
    private javax.swing.JToggleButton seatA16;
    private javax.swing.JToggleButton seatA17;
    private javax.swing.JToggleButton seatA18;
    private javax.swing.JToggleButton seatA19;
    private javax.swing.JToggleButton seatA2;
    private javax.swing.JToggleButton seatA20;
    private javax.swing.JToggleButton seatA21;
    private javax.swing.JToggleButton seatA22;
    private javax.swing.JToggleButton seatA3;
    private javax.swing.JToggleButton seatA4;
    private javax.swing.JToggleButton seatA5;
    private javax.swing.JToggleButton seatA6;
    private javax.swing.JToggleButton seatA7;
    private javax.swing.JToggleButton seatA8;
    private javax.swing.JToggleButton seatA9;
    private javax.swing.JToggleButton seatB1;
    private javax.swing.JToggleButton seatB10;
    private javax.swing.JToggleButton seatB11;
    private javax.swing.JToggleButton seatB12;
    private javax.swing.JToggleButton seatB13;
    private javax.swing.JToggleButton seatB14;
    private javax.swing.JToggleButton seatB15;
    private javax.swing.JToggleButton seatB16;
    private javax.swing.JToggleButton seatB17;
    private javax.swing.JToggleButton seatB18;
    private javax.swing.JToggleButton seatB19;
    private javax.swing.JToggleButton seatB2;
    private javax.swing.JToggleButton seatB20;
    private javax.swing.JToggleButton seatB21;
    private javax.swing.JToggleButton seatB22;
    private javax.swing.JToggleButton seatB3;
    private javax.swing.JToggleButton seatB4;
    private javax.swing.JToggleButton seatB5;
    private javax.swing.JToggleButton seatB6;
    private javax.swing.JToggleButton seatB7;
    private javax.swing.JToggleButton seatB8;
    private javax.swing.JToggleButton seatB9;
    private javax.swing.JToggleButton seatC1;
    private javax.swing.JToggleButton seatC10;
    private javax.swing.JToggleButton seatC11;
    private javax.swing.JToggleButton seatC12;
    private javax.swing.JToggleButton seatC13;
    private javax.swing.JToggleButton seatC14;
    private javax.swing.JToggleButton seatC15;
    private javax.swing.JToggleButton seatC16;
    private javax.swing.JToggleButton seatC17;
    private javax.swing.JToggleButton seatC18;
    private javax.swing.JToggleButton seatC19;
    private javax.swing.JToggleButton seatC2;
    private javax.swing.JToggleButton seatC20;
    private javax.swing.JToggleButton seatC21;
    private javax.swing.JToggleButton seatC22;
    private javax.swing.JToggleButton seatC3;
    private javax.swing.JToggleButton seatC4;
    private javax.swing.JToggleButton seatC5;
    private javax.swing.JToggleButton seatC6;
    private javax.swing.JToggleButton seatC7;
    private javax.swing.JToggleButton seatC8;
    private javax.swing.JToggleButton seatC9;
    private javax.swing.JToggleButton seatD1;
    private javax.swing.JToggleButton seatD10;
    private javax.swing.JToggleButton seatD11;
    private javax.swing.JToggleButton seatD12;
    private javax.swing.JToggleButton seatD13;
    private javax.swing.JToggleButton seatD14;
    private javax.swing.JToggleButton seatD15;
    private javax.swing.JToggleButton seatD16;
    private javax.swing.JToggleButton seatD17;
    private javax.swing.JToggleButton seatD18;
    private javax.swing.JToggleButton seatD19;
    private javax.swing.JToggleButton seatD2;
    private javax.swing.JToggleButton seatD20;
    private javax.swing.JToggleButton seatD21;
    private javax.swing.JToggleButton seatD22;
    private javax.swing.JToggleButton seatD3;
    private javax.swing.JToggleButton seatD4;
    private javax.swing.JToggleButton seatD5;
    private javax.swing.JToggleButton seatD6;
    private javax.swing.JToggleButton seatD7;
    private javax.swing.JToggleButton seatD8;
    private javax.swing.JToggleButton seatD9;
    private javax.swing.JToggleButton seatE1;
    private javax.swing.JToggleButton seatE10;
    private javax.swing.JToggleButton seatE11;
    private javax.swing.JToggleButton seatE12;
    private javax.swing.JToggleButton seatE13;
    private javax.swing.JToggleButton seatE14;
    private javax.swing.JToggleButton seatE15;
    private javax.swing.JToggleButton seatE16;
    private javax.swing.JToggleButton seatE17;
    private javax.swing.JToggleButton seatE18;
    private javax.swing.JToggleButton seatE19;
    private javax.swing.JToggleButton seatE2;
    private javax.swing.JToggleButton seatE3;
    private javax.swing.JToggleButton seatE4;
    private javax.swing.JToggleButton seatE5;
    private javax.swing.JToggleButton seatE6;
    private javax.swing.JToggleButton seatE7;
    private javax.swing.JToggleButton seatE8;
    private javax.swing.JToggleButton seatE9;
    private javax.swing.JToggleButton seatF1;
    private javax.swing.JToggleButton seatF10;
    private javax.swing.JToggleButton seatF11;
    private javax.swing.JToggleButton seatF12;
    private javax.swing.JToggleButton seatF13;
    private javax.swing.JToggleButton seatF14;
    private javax.swing.JToggleButton seatF15;
    private javax.swing.JToggleButton seatF16;
    private javax.swing.JToggleButton seatF17;
    private javax.swing.JToggleButton seatF18;
    private javax.swing.JToggleButton seatF19;
    private javax.swing.JToggleButton seatF2;
    private javax.swing.JToggleButton seatF20;
    private javax.swing.JToggleButton seatF21;
    private javax.swing.JToggleButton seatF22;
    private javax.swing.JToggleButton seatF3;
    private javax.swing.JToggleButton seatF4;
    private javax.swing.JToggleButton seatF5;
    private javax.swing.JToggleButton seatF6;
    private javax.swing.JToggleButton seatF7;
    private javax.swing.JToggleButton seatF8;
    private javax.swing.JToggleButton seatF9;
    private javax.swing.JToggleButton seatG1;
    private javax.swing.JToggleButton seatG10;
    private javax.swing.JToggleButton seatG11;
    private javax.swing.JToggleButton seatG12;
    private javax.swing.JToggleButton seatG13;
    private javax.swing.JToggleButton seatG14;
    private javax.swing.JToggleButton seatG15;
    private javax.swing.JToggleButton seatG16;
    private javax.swing.JToggleButton seatG17;
    private javax.swing.JToggleButton seatG18;
    private javax.swing.JToggleButton seatG19;
    private javax.swing.JToggleButton seatG2;
    private javax.swing.JToggleButton seatG20;
    private javax.swing.JToggleButton seatG21;
    private javax.swing.JToggleButton seatG22;
    private javax.swing.JToggleButton seatG3;
    private javax.swing.JToggleButton seatG4;
    private javax.swing.JToggleButton seatG5;
    private javax.swing.JToggleButton seatG6;
    private javax.swing.JToggleButton seatG7;
    private javax.swing.JToggleButton seatG8;
    private javax.swing.JToggleButton seatG9;
    private javax.swing.JToggleButton seatH1;
    private javax.swing.JToggleButton seatH10;
    private javax.swing.JToggleButton seatH11;
    private javax.swing.JToggleButton seatH12;
    private javax.swing.JToggleButton seatH13;
    private javax.swing.JToggleButton seatH14;
    private javax.swing.JToggleButton seatH15;
    private javax.swing.JToggleButton seatH16;
    private javax.swing.JToggleButton seatH17;
    private javax.swing.JToggleButton seatH18;
    private javax.swing.JToggleButton seatH19;
    private javax.swing.JToggleButton seatH2;
    private javax.swing.JToggleButton seatH20;
    private javax.swing.JToggleButton seatH21;
    private javax.swing.JToggleButton seatH22;
    private javax.swing.JToggleButton seatH3;
    private javax.swing.JToggleButton seatH4;
    private javax.swing.JToggleButton seatH5;
    private javax.swing.JToggleButton seatH6;
    private javax.swing.JToggleButton seatH7;
    private javax.swing.JToggleButton seatH8;
    private javax.swing.JToggleButton seatH9;
    private javax.swing.JToggleButton seatI1;
    private javax.swing.JToggleButton seatI10;
    private javax.swing.JToggleButton seatI11;
    private javax.swing.JToggleButton seatI12;
    private javax.swing.JToggleButton seatI13;
    private javax.swing.JToggleButton seatI14;
    private javax.swing.JToggleButton seatI15;
    private javax.swing.JToggleButton seatI16;
    private javax.swing.JToggleButton seatI17;
    private javax.swing.JToggleButton seatI18;
    private javax.swing.JToggleButton seatI19;
    private javax.swing.JToggleButton seatI2;
    private javax.swing.JToggleButton seatI20;
    private javax.swing.JToggleButton seatI21;
    private javax.swing.JToggleButton seatI22;
    private javax.swing.JToggleButton seatI3;
    private javax.swing.JToggleButton seatI4;
    private javax.swing.JToggleButton seatI5;
    private javax.swing.JToggleButton seatI6;
    private javax.swing.JToggleButton seatI7;
    private javax.swing.JToggleButton seatI8;
    private javax.swing.JToggleButton seatI9;
    private javax.swing.JToggleButton seatJ1;
    private javax.swing.JToggleButton seatJ10;
    private javax.swing.JToggleButton seatJ11;
    private javax.swing.JToggleButton seatJ12;
    private javax.swing.JToggleButton seatJ13;
    private javax.swing.JToggleButton seatJ14;
    private javax.swing.JToggleButton seatJ15;
    private javax.swing.JToggleButton seatJ16;
    private javax.swing.JToggleButton seatJ17;
    private javax.swing.JToggleButton seatJ18;
    private javax.swing.JToggleButton seatJ19;
    private javax.swing.JToggleButton seatJ2;
    private javax.swing.JToggleButton seatJ20;
    private javax.swing.JToggleButton seatJ21;
    private javax.swing.JToggleButton seatJ22;
    private javax.swing.JToggleButton seatJ3;
    private javax.swing.JToggleButton seatJ4;
    private javax.swing.JToggleButton seatJ5;
    private javax.swing.JToggleButton seatJ6;
    private javax.swing.JToggleButton seatJ7;
    private javax.swing.JToggleButton seatJ8;
    private javax.swing.JToggleButton seatJ9;
    private javax.swing.JToggleButton seatK1;
    private javax.swing.JToggleButton seatK10;
    private javax.swing.JToggleButton seatK11;
    private javax.swing.JToggleButton seatK12;
    private javax.swing.JToggleButton seatK13;
    private javax.swing.JToggleButton seatK14;
    private javax.swing.JToggleButton seatK15;
    private javax.swing.JToggleButton seatK16;
    private javax.swing.JToggleButton seatK17;
    private javax.swing.JToggleButton seatK18;
    private javax.swing.JToggleButton seatK19;
    private javax.swing.JToggleButton seatK2;
    private javax.swing.JToggleButton seatK20;
    private javax.swing.JToggleButton seatK21;
    private javax.swing.JToggleButton seatK22;
    private javax.swing.JToggleButton seatK3;
    private javax.swing.JToggleButton seatK4;
    private javax.swing.JToggleButton seatK5;
    private javax.swing.JToggleButton seatK6;
    private javax.swing.JToggleButton seatK7;
    private javax.swing.JToggleButton seatK8;
    private javax.swing.JToggleButton seatK9;
    private javax.swing.JToggleButton seatL1;
    private javax.swing.JToggleButton seatL10;
    private javax.swing.JToggleButton seatL11;
    private javax.swing.JToggleButton seatL12;
    private javax.swing.JToggleButton seatL13;
    private javax.swing.JToggleButton seatL14;
    private javax.swing.JToggleButton seatL15;
    private javax.swing.JToggleButton seatL16;
    private javax.swing.JToggleButton seatL17;
    private javax.swing.JToggleButton seatL18;
    private javax.swing.JToggleButton seatL19;
    private javax.swing.JToggleButton seatL2;
    private javax.swing.JToggleButton seatL20;
    private javax.swing.JToggleButton seatL21;
    private javax.swing.JToggleButton seatL22;
    private javax.swing.JToggleButton seatL23;
    private javax.swing.JToggleButton seatL24;
    private javax.swing.JToggleButton seatL3;
    private javax.swing.JToggleButton seatL4;
    private javax.swing.JToggleButton seatL5;
    private javax.swing.JToggleButton seatL6;
    private javax.swing.JToggleButton seatL7;
    private javax.swing.JToggleButton seatL8;
    private javax.swing.JToggleButton seatL9;
    private javax.swing.JPanel seatLegendPanel;
    private javax.swing.JPanel seatToggleButtonsPanel;
    private javax.swing.JPanel seatsDiagramPanel;
    private javax.swing.JLabel seatsSelectedLabelTS;
    private javax.swing.JTextField securityCodeTextField;
    private javax.swing.JPanel selectSeatsPanel;
    private javax.swing.JPanel selectedMoviePanel;
    private javax.swing.JLabel seniorCalculationLabel;
    private javax.swing.JLabel seniorLabelTS;
    private javax.swing.JLabel seniorPriceLabel;
    private javax.swing.JPanel seniorSpinnerPanel;
    private javax.swing.JLabel seniorTicketLabel;
    private javax.swing.JPanel seniorTicketsCalculationPanel;
    private javax.swing.JSpinner seniorTicketsSpinner;
    private javax.swing.JPanel seniorTicketsTitlePanel;
    private javax.swing.JLabel seniorTotalTS;
    private javax.swing.JTextField seniorTotalTextField;
    private javax.swing.JButton signInButton;
    private javax.swing.JLabel spaceHLabel;
    private javax.swing.JPanel spaceHTitlePanel;
    private javax.swing.JButton spacerButtonTS1;
    private javax.swing.JButton spacerButtonTS2;
    private javax.swing.JPanel spacerEastPanel;
    private javax.swing.JPanel spacerSouthPanel;
    private javax.swing.JToggleButton spacerTB1;
    private javax.swing.JToggleButton spacerTB10;
    private javax.swing.JToggleButton spacerTB11;
    private javax.swing.JToggleButton spacerTB12;
    private javax.swing.JToggleButton spacerTB13;
    private javax.swing.JToggleButton spacerTB14;
    private javax.swing.JToggleButton spacerTB15;
    private javax.swing.JToggleButton spacerTB16;
    private javax.swing.JToggleButton spacerTB17;
    private javax.swing.JToggleButton spacerTB18;
    private javax.swing.JToggleButton spacerTB19;
    private javax.swing.JToggleButton spacerTB2;
    private javax.swing.JToggleButton spacerTB20;
    private javax.swing.JToggleButton spacerTB21;
    private javax.swing.JToggleButton spacerTB22;
    private javax.swing.JToggleButton spacerTB23;
    private javax.swing.JToggleButton spacerTB24;
    private javax.swing.JToggleButton spacerTB25;
    private javax.swing.JToggleButton spacerTB26;
    private javax.swing.JToggleButton spacerTB27;
    private javax.swing.JToggleButton spacerTB28;
    private javax.swing.JToggleButton spacerTB29;
    private javax.swing.JToggleButton spacerTB3;
    private javax.swing.JToggleButton spacerTB30;
    private javax.swing.JToggleButton spacerTB31;
    private javax.swing.JToggleButton spacerTB32;
    private javax.swing.JToggleButton spacerTB33;
    private javax.swing.JToggleButton spacerTB34;
    private javax.swing.JToggleButton spacerTB35;
    private javax.swing.JToggleButton spacerTB36;
    private javax.swing.JToggleButton spacerTB37;
    private javax.swing.JToggleButton spacerTB38;
    private javax.swing.JToggleButton spacerTB39;
    private javax.swing.JToggleButton spacerTB4;
    private javax.swing.JToggleButton spacerTB40;
    private javax.swing.JToggleButton spacerTB41;
    private javax.swing.JToggleButton spacerTB42;
    private javax.swing.JToggleButton spacerTB43;
    private javax.swing.JToggleButton spacerTB44;
    private javax.swing.JToggleButton spacerTB45;
    private javax.swing.JToggleButton spacerTB46;
    private javax.swing.JToggleButton spacerTB47;
    private javax.swing.JToggleButton spacerTB48;
    private javax.swing.JToggleButton spacerTB49;
    private javax.swing.JToggleButton spacerTB5;
    private javax.swing.JToggleButton spacerTB6;
    private javax.swing.JToggleButton spacerTB7;
    private javax.swing.JToggleButton spacerTB8;
    private javax.swing.JToggleButton spacerTB9;
    private javax.swing.JToggleButton spacerToggleButtonTS1;
    private javax.swing.JToggleButton spacerToggleButtonTS2;
    private javax.swing.JPanel spacerWestPanel;
    private javax.swing.JComboBox statesComboBox;
    private javax.swing.JTextField streetAddressTextField;
    private javax.swing.JButton submitRegistrationButton;
    private javax.swing.JToggleButton threeDJToggleButton;
    private javax.swing.JPanel threeTicketOptionsPanel;
    private javax.swing.JPanel topPanel;
    private javax.swing.JPanel totalCostPanel;
    private javax.swing.JPanel transactionSummaryPanel;
    private javax.swing.JPanel twoD3DandTitlesPanel;
    private javax.swing.JToggleButton twoDJToggleButton;
    private javax.swing.JPanel twoDor3DPanel;
    private javax.swing.JLabel unavailableButtonLabel;
    private javax.swing.JPanel unavailablePanel;
    private javax.swing.JPanel unavailableRightPanel;
    private javax.swing.JToggleButton unavailableToggleButtonTS;
    private javax.swing.JButton upButton;
    private javax.swing.JPanel userInfoPanel;
    private javax.swing.JTextField zipCodeTextField;
    // End of variables declaration//GEN-END:variables

    private int[][] repositionArray(int [][]toRepositionArray){
        int[][] toModify= toRepositionArray;
        int[][] modified= new int[12][24];
        for(int columns = 0;columns<12;columns++){
            for(int rows =0; rows<24;rows++){
                modified[columns][rows]=toModify[rows][columns];
            }
        }
        return modified;
    }
    private int[][] reverseRepositionArray(int [][]toRepositionArray){
        int[][] toModify= toRepositionArray;
        int[][] modified= new int[24][12];
        for(int rows =0; rows<12;rows++){
            for(int columns = 0;columns<24;columns++){
                modified[columns][rows]=toModify[rows][columns];
            }
        }
        return modified;
    }
    
    private void readSeatsFromDB() {
        try {
            
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database1.sqlite");
            
            String sql = " select * from auditorium15seats";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            int [][] readFromDB = new int[24][12];
            for(int r=0; r<24; r++){
                rs.next();
                for (int c=0;c<12;c++){
                    if(c ==0){//if column is A
                        readFromDB[r][c]=(int)rs.getObject("A");
                    }
                    else if(c ==1){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("B");
                    }
                    else if(c ==2){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("C");
                    }
                    else if(c ==3){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("D");
                    }
                    else if(c ==4){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("E");
                    }
                    else if(c ==5){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("F");
                    }
                    else if(c ==6){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("G");
                    }
                    else if(c ==7){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("H");
                    }
                    else if(c ==8){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("I");
                    }
                    else if(c ==9){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("J");
                    }
                    else if(c ==10){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("K");
                    }
                    else if(c ==11){//if column is B
                        readFromDB[r][c]= (int)rs.getObject("L");
                    }
                }
            }
            rs.close();
            pst.close();
            
            
            int [][] readyArray = repositionArray(readFromDB);
            
            this.arrayOfSeats = readyArray;
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
