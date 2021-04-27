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
	* In the new box, type: 
		> --module-path [Path to the 'Chocolate-Chiptunes\lib\javafx-sdk-11.0.2\lib' folder on your disk] --add-modules=javafx.controls,javafx.fxml
	* Click apply
2. You will then need to add the Maven files to your .m2 folder. To do so...
	* Find the repository.7z file in the root of the project directory and open it
	* Go to your C:/Users/[your name]/.m2 folder and paste in the repository folder from within the .7z file
	* Overwrite any conflicts
3. After you're done, you should be able to run the program by right-clicking on Main.java and then clicking "Run 'Main.main()'"
## Demo video

Video: https://www.youtube.com/watch?v=ZNwImpA0u_g

## Contributors

* Dawn Ramsey (ramseyd1@wit.edu), Team Lead
* Nicholas Scalese (scalesen@wit.edu), Developer
* Giovanni Rico (ricog@wit.edu), Developer
