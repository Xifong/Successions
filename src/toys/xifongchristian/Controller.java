package toys.xifongchristian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Controller extends JFrame implements ActionListener {
    private static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 600);
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(400, 400);

    private JMenuBar mb_menu;
    private JMenu m_file, m_game, m_help;
    private JMenuItem mi_file_options, mi_file_exit;
    private JMenuItem mi_game_play, mi_game_stop, mi_game_reset;
    private JMenuItem mi_help_about;

    private int fps = 25;
    private int cycles = 10;
    private int rootCharm = 100; //Actually make use of this - how does the Controller interatc with the model?

    private SimInstance simulationManager;
    private IModel model;
    private FamilyTreeDisplay display;

    private boolean isRunning = false;
    private boolean hasRun = false;

     Controller() {
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setTitle("Family Tree");
         this.setSize(Controller.DEFAULT_WINDOW_SIZE);
         this.setMinimumSize(MINIMUM_WINDOW_SIZE);
         this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth())/2,
                 (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight())/2);
         mb_menu = new JMenuBar();
         setJMenuBar(mb_menu);
         m_file = new JMenu("File");
         mb_menu.add(m_file);
         m_game = new JMenu("Game");
         mb_menu.add(m_game);
         m_help = new JMenu("Help");
         mb_menu.add(m_help);
         mi_file_options = new JMenuItem("Options");
         mi_file_options.addActionListener(this);
         mi_file_exit = new JMenuItem("Exit");
         mi_file_exit.addActionListener(this);
         m_file.add(mi_file_options);
         m_file.add(new JSeparator());
         m_file.add(mi_file_exit);
         mi_game_play = new JMenuItem("Play");
         mi_game_play.addActionListener(this);
         mi_game_stop = new JMenuItem("Stop");
         mi_game_stop.setEnabled(false);
         mi_game_stop.addActionListener(this);
         mi_game_reset = new JMenuItem("Reset");
         mi_game_reset.addActionListener(this);
         m_game.add(new JSeparator());
         m_game.add(mi_game_play);
         m_game.add(mi_game_stop);
         m_game.add(mi_game_reset);
         mi_help_about = new JMenuItem("About");
         mi_help_about.addActionListener(this);
         m_help.add(mi_help_about);
         display = new FamilyTreeDisplay();
         add(display);

         initialiseModel();
    }

    private Options bundleOptions(){
        return new Options(cycles);
    }

    private void stopSim(){
        int yearOffset = simulationManager.interruptSim();
        model.setYearOffset(yearOffset);
    }

    //When thread terminates by its self, setGameBeing Played(false)
    //Also, UI thread is being starved
    public void setGameBeingPlayed(boolean isBeingPlayed) {
        if (isBeingPlayed) {
            mi_game_play.setEnabled(false);
            mi_game_stop.setEnabled(true);
            simulationManager = new SimStarter(bundleOptions(), model).getSimInstance(this);
            isRunning = true;
            hasRun = true;
            simulationManager.begin();
        } else {
            mi_game_play.setEnabled(true);
            mi_game_stop.setEnabled(false);
            if(isRunning) {
                stopSim();
                isRunning = false;
            }
        }
    }

    private void initialiseModel(){
        setGameBeingPlayed(false);
        model = new DefaultModel(new FamilyModel(), 0);
        hasRun = false;
        display.repaint();
    }

    public void notifySimDetailsChanged(){
        display.repaint();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mi_file_exit)) {
            // Exit the game
            System.exit(0);
        } else if (ae.getSource().equals(mi_file_options)) {
            // Put up an options panel to change the number of moves per second
            final JFrame f_options = new JFrame();
            f_options.setTitle("Options");
            f_options.setSize(300, 60);
            f_options.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - f_options.getWidth()) / 2,
                    (Toolkit.getDefaultToolkit().getScreenSize().height - f_options.getHeight()) / 2);
            f_options.setResizable(false);
            JPanel p_options = new JPanel();
            p_options.setOpaque(false);
            f_options.add(p_options);

            //These will ofc need some validation
            p_options.add(new JLabel("Cycles"));
            final JTextField cyclesField = new JTextField(5);
            p_options.add(cyclesField);
            cyclesField.addActionListener(ael -> {
                cycles = Integer.parseInt(cyclesField.getText());
            });

            p_options.add(new JLabel("Root Charm"));
            final JTextField rootCharmField = new JTextField(3);
            p_options.add(rootCharmField);
            rootCharmField.addActionListener(ael -> {
                rootCharm = Integer.parseInt(rootCharmField.getText());
            });

            p_options.add(new JLabel("Number of frames per second:"));
            Integer[] secondOptions = {1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 40, 50, 60};
            final JComboBox cb_seconds = new JComboBox(secondOptions);
            p_options.add(cb_seconds);
            cb_seconds.setSelectedItem(fps);
            cb_seconds.addActionListener(ael -> {
                fps = (Integer) cb_seconds.getSelectedItem();
                f_options.dispose();
            });
            f_options.setVisible(true);
        } else if (ae.getSource().equals(mi_game_reset)) {
            initialiseModel();
        } else if (ae.getSource().equals(mi_game_play)) {
            setGameBeingPlayed(true);
        } else if (ae.getSource().equals(mi_game_stop)) {
            setGameBeingPlayed(false);
        } else if (ae.getSource().equals(mi_help_about)) {
            JOptionPane.showMessageDialog(null, "A message");
        }
    }


    private class FamilyTreeDisplay extends JPanel implements MouseListener {
        private int offset = 10;
        private JLabel yearDisplay;
        FamilyTreeDisplay(){
            //Add a side panel for sim info
            yearDisplay = new JLabel();
            this.add(yearDisplay);
        }

        private void setYearLabel(){
            if(hasRun) {
                int year = simulationManager.getCurrentYear();
                if (year != -1) {
                    //Rn this is doubling the year - oops, need to check running status
                    yearDisplay.setText("Year: " + (simulationManager.getCurrentYear() + model.getYearOffset()));
                    return;
                }
            }
            yearDisplay.setText("Year: N/A");
        }

        @Override
        public void paintComponent(Graphics g) {
            //Only called in response to events - cannot have animations encoded here
            super.paintComponent(g);
            offset = (offset - 10)%500;
            g.draw3DRect(100 - offset, 100, 10, 10, true);
            setYearLabel();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
