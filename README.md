<img src="https://imgur.com/a/Cs4maqS" alt="SUDOKUH Logo" width="600"/>

# SUDOKUH
[![forthebadge made-with-java](https://forthebadge.com/images/badges/made-with-java.svg)](https://java.com/)

SUDOKUH is a simple sudoku game for various sizes (2x2, 3x3, 4x4) and a sudoku solver for normal sudoku, Str8ts and Killer in various sizes (2x2, 3x3, 4x4).

SUDOKUH is created by [Luca Kellermann](https://github.com/Lukellmann), [Fabian Heinl](https://github.com/WHYZNSoftware) and [Philipp Kremling](https://github.com/KremlingP).

## Getting started
To start only run the SUDOKUH.jar, the start menu will automatically open. Here you can choose between the different categories: the sudoku player, the sudoku solver for a normal sudoku, the sudoku solver for Str8ts and the sudoku solver for Killer. With clicking on one of the categories you'll get a menu in which you can choose between the three sizes (2x2, 3x3, 4x4). Clicking on "Start" will open a new menu in which you have the game overlay. In the start menu there also is a settings button. Clicking on this you'll get a new menu and you can choose the theme (standard or dark mode), Auto Step (this means wether the next cell should automatically by selected when typing in a number), highlighting (this means wether possible conflicting cells for the actual chosen cell should be highlighted) and the tip limit (how much tips you want to have in the play mode). Clicking on the back button will bring you back to the home menu and the settings will automatically be saved.

## The ingame menu
After selecting a category you will get to the ingame menu which in general has three elements: In the center you've got the sudoku field. When clicking on a cell you can choose this cell for input. If the highlighting option is activated you can also see all possible conflicting cells highlighted. You can also navigate in the sudoku menu using the arrow keys on your keyboard.

On the right edge there is the buttons menu: Here you have got all the buttons that are available for you: For every category that would be the numbers and a delete button. Each category has also got a few extra buttons (see below). Most of the buttons also can be used by typing an equivalent key on the keyboard (numbers for the numbers-buttons, back for the delete-button). If auto-step is enabled the next cell will automatically be selected after typing in a value.

On the top there is the information panel: in here you can see relevant information such as the playing time or wether the solution is correct or not (depends on the category). 

On the right top corner there also is the home-button. Clicking on this button will bring you back to the home menu. All actions taken in the games will be deleted.

## Categories
### Play Sudoku
Caution: Choosing the 4x4-Sudoku for playing will take really long to generate! Please be aware of that.

When playing sudoku the play time will be displayed in the information panel. As soon as the sudoku is solved completely the timer will stop and the whole playing time is displayed. Clicking on the main menu button pauses the timer.

For playing there are a few extra buttons to use: At first the tip button: Clicking on it will show the correct number for the selected cell. The cell is now seen as predefined and can not be changed anymore. How many tips you have depends on the settings in the main menu (standard: 3). As soon as you run out of tips the button changes to red and has no longer a function. (Keyboard equivalent: T)

Secondly there is the validate button: Clicking on it will force the program to check your solution: Wether it is correct or not. The result will be shown for five seconds in the information panel, afterwards the playing time is displayed again. The validation process gets automatically initiated when typing in a number, so normally you needn't click that button. (Keyboard equivalent: E)

Lastly there is the note button: Clicking on it will turn it green, symbolizing the note mode is active: When now typing in a number, it will be displayed small in the cell. Here you can type in any of the given numbers as a note for you: It won't affect the result. If you want to delete one set note, you can insert the same number: It get's removed from your notes. You can delete all the notes with the delete button and overwrite them with an input by turning the note mode off (clicking again on the note button) and then typing in a number. (Keyboard equivalent: N)

### Solve Sudoku
In this category you can type in an unsolved sudoku and the algorithm will solve it for you. Therefore you've got a solve button alongside the normal numbers buttons and the delete button. Clicking on it will fill the sudoku with matching values. All inputs that were set by you will be displayed as predefined (but you can change them). When the sudoku is unsolveable a text will be displayed in the information panel. (Keyboard equivalent: ENTER)

### Solve Str8ts
Str8ts is a special variant of sudoku: Here you don't have subgrids and only one big field: So the only conditions are that the same number mustn't be in the same row or column. Besides that there can be blocked fields (commonly displayed as black) which can't contain numbers or the predefined numbers in there can not be changed. For this there is a color change button: clicking on it will change the color of the actual chosen cell. Clicking it again will change it back. (Keyboard equivalent: F)

For solving the sudoku there again is a solve button. Clicking on it will fill the sudoku with matching values. When the sudoku is unsolveable a text will be displayed in the information panel. (Keyboard equivalent: ENTER)

### Solve Killer
Coming soon (maybe)
