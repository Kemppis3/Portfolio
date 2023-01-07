from tkinter import *

root = Tk()
root.title("Calculator")
#Icon needs to be in .ico format
#Iconbitmap takes the path to the icon as the argument. You might need to use raw string format
root.iconbitmap(r'calculator.ico')

#Building a calculator with graphical user interface using tkinter

field = Entry(root, width=20, borderwidth=5)
field.grid(row=0, column=0, columnspan=10, padx=10, pady=10)

afterEqualCheck = False
operation_mode = ""

#Defining button action functions
def buttoncommand(button):
    global afterEqualCheck
    if(afterEqualCheck == True):
        field.delete(0, END)
        afterEqualCheck = False
    currentNumber = field.get()
    field.delete(0, END)
    field.insert(0, currentNumber + button)

def clear_input_field():
    field.delete(0, END)

def add():
    addNumber = int(field.get())
    global first_num
    global operation_mode
    operation_mode = "addition"
    first_num = addNumber
    field.delete(0, END)

def subtract():
    subNumber = int(field.get())
    global first_num
    global operation_mode
    operation_mode = "subtraction"
    first_num = subNumber
    field.delete(0, END)
    
def multiply():
    multiNumber = int(field.get())
    global first_num
    global operation_mode
    first_num = multiNumber
    operation_mode = "multiplication"
    field.delete(0, END)

def divide():
    divNumber = int(field.get())
    global first_num
    global operation_mode
    first_num = divNumber
    operation_mode = "division"
    field.delete(0, END)    

def equals():
    sec_num = int(field.get())
    field.delete(0, END)
    global afterEqualCheck
    afterEqualCheck = True
    if operation_mode == "addition":
        field.insert(0, first_num + sec_num)
    
    if operation_mode == "subtraction":
        field.insert(0, first_num - sec_num)
    
    if operation_mode == "multiplication":
        field.insert(0, first_num * sec_num)
    
    if operation_mode == "division":
        field.insert(0, round((first_num / sec_num), 5))

#Creating buttons and defining their functionality
button_7 = Button(root, text="7", padx= 20, pady=20, command=lambda: buttoncommand("7"))
button_8 = Button(root, text="8", padx= 20, pady=20, command=lambda: buttoncommand("8"))
button_9 = Button(root, text="9", padx= 20, pady=20, command=lambda: buttoncommand("9"))
button_clear = Button(root, text="C", padx= 20, pady=20, command=clear_input_field)

button_4 = Button(root, text="4", padx= 20, pady=20, command=lambda: buttoncommand("4"))
button_5 = Button(root, text="5", padx= 20, pady=20, command=lambda: buttoncommand("5"))
button_6 = Button(root, text="6", padx= 20, pady=20, command=lambda: buttoncommand("6"))
button_multi = Button(root, text = "X", padx=20, pady=20, command=multiply)

button_1 = Button(root, text="1", padx= 20, pady=20, command=lambda: buttoncommand("1"))
button_2 = Button(root, text="2", padx= 20, pady=20, command=lambda: buttoncommand("2"))
button_3 = Button(root, text="3", padx= 21, pady=20, command=lambda: buttoncommand("3"))
button_divide = Button(root, text="/", padx=21, pady=20, command=divide)

button_subtract = Button(root, text="-", padx = 20, pady=20, command=subtract)
button_0 = Button(root, text="0", padx= 20, pady=20, command=lambda: buttoncommand("0"))
button_plus = Button(root, text="+", padx= 19.5, pady=20, command=add)
button_equals = Button(root, text="=", padx= 20, pady=20, command=equals)

#Bringing buttons on the screen

button_subtract.grid(row=4, column=0)
button_0.grid(row=4, column=1)
button_plus.grid(row=4, column=2)
button_equals.grid(row=4, column=3)


button_1.grid(row=3, column=0)
button_2.grid(row=3, column=1)
button_3.grid(row=3, column=2)
button_divide.grid(row=3, column=3)

button_4.grid(row=2, column=0)
button_5.grid(row=2, column=1)
button_6.grid(row=2, column=2)
button_multi.grid(row=2, column=3)

button_7.grid(row=1, column=0)
button_8.grid(row=1, column=1)
button_9.grid(row=1, column=2)
button_clear.grid(row=1, column=3)

root.mainloop()