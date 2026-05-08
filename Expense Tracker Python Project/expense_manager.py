import json
import os

FILE_NAME = "data.json"

def load_data():
        with open(FILE_NAME, "r") as f:
            return json.load(f)

def save_data(data):
    with open(FILE_NAME, "w") as f:
        json.dump(data, f, indent=4)

def add_expense(date, category, amount, description):
    data = load_data()
    data.append({
        "date": date,
        "category": category,
        "amount": amount,
        "description": description
    })
    save_data(data)

def get_all_expenses():
    return load_data()

def delete_expense(index):
    data = load_data()
    if 0 <= index < len(data):
        removed = data.pop(index)
        save_data(data)
        return removed
    return None

def edit_expense(index, updated_fields):
    data = load_data()
    if 0 <= index < len(data):
        data[index].update(updated_fields)
        save_data(data)
        return True
    return False
