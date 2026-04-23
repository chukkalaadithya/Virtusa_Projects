from storage import load_data, save_data

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