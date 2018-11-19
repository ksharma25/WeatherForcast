package com.weather.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.weather.model.CoolestData;
import com.weather.service.WeatherService;

public class Startup extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel countryLabel;
	JLabel zipLabel;
	JLabel tempLabel;
	JLabel hourLabel;
	JLabel cityLabel;
	JButton actionButton;
	JTextField countryField;
	JTextField zipField;
	JTextField tempField;
	JTextField hourField;
	JTextField cityField;

	Startup() {
		countryLabel = new JLabel("COUNTRY CODE ");
		zipLabel = new JLabel("POSTAL CODE   ");
		tempLabel = new JLabel("COOLEST TEMP");
		hourLabel = new JLabel("COOLEST HOUR");
		cityLabel = new JLabel("CITY DETAILS     ");
		actionButton = new JButton("GO");

		countryField = new JTextField(10);
		zipField = new JTextField(10);
		tempField = new JTextField(10);
		hourField = new JTextField(10);
		cityField = new JTextField(10);

		add(countryLabel);
		add(countryField);
		add(zipLabel);
		add(zipField);
		add(tempLabel);
		add(tempField);
		add(hourLabel);
		add(hourField);
		add(cityLabel);
		add(cityField);
		add(actionButton);

		actionButton.addActionListener(this);

		setSize(250, 250);
		setLayout(new FlowLayout());
		setTitle("FROST WEATHER");
	}

	public void actionPerformed(ActionEvent ae) {

		String countryCode;
		Integer zipCode;
		Float coolestTemp;
		String coolestHour;

		if (ae.getSource() == actionButton) {

			try {
				countryCode = countryField.getText();
				zipCode = Integer.parseInt(zipField.getText());

				CoolestData coolestData = WeatherService.coolestTemp(countryCode, zipCode);
				coolestTemp = coolestData.getTemp();
				coolestHour = coolestData.getHour();

				if (coolestTemp != null && coolestHour != null) {
					tempField.setText(String.valueOf(coolestTemp));
					hourField.setText(String.valueOf(coolestHour));
					cityField.setText(coolestData.getCity());
				} else {
					tempField.setText("Not Found!");
					hourField.setText("Not Found!");
					cityField.setText("Not Found!");
				}
			} catch (Exception e) {
				tempField.setText("Not Found!");
				hourField.setText("Not Found!");
				cityField.setText("Not Found!");
			}

		}

	}

	public static void main(String args[]) {
		Startup a = new Startup();
		a.setVisible(true);
		a.setLocation(200, 200);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
