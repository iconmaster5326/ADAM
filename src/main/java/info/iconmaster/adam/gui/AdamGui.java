package info.iconmaster.adam.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AdamGui extends JFrame {
	private static final long serialVersionUID = -8286165289807880809L;
	
	public AdamGui() {
		// view
		
		setSize(1024, 768);
		setTitle("ADAM");
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
		JScrollPane outputScroll = new JScrollPane(output);
		outputPanel.add(outputScroll);
		topBar.add(outputPanel);
		
		JTextField input = new JTextField();
		input.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
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
		
		JPanel worldPanel = new JPanel(new BorderLayout());
		worldPanel.setBorder(BorderFactory.createEtchedBorder());
		bottomBar.add(worldPanel);
		
		JPanel inspectPanel = new JPanel(new BorderLayout());
		inspectPanel.setBorder(BorderFactory.createEtchedBorder());
		bottomBar.add(inspectPanel);
		
		add(topBar);
		add(bottomBar);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		// controller
		
		input.addActionListener((ev)->{
			output.setText(output.getText()+"Action!\n");
		});
	}
}
