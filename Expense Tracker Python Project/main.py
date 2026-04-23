from expense_manager import *
from analytics import *
from config import VALID_CATEGORIES
from utils import *


def menu():
    print("\n====== Smart Expense Tracker ======")
    print("1. Add Transaction")
    print("2. View Transactions")
    print("3. Edit Transaction")
    print("4. Delete Transaction")
    print("5. Monthly Summary")
    print("6. View Insights (Graph)")
    print("7. Exit")


def display(data):
    if not data:
        print("\nNo transactions found.")
        return

    print("\n--- Transaction List ---")
    print("No | Date       | Category   | Amount   | Description")
    print("-" * 70)

    for i, e in enumerate(data, 1):
        print(f"{i:<3}| {e['date']:<10} | {e['category']:<10} | ₹{e['amount']:<8} | {e['description']}")


while True:
    menu()
    ch = input("\nSelect an option: ").strip()

    if ch == "1":
        try:
            print("\n--- Add Transaction ---")

            date = get_valid_date()
            category = handle_category(VALID_CATEGORIES)
            amount = get_valid_amount()
            desc = input("Enter description: ")

            confirm = input("Save this transaction? (y/n): ").lower()
            if confirm == "y":
                add_expense(date, category, amount, desc)
                print(" Transaction added successfully.")
            else:
                print(" Transaction cancelled.")

        except:
            print("↩ Operation cancelled.")

    elif ch == "2":
        display(get_all_expenses())

    elif ch == "3":
        data = get_all_expenses()
        display(data)

        try:
            idx = int(input("Enter transaction number to edit: ")) - 1

            new_amount = get_valid_amount()
            new_category = handle_category(VALID_CATEGORIES)

            confirm = input("Confirm update? (y/n): ").lower()
            if confirm == "y":
                if edit_expense(idx, {"amount": new_amount, "category": new_category}):
                    print(" Transaction updated.")
                else:
                    print(" Invalid selection.")
            else:
                print(" Update cancelled.")

        except:
            print("Error while updating.")

    elif ch == "4":
        data = get_all_expenses()
        display(data)

        try:
            idx = int(input("Enter transaction number to delete: ")) - 1

            confirm = input("Are you sure? (y/n): ").lower()
            if confirm == "y":
                removed = delete_expense(idx)
                print(f"Deleted: {removed}" if removed else " Invalid selection.")
            else:
                print("Deletion cancelled.")

        except:
            print("Error while deleting.")

    elif ch == "5":
        month = input("Enter month (YYYY-MM): ")
        total, avg = monthly_summary(get_all_expenses(), month)

        print(f"\nMonthly Summary ({month})")
        print(f"Total Expense: ₹{total}")
        print(f"Average per day: ₹{round(avg, 2)}")

    elif ch == "6":
        month = input("Enter month (YYYY-MM): ")
        summary = category_summary(get_all_expenses(), month)
        show_advanced_chart(summary, month)

    elif ch == "7":
        print("Exiting... Goodbye")
        break

    else:
        print("Invalid option. Please try again.")