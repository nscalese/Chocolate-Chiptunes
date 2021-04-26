# Chocolate-Chiptunes

## Introduction

An SNES/NES style music creation tool.

## Features
1. Create instruments
	* Create up to 4 instruments
	* Choose waveform for instrument
	* Edit the ADSR values for a waveform
2. Create melodies
	* Place notes in piano roll
	* Control BPM and master volume
	* Play melody
3. Save projects
	* Save project file
	* Load project file
	* Export file as .wav
4. Manage user account
	* Create account
	* Save projects to account

## Getting Started
### Installation and Setup
1. Clone this repository
		> git clone git://github.com/nscalese/Chocolate-Chiptunes.git
2. We created this program in IntelliJ IDEA and recommend running it there to prevent issues
	
### Run
1. Upon running the Main class from within IntelliJ, you will likely be told that there are JavaFX components missing. To fix this:
	* Go to Run -> Edit Configurations... -> Application -> Main -> Modify Options -> Add VM options
	* In the new box, type --module-path [Path to the 'Chocolate-Chiptunes\lib\javafx-sdk-11.0.2\lib' folder on your disk] --add-modules=javafx.controls,javafx.fxml
	* Click apply
2. You will then need to add the Maven files to the project structure. To do so...
	* Go to File -> Project Structure -> Modules -> Dependencies -> Click on the + at the bottom of the list -> JARs or Dependencies -> Then add the repository.zip file at the root of the project folder
	* Click apply
3. After you're done, you should be able to run the program by right-clicking on Main.java and then clicking "Run 'Main.main()'"
## Demo video

Upload your demo video to youtube and put a link here. Basically, the video content is very much like the quick live demo of your product with the followings:
1. Introduction
2. How to run the app
3. Quick walkthrough of all the functions and their sub functions of your app one by one

Please make it short and interesting!

Sample: https://www.youtube.com/watch?v=Pr-JMqTkdEM

How to record your screen: https://www.techradar.com/how-to/record-your-screen

## Contributors

* Dawn Ramsey (ramseyd1@wit.edu), Team Lead
* Nicholas Scalese (), role
* ...