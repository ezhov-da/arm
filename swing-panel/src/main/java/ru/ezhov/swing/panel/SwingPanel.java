package ru.ezhov.swing.panel;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ru.ezhov.arm.plugin.gui.panel.PanelPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.logging.Logger;

public class SwingPanel implements PanelPlugin {
	private static final Logger LOG = Logger.getLogger(SwingPanel.class.getName());

	@Override
	public Pane getPane() {
		final SwingNode swingNode = new SwingNode();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JPanel panel = new JPanel(new BorderLayout());
				final JLabel label = new JLabel("ТЕСТ");
				JButton button = new JButton("РАНДОМ");
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								label.setText(String.valueOf(UUID.randomUUID()));
							}
						});
					}
				});
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setVerticalAlignment(SwingConstants.CENTER);
				panel.add(label, BorderLayout.CENTER);
				panel.add(button, BorderLayout.SOUTH);
				swingNode.setContent(panel);
			}
		});
		return new BorderPane(swingNode);
	}
}
