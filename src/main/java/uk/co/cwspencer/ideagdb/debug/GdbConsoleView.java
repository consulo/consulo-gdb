package uk.co.cwspencer.ideagdb.debug;

import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.logging.Logger;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import uk.co.cwspencer.gdb.Gdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Console tab for GDB input and output.
 */
public class GdbConsoleView
{
	private static final Logger m_log = Logger.getInstance(GdbConsoleView.class);

	private JPanel m_contentPanel;
	private JTextField m_prompt;

	private Gdb m_gdb;

	// The actual console
	private ConsoleView m_console;

	// The last command that was sent
	private String m_lastCommand;

	public GdbConsoleView(Gdb gdb, @NotNull Project project)
	{
		m_contentPanel = new JPanel(new BorderLayout());
		m_prompt = new JTextField();

		m_gdb = gdb;
		TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
		builder.setUsePredefinedMessageFilter(true);
		builder.setViewer(true);
		m_console = builder.getConsole();
		m_contentPanel.add(m_console.getComponent(), BorderLayout.CENTER);
		m_prompt.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				String command = event.getActionCommand();
				if(command.isEmpty() && m_lastCommand != null)
				{
					// Resend the last command
					m_gdb.sendCommand(m_lastCommand);
				}
				else if(!command.isEmpty())
				{
					// Send the command to GDB
					m_lastCommand = command;
					m_prompt.setText("");
					m_gdb.sendCommand(command);
				}
			}
		});
		m_contentPanel.add(m_prompt, BorderLayout.SOUTH);
	}

	public ConsoleView getConsole()
	{
		return m_console;
	}

	public JComponent getComponent()
	{
		return m_contentPanel;
	}

	public JComponent getPreferredFocusableComponent()
	{
		return m_prompt;
	}
}
