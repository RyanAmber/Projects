import sys
from frontier import Node,StackFrontier, QueueFrontier
from Maze import Maze
maze1 = "\n\
#####B# \n\
##### # \n\
####  # \n\
#### ## \n\
     ## \n\
A###### \n\
"

maze2 = " \n\
###                 ######### \n\
#   ###################   # # \n\
# ####                # # # # \n\
# ################### # # # # \n\
#                     # # # # \n\
##################### # # # # \n\
#   ##                # # # # \n\
# # ## ### ## ######### # # # \n\
# #    #   ##B#         # # # \n\
# # ## ################ # # # \n\
### ##             #### # # # \n\
### ############## ## # # # # \n\
###             ##    # # # # \n\
###### ######## ####### # # # \n\
###### ####             #   # \n\
A      ###################### \n\
"

maze3 = "\n\
##    # \n\
## ## # \n\
#B #  # \n\
# ## ## \n\
     ## \n\
A###### \n\
"

#maze = maze1
maze = maze2
# maze = maze3

m = Maze(maze) # text from the top of the document
print("Maze:")
m.print()
print("Solving...")
m.solve()
print("States Explored:", m.num_explored)
print("Solution:")
m.print()
m.output_image("maze.png", show_explored=True)