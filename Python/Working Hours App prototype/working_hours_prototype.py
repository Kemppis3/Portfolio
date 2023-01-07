
"""
@Author: Joni Kemppainen, University of Oulu

Little application i'm creating as a project for my father so he can easily write down his working hours in digital format and deliver them to his employer as Microsoft Excel files.
Application is still in development.
"""
from tkinter import *               
import mysql.connector
import csv


base = Tk()                         #Create a window for the application
base.title("Työtunnit-sovellus")    #Naming the application
base.iconbitmap('icon.ico')         #Adding an icon for the application
base.geometry("400x400")            #Setting the window size

my_database = mysql.connector.connect(  #Connect to the database. Use your own information
    host = "localhost",                 #Host is usually "localhost"
    user = "root",                      #Username can vary but usually is "root"
    passwd = "Noonoo234",               #Creating a strong password is recommended
    database = "workinghours",          #Set this to equal to the database you created
)

#Create a cursor
my_cursor = my_database.cursor()

#Create a database for the application (Do this only once!)

"""
cursor.execute("CREATE DATABASE workingHours")

You can loop through the databases to see if your database was created. Using MySQL workbench application for this is recommended
my_cursor.execute("SHOW DATABASES")
for db in my_cursor:
    print(db)
"""

"""
Create a table for the information we want to save
my_cursor.execute("CREATE TABLE tyotunnit (date VARCHAR(255), workhours INTEGER(10), drivingkilometers INTEGER(10), workplace VARCHAR(255), date_id INTEGER AUTO_INCREMENT PRIMARY KEY)")

Checking if the table was created
my_cursor.execute("SHOW TABLES")

"""

#Title label
title_lable = Label(base, text="Työtuntien kirjaus", font=("Helvetica", 15))
title_lable.grid(row=0, column=0, columnspan=1, padx=10, pady=10)

#Text labels
date_label = Label(base, text="Päivämäärä: ").grid(row=1, column=0)

hours_label = Label(base, text="Työtunnit: ").grid(row=2, column=0)

kilometers_label = Label(base, text="Kilometrit: ").grid(row=3, column=0)

station_label = Label(base, text="Työpaikka: ").grid(row=4, column=0)


#Creating input fields
date_label_field = Entry(base)
date_label_field.grid(row=1, column=1)
hours_label_field = Entry(base)
hours_label_field.grid(row=2, column=1)
kilometers_label_field = Entry(base)
kilometers_label_field.grid(row=3, column=1)
station_label_field = Entry(base)
station_label_field.grid(row=4, column=1)


#Defining button functions
def add_workhours():
    adding_format = "INSERT INTO tyotunnit (date, workhours, drivingkilometers, workplace) VALUES (%s, %s, %s, %s)"
    given_values = (date_label_field.get(), hours_label_field.get(), kilometers_label_field.get(), station_label_field.get())
    my_cursor.execute(adding_format, given_values)
    my_database.commit()
    clear_input_fields()

def clear_input_fields():
    date_label_field.delete(0, END)
    hours_label_field.delete(0, END)
    kilometers_label_field.delete(0, END)
    station_label_field.delete(0, END)

def csv_to_excel(workdays):
    with open('tyotunnit.csv', 'a') as workfile:
        writing = csv.writer(workfile, dialect='excel')
        for workday in workdays:
            writing.writerow(workday)


#Creating another window for showcasing the data in the datatable
def show_all_workdays():
    workday_window = Tk()
    workday_window.title("Työpäivät")
    workday_window.iconbitmap('icon.ico')
    workday_window.geometry("800x600")
    
    workday_data_label = Label(workday_window, text="Päivämäärä")
    workday_data_label.grid(row=0, column=0)
    workday_hours_label = Label(workday_window, text="Työtunnit")
    workday_hours_label.grid(row=0, column=1)
    workday_kilometers_label = Label(workday_window, text="Kilometrit")
    workday_kilometers_label.grid(row=0, column=2)
    workday_station_label = Label(workday_window, text="Työpiste")
    workday_station_label.grid(row=0, column=3)
    
    my_cursor.execute("SELECT date, workhours, drivingkilometers, workplace FROM tyotunnit")
    days = my_cursor.fetchall()
    for data_row, day in enumerate(days):
        data_coloum = 0
        for daydata in day:
            day_label = Label(workday_window, text=daydata)
            day_label.grid(row=data_row + 1, column=data_coloum)
            data_coloum += 1

    #Creating a button for creating csv-files
    export_button = Button(workday_window, text="Vie Exceliin", command=lambda: csv_to_excel(days))
    export_button.grid(row=data_row + 2, column=0, padx=10)


#Adding buttons to base window
add_information_to_database = Button(base, text="Lisää työpäivä", command=add_workhours).grid(row=5, column=0, padx=15, pady=20)

list_all_workdays = Button(base, text="Näytä kaikki työpäivät", command=show_all_workdays).grid(row=5, column=1)

base.mainloop()