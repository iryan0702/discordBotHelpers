'''
botAlarm.py

– A simple program which alerts the user at specified time intervals with a sound effect and pop-up notification (MacOS). 
The user can add additional tasks by specifying their names and time intervals – whenever the main alert is triggered,
the alert will also display the names of the additional tasks if the tasks' time interval has passed.

This program was created to help people who play with discord bots keep track of commands to input over a period of time.
Discord game bots often have several commands which are tied to different cooldowns, which may be hard for a person to
keep track of while doing other things at the same time.

Example arguments:
> python botAlarm.py 90 10 tip 6 work 12
This timer will create an alert every 90+10 seconds.
Additionally, if 5 minutes have elapsed since the last alert for the task 'tip', the new alert will also mention 'tip'.
(Likewise, 10 minutes for the 'work' task)
'''

# import libraries
import random
import time
import os
import sys
from playsound import playsound
from pync import Notifier

def main():
    # inform user of program arguments if none are provided
    if len(sys.argv) <= 2: 
        print("argument: (wait time sec.) (rest time sec.) [((task name), (task time min.))...]")
        return

    # setup variables
    waitTime = int(sys.argv[1])
    restTime = int(sys.argv[2])

    nameList = []
    maxTimeList = []
    timeList = []
    
    # parse input tasks into lists of task names, max task time interval, and current task time
    for i in range(3, len(sys.argv), 2):
        nameList.append(sys.argv[i])
        maxTimeList.append(int(sys.argv[i+1])*60 + restTime)
        timeList.append(int(sys.argv[i+1])*60 + restTime)

    while True:
        # wait for the specified wait and rest time and play a sound effect after each period
        playsound('audio/startSound.wav')

        time.sleep(restTime)

        playsound('audio/endSound.wav')

        time.sleep(waitTime)

        # decrement the remaining time of all tasks by the main timer, if the timer completes,
        # append the task name to the output and reset the task's remaining time.
        doString = ""
        for i in range(len(timeList)):
            timeList[i] -= waitTime + restTime
            if timeList[i] <= 0:
                doString += nameList[i] + " "
                timeList[i] = maxTimeList[i]

        Notifier.notify('Do: ' + doString, title='Time for Bot!')

        
    
if __name__ == "__main__":
    main()