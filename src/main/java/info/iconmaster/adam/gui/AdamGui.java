package info.iconmaster.adam.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import info.iconmaster.adam.game.AdamGame;

public class AdamGui extends JFrame {
	private static final long serialVersionUID = -8286165289807880809L;
	
	public static class RightClickHandler implements MouseListener {
		JPopupMenu menu;
		
		public RightClickHandler(JPopupMenu menu) {
			this.menu = menu;
		}
		
		@Override public void mouseExited(MouseEvent e) {}
		@Override public void mouseEntered(MouseEvent e) {}
		@Override public void mouseClicked(MouseEvent e) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
	
	public AdamGame game = null;
	
	public AdamGui() {
		// view - main GUI
		
		setSize(1024, 768);
		setTitle("ADAM");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// ignore
		}
		
		JPanel topBar = new JPanel();
		topBar.setLayout(new BoxLayout(topBar, BoxLayout.Y_AXIS));
		
		JPanel outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(BorderFactory.createEtchedBorder());
		JTextPane output = new JTextPane();
		output.setEditable(false);
		output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		output.setText("Welcome to ADAM!\nGo to 'File -> New Game' to start a new game.\n");
		JScrollPane outputScroll = new JScrollPane(output);
		outputPanel.add(outputScroll);
		topBar.add(outputPanel);
		
		JTextField input = new JTextField();
		input.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		input.setEnabled(false);
		JPanel inputPanel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = -3145773988628965624L;
			@Override
			public Dimension getMaximumSize() {
				return new Dimension(Integer.MAX_VALUE, input.getPreferredSize().height);
			}
		};
		
		JLabel inputLabel = new JLabel("  >  ");
        inputPanel.add(inputLabel, BorderLayout.LINE_START);
        inputPanel.add(input, BorderLayout.CENTER);
        topBar.add(inputPanel);
		
		JPanel bottomBar = new JPanel();
		bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.X_AXIS));
		
		JPanel charPanel = new JPanel(new BorderLayout());
		charPanel.setBorder(BorderFactory.createEtchedBorder());
		bottomBar.add(charPanel);
		
		WorldPane worldPanel = new WorldPane(this);
		worldPanel.setLayout(new BorderLayout());
		worldPanel.setBorder(BorderFactory.createEtchedBorder());
		bottomBar.add(worldPanel);
		
		JPanel inspectPanel = new JPanel(new BorderLayout());
		inspectPanel.setBorder(BorderFactory.createEtchedBorder());
		bottomBar.add(inspectPanel);
		
		add(topBar);
		add(bottomBar);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		// view- menu bar
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuTabFile = new JMenu("File");
		menuBar.add(menuTabFile);
		
		JMenuItem menuFileNewGame = new JMenuItem("New Game");
		menuTabFile.add(menuFileNewGame);
		
		// view - popup menus
		
		JPopupMenu outputMenu = new JPopupMenu();
		
		JMenuItem outputMenuClear = new JMenuItem("Clear log");
		outputMenu.add(outputMenuClear);
		
		// controller
		
		input.addActionListener((ev)->{
			if (input.getText().isEmpty()) return;
			
			String result = game.doAction(input.getText());
			output.setText(output.getText()+">>> "+input.getText()+"\n"+result+"\n");
			input.setText("");
			
			repaint();
		});
		
		input.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					input.setText("up");
				}
				
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					input.setText("down");
				}
			}
		});
		
		output.addMouseListener(new RightClickHandler(outputMenu));
		
		outputMenuClear.addActionListener((ev)->{
			output.setText("");
		});
		
		menuFileNewGame.addActionListener((ev)->{
			game = new AdamGame();
			
			input.setEnabled(true);
			output.setText(output.getText()+"A new world has begun!\n");
			
			repaint();
		});
	}
}
