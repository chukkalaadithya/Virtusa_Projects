from datetime import datetime
import json
import os

FILE_NAME = "data.json"

def load_data():
    if not os.path.exists(FILE_NAME):
        return []
    try:
        with open(FILE_NAME, "r") as f:
            return json.load(f)
    except:
        return []

def save_data(data):
    with open(FILE_NAME, "w") as f:
        json.dump(data, f, indent=4)
        
def check_exit(value):
    if value.lower() in ["exit", "cancel"]:
        print("Cancelled, returning to menu...")
        raise KeyboardInterrupt

def get_valid_date():
    while True:
        value = input("Enter date (YYYY-MM-DD) or 'exit': ").strip()
        check_exit(value)
        try:
            datetime.strptime(value, "%Y-%m-%d")
            return value
        except:
            today = datetime.today().strftime("%Y-%m-%d")
            print(f"Invalid date, using today: {today}")
            return today 

def get_valid_amount():
    while True:
        value = input("Enter amount or 'exit': ").strip()
        check_exit(value)
        try:
            num = float(value)
            if num <= 0:  
                print("Must be > 0")
                continue
            return num
        except:
            print("Invalid amount")

def handle_category(valid_categories):
    print("Categories:", ", ".join(valid_categories))
    value = input("Enter category or 'exit': ").strip()
    check_exit(value)  

    value = value.capitalize()

    if value == "Other":
        choice = input("Custom category? (y/n): ").strip().lower()
        check_exit(choice)
        if choice == "y":
            custom = input("Enter custom: ").strip()
            check_exit(custom)
            return custom.capitalize() if custom else "Other"
        return "Other"

    if value not in valid_categories:
        print("Unknown → set to Other")
        return "Other"

    return value  
