'''
autotype.py

Given the name of a .txt file, performs keyboard inputs of each line of the file followed by an enter input.

This program was created to help macro large sets of inputs for Discord bot commands.
(e.g. After waking up, a large amount of bot commands would be off-cooldown, this can be used to enter all the commands quickly)
'''

# import libraries
import sys
import time
from pyautogui import typewrite

# delay timings: adjust as needed
startupTime = 1
delayTime = 0.75

def main():    
    # inform user of program arguments if none are provided
    if len(sys.argv) <= 1: 
        print("argument: name of .txt file")
        return

    # read filename input and lines of file with filename
    fileName = sys.argv[1]
    file1 = open(fileName + ".txt", 'r')
    lines = file1.readlines()
    typewrite("\n") # reset existing text
    time.sleep(startupTime)
    for line in lines:
        # for each line, wait a delay time, input line, and enter line
        time.sleep(delayTime)
        typewrite(line)
        typewrite('\n')



if __name__ == "__main__":
    main()