# My Personal Project: Auto-Scheduler

## Description:

Every day I write down a study schedule on a piece of paper.
 Starting with a list of tasks that I need to complete and a blank table,
  I arduously fill in the table to create a detailed schedule for my day.
  I would like to create an application allowing users to automate
   as much of the schedule making process as possible, in order to save them time and energy. 
   My goal is to make organisation and time management easy for anyone to accomplish.
   I am always trying improve my productivity, and time management, so
     I am very interested in creating a program to assist with this pursuit.
   
   The application will generate a schedule for the user's day, automatically.
   My goal is to combine a to-do list with a schedule.
   The user will first enter the starting time and duration of any scheduled events they have. 
   For example, a dentist appointment 17:00 or a lecture at 09:00. Then, the user will be prompted to
   enter all the tasks they may have on their real life to-do list, along with their estimated durations.
   Automatically the app will schedule these tasks, while working around the previously entered events. The app
   will then print out a schedule of the day for the user, and provide a categorical breakdown of the day's activities.


## User Stories - Part 1:
- As a user, I want to be able to enter a start and end time for my day to set a time range
for my schedule
- As a user, I want to be able to enter time specific appointments and scheduled obligations
- As a user, I want to be warned by program if appointments interfere with each other and have the option to overwrite or re-schedule
- As a user, I want to be able to enter tasks into a To-do list
- As a user, I want to be able to edit my To-do list
- As a user, I want to be warned when I do not have enough time in the day to complete my todo list
and given the option to edit my todo list
- As a user, I want my to-do list tasks to be automatically added into my schedule
- As a user, I want to be able to see a breakdown of the time each type of activity will take

##User Stories - Part 2:
- As a user, I want to be able to save my Schedule to a file
- As a user, I want to be able to load my Schedule from a file
- As a user, I want to be able to save and load multiple schedules under different names that I enter

##User Stories - Part 3:
The following have been implemented in the GUI.
- As a user, I want to be able to enter a start and end time for my day to set a time range
for my schedule
- As a user, I want to be able to enter time specific appointments and scheduled obligations
- As a user, I want to be warned by program if appointments interfere with each other and have the option to overwrite or re-schedule
- As a user, I want to be able to enter tasks into a To-do list
- As a user, I want to be able to edit my To-do list
- As a user, I want my to-do list tasks to be automatically added into my schedule
- As a user, I want to be able to see a breakdown of the time each type of activity will take
- As a user, I want to be able to save my Schedule to a file
- As a user, I want to be able to load my Schedule from a file
- As a user, I want to be able to save and load multiple schedules under different names that I enter

##Part 4 - Task 2:
- I have chosen the task of implementing a type hierarchy in my code. There are multiple
hierarchies in my project however, the one that meets the criteria is hierarchy between the
TimeableList, ToDoList, and Schedule classes. ToDoList and schedule both override the addTask method of the 
TimeableList class.


##Part 4 - Task 3:
- Refactor category list from schedule class into its own class
- Refactor todolist so that it has a bi-directional association with schedule
- Refactor GUI so a single class that manages all the windows and has only one Schedule and ToDoList object.
- Refactor NotEnoughFreeTimeDisplay and EnoughFreeTimeDisplay into the ScheduleEditorWindow Class
